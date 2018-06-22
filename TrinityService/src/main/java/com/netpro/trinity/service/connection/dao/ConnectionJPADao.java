package com.netpro.trinity.service.connection.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.service.connection.entity.Connection;

@Repository  //宣告這是一個DAO類別
public interface ConnectionJPADao extends JpaRepository<Connection, String> {
	@Query("select count(conn)>0 from Connection conn where conn.connectionname=:connectionname AND 1=1")
	Boolean existByName(@Param("connectionname") String connectionname);
	
	//connection uid field with In
	List<Connection> findByConnectionuidIn(List<String> uids);
	List<Connection> findByConnectionuidIn(List<String> uids, Sort sort);
	Page<Connection> findByConnectionuidIn(List<String> uids, Pageable pageable);
	//connection uid field with Not In
	List<Connection> findByConnectionuidNotIn(List<String> uids);
	List<Connection> findByConnectionuidNotIn(List<String> uids, Sort sort);
	Page<Connection> findByConnectionuidNotIn(List<String> uids, Pageable pageable);
	
	
	//connection name field
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
	//connection name field with In Connectionuid constraints
	List<Connection> findByconnectionnameAndConnectionuidIn(String name, List<String> uids);
	List<Connection> findByconnectionnameAndConnectionuidIn(String name, Sort sort, List<String> uids);
	Page<Connection> findByconnectionnameAndConnectionuidIn(String name, Pageable pageable, List<String> uids);
	List<Connection> findByconnectionnameIgnoreCaseAndConnectionuidIn(String name, List<String> uids);
	List<Connection> findByconnectionnameIgnoreCaseAndConnectionuidIn(String name, Sort sort, List<String> uids);
	Page<Connection> findByconnectionnameIgnoreCaseAndConnectionuidIn(String name, Pageable pageable, List<String> uids);
	List<Connection> findByconnectionnameLikeAndConnectionuidIn(String name, List<String> uids);
	List<Connection> findByconnectionnameLikeAndConnectionuidIn(String name, Sort sort, List<String> uids);
	Page<Connection> findByconnectionnameLikeAndConnectionuidIn(String name, Pageable pageable, List<String> uids);
	List<Connection> findByconnectionnameLikeIgnoreCaseAndConnectionuidIn(String name, List<String> uids);
	List<Connection> findByconnectionnameLikeIgnoreCaseAndConnectionuidIn(String name, Sort sort, List<String> uids);
	Page<Connection> findByconnectionnameLikeIgnoreCaseAndConnectionuidIn(String name, Pageable pageable, List<String> uids);
	//connection name field with Not In Connectionuid constraints
	List<Connection> findByconnectionnameAndConnectionuidNotIn(String name, List<String> uids);
	List<Connection> findByconnectionnameAndConnectionuidNotIn(String name, Sort sort, List<String> uids);
	Page<Connection> findByconnectionnameAndConnectionuidNotIn(String name, Pageable pageable, List<String> uids);
	List<Connection> findByconnectionnameIgnoreCaseAndConnectionuidNotIn(String name, List<String> uids);
	List<Connection> findByconnectionnameIgnoreCaseAndConnectionuidNotIn(String name, Sort sort, List<String> uids);
	Page<Connection> findByconnectionnameIgnoreCaseAndConnectionuidNotIn(String name, Pageable pageable, List<String> uids);
	List<Connection> findByconnectionnameLikeAndConnectionuidNotIn(String name, List<String> uids);
	List<Connection> findByconnectionnameLikeAndConnectionuidNotIn(String name, Sort sort, List<String> uids);
	Page<Connection> findByconnectionnameLikeAndConnectionuidNotIn(String name, Pageable pageable, List<String> uids);
	List<Connection> findByconnectionnameLikeIgnoreCaseAndConnectionuidNotIn(String name, List<String> uids);
	List<Connection> findByconnectionnameLikeIgnoreCaseAndConnectionuidNotIn(String name, Sort sort, List<String> uids);
	Page<Connection> findByconnectionnameLikeIgnoreCaseAndConnectionuidNotIn(String name, Pageable pageable, List<String> uids);
	
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
	//description field with In Connectionuid constraints
	List<Connection> findBydescriptionAndConnectionuidIn(String description, List<String> uids);
	List<Connection> findBydescriptionAndConnectionuidIn(String description, Sort sort, List<String> uids);
	Page<Connection> findBydescriptionAndConnectionuidIn(String description, Pageable pageable, List<String> uids);
	List<Connection> findBydescriptionIgnoreCaseAndConnectionuidIn(String description, List<String> uids);
	List<Connection> findBydescriptionIgnoreCaseAndConnectionuidIn(String description, Sort sort, List<String> uids);
	Page<Connection> findBydescriptionIgnoreCaseAndConnectionuidIn(String description, Pageable pageable, List<String> uids);
	List<Connection> findBydescriptionLikeAndConnectionuidIn(String description, List<String> uids);
	List<Connection> findBydescriptionLikeAndConnectionuidIn(String description, Sort sort, List<String> uids);
	Page<Connection> findBydescriptionLikeAndConnectionuidIn(String description, Pageable pageable, List<String> uids);
	List<Connection> findBydescriptionLikeIgnoreCaseAndConnectionuidIn(String description, List<String> uids);
	List<Connection> findBydescriptionLikeIgnoreCaseAndConnectionuidIn(String description, Sort sort, List<String> uids);
	Page<Connection> findBydescriptionLikeIgnoreCaseAndConnectionuidIn(String description, Pageable pageable, List<String> uids);
	//description field with Not In Connectionuid constraints
	List<Connection> findBydescriptionAndConnectionuidNotIn(String description, List<String> uids);
	List<Connection> findBydescriptionAndConnectionuidNotIn(String description, Sort sort, List<String> uids);
	Page<Connection> findBydescriptionAndConnectionuidNotIn(String description, Pageable pageable, List<String> uids);
	List<Connection> findBydescriptionIgnoreCaseAndConnectionuidNotIn(String description, List<String> uids);
	List<Connection> findBydescriptionIgnoreCaseAndConnectionuidNotIn(String description, Sort sort, List<String> uids);
	Page<Connection> findBydescriptionIgnoreCaseAndConnectionuidNotIn(String description, Pageable pageable, List<String> uids);
	List<Connection> findBydescriptionLikeAndConnectionuidNotIn(String description, List<String> uids);
	List<Connection> findBydescriptionLikeAndConnectionuidNotIn(String description, Sort sort, List<String> uids);
	Page<Connection> findBydescriptionLikeAndConnectionuidNotIn(String description, Pageable pageable, List<String> uids);
	List<Connection> findBydescriptionLikeIgnoreCaseAndConnectionuidNotIn(String description, List<String> uids);
	List<Connection> findBydescriptionLikeIgnoreCaseAndConnectionuidNotIn(String description, Sort sort, List<String> uids);
	Page<Connection> findBydescriptionLikeIgnoreCaseAndConnectionuidNotIn(String description, Pageable pageable, List<String> uids);
	
