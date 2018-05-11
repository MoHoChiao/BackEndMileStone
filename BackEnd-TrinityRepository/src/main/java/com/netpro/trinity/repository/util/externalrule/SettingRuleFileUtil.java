package com.netpro.trinity.repository.util.externalrule;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.netpro.trinity.repository.entity.externalrule.jdbc.DmExtRule;
import com.netpro.trinity.repository.prop.TrinitySysSetting;
import com.netpro.trinity.repository.util.Constant;
import com.netpro.trinity.repository.util.XMLUtil;
import com.netpro.trinity.repository.util.drivermanager.FileDetailUtil;

@Component
public class SettingRuleFileUtil {
	@Autowired
	private TrinitySysSetting trinitySys;
		
	public List<DmExtRule> getRuleByAgentUID(String agentUID) throws IOException, Exception{
		List<DmExtRule> ret = new ArrayList<DmExtRule>();
		
		Document doc = XMLUtil.toDocument(readFile());
		NodeList nl = doc.getElementsByTagName(Constant.TAG_AGENT);
		Node agentHostNode = getNodeByAgentUID(nl, agentUID);
		
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
			
			DmExtRule key = new DmExtRule();
			key.setExtjaruid(jaruid);
			key.setRulename(temp[1]);
			
			ret.add(key);
		}
		
		return ret;
	}
	
	private String readFile() throws IOException{
		StringBuffer sb = new StringBuffer();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(new File(this.trinitySys.getDir().getExtlib(), Constant.EXT_RULE_CFG)));
			
			String temp = "";
			while ((temp = br.readLine()) != null)
				sb.append(temp);
		} finally {
			if (br != null)
				br.close();
		}
		
		return sb.toString();
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
	
	public String writeExtRuleCfg(Map<String, List<Map<String, String>>> dataMap) throws Exception{
		Document doc = XMLUtil.newDocument();
		Element root = doc.createElement("root");
		doc.appendChild(root);
		
		for (Map.Entry<String, List<Map<String, String>>> entry : dataMap.entrySet()){
			Element agentNode = doc.createElement(Constant.TAG_AGENT);
			agentNode.setAttribute(Constant.TAG_ATTR_AGENTUID, entry.getKey());
			root.appendChild(agentNode);
			
			for (Map<String, String> map : entry.getValue()){
				Element ruleNode = doc.createElement(Constant.TAG_RULE);
				ruleNode.setAttribute(Constant.TAG_ATTR_ACTIVE, map.get(Constant.TAG_ATTR_ACTIVE));
				ruleNode.setAttribute(Constant.TAG_ATTR_NAME, map.get(Constant.TAG_ATTR_NAME));
				ruleNode.setAttribute(Constant.TAG_ATTR_CLASSPATH, map.get(Constant.TAG_ATTR_CLASSPATH));
				ruleNode.setAttribute(Constant.TAG_ATTR_FILENAME, map.get(Constant.TAG_ATTR_FILENAME));
				ruleNode.setAttribute(Constant.TAG_ATTR_JARUID, map.get(Constant.TAG_ATTR_JARUID));
				ruleNode.setAttribute(Constant.TAG_ATTR_MD5, map.get(Constant.TAG_ATTR_MD5));
				
				agentNode.appendChild(ruleNode);
			}
		}
		String cfg = XMLUtil.toXML(doc);
		writeCfg(cfg);
		return cfg;
	}
	
	public void writeCfg(String xmlData) throws IOException{
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
	
	public String updataPackageName(List<String> jarUIDs, String pName) throws IOException, Exception{
		Document doc = XMLUtil.toDocument(readFile());
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
		Document doc = XMLUtil.toDocument(readFile());
		NodeList nl = doc.getElementsByTagName(Constant.TAG_RULE);
		for (int i = 0 ; i < nl.getLength() ; i++){
			Element ruleNode = (Element)nl.item(i);
			if (jarUIDs.contains(ruleNode.getAttribute(Constant.TAG_ATTR_JARUID)))
				return true;
		}
		return false;
	}
	
	public boolean haveRulePublishByJar(String jarUID) throws IOException, Exception{
		Document doc = XMLUtil.toDocument(readFile());
		NodeList nl = doc.getElementsByTagName(Constant.TAG_RULE);
		for (int i = 0 ; i < nl.getLength() ; i++){
			Element ruleNode = (Element)nl.item(i);
			if (ruleNode.getAttribute(Constant.TAG_ATTR_JARUID).equals(jarUID))
				return true;
		}
		return false;
	}
	
	public boolean haveRulePublishByRule(String jarUID, String ruleName) throws IOException, Exception{
		Document doc = XMLUtil.toDocument(readFile());
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
		Document doc = XMLUtil.toDocument(readFile());
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
		
		Document doc = XMLUtil.toDocument(readFile());
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
	
	private List<String> getRules(File jarFile){
		List<String> ret = new ArrayList<String>();
		ZipFile zipFile = null;
		URLConnection c = null;
		try {
			zipFile = new ZipFile(jarFile);
			URL url = new URL("jar", "", -1, new File(zipFile.getName()).toURI().toString() + "!/");

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
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			try {
				if (zipFile != null)
					zipFile.close();
				if (c != null)
					((JarURLConnection)c).getJarFile().close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ret;
	}
}
