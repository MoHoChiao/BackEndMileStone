package com.netpro.trinity.service.externalrule.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.netpro.trinity.service.externalrule.dao.ResdocJPADao;
import com.netpro.trinity.service.externalrule.entity.Resdoc;
import com.netpro.trinity.service.externalrule.entity.ResdocPKs;

@Service
public class ResdocService {
	
	@Autowired
	private ResdocJPADao dao;
	
	public List<Resdoc> getAll() throws Exception{
		return this.dao.findAll();
	}
	
	public Resdoc getByPKs(ResdocPKs pks) throws IllegalArgumentException, Exception{
		if(null == pks)
			throw new IllegalArgumentException("Resdoc pks can not be empty!");
		
		if(null == pks.getModule() || pks.getModule().trim().isEmpty())
			throw new IllegalArgumentException("Module Name can not be empty!");
		
		if(null == pks.getResname() || pks.getResname().trim().isEmpty())
			throw new IllegalArgumentException("ResName can not be empty!");
		
		if(null == pks.getLangcode() || pks.getLangcode().trim().isEmpty())
			throw new IllegalArgumentException("Language Code can not be empty!");
		
		Resdoc doc = this.dao.findById(pks).get();
		if(doc == null)
			throw new IllegalArgumentException("Resdoc pks does not exist!(" + pks.getModule() + 
					", " + pks.getResname() + ", " + pks.getResname() + ")");

		return doc;
	}
	
	public Resdoc modify(Resdoc doc) throws IllegalArgumentException, Exception{
		String module = doc.getModule();
		if(null == module || module.trim().isEmpty())
			throw new IllegalArgumentException("Module Name can not be empty!");
		
		String resname = doc.getResname();
		if(null == resname || resname.trim().isEmpty())
			throw new IllegalArgumentException("ResName can not be empty!");
		
		String langcode = doc.getLangcode();
		if(null == langcode || langcode.trim().isEmpty())
			throw new IllegalArgumentException("Language Code can not be empty!");
		
		if(null == doc.getDocument())
			doc.setDocument("");
		
		return this.dao.save(doc);
	}
	
	public Integer editResNameOnly(String newResName, Resdoc doc) throws IllegalArgumentException, Exception{
		if(null == newResName || newResName.trim().isEmpty())
			throw new IllegalArgumentException("New ResName can not be empty!");
		
		String module = doc.getModule();
		if(null == module || module.trim().isEmpty())
			throw new IllegalArgumentException("Module Name can not be empty!");
		
		String resname = doc.getResname();
		if(null == resname || resname.trim().isEmpty())
			throw new IllegalArgumentException("ResName can not be empty!");
		
		String langcode = doc.getLangcode();
		if(null == langcode || langcode.trim().isEmpty())
			throw new IllegalArgumentException("Language Code can not be empty!");
		
		return this.dao.updateResNameOnly(newResName, module, resname, langcode);
	}
	
	public void deleteByPKs(ResdocPKs pks) throws IllegalArgumentException, Exception{
		if(null == pks)
			throw new IllegalArgumentException("Resdoc pks can not be empty!");
		
		if(null == pks.getModule() || pks.getModule().trim().isEmpty())
			throw new IllegalArgumentException("Module Name can not be empty!");
		
		if(null == pks.getResname() || pks.getResname().trim().isEmpty())
			throw new IllegalArgumentException("ResName can not be empty!");
		
		if(null == pks.getLangcode() || pks.getLangcode().trim().isEmpty())
			throw new IllegalArgumentException("Language Code can not be empty!");
		
		this.dao.deleteById(pks);
	}
	
	public void deleteByModule(String module) throws IllegalArgumentException, Exception{
		if(null == module || module.trim().isEmpty())
			throw new IllegalArgumentException("Module Name can not be empty!");
		
		try {
			this.dao.deleteByModule(module);
		}catch(EmptyResultDataAccessException e) {}
	}
	
	public void deleteByName(String resName) throws IllegalArgumentException, Exception{
		if(null == resName || resName.trim().isEmpty())
			throw new IllegalArgumentException("ResName can not be empty!");
		
		try {
			this.dao.deleteByResname(resName);
		}catch(EmptyResultDataAccessException e) {}
	}
}
