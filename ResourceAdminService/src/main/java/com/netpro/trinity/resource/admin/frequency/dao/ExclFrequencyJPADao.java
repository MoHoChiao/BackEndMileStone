package com.netpro.trinity.resource.admin.frequency.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.resource.admin.frequency.entity.ExclFrequency;

@Repository  //宣告這是一個DAO類別
public interface ExclFrequencyJPADao extends JpaRepository<ExclFrequency, String> {
	@Query("select count(excl)>0 from excludefrequency excl where excl.excludefrequencyname=:excludefrequencyname AND 1=1")
	Boolean existByName(@Param("excludefrequencyname") String excludefrequencyname);
	
	List<ExclFrequency> findByExcludefrequencynameLikeIgnoreCase(String name);
	List<ExclFrequency> findByExcludefrequencynameLikeIgnoreCase(String name, Sort sort);
	Page<ExclFrequency> findByExcludefrequencynameLikeIgnoreCase(String name, Pageable pageable);
	
}