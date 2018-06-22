package com.netpro.trinity.service.frequency.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.service.frequency.entity.FrequencyRelation;

@Repository  //宣告這是一個DAO類別
public class FrequencyRelationJDBCDao {
	public static final String insert_sql = "INSERT INTO frequencyrelation "
							+ "(freqcategoryuid, frequencyuid) "
							+ "VALUES (?, ?)";
	
	@Autowired
    protected JdbcTemplate jtm;
	
	public List<String> findAllFrequencyUids() throws DataAccessException{

		String sql = "SELECT distinct(fr.frequencyuid) "
        		+ "FROM frequencyrelation fr";

        List<String> lists = (List<String>) jtm.queryForList(sql, String.class);
        return lists;
    }
	
	public List<String> findFrequencyUidsByCategoryUid(String uid) throws DataAccessException{

        String sql = "SELECT fr.frequencyuid "
        		+ "FROM frequencyrelation fr "
				+ "WHERE fr.freqcategoryuid = ? "
        		+ "ORDER BY fr.lastupdatetime DESC";
        Object[] param = new Object[] {uid};

        List<String> lists = (List<String>) jtm.queryForList(sql, param, String.class);
        return lists;
    }
	
	public int save(FrequencyRelation rel) throws DataAccessException{
		Object[] params = new Object[] {rel.getFreqcategoryuid(), rel.getFrequencyuid()};
		
		return jtm.update(insert_sql, params);
	}
	
	public int deleteByFrequencyUid(String uid) throws DataAccessException{
		String sql = "DELETE FROM frequencyrelation WHERE frequencyuid = ?";
		Object[] param = new Object[] {uid};
		return jtm.update(sql, param);
	}
	
	public Boolean existByCategoryUid(String uid) throws DataAccessException{
        String sql = "SELECT COUNT(rel) > 0 "
        		+ "FROM frequencyrelation rel "
        		+ "WHERE freqcategoryuid=? AND 1=1";
        Object[] param = new Object[] {uid};
        
        Boolean ret = (Boolean) jtm.queryForObject(sql, Boolean.class, param);

        return ret;
    }
}
