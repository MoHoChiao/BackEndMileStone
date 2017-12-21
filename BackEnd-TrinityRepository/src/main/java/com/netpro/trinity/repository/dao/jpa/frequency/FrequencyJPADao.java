package com.netpro.trinity.repository.dao.jpa.frequency;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.repository.entity.frequency.jpa.Frequency;

@Repository  //宣告這是一個DAO類別
public interface FrequencyJPADao extends JpaRepository<Frequency, String> {
	@Query("select count(freq)>0 from Frequency freq where freq.frequencyname=:frequencyname AND 1=1")
	Boolean existByName(@Param("frequencyname") String frequencyname);
	
	@Query("select count(freq)>0 from Frequency freq where freq.wcalendaruid=:wcalendaruid AND 1=1")
	Boolean existByWCalendaruid(@Param("wcalendaruid") String wcalendaruid);
}

