package com.netpro.trinity.repository.dao.jdbc.externalrule;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.repository.entity.externalrule.jdbc.DmExtRule;

@Repository  //宣告這是一個DAO類別
public class DmExtRuleJDBCDao {
	public static final String insert_sql = "INSERT INTO dmextrule "
							+ "(extjaruid, rulename, fullclasspath, active, description) "
							+ "VALUES (?, ?, ?, ?, ?)";
	
	@Autowired
    protected JdbcTemplate jtm;
	
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
	
	public Boolean existByAllPKs(DmExtRule rule) throws DataAccessException{

        String sql = "SELECT COUNT(list) > 0 "
        		+ "FROM dmextrule list "
        		+ "WHERE extjaruid=? AND rulename=? AND 1=1";
        Object[] param = new Object[] {rule.getExtjaruid(), rule.getRulename()};
        
        Boolean ret = (Boolean) jtm.queryForObject(sql, Boolean.class, param);

        return ret;
    }
	
	public int save(DmExtRule list) throws DataAccessException{
		Object[] params = new Object[] {list.getExtjaruid(), list.getRulename(), list.getFullclasspath(), 
				list.getActive(), list.getDescription()};
		
		return jtm.update(insert_sql, params);
	}
	
	public int[] saveBatch(String extJarUid, List<DmExtRule> lists) throws DataAccessException{
		int[] insertCounts = jtm.batchUpdate(insert_sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setString(1, extJarUid);
				ps.setString(2, lists.get(i).getRulename());
				ps.setString(3, lists.get(i).getFullclasspath());
				ps.setString(4, lists.get(i).getActive());
				ps.setString(5, lists.get(i).getDescription());
			}
			
			@Override
			public int getBatchSize() {
				return lists.size();
			}
		});
		
        return insertCounts;
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
