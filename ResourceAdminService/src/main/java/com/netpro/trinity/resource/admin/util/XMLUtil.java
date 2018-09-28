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

import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * @author Jack Lu
 */
public abstract class XMLUtil {
	
	public static final Document newDocument() throws Exception{
		Document document = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder().newDocument();
		return document;
	}
	
	//回傳的字串中'<'等特殊字元會被轉換為&lt;等字元
	public static final String toXML(Document document) throws Exception {
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,"yes");
		StringWriter sw = new StringWriter();
		Result result = new StreamResult(sw);
		transformer.transform(new DOMSource(document), result);
		return sw.toString();
	}
	
	// 將文件Parse成Document物件
	public static Document toDocument(String xmlString) throws Exception {
		DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
		Document doc = f.newDocumentBuilder().parse(
				new InputSource(new StringReader(xmlString)));
		
		return doc;
	}
	
	public static Document toDocument(InputStream is) throws Exception {
		DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
		Document doc = f.newDocumentBuilder().parse(is);
		
		return doc;
	}
	
	public static String createOption(String config) {
		StringBuffer option = new StringBuffer("");
		Map<String, String> map = parseConfig(config);
		
		int i = 0;
		for (Entry<String, String> e : map.entrySet()) {
			if (i > 0)
				option.append(", ");
			option.append(e.getKey() + " = " + e.getValue());
			i++;
		}
		return option.toString();
	}
	
	public static String xml2Text(String config) {
		StringBuffer sb = new StringBuffer("");
		Map<String, String> map = parseConfig(config);
		int i = 0;
		for (Entry<String, String> e : map.entrySet()) {
			if (i > 0)
				sb.append(", ");
			sb.append(e.getKey() + " = " + e.getValue());
			i++;
		}
		return sb.toString();
	}
	
	public static String xml2Tooltip(String config) {
		StringBuffer sb = new StringBuffer("");
		Map<String, String> map = parseConfig(config);
		for (Entry<String, String> e : map.entrySet()) {
			sb.append(e.getKey() + " = " + e.getValue() + "\n");
		}
		return sb.toString();
	}
	
	private static Map<String, String> parseConfig(String config) {
		Map<String, String> map = new HashMap<String, String>();
		
		try {
			Document doc = XMLUtil.toDocument(config);
			NodeList nList = doc.getElementsByTagName("item");
			for (int i = 0; i < nList.getLength(); i++) {
				Element ele = (Element)nList.item(i);
				String name = ele.getAttribute("name");
				String value = ele.getAttribute("value");
				if (name != null && name.length() > 0)
					map.put(name, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return map;
	}
	
	public static Set<String> getConfigColumns(String config) {
		Set<String> set = new HashSet<String>();
		
		try {
			Document doc = XMLUtil.toDocument(config);
			NodeList nList = doc.getElementsByTagName("item");
			for (int i = 0; i < nList.getLength(); i++) {
				Element ele = (Element)nList.item(i);
				if ("Column".equals(ele.getAttribute("type"))) {
					set.add(ele.getAttribute("value"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return set;
	}
	
}