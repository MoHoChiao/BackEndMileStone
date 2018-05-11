/**
 * Copyright (C) NetPro Information Service Ltd., 2011.
 * All rights reserved.
 * 
 * This software is covered by the license agreement between
 * the end user and NetPro Information Service Ltd., and may be 
 * used and copied only in accordance with the terms of the 
 * said agreement.
 * 
 * NetPro Information Service Ltd. assumes no responsibility or 
 * liability for any errors or inaccuracies in this software, 
 * or any consequential, incidental or indirect damage arising
 * out of the use of the software.
 */

package com.netpro.trinity.repository.util.drivermanager;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.netpro.trinity.repository.util.Constant;

/**
 * 
 * @author Dean
 */
public class FileDetailUtil {	
	private static MessageDigest digest = null;
	
	public static final char[] hexChar = new char[] { '0', '1', '2', '3', '4', '5', '6', '7',
													  '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	
	private static void initMD5Digest() throws NoSuchAlgorithmException, Exception{
		digest = MessageDigest.getInstance("MD5"); //$NON-NLS-1$
	}
	

	public static Map<String, String> getFileDetail(String rootPath) throws NoSuchAlgorithmException, Exception{
		Map<String, String> ret = new HashMap<String, String>();
		File f = new File(rootPath);
		
		if (!f.exists())
			return null;
		
		List<String> filePathList = new LinkedList<String>();
		for (File file : f.listFiles()){
			if (file.isDirectory()){
				filePathList.addAll(parseDir("", file));
			} else {
				filePathList.add(file.getName());
			}
		}
		
		for (String s : filePathList){
			f = new File(rootPath, s);
			if (f.isDirectory())
				continue;
			
			ret.put(s, getFileMD5(f));
		}
			
		return ret;
	}
	
	public static Map<String, String> getFileDetailInXmlRule(String rootPath, String ip) throws FileNotFoundException, Exception{
		Map<String, String> ret = new HashMap<String, String>();
		File extf = new File(rootPath, Constant.EXT_RULE_CFG);
		if (!extf.exists())
			return ret;
		
		Scanner s = null;
		try {
			StringBuffer sb = new StringBuffer();
			s = new Scanner(extf);
			while (s.hasNextLine()){
				sb.append(s.nextLine());
			}
			
			ret = getDetailInDoc(parseXML(sb.toString()), ip);
			return ret;
		}finally {
			try {
				if(null != s)
					s.close();
			}catch(Exception e) {}
		}
	}
	
	public static List<Map<String, String>> getXmlRuleByIP(String rootPath, String ip) throws FileNotFoundException,Exception{
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		File extf = new File(rootPath, Constant.EXT_RULE_CFG);
		if (!extf.exists())
			return list;
		
		Scanner s = null;
		try {
			StringBuffer sb = new StringBuffer();
			s = new Scanner(extf);
			while (s.hasNextLine()){
				sb.append(s.nextLine());
			}
			
			Document doc = parseXML(sb.toString());
			NodeList agentNodeList = doc.getElementsByTagName(Constant.TAG_AGENT);
			for (int i = 0 ; i < agentNodeList.getLength() ; i++){
				Node agentNode = agentNodeList.item(i);
				if (getNodeAttByName(agentNode, Constant.TAG_ATTR_AGENTUID).equals(ip.trim())){
					NodeList ruleNodeList = agentNode.getChildNodes();
					for (int j = 0 ; j < ruleNodeList.getLength() ; j++){
						Node ruleNode = ruleNodeList.item(j);
						
						//只處理一般Node
						if(ruleNode.getNodeType() != 1)
							continue;
						
						Map<String, String> map = new HashMap<String, String>();
						map.put(Constant.TAG_ATTR_ACTIVE, getNodeAttByName(ruleNode, Constant.TAG_ATTR_ACTIVE));
						map.put(Constant.TAG_ATTR_NAME, getNodeAttByName(ruleNode, Constant.TAG_ATTR_NAME));
						map.put(Constant.TAG_ATTR_CLASSPATH, getNodeAttByName(ruleNode, Constant.TAG_ATTR_CLASSPATH));
						list.add(map);
					}
				}
			}
			return list;
		}finally {
			try {
				if(null != s)
					s.close();
			}catch(Exception e) {}
		}
	}
	
	private static List<String> parseDir(String root, File f) throws Exception{
		List<String> list = new LinkedList<String>();
		if (root.trim().length() != 0)
			root = root + File.separator + f.getName();	
		else 
			root = f.getName();
		
		if (f.listFiles().length == 0){
			list.add(root);
			return list;
		}
			
		for (File file : f.listFiles()){
			if (file.isDirectory()){
				list.addAll(parseDir(root, file));
			} else {
				list.add(root + File.separator + file.getName());
			}
		}
		return list;
	}
	
	public static void checkDir(String rootPath, String filePath) throws Exception{
		String[] files = filePath.split(File.separator);
		int size = files.length;
		if (size == 1)
			return;
		
		String filename = files[size - 1];
		String dirPath = filePath.substring(0, filePath.lastIndexOf(filename));
		
		File dir = new File(rootPath, dirPath);
		if (!dir.exists())
			dir.mkdirs();
	}
	
	public static void deleteEmptyFolders(String folderName) throws Exception {
		List<File> emptyFolders = new ArrayList<File>();
		findEmptyFoldersInDir(new File(folderName), emptyFolders);
		List<String> fileNames = new ArrayList<String>();
		
		for (File f : emptyFolders)
			fileNames.add(f.getAbsolutePath());

		for (File f : emptyFolders){
			if (new File(folderName).getName().equals(f.getName()))
				continue;
			f.delete();
		}
	}

	public static boolean findEmptyFoldersInDir(File folder, List<File> emptyFolders) throws Exception{
		boolean isEmpty = false;
		File[] filesAndDirs = folder.listFiles();
		List<File> filesDirs = Arrays.asList(filesAndDirs);
		if (filesDirs.size() == 0)
			isEmpty = true;

		if (filesDirs.size() > 0) {
			boolean allDirsEmpty = true;
			boolean noFiles = true;
			for (File file : filesDirs) {
				if (!file.isFile()) {
					boolean isEmptyChild = findEmptyFoldersInDir(file,
							emptyFolders);
					if (!isEmptyChild) {
						allDirsEmpty = false;
					}
				}
				if (file.isFile()) {
					noFiles = false;
				}
			}
			if (noFiles == true && allDirsEmpty == true) {
				isEmpty = true;
			}
		}
		if (isEmpty) {
			emptyFolders.add(folder);
		}
		return isEmpty;
	}
	
	private static byte[] getDigestHexBytes(byte[] source) throws Exception{
		byte[] bytes = digest.digest(source);
        return bytes;
    }
	
	private static String getHexString(byte[] bytes) throws Exception{
		StringBuffer sb = new StringBuffer(bytes.length * 2);
    
		for (int i = 0; i < bytes.length; i++) {
            sb.append(hexChar[(0x00f0 & bytes[i]) >> 4]);
            sb.append(hexChar[(0x000f & bytes[i])]);
		}

		return sb.toString();
	}
	
	public static String getFileMD5(byte[] source) throws Exception{
		if (digest == null)
			initMD5Digest();
		return getHexString(getDigestHexBytes(source));
	}
	
	public static String getFileMD5(File f) throws NoSuchAlgorithmException, Exception{
		if (digest == null)
			initMD5Digest();
		
		byte source[] = new byte[(int) f.length()];
		BufferedInputStream bis = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(f));
			bis.read(source);
			return getHexString(getDigestHexBytes(source));
		} finally {
			try {
				if(null != bis)
					bis.close();
			}catch(Exception e) {}
		}
	}
	
	private static Document parseXML(String xmlString) throws Exception{
		DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
		DocumentBuilder b = null;
		Document doc = null;
		
		b = f.newDocumentBuilder();
		doc = b.parse(new InputSource(new StringReader(xmlString)));
		
		return doc;
	}
	
	private static Map<String, String> getDetailInDoc(Document doc, String ip) throws Exception{
		Map<String, String> ret = new HashMap<String, String>();
		NodeList agentNodeList = doc.getElementsByTagName(Constant.TAG_AGENT);
		for (int i = 0 ; i < agentNodeList.getLength() ; i++){
			Node agentNode = agentNodeList.item(i);
			if (getNodeAttByName(agentNode, Constant.TAG_ATTR_AGENTUID).equals(ip.trim())){
				NodeList ruleNodeList = agentNode.getChildNodes();
				for (int j = 0 ; j < ruleNodeList.getLength() ; j++){
					Node ruleNode = ruleNodeList.item(j);
					
					//只處理一般Node
					if(ruleNode.getNodeType() != 1)
						continue;
					
					String key = getNodeAttByName(ruleNode, Constant.TAG_ATTR_FILENAME);
					String value = getNodeAttByName(ruleNode, Constant.TAG_ATTR_MD5);
					ret.put(key, value);
				}
			}
		}
		return ret;
	}
	
	public static String getNodeAttByName(Node node, String attr) throws Exception{
		NamedNodeMap nnm = node.getAttributes();
		for (int i = 0 ; i < nnm.getLength() ; i++){
			Node na = nnm.item(i);
			if (na.getNodeName().equals(attr))
				return na.getNodeValue();
		}
		return "";
	}
	
	public static List<Map<String, String>> getExtRuleDetail(String rootPath)throws FileNotFoundException, Exception{
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		File extf = new File(rootPath, Constant.EXT_RULE_CFG);
		if (!extf.exists())
			return list;
		
		Scanner s = null;
		try {
			StringBuffer sb = new StringBuffer();
			s = new Scanner(extf);
			while (s.hasNextLine()){
				sb.append(s.nextLine());
			}
			parseXmlToList(sb.toString(), list);
			return list;
		}finally {
			try {
				if(null != s)
					s.close();
			}catch(Exception e) {}
		}
	}
	
	private static void parseXmlToList(String xml, List<Map<String, String>> list) throws Exception {
		Document doc = parseXML(xml);
		NodeList ruleNodeList = doc.getElementsByTagName(Constant.TAG_RULE);
		for (int i = 0 ; i < ruleNodeList.getLength() ; i++){
			Node ruleNode = ruleNodeList.item(i);
			
			//只處理一般Node
			if(ruleNode.getNodeType() != 1)
				continue;
			
			Map<String, String> map = new HashMap<String, String>();
			map.put(Constant.TAG_ATTR_ACTIVE, getNodeAttByName(ruleNode, Constant.TAG_ATTR_ACTIVE));
			map.put(Constant.TAG_ATTR_NAME, getNodeAttByName(ruleNode, Constant.TAG_ATTR_NAME));
			map.put(Constant.TAG_ATTR_CLASSPATH, getNodeAttByName(ruleNode, Constant.TAG_ATTR_CLASSPATH));
			
			list.add(map);
		}
	}
	
	public static void writeExtRuleFile(List<Map<String, String>> list, String rootPath) 
				throws TransformerConfigurationException, TransformerException, IOException, Exception{
		String xml = parseListToXml(list);
		
		BufferedWriter bw = null;
		
		try {
			bw = new BufferedWriter(new FileWriter(new File(rootPath, Constant.EXT_RULE_CFG)));
			bw.write(xml, 0, xml.length());
		}finally {
			try {
				if(null != bw)
					bw.close();
			}catch(Exception e) {}
		}
	}
	
	private static String parseListToXml(List<Map<String, String>> list) 
				throws TransformerConfigurationException,TransformerException,Exception  {
		
        StringWriter sw = null;
		try {
			DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
			
			Document doc = docBuilder.newDocument();
			Element root = doc.createElement("xml");
			doc.appendChild(root);
			for (Map<String, String> map : list){
				Element child = doc.createElement(Constant.TAG_RULE);
				for (Map.Entry<String, String> entry : map.entrySet()){
					child.setAttribute(entry.getKey(), entry.getValue());
				}
				root.appendChild(child);
			}
			
            TransformerFactory transfac = TransformerFactory.newInstance();
			Transformer trans = transfac.newTransformer();
            trans.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            //trans.setOutputProperty(OutputKeys.INDENT, "yes");

			sw = new StringWriter();
			StreamResult result = new StreamResult(sw);
			DOMSource source = new DOMSource(doc);
			trans.transform(source, result);
			String xmlString = sw.toString();

			return xmlString;
		} finally {
			try {
				if(null != sw)
					sw.close();
			}catch(Exception e) {}
		}
	}
}

