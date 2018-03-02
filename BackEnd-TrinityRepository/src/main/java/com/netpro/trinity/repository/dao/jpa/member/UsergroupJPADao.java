package com.netpro.trinity.repository.dao.jpa.member;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.repository.entity.member.jpa.Usergroup;

@Repository  //宣告這是一個DAO類別
public interface UsergroupJPADao extends JpaRepository<Usergroup, String> {  //自動繼承JapRepository下的所有方法
	@Query("select count(g)>0 from usergroup g where g.groupname=:groupname AND 1=1")
	Boolean existByName(@Param("groupname") String groupname);
	
	//groupname field
	List<Usergroup> findBygroupname(String name);
	List<Usergroup> findBygroupname(String name, Sort sort);
	Page<Usergroup> findBygroupname(String name, Pageable pageable);
	List<Usergroup> findBygroupnameIgnoreCase(String name);
	List<Usergroup> findBygroupnameIgnoreCase(String name, Sort sort);
	Page<Usergroup> findBygroupnameIgnoreCase(String name, Pageable pageable);
	List<Usergroup> findBygroupnameLike(String name);
	List<Usergroup> findBygroupnameLike(String name, Sort sort);
	Page<Usergroup> findBygroupnameLike(String name, Pageable pageable);
	List<Usergroup> findBygroupnameLikeIgnoreCase(String name);
	List<Usergroup> findBygroupnameLikeIgnoreCase(String name, Sort sort);
	Page<Usergroup> findBygroupnameLikeIgnoreCase(String name, Pageable pageable);
	
	//description field
	List<Usergroup> findBydescription(String description);
	List<Usergroup> findBydescription(String description, Sort sort);
	Page<Usergroup> findBydescription(String description, Pageable pageable);
	List<Usergroup> findBydescriptionIgnoreCase(String description);
	List<Usergroup> findBydescriptionIgnoreCase(String description, Sort sort);
	Page<Usergroup> findBydescriptionIgnoreCase(String description, Pageable pageable);
	List<Usergroup> findBydescriptionLike(String description);
	List<Usergroup> findBydescriptionLike(String description, Sort sort);
	Page<Usergroup> findBydescriptionLike(String description, Pageable pageable);
	List<Usergroup> findBydescriptionLikeIgnoreCase(String description);
	List<Usergroup> findBydescriptionLikeIgnoreCase(String description, Sort sort);
	Page<Usergroup> findBydescriptionLikeIgnoreCase(String description, Pageable pageable);
}