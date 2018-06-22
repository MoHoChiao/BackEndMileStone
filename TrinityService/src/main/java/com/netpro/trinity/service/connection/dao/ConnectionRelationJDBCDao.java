package com.netpro.trinity.service.connection.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.service.connection.entity.ConnectionRelation;

@Repository  //宣告這是一個DAO類別
public class ConnectionRelationJDBCDao {
	public static final String insert_sql = "INSERT INTO connectionrelation "
							+ "(conncategoryuid, connectionuid) "
							+ "VALUES (?, ?)";
	
	@Autowired
    protected JdbcTemplate jtm;
	
	public List<String> findAllConnectionUids() throws DataAccessException{

		String sql = "SELECT distinct(cr.connectionuid) "
        		+ "FROM connectionrelation cr";

        List<String> lists = (List<String>) jtm.queryForList(sql, String.class);
        return lists;
    }
	
	public List<String> findConnectionUidsByCategoryUid(String uid) throws DataAccessException{

        String sql = "SELECT cr.connectionuid "
        		+ "FROM connectionrelation cr "
				+ "WHERE cr.conncategoryuid = ? "
        		+ "ORDER BY cr.lastupdatetime DESC";
        Object[] param = new Object[] {uid};

        List<String> lists = (List<String>) jtm.queryForList(sql, param, String.class);
        return lists;
    }
	
	public int save(ConnectionRelation rel) throws DataAccessException{
		Object[] params = new Object[] {rel.getConncategoryuid(), rel.getConnectionuid()};
		
		return jtm.update(insert_sql, params);
	}
	
	public int deleteByConnectionUid(String uid) throws DataAccessException{
		String sql = "DELETE FROM connectionrelation WHERE connectionuid = ?";
		Object[] param = new Object[] {uid};
		return jtm.update(sql, param);
	}
	
	public Boolean existByCategoryUid(String uid) throws DataAccessException{
        String sql = "SELECT COUNT(rel) > 0 "
        		+ "FROM connectionrelation rel "
        		+ "WHERE conncategoryuid=? AND 1=1";
        Object[] param = new Object[] {uid};
        
        Boolean ret = (Boolean) jtm.queryForObject(sql, Boolean.class, param);

        return ret;
    }
}
