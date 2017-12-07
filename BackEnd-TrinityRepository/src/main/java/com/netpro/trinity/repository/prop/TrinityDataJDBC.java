package com.netpro.trinity.repository.prop;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="trinity-data-jdbc")
public class TrinityDataJDBC
{
    List<JDBCDriverInfo> info = new ArrayList<JDBCDriverInfo>();
    
    public void setInfo(List<JDBCDriverInfo> info) {
		this.info = info;
	}
	public List<JDBCDriverInfo> getInfo() {
		return info;
	}
    
	public static class JDBCDriverInfo {
        private String name;
        private String driver;
        private String url;
        private String owner;
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