package com.netpro.trinity.resource.admin.frequency.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.resource.admin.frequency.entity.Frequency;

@Repository  //宣告這是一個DAO類別
public interface FrequencyJPADao extends JpaRepository<Frequency, String> {
	@Query("select case when count(freq)>0 then true else false end from Frequency freq where freq.frequencyname=:frequencyname AND 1=1")
	Boolean existByName(@Param("frequencyname") String frequencyname);
	
	@Query("select case when count(freq)>0 then true else false end from Frequency freq where freq.wcalendaruid=:wcalendaruid AND 1=1")
	Boolean existByWCalendaruid(@Param("wcalendaruid") String wcalendaruid);
	
	//Frequency uid field with In
	List<Frequency> findByFrequencyuidIn(List<String> uids);
	List<Frequency> findByFrequencyuidIn(List<String> uids, Sort sort);
	Page<Frequency> findByFrequencyuidIn(List<String> uids, Pageable pageable);
	//Frequency uid field with Not In
	List<Frequency> findByFrequencyuidNotIn(List<String> uids);
	List<Frequency> findByFrequencyuidNotIn(List<String> uids, Sort sort);
	Page<Frequency> findByFrequencyuidNotIn(List<String> uids, Pageable pageable);
		
		
	//frequencyname name field
	List<Frequency> findByfrequencyname(String name);
	List<Frequency> findByfrequencyname(String name, Sort sort);
	Page<Frequency> findByfrequencyname(String name, Pageable pageable);
	List<Frequency> findByfrequencynameIgnoreCase(String name);
	List<Frequency> findByfrequencynameIgnoreCase(String name, Sort sort);
	Page<Frequency> findByfrequencynameIgnoreCase(String name, Pageable pageable);
	List<Frequency> findByfrequencynameLike(String name);
	List<Frequency> findByfrequencynameLike(String name, Sort sort);
	Page<Frequency> findByfrequencynameLike(String name, Pageable pageable);
	List<Frequency> findByfrequencynameLikeIgnoreCase(String name);
	List<Frequency> findByfrequencynameLikeIgnoreCase(String name, Sort sort);
	Page<Frequency> findByfrequencynameLikeIgnoreCase(String name, Pageable pageable);
	//Frequency name field with In Frequencyuid constraints
	List<Frequency> findByfrequencynameAndFrequencyuidIn(String name, List<String> uids);
	List<Frequency> findByfrequencynameAndFrequencyuidIn(String name, Sort sort, List<String> uids);
	Page<Frequency> findByfrequencynameAndFrequencyuidIn(String name, Pageable pageable, List<String> uids);
	List<Frequency> findByfrequencynameIgnoreCaseAndFrequencyuidIn(String name, List<String> uids);
	List<Frequency> findByfrequencynameIgnoreCaseAndFrequencyuidIn(String name, Sort sort, List<String> uids);
	Page<Frequency> findByfrequencynameIgnoreCaseAndFrequencyuidIn(String name, Pageable pageable, List<String> uids);
	List<Frequency> findByfrequencynameLikeAndFrequencyuidIn(String name, List<String> uids);
	List<Frequency> findByfrequencynameLikeAndFrequencyuidIn(String name, Sort sort, List<String> uids);
	Page<Frequency> findByfrequencynameLikeAndFrequencyuidIn(String name, Pageable pageable, List<String> uids);
	List<Frequency> findByfrequencynameLikeIgnoreCaseAndFrequencyuidIn(String name, List<String> uids);
	List<Frequency> findByfrequencynameLikeIgnoreCaseAndFrequencyuidIn(String name, Sort sort, List<String> uids);
	Page<Frequency> findByfrequencynameLikeIgnoreCaseAndFrequencyuidIn(String name, Pageable pageable, List<String> uids);
	//Frequency name field with Not In Frequencyuid constraints
	List<Frequency> findByfrequencynameAndFrequencyuidNotIn(String name, List<String> uids);
	List<Frequency> findByfrequencynameAndFrequencyuidNotIn(String name, Sort sort, List<String> uids);
	Page<Frequency> findByfrequencynameAndFrequencyuidNotIn(String name, Pageable pageable, List<String> uids);
	List<Frequency> findByfrequencynameIgnoreCaseAndFrequencyuidNotIn(String name, List<String> uids);
	List<Frequency> findByfrequencynameIgnoreCaseAndFrequencyuidNotIn(String name, Sort sort, List<String> uids);
	Page<Frequency> findByfrequencynameIgnoreCaseAndFrequencyuidNotIn(String name, Pageable pageable, List<String> uids);
	List<Frequency> findByfrequencynameLikeAndFrequencyuidNotIn(String name, List<String> uids);
	List<Frequency> findByfrequencynameLikeAndFrequencyuidNotIn(String name, Sort sort, List<String> uids);
	Page<Frequency> findByfrequencynameLikeAndFrequencyuidNotIn(String name, Pageable pageable, List<String> uids);
	List<Frequency> findByfrequencynameLikeIgnoreCaseAndFrequencyuidNotIn(String name, List<String> uids);
	List<Frequency> findByfrequencynameLikeIgnoreCaseAndFrequencyuidNotIn(String name, Sort sort, List<String> uids);
	Page<Frequency> findByfrequencynameLikeIgnoreCaseAndFrequencyuidNotIn(String name, Pageable pageable, List<String> uids);
		
