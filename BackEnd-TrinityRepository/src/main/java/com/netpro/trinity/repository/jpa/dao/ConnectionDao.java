package com.netpro.trinity.repository.jpa.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.repository.jpa.entity.Connection;

@Repository  //宣告這是一個DAO類別
public interface ConnectionDao extends JpaRepository<Connection, String> {
	@Query("select count(conn)>0 from Connection conn where conn.connectionname=:connectionname")
	Boolean existByName(@Param("connectionname") String connectionname);
	
	//connectionname field
	List<Connection> findByconnectionname(String name);
	List<Connection> findByconnectionname(String name, Sort sort);
	Page<Connection> findByconnectionname(String name, Pageable pageable);
	List<Connection> findByconnectionnameIgnoreCase(String name);
	List<Connection> findByconnectionnameIgnoreCase(String name, Sort sort);
	Page<Connection> findByconnectionnameIgnoreCase(String name, Pageable pageable);
	List<Connection> findByconnectionnameLike(String name);
	List<Connection> findByconnectionnameLike(String name, Sort sort);
	Page<Connection> findByconnectionnameLike(String name, Pageable pageable);
	List<Connection> findByconnectionnameLikeIgnoreCase(String name);
	List<Connection> findByconnectionnameLikeIgnoreCase(String name, Sort sort);
	Page<Connection> findByconnectionnameLikeIgnoreCase(String name, Pageable pageable);
	
	//connectiontype field
	List<Connection> findByconnectiontype(String type);
	List<Connection> findByconnectiontype(String type, Sort sort);
	Page<Connection> findByconnectiontype(String type, Pageable pageable);
	List<Connection> findByconnectiontypeIgnoreCase(String type);
	List<Connection> findByconnectiontypeIgnoreCase(String type, Sort sort);
	Page<Connection> findByconnectiontypeIgnoreCase(String type, Pageable pageable);
	List<Connection> findByconnectiontypeLike(String type);
	List<Connection> findByconnectiontypeLike(String type, Sort sort);
	Page<Connection> findByconnectiontypeLike(String type, Pageable pageable);
	List<Connection> findByconnectiontypeLikeIgnoreCase(String type);
	List<Connection> findByconnectiontypeLikeIgnoreCase(String type, Sort sort);
	Page<Connection> findByconnectiontypeLikeIgnoreCase(String type, Pageable pageable);
	
	//description field
	List<Connection> findBydescription(String description);
	List<Connection> findBydescription(String description, Sort sort);
	Page<Connection> findBydescription(String description, Pageable pageable);
	List<Connection> findBydescriptionIgnoreCase(String description);
	List<Connection> findBydescriptionIgnoreCase(String description, Sort sort);
	Page<Connection> findBydescriptionIgnoreCase(String description, Pageable pageable);
	List<Connection> findBydescriptionLike(String description);
	List<Connection> findBydescriptionLike(String description, Sort sort);
	Page<Connection> findBydescriptionLike(String description, Pageable pageable);
	List<Connection> findBydescriptionLikeIgnoreCase(String description);
	List<Connection> findBydescriptionLikeIgnoreCase(String description, Sort sort);
	Page<Connection> findBydescriptionLikeIgnoreCase(String description, Pageable pageable);
}