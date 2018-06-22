package com.netpro.trinity.service.externalrule.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.service.externalrule.entity.Dmextpackage;

@Repository  //宣告這是一個DAO類別
public interface DmExtPackageJPADao extends JpaRepository<Dmextpackage, String> {
	@Query("select count(p)>0 from dmextpackage p where p.packagename=:packagename AND 1=1")
	Boolean existByName(@Param("packagename") String name);
	
	//package name field
	List<Dmextpackage> findBypackagename(String name);
	List<Dmextpackage> findBypackagename(String name, Sort sort);
	Page<Dmextpackage> findBypackagename(String name, Pageable pageable);
	List<Dmextpackage> findBypackagenameIgnoreCase(String name);
	List<Dmextpackage> findBypackagenameIgnoreCase(String name, Sort sort);
	Page<Dmextpackage> findBypackagenameIgnoreCase(String name, Pageable pageable);
	List<Dmextpackage> findBypackagenameLike(String name);
	List<Dmextpackage> findBypackagenameLike(String name, Sort sort);
	Page<Dmextpackage> findBypackagenameLike(String name, Pageable pageable);
	List<Dmextpackage> findBypackagenameLikeIgnoreCase(String name);
	List<Dmextpackage> findBypackagenameLikeIgnoreCase(String name, Sort sort);
	Page<Dmextpackage> findBypackagenameLikeIgnoreCase(String name, Pageable pageable);
}