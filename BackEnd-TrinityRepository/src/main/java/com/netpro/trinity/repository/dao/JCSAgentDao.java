package com.netpro.trinity.repository.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.repository.entity.JCSAgent;

@Repository  //宣告這是一個DAO類別
public interface JCSAgentDao extends JpaRepository<JCSAgent, String> {
	List<JCSAgent> findByAgentname(String name);
}