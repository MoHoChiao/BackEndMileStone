package com.netpro.trinity.service.frequency.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.service.filesource.entity.ExclFrequency;

@Repository  //宣告這是一個DAO類別
public interface ExclFrequencyJPADao extends JpaRepository<ExclFrequency, String> {
	@Query("select count(excl)>0 from excludefrequency excl where excl.excludefrequencyname=:excludefrequencyname AND 1=1")
	Boolean existByName(@Param("excludefrequencyname") String excludefrequencyname);
	
	//exclude frequency name field
	List<ExclFrequency> findByexcludefrequencyname(String name);
	List<ExclFrequency> findByexcludefrequencyname(String name, Sort sort);
	Page<ExclFrequency> findByexcludefrequencyname(String name, Pageable pageable);
	List<ExclFrequency> findByexcludefrequencynameIgnoreCase(String name);
	List<ExclFrequency> findByexcludefrequencynameIgnoreCase(String name, Sort sort);
	Page<ExclFrequency> findByexcludefrequencynameIgnoreCase(String name, Pageable pageable);
	List<ExclFrequency> findByexcludefrequencynameLike(String name);
	List<ExclFrequency> findByexcludefrequencynameLike(String name, Sort sort);
	Page<ExclFrequency> findByexcludefrequencynameLike(String name, Pageable pageable);
	List<ExclFrequency> findByexcludefrequencynameLikeIgnoreCase(String name);
	List<ExclFrequency> findByexcludefrequencynameLikeIgnoreCase(String name, Sort sort);
	Page<ExclFrequency> findByexcludefrequencynameLikeIgnoreCase(String name, Pageable pageable);
	
	//activate field
	List<ExclFrequency> findByactivate(String activate);
	List<ExclFrequency> findByactivate(String activate, Sort sort);
	Page<ExclFrequency> findByactivate(String activate, Pageable pageable);
	List<ExclFrequency> findByactivateIgnoreCase(String activate);
	List<ExclFrequency> findByactivateIgnoreCase(String activate, Sort sort);
	Page<ExclFrequency> findByactivateIgnoreCase(String activate, Pageable pageable);
	List<ExclFrequency> findByactivateLike(String activate);
	List<ExclFrequency> findByactivateLike(String activate, Sort sort);
	Page<ExclFrequency> findByactivateLike(String activate, Pageable pageable);
	List<ExclFrequency> findByactivateLikeIgnoreCase(String activate);
	List<ExclFrequency> findByactivateLikeIgnoreCase(String activate, Sort sort);
	Page<ExclFrequency> findByactivateLikeIgnoreCase(String activate, Pageable pageable);
	
	//description field
	List<ExclFrequency> findBydescription(String description);
	List<ExclFrequency> findBydescription(String description, Sort sort);
	Page<ExclFrequency> findBydescription(String description, Pageable pageable);
	List<ExclFrequency> findBydescriptionIgnoreCase(String description);
	List<ExclFrequency> findBydescriptionIgnoreCase(String description, Sort sort);
	Page<ExclFrequency> findBydescriptionIgnoreCase(String description, Pageable pageable);
	List<ExclFrequency> findBydescriptionLike(String description);
	List<ExclFrequency> findBydescriptionLike(String description, Sort sort);
	Page<ExclFrequency> findBydescriptionLike(String description, Pageable pageable);
	List<ExclFrequency> findBydescriptionLikeIgnoreCase(String description);
	List<ExclFrequency> findBydescriptionLikeIgnoreCase(String description, Sort sort);
	Page<ExclFrequency> findBydescriptionLikeIgnoreCase(String description, Pageable pageable);
}