/**
 * Copyright (C) NetPro Information Service Ltd., 2010.
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

package com.netpro.trinity.resource.admin.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/*******************************************************************************
 * <p><b>Description</b>: {description}
 *
 * <p><b>Modification History</b>:
 * <table border=1>
 * <tr>
 * <td>Date</td>
 * <td>Developer</td>
 * <td>Description</td>
 * </tr>
 * <tr>
 * <td>2010/2/2</td>
 * <td>Jonathan</td>
 * <td>{description}</td>
 * </tr>
 * </table>
 *
 * @version 1.0
 * @author Jonathan
 ******************************************************************************/
@Component
public class XMLDataUtility {

	/**
	 * 將 HashMap 物件轉成 XML string
	 * 
	 * @param HashMap <String,String> map
	 * 
	 * @return String XML
	 */
	public String parseHashMapToXMLString(Map<String, String> map, boolean isEncoding) {
		
		DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		Document doc;
		
		try {
			docBuilder = dbfac.newDocumentBuilder();
			doc = docBuilder.newDocument();

			Element root = doc.createElement("xmldata");
			doc.appendChild(root);
			for (String name : map.keySet()) {
				Element child = doc.createElement("prop");
				child.setAttribute("name", name);
				child.setAttribute("value", map.get(name));
				root.appendChild(child);
			}

			TransformerFactory transfac = TransformerFactory.newInstance();
			Transformer trans = transfac.newTransformer();
			if(isEncoding){
				trans.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
				trans.setOutputProperty(OutputKeys.INDENT, "yes");
			}else{
				trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			}	

			StringWriter sw = new StringWriter();
			StreamResult result = new StreamResult(sw);
			DOMSource source = new DOMSource(doc);
			trans.transform(source, result);
			String xmlString = sw.toString();

			return xmlString;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}

		return "";
	}
	
	public String parseHashMapToXMLStringForFTPStep(Map<String, String> propMap, List<Map<String, String>> ftpList) {
		DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		Document doc;
		
		try {
			docBuilder = dbfac.newDocumentBuilder();
			doc = docBuilder.newDocument();

			Element root = doc.createElement("xmldata");
			doc.appendChild(root);
			for (String name : propMap.keySet()) {
				Element child = doc.createElement("prop");
				child.setAttribute("name", name);
				child.setAttribute("value", propMap.get(name));
				root.appendChild(child);
			}
			
			for (Map<String, String> map : ftpList) {
				Element child = doc.createElement("ftp");
				for (Map.Entry<String, String> entry : map.entrySet()){
					child.setAttribute(entry.getKey(), entry.getValue());
				}
				root.appendChild(child);
			}

			TransformerFactory transfac = TransformerFactory.newInstance();
			Transformer trans = transfac.newTransformer();
			
//			以下兩句組出 <?xml version="1.0" encoding="UTF-8"?>
//			trans.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
//			trans.setOutputProperty(OutputKeys.INDENT, "yes");
			
			trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

			StringWriter sw = new StringWriter();
			StreamResult result = new StreamResult(sw);
			DOMSource source = new DOMSource(doc);
			trans.transform(source, result);
			String xmlString = sw.toString();

			return xmlString;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}

		return "";
	}

