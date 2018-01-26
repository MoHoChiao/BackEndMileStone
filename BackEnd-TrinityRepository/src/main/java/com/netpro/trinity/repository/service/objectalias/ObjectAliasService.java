package com.netpro.trinity.repository.service.objectalias;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netpro.trinity.repository.dao.jpa.objectalias.ObjectAliasJPADao;

@Service
public class ObjectAliasService {
	@Autowired
	private ObjectAliasJPADao dao;
	
	public boolean existByObjectuid(String objectuid) throws IllegalArgumentException, Exception {
		if(null == objectuid || objectuid.trim().length() <= 0)
			throw new IllegalArgumentException("Object Uid can not be empty!");
		
		return this.dao.existByObjectuid(objectuid);
	}
}
