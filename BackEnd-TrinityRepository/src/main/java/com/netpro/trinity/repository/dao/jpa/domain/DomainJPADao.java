package com.netpro.trinity.repository.dao.jpa.domain;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.repository.entity.domain.jpa.Domain;

@Repository  //宣告這是一個DAO類別
public interface DomainJPADao extends JpaRepository<Domain, String> {
	@Query("select count(d)>0 from domain d where d.name=:name AND 1=1")
	Boolean existByName(@Param("name") String name);
	
	//domain name field
	List<Domain> findByname(String name);
	List<Domain> findByname(String name, Sort sort);
	Page<Domain> findByname(String name, Pageable pageable);
	List<Domain> findBynameIgnoreCase(String name);
	List<Domain> findBynameIgnoreCase(String name, Sort sort);
	Page<Domain> findBynameIgnoreCase(String name, Pageable pageable);
	List<Domain> findBynameLike(String name);
	List<Domain> findBynameLike(String name, Sort sort);
	Page<Domain> findBynameLike(String name, Pageable pageable);
	List<Domain> findBynameLikeIgnoreCase(String name);
	List<Domain> findBynameLikeIgnoreCase(String name, Sort sort);
	Page<Domain> findBynameLikeIgnoreCase(String name, Pageable pageable);
	
	//description field
	List<Domain> findBydescription(String description);
	List<Domain> findBydescription(String description, Sort sort);
	Page<Domain> findBydescription(String description, Pageable pageable);
	List<Domain> findBydescriptionIgnoreCase(String description);
	List<Domain> findBydescriptionIgnoreCase(String description, Sort sort);
	Page<Domain> findBydescriptionIgnoreCase(String description, Pageable pageable);
	List<Domain> findBydescriptionLike(String description);
	List<Domain> findBydescriptionLike(String description, Sort sort);
	Page<Domain> findBydescriptionLike(String description, Pageable pageable);
	List<Domain> findBydescriptionLikeIgnoreCase(String description);
	List<Domain> findBydescriptionLikeIgnoreCase(String description, Sort sort);
	Page<Domain> findBydescriptionLikeIgnoreCase(String description, Pageable pageable);
}