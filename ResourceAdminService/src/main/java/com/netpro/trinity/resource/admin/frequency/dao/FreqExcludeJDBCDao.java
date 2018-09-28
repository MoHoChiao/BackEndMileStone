package com.netpro.trinity.resource.admin.frequency.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.resource.admin.frequency.entity.FreqExclude;

@Repository  //宣告這是一個DAO類別
public class FreqExcludeJDBCDao {
	public static final String insert_sql = "INSERT INTO freqexclude "
							+ "(excludefrequencyuid, frequencyuid) "
							+ "VALUES (?, ?)";
	
	@Autowired
    protected JdbcTemplate jtm;
	
	public List<String> findAllFrequencyUids() throws DataAccessException{

		String sql = "SELECT distinct(fe.frequencyuid) "
        		+ "FROM freqexclude fe";

        List<String> lists = (List<String>) jtm.queryForList(sql, String.class);
        return lists;
    }
	
	public List<String> findFrequencyUidsByExcludeFrequencyUid(String uid) throws DataAccessException{

        String sql = "SELECT fe.frequencyuid "
        		+ "FROM freqexclude fe "
				+ "WHERE fe.excludefrequencyuid = ? "
        		+ "ORDER BY fe.lastupdatetime DESC";
        Object[] param = new Object[] {uid};

        List<String> lists = (List<String>) jtm.queryForList(sql, param, String.class);
        return lists;
    }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<FreqExclude> findFreqFullPathByExcludeFrequencyUid(String uid) throws DataAccessException{

        String sql = "SELECT f.frequencyuid, f.frequencyname, fc.freqcategoryname FROM freqexclude fe "
        		+ "LEFT JOIN frequency f ON fe.frequencyuid=f.frequencyuid "
        		+ "LEFT JOIN frequencyrelation fr ON f.frequencyuid=fr.frequencyuid "
        		+ "LEFT JOIN frequencycategory fc ON fr.freqcategoryuid=fc.freqcategoryuid "
				+ "WHERE fe.excludefrequencyuid = ? "
        		+ "ORDER BY fc.freqcategoryname, f.frequencyname";
        Object[] param = new Object[] {uid};

        List<FreqExclude> lists = (List<FreqExclude>) jtm.query(sql, param,
                new BeanPropertyRowMapper(FreqExclude.class));
        return lists;
    }
	
	public int save(FreqExclude fe) throws DataAccessException{
		Object[] params = new Object[] {fe.getExcludefrequencyuid(), fe.getFrequencyuid()};
		
		return jtm.update(insert_sql, params);
	}
	
	public int deleteByFrequencyUid(String uid) throws DataAccessException{
		String sql = "DELETE FROM freqexclude WHERE frequencyuid = ?";
		Object[] param = new Object[] {uid};
		return jtm.update(sql, param);
	}
	
	public int deleteByExcludeFrequencyUid(String uid) throws DataAccessException{
		String sql = "DELETE FROM freqexclude WHERE excludefrequencyuid = ?";
		Object[] param = new Object[] {uid};
		return jtm.update(sql, param);
	}
	
	public int deleteByPKUids(String excludeFreqUid, String freqUid) throws DataAccessException{
		String sql = "DELETE FROM freqexclude WHERE excludefrequencyuid = ? AND frequencyuid = ?";
		Object[] param = new Object[] {excludeFreqUid, freqUid};
		return jtm.update(sql, param);
	}
	
	public Boolean existByExcludeFrequencyUid(String uid) throws DataAccessException{
        String sql = "SELECT COUNT(fe) > 0 "
        		+ "FROM freqexclude fe "
        		+ "WHERE fe.excludefrequencyuid=? AND 1=1";
        Object[] param = new Object[] {uid};
        
        Boolean ret = (Boolean) jtm.queryForObject(sql, Boolean.class, param);

        return ret;
    }
}
