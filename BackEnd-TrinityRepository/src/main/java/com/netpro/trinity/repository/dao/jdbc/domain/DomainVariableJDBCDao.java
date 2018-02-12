package com.netpro.trinity.repository.dao.jdbc.domain;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.repository.entity.domain.jdbc.DomainVariable;

@Repository  //宣告這是一個DAO類別
public class DomainVariableJDBCDao {
	public static final String insert_sql = "INSERT INTO domainvariable "
							+ "(domainuid, variablename, variablevalue) "
							+ "VALUES (?, ?, ?)";
	
	@Autowired
    protected JdbcTemplate jtm;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<DomainVariable> findByDomainUid(String uid) throws DataAccessException{

        String sql = "SELECT dv.variablename, dv.variablevalue "
        		+ "FROM domainvariable dv "
        		+ "WHERE dv.domainuid = ? "
        		+ "ORDER BY dv.variablename";
        Object[] param = new Object[] {uid};

        List<DomainVariable> lists = (List<DomainVariable>) jtm.query(sql, param,
                new BeanPropertyRowMapper(DomainVariable.class));

        return lists;
    }
	
	public Boolean existByAllPKs(DomainVariable dvList) throws DataAccessException{

        String sql = "SELECT COUNT(list) > 0 "
        		+ "FROM domainvariable list "
        		+ "WHERE domainuid=? AND variablename=? AND 1=1";
        Object[] param = new Object[] {dvList.getDomainuid(), dvList.getVariablename()};
        
        Boolean ret = (Boolean) jtm.queryForObject(sql, Boolean.class, param);

        return ret;
    }
	
	public int save(DomainVariable list) throws DataAccessException{
		Object[] params = new Object[] {list.getDomainuid(), list.getVariablename(), list.getVariablevalue()};
		
		return jtm.update(insert_sql, params);
	}
	
	public int[] saveBatch(String domainUid, List<DomainVariable> lists) throws DataAccessException{
		int[] insertCounts = jtm.batchUpdate(insert_sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setString(1, domainUid);
				ps.setString(2, lists.get(i).getVariablename().toUpperCase());
				ps.setString(3, lists.get(i).getVariablevalue());
			}
			
			@Override
			public int getBatchSize() {
				return lists.size();
			}
		});
		
        return insertCounts;
	}
	
	public int deleteByDomainUid(String uid) throws DataAccessException{
		String sql = "DELETE FROM domainvariable WHERE domainuid = ?";
		Object[] param = new Object[] {uid};
		return jtm.update(sql, param);
	}
}
