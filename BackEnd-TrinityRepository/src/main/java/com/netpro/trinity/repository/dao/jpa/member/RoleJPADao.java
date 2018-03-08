package com.netpro.trinity.repository.dao.jpa.member;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.repository.entity.member.jpa.Role;

@Repository  //宣告這是一個DAO類別
public interface RoleJPADao extends JpaRepository<Role, String> {  //自動繼承JapRepository下的所有方法
	@Query("select count(r)>0 from role r where r.rolename=:rolename AND 1=1")
	Boolean existByName(@Param("rolename") String rolename);
	
	//rolename field
	List<Role> findByrolename(String name);
	List<Role> findByrolename(String name, Sort sort);
	Page<Role> findByrolename(String name, Pageable pageable);
	List<Role> findByrolenameIgnoreCase(String name);
	List<Role> findByrolenameIgnoreCase(String name, Sort sort);
	Page<Role> findByrolenameIgnoreCase(String name, Pageable pageable);
	List<Role> findByrolenameLike(String name);
	List<Role> findByrolenameLike(String name, Sort sort);
	Page<Role> findByrolenameLike(String name, Pageable pageable);
	List<Role> findByrolenameLikeIgnoreCase(String name);
	List<Role> findByrolenameLikeIgnoreCase(String name, Sort sort);
	Page<Role> findByrolenameLikeIgnoreCase(String name, Pageable pageable);
	
	//description field
	List<Role> findBydescription(String description);
	List<Role> findBydescription(String description, Sort sort);
	Page<Role> findBydescription(String description, Pageable pageable);
	List<Role> findBydescriptionIgnoreCase(String description);
	List<Role> findBydescriptionIgnoreCase(String description, Sort sort);
	Page<Role> findBydescriptionIgnoreCase(String description, Pageable pageable);
	List<Role> findBydescriptionLike(String description);
	List<Role> findBydescriptionLike(String description, Sort sort);
	Page<Role> findBydescriptionLike(String description, Pageable pageable);
	List<Role> findBydescriptionLikeIgnoreCase(String description);
	List<Role> findBydescriptionLikeIgnoreCase(String description, Sort sort);
	Page<Role> findBydescriptionLikeIgnoreCase(String description, Pageable pageable);
}