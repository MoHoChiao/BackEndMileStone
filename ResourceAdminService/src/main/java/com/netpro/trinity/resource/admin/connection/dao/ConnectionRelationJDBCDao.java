package com.netpro.trinity.resource.admin.connection.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.resource.admin.connection.entity.ConnectionCategory;
import com.netpro.trinity.resource.admin.connection.entity.ConnectionRelation;

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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ConnectionCategory findCategoryByConnectionUid(String uid) throws DataAccessException{

		String sql = "SELECT cc.conncategoryname, cc.conncategoryuid "
        		+ "FROM connectionrelation cr left join connectioncategory cc "
				+ "on cr.conncategoryuid = cc.conncategoryuid "
				+ "WHERE cr.connectionuid = ? ";
        Object[] param = new Object[] {uid};
        
        ConnectionCategory category = null;
        try {
        	category = (ConnectionCategory) jtm.queryForObject(sql, param, new BeanPropertyRowMapper(ConnectionCategory.class));
        } catch (EmptyResultDataAccessException e) {}
        
        return category;
    }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, String> findConnectionUidAndCategoryNameMap() throws DataAccessException{
        String sql = "SELECT cr.connectionuid, cc.conncategoryname "
        		+ "FROM connectionrelation cr left join connectioncategory cc "
				+ "on cr.conncategoryuid = cc.conncategoryuid";
        
        List<ConnectionRelation> lists = (List<ConnectionRelation>) jtm.query(sql, new BeanPropertyRowMapper(ConnectionRelation.class));
        
        Map<String, String> map = new HashMap<String, String>();
        for(ConnectionRelation list : lists) {
        	map.put(list.getConnectionuid(), list.getConncategoryname());
        }
        
        return map;
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
