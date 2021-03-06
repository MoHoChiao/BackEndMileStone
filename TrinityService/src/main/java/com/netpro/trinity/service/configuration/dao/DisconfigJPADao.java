package com.netpro.trinity.service.configuration.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.service.configuration.entity.Disconfig;
import com.netpro.trinity.service.configuration.entity.DisconfigPKs;

@Repository  //宣告這是一個DAO類別
public interface DisconfigJPADao extends JpaRepository<Disconfig, DisconfigPKs> {  //自動繼承JapRepository下的所有方法
//	List<Disconfig_Entity> findByAge(Integer age); //自定義一個根據User的屬性及方法名稱查詢
//	Disconfig_Entity findByIdAndName(Long id, String name);	//自定義一個根據User的屬性及方法名稱查詢
//	
	@Query("select d from Disconfig d where (d.module=:module1 Or d.module=:module2) And "
			+ "(d.configname=:configname1 Or d.configname=:configname2) order by d.value asc") //使用@Query查詢
	List<Disconfig> findUiapPosition(@Param("module1")String module1, 
			@Param("module2")String module2, @Param("configname1")String configname1, @Param("configname2")String configname2);
	
	
	List<Disconfig> findByModuleOrderByConfigname(String module);
}