	public String updateXMLDataRerunProp(String xmlData, boolean singleRerun) {
		if (xmlData == null || xmlData.length() == 0) {
			if (singleRerun)
				xmlData = "<xmldata><rerun><prop name=\"single\" value=\"true\"/></rerun></xmldata>";
		} else {
			try {
				Document doc = parseXML(xmlData);
				if (doc != null) {
					NodeList rootNL = doc.getElementsByTagName("xmldata");
					Node rootNode = rootNL.item(0);
					NodeList rerunNL = doc.getElementsByTagName("rerun");
					int len = rerunNL.getLength();

					if (singleRerun) {
						if (len == 0) {
							Element rerun = doc.createElement("rerun");
							Element child = doc.createElement("prop");
							child.setAttribute("name", "single");
							child.setAttribute("value", "true");
							rerun.appendChild(child);
							rootNode.appendChild(rerun);
						}
					} else {
						if (len > 0) {
							rootNode.removeChild(rerunNL.item(0));
						}
					}

					TransformerFactory transfac = TransformerFactory.newInstance();
					Transformer trans = transfac.newTransformer();
					trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

					StringWriter sw = new StringWriter();
					StreamResult result = new StreamResult(sw);
					DOMSource source = new DOMSource(doc);
					trans.transform(source, result);
					xmlData = sw.toString();
				}
			} catch (TransformerConfigurationException e) {
				e.printStackTrace();
			} catch (TransformerException e) {
				e.printStackTrace();
			}
		}
		
		return xmlData;
	}
	
	/**
	 * 將 XML string 轉成 HashMap, 分別放入 name 及 value
	 * 
	 * @param String xmlData
	 * 
	 * @return HashMap<String, String> hm
	 */
	public HashMap<String, String> parseXMLDataToHashMap(String xmlData) {

		HashMap<String, String> hm = new HashMap<String, String>();
		if (xmlData == null || xmlData.length() < 1) {
			return null;
		}

		Document doc = parseXML(xmlData);
		NodeList node = doc.getElementsByTagName("prop");
		
		//如果找不到prop的屬性，則直接回傳空值
		if(node==null || node.getLength()==0){
			return null;	
		}
		
		Node nodeItem;
		NamedNodeMap map;

		int length = node.getLength();
		for (int i = 0; i < length; i++) {
			nodeItem = node.item(i);
			map = nodeItem.getAttributes();
			hm.put(map.getNamedItem("name").getNodeValue(), map.getNamedItem("value").getNodeValue());
		}

		return hm;
	}

	/**
	 * 將傳入的MetaLookupTable物件轉成 XML string
	 * 
	 * @param HashMap<String, String> propMap,HashMap<String, String> lookMap
	 * 
	 * @return String XML
	 */
	public String parseLookupTbMapToXMLString(HashMap<String, String> propMap,HashMap<String, String> lookMap) {
		
		DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		Document doc;
		
		try {
			docBuilder = dbfac.newDocumentBuilder();
			doc = docBuilder.newDocument();

			Element root = doc.createElement("xmldata");
			doc.appendChild(root);
			
			if(!propMap.isEmpty()){
				for (String name : propMap.keySet()) {
					Element child = doc.createElement("prop");
					child.setAttribute("name", name);
					child.setAttribute("value", propMap.get(name));
					root.appendChild(child);
				}					
			}
			
			if(!lookMap.isEmpty()){
				for (String key : lookMap.keySet()) {
					Element child = doc.createElement("lookup");
					child.setAttribute("key", key);
					child.setAttribute("value", lookMap.get(key));
					root.appendChild(child);
				}						
			}
			
			TransformerFactory transfac = TransformerFactory.newInstance();
			Transformer trans = transfac.newTransformer();
			
//			以下兩句組出 <?xml version="1.0" encoding="UTF-8"?>
//			trans.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
//			trans.setOutputProperty(OutputKeys.INDENT, "yes");
			
			trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

			StringWriter sw = new StringWriter();
			StreamResult result = new StreamResult(sw);
			DOMSource source = new DOMSource(doc);
			
			trans.transform(source, result);
			String xmlString = sw.toString();

			return xmlString;
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}

		return "";
	}	

