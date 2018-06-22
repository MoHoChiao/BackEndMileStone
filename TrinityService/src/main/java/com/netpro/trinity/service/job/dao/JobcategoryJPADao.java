package com.netpro.trinity.service.job.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.service.job.entity.Jobcategory;

@Repository  //宣告這是一個DAO類別
public interface JobcategoryJPADao extends JpaRepository<Jobcategory, String> {
	@Query("select count(category)>0 from Jobcategory category where category.categoryname=:categoryname AND 1=1")
	Boolean existByName(@Param("categoryname") String categoryname);
	
	//category uid field
	List<Jobcategory> findByCategoryuidIn(List<String> uids);
	List<Jobcategory> findByCategoryuidIn(List<String> uids, Sort sort);
	List<Jobcategory> findByCategoryuidNotIn(List<String> uids, Sort sort);
	Page<Jobcategory> findByCategoryuidIn(List<String> uids, Pageable pageable);
	
	
	//category name field
	List<Jobcategory> findBycategoryname(String name);
	List<Jobcategory> findBycategoryname(String name, Sort sort);
	Page<Jobcategory> findBycategoryname(String name, Pageable pageable);
	List<Jobcategory> findBycategorynameIgnoreCase(String name);
	List<Jobcategory> findBycategorynameIgnoreCase(String name, Sort sort);
	Page<Jobcategory> findBycategorynameIgnoreCase(String name, Pageable pageable);
	List<Jobcategory> findBycategorynameLike(String name);
	List<Jobcategory> findBycategorynameLike(String name, Sort sort);
	Page<Jobcategory> findBycategorynameLike(String name, Pageable pageable);
	List<Jobcategory> findBycategorynameLikeIgnoreCase(String name);
	List<Jobcategory> findBycategorynameLikeIgnoreCase(String name, Sort sort);
	Page<Jobcategory> findBycategorynameLikeIgnoreCase(String name, Pageable pageable);
	//category name field with categoryUid constraints
	List<Jobcategory> findBycategorynameAndCategoryuidIn(String name, List<String> uids);
	List<Jobcategory> findBycategorynameAndCategoryuidIn(String name, Sort sort, List<String> uids);
	Page<Jobcategory> findBycategorynameAndCategoryuidIn(String name, Pageable pageable, List<String> uids);
	List<Jobcategory> findBycategorynameIgnoreCaseAndCategoryuidIn(String name, List<String> uids);
	List<Jobcategory> findBycategorynameIgnoreCaseAndCategoryuidIn(String name, Sort sort, List<String> uids);
	Page<Jobcategory> findBycategorynameIgnoreCaseAndCategoryuidIn(String name, Pageable pageable, List<String> uids);
	List<Jobcategory> findBycategorynameLikeAndCategoryuidIn(String name, List<String> uids);
	List<Jobcategory> findBycategorynameLikeAndCategoryuidIn(String name, Sort sort, List<String> uids);
	Page<Jobcategory> findBycategorynameLikeAndCategoryuidIn(String name, Pageable pageable, List<String> uids);
	List<Jobcategory> findBycategorynameLikeIgnoreCaseAndCategoryuidIn(String name, List<String> uids);
	List<Jobcategory> findBycategorynameLikeIgnoreCaseAndCategoryuidIn(String name, Sort sort, List<String> uids);
	Page<Jobcategory> findBycategorynameLikeIgnoreCaseAndCategoryuidIn(String name, Pageable pageable, List<String> uids);
	
	
	//description field
	List<Jobcategory> findBydescription(String description);
	List<Jobcategory> findBydescription(String description, Sort sort);
	Page<Jobcategory> findBydescription(String description, Pageable pageable);
	List<Jobcategory> findBydescriptionIgnoreCase(String description);
	List<Jobcategory> findBydescriptionIgnoreCase(String description, Sort sort);
	Page<Jobcategory> findBydescriptionIgnoreCase(String description, Pageable pageable);
	List<Jobcategory> findBydescriptionLike(String description);
	List<Jobcategory> findBydescriptionLike(String description, Sort sort);
	Page<Jobcategory> findBydescriptionLike(String description, Pageable pageable);
	List<Jobcategory> findBydescriptionLikeIgnoreCase(String description);
	List<Jobcategory> findBydescriptionLikeIgnoreCase(String description, Sort sort);
	Page<Jobcategory> findBydescriptionLikeIgnoreCase(String description, Pageable pageable);
	//description field with categoryUid constraints
	List<Jobcategory> findBydescriptionAndCategoryuidIn(String description, List<String> uids);
	List<Jobcategory> findBydescriptionAndCategoryuidIn(String description, Sort sort, List<String> uids);
	Page<Jobcategory> findBydescriptionAndCategoryuidIn(String description, Pageable pageable, List<String> uids);
	List<Jobcategory> findBydescriptionIgnoreCaseAndCategoryuidIn(String description, List<String> uids);
	List<Jobcategory> findBydescriptionIgnoreCaseAndCategoryuidIn(String description, Sort sort, List<String> uids);
	Page<Jobcategory> findBydescriptionIgnoreCaseAndCategoryuidIn(String description, Pageable pageable, List<String> uids);
	List<Jobcategory> findBydescriptionLikeAndCategoryuidIn(String description, List<String> uids);
	List<Jobcategory> findBydescriptionLikeAndCategoryuidIn(String description, Sort sort, List<String> uids);
	Page<Jobcategory> findBydescriptionLikeAndCategoryuidIn(String description, Pageable pageable, List<String> uids);
	List<Jobcategory> findBydescriptionLikeIgnoreCaseAndCategoryuidIn(String description, List<String> uids);
	List<Jobcategory> findBydescriptionLikeIgnoreCaseAndCategoryuidIn(String description, Sort sort, List<String> uids);
	Page<Jobcategory> findBydescriptionLikeIgnoreCaseAndCategoryuidIn(String description, Pageable pageable, List<String> uids);
	
