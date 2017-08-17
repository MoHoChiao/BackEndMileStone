package com.netpro.trinity.config.service.config.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="trinity")
public class TrinityAppsConfig
{
    List<App> apps = new ArrayList<App>();

    public void setApps(List<App> apps) {
		this.apps = apps;
	}
	public List<App> getApps() {
		return apps;
	}
    public static class App
    {
        private String name;
        private String cls;
        private String img;
        private String alt;
        private String model;
        private List<String> desc;
        
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getCls() {
			return cls;
		}
		public void setCls(String cls) {
			this.cls = cls;
		}
		public String getImg() {
			return img;
		}
		public void setImg(String img) {
			this.img = img;
		}
		public String getAlt() {
			return alt;
		}
		public void setAlt(String alt) {
			this.alt = alt;
		}
		public String getModel() {
			return model;
		}
		public void setModel(String model) {
			this.model = model;
		}
		public List<String> getDesc() {
			return desc;
		}
		public void setDesc(List<String> desc) {
			this.desc = desc;
		}
    }
}