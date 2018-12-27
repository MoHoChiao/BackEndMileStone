package com.netpro.trinity.resource.admin.agent.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.resource.admin.agent.entity.VRAgent;

@Repository  //宣告這是一個DAO類別
public interface VRAgentJPADao extends JpaRepository<VRAgent, String> {
	@Query("select case when count(vr)>0 then true else false end from jcsvirtualagent vr where vr.virtualagentname=:virtualagentname AND 1=1")
	Boolean existByName(@Param("virtualagentname") String virtualagentname);
	
	List<VRAgent> findByVirtualagentnameLikeIgnoreCase(String name);
	List<VRAgent> findByVirtualagentnameLikeIgnoreCase(String name, Sort sort);
	Page<VRAgent> findByVirtualagentnameLikeIgnoreCase(String name, Pageable pageable);
}