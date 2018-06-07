package com.netpro.trinity.repository.configuration.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.netpro.trinity.repository.configuration.dao.TrinityconfigJPADao;
import com.netpro.trinity.repository.configuration.entity.Trinityconfig;
import com.netpro.trinity.repository.util.Crypto;

@Service
public class TrinityconfigService {
	@Autowired
	private TrinityconfigJPADao dao;
	
	@Value("${encrypt.key}")
	private String encryptKey;
	
	public List<Trinityconfig> getAll() throws Exception{
		List<Trinityconfig> configs = this.dao.findAll();
		decryptSMTPPassword(configs);
		return configs;
	}
	
	public Trinityconfig getByUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("Trinity Config UID can not be empty!");
		
		Trinityconfig config = this.dao.findOne(uid);
		if(config == null)
			throw new IllegalArgumentException("Trinity Config UID does not exist!(" + uid + ")");

		decryptSMTPPassword(config);
		
		return config;
	}
	
	public Trinityconfig edit(Trinityconfig config) throws IllegalArgumentException, Exception{
		String uid = config.getVersionid();
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("Trinity Config Uid can not be empty!");

		Trinityconfig old_config = this.dao.findOne(uid);
		if(null == old_config)
			throw new IllegalArgumentException("Trinity Config Uid does not exist!(" + uid + ")");
		
		String dualservermode = config.getDualservermode();
		if(null == dualservermode || (!dualservermode.equals("1") && !dualservermode.equals("0")))
			throw new IllegalArgumentException("Dual Server Mode can only be 1 or 0!");
		
		if(null == config.getPrimaryhost())
			config.setPrimaryhost("");
		
		if("localhost".equals(config.getPrimaryhost().toLowerCase()))
			config.setPrimaryhost("127.0.0.1");
		
		if(null == config.getPrimaryport() || config.getPrimaryport() < 0)
			config.setPrimaryport(0);
		
		if(null == config.getStandbyhost())
			config.setStandbyhost("");
		
		if("localhost".equals(config.getStandbyhost().toLowerCase()))
			config.setStandbyhost("127.0.0.1");
		
		if(null == config.getStandbyport() || config.getStandbyport() < 0)
			config.setStandbyport(0);;
		
		if(null == config.getSmtpserver())
			config.setSmtpserver("");
		
		if(null == config.getSmtpuser())
			config.setSmtpuser("");
		
		if(null == config.getSmtppassword()) {
			config.setSmtppassword("");
		}else {
			config.setSmtppassword(Crypto.getEncryptString(config.getSmtppassword(), encryptKey));
		}
		
		String authmode = config.getAuthmode();
		if(null == authmode || (!authmode.equals("1") && !authmode.equals("0")))
			throw new IllegalArgumentException("Auth Mode can only be 1 or 0!");
		
		if(null == config.getLdapserver())
			config.setLdapserver("");
		
		if(null == config.getDefaultdomain())
			config.setDefaultdomain("");
		
		//拿掉MetaAutoCompare功能, 但要維持相容性
		config.setMetaautocompare("0");
		config.setMetacomparetime(0);
		config.setMetadocumenttype("000");
		config.setMetacomparekeepperiod(0);
		config.setPwdenforce("0");
		config.setPwdlevel("0");
		config.setPwdminlen(0);
		config.setPwdcycle(0);
		config.setPwdexpireday(0);
		
		/*
		 * because lastupdatetime column is auto created value, it can not be reload new value.
		 * here, we force to give value to lastupdatetime column.
		 */
		config.setLastupdatetime(new Date());
						
		return this.dao.save(config);
	}
	
	public boolean existByUid(String uid) throws Exception {
		return this.dao.exists(uid);
	}
	
	private void decryptSMTPPassword(List<Trinityconfig> configs) throws Exception {
		for(Trinityconfig config : configs) {
			decryptSMTPPassword(config);
		}
	}
	
	private void decryptSMTPPassword(Trinityconfig config) throws Exception {
		config.setSmtppassword(Crypto.getDecryptString(config.getSmtppassword(), encryptKey));
	}
}
