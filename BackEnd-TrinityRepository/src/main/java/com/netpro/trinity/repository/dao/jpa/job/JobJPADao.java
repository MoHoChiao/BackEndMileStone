package com.netpro.trinity.repository.dao.jpa.job;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.repository.entity.job.jpa.Job;

@Repository  //宣告這是一個DAO類別
public interface JobJPADao extends JpaRepository<Job, String> {
	@Query("select count(job)>0 from Job job where job.jobname=:jobname AND 1=1")
	Boolean existByName(@Param("jobname") String jobname);
	
	@Query("select count(job)>0 from Job job where job.filesourceuid=:filesourceuid AND 1=1")
	Boolean existByFilesourceuid(@Param("filesourceuid") String filesourceuid);
	
	//category uid field
	List<Job> findByCategoryuid(String uid);
	List<Job> findByCategoryuid(String uid, Sort sort);
	Page<Job> findByCategoryuid(String uid, Pageable pageable);
		
		
	//job name field
	List<Job> findByjobname(String name);
	List<Job> findByjobname(String name, Sort sort);
	Page<Job> findByjobname(String name, Pageable pageable);
	List<Job> findByjobnameIgnoreCase(String name);
	List<Job> findByjobnameIgnoreCase(String name, Sort sort);
	Page<Job> findByjobnameIgnoreCase(String name, Pageable pageable);
	List<Job> findByjobnameLike(String name);
	List<Job> findByjobnameLike(String name, Sort sort);
	Page<Job> findByjobnameLike(String name, Pageable pageable);
	List<Job> findByjobnameLikeIgnoreCase(String name);
	List<Job> findByjobnameLikeIgnoreCase(String name, Sort sort);
	Page<Job> findByjobnameLikeIgnoreCase(String name, Pageable pageable);
	//job name field with jobuid constraints
	List<Job> findByjobnameAndCategoryuid(String name, String uid);
	List<Job> findByjobnameAndCategoryuid(String name, Sort sort, String uid);
	Page<Job> findByjobnameAndCategoryuid(String name, Pageable pageable, String uid);
	List<Job> findByjobnameIgnoreCaseAndCategoryuid(String name, String uid);
	List<Job> findByjobnameIgnoreCaseAndCategoryuid(String name, Sort sort, String uid);
	Page<Job> findByjobnameIgnoreCaseAndCategoryuid(String name, Pageable pageable, String uid);
	List<Job> findByjobnameLikeAndCategoryuid(String name, String uid);
	List<Job> findByjobnameLikeAndCategoryuid(String name, Sort sort, String uid);
	Page<Job> findByjobnameLikeAndCategoryuid(String name, Pageable pageable, String uid);
	List<Job> findByjobnameLikeIgnoreCaseAndCategoryuid(String name, String uid);
	List<Job> findByjobnameLikeIgnoreCaseAndCategoryuid(String name, Sort sort, String uid);
	Page<Job> findByjobnameLikeIgnoreCaseAndCategoryuid(String name, Pageable pageable, String uid);
	
	//activate field
	List<Job> findByactivate(String activate);
	List<Job> findByactivate(String activate, Sort sort);
	Page<Job> findByactivate(String activate, Pageable pageable);
	List<Job> findByactivateIgnoreCase(String activate);
	List<Job> findByactivateIgnoreCase(String activate, Sort sort);
	Page<Job> findByactivateIgnoreCase(String activate, Pageable pageable);
	List<Job> findByactivateLike(String activate);
	List<Job> findByactivateLike(String activate, Sort sort);
	Page<Job> findByactivateLike(String activate, Pageable pageable);
	List<Job> findByactivateLikeIgnoreCase(String activate);
	List<Job> findByactivateLikeIgnoreCase(String activate, Sort sort);
	Page<Job> findByactivateLikeIgnoreCase(String activate, Pageable pageable);
	//activate field with categoryuid constraints
	List<Job> findByactivateAndCategoryuid(String activate, String uid);
	List<Job> findByactivateAndCategoryuid(String activate, Sort sort, String uid);
	Page<Job> findByactivateAndCategoryuid(String activate, Pageable pageable, String uid);
	List<Job> findByactivateIgnoreCaseAndCategoryuid(String activate, String uid);
	List<Job> findByactivateIgnoreCaseAndCategoryuid(String activate, Sort sort, String uid);
	Page<Job> findByactivateIgnoreCaseAndCategoryuid(String activate, Pageable pageable, String uid);
	List<Job> findByactivateLikeAndCategoryuid(String activate, String uid);
	List<Job> findByactivateLikeAndCategoryuid(String activate, Sort sort, String uid);
	Page<Job> findByactivateLikeAndCategoryuid(String activate, Pageable pageable, String uid);
	List<Job> findByactivateLikeIgnoreCaseAndCategoryuid(String activate, String uid);
	List<Job> findByactivateLikeIgnoreCaseAndCategoryuid(String activate, Sort sort, String uid);
	Page<Job> findByactivateLikeIgnoreCaseAndCategoryuid(String activate, Pageable pageable, String uid);
	
		
	//description field
	List<Job> findBydescription(String description);
	List<Job> findBydescription(String description, Sort sort);
	Page<Job> findBydescription(String description, Pageable pageable);
	List<Job> findBydescriptionIgnoreCase(String description);
	List<Job> findBydescriptionIgnoreCase(String description, Sort sort);
	Page<Job> findBydescriptionIgnoreCase(String description, Pageable pageable);
	List<Job> findBydescriptionLike(String description);
	List<Job> findBydescriptionLike(String description, Sort sort);
	Page<Job> findBydescriptionLike(String description, Pageable pageable);
	List<Job> findBydescriptionLikeIgnoreCase(String description);
	List<Job> findBydescriptionLikeIgnoreCase(String description, Sort sort);
	Page<Job> findBydescriptionLikeIgnoreCase(String description, Pageable pageable);
	//description field with categoryuid constraints
	List<Job> findBydescriptionAndCategoryuid(String description, String uid);
	List<Job> findBydescriptionAndCategoryuid(String description, Sort sort, String uid);
	Page<Job> findBydescriptionAndCategoryuid(String description, Pageable pageable, String uid);
	List<Job> findBydescriptionIgnoreCaseAndCategoryuid(String description, String uid);
	List<Job> findBydescriptionIgnoreCaseAndCategoryuid(String description, Sort sort, String uid);
	Page<Job> findBydescriptionIgnoreCaseAndCategoryuid(String description, Pageable pageable, String uid);
	List<Job> findBydescriptionLikeAndCategoryuid(String description, String uid);
	List<Job> findBydescriptionLikeAndCategoryuid(String description, Sort sort, String uid);
	Page<Job> findBydescriptionLikeAndCategoryuid(String description, Pageable pageable, String uid);
	List<Job> findBydescriptionLikeIgnoreCaseAndCategoryuid(String description, String uid);
	List<Job> findBydescriptionLikeIgnoreCaseAndCategoryuid(String description, Sort sort, String uid);
	Page<Job> findBydescriptionLikeIgnoreCaseAndCategoryuid(String description, Pageable pageable, String uid);
}	