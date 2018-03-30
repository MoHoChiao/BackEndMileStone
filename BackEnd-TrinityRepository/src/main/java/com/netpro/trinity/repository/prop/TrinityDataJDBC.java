package com.netpro.trinity.repository.prop;

import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="trinity-data-jdbc")
@RefreshScope
public class TrinityDataJDBC
{
	@Value("${trinity-data-jdbc}")
	private Map<String, JDBCDriverInfo> info = new TreeMap<String, JDBCDriverInfo>();
	    
    public void setInfo(Map<String, JDBCDriverInfo> info) {
		this.info = info;
	}
	public Map<String, JDBCDriverInfo> getInfo() {
		return info;
	}
	
	public static class JDBCDriverInfo {
		private String jar;
        private String name;
        private String driver;
        private String url;
        private String owner;
        
        public String getJar() {
			return jar;
		}
		public void setJar(String jar) {
			this.jar = jar;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getDriver() {
			return driver;
		}
		public void setDriver(String driver) {
			this.driver = driver;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getOwner() {
			return owner;
		}
		public void setOwner(String owner) {
			this.owner = owner;
		}
    }
}