	/**
	 * 將傳入的MetaLookupTable XML string轉成 List< HashMap<String,String> >回傳
	 * (目前只需要拆解MetaLookupTable Attribute)
	 * 
	 * @param String xmlData
	 * 
	 * @return List< HashMap<String,String> >
	 */
	public List<HashMap<String,String>> parseLookupTbXMLToAttribute(String xmlData) {

		List<HashMap<String,String>> lookupTbAttribute = new ArrayList<HashMap<String,String>>();
		
		if (xmlData == null || xmlData.length() < 1) {
			return null;
		}

		Document doc = parseXML(xmlData);
		NodeList node = doc.getElementsByTagName("lookup");//將lookup element拆解一一放入List中
		
		//如果找不到lookup的屬性，則直接回傳空值
		if(node==null || node.getLength()==0){
			return null;	
		}
		
		Node nodeItem;
		NamedNodeMap map;
		
		//跑loop將物件一一放入
		for (int i = 0; i < node.getLength(); i++) {
			
			nodeItem = node.item(i);
			map = nodeItem.getAttributes();
			
			HashMap<String,String> tempAttributes = new HashMap<String,String>();
			
			tempAttributes.put("key", map.getNamedItem("key").getNodeValue());
			tempAttributes.put("value", map.getNamedItem("value").getNodeValue());
			
			lookupTbAttribute.add(tempAttributes);
		}

		return lookupTbAttribute;
	}
	
	public List<Map<String, String>> parseXMLDataFtpToList(String xmlData){
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		if(xmlData == null || xmlData.length() < 1 || !xmlData.startsWith("<")){
			return list;
		}
		
		Document doc = parseXML(xmlData);
		NodeList node = doc.getElementsByTagName("ftp");
		Node nodeItem;
		NamedNodeMap map;
		
		int length = node.getLength();
		for(int i = 0; i < length; i++){
			nodeItem = node.item(i);
			map = nodeItem.getAttributes();
			
			String path = map.getNamedItem("path").getNodeValue();
			String name = map.getNamedItem("name").getNodeValue();
			String remote = map.getNamedItem("remote").getNodeValue();
			
			Map<String, String> hm = new HashMap<String, String>();
			hm.put("path", path);
			hm.put("name", name);
			hm.put("remote", remote);
			
			list.add(hm);
		}
		
		return list;
	}
	
	public Document parseXML(String xmlString) {
		
		DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
		DocumentBuilder b = null;
		Document doc = null;
		
		try {
			b = f.newDocumentBuilder();
			doc = b.parse(new InputSource(new StringReader(xmlString)));
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		return doc;
	}
	
	public String parseMailListToXMLStringForTrinityUser(List<String> mailList) {
		DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		Document doc;
		
		try {
			docBuilder = dbfac.newDocumentBuilder();
			doc = docBuilder.newDocument();
			
			Element root = doc.createElement("xmldata");
			doc.appendChild(root);
			
			for(String mail:mailList) {
				Element child = doc.createElement("mail");
				child.setAttribute("value", mail);
				root.appendChild(child);
			}
			
			TransformerFactory transfac = TransformerFactory.newInstance();
			Transformer trans = transfac.newTransformer();
			
			trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			
			StringWriter sw = new StringWriter();
			StreamResult result = new StreamResult(sw);
			DOMSource source = new DOMSource(doc);
			trans.transform(source, result);
			String xmlString = sw.toString();
			
			return xmlString;
		}catch(ParserConfigurationException e) {
			e.printStackTrace();
		}catch(TransformerConfigurationException e) {
			e.printStackTrace();
		}catch(TransformerException e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	public String parseXMLStringToMailListStringForTrinityUser(String xmldata) {
		StringBuffer retString = new StringBuffer("");
		if(null == xmldata || xmldata.length()<1 || !xmldata.startsWith("<")) {
			return retString.toString();
		}
		
		Document doc = parseXML(xmldata);
		NodeList node = doc.getElementsByTagName("mail");
		Node nodeItem;
		NamedNodeMap map;
		
		int length = node.getLength();
		
		for(int i=0;i<length;i++) {
			nodeItem = node.item(i);
			map = nodeItem.getAttributes();
			String mail = map.getNamedItem("value").getNodeValue();
			retString.append(";"+mail);
		}
		
		return retString.toString();
	}
}
