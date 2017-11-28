package com.netpro.trinity.repository.jpa.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.repository.jpa.entity.Accessright;
import com.netpro.trinity.repository.jpa.entity.AccessrightPKs;

@Repository  //宣告這是一個DAO類別
public interface AccessrightJPADao extends JpaRepository<Accessright, AccessrightPKs> {  //自動繼承JapRepository下的所有方法
	
	Long deleteByPeopleuid(String peopleuid);
	Long deleteByObjectuid(String objectuid);
	
}