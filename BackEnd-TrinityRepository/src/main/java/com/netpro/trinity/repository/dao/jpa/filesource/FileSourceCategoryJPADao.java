package com.netpro.trinity.repository.dao.jpa.filesource;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.repository.entity.filesource.jpa.FileSourceCategory;

@Repository  //宣告這是一個DAO類別
public interface FileSourceCategoryJPADao extends JpaRepository<FileSourceCategory, String> {
	@Query("select count(category)>0 from Filesourcecategory category where category.fscategoryname=:fscategoryname AND 1=1")
	Boolean existByName(@Param("fscategoryname") String fscategoryname);
	
	//file source category name field
	List<FileSourceCategory> findByfscategoryname(String name);
	List<FileSourceCategory> findByfscategoryname(String name, Sort sort);
	Page<FileSourceCategory> findByfscategoryname(String name, Pageable pageable);
	List<FileSourceCategory> findByfscategorynameIgnoreCase(String name);
	List<FileSourceCategory> findByfscategorynameIgnoreCase(String name, Sort sort);
	Page<FileSourceCategory> findByfscategorynameIgnoreCase(String name, Pageable pageable);
	List<FileSourceCategory> findByfscategorynameLike(String name);
	List<FileSourceCategory> findByfscategorynameLike(String name, Sort sort);
	Page<FileSourceCategory> findByfscategorynameLike(String name, Pageable pageable);
	List<FileSourceCategory> findByfscategorynameLikeIgnoreCase(String name);
	List<FileSourceCategory> findByfscategorynameLikeIgnoreCase(String name, Sort sort);
	Page<FileSourceCategory> findByfscategorynameLikeIgnoreCase(String name, Pageable pageable);
	
	//description field
	List<FileSourceCategory> findBydescription(String description);
	List<FileSourceCategory> findBydescription(String description, Sort sort);
	Page<FileSourceCategory> findBydescription(String description, Pageable pageable);
	List<FileSourceCategory> findBydescriptionIgnoreCase(String description);
	List<FileSourceCategory> findBydescriptionIgnoreCase(String description, Sort sort);
	Page<FileSourceCategory> findBydescriptionIgnoreCase(String description, Pageable pageable);
	List<FileSourceCategory> findBydescriptionLike(String description);
	List<FileSourceCategory> findBydescriptionLike(String description, Sort sort);
	Page<FileSourceCategory> findBydescriptionLike(String description, Pageable pageable);
	List<FileSourceCategory> findBydescriptionLikeIgnoreCase(String description);
	List<FileSourceCategory> findBydescriptionLikeIgnoreCase(String description, Sort sort);
	Page<FileSourceCategory> findBydescriptionLikeIgnoreCase(String description, Pageable pageable);
}