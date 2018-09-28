package com.netpro.trinity.resource.admin.externalrule.lib;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.netpro.trinity.resource.admin.drivermanager.util.FileDetailUtil;
import com.netpro.trinity.resource.admin.externalrule.dto.Publication;
import com.netpro.trinity.resource.admin.externalrule.dto.PublishRule;
import com.netpro.trinity.resource.admin.externalrule.entity.DmExtRule;
import com.netpro.trinity.resource.admin.prop.dto.TrinitySysSetting;
import com.netpro.trinity.resource.admin.util.Constant;
import com.netpro.trinity.resource.admin.util.XMLUtil;

@Component
public class ExernalRuleUtil {
	@Autowired
	private TrinitySysSetting trinitySys;
		
	public List<String> getRulePKStringByAgentUID(String agentUid) throws IOException, Exception{
		List<String> ret = new ArrayList<String>();
		
		Document doc = XMLUtil.toDocument(readEextruleCfgFile());
		NodeList nl = doc.getElementsByTagName(Constant.TAG_AGENT);
		Node agentHostNode = getNodeByAgentUID(nl, agentUid);
		
		if (agentHostNode == null)
			return ret;
		
		NodeList ruleNodeList = agentHostNode.getChildNodes();
		for (int i = 0 ; i < ruleNodeList.getLength() ; i++){
			Node ruleNode = ruleNodeList.item(i);
			
			//只處理一般Node
			if(ruleNode.getNodeType() != 1)
				continue;
			
			String ruleName = FileDetailUtil.getNodeAttByName(ruleNode, Constant.TAG_ATTR_NAME);
			String jaruid = FileDetailUtil.getNodeAttByName(ruleNode, Constant.TAG_ATTR_JARUID);
			
			String temp[] = ruleName.split("\\.");
			
			ret.add(jaruid+":"+temp[1]);
		}
		
		return ret;
	}
	
	private String readEextruleCfgFile() throws IOException{
		StringBuffer sb = new StringBuffer();
		BufferedReader br = null;
		try {
			File xmlFile = new File(this.trinitySys.getDir().getExtlib(), Constant.EXT_RULE_CFG);
			if(xmlFile.exists()) {
				br = new BufferedReader(new FileReader(xmlFile));
				
				String temp = "";
				while ((temp = br.readLine()) != null)
					sb.append(temp);
				
				return sb.toString();
			}else {
				writeEextruleCfgFile("<root/>");
				return "<root/>";
			}
		} finally {
			if (br != null)
				br.close();
		}
	}
	
