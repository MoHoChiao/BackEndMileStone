package com.netpro.trinity.repository.agent.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.repository.agent.entity.JCSAgent;

@Repository  //宣告這是一個DAO類別
public interface JCSAgentJPADao extends JpaRepository<JCSAgent, String> {
	@Query("select count(agent)>0 from JCSAgent agent where agent.agentname=:agentname AND 1=1")
	Boolean existByName(@Param("agentname") String agentname);
	
	@Query("select agentname from JCSAgent")
	List<String> findAllAgentNames();
	
	@Query("select agentuid from JCSAgent")
	List<String> findAllAgentUids();
	
	//agentname field
	List<JCSAgent> findByagentname(String name);
	List<JCSAgent> findByagentname(String name, Sort sort);
	Page<JCSAgent> findByagentname(String name, Pageable pageable);
	List<JCSAgent> findByagentnameIgnoreCase(String name);
	List<JCSAgent> findByagentnameIgnoreCase(String name, Sort sort);
	Page<JCSAgent> findByagentnameIgnoreCase(String name, Pageable pageable);
	List<JCSAgent> findByagentnameLike(String name);
	List<JCSAgent> findByagentnameLike(String name, Sort sort);
	Page<JCSAgent> findByagentnameLike(String name, Pageable pageable);
	List<JCSAgent> findByagentnameLikeIgnoreCase(String name);
	List<JCSAgent> findByagentnameLikeIgnoreCase(String name, Sort sort);
	Page<JCSAgent> findByagentnameLikeIgnoreCase(String name, Pageable pageable);
	
	//activate field
	List<JCSAgent> findByactivate(String activate);
	List<JCSAgent> findByactivate(String activate, Sort sort);
	Page<JCSAgent> findByactivate(String activate, Pageable pageable);
	List<JCSAgent> findByactivateIgnoreCase(String activate);
	List<JCSAgent> findByactivateIgnoreCase(String activate, Sort sort);
	Page<JCSAgent> findByactivateIgnoreCase(String activate, Pageable pageable);
	List<JCSAgent> findByactivateLike(String activate);
	List<JCSAgent> findByactivateLike(String activate, Sort sort);
	Page<JCSAgent> findByactivateLike(String activate, Pageable pageable);
	List<JCSAgent> findByactivateLikeIgnoreCase(String activate);
	List<JCSAgent> findByactivateLikeIgnoreCase(String activate, Sort sort);
	Page<JCSAgent> findByactivateLikeIgnoreCase(String activate, Pageable pageable);
	
	//host field
	List<JCSAgent> findByhost(String host);
	List<JCSAgent> findByhost(String host, Sort sort);
	Page<JCSAgent> findByhost(String host, Pageable pageable);
	List<JCSAgent> findByhostIgnoreCase(String host);
	List<JCSAgent> findByhostIgnoreCase(String host, Sort sort);
	Page<JCSAgent> findByhostIgnoreCase(String host, Pageable pageable);
	List<JCSAgent> findByhostLike(String host);
	List<JCSAgent> findByhostLike(String host, Sort sort);
	Page<JCSAgent> findByhostLike(String host, Pageable pageable);
	List<JCSAgent> findByhostLikeIgnoreCase(String host);
	List<JCSAgent> findByhostLikeIgnoreCase(String host, Sort sort);
	Page<JCSAgent> findByhostLikeIgnoreCase(String host, Pageable pageable);
	
	//port field
	List<JCSAgent> findByport(Integer port);
	List<JCSAgent> findByport(Integer port, Sort sort);
	Page<JCSAgent> findByport(Integer port, Pageable pageable);
	
	//description field
	List<JCSAgent> findBydescription(String description);
	List<JCSAgent> findBydescription(String description, Sort sort);
	Page<JCSAgent> findBydescription(String description, Pageable pageable);
	List<JCSAgent> findBydescriptionIgnoreCase(String description);
	List<JCSAgent> findBydescriptionIgnoreCase(String description, Sort sort);
	Page<JCSAgent> findBydescriptionIgnoreCase(String description, Pageable pageable);
	List<JCSAgent> findBydescriptionLike(String description);
	List<JCSAgent> findBydescriptionLike(String description, Sort sort);
	Page<JCSAgent> findBydescriptionLike(String description, Pageable pageable);
	List<JCSAgent> findBydescriptionLikeIgnoreCase(String description);
	List<JCSAgent> findBydescriptionLikeIgnoreCase(String description, Sort sort);
	Page<JCSAgent> findBydescriptionLikeIgnoreCase(String description, Pageable pageable);
}