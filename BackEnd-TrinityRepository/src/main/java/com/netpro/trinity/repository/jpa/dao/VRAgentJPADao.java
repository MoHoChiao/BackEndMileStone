package com.netpro.trinity.repository.jpa.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.repository.jpa.entity.VRAgent;

@Repository  //宣告這是一個DAO類別
public interface VRAgentJPADao extends JpaRepository<VRAgent, String> {
	@Query("select count(vr)>0 from jcsvirtualagent vr where vr.virtualagentname=:virtualagentname AND 1=1")
	Boolean existByName(@Param("virtualagentname") String virtualagentname);
	
	//vr-agentname field
	List<VRAgent> findByvirtualagentname(String name);
	List<VRAgent> findByvirtualagentname(String name, Sort sort);
	Page<VRAgent> findByvirtualagentname(String name, Pageable pageable);
	List<VRAgent> findByvirtualagentnameIgnoreCase(String name);
	List<VRAgent> findByvirtualagentnameIgnoreCase(String name, Sort sort);
	Page<VRAgent> findByvirtualagentnameIgnoreCase(String name, Pageable pageable);
	List<VRAgent> findByvirtualagentnameLike(String name);
	List<VRAgent> findByvirtualagentnameLike(String name, Sort sort);
	Page<VRAgent> findByvirtualagentnameLike(String name, Pageable pageable);
	List<VRAgent> findByvirtualagentnameLikeIgnoreCase(String name);
	List<VRAgent> findByvirtualagentnameLikeIgnoreCase(String name, Sort sort);
	Page<VRAgent> findByvirtualagentnameLikeIgnoreCase(String name, Pageable pageable);
	
	//activate field
	List<VRAgent> findByactivate(String activate);
	List<VRAgent> findByactivate(String activate, Sort sort);
	Page<VRAgent> findByactivate(String activate, Pageable pageable);
	List<VRAgent> findByactivateIgnoreCase(String activate);
	List<VRAgent> findByactivateIgnoreCase(String activate, Sort sort);
	Page<VRAgent> findByactivateIgnoreCase(String activate, Pageable pageable);
	List<VRAgent> findByactivateLike(String activate);
	List<VRAgent> findByactivateLike(String activate, Sort sort);
	Page<VRAgent> findByactivateLike(String activate, Pageable pageable);
	List<VRAgent> findByactivateLikeIgnoreCase(String activate);
	List<VRAgent> findByactivateLikeIgnoreCase(String activate, Sort sort);
	Page<VRAgent> findByactivateLikeIgnoreCase(String activate, Pageable pageable);
	
	//description field
	List<VRAgent> findBydescription(String description);
	List<VRAgent> findBydescription(String description, Sort sort);
	Page<VRAgent> findBydescription(String description, Pageable pageable);
	List<VRAgent> findBydescriptionIgnoreCase(String description);
	List<VRAgent> findBydescriptionIgnoreCase(String description, Sort sort);
	Page<VRAgent> findBydescriptionIgnoreCase(String description, Pageable pageable);
	List<VRAgent> findBydescriptionLike(String description);
	List<VRAgent> findBydescriptionLike(String description, Sort sort);
	Page<VRAgent> findBydescriptionLike(String description, Pageable pageable);
	List<VRAgent> findBydescriptionLikeIgnoreCase(String description);
	List<VRAgent> findBydescriptionLikeIgnoreCase(String description, Sort sort);
	Page<VRAgent> findBydescriptionLikeIgnoreCase(String description, Pageable pageable);
	
	//mode field
	List<VRAgent> findBymode(String mode);
	List<VRAgent> findBymode(String mode, Sort sort);
	Page<VRAgent> findBymode(String mode, Pageable pageable);
	List<VRAgent> findBymodeIgnoreCase(String mode);
	List<VRAgent> findBymodeIgnoreCase(String mode, Sort sort);
	Page<VRAgent> findBymodeIgnoreCase(String mode, Pageable pageable);
	List<VRAgent> findBymodeLike(String mode);
	List<VRAgent> findBymodeLike(String mode, Sort sort);
	Page<VRAgent> findBymodeLike(String mode, Pageable pageable);
	List<VRAgent> findBymodeLikeIgnoreCase(String mode);
	List<VRAgent> findBymodeLikeIgnoreCase(String mode, Sort sort);
	Page<VRAgent> findBymodeLikeIgnoreCase(String mode, Pageable pageable);
}