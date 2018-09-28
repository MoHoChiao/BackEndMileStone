package com.netpro.trinity.resource.admin.externalrule.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.netpro.trinity.resource.admin.externalrule.entity.Resdoc;
import com.netpro.trinity.resource.admin.externalrule.entity.ResdocPKs;

@Repository  //宣告這是一個DAO類別
public interface ResdocJPADao extends JpaRepository<Resdoc, ResdocPKs> {  //自動繼承JapRepository下的所有方法
	@Transactional
	@Modifying
	@Query("UPDATE resdoc doc SET doc.resname=:newResName WHERE doc.module=:module AND doc.resname=:resname AND doc.langcode=:langcode")
	public Integer updateResNameOnly(@Param("newResName") String newResName, @Param("module") String module, 
			@Param("resname") String resname, @Param("langcode") String langcode);
	
	@Transactional
	List<Resdoc> deleteByModule(String module);
	@Transactional
	List<Resdoc> deleteByResname(String resName);
}