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

package com.netpro.trinity.repository.drivermanager.lib;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.netpro.trinity.repository.prop.TrinitySysSetting;
import com.netpro.trinity.repository.util.Item;

@Component
public class PublishFileUtil {
	@Autowired
	private TrinitySysSetting trinitySys;
	
	private String cfgFileName = "filePublishCfg.xml";
	
	private final String TYPE_FOLDER = "Folder";
	private final String TYPE_FILE = "File";
	
	public final String PROP_PUBLISH = "publish";
	public final String PROP_MD5 = "md5";
	
	public Item getPublishCfgItem() throws FileNotFoundException, IOException, Exception{
		return Item.valueOf(getPublishCfg());
	}
	
	public String getPublishCfg() throws FileNotFoundException,IOException,Exception {
		String jdbcDirPath = trinitySys.getDir().getData() + File.separator;
		File cfg = new File(jdbcDirPath, cfgFileName);
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new FileReader(cfg));
			StringBuffer sb = new StringBuffer();
			String temp = "";
			while ((temp = br.readLine()) != null)
				sb.append(temp);
			
			return sb.toString();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException e) {}
		}
	}
	
	public void setPublishCfgItem(Item item) throws IOException, Exception{
		setPublishCfg(item.toString());
	}
	
	public void setPublishCfg(String cfg) throws IOException,Exception{
		String jdbcDirPath = trinitySys.getDir().getData() + File.separator;
		File cfgFile = new File(jdbcDirPath, cfgFileName);
		BufferedWriter bw = null;
		
		try {
			bw = new BufferedWriter(new FileWriter(cfgFile));
			bw.write(cfg);
			bw.flush();
		} finally {
			try {
				if (bw != null)
					bw.close();
			} catch (IOException e) {}
		}
	}
	
	public Map<String, String> getPublishFile() throws FileNotFoundException, IOException, Exception{
		return getPublishFile(null);
	}
	
	public Map<String, String> getPublishFile(String path) throws FileNotFoundException, IOException, Exception{
		Map<String, String> ret = new HashMap<String, String>();
		
		Item root = getPublishCfgItem();
		for (Item folderItem : root.getItems()){
			String folderName = folderItem.getName();
			for (Item item : folderItem.getItems()){
				String type = item.getType();
				if (type.equals(TYPE_FOLDER)){
					parseFolder(folderName, item, ret);
				} else if (type.equals(TYPE_FILE)){
					if (item.getProp(PROP_PUBLISH).equals("1")){
						String filePath = folderName + "/" + item.getName();
						ret.put(filePath, "1");
					} else {
						String filePath = folderName + "/" + item.getName();
						ret.put(filePath, "0");
					}
				}
			}
		}
		
		if (path == null){
			return ret;
		} else {
			Map<String, String> tempMap = new HashMap<String, String>();
			for (Map.Entry<String, String> entry : ret.entrySet()){
				if (entry.getKey().startsWith(path))
					tempMap.put(entry.getKey(), entry.getValue());
			}
			return tempMap;
		}		
	}
	
	private void parseFolder(String folderName, Item folderItem, Map<String, String> result) throws Exception{
		String rootFolder = "";
		if (folderName.trim().length() == 0)
			rootFolder = folderItem.getName();
		else 
			rootFolder = folderName + "/" + folderItem.getName();
		
		for (Item item : folderItem.getItems()){
			String type = item.getType();
			if (type.equals(TYPE_FOLDER)){
				parseFolder(rootFolder, item, result);
			} else if (type.equals(TYPE_FILE)){
				if (item.getProp(PROP_PUBLISH).equals("1")){
					String filePath = rootFolder + "/" + item.getName();
					result.put(filePath, "1");
				} else {
					String filePath = rootFolder + "/" + item.getName();
					result.put(filePath, "0");
				}
			}
		}
	}
	
	public void genPublishItem(Map<String, Map<String, String>> records, boolean includeChild) 
				throws FileNotFoundException, IOException, Exception{
		records = fullJDBCDriverDetail(records);
		Item item = getPublishCfgItem();
		for (Map.Entry<String, Map<String, String>> entry : records.entrySet()){
			String fullPath = entry.getKey();
			String splitReg = File.separator + File.separator;
			String[] dir = fullPath.split(splitReg);
			if (dir.length > 1){
				Item tempItem = item;
				for (String temp : dir){
					tempItem = getChild(tempItem, temp, entry.getValue());
				}
				tempItem.putProp(PROP_PUBLISH, entry.getValue().get(PROP_PUBLISH));
				if (includeChild){
					for (Item chileItem : tempItem.getItems()){
						setChildProp(chileItem, entry.getValue().get(PROP_PUBLISH));
					}
				}
			} else if (dir.length == 1){
				Item fileItem = item.getChild(dir[0]);
				
				if (fileItem == null){
					fileItem = createItem(dir[0], entry.getValue());
					List<Item> l = item.getItems();
					l.add(fileItem);
					item.setItems(l);
				}
				
				fileItem.putProp(PROP_PUBLISH, entry.getValue().get(PROP_PUBLISH));
				if (includeChild){
					for (Item chileItem : fileItem.getItems()){
						setChildProp(chileItem, entry.getValue().get(PROP_PUBLISH));
					}
				}
			}
		}
		setPublishCfgItem(item);
	}
	
	private Map<String, Map<String, String>> fullJDBCDriverDetail(Map<String, Map<String, String>> srcmap) throws Exception{
		Map<String, Map<String, String>> ret = new HashMap<String, Map<String,String>>();
		ret.putAll(srcmap);
		
		String jdbcDirPath = trinitySys.getDir().getData() + File.separator;
		for (Map.Entry<String, Map<String, String>> entry : srcmap.entrySet()){
			String driverName = entry.getKey();
			File folder = new File(jdbcDirPath, driverName);
			File[] files = folder.listFiles();
			if(null == files)
				continue;
				
			for (File file : files){
				Map<String, String> folderPropMap = entry.getValue();
				Map<String, String> filePropMap = new HashMap<String, String>();
				
				filePropMap.putAll(folderPropMap);
				
				String md5 = FileDetailUtil.getFileMD5(file);
				filePropMap.put(PROP_MD5, md5);

				ret.put(entry.getKey() + File.separator + file.getName(), filePropMap);
			}
		}
		
		return ret;
	}
	
	private void setChildProp(Item item, String value) throws Exception{
		item.putProp(PROP_PUBLISH, value);
		for (Item chileItem : item.getItems()){
			setChildProp(chileItem, value);
		}
	}
	
	private Item getChild(Item parentItem, String name, Map<String, String> map) throws Exception{
		Item childItem = parentItem.getChild(name);
		if (childItem == null){
			childItem = createItem(name, map);
			List<Item> l = parentItem.getItems();
			l.add(childItem);
			parentItem.setItems(l);
		} 
		return childItem;
	}
	
	private Item createItem(String name, Map<String, String> map) throws Exception{
		Item item = new Item();
		item.setName(name);
		if (name.indexOf('.') != -1){
			item.setType(TYPE_FILE);
			item.putProp(PROP_MD5, map.get(PROP_MD5));
		} else {
			item.setType(TYPE_FOLDER);
		}
		return item;
	}
}
