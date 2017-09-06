package com.netpro.trinity.repository.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.repository.entity.Disconfig_Entity;
import com.netpro.trinity.repository.entity.Disconfig_PKs;

@Repository  //宣告這是一個DAO類別
public interface DisconfigDao extends JpaRepository<Disconfig_Entity, Disconfig_PKs> {  //自動繼承JapRepository下的所有方法
//	List<Disconfig_Entity> findByAge(Integer age); //自定義一個根據User的屬性及方法名稱查詢
//	Disconfig_Entity findByIdAndName(Long id, String name);	//自定義一個根據User的屬性及方法名稱查詢
//	
	@Query("select d from Disconfig d where (d.module=:module1 Or d.module=:module2) And "
			+ "(d.configname=:configname1 Or d.configname=:configname2) order by d.value asc") //使用@Query查詢
	List<Disconfig_Entity> findUiapPosition(@Param("module1")String module1, 
			@Param("module2")String module2, @Param("configname1")String configname1, @Param("configname2")String configname2);
	
}