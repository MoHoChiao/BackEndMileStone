package com.netpro.trinity.resource.admin.member.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.resource.admin.member.entity.Trinityuser;

@Repository  //宣告這是一個DAO類別
public interface TrinityuserJPADao extends JpaRepository<Trinityuser, String> {  //自動繼承JapRepository下的所有方法
	@Query("select case when count(user)>0 then true else false end from trinityuser user where user.username=:username AND 1=1")
	Boolean existByName(@Param("username") String username);
	@Query("select case when count(user)>0 then true else false end from trinityuser user where user.userid=:userid AND 1=1")
	Boolean existByID(@Param("userid") String userid);
	
	//userid field
	Trinityuser findByuserid(String id);
	
	List<Trinityuser> findByUsernameLikeIgnoreCaseOrUseridLikeIgnoreCase(String name, String userid);
	List<Trinityuser> findByUsernameLikeIgnoreCaseOrUseridLikeIgnoreCase(String name, String userid, Sort sort);
	Page<Trinityuser> findByUsernameLikeIgnoreCaseOrUseridLikeIgnoreCase(String name, String userid, Pageable pageable);
}