	//connection type field
	List<Connection> findByconnectiontype(String name);
	List<Connection> findByconnectiontype(String name, Sort sort);
	Page<Connection> findByconnectiontype(String name, Pageable pageable);
	List<Connection> findByconnectiontypeIgnoreCase(String name);
	List<Connection> findByconnectiontypeIgnoreCase(String name, Sort sort);
	Page<Connection> findByconnectiontypeIgnoreCase(String name, Pageable pageable);
	List<Connection> findByconnectiontypeLike(String name);
	List<Connection> findByconnectiontypeLike(String name, Sort sort);
	Page<Connection> findByconnectiontypeLike(String name, Pageable pageable);
	List<Connection> findByconnectiontypeLikeIgnoreCase(String name);
	List<Connection> findByconnectiontypeLikeIgnoreCase(String name, Sort sort);
	Page<Connection> findByconnectiontypeLikeIgnoreCase(String name, Pageable pageable);
	//connection type field with In Connectionuid constraints
	List<Connection> findByconnectiontypeAndConnectionuidIn(String name, List<String> uids);
	List<Connection> findByconnectiontypeAndConnectionuidIn(String name, Sort sort, List<String> uids);
	Page<Connection> findByconnectiontypeAndConnectionuidIn(String name, Pageable pageable, List<String> uids);
	List<Connection> findByconnectiontypeIgnoreCaseAndConnectionuidIn(String name, List<String> uids);
	List<Connection> findByconnectiontypeIgnoreCaseAndConnectionuidIn(String name, Sort sort, List<String> uids);
	Page<Connection> findByconnectiontypeIgnoreCaseAndConnectionuidIn(String name, Pageable pageable, List<String> uids);
	List<Connection> findByconnectiontypeLikeAndConnectionuidIn(String name, List<String> uids);
	List<Connection> findByconnectiontypeLikeAndConnectionuidIn(String name, Sort sort, List<String> uids);
	Page<Connection> findByconnectiontypeLikeAndConnectionuidIn(String name, Pageable pageable, List<String> uids);
	List<Connection> findByconnectiontypeLikeIgnoreCaseAndConnectionuidIn(String name, List<String> uids);
	List<Connection> findByconnectiontypeLikeIgnoreCaseAndConnectionuidIn(String name, Sort sort, List<String> uids);
	Page<Connection> findByconnectiontypeLikeIgnoreCaseAndConnectionuidIn(String name, Pageable pageable, List<String> uids);
	//connection type field with Not In Connectionuid constraints
	List<Connection> findByconnectiontypeAndConnectionuidNotIn(String name, List<String> uids);
	List<Connection> findByconnectiontypeAndConnectionuidNotIn(String name, Sort sort, List<String> uids);
	Page<Connection> findByconnectiontypeAndConnectionuidNotIn(String name, Pageable pageable, List<String> uids);
	List<Connection> findByconnectiontypeIgnoreCaseAndConnectionuidNotIn(String name, List<String> uids);
	List<Connection> findByconnectiontypeIgnoreCaseAndConnectionuidNotIn(String name, Sort sort, List<String> uids);
	Page<Connection> findByconnectiontypeIgnoreCaseAndConnectionuidNotIn(String name, Pageable pageable, List<String> uids);
	List<Connection> findByconnectiontypeLikeAndConnectionuidNotIn(String name, List<String> uids);
	List<Connection> findByconnectiontypeLikeAndConnectionuidNotIn(String name, Sort sort, List<String> uids);
	Page<Connection> findByconnectiontypeLikeAndConnectionuidNotIn(String name, Pageable pageable, List<String> uids);
	List<Connection> findByconnectiontypeLikeIgnoreCaseAndConnectionuidNotIn(String name, List<String> uids);
	List<Connection> findByconnectiontypeLikeIgnoreCaseAndConnectionuidNotIn(String name, Sort sort, List<String> uids);
	Page<Connection> findByconnectiontypeLikeIgnoreCaseAndConnectionuidNotIn(String name, Pageable pageable, List<String> uids);
}