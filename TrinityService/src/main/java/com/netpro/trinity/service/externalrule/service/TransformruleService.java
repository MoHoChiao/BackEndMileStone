package com.netpro.trinity.service.externalrule.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.netpro.trinity.service.externalrule.dao.TransformruleJPADao;
import com.netpro.trinity.service.externalrule.entity.Transformrule;

@Service
public class TransformruleService {
	
	@Autowired
	private TransformruleJPADao dao;
	
	public List<Transformrule> getAllTransformrule() throws Exception{
		return this.dao.findAll();
	}
	
	public Transformrule getByRule(String rule) throws IllegalArgumentException, Exception{
		if(null == rule || rule.isEmpty())
			throw new IllegalArgumentException("Transformrule UID can not be empty!");
		
		Transformrule transformrule = null;
		try {
			transformrule = this.dao.findById(rule).get();
		}catch(NoSuchElementException e) {}
		
		if(null == transformrule)
			throw new IllegalArgumentException("Rule Uid does not exist!(" + rule + ")");
		
		return transformrule;
	}
	
	public Transformrule modify(Transformrule rule) throws IllegalArgumentException, Exception{
		String ruleuid = rule.getRule();
		if(null == ruleuid || ruleuid.trim().isEmpty())
			throw new IllegalArgumentException("Transformrule UID can not be empty!");
		
		String needargument = rule.getNeedargument();
		if(null == needargument || (!needargument.equals("1") && !needargument.equals("0")))
			throw new IllegalArgumentException("Need Argument Field can only be 1 or 0!");
		
		if(null == rule.getArgtemplate())
			rule.setArgtemplate("");
		
		if(null == rule.getRuledescription())
			rule.setRuledescription("");
		
		return this.dao.save(rule);
	}
	
	public Integer editRuleOnly(String newRule, String targetRule) throws IllegalArgumentException, Exception{
		if(null == newRule || newRule.trim().isEmpty())
			throw new IllegalArgumentException("New Rule can not be empty!");
		
		if(null == targetRule || targetRule.trim().isEmpty())
			throw new IllegalArgumentException("Target Rule can not be empty!");
		
		return this.dao.updateRuleOnly(newRule, targetRule);
	}
	
	public void deleteByRule(String rule) throws IllegalArgumentException, Exception{
		if(null == rule || rule.isEmpty())
			throw new IllegalArgumentException("Transformrule UID can not be empty!");
		
		try {
			this.dao.deleteById(rule);
		}catch(EmptyResultDataAccessException e) {}
	}
	
	public Boolean existByRule(String rule) throws IllegalArgumentException, Exception{
		if(null == rule || rule.isEmpty())
			throw new IllegalArgumentException("Transformrule UID can not be empty!");
				
		return this.dao.existByRule(rule);
	}
}
