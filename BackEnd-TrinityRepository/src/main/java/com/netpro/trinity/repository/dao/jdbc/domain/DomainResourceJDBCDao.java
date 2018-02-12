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

import com.netpro.trinity.repository.entity.domain.jdbc.DomainResource;

@Repository  //宣告這是一個DAO類別
public class DomainResourceJDBCDao {
	public static final String insert_sql = "INSERT INTO domainresource "
							+ "(domainuid, resourcename, resourcevalue) "
							+ "VALUES (?, ?, ?)";
	
	@Autowired
    protected JdbcTemplate jtm;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<DomainResource> findByDomainUid(String uid) throws DataAccessException{

        String sql = "SELECT dr.resourcename, dr.resourcevalue "
        		+ "FROM domainresource dr "
        		+ "WHERE dr.domainuid = ? "
        		+ "ORDER BY dr.resourcename";
        Object[] param = new Object[] {uid};

        List<DomainResource> lists = (List<DomainResource>) jtm.query(sql, param,
                new BeanPropertyRowMapper(DomainResource.class));

        return lists;
    }
	
	public Boolean existByAllPKs(DomainResource drList) throws DataAccessException{

        String sql = "SELECT COUNT(list) > 0 "
        		+ "FROM domainresource list "
        		+ "WHERE domainuid=? AND resourcename=? AND 1=1";
        Object[] param = new Object[] {drList.getDomainuid(), drList.getResourcename()};
        
        Boolean ret = (Boolean) jtm.queryForObject(sql, Boolean.class, param);

        return ret;
    }
	
	public int save(DomainResource list) throws DataAccessException{
		Object[] params = new Object[] {list.getDomainuid(), list.getResourcename(), list.getResourcevalue()};
		
		return jtm.update(insert_sql, params);
	}
	
	public int[] saveBatch(String domainUid, List<DomainResource> lists) throws DataAccessException{
		int[] insertCounts = jtm.batchUpdate(insert_sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setString(1, domainUid);
				ps.setString(2, lists.get(i).getResourcename().toUpperCase());
				ps.setString(3, lists.get(i).getResourcevalue());
			}
			
			@Override
			public int getBatchSize() {
				return lists.size();
			}
		});
		
        return insertCounts;
	}
	
	public int deleteByDomainUid(String uid) throws DataAccessException{
		String sql = "DELETE FROM domainresource WHERE domainuid = ?";
		Object[] param = new Object[] {uid};
		return jtm.update(sql, param);
	}
}
