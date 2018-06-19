package com.netpro.trinity.repository.jnlp.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netpro.ac.util.CookieUtils;
import com.netpro.ac.util.TrinityWebV2Utils;
import com.netpro.trinity.repository.configuration.entity.Disconfig;
import com.netpro.trinity.repository.prop.dto.TrinityPropSetting;
import com.netpro.trinity.repository.prop.dto.TrinityRepoSetting;
import com.netpro.trinity.repository.prop.dto.TrinitySysSetting;
import com.netpro.trinity.repository.util.Crypto;

@Service
public class JnlpService {
		
	@Autowired
	private TrinitySysSetting trinitySys;
	
	@Autowired
	private TrinityPropSetting trinityProp;
	
	@Autowired
	private TrinityRepoSetting repo;
	
	public File getSoftwareFile(String softwareName) throws Exception {	
		String separator = System.getProperty("file.separator");
		String filePath = trinitySys.getDir().getSoftware();
		
		File file = new File(filePath + separator + softwareName);
		return file;
	}
	
	public String getJFDesignerContent(HttpServletRequest req, List<Disconfig> uiapPosition) throws Exception {	
		String separator = System.getProperty("file.separator");
		String filePath = trinitySys.getDir().getSoftware();
		String host = trinityProp.getServer().getDisHost();
		String port = trinityProp.getServer().getDisPort();
		String serverURL = "http://"+host+":"+port;
		
		FileInputStream ints = new FileInputStream(filePath + separator + "JFDesigner.jnlp.template");
		File f = new File(filePath + separator + "jfdesigner.jar");
		
		Map<String,String> prop_map = new HashMap<String, String>();
		prop_map.put("\\{prevent.cache}", String.valueOf(f.lastModified()));
		prop_map.put("\\{trinity.server.url}", serverURL);
		prop_map.put("\\{context_path}", "");
		
		
		prop_map.put("\\{trinity.uiap.ip}", getServiceIP(uiapPosition));
		prop_map.put("\\{trinity.uiap.port}", getServicePort("uiap", uiapPosition));
		
		checkAccessToken(req, prop_map);
		
		return getContent(ints, prop_map);
	}
	
	public String getTaskConsoleContent(HttpServletRequest req, List<Disconfig> uiapPosition) throws Exception {	
		String separator = System.getProperty("file.separator");
		String filePath = trinitySys.getDir().getSoftware();
		String host = trinityProp.getServer().getDisHost();
		String port = trinityProp.getServer().getDisPort();
		String serverURL = "http://"+host+":"+port;
		String encryptKey = trinityProp.getEncrypt().getKey();
		String userid = repo.getDatasource().get("username");
		String password = repo.getDatasource().get("password");
		password = Crypto.decryptPassword(password, userid);
		
		FileInputStream ints = new FileInputStream(filePath + separator + "TaskConsole.jnlp.template");
		File f = new File(filePath + separator + "console.jar");
		
		Map<String,String> prop_map = new HashMap<String, String>();
		prop_map.put("\\{prevent.cache}", String.valueOf(f.lastModified()));
		prop_map.put("\\{trinity.server.url}", serverURL);
		prop_map.put("\\{context_path}", "");
		prop_map.put("\\{database.driver}", repo.getDatasource().get("driver-class-name"));
		prop_map.put("\\{database.url}", repo.getDatasource().get("url"));
		prop_map.put("\\{database.username}", userid);
		prop_map.put("\\{database.password.encrypt}", Crypto.getEncryptString(password, encryptKey));
		
		prop_map.put("\\{trinity.uiap.ip}", "uiap_ip="+getServiceIP(uiapPosition));
		prop_map.put("\\{trinity.uiap.port}", "uiap_port="+getServicePort("uiap", uiapPosition));
		
		checkAccessToken(req, prop_map);
		
		return getContent(ints, prop_map);
	}
	
