package com.netpro.trinity.service.member.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.service.member.entity.Role;

@Repository  //宣告這是一個DAO類別
public interface RoleJPADao extends JpaRepository<Role, String> {  //自動繼承JapRepository下的所有方法
	@Query("select count(r)>0 from role r where r.rolename=:rolename AND 1=1")
	Boolean existByName(@Param("rolename") String rolename);
	
	List<Role> findByRolenameLikeIgnoreCase(String name);
	List<Role> findByRolenameLikeIgnoreCase(String name, Sort sort);
	Page<Role> findByRolenameLikeIgnoreCase(String name, Pageable pageable);
}