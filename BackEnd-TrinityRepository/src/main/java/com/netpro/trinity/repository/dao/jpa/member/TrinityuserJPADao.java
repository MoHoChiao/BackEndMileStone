package com.netpro.trinity.repository.dao.jpa.member;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.repository.entity.member.jpa.Trinityuser;

@Repository  //宣告這是一個DAO類別
public interface TrinityuserJPADao extends JpaRepository<Trinityuser, String> {  //自動繼承JapRepository下的所有方法
	@Query("select count(user)>0 from trinityuser user where user.username=:username AND 1=1")
	Boolean existByName(@Param("username") String username);
	@Query("select count(user)>0 from trinityuser user where user.userid=:userid AND 1=1")
	Boolean existByID(@Param("userid") String userid);
	
	//userid field
	Trinityuser findByuserid(String id);
	
	//username field
	List<Trinityuser> findByusername(String name);
	List<Trinityuser> findByusername(String name, Sort sort);
	Page<Trinityuser> findByusername(String name, Pageable pageable);
	List<Trinityuser> findByusernameIgnoreCase(String name);
	List<Trinityuser> findByusernameIgnoreCase(String name, Sort sort);
	Page<Trinityuser> findByusernameIgnoreCase(String name, Pageable pageable);
	List<Trinityuser> findByusernameLike(String name);
	List<Trinityuser> findByusernameLike(String name, Sort sort);
	Page<Trinityuser> findByusernameLike(String name, Pageable pageable);
	List<Trinityuser> findByusernameLikeIgnoreCase(String name);
	List<Trinityuser> findByusernameLikeIgnoreCase(String name, Sort sort);
	Page<Trinityuser> findByusernameLikeIgnoreCase(String name, Pageable pageable);
	
	//activate field
	List<Trinityuser> findByactivate(String activate);
	List<Trinityuser> findByactivate(String activate, Sort sort);
	Page<Trinityuser> findByactivate(String activate, Pageable pageable);
	List<Trinityuser> findByactivateIgnoreCase(String activate);
	List<Trinityuser> findByactivateIgnoreCase(String activate, Sort sort);
	Page<Trinityuser> findByactivateIgnoreCase(String activate, Pageable pageable);
	List<Trinityuser> findByactivateLike(String activate);
	List<Trinityuser> findByactivateLike(String activate, Sort sort);
	Page<Trinityuser> findByactivateLike(String activate, Pageable pageable);
	List<Trinityuser> findByactivateLikeIgnoreCase(String activate);
	List<Trinityuser> findByactivateLikeIgnoreCase(String activate, Sort sort);
	Page<Trinityuser> findByactivateLikeIgnoreCase(String activate, Pageable pageable);
	
	//usertype field
	List<Trinityuser> findByusertype(String usertype);
	List<Trinityuser> findByusertype(String usertype, Sort sort);
	Page<Trinityuser> findByusertype(String usertype, Pageable pageable);
	List<Trinityuser> findByusertypeIgnoreCase(String usertype);
	List<Trinityuser> findByusertypeIgnoreCase(String usertype, Sort sort);
	Page<Trinityuser> findByusertypeIgnoreCase(String usertype, Pageable pageable);
	List<Trinityuser> findByusertypeLike(String usertype);
	List<Trinityuser> findByusertypeLike(String usertype, Sort sort);
	Page<Trinityuser> findByusertypeLike(String usertype, Pageable pageable);
	List<Trinityuser> findByusertypeLikeIgnoreCase(String usertype);
	List<Trinityuser> findByusertypeLikeIgnoreCase(String usertype, Sort sort);
	Page<Trinityuser> findByusertypeLikeIgnoreCase(String usertype, Pageable pageable);
	
	//description field
	List<Trinityuser> findBydescription(String description);
	List<Trinityuser> findBydescription(String description, Sort sort);
	Page<Trinityuser> findBydescription(String description, Pageable pageable);
	List<Trinityuser> findBydescriptionIgnoreCase(String description);
	List<Trinityuser> findBydescriptionIgnoreCase(String description, Sort sort);
	Page<Trinityuser> findBydescriptionIgnoreCase(String description, Pageable pageable);
	List<Trinityuser> findBydescriptionLike(String description);
	List<Trinityuser> findBydescriptionLike(String description, Sort sort);
	Page<Trinityuser> findBydescriptionLike(String description, Pageable pageable);
	List<Trinityuser> findBydescriptionLikeIgnoreCase(String description);
	List<Trinityuser> findBydescriptionLikeIgnoreCase(String description, Sort sort);
	Page<Trinityuser> findBydescriptionLikeIgnoreCase(String description, Pageable pageable);
}