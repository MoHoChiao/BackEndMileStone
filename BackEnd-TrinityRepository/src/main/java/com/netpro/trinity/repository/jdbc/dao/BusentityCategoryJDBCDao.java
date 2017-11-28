package com.netpro.trinity.repository.jdbc.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository  //宣告這是一個DAO類別
public class BusentityCategoryJDBCDao {
	public static final String insert_sql = "INSERT INTO busentitycategory "
							+ "(busentityuid, categoryuid) "
							+ "VALUES (?, ?)";
	
	@Autowired
    protected JdbcTemplate jtm;
	
	public List<String> findAll() throws DataAccessException{

		String sql = "SELECT bec.categoryuid "
        		+ "FROM busentitycategory bec "
        		+ "ORDER BY bec.lastupdatetime DESC";

        List<String> lists = (List<String>) jtm.queryForList(sql, String.class);
        return lists;
    }
	
	public List<String> findByEntityUid(String uid) throws DataAccessException{

        String sql = "SELECT bec.categoryuid "
        		+ "FROM busentitycategory bec "
				+ "WHERE bec.busentityuid = ? "
        		+ "ORDER BY bec.lastupdatetime DESC";
        Object[] param = new Object[] {uid};

        List<String> lists = (List<String>) jtm.queryForList(sql, param, String.class);
        return lists;
    }
	
	public List<String> findByCategoryUid(String uid) throws DataAccessException{

        String sql = "SELECT bec.busentityuid "
        		+ "FROM busentitycategory bec "
				+ "WHERE bec.categoryuid = ? ";
        Object[] param = new Object[] {uid};

        List<String> lists = (List<String>) jtm.queryForList(sql, param, String.class);
        return lists;
    }
	
	public Boolean exitByEntityUid(String uid) throws DataAccessException{

        String sql = "SELECT COUNT(*) "
        		+ "FROM busentitycategory "
        		+ "WHERE busentityuid=? AND 1=1";
        Object[] param = new Object[] {uid};
        
        Integer ret = (Integer) jtm.queryForObject(sql, Integer.class, param);

        return ret > 0 ? true : false;
    }
	
	public int deleteByCategoryUid(String uid) throws DataAccessException{
		String sql = "DELETE FROM busentitycategory WHERE categoryuid = ?";
		Object[] param = new Object[] {uid};
		return jtm.update(sql, param);
	}
}
