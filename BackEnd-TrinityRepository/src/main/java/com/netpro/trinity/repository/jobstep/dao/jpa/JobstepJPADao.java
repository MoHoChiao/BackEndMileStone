package com.netpro.trinity.repository.jobstep.dao.jpa;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.repository.jobstep.entity.jpa.Jobstep;

@Repository  //宣告這是一個DAO類別
public interface JobstepJPADao extends JpaRepository<Jobstep, String> {
	@Query("select count(step)>0 from Jobstep step where step.stepname=:stepname AND 1=1")
	Boolean existByName(@Param("stepname") String stepname);
	
	//job step type field
	List<Jobstep> findBysteptype(String name);
	List<Jobstep> findBysteptype(String name, Sort sort);
	Page<Jobstep> findBysteptype(String name, Pageable pageable);
	List<Jobstep> findBysteptypeIgnoreCase(String name);
	List<Jobstep> findBysteptypeIgnoreCase(String name, Sort sort);
	Page<Jobstep> findBysteptypeIgnoreCase(String name, Pageable pageable);
	List<Jobstep> findBysteptypeLike(String name);
	List<Jobstep> findBysteptypeLike(String name, Sort sort);
	Page<Jobstep> findBysteptypeLike(String name, Pageable pageable);
	List<Jobstep> findBysteptypeLikeIgnoreCase(String name);
	List<Jobstep> findBysteptypeLikeIgnoreCase(String name, Sort sort);
	Page<Jobstep> findBysteptypeLikeIgnoreCase(String name, Pageable pageable);
}