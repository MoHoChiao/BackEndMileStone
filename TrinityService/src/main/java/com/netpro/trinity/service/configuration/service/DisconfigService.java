package com.netpro.trinity.service.configuration.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netpro.trinity.service.configuration.dao.DisconfigJPADao;
import com.netpro.trinity.service.configuration.entity.Disconfig;
import com.netpro.trinity.service.configuration.entity.DisconfigPKs;

@Service
public class DisconfigService {
	@Autowired
	private DisconfigJPADao dao;
	
	public Disconfig getByUid(DisconfigPKs pks) throws IllegalArgumentException, Exception{
		if(null == pks)
			throw new IllegalArgumentException("Disconfig pks can not be empty!");
		
		if(null == pks.getModule() || pks.getModule().trim().isEmpty())
			throw new IllegalArgumentException("Module Name can not be empty!");
		
		if(null == pks.getConfigname() || pks.getConfigname().trim().isEmpty())
			throw new IllegalArgumentException("Config Name can not be empty!");
		
		Disconfig config = null;
		try {
			config = this.dao.findById(pks).get();
		}catch(NoSuchElementException e) {}
		 
		if(config == null)
			throw new IllegalArgumentException("Disconfig pks does not exist!(" + pks.getModule() + ", " + pks.getConfigname() + ")");

		return config;
	}
	
	public List<Disconfig> getUiapPosition(String module1, String module2, String configname1, String configname2) throws Exception{
		return this.dao.findUiapPosition(module1, module2, configname1, configname2);
	}
	
	public List<Disconfig> getByModule(String module) throws IllegalArgumentException, Exception{
		if(module == null || module.isEmpty())
			throw new IllegalArgumentException("Module Name can not be empty!");
		
		return this.dao.findByModuleOrderByConfigname(module);
	}
}