	public String getMetamanContent(HttpServletRequest req, List<Disconfig> metamanPosition) throws Exception {	
		String separator = System.getProperty("file.separator");
		String filePath = trinitySys.getDir().getSoftware();
		String host = trinityProp.getServer().getDisHost();
		String port = trinityProp.getServer().getDisPort();
		String serverURL = "http://"+host+":"+port;
		
		FileInputStream ints = new FileInputStream(filePath + separator + "Metaman.jnlp.template");
		File f = new File(filePath + separator + "metaman.jar");
		
		Map<String,String> prop_map = new HashMap<String, String>();
		prop_map.put("\\{prevent.cache}", String.valueOf(f.lastModified()));
		prop_map.put("\\{trinity.server.url}", serverURL);
		prop_map.put("\\{context_path}", "");
		
		prop_map.put("\\{trinity.metaman.ip}", getServiceIP(metamanPosition));
		prop_map.put("\\{trinity.metaman.port}", getServicePort("metamanserver", metamanPosition));
		
		checkAccessToken(req, prop_map);
		
		return getContent(ints, prop_map);
	}
	
	public String getUpdaterContent(HttpServletRequest req, List<Disconfig> uiapPosition) throws Exception {	
		String separator = System.getProperty("file.separator");
		String filePath = trinitySys.getDir().getSoftware();
		String host = trinityProp.getServer().getDisHost();
		String port = trinityProp.getServer().getDisPort();
		String serverURL = "http://"+host+":"+port;
		
		FileInputStream ints = new FileInputStream(filePath + separator + "Updater.jnlp.template");
		File f = new File(filePath + separator + "updater.jar");
		
		Map<String,String> prop_map = new HashMap<String, String>();
		prop_map.put("\\{prevent.cache}", String.valueOf(f.lastModified()));
		prop_map.put("\\{trinity.server.url}", serverURL);
		prop_map.put("\\{context_path}", "");
		
		prop_map.put("\\{trinity.uiap.ip}", getServiceIP(uiapPosition));
		prop_map.put("\\{trinity.uiap.port}", getServicePort("uiap", uiapPosition));
		
		checkAccessToken(req, prop_map);
				
		return getContent(ints, prop_map);
	}
	
	private String getServiceIP(List<Disconfig> servicePosition) {
		String uiap_ip = trinityProp.getUiap().getHost();
		Disconfig dto1 = servicePosition.get(0);
		Disconfig dto2 = servicePosition.get(1);
		
		if(uiap_ip == null) {
			if("server".equals(dto1.getModule())) {
				uiap_ip = dto1.getValue();
			}else {
				uiap_ip = dto2.getValue();
			}
		}
		return uiap_ip;
	}
	
	private String getServicePort(String whichService, List<Disconfig> servicePosition) {
		String uiap_port = trinityProp.getUiap().getPort();
		Disconfig dto1 = servicePosition.get(0);
		Disconfig dto2 = servicePosition.get(1);
		
		if(uiap_port == null) {
			if(whichService.equals(dto2.getModule())) {
				uiap_port = dto2.getValue();
			}else {
				uiap_port = dto1.getValue();
			}
		}
		return uiap_port;
	}
	
	private void  checkAccessToken(HttpServletRequest req, Map<String,String> prop_map) {
		String accessToken = CookieUtils.getCookieValue(req, TrinityWebV2Utils.CNAME_ACCESS_TOKEN);
		prop_map.put("\\{trinity.sso_uuid}", null == accessToken || "".equals(accessToken) ? "none" : accessToken);
	}
	
	private String getContent(FileInputStream ints, Map<String,String> prop_map) throws Exception{
		String content="";
		
		int ca = ints.available();
		byte[] by = new byte[ca];
		ints.read(by);
		content = new String(by);
		ints.close();

		if (!prop_map.isEmpty()) {
			for (String index_pops : prop_map.keySet()) {
				String pop_key = index_pops.toString();
				String pop_val = prop_map.get(pop_key);
				
				content = content.replaceAll(pop_key, pop_val);
			}
		}
		
		return content;
	}
}
