package com.netpro.trinity.service.member.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.service.member.entity.Usergroup;

@Repository  //宣告這是一個DAO類別
public interface UsergroupJPADao extends JpaRepository<Usergroup, String> {  //自動繼承JapRepository下的所有方法
	@Query("select count(g)>0 from usergroup g where g.groupname=:groupname AND 1=1")
	Boolean existByName(@Param("groupname") String groupname);
	
	List<Usergroup> findByGroupnameLikeIgnoreCase(String name);
	List<Usergroup> findByGroupnameLikeIgnoreCase(String name, Sort sort);
	Page<Usergroup> findByGroupnameLikeIgnoreCase(String name, Pageable pageable);
}