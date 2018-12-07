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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

/**
 * 
 * @author Dean
 */
public class DriverFileUtil {
	
	public static String filePath = System.getProperty("jdbc.dir") + "/" + Constant.DRIVER_FILE_NAME;
	
	public static String readValue(String key) {
		Properties props = new Properties();
		FileInputStream inFile = null;
		try {
			inFile = new FileInputStream(filePath);
			props.load(new BufferedInputStream(inFile));
			return props.getProperty(key);
		} catch (Exception ex) {
			ex.printStackTrace();
			return "";
		} finally {
			if (inFile != null){
				try {
					inFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static Enumeration<String> getKeys() {
		Properties props = new Properties();
		FileInputStream inFile = null;
		try {
			inFile = new FileInputStream(filePath);
			props.load(new BufferedInputStream(inFile));
			return (Enumeration<String>)props.propertyNames();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			if (inFile != null){
				try {
					inFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

//	public static Properties readProperties() {
//		Properties props = new Properties();
//		try {
//			props.load(new BufferedInputStream(new FileInputStream(filePath)));
//			return props;
//		} catch (Exception ex) {
//			return null;
//		}
//	}

//	public static void writeProperties(String parameterKey,	String parameterValue) {
//		Properties prop = new Properties();
//		try {
//			prop.load(new FileInputStream(filePath));
//			prop.setProperty(parameterKey, parameterValue);
//			prop.store(new FileOutputStream(filePath), "Last Update, " + new Date());
//		} catch (Exception ex) {
//		}
//	}
	
	public static boolean writeProperties(Map<String, String> data) {
		Properties prop = new Properties() {
		    /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
		    public synchronized Enumeration<Object> keys() {
		        return Collections.enumeration(new TreeSet<Object>(super.keySet()));
		    }
		};
		
		FileInputStream inFile = null;
		FileOutputStream outFile = null;
		try {
			inFile = new FileInputStream(filePath);
			prop.load(new BufferedInputStream(inFile));
			for (String key : data.keySet()){
				prop.setProperty(key, data.get(key));
			}
			outFile = new FileOutputStream(filePath);
			prop.store(outFile, "Last Update, " + new Date());
			return true;
		} catch (Exception ex) {
			return false;
		} finally {
			try {
				if (inFile != null)
					inFile.close();
				if (outFile != null)
					outFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void removeProperty(String folderName) {
		Properties props = new Properties() {
		    /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
		    public synchronized Enumeration<Object> keys() {
		        return Collections.enumeration(new TreeSet<Object>(super.keySet()));
		    }
		};
		
		FileInputStream inFile = null;
		try {
			inFile = new FileInputStream(filePath);
			props.load(new BufferedInputStream(inFile));
			props.remove(folderName + Constant.JDBC_DRIVER);
			props.remove(folderName + Constant.JDBC_URL);
			props.remove(folderName + Constant.JDBC_OWNER);
			props.store(new FileOutputStream(filePath), "Last Update, " + new Date());
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (inFile != null){
				try {
					inFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

//	public static void updateProperty(String key, String value) {
//		Properties props = new Properties();
//		try {
//			props.load(new BufferedInputStream(new FileInputStream(filePath)));
//			props.setProperty(key, value);
//			props.store(new FileOutputStream(filePath), "Last Update, " + new Date());
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//	}

//	public static boolean checkKeyAvailability(String key) {
//		boolean flag = true;
//		Enumeration<String> keys = getKeys();
//		while (keys.hasMoreElements()) {
//			String keyStr = keys.nextElement();
//			if (keyStr.equalsIgnoreCase(key)) {
//				flag = false;
//			}
//		}
//		return flag;
//	}
	
	public static Set<String> getAllNames(){
		Set<String> result = new HashSet<String>();
		Enumeration<String> en = getKeys();
		while(en.hasMoreElements()){
			String key = en.nextElement();
			if (key.split("\\.").length == 0)
				continue;
			result.add(key.split("\\.")[0]);
		}
		return result;
	}
	
	public static List<Map<String, String>> getAllJdbcDriver(){
		LinkedList<Map<String, String>> return_list = new LinkedList<Map<String, String>>();
		Map<String, Map<String, String>> mappingData = new HashMap<String, Map<String, String>>();
		Set<String> names = getAllNames();
		List<String> systemList = new ArrayList<String>();
		List<String> userList = new ArrayList<String>();
		
		for (String name : names){
			Map<String, String> return_map = new HashMap<String, String>();
			return_map.put("driver", readValue(name + Constant.JDBC_DRIVER));
			return_map.put("url", readValue(name + Constant.JDBC_URL));
			String owner = readValue(name + Constant.JDBC_OWNER);
			return_map.put("icon", owner);
			return_map.put("label", name);
			
			if ("system".equalsIgnoreCase(owner)){
				systemList.add(name);
			} else {
				userList.add(name);
			}
			mappingData.put(name, return_map);
		}
		//排序(system)
		if (systemList.size() != 0 ){
			String[] systemArray = systemList.toArray(new String[systemList.size()]);
			Arrays.sort(systemArray);
			for (int i = 0 ; i < systemArray.length ; i++){
				if (systemArray[i] != null && systemArray[i].trim().length() != 0){
					return_list.add(mappingData.get(systemArray[i]));
				}
			}
		}
		//排序(user)
		if (userList.size() != 0 ){
			String[] userArray = (String[])userList.toArray(new String[userList.size()]);
			Arrays.sort(userArray);
			for (int i = 0 ; i < userArray.length ; i++){
				if (userArray[i] != null && userArray[i].trim().length() != 0){
					return_list.add(mappingData.get(userArray[i]));
				}		
			}
		}
		
		return return_list;
	}

	public static List<String> getFiles(String folder){
		List<String> result = new ArrayList<String>();
		File f = new File(System.getProperty("jdbc.dir") + "/" + folder);
		if (f.exists()){
			String[] files = f.list();
			for (int i = 0 ; i < files.length ; i++){
				if (files[i].indexOf("svn") == -1)
					result.add(files[i]);
			}
		}
		return result;
	}
}