	//activate field
	List<Jobcategory> findByactivate(String activate);
	List<Jobcategory> findByactivate(String activate, Sort sort);
	Page<Jobcategory> findByactivate(String activate, Pageable pageable);
	List<Jobcategory> findByactivateIgnoreCase(String activate);
	List<Jobcategory> findByactivateIgnoreCase(String activate, Sort sort);
	Page<Jobcategory> findByactivateIgnoreCase(String activate, Pageable pageable);
	List<Jobcategory> findByactivateLike(String activate);
	List<Jobcategory> findByactivateLike(String activate, Sort sort);
	Page<Jobcategory> findByactivateLike(String activate, Pageable pageable);
	List<Jobcategory> findByactivateLikeIgnoreCase(String activate);
	List<Jobcategory> findByactivateLikeIgnoreCase(String activate, Sort sort);
	Page<Jobcategory> findByactivateLikeIgnoreCase(String activate, Pageable pageable);
	//activate field with categoryUid constraints
	List<Jobcategory> findByactivateAndCategoryuidIn(String activate, List<String> uids);
	List<Jobcategory> findByactivateAndCategoryuidIn(String activate, Sort sort, List<String> uids);
	Page<Jobcategory> findByactivateAndCategoryuidIn(String activate, Pageable pageable, List<String> uids);
	List<Jobcategory> findByactivateIgnoreCaseAndCategoryuidIn(String activate, List<String> uids);
	List<Jobcategory> findByactivateIgnoreCaseAndCategoryuidIn(String activate, Sort sort, List<String> uids);
	Page<Jobcategory> findByactivateIgnoreCaseAndCategoryuidIn(String activate, Pageable pageable, List<String> uids);
	List<Jobcategory> findByactivateLikeAndCategoryuidIn(String activate, List<String> uids);
	List<Jobcategory> findByactivateLikeAndCategoryuidIn(String activate, Sort sort, List<String> uids);
	Page<Jobcategory> findByactivateLikeAndCategoryuidIn(String activate, Pageable pageable, List<String> uids);
	List<Jobcategory> findByactivateLikeIgnoreCaseAndCategoryuidIn(String activate, List<String> uids);
	List<Jobcategory> findByactivateLikeIgnoreCaseAndCategoryuidIn(String activate, Sort sort, List<String> uids);
	Page<Jobcategory> findByactivateLikeIgnoreCaseAndCategoryuidIn(String activate, Pageable pageable, List<String> uids);
}

