package com.netpro.trinity.client.service.dto.prop;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="trinity-prop-setting")
public class TrinityPropSetting
{
    Server server = new Server();
    List<App> apps = new ArrayList<App>();
    Encrypt encrypt = new Encrypt();
    
	public void setServer(Server server) {
		this.server = server;
	}
	public Server getServer() {
		return server;
	}
	
	public void setApps(List<App> apps) {
		this.apps = apps;
	}
	public List<App> getApps() {
		return apps;
	}
	
	public Encrypt getEncrypt() {
		return encrypt;
	}
	public void setEncryptkey(Encrypt encrypt) {
		this.encrypt = encrypt;
	}
	
	
	public static class Server {
		private String host;
		private String port;
		
		public String getHost() {
			return host;
		}
		public void setHost(String host) {
			this.host = host;
		}
		
		public String getPort() {
			return port;
		}
		public void setPort(String port) {
			this.port = port;
		}
	}
	
	public static class Encrypt {
		private String key;

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}
	}
	
	public static class App {
        private String name;
        private String img;
        private String alt;
        private String model;
        private String url;
        private String target;
        private String authorization;
        private List<String> desc;
        
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
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
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getTarget() {
			return target;
		}
		public void setTarget(String target) {
			this.target = target;
		}
		public String getAuthorization() {
			return authorization;
		}
		public void setAuthorization(String authorization) {
			this.authorization = authorization;
		}
    }
}