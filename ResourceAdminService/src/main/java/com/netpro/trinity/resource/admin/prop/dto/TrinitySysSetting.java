package com.netpro.trinity.resource.admin.prop.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="trinity-sys-setting")
public class TrinitySysSetting
{
	Dir dir = new Dir();
    
	public void setDir(Dir dir) {
		this.dir = dir;
	}
	public Dir getDir() {
		return dir;
	}
	
	
	public static class Dir {
        private String home;
        private String config;
        private String software;
        private String jdbc;
        private String log;
        private String comparereport;
        private String extlib;
        private String data;
        
		public String getHome() {
			return home;
		}
		public void setHome(String home) {
			this.home = home;
		}
		public String getConfig() {
			return config;
		}
		public void setConfig(String config) {
			this.config = config;
		}
		public String getSoftware() {
			return software;
		}
		public void setSoftware(String software) {
			this.software = software;
		}
		public String getJdbc() {
			return jdbc;
		}
		public void setJdbc(String jdbc) {
			this.jdbc = jdbc;
		}
		public String getLog() {
			return log;
		}
		public void setLog(String log) {
			this.log = log;
		}
		public String getComparereport() {
			return comparereport;
		}
		public void setComparereport(String comparereport) {
			this.comparereport = comparereport;
		}
		public String getExtlib() {
			return extlib;
		}
		public void setExtlib(String extlib) {
			this.extlib = extlib;
		}
		public String getData() {
			return data;
		}
		public void setData(String data) {
			this.data = data;
		}
    }
}