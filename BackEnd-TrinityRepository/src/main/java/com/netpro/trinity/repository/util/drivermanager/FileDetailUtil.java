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
import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

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
	
	public static String getNodeAttByName(Node node, String attr) throws Exception{
		NamedNodeMap nnm = node.getAttributes();
		for (int i = 0 ; i < nnm.getLength() ; i++){
			Node na = nnm.item(i);
			if (na.getNodeName().equals(attr))
				return na.getNodeValue();
		}
		return "";
	}
}

