package com.netpro.trinity.resource.admin.frequency.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.resource.admin.frequency.entity.FrequencyCategory;

@Repository  //宣告這是一個DAO類別
public interface FrequencyCategoryJPADao extends JpaRepository<FrequencyCategory, String> {
	@Query("select count(category)>0 from Frequencycategory category where category.freqcategoryname=:freqcategoryname AND 1=1")
	Boolean existByName(@Param("freqcategoryname") String freqcategoryname);
	
	List<FrequencyCategory> findByFreqcategorynameLikeIgnoreCase(String name);
	List<FrequencyCategory> findByFreqcategorynameLikeIgnoreCase(String name, Sort sort);
	Page<FrequencyCategory> findByFreqcategorynameLikeIgnoreCase(String name, Pageable pageable);
}