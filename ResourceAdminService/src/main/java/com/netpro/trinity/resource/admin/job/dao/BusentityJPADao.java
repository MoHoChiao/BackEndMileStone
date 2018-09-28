package com.netpro.trinity.resource.admin.job.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.resource.admin.job.entity.Busentity;

@Repository  //宣告這是一個DAO類別
public interface BusentityJPADao extends JpaRepository<Busentity, String> {
	@Query("select count(entity)>0 from Busentity entity where entity.busentityname=:busentityname AND 1=1")
	Boolean existByName(@Param("busentityname") String busentityname);
	
	List<Busentity> findByBusentitynameLikeIgnoreCase(String name);
	List<Busentity> findByBusentitynameLikeIgnoreCase(String name, Sort sort);
	Page<Busentity> findByBusentitynameLikeIgnoreCase(String name, Pageable pageable);
}