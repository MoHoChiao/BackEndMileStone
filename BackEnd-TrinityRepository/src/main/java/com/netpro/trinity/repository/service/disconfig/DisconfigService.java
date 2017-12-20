package com.netpro.trinity.repository.service.disconfig;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netpro.trinity.repository.dao.jpa.disconfig.DisconfigJPADao;
import com.netpro.trinity.repository.entity.disconfig.jpa.Disconfig;
import com.netpro.trinity.repository.entity.disconfig.jpa.DisconfigPKs;

@Service
public class DisconfigService {
	@Autowired
	private DisconfigJPADao dao;
	
	public Disconfig getByUid(DisconfigPKs pks) throws IllegalArgumentException, Exception{
		if(pks == null || pks.getModule().isEmpty() || pks.getConfigname().isEmpty())
			throw new IllegalArgumentException("Disconfig pks can not be empty!");
		
		Disconfig config = this.dao.findOne(pks);
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
