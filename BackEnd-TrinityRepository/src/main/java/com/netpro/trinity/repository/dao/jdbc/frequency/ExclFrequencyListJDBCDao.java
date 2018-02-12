package com.netpro.trinity.repository.dao.jdbc.frequency;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.repository.entity.frequency.jdbc.ExclFrequencyList;

@Repository  //宣告這是一個DAO類別
public class ExclFrequencyListJDBCDao {
	public static final String insert_sql = "INSERT INTO excludefrequencylist "
							+ "(excludefrequencyuid, seq, starttime, endtime) "
							+ "VALUES (?, ?, ?, ?)";
	
	@Autowired
    protected JdbcTemplate jtm;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<ExclFrequencyList> findByExclFreqUid(String uid) throws DataAccessException{

        String sql = "SELECT excl.excludefrequencyuid, excl.seq, excl.starttime, excl.endtime "
        		+ "FROM excludefrequencylist excl "
        		+ "WHERE excl.excludefrequencyuid = ? "
        		+ "ORDER BY excl.seq";
        Object[] param = new Object[] {uid};

        List<ExclFrequencyList> lists = (List<ExclFrequencyList>) jtm.query(sql, param,
                new BeanPropertyRowMapper(ExclFrequencyList.class));

        return lists;
    }
	
	public Integer findMaxSeqByExclFreqUid(String uid) throws DataAccessException{

        String sql = "SELECT Max(seq) "
        		+ "FROM excludefrequencylist "
        		+ "WHERE excludefrequencyuid=?";
        Object[] param = new Object[] {uid};
        
        Integer ret = (Integer) jtm.queryForObject(sql, Integer.class, param);

        return ret;
    }
	
	public Boolean existByAllPKs(ExclFrequencyList exclFreqList) throws DataAccessException{

        String sql = "SELECT COUNT(list) > 0 "
        		+ "FROM excludefrequencylist list "
        		+ "WHERE excludefrequencyuid=? AND seq=? AND 1=1";
        Object[] param = new Object[] {exclFreqList.getExcludefrequencyuid(), exclFreqList.getSeq()};
        
        Boolean ret = (Boolean) jtm.queryForObject(sql, Boolean.class, param);

        return ret;
    }
	
	public Boolean existByTime(ExclFrequencyList exclFreqList) throws DataAccessException{

        String sql = "SELECT COUNT(list) > 0 "
        		+ "FROM excludefrequencylist list "
        		+ "WHERE excludefrequencyuid=? AND starttime=? AND endtime=? AND 1=1";
        Object[] param = new Object[] {exclFreqList.getExcludefrequencyuid(), exclFreqList.getStarttime(), exclFreqList.getEndtime()};
        
        Boolean ret = (Boolean) jtm.queryForObject(sql, Boolean.class, param);

        return ret;
    }
	
	public int save(ExclFrequencyList list) throws DataAccessException{
		Object[] params = new Object[] {list.getExcludefrequencyuid(), list.getSeq(), 
				list.getStarttime(), list.getEndtime()};
		
		return jtm.update(insert_sql, params);
	}
	
	public int[] saveBatch(String excludefrequencyuid, List<ExclFrequencyList> lists) throws DataAccessException{
		int[] insertCounts = jtm.batchUpdate(insert_sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setString(1, excludefrequencyuid);
				ps.setInt(2, lists.get(i).getSeq());
				ps.setLong(3, lists.get(i).getStarttime());
				ps.setLong(4, lists.get(i).getEndtime());
			}
			
			@Override
			public int getBatchSize() {
				return lists.size();
			}
		});
		
        return insertCounts;
	}
	
	public int deleteByExclFreqUid(String uid) throws DataAccessException{
		String sql = "DELETE FROM excludefrequencylist WHERE excludefrequencyuid = ?";
		Object[] param = new Object[] {uid};
		return jtm.update(sql, param);
	}
}
