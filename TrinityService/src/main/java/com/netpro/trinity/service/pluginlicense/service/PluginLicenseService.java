package com.netpro.trinity.service.pluginlicense.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.netpro.license.function.MethodInterface;
import com.netpro.trinity.service.pluginlicense.dao.PluginLicenseJPADao;
import com.netpro.trinity.service.pluginlicense.entity.Plugin;
import com.netpro.trinity.service.util.Crypto;

@Service
public class PluginLicenseService {
	private static final Logger LOGGER = LoggerFactory.getLogger(PluginLicenseService.class);
	
	@Autowired
	private PluginLicenseJPADao dao;
	
	public List<Plugin> getAll() throws Exception {
		return this.dao.findAll();
	}
	
	public List<Plugin> getLicenseStatus() throws Exception {
		// method.mds(protectKey) + method.md(licenseJar) and Crypto.encrypt() it 
		String eKey = "6A63F0D2EB042B7BE2F788CEB298EE990CBF8F4D22D6B070CE45DA46F10ED683653A2448ABA7283A275785518BE6A8ACA7B258D7EB11C8867068F666637640B931648FCFF7CFBFEF794BC994B5C3E654";
		
		Class<?> cl = Class.forName("com.netpro.license.function.Method");
		MethodInterface mi = (MethodInterface)cl.newInstance();
		
		List<Plugin> pluginListNew = new ArrayList<Plugin>();
		List<Plugin> pluginList = this.dao.findAll();
		Plugin licenseStatus;
		boolean status;
		String eDate;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		for (Plugin p : pluginList) {
			status = true;
			eDate = "--";
			
			if (p.getPluginlicense() != null) {
				String licenseKey =  p.getPluginlicense().getLicensekey();
				
				if (licenseKey != null && licenseKey.length() > 10) {
					String keyInfo = mi.dd(Crypto.decrypt(eKey, "trinity"), licenseKey);
					eDate = keyInfo.substring(keyInfo.indexOf("exp=") + 4, keyInfo.indexOf("exp=") + 14);
					Date exp = sdf.parse(eDate);

					if (exp.compareTo(new Date()) < 1) {
						status = false;
					}
				}
			}
			
			licenseStatus = new Plugin();
			licenseStatus.setPluginname(p.getPluginname());
			licenseStatus.setExpireddate(eDate);
			licenseStatus.setStatus(status);
			
			pluginListNew.add(licenseStatus);
		}
		
		return pluginListNew;
	}
	
	
	public String importPluginLicense(MultipartFile file) throws Exception {
		StringBuffer sb = new StringBuffer();
		InputStream is = new ByteArrayInputStream(file.getBytes());
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String line;
		
		while ((line = br.readLine()) != null) {
			sb.append(line).append("\n");
		}
		
		return sb.toString();
	}
}
