package com.netpro.trinity.repository.service.externalrule;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netpro.trinity.repository.dao.jdbc.externalrule.DmExtRuleJDBCDao;
import com.netpro.trinity.repository.entity.externalrule.jdbc.DmExtRule;

@Service
public class DmExtRuleService {
	private static final Logger LOGGER = LoggerFactory.getLogger(DmExtRuleService.class);
	
	@Autowired
	private DmExtRuleJDBCDao dao;
	
	@Autowired
	private DmExtJarService jarService;
	
	public List<DmExtRule> getByExtJarUid(String extJarUid) throws IllegalArgumentException, Exception{
		if(extJarUid == null || extJarUid.isEmpty())
			throw new IllegalArgumentException("External Jar UID can not be empty!");

		return this.dao.findByExtJarUid(extJarUid);
	}
	
	public DmExtRule add(DmExtRule rule) throws IllegalArgumentException, Exception{
		String extjaruid = rule.getExtjaruid();
		if(null == extjaruid || extjaruid.trim().isEmpty())
			throw new IllegalArgumentException("External Jar UID can not be empty!");
		
		if(!this.jarService.existByUid(extjaruid))
			throw new IllegalArgumentException("External Jar UID does not exist!(" + extjaruid + ")");
		
		String rulename = rule.getRulename();
		if(null == rulename || rulename.trim().isEmpty())
			throw new IllegalArgumentException("External Rule Name can not be empty!!");
		
		if(this.dao.existByAllPKs(rule))
			throw new IllegalArgumentException("Duplicate External Rule!");
		
		String activate = rule.getActive();
		if(null == activate || (!activate.equals("1") && !activate.equals("0")))
			throw new IllegalArgumentException("External Rule activate value can only be 1 or 0!");
		
		String fullclasspath = rule.getFullclasspath();
		if(null == fullclasspath || fullclasspath.trim().isEmpty())
			throw new IllegalArgumentException("Full class path can not be empty!");
		
		String description = rule.getDescription();
		if(null == description)
			rule.setDescription("");
		
		if(this.dao.save(rule) > 0)
			return rule;
		else
			throw new IllegalArgumentException("Add External Rule Fail!");
	}
	
	public List<DmExtRule> add(String extJarUid, List<DmExtRule> rules) throws IllegalArgumentException, Exception{
		List<DmExtRule> new_rules = new ArrayList<DmExtRule>();
		
		if(null == rules)
			return new_rules;
		
		for(DmExtRule rule: rules) {
			try {
				rule.setExtjaruid(extJarUid);
				this.add(rule);
				new_rules.add(rule);
			}catch(Exception e) {
				DmExtRuleService.LOGGER.warn("Warning; reason was:", e);
			}
		}
		return new_rules;
	}
	
	public int[] addBatch(String extJarUid, List<DmExtRule> rules) throws IllegalArgumentException, Exception{
		if(null == extJarUid || extJarUid.trim().isEmpty())
			throw new IllegalArgumentException("External Jar Uid can not be empty!");
		
		if(!this.jarService.existByUid(extJarUid))
			throw new IllegalArgumentException("External Jar Uid does not exist!(" + extJarUid + ")");
		
		return this.dao.saveBatch(extJarUid, rules);
	}
	
	public void deleteByExtJarUid(String extJarUid) throws IllegalArgumentException, Exception{
		if(null == extJarUid || extJarUid.trim().length() <= 0)
			throw new IllegalArgumentException("External Jar UID can not be empty!");
		
		this.dao.deleteByExtJarUid(extJarUid);
	}
	
	public void deleteByAllPKs(String extJarUid, String ruleName) throws IllegalArgumentException, Exception{
		if(null == extJarUid || extJarUid.trim().length() <= 0)
			throw new IllegalArgumentException("External Jar UID can not be empty!");
		
		if(null == ruleName || ruleName.trim().length() <= 0)
			throw new IllegalArgumentException("External Rule Name can not be empty!");
		
		this.dao.deleteByAllPKs(extJarUid, ruleName);
	}
}
