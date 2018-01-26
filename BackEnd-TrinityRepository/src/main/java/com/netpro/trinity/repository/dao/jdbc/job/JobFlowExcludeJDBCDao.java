package com.netpro.trinity.repository.dao.jdbc.job;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.repository.entity.job.jdbc.JobFlowExclude;

@Repository  //宣告這是一個DAO類別
public class JobFlowExcludeJDBCDao {
	public static final String insert_sql = "INSERT INTO jobflowexclude "
							+ "(excludefrequencyuid, jobflowuid) "
							+ "VALUES (?, ?)";
	
	@Autowired
    protected JdbcTemplate jtm;
	
	public List<String> findAllFlowUids() throws DataAccessException{

		String sql = "SELECT distinct(jfe.jobflowuid) "
        		+ "FROM jobflowexclude jfe";

        List<String> lists = (List<String>) jtm.queryForList(sql, String.class);
        return lists;
    }
	
	public List<String> findFlowUidsByExcludeFrequencyUid(String uid) throws DataAccessException{

        String sql = "SELECT jfe.jobflowuid "
        		+ "FROM jobflowexclude jfe "
				+ "WHERE jfe.excludefrequencyuid = ? "
        		+ "ORDER BY jfe.lastupdatetime DESC";
        Object[] param = new Object[] {uid};

        List<String> lists = (List<String>) jtm.queryForList(sql, param, String.class);
        return lists;
    }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<JobFlowExclude> findFlowFullPathByExcludeFrequencyUid(String uid) throws DataAccessException{

        String sql = "SELECT jf.jobflowuid, jf.flowname, vbec.busentityname, vbec.categoryname FROM jobflowexclude jfe "
        		+ "LEFT JOIN jobflow jf ON jfe.jobflowuid=jf.jobflowuid "
        		+ "LEFT JOIN View_BusEntityCategory vbec ON jf.categoryuid=vbec.categoryuid "
				+ "WHERE jfe.excludefrequencyuid = ? "
        		+ "ORDER BY vbec.busentityname, vbec.categoryname, jf.flowname";
        Object[] param = new Object[] {uid};

        List<JobFlowExclude> lists = (List<JobFlowExclude>) jtm.query(sql, param,
                new BeanPropertyRowMapper(JobFlowExclude.class));
        return lists;
    }
	
	public int save(JobFlowExclude jfe) throws DataAccessException{
		Object[] params = new Object[] {jfe.getExcludefrequencyuid(), jfe.getJobflowuid()};
		
		return jtm.update(insert_sql, params);
	}
	
	public int deleteByFlowUid(String uid) throws DataAccessException{
		String sql = "DELETE FROM jobflowexclude WHERE jobflowuid = ?";
		Object[] param = new Object[] {uid};
		return jtm.update(sql, param);
	}
	
	public int deleteByExcludeFrequencyUid(String uid) throws DataAccessException{
		String sql = "DELETE FROM jobflowexclude WHERE excludefrequencyuid = ?";
		Object[] param = new Object[] {uid};
		return jtm.update(sql, param);
	}
	
	public Boolean exitByExcludeFrequencyUid(String uid) throws DataAccessException{
        String sql = "SELECT COUNT(jfe) > 0 "
        		+ "FROM jobflowexclude jfe "
        		+ "WHERE jfe.excludefrequencyuid=? AND 1=1";
        Object[] param = new Object[] {uid};
        
        Boolean ret = (Boolean) jtm.queryForObject(sql, Boolean.class, param);

        return ret;
    }
}
