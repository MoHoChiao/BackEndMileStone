package com.netpro.trinity.repository.dao.jpa.transformrule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.netpro.trinity.repository.entity.transformrule.jpa.Transformrule;

@Repository  //宣告這是一個DAO類別
public interface TransformruleJPADao extends JpaRepository<Transformrule, String> {
	@Query("select count(rule)>0 from transformrule rule where rule.rule=:rule AND 1=1")
	Boolean existByRule(@Param("rule") String rule);
	
	@Transactional
	@Modifying
	@Query("UPDATE transformrule trans SET trans.rule=:newRule WHERE trans.rule=:targetRule")
	public Integer updateRuleOnly(@Param("newRule") String newRule, @Param("targetRule") String targetRule);
}