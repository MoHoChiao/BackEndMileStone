package com.netpro.trinity.resource.admin.externalrule.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.resource.admin.externalrule.dto.PublishRule;
import com.netpro.trinity.resource.admin.externalrule.entity.DmExtRule;

@Repository  //宣告這是一個DAO類別
public class DmExtRuleJDBCDao {
	public static final String insert_sql = "INSERT INTO dmextrule "
							+ "(extjaruid, rulename, fullclasspath, active, description) "
							+ "VALUES (?, ?, ?, ?, ?)";
	public static final String update_sql = "UPDATE dmextrule SET rulename = ?, active = ?, description = ? " 
							+ "WHERE extjaruid = ? AND rulename = ?";
			
	
	@Autowired
    protected JdbcTemplate jtm;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<DmExtRule> findAll() throws DataAccessException{

        String sql = "SELECT rule.extjaruid, rule.rulename, rule.fullclasspath, rule.active, rule.description "
        		+ "FROM dmextrule rule "
        		+ "ORDER BY rule.rulename";

        List<DmExtRule> lists = (List<DmExtRule>) jtm.query(sql,
                new BeanPropertyRowMapper(DmExtRule.class));

        return lists;
    }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<DmExtRule> findByExtJarUid(String extJarUid) throws DataAccessException{

        String sql = "SELECT rule.extjaruid, rule.rulename, rule.fullclasspath, rule.active, rule.description "
        		+ "FROM dmextrule rule "
        		+ "WHERE rule.extjaruid = ? "
        		+ "ORDER BY rule.rulename";
        Object[] param = new Object[] {extJarUid};

        List<DmExtRule> lists = (List<DmExtRule>) jtm.query(sql, param,
                new BeanPropertyRowMapper(DmExtRule.class));

        return lists;
    }
	
	public DmExtRule findByAllPKs(String extJarUid, String ruleName) throws DataAccessException{
		String sql = "SELECT rule.extjaruid, rule.rulename, rule.fullclasspath, rule.active, rule.description "
        		+ "FROM dmextrule rule "
        		+ "WHERE rule.extjaruid = ? AND rule.rulename = ? "
        		+ "ORDER BY rule.rulename";
        Object[] param = new Object[] {extJarUid, ruleName};
        
        DmExtRule rule = null;
        try {
        	rule = (DmExtRule)jtm.queryForObject(
        			sql, param, new BeanPropertyRowMapper<DmExtRule>(DmExtRule.class));
        } catch (EmptyResultDataAccessException e) {}
        
        return rule;
    }
	
	public List<String> findFullClassPathsByExtJarUid(String extJarUid) throws DataAccessException{

        String sql = "SELECT rule.fullclasspath "
        		+ "FROM dmextrule rule "
        		+ "WHERE rule.extjaruid = ? ";
        Object[] param = new Object[] {extJarUid};

        List<String> lists = (List<String>) jtm.queryForList(sql, param, String.class);
        return lists;
    }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<PublishRule> findAllExt() throws DataAccessException{

        String sql = "SELECT der.rulename, der.fullclasspath, der.active, der.description, dej.extjaruid, dej.filename, dej.md5, dep.packageuid, dep.packagename "
        		+ "FROM Dmextrule der, Dmextjar dej, Dmextpackage dep "
        		+ "WHERE der.extjaruid = dej.extjaruid AND dej.packageuid = dep.packageuid "
        		+ "ORDER BY dep.packagename, dej.filename, der.rulename";

        List<PublishRule> lists = (List<PublishRule>) jtm.query(sql,
                new BeanPropertyRowMapper(PublishRule.class));

        return lists;
    }
	
	public Boolean existByAllPKs(DmExtRule rule) throws DataAccessException{
        String sql = "SELECT COUNT(list) > 0 "
        		+ "FROM dmextrule list "
        		+ "WHERE extjaruid=? AND rulename=? AND 1=1";
        Object[] param = new Object[] {rule.getExtjaruid(), rule.getRulename()};
        
        Boolean ret = (Boolean) jtm.queryForObject(sql, Boolean.class, param);
        
        return ret;
    }
	
	public int save(DmExtRule rule) throws DataAccessException{
		Object[] params = new Object[] {rule.getExtjaruid(), rule.getRulename(), rule.getFullclasspath(), 
				rule.getActive(), rule.getDescription()};
		
		return jtm.update(insert_sql, params);
	}
	
	public int update(String oldRuleName, DmExtRule rule) throws DataAccessException{
		Object[] params = new Object[] {rule.getRulename(), rule.getActive(), rule.getDescription(), 
				rule.getExtjaruid(), oldRuleName};
		
		return jtm.update(update_sql, params);
	}
	
	public int deleteByExtJarUid(String extJarUid) throws DataAccessException{
		String sql = "DELETE FROM dmextrule WHERE extjaruid = ?";
		Object[] param = new Object[] {extJarUid};
		return jtm.update(sql, param);
	}
	
	public int deleteByAllPKs(String extJarUid, String ruleName) throws DataAccessException{
		String sql = "DELETE FROM dmextrule WHERE extjaruid = ? AND rulename = ?";
		Object[] param = new Object[] {extJarUid, ruleName};
		return jtm.update(sql, param);
	}
}
