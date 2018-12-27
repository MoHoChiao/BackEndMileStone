package com.netpro.trinity.resource.admin.connection.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.resource.admin.connection.entity.Connection;

@Repository  //宣告這是一個DAO類別
public interface ConnectionJPADao extends JpaRepository<Connection, String> {
	@Query("select case when count(conn)>0 then true else false end from Connection conn where conn.connectionname=:connectionname AND 1=1")
	Boolean existByName(@Param("connectionname") String connectionname);
	
	//connection uid field with In
	List<Connection> findByConnectionuidIn(List<String> uids);
	List<Connection> findByConnectionuidIn(List<String> uids, Sort sort);
	Page<Connection> findByConnectionuidIn(List<String> uids, Pageable pageable);
	//connection uid field with Not In
	List<Connection> findByConnectionuidNotIn(List<String> uids);
	List<Connection> findByConnectionuidNotIn(List<String> uids, Sort sort);
	Page<Connection> findByConnectionuidNotIn(List<String> uids, Pageable pageable);
	
	//find by name
	List<Connection> findByConnectionnameLikeIgnoreCase(String name);
	List<Connection> findByConnectionnameLikeIgnoreCase(String name, Sort sort);
	Page<Connection> findByConnectionnameLikeIgnoreCase(String name, Pageable pageable);

	//find by name and in category
	List<Connection> findByConnectionnameLikeIgnoreCaseAndConnectionuidIn(String name, List<String> uids);
	List<Connection> findByConnectionnameLikeIgnoreCaseAndConnectionuidIn(String name, Sort sort, List<String> uids);
	Page<Connection> findByConnectionnameLikeIgnoreCaseAndConnectionuidIn(String name, Pageable pageable, List<String> uids);
	
	//find by name and not in category
	List<Connection> findByConnectionnameLikeIgnoreCaseAndConnectionuidNotIn(String name, List<String> uids);
	List<Connection> findByConnectionnameLikeIgnoreCaseAndConnectionuidNotIn(String name, Sort sort, List<String> uids);
	Page<Connection> findByConnectionnameLikeIgnoreCaseAndConnectionuidNotIn(String name, Pageable pageable, List<String> uids);
}