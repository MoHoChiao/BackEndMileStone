package com.netpro.trinity.repository.frequency.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.repository.filesource.entity.FrequencyCategory;

@Repository  //宣告這是一個DAO類別
public interface FrequencyCategoryJPADao extends JpaRepository<FrequencyCategory, String> {
	@Query("select count(category)>0 from Frequencycategory category where category.freqcategoryname=:freqcategoryname AND 1=1")
	Boolean existByName(@Param("freqcategoryname") String freqcategoryname);
	
	//frequency category name field
	List<FrequencyCategory> findByfreqcategoryname(String name);
	List<FrequencyCategory> findByfreqcategoryname(String name, Sort sort);
	Page<FrequencyCategory> findByfreqcategoryname(String name, Pageable pageable);
	List<FrequencyCategory> findByfreqcategorynameIgnoreCase(String name);
	List<FrequencyCategory> findByfreqcategorynameIgnoreCase(String name, Sort sort);
	Page<FrequencyCategory> findByfreqcategorynameIgnoreCase(String name, Pageable pageable);
	List<FrequencyCategory> findByfreqcategorynameLike(String name);
	List<FrequencyCategory> findByfreqcategorynameLike(String name, Sort sort);
	Page<FrequencyCategory> findByfreqcategorynameLike(String name, Pageable pageable);
	List<FrequencyCategory> findByfreqcategorynameLikeIgnoreCase(String name);
	List<FrequencyCategory> findByfreqcategorynameLikeIgnoreCase(String name, Sort sort);
	Page<FrequencyCategory> findByfreqcategorynameLikeIgnoreCase(String name, Pageable pageable);
	
	//description field
	List<FrequencyCategory> findBydescription(String description);
	List<FrequencyCategory> findBydescription(String description, Sort sort);
	Page<FrequencyCategory> findBydescription(String description, Pageable pageable);
	List<FrequencyCategory> findBydescriptionIgnoreCase(String description);
	List<FrequencyCategory> findBydescriptionIgnoreCase(String description, Sort sort);
	Page<FrequencyCategory> findBydescriptionIgnoreCase(String description, Pageable pageable);
	List<FrequencyCategory> findBydescriptionLike(String description);
	List<FrequencyCategory> findBydescriptionLike(String description, Sort sort);
	Page<FrequencyCategory> findBydescriptionLike(String description, Pageable pageable);
	List<FrequencyCategory> findBydescriptionLikeIgnoreCase(String description);
	List<FrequencyCategory> findBydescriptionLikeIgnoreCase(String description, Sort sort);
	Page<FrequencyCategory> findBydescriptionLikeIgnoreCase(String description, Pageable pageable);
}