package com.netpro.trinity.resource.admin.agent.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.resource.admin.agent.entity.JCSAgent;

@Repository  //宣告這是一個DAO類別
public interface JCSAgentJPADao extends JpaRepository<JCSAgent, String> {
	@Query("select case when count(a)>0 then true else false end from JCSAgent a where a.agentname=:agentname AND 1=1")
	Boolean existByName(@Param("agentname") String agentname);
	
	@Query("select a.agentname from JCSAgent a")
	List<String> findAllAgentNames();
	
	@Query("select a.agentuid from JCSAgent a")
	List<String> findAllAgentUids();
	
//	@Query("select agent.agentuid, agent.agentname, agent.description, agent.host, agent.lastupdatetime from JCSAgent agent "
//			+ "where agent.agentname like %:param% "
//			+ "or agent.host like %:param%")
//	Page<JCSAgent> findAgentsProfileByFilter(@Param("param") String param, Pageable pageable);
	
	List<JCSAgent> findByAgentnameLikeIgnoreCaseOrHostLikeIgnoreCase(String name, String host);
	List<JCSAgent> findByAgentnameLikeIgnoreCaseOrHostLikeIgnoreCase(String name, String host, Sort sort);
	Page<JCSAgent> findByAgentnameLikeIgnoreCaseOrHostLikeIgnoreCase(String name, String host, Pageable pageable);
}