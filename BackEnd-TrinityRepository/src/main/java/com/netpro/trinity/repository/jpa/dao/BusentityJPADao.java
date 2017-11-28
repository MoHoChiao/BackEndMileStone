package com.netpro.trinity.repository.jpa.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.repository.jpa.entity.Busentity;

@Repository  //宣告這是一個DAO類別
public interface BusentityJPADao extends JpaRepository<Busentity, String> {
	@Query("select count(entity)>0 from Busentity entity where entity.busentityname=:busentityname")
	Boolean existByName(@Param("busentityname") String busentityname);
	
	//busentityname field
	List<Busentity> findBybusentityname(String name);
	List<Busentity> findBybusentityname(String name, Sort sort);
	Page<Busentity> findBybusentityname(String name, Pageable pageable);
	List<Busentity> findBybusentitynameIgnoreCase(String name);
	List<Busentity> findBybusentitynameIgnoreCase(String name, Sort sort);
	Page<Busentity> findBybusentitynameIgnoreCase(String name, Pageable pageable);
	List<Busentity> findBybusentitynameLike(String name);
	List<Busentity> findBybusentitynameLike(String name, Sort sort);
	Page<Busentity> findBybusentitynameLike(String name, Pageable pageable);
	List<Busentity> findBybusentitynameLikeIgnoreCase(String name);
	List<Busentity> findBybusentitynameLikeIgnoreCase(String name, Sort sort);
	Page<Busentity> findBybusentitynameLikeIgnoreCase(String name, Pageable pageable);
	
	//description field
	List<Busentity> findBydescription(String description);
	List<Busentity> findBydescription(String description, Sort sort);
	Page<Busentity> findBydescription(String description, Pageable pageable);
	List<Busentity> findBydescriptionIgnoreCase(String description);
	List<Busentity> findBydescriptionIgnoreCase(String description, Sort sort);
	Page<Busentity> findBydescriptionIgnoreCase(String description, Pageable pageable);
	List<Busentity> findBydescriptionLike(String description);
	List<Busentity> findBydescriptionLike(String description, Sort sort);
	Page<Busentity> findBydescriptionLike(String description, Pageable pageable);
	List<Busentity> findBydescriptionLikeIgnoreCase(String description);
	List<Busentity> findBydescriptionLikeIgnoreCase(String description, Sort sort);
	Page<Busentity> findBydescriptionLikeIgnoreCase(String description, Pageable pageable);
}