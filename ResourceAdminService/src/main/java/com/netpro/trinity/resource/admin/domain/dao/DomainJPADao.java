package com.netpro.trinity.resource.admin.domain.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.resource.admin.domain.entity.Domain;

@Repository  //宣告這是一個DAO類別
public interface DomainJPADao extends JpaRepository<Domain, String> {
	@Query("select case when count(d)>0 then true else false end from domain d where d.name=:name AND 1=1")
	Boolean existByName(@Param("name") String name);
	
	List<Domain> findByNameLikeIgnoreCase(String name);
	List<Domain> findByNameLikeIgnoreCase(String name, Sort sort);
	Page<Domain> findByNameLikeIgnoreCase(String name, Pageable pageable);
}