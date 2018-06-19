package com.netpro.trinity.repository.drivermanager.lib;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.netpro.trinity.repository.drivermanager.dto.DriverInfo;
import com.netpro.trinity.repository.prop.dto.TrinityDataJDBC;

@Component
public class MetadataDriverManager {

	public static final String MSACCESS = "MSAccess";
	
	private static Map<String, Driver> driverMp = null;
	private static Map<String, String> nameMp 	= null;
	
	@Autowired
	private TrinityDataJDBC jdbcInfo;
	
	@Autowired
	private DriverLoader driverLoader;
	
	public final boolean hasLoad(String target) throws Exception {
		if (driverMp != null && !driverMp.isEmpty()) {
			if (driverMp.containsKey(target)) {
				return true;
			}
		}
		return false;
	}
	
	public final void forName(String driver)
			throws ClassNotFoundException {
		if (nameMp != null && !nameMp.isEmpty()) {
			if (!nameMp.containsValue(driver)) {
				throw new ClassNotFoundException("Can not find the driver.");
			}
		}
//		throw new ClassNotFoundException("Can not find the driver.");
	}
	
	/**
	 * Attempts to establish a connection to the given database URL.
	 * 
	 * @param url
	 * @param user
	 * @param password
	 * @return Connection
	 * @throws SQLException
	 */
	public final Connection getConnection(String url, String user,
			String password) throws SQLException {
		DriverManager.setLoginTimeout(5);
		return DriverManager.getConnection(url, user, password);
	}
	
	public final Connection getConnection(String url, Properties prop) throws SQLException {
		return DriverManager.getConnection(url, prop);
	}
	
	/**
	 * According assign target, attempts to establish a connection to the given database URL.
	 * 
	 * @param target
	 * @param url
	 * @param user
	 * @param password
	 * @return Connection
	 * @throws SQLException
	 */
	public final Connection getConnection(String target, String url,
			String user, String password) throws SQLException {
		if (driverMp != null && !driverMp.isEmpty() && target != null
				&& target.length() != 0) {
			DriverManager.setLoginTimeout(5);
			
			Properties info = new Properties();

			if (user != null) {
				info.put("user", user);
			}
			if (password != null) {
				info.put("password", password);
			}

			if(target.toLowerCase().indexOf("oracle") != -1){
				info.put("includeSynonyms", true);
			}
			
			Driver d = driverMp.get(target);

			return d.connect(url, info);
		}

		return null;
	}
	
	protected void load() throws Exception {
		for (String target : this.jdbcInfo.getInfo().keySet()) {
			load(target);
		}
	}
	
	protected void load(String target) throws Exception {		
		DriverVO dvo = getDriverInfo(target);
		Driver d = driverLoader.getDriver(dvo);
		
		if (d != null) {
			Driver instance = new DriverShim(d);
			registerDriver(instance);
			driverMp.put(target, instance);
			nameMp.put(target, dvo.getDriver());
		}
		for(String name : nameMp.keySet()) {
			System.out.println(name+":"+nameMp.get(name));
		}
		System.out.println("_____________________________________________________");
	}
	
	protected void unload() throws Exception {
		if (driverMp != null && !driverMp.isEmpty()) {
			deregisterDriver(driverMp);
		}
	}
	
	protected void unload(String target) throws Exception {
		if (driverMp != null && !driverMp.isEmpty()) {
			if (driverMp.containsKey(target)) {
				deregisterDriver(driverMp.remove(target));
				nameMp.remove(target);
			}
		}
	}
	
	/**
	 * Registers the given driver with the DriverManager.
	 * 
	 * @param driver
	 */
	private void registerDriver(Driver driver) throws Exception {
		DriverManager.registerDriver(driver);
	}

	private void deregisterDriver(Map<String, Driver> driverMp) throws Exception {
		for (Driver d : driverMp.values()) {
			deregisterDriver(d);
		}		
	}
	
	/**
	 * Drops a driver from the DriverManager's list.
	 * 
	 * @param driver
	 */
	private void deregisterDriver(Driver driver) throws Exception {
		DriverManager.deregisterDriver(driver);
	}
	
	/**
	 * Get the target driver information.
	 * 
	 * @param target
	 * @return DriverVO
	 */
	private DriverVO getDriverInfo(String target) throws Exception{
		Map<String, DriverInfo> infoMap = this.jdbcInfo.getInfo();
		DriverInfo info = infoMap.get(target);
		
		DriverVO dvo = new DriverVO();
		String jarname	= info.getJar();
		String driver 	= info.getDriver();
		String url 		= info.getUrl();
				
		dvo.setTarget(target);
		dvo.setJarname(jarname);
		dvo.setDriver(driver);
		dvo.setUrl(url);
		
		return dvo;
	}
	
	static {
		if (driverMp == null) {
			driverMp = new HashMap<String, Driver>();
		}
		if (nameMp == null) {
			nameMp = new HashMap<String, String>();
		}
	}
	
}

