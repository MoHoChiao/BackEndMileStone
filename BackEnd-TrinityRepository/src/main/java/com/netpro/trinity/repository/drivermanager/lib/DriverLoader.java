package com.netpro.trinity.repository.drivermanager.lib;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.netpro.trinity.repository.prop.TrinitySysSetting;

@Component
public class DriverLoader {

	/**
	 * Dynamic loading JDBC driver.
	 * 
	 * @param dObj
	 * @return Driver
	 */
	public static String filePath;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DriverLoader.class);
	
	@Autowired
	private TrinitySysSetting trinitySys;
	
	public Driver getDriver(DriverVO dObj) throws Exception{
		if (filePath == null || filePath == ""){
			filePath = this.trinitySys.getDir().getJdbc() + File.separator;
		}
		List<JarURLConnection> urlList = new ArrayList<JarURLConnection>();
		try {
			String path = filePath + File.separator + dObj.getTarget();
			File fp = new File(path);
			if (!fp.exists()){
				return (Driver) Class.forName(dObj.getDriver()).newInstance();
			}
			File[] files = fp.listFiles();
			for (int i = 0 ; i < files.length ; i++){
				if(files[i].getName().indexOf("jar") != -1){
					URL url = new URL("jar", "", -1, new File(path, files[i].getName()).toURI().toString() + "!/");
					urlList.add((JarURLConnection)(url.openConnection()));
				}
			}
			URL[] urls = new URL[urlList.size()];
			for(int i = 0 ; i < urlList.size() ; i++){
				urls[i] = urlList.get(i).getURL();
			}
			
			URLClassLoader ucl = new URLClassLoader(urls);
			
			return (Driver) Class.forName(dObj.getDriver(), true, ucl).newInstance();

		} catch (Exception e) {
			DriverLoader.LOGGER.error("Exception; reason was:", e);
			throw e;
		} finally {
			if (urlList.size() != 0){
				for (JarURLConnection jarUrlconn: urlList){
					try {
						jarUrlconn.getJarFile().close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

}
