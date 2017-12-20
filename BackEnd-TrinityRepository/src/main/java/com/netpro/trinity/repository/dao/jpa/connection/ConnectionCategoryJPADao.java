package com.netpro.trinity.repository.dao.jpa.connection;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.repository.entity.connection.jpa.ConnectionCategory;

@Repository  //宣告這是一個DAO類別
public interface ConnectionCategoryJPADao extends JpaRepository<ConnectionCategory, String> {
	@Query("select count(category)>0 from Connectioncategory category where category.conncategoryname=:conncategoryname AND 1=1")
	Boolean existByName(@Param("conncategoryname") String conncategoryname);
	
	//file source category name field
	List<ConnectionCategory> findByconncategoryname(String name);
	List<ConnectionCategory> findByconncategoryname(String name, Sort sort);
	Page<ConnectionCategory> findByconncategoryname(String name, Pageable pageable);
	List<ConnectionCategory> findByconncategorynameIgnoreCase(String name);
	List<ConnectionCategory> findByconncategorynameIgnoreCase(String name, Sort sort);
	Page<ConnectionCategory> findByconncategorynameIgnoreCase(String name, Pageable pageable);
	List<ConnectionCategory> findByconncategorynameLike(String name);
	List<ConnectionCategory> findByconncategorynameLike(String name, Sort sort);
	Page<ConnectionCategory> findByconncategorynameLike(String name, Pageable pageable);
	List<ConnectionCategory> findByconncategorynameLikeIgnoreCase(String name);
	List<ConnectionCategory> findByconncategorynameLikeIgnoreCase(String name, Sort sort);
	Page<ConnectionCategory> findByconncategorynameLikeIgnoreCase(String name, Pageable pageable);
	
	//description field
	List<ConnectionCategory> findBydescription(String description);
	List<ConnectionCategory> findBydescription(String description, Sort sort);
	Page<ConnectionCategory> findBydescription(String description, Pageable pageable);
	List<ConnectionCategory> findBydescriptionIgnoreCase(String description);
	List<ConnectionCategory> findBydescriptionIgnoreCase(String description, Sort sort);
	Page<ConnectionCategory> findBydescriptionIgnoreCase(String description, Pageable pageable);
	List<ConnectionCategory> findBydescriptionLike(String description);
	List<ConnectionCategory> findBydescriptionLike(String description, Sort sort);
	Page<ConnectionCategory> findBydescriptionLike(String description, Pageable pageable);
	List<ConnectionCategory> findBydescriptionLikeIgnoreCase(String description);
	List<ConnectionCategory> findBydescriptionLikeIgnoreCase(String description, Sort sort);
	Page<ConnectionCategory> findBydescriptionLikeIgnoreCase(String description, Pageable pageable);
}