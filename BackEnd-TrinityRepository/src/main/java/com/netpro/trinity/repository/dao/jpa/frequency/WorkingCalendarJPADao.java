package com.netpro.trinity.repository.dao.jpa.frequency;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.repository.entity.frequency.jpa.WorkingCalendar;

@Repository  //宣告這是一個DAO類別
public interface WorkingCalendarJPADao extends JpaRepository<WorkingCalendar, String> {
	@Query("select count(wc)>0 from workingcalendar wc where wc.wcalendarname=:wcalendarname AND 1=1")
	Boolean existByName(@Param("wcalendarname") String wcalendarname);
	
	//working calendar name field
	List<WorkingCalendar> findBywcalendarname(String name);
	List<WorkingCalendar> findBywcalendarname(String name, Sort sort);
	Page<WorkingCalendar> findBywcalendarname(String name, Pageable pageable);
	List<WorkingCalendar> findBywcalendarnameIgnoreCase(String name);
	List<WorkingCalendar> findBywcalendarnameIgnoreCase(String name, Sort sort);
	Page<WorkingCalendar> findBywcalendarnameIgnoreCase(String name, Pageable pageable);
	List<WorkingCalendar> findBywcalendarnameLike(String name);
	List<WorkingCalendar> findBywcalendarnameLike(String name, Sort sort);
	Page<WorkingCalendar> findBywcalendarnameLike(String name, Pageable pageable);
	List<WorkingCalendar> findBywcalendarnameLikeIgnoreCase(String name);
	List<WorkingCalendar> findBywcalendarnameLikeIgnoreCase(String name, Sort sort);
	Page<WorkingCalendar> findBywcalendarnameLikeIgnoreCase(String name, Pageable pageable);
	
	//activate field
	List<WorkingCalendar> findByactivate(String activate);
	List<WorkingCalendar> findByactivate(String activate, Sort sort);
	Page<WorkingCalendar> findByactivate(String activate, Pageable pageable);
	List<WorkingCalendar> findByactivateIgnoreCase(String activate);
	List<WorkingCalendar> findByactivateIgnoreCase(String activate, Sort sort);
	Page<WorkingCalendar> findByactivateIgnoreCase(String activate, Pageable pageable);
	List<WorkingCalendar> findByactivateLike(String activate);
	List<WorkingCalendar> findByactivateLike(String activate, Sort sort);
	Page<WorkingCalendar> findByactivateLike(String activate, Pageable pageable);
	List<WorkingCalendar> findByactivateLikeIgnoreCase(String activate);
	List<WorkingCalendar> findByactivateLikeIgnoreCase(String activate, Sort sort);
	Page<WorkingCalendar> findByactivateLikeIgnoreCase(String activate, Pageable pageable);
	
	//description field
	List<WorkingCalendar> findBydescription(String description);
	List<WorkingCalendar> findBydescription(String description, Sort sort);
	Page<WorkingCalendar> findBydescription(String description, Pageable pageable);
	List<WorkingCalendar> findBydescriptionIgnoreCase(String description);
	List<WorkingCalendar> findBydescriptionIgnoreCase(String description, Sort sort);
	Page<WorkingCalendar> findBydescriptionIgnoreCase(String description, Pageable pageable);
	List<WorkingCalendar> findBydescriptionLike(String description);
	List<WorkingCalendar> findBydescriptionLike(String description, Sort sort);
	Page<WorkingCalendar> findBydescriptionLike(String description, Pageable pageable);
	List<WorkingCalendar> findBydescriptionLikeIgnoreCase(String description);
	List<WorkingCalendar> findBydescriptionLikeIgnoreCase(String description, Sort sort);
	Page<WorkingCalendar> findBydescriptionLikeIgnoreCase(String description, Pageable pageable);
}