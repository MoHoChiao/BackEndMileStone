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

import com.netpro.trinity.repository.entity.frequency.jdbc.FrequencyList;

@Repository  //宣告這是一個DAO類別
public class FrequencyListJDBCDao {
	public static final String insert_sql = "INSERT INTO frequencylist "
							+ "(frequencyuid, seq, yearnum, monthnum, daynum, weekdaynum, hour, minute) "
							+ "VALUES (?, ?, ?, ?, ? ,? ,? ,?)";
	
	@Autowired
    protected JdbcTemplate jtm;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<FrequencyList> findByFrequencyUid(String uid) throws DataAccessException{

        String sql = "SELECT f.frequencyuid, f.seq, f.yearnum, f.monthnum, f.daynum, f.weekdaynum, f.hour, f.minute "
        		+ "FROM frequencylist f "
        		+ "WHERE f.frequencyuid = ? "
        		+ "ORDER BY f.seq";
        Object[] param = new Object[] {uid};

        List<FrequencyList> lists = (List<FrequencyList>) jtm.query(sql, param,
                new BeanPropertyRowMapper(FrequencyList.class));

        return lists;
    }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<FrequencyList> findDistinctDateByFrequencyUid(String uid) throws DataAccessException{

        String sql = "SELECT DISTINCT f.yearnum, f.monthnum, f.daynum, f.weekdaynum "
        		+ "FROM frequencylist f "
        		+ "WHERE f.frequencyuid = ?";
        Object[] param = new Object[] {uid};

        List<FrequencyList> lists = (List<FrequencyList>) jtm.query(sql, param,
                new BeanPropertyRowMapper(FrequencyList.class));

        return lists;
    }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<FrequencyList> findDistinctTimeByFrequencyUid(String uid) throws DataAccessException{

        String sql = "SELECT DISTINCT f.hour, f.minute "
        		+ "FROM frequencylist f "
        		+ "WHERE f.frequencyuid = ?";
        Object[] param = new Object[] {uid};

        List<FrequencyList> lists = (List<FrequencyList>) jtm.query(sql, param,
                new BeanPropertyRowMapper(FrequencyList.class));

        return lists;
    }
	
	public Boolean exitByAllPKs(FrequencyList fList) throws DataAccessException{

        String sql = "SELECT COUNT(list) > 0 "
        		+ "FROM frequencylist list "
        		+ "WHERE frequencyuid=? AND seq=? AND 1=1";
        Object[] param = new Object[] {fList.getFrequencyuid(), fList.getSeq()};
        
        Boolean ret = (Boolean) jtm.queryForObject(sql, Boolean.class, param);

        return ret;
    }
	
	public Boolean isNumFieldContainsNegativeOne(String freqUid, String fieldName) throws DataAccessException{
		String sql = "SELECT COUNT(list) > 0 "
        		+ "FROM frequencylist list "
        		+ "WHERE frequencyuid=? AND " + fieldName + "=? AND 1=1";
        Object[] param = new Object[] {freqUid, -1};
        
        Boolean ret = (Boolean) jtm.queryForObject(sql, Boolean.class, param);

        return ret;
	}
	
	public int save(FrequencyList list) throws DataAccessException{
		Object[] params = new Object[] {list.getFrequencyuid(), list.getSeq(), list.getYearnum(), 
				list.getMonthnum(), list.getDaynum(), list.getWeekdaynum(), list.getHour(), list.getMinute()};
		
		return jtm.update(insert_sql, params);
	}
	
	public int[] saveBatch(List<FrequencyList> lists) throws DataAccessException{
		int[] insertCounts = jtm.batchUpdate(insert_sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setString(1, lists.get(i).getFrequencyuid());
				ps.setInt(2, lists.get(i).getSeq());
				ps.setInt(3, lists.get(i).getYearnum());
				ps.setInt(4, lists.get(i).getMonthnum());
				ps.setInt(5, lists.get(i).getDaynum());
				ps.setInt(6, lists.get(i).getWeekdaynum());
				ps.setInt(7, lists.get(i).getHour());
				ps.setInt(8, lists.get(i).getMinute());
			}
			
			@Override
			public int getBatchSize() {
				return lists.size();
			}
		});
		
        return insertCounts;
	}
	
	public int deleteByFrequencyUid(String uid) throws DataAccessException{
		String sql = "DELETE FROM frequencylist WHERE frequencyuid = ?";
		Object[] param = new Object[] {uid};
		return jtm.update(sql, param);
	}
}
