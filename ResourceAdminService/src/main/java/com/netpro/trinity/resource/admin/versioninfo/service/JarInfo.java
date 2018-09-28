package com.netpro.trinity.resource.admin.versioninfo.service;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * Reade Jar Manifest Info and scant Trinity Jar file;
 * 
 * 
 * @author troychen
 *
 */
public abstract class JarInfo {

	protected String trinityHome;

	public JarInfo(String trinityHome) {
		this.trinityHome = trinityHome;
	}

	/**
	 * Get Jar Manifest Attributes
	 * 
	 * @param file
	 * @return
	 */
	private Attributes getManifestAttributes(File file) {
		Attributes attributes = null;
		JarFile jarFile = null;
		try {
			jarFile = new JarFile(file);
			Manifest manifest = jarFile.getManifest();
			if (manifest != null) {
				attributes = manifest.getMainAttributes();
			}
			
		} catch (IOException e) {
			System.out.println("Error!, Reade Jar File Error." + e.getMessage());
		} finally {
			try {
				if (jarFile != null) {
					jarFile.close();
				}
			} catch (IOException e) {
				System.err.println("Close Jar file error.");
			}
		}

		return attributes;
	}

	/**
	 * Get Manifest attributer "Specification-Vendor"
	 * 
	 * @param file
	 * @return
	 */
	protected String getProvider(File file) {

		String provider = null;
		Attributes attributes = getManifestAttributes(file);
		if (attributes != null) {
			provider = attributes.getValue("Specification-Vendor");
		}

		if (provider == null) {
			provider = "";
		}

		return provider;
	}

	/**
	 * Get Manifest all attribute
	 * 
	 * @param file
	 * @return
	 */
	protected Map<String, String> getManifest(File file) {
		Map<String, String> map = new HashMap<String, String>();

		Attributes attributes = getManifestAttributes(file);
		if (attributes == null) {
			return map;
		}

		Set<Object> keySet = attributes.keySet();
		for (Object key : keySet) {
			String sKey = key.toString();
			map.put(sKey, attributes.getValue(sKey));
		}

		return map;
	}

	/**
	 * Scan folder and filter not trinity library
	 * 
	 * 
	 * @param path
	 * @return
	 */
	protected Map<String, Map<String, String>> scanTrinityJar(String path) {

		File file = new File(trinityHome, path);
		File[] jarFiles = file.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {

				return (name.lastIndexOf("jar") != -1) || (name.lastIndexOf("war") != -1);
			}
		});

		Map<String, Map<String, String>> trinityJarMap = new HashMap<String, Map<String, String>>();
		if (jarFiles == null || jarFiles.length == 0) {
			return trinityJarMap;
		}
		
		for (File jar : jarFiles) {
			String fileName = jar.getName();
			String provider = getProvider(jar);

			if (fileName.indexOf("com.netpro") != -1 || "NetPro Information Service Ltd.".equals(provider)) {
				Map<String, String> attrMap = getManifest(jar);
				trinityJarMap.put(fileName, attrMap);
			}
		}

		return trinityJarMap;
	}

}
