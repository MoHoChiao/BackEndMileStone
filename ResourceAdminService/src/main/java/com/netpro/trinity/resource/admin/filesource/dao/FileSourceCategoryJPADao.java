package com.netpro.trinity.resource.admin.filesource.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.resource.admin.filesource.entity.FileSourceCategory;

@Repository  //宣告這是一個DAO類別
public interface FileSourceCategoryJPADao extends JpaRepository<FileSourceCategory, String> {
	@Query("select case when count(fc)>0 then true else false end from Filesourcecategory fc where fc.fscategoryname=:fscategoryname AND 1=1")
	Boolean existByName(@Param("fscategoryname") String fscategoryname);
	
	List<FileSourceCategory> findByFscategorynameLikeIgnoreCase(String name);
	List<FileSourceCategory> findByFscategorynameLikeIgnoreCase(String name, Sort sort);
	Page<FileSourceCategory> findByFscategorynameLikeIgnoreCase(String name, Pageable pageable);
}