	public void writeEextruleCfgFile(String xmlData) throws IOException{
		File cfgFile = new File(this.trinitySys.getDir().getExtlib(), Constant.EXT_RULE_CFG);
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(cfgFile));
			bw.write(xmlData);
			bw.flush();
		} finally {
			try {
				if (bw != null)
					bw.close();
			} catch (IOException e) {}
		}
	}
	
	public String writeEextruleCfg(List<Publication> publications) throws Exception{
		Document doc = XMLUtil.newDocument();
		Element root = doc.createElement("root");
		doc.appendChild(root);
		
		for (Publication publication : publications){
			if(publication.getPublishRule().size() > 0) {	//size大於0表示此agent底下有rule的設定, 才可能需要建立agent節點
				Element agentNode = doc.createElement(Constant.TAG_AGENT);
				agentNode.setAttribute(Constant.TAG_ATTR_AGENTUID, publication.getAgentuid());
				
				for (PublishRule rule : publication.getPublishRule()){
					if(rule.getPublished()) {	//當為published rule時, 才需要把此rule加入agent節點中
						Element ruleNode = doc.createElement(Constant.TAG_RULE);
						ruleNode.setAttribute(Constant.TAG_ATTR_ACTIVE, rule.getActive());
						ruleNode.setAttribute(Constant.TAG_ATTR_NAME, rule.getPackagename() + "." +rule.getRulename());
						ruleNode.setAttribute(Constant.TAG_ATTR_CLASSPATH, rule.getFullclasspath());
						ruleNode.setAttribute(Constant.TAG_ATTR_FILENAME, rule.getFilename());
						ruleNode.setAttribute(Constant.TAG_ATTR_JARUID, rule.getExtjaruid());
						ruleNode.setAttribute(Constant.TAG_ATTR_MD5, rule.getMd5());
						
						agentNode.appendChild(ruleNode);
					}
				}
				
				if(agentNode.getChildNodes().getLength() > 0) {	//當此agent至少有一個published rule時, 則此agent節點才需要加進root節點
					root.appendChild(agentNode);
				}
			}
		}
		String new_cfg = XMLUtil.toXML(doc);
		return new_cfg;
	}
	
	public void writeImportJarFile(byte[] bytes, String tempRootPath, String fileName) throws IOException, Exception {
		File tempFolder = new File(tempRootPath);
		if (!tempFolder.exists())
			tempFolder.mkdirs();
		
		FileOutputStream fos = null;
		try {
			File tempFile = new File(tempRootPath , fileName);
			fos = new FileOutputStream(tempFile);
			fos.write(bytes);
			fos.flush();
		} finally {
			try {
				if (fos != null)
					fos.close();
			} catch (IOException e) {}
		}
	}
	
	public List<Map<String, String>> getInfoFromJarConfigFile(String tempRootPath, String fileName) throws FileNotFoundException, IOException, Exception{
		JarFile jarFile = null;
		ZipFile zipFile = null;
		List<Map<String, String>> retInfo = new LinkedList<Map<String, String>>();; 
		try {
			jarFile = new JarFile(new File(tempRootPath , fileName));
			zipFile = new ZipFile(new File(tempRootPath , fileName));
			Enumeration<? extends ZipEntry> zipEntrys = zipFile.entries();
			while (zipEntrys.hasMoreElements()) {
				ZipEntry zipEntry = zipEntrys.nextElement();

				if (zipEntry.getName().equals("config.xml")) {
					InputStream is = jarFile.getInputStream(zipEntry);
					Document doc = XMLUtil.toDocument(is);
					if (doc == null)
						throw new FileNotFoundException("config.xml file can not be found in import jar file!");
					
					Element root = doc.getDocumentElement();
					
					//取出attribute (name及desc)
					String packageName = root.getAttribute("name");
					if (packageName == null || packageName.trim().isEmpty())
						throw new FileNotFoundException("Package Name Attribute can not be found in import jar file!");
					String packageDesc = root.getAttribute("description") == null ? "" : root.getAttribute("description");
										
					//把取出的name及desc存入要回傳的List
					Map<String, String> packageInfo = new HashMap<String, String>();
					packageInfo.put("packageName", packageName);
					packageInfo.put("packageDesc", packageDesc);
					retInfo.add(packageInfo);
								
					//取出rule, 且存入要回傳的List
					NodeList nl = root.getChildNodes();
					for (int i = 0 ; i < nl.getLength() ; i++){
						Node ruleNode = nl.item(i);
						if (ruleNode.getNodeType() == Node.TEXT_NODE)
							continue;
						
						Map<String, String> atbMap = getAttributes(ruleNode.getAttributes());
						
						String fullClass = atbMap.get("fullclass");
						String ruleName = atbMap.get("rulename");
						String description = atbMap.get("description");
						
						if (null == fullClass || fullClass.trim().isEmpty())
							continue;
							
						if (null == ruleName || ruleName.trim().isEmpty())
							continue;
						
						if(null == description || description.trim().isEmpty())
							description = "";
						
						//把取出的rule資料存入Map
						Map<String, String> ruleInfo = new HashMap<String, String>();
						ruleInfo.put("ruleName", ruleName);
						ruleInfo.put("fullClass", fullClass);
						ruleInfo.put("description", description);
						retInfo.add(ruleInfo);
					}
					break;
				}
			}
			return retInfo;
		} finally {
			try {
				if (jarFile != null)
					jarFile.close();
				if (zipFile != null)
					zipFile.close();
			}catch(Exception e) {}
		}
	}
	
	public String updateCfgPackageName(List<String> jarUIDs, String pName) throws IOException, Exception{
		Document doc = XMLUtil.toDocument(readEextruleCfgFile());
		NodeList nl = doc.getElementsByTagName(Constant.TAG_RULE);
		for (int i = 0 ; i < nl.getLength() ; i++){
			Element ruleNode = (Element)nl.item(i);
			String jarUID = ruleNode.getAttribute(Constant.TAG_ATTR_JARUID);
			if (jarUIDs.contains(jarUID)){
				String name = ruleNode.getAttribute(Constant.TAG_ATTR_NAME);
				String[] temp = name.split("\\.");
				String newName = pName + "." + temp[1];
				ruleNode.setAttribute(Constant.TAG_ATTR_NAME, newName);
				
				String filename = ruleNode.getAttribute(Constant.TAG_ATTR_FILENAME);
				int index = filename.indexOf(Constant.SEPARATOR);
				ruleNode.setAttribute(Constant.TAG_ATTR_FILENAME, pName + filename.substring(index));
			}
		}
		return XMLUtil.toXML(doc);
	}
	
	public boolean haveRulePublishByPackage(List<String> jarUIDs) throws IOException, Exception{
		Document doc = XMLUtil.toDocument(readEextruleCfgFile());
		NodeList nl = doc.getElementsByTagName(Constant.TAG_RULE);
		for (int i = 0 ; i < nl.getLength() ; i++){
			Element ruleNode = (Element)nl.item(i);
			if (jarUIDs.contains(ruleNode.getAttribute(Constant.TAG_ATTR_JARUID)))
				return true;
		}
		return false;
	}
	
	public boolean haveRulePublishByJar(String jarUID) throws IOException, Exception{
		Document doc = XMLUtil.toDocument(readEextruleCfgFile());
		NodeList nl = doc.getElementsByTagName(Constant.TAG_RULE);
		for (int i = 0 ; i < nl.getLength() ; i++){
			Element ruleNode = (Element)nl.item(i);
			if (ruleNode.getAttribute(Constant.TAG_ATTR_JARUID).equals(jarUID))
				return true;
		}
		return false;
	}
	
	public boolean haveRulePublishByRule(String jarUID, String ruleName) throws IOException, Exception{
		Document doc = XMLUtil.toDocument(readEextruleCfgFile());
		NodeList nl = doc.getElementsByTagName(Constant.TAG_RULE);
		for (int i = 0 ; i < nl.getLength() ; i++){
			Element ruleNode = (Element)nl.item(i);
			if (ruleNode.getAttribute(Constant.TAG_ATTR_JARUID).equals(jarUID)){
				String fullname = ruleNode.getAttribute(Constant.TAG_ATTR_NAME);
				String[] temp = fullname.split("\\.");
				if (temp[1].equals(ruleName)){
					return true;						
				}
			}
		}
		return false;
	}
	
	public String updateRuleNameOrActiveInCfg(String jarUID, String oldName, String newName, String active) throws Exception{
		Document doc = XMLUtil.toDocument(readEextruleCfgFile());
		NodeList nl = doc.getElementsByTagName(Constant.TAG_RULE);
		for (int i = 0 ; i < nl.getLength() ; i++){
			Element ruleNode = (Element)nl.item(i);
			if (ruleNode.getAttribute(Constant.TAG_ATTR_JARUID).equals(jarUID)){
				String fullname = ruleNode.getAttribute(Constant.TAG_ATTR_NAME);
				String[] temp = fullname.split("\\.");
				if (temp[1].equals(oldName)){
					ruleNode.setAttribute(Constant.TAG_ATTR_NAME, temp[0] + "." + newName);
					ruleNode.setAttribute(Constant.TAG_ATTR_ACTIVE, active);
				}
			}
		}
		return XMLUtil.toXML(doc);
	}
	
	public String updateOverrideRuleActive(List<DmExtRule> ruleList, String md5) throws Exception{
		if (ruleList.size() == 0)
			return "";
		
		Document doc = XMLUtil.toDocument(readEextruleCfgFile());
		NodeList nl = doc.getElementsByTagName(Constant.TAG_RULE);
		for (int i = 0 ; i < nl.getLength() ; i++){
			Element ruleNode = (Element)nl.item(i);
			if (ruleNode.getAttribute(Constant.TAG_ATTR_JARUID).equals(ruleList.get(0).getExtjaruid())){
				for (DmExtRule der : ruleList){
					String ruleName = der.getRulename();
					if (ruleNode.getAttribute(Constant.TAG_ATTR_NAME).split("\\.")[1].equals(ruleName)){
						ruleNode.setAttribute(Constant.TAG_ATTR_ACTIVE, der.getActive());
						ruleNode.setAttribute(Constant.TAG_ATTR_MD5, md5);
						break;
					}
				}
			}
		}
		return XMLUtil.toXML(doc);
	}
	
	public List<String> getClsNames(File jarFile) throws ZipException, IOException, Exception{
		List<String> ret = new ArrayList<String>();
		ZipFile zipFile = null;
		URLConnection c = null;
		try {
			zipFile = new ZipFile(jarFile);
			String uriStr = new File(zipFile.getName()).toURI().toString();
			URL url = new URL("jar", "", -1, uriStr + "!/");

			Enumeration<? extends ZipEntry> zipEntrys = zipFile.entries();
			while (zipEntrys.hasMoreElements()) {
				c = url.openConnection();
				URLClassLoader ucl = new URLClassLoader(new URL[] { url }, this.getClass().getClassLoader());
				ZipEntry zipEntry = zipEntrys.nextElement();
				if (!zipEntry.getName().endsWith(".class"))
					continue;

				String clsName = check(zipEntry, ucl);
				if (clsName != "")
					ret.add(clsName);
			}
		} finally {
			try {
				if (zipFile != null)
					zipFile.close();
				if (c != null)
					((JarURLConnection)c).getJarFile().close();
			} catch (Exception e) {}
		}
		return ret;
	}
	
	@SuppressWarnings("resource")
	public String getClsName(String fullClsName){
		String[] temp = fullClsName.split(Constant.SEPARATOR);
		String clsFolder = temp[0] + Constant.SEPARATOR + temp[1];
		String className = temp[2].split(Constant.CLASSES)[0];
		try {
			URL url = new File(this.trinitySys.getDir().getExtlib(), clsFolder).toURI().toURL();
			URL[] urls = {url};
			URLClassLoader myClassLoader = new URLClassLoader(urls, this.getClass().getClassLoader());
			Class<?> clz = myClassLoader.loadClass(className);
			if (isImplementer(clz, "com.netpro.dm.core.ITransformRule", "com.netpro.dm.transformer.AbstractTransformRule")) {
				return clz.getName();
			}
//			if (ITransformRule.class.isAssignableFrom(clz)) {
//				return clz.getName();
//			}
		} catch (Throwable e) {
//			e.printStackTrace();
		}
		return "";
	}
	
	public String fullFileName(String pName, String fileName){
		String ret = "";
		String[] temps = fileName.split(Constant.SEPARATOR);
		int len = temps.length;
		if (len == 1){
			if (fileName.endsWith(Constant.CLASSES)){
				ret = pName + Constant.SEPARATOR + Constant.FOLDER_CLASS + Constant.SEPARATOR + fileName;
			} else {
				ret = pName + Constant.SEPARATOR + fileName;
			}
		} else {
			ret = fileName;
		}
		
		return ret;
	}
	
	public String getRuleExtDesc(String fileName, String ruleName, String fullClass) throws IOException{
		ZipFile zipFile = null;
		URLConnection c = null;
		try {
			zipFile = new ZipFile(new File(this.trinitySys.getDir().getExtlib(), fileName));
			URL url = new URL("jar", "", -1, new File(zipFile.getName()).toURI().toString() + "!/");

			Enumeration<? extends ZipEntry> zipEntrys = zipFile.entries();
			while (zipEntrys.hasMoreElements()) {
				c = url.openConnection();
				URLClassLoader ucl = new URLClassLoader(new URL[] { url }, this.getClass().getClassLoader());
				ZipEntry zipEntry = zipEntrys.nextElement();
				if (!zipEntry.getName().endsWith(Constant.CLASSES))
					continue;

				Class<?> objClass = getClass(zipEntry, ucl);
				StringBuffer sb = new StringBuffer();
				if (null != objClass && fullClass.equals(objClass.getName())){
					String[] fullClassArray = fullClass.split("\\.");
					InputStream is = null;
					BufferedReader br = null;
					try {
						is = objClass.getResourceAsStream(fullClassArray[fullClassArray.length - 1] + ".html");
						if (is != null){
							br = new BufferedReader(new InputStreamReader(is, "utf8"));
							String temp = "";
							while ((temp = br.readLine()) != null)
								sb.append(temp);
							
							return sb.toString();
						}
					} finally {
						try {
							if(null != br)
								br.close();
							if(is != null)
								is.close();
						}catch(Exception e) {}
					}
				}
			}
		} finally {
			try {
				if (zipFile != null)
					zipFile.close();
				if (c != null)
					((JarURLConnection)c).getJarFile().close();
			} catch (Exception e) {}
		}
		return "";
	}
	
	public void checkDir(String filePath){
		String[] files = filePath.split(Constant.SEPARATOR);
		int size = files.length;
		if (size == 1)
			return;
		
		String filename = files[size - 1];
		String dirPath = filePath.substring(0, filePath.lastIndexOf(filename));
		
		File dir = new File(this.trinitySys.getDir().getExtlib(), dirPath);
		if (!dir.exists())
			dir.mkdirs();
	}
	
	public String checkFileType(File file) throws Exception {
		ZipFile zipFile = null;
		URLConnection c = null;
		try {
			if (file.getName().endsWith(Constant.CLASSES))
				return Constant.RULE_CLASS;

			if (!file.getName().endsWith(".jar"))
				return Constant.LIBRARY;

			zipFile = new ZipFile(file);
			URL url = new URL("jar", "", -1, new File(zipFile.getName()).toURI().toString() + "!/");

			Enumeration<? extends ZipEntry> zipEntrys = zipFile.entries();
			while (zipEntrys.hasMoreElements()) {
				c = url.openConnection();
				URLClassLoader ucl = new URLClassLoader(new URL[] { url }, this.getClass().getClassLoader());
				ZipEntry zipEntry = zipEntrys.nextElement();
				if (!zipEntry.getName().endsWith(Constant.CLASSES))
					continue;

				if (check(zipEntry, ucl) != "")
					return Constant.RULE_JAR;
			}
			return Constant.LIBRARY;
		} catch (Exception e){
			throw e;
		} finally {
			try {
				if (zipFile != null)
					zipFile.close();
				if (c != null)
					((JarURLConnection)c).getJarFile().close();
			} catch (Exception e) {}
		}
	}
	
	private Class<?> getClass(ZipEntry zipEntry, URLClassLoader ucl) {
		try {
			String className = zipEntry.getName().replace("/", ".");
			className = className.substring(0, className.length() - 6);

			Class<?> objClass = Class.forName(className, false, ucl);

			return objClass;
			
		} catch (Throwable e){}
		return null;
	}
	
	private String check(ZipEntry zipEntry, URLClassLoader ucl) {
		String className = "";
		try {
			className = zipEntry.getName().replace("/", ".");
			className = className.substring(0, className.length() - 6);

			Class<?> objClass = Class.forName(className, false, ucl);

			if (isImplementer(objClass, "com.netpro.dm.core.ITransformRule", "com.netpro.dm.transformer.AbstractTransformRule")) {
				return objClass.getName();
			}
		} catch (Throwable e){
			System.out.println("Error Class:"+className);
			e.printStackTrace();
		}
		return "";
	}
	
	private boolean isImplementer(Class<?> cls, String interfaze, String superClass){
		Class<?> []ints = cls.getInterfaces();
		Class<?> clsSuperClass = cls.getSuperclass();
		
		if(null != clsSuperClass) {
			String superClassName = clsSuperClass.getName();
			if(superClass.equals(superClassName))
				return true;
		}
		
		for(Class<?> intf : ints) {
			String interfaceName = intf.getName();
			if(interfaceName.equals(interfaze)){
				return true;
			}
		}
		return false;
	}
	
	private Node getNodeByAgentUID(NodeList nl, String agentUID) throws Exception{
		for (int i = 0 ; i < nl.getLength() ; i++){
			Node node = nl.item(i);
			if (FileDetailUtil.getNodeAttByName(node, Constant.TAG_ATTR_AGENTUID).equals(agentUID)){
				return node;
			}
		}
		return null;
	}
	
	private Map<String, String> getAttributes(NamedNodeMap nnm){
		Map<String, String> ret = new HashMap<String, String>();
		for (int j = 0 ; j < nnm.getLength() ; j++){
			Node att = nnm.item(j);
			ret.put(att.getNodeName(), att.getNodeValue());
		}
		return ret;
	}
}
