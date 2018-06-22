package com.netpro.trinity.service.job.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.service.job.entity.JobFullPath;

@Repository  //宣告這是一個DAO類別
public class BusentityCategoryJDBCDao {
	public static final String insert_sql = "INSERT INTO busentitycategory "
							+ "(busentityuid, categoryuid) "
							+ "VALUES (?, ?)";
	
	@Autowired
    protected JdbcTemplate jtm;
	
	public List<String> findAllCategoryUids() throws DataAccessException{

		String sql = "SELECT bec.categoryuid "
        		+ "FROM busentitycategory bec "
        		+ "ORDER BY bec.lastupdatetime DESC";

        List<String> lists = (List<String>) jtm.queryForList(sql, String.class);
        return lists;
    }
	
	public List<String> findCategoryUidsByEntityUid(String uid) throws DataAccessException{

        String sql = "SELECT bec.categoryuid "
        		+ "FROM busentitycategory bec "
				+ "WHERE bec.busentityuid = ? "
        		+ "ORDER BY bec.lastupdatetime DESC";
        Object[] param = new Object[] {uid};

        List<String> lists = (List<String>) jtm.queryForList(sql, param, String.class);
        return lists;
    }
	
	public List<String> findEntityUidsByCategoryUid(String uid) throws DataAccessException{

        String sql = "SELECT bec.busentityuid "
        		+ "FROM busentitycategory bec "
				+ "WHERE bec.categoryuid = ? ";
        Object[] param = new Object[] {uid};

        List<String> lists = (List<String>) jtm.queryForList(sql, param, String.class);
        return lists;
    }
	
	public List<JobFullPath> findViewEntityCategoryByCategoryUid(String uid) throws DataAccessException{

        String sql = "SELECT v_bec.busentityuid, v_bec.busentityname, v_bec.categoryuid, v_bec.categoryname "
        		+ "FROM View_BusEntityCategory v_bec "
				+ "WHERE v_bec.categoryuid = ? ";
        Object[] param = new Object[] {uid};

        List<JobFullPath> lists = (List<JobFullPath>) jtm.query(sql, param,
                new BeanPropertyRowMapper<JobFullPath>(JobFullPath.class));
        return lists;
    }
	
	public Boolean existByEntityUid(String uid) throws DataAccessException{

        String sql = "SELECT COUNT(bec) > 0 "
        		+ "FROM busentitycategory bec "
        		+ "WHERE busentityuid=? AND 1=1";
        Object[] param = new Object[] {uid};
        
        Boolean ret = (Boolean) jtm.queryForObject(sql, Boolean.class, param);

        return ret;
    }
	
	public int deleteByCategoryUid(String uid) throws DataAccessException{
		String sql = "DELETE FROM busentitycategory WHERE categoryuid = ?";
		Object[] param = new Object[] {uid};
		return jtm.update(sql, param);
	}
}
