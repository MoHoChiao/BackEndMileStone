package com.netpro.trinity.repository.dao.jpa.resdoc;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.netpro.trinity.repository.entity.resdoc.jpa.Resdoc;
import com.netpro.trinity.repository.entity.resdoc.jpa.ResdocPKs;

@Repository  //宣告這是一個DAO類別
public interface ResdocJPADao extends JpaRepository<Resdoc, ResdocPKs> {  //自動繼承JapRepository下的所有方法
	@Transactional
	List<Resdoc> deleteByModule(String module);
	@Transactional
	List<Resdoc> deleteByResname(String resName);
}