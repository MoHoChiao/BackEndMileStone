package com.netpro.trinity.service.job.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.service.job.entity.JobFlow;

@Repository  //宣告這是一個DAO類別
public interface JobFlowJPADao extends JpaRepository<JobFlow, String> {
	@Query("select count(flow)>0 from jobflow flow where flow.flowname=:flowname AND 1=1")
	Boolean existByName(@Param("flowname") String flowname);
	
	@Query("select count(flow)>0 from jobflow flow where flow.frequencyuid=:frequencyuid AND 1=1")
	Boolean existByFrequencyuid(@Param("frequencyuid") String frequencyuid);
	
	//category uid field
	List<JobFlow> findByCategoryuid(String uid);
	List<JobFlow> findByCategoryuid(String uid, Sort sort);
	Page<JobFlow> findByCategoryuid(String uid, Pageable pageable);
		
		
	//flow name field
	List<JobFlow> findByflowname(String name);
	List<JobFlow> findByflowname(String name, Sort sort);
	Page<JobFlow> findByflowname(String name, Pageable pageable);
	List<JobFlow> findByflownameIgnoreCase(String name);
	List<JobFlow> findByflownameIgnoreCase(String name, Sort sort);
	Page<JobFlow> findByflownameIgnoreCase(String name, Pageable pageable);
	List<JobFlow> findByflownameLike(String name);
	List<JobFlow> findByflownameLike(String name, Sort sort);
	Page<JobFlow> findByflownameLike(String name, Pageable pageable);
	List<JobFlow> findByflownameLikeIgnoreCase(String name);
	List<JobFlow> findByflownameLikeIgnoreCase(String name, Sort sort);
	Page<JobFlow> findByflownameLikeIgnoreCase(String name, Pageable pageable);
	//job name field with jobuid constraints
	List<JobFlow> findByflownameAndCategoryuid(String name, String uid);
	List<JobFlow> findByflownameAndCategoryuid(String name, Sort sort, String uid);
	Page<JobFlow> findByflownameAndCategoryuid(String name, Pageable pageable, String uid);
	List<JobFlow> findByflownameIgnoreCaseAndCategoryuid(String name, String uid);
	List<JobFlow> findByflownameIgnoreCaseAndCategoryuid(String name, Sort sort, String uid);
	Page<JobFlow> findByflownameIgnoreCaseAndCategoryuid(String name, Pageable pageable, String uid);
	List<JobFlow> findByflownameLikeAndCategoryuid(String name, String uid);
	List<JobFlow> findByflownameLikeAndCategoryuid(String name, Sort sort, String uid);
	Page<JobFlow> findByflownameLikeAndCategoryuid(String name, Pageable pageable, String uid);
	List<JobFlow> findByflownameLikeIgnoreCaseAndCategoryuid(String name, String uid);
	List<JobFlow> findByflownameLikeIgnoreCaseAndCategoryuid(String name, Sort sort, String uid);
	Page<JobFlow> findByflownameLikeIgnoreCaseAndCategoryuid(String name, Pageable pageable, String uid);
	
	//activate field
	List<JobFlow> findByactivate(String activate);
	List<JobFlow> findByactivate(String activate, Sort sort);
	Page<JobFlow> findByactivate(String activate, Pageable pageable);
	List<JobFlow> findByactivateIgnoreCase(String activate);
	List<JobFlow> findByactivateIgnoreCase(String activate, Sort sort);
	Page<JobFlow> findByactivateIgnoreCase(String activate, Pageable pageable);
	List<JobFlow> findByactivateLike(String activate);
	List<JobFlow> findByactivateLike(String activate, Sort sort);
	Page<JobFlow> findByactivateLike(String activate, Pageable pageable);
	List<JobFlow> findByactivateLikeIgnoreCase(String activate);
	List<JobFlow> findByactivateLikeIgnoreCase(String activate, Sort sort);
	Page<JobFlow> findByactivateLikeIgnoreCase(String activate, Pageable pageable);
	//activate field with categoryuid constraints
	List<JobFlow> findByactivateAndCategoryuid(String activate, String uid);
	List<JobFlow> findByactivateAndCategoryuid(String activate, Sort sort, String uid);
	Page<JobFlow> findByactivateAndCategoryuid(String activate, Pageable pageable, String uid);
	List<JobFlow> findByactivateIgnoreCaseAndCategoryuid(String activate, String uid);
	List<JobFlow> findByactivateIgnoreCaseAndCategoryuid(String activate, Sort sort, String uid);
	Page<JobFlow> findByactivateIgnoreCaseAndCategoryuid(String activate, Pageable pageable, String uid);
	List<JobFlow> findByactivateLikeAndCategoryuid(String activate, String uid);
	List<JobFlow> findByactivateLikeAndCategoryuid(String activate, Sort sort, String uid);
	Page<JobFlow> findByactivateLikeAndCategoryuid(String activate, Pageable pageable, String uid);
	List<JobFlow> findByactivateLikeIgnoreCaseAndCategoryuid(String activate, String uid);
	List<JobFlow> findByactivateLikeIgnoreCaseAndCategoryuid(String activate, Sort sort, String uid);
	Page<JobFlow> findByactivateLikeIgnoreCaseAndCategoryuid(String activate, Pageable pageable, String uid);
	
		
	//description field
	List<JobFlow> findBydescription(String description);
	List<JobFlow> findBydescription(String description, Sort sort);
	Page<JobFlow> findBydescription(String description, Pageable pageable);
	List<JobFlow> findBydescriptionIgnoreCase(String description);
	List<JobFlow> findBydescriptionIgnoreCase(String description, Sort sort);
	Page<JobFlow> findBydescriptionIgnoreCase(String description, Pageable pageable);
	List<JobFlow> findBydescriptionLike(String description);
	List<JobFlow> findBydescriptionLike(String description, Sort sort);
	Page<JobFlow> findBydescriptionLike(String description, Pageable pageable);
	List<JobFlow> findBydescriptionLikeIgnoreCase(String description);
	List<JobFlow> findBydescriptionLikeIgnoreCase(String description, Sort sort);
	Page<JobFlow> findBydescriptionLikeIgnoreCase(String description, Pageable pageable);
	//description field with categoryuid constraints
	List<JobFlow> findBydescriptionAndCategoryuid(String description, String uid);
	List<JobFlow> findBydescriptionAndCategoryuid(String description, Sort sort, String uid);
	Page<JobFlow> findBydescriptionAndCategoryuid(String description, Pageable pageable, String uid);
	List<JobFlow> findBydescriptionIgnoreCaseAndCategoryuid(String description, String uid);
	List<JobFlow> findBydescriptionIgnoreCaseAndCategoryuid(String description, Sort sort, String uid);
	Page<JobFlow> findBydescriptionIgnoreCaseAndCategoryuid(String description, Pageable pageable, String uid);
	List<JobFlow> findBydescriptionLikeAndCategoryuid(String description, String uid);
	List<JobFlow> findBydescriptionLikeAndCategoryuid(String description, Sort sort, String uid);
	Page<JobFlow> findBydescriptionLikeAndCategoryuid(String description, Pageable pageable, String uid);
	List<JobFlow> findBydescriptionLikeIgnoreCaseAndCategoryuid(String description, String uid);
	List<JobFlow> findBydescriptionLikeIgnoreCaseAndCategoryuid(String description, Sort sort, String uid);
	Page<JobFlow> findBydescriptionLikeIgnoreCaseAndCategoryuid(String description, Pageable pageable, String uid);
}	