	//description field
	List<Frequency> findBydescription(String description);
	List<Frequency> findBydescription(String description, Sort sort);
	Page<Frequency> findBydescription(String description, Pageable pageable);
	List<Frequency> findBydescriptionIgnoreCase(String description);
	List<Frequency> findBydescriptionIgnoreCase(String description, Sort sort);
	Page<Frequency> findBydescriptionIgnoreCase(String description, Pageable pageable);
	List<Frequency> findBydescriptionLike(String description);
	List<Frequency> findBydescriptionLike(String description, Sort sort);
	Page<Frequency> findBydescriptionLike(String description, Pageable pageable);
	List<Frequency> findBydescriptionLikeIgnoreCase(String description);
	List<Frequency> findBydescriptionLikeIgnoreCase(String description, Sort sort);
	Page<Frequency> findBydescriptionLikeIgnoreCase(String description, Pageable pageable);
	//description field with In Frequencyuid constraints
	List<Frequency> findBydescriptionAndFrequencyuidIn(String description, List<String> uids);
	List<Frequency> findBydescriptionAndFrequencyuidIn(String description, Sort sort, List<String> uids);
	Page<Frequency> findBydescriptionAndFrequencyuidIn(String description, Pageable pageable, List<String> uids);
	List<Frequency> findBydescriptionIgnoreCaseAndFrequencyuidIn(String description, List<String> uids);
	List<Frequency> findBydescriptionIgnoreCaseAndFrequencyuidIn(String description, Sort sort, List<String> uids);
	Page<Frequency> findBydescriptionIgnoreCaseAndFrequencyuidIn(String description, Pageable pageable, List<String> uids);
	List<Frequency> findBydescriptionLikeAndFrequencyuidIn(String description, List<String> uids);
	List<Frequency> findBydescriptionLikeAndFrequencyuidIn(String description, Sort sort, List<String> uids);
	Page<Frequency> findBydescriptionLikeAndFrequencyuidIn(String description, Pageable pageable, List<String> uids);
	List<Frequency> findBydescriptionLikeIgnoreCaseAndFrequencyuidIn(String description, List<String> uids);
	List<Frequency> findBydescriptionLikeIgnoreCaseAndFrequencyuidIn(String description, Sort sort, List<String> uids);
	Page<Frequency> findBydescriptionLikeIgnoreCaseAndFrequencyuidIn(String description, Pageable pageable, List<String> uids);
	//description field with Not In Frequencyuid constraints
	List<Frequency> findBydescriptionAndFrequencyuidNotIn(String description, List<String> uids);
	List<Frequency> findBydescriptionAndFrequencyuidNotIn(String description, Sort sort, List<String> uids);
	Page<Frequency> findBydescriptionAndFrequencyuidNotIn(String description, Pageable pageable, List<String> uids);
	List<Frequency> findBydescriptionIgnoreCaseAndFrequencyuidNotIn(String description, List<String> uids);
	List<Frequency> findBydescriptionIgnoreCaseAndFrequencyuidNotIn(String description, Sort sort, List<String> uids);
	Page<Frequency> findBydescriptionIgnoreCaseAndFrequencyuidNotIn(String description, Pageable pageable, List<String> uids);
	List<Frequency> findBydescriptionLikeAndFrequencyuidNotIn(String description, List<String> uids);
	List<Frequency> findBydescriptionLikeAndFrequencyuidNotIn(String description, Sort sort, List<String> uids);
	Page<Frequency> findBydescriptionLikeAndFrequencyuidNotIn(String description, Pageable pageable, List<String> uids);
	List<Frequency> findBydescriptionLikeIgnoreCaseAndFrequencyuidNotIn(String description, List<String> uids);
	List<Frequency> findBydescriptionLikeIgnoreCaseAndFrequencyuidNotIn(String description, Sort sort, List<String> uids);
	Page<Frequency> findBydescriptionLikeIgnoreCaseAndFrequencyuidNotIn(String description, Pageable pageable, List<String> uids);
		
	//activate field
	List<Frequency> findByactivate(String activate);
	List<Frequency> findByactivate(String activate, Sort sort);
	Page<Frequency> findByactivate(String activate, Pageable pageable);
	List<Frequency> findByactivateIgnoreCase(String activate);
	List<Frequency> findByactivateIgnoreCase(String activate, Sort sort);
	Page<Frequency> findByactivateIgnoreCase(String activate, Pageable pageable);
	List<Frequency> findByactivateLike(String activate);
	List<Frequency> findByactivateLike(String activate, Sort sort);
	Page<Frequency> findByactivateLike(String activate, Pageable pageable);
	List<Frequency> findByactivateLikeIgnoreCase(String activate);
	List<Frequency> findByactivateLikeIgnoreCase(String activate, Sort sort);
	Page<Frequency> findByactivateLikeIgnoreCase(String activate, Pageable pageable);
	//activate field with In Frequencyuid constraints
	List<Frequency> findByactivateAndFrequencyuidIn(String activate, List<String> uids);
	List<Frequency> findByactivateAndFrequencyuidIn(String activate, Sort sort, List<String> uids);
	Page<Frequency> findByactivateAndFrequencyuidIn(String activate, Pageable pageable, List<String> uids);
	List<Frequency> findByactivateIgnoreCaseAndFrequencyuidIn(String activate, List<String> uids);
	List<Frequency> findByactivateIgnoreCaseAndFrequencyuidIn(String activate, Sort sort, List<String> uids);
	Page<Frequency> findByactivateIgnoreCaseAndFrequencyuidIn(String activate, Pageable pageable, List<String> uids);
	List<Frequency> findByactivateLikeAndFrequencyuidIn(String activate, List<String> uids);
	List<Frequency> findByactivateLikeAndFrequencyuidIn(String activate, Sort sort, List<String> uids);
	Page<Frequency> findByactivateLikeAndFrequencyuidIn(String activate, Pageable pageable, List<String> uids);
	List<Frequency> findByactivateLikeIgnoreCaseAndFrequencyuidIn(String activate, List<String> uids);
	List<Frequency> findByactivateLikeIgnoreCaseAndFrequencyuidIn(String activate, Sort sort, List<String> uids);
	Page<Frequency> findByactivateLikeIgnoreCaseAndFrequencyuidIn(String activate, Pageable pageable, List<String> uids);
	//activate field with Not In Frequencyuid constraints
	List<Frequency> findByactivateAndFrequencyuidNotIn(String activate, List<String> uids);
	List<Frequency> findByactivateAndFrequencyuidNotIn(String activate, Sort sort, List<String> uids);
	Page<Frequency> findByactivateAndFrequencyuidNotIn(String activate, Pageable pageable, List<String> uids);
	List<Frequency> findByactivateIgnoreCaseAndFrequencyuidNotIn(String activate, List<String> uids);
	List<Frequency> findByactivateIgnoreCaseAndFrequencyuidNotIn(String activate, Sort sort, List<String> uids);
	Page<Frequency> findByactivateIgnoreCaseAndFrequencyuidNotIn(String activate, Pageable pageable, List<String> uids);
	List<Frequency> findByactivateLikeAndFrequencyuidNotIn(String activate, List<String> uids);
	List<Frequency> findByactivateLikeAndFrequencyuidNotIn(String activate, Sort sort, List<String> uids);
	Page<Frequency> findByactivateLikeAndFrequencyuidNotIn(String activate, Pageable pageable, List<String> uids);
	List<Frequency> findByactivateLikeIgnoreCaseAndFrequencyuidNotIn(String activate, List<String> uids);
	List<Frequency> findByactivateLikeIgnoreCaseAndFrequencyuidNotIn(String activate, Sort sort, List<String> uids);
	Page<Frequency> findByactivateLikeIgnoreCaseAndFrequencyuidNotIn(String activate, Pageable pageable, List<String> uids);
}

