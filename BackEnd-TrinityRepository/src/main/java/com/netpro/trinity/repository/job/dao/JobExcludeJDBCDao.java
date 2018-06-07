package com.netpro.trinity.repository.job.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.repository.job.entity.JobExclude;

@Repository  //宣告這是一個DAO類別
public class JobExcludeJDBCDao {
	public static final String insert_sql = "INSERT INTO jobexclude "
							+ "(excludefrequencyuid, jobuid) "
							+ "VALUES (?, ?)";
	
	@Autowired
    protected JdbcTemplate jtm;
	
	public List<String> findAllJobUids() throws DataAccessException{

		String sql = "SELECT distinct(je.jobuid) "
        		+ "FROM jobexclude je";

        List<String> lists = (List<String>) jtm.queryForList(sql, String.class);
        return lists;
    }
	
	public List<String> findJobUidsByExcludeFrequencyUid(String uid) throws DataAccessException{

        String sql = "SELECT je.jobuid "
        		+ "FROM jobexclude je "
				+ "WHERE je.excludefrequencyuid = ? "
        		+ "ORDER BY je.lastupdatetime DESC";
        Object[] param = new Object[] {uid};

        List<String> lists = (List<String>) jtm.queryForList(sql, param, String.class);
        return lists;
    }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<JobExclude> findJobFullPathByExcludeFrequencyUid(String uid) throws DataAccessException{

        String sql = "SELECT j.jobuid, j.jobname, vbec.busentityname, vbec.categoryname FROM jobexclude je "
        		+ "LEFT JOIN job j ON je.jobuid=j.jobuid "
        		+ "LEFT JOIN View_BusEntityCategory vbec ON j.categoryuid=vbec.categoryuid "
				+ "WHERE je.excludefrequencyuid = ? "
        		+ "ORDER BY vbec.busentityname, vbec.categoryname, j.jobname";
        Object[] param = new Object[] {uid};

        List<JobExclude> lists = (List<JobExclude>) jtm.query(sql, param,
                new BeanPropertyRowMapper(JobExclude.class));
        return lists;
    }
	
	public int save(JobExclude je) throws DataAccessException{
		Object[] params = new Object[] {je.getExcludefrequencyuid(), je.getJobuid()};
		
		return jtm.update(insert_sql, params);
	}
	
	public int deleteByJobUid(String uid) throws DataAccessException{
		String sql = "DELETE FROM jobexclude WHERE jobuid = ?";
		Object[] param = new Object[] {uid};
		return jtm.update(sql, param);
	}
	
	public int deleteByExcludeFrequencyUid(String uid) throws DataAccessException{
		String sql = "DELETE FROM jobexclude WHERE excludefrequencyuid = ?";
		Object[] param = new Object[] {uid};
		return jtm.update(sql, param);
	}
	
	public int deleteByPKUids(String excludeFreqUid, String jobUid) throws DataAccessException{
		String sql = "DELETE FROM jobexclude WHERE excludefrequencyuid = ? AND jobuid = ?";
		Object[] param = new Object[] {excludeFreqUid, jobUid};
		return jtm.update(sql, param);
	}
	
	public Boolean existByExcludeFrequencyUid(String uid) throws DataAccessException{
        String sql = "SELECT COUNT(je) > 0 "
        		+ "FROM jobexclude je "
        		+ "WHERE je.excludefrequencyuid=? AND 1=1";
        Object[] param = new Object[] {uid};
        
        Boolean ret = (Boolean) jtm.queryForObject(sql, Boolean.class, param);

        return ret;
    }
}
