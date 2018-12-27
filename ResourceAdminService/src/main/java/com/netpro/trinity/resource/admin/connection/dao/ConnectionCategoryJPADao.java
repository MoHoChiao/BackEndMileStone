package com.netpro.trinity.resource.admin.connection.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.resource.admin.connection.entity.ConnectionCategory;

@Repository  //宣告這是一個DAO類別
public interface ConnectionCategoryJPADao extends JpaRepository<ConnectionCategory, String> {
	@Query("select case when count(category)>0 then true else false end from Connectioncategory category where category.conncategoryname=:conncategoryname AND 1=1")
	Boolean existByName(@Param("conncategoryname") String conncategoryname);
	
	List<ConnectionCategory> findByConncategorynameLikeIgnoreCase(String name);
	List<ConnectionCategory> findByConncategorynameLikeIgnoreCase(String name, Sort sort);
	Page<ConnectionCategory> findByConncategorynameLikeIgnoreCase(String name, Pageable pageable);
}