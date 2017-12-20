package com.netpro.trinity.repository.dao.jdbc.workingcalendar;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.repository.entity.workingcalendar.jdbc.WorkingCalendarList;

@Repository  //宣告這是一個DAO類別
public class WorkingCalendarListJDBCDao {
	public static final String insert_sql = "INSERT INTO workingcalendarlist "
							+ "(wcalendaruid, yearnum, monthnum, daynum) "
							+ "VALUES (?, ?, ?, ?)";
	
	@Autowired
    protected JdbcTemplate jtm;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<WorkingCalendarList> findByWCUid(String uid) throws DataAccessException{

        String sql = "SELECT wc.wcalendaruid, wc.yearnum, wc.monthnum, wc.daynum "
        		+ "FROM workingcalendarlist wc "
        		+ "WHERE wc.wcalendaruid = ? "
        		+ "ORDER BY wc.yearnum, wc.monthnum, wc.daynum";
        Object[] param = new Object[] {uid};

        List<WorkingCalendarList> lists = (List<WorkingCalendarList>) jtm.query(sql, param,
                new BeanPropertyRowMapper(WorkingCalendarList.class));

        return lists;
    }
	
	public Boolean exitByAllPKs(WorkingCalendarList wcList) throws DataAccessException{

        String sql = "SELECT COUNT(list) > 0 "
        		+ "FROM workingcalendarlist list "
        		+ "WHERE wcalendaruid=? AND yearnum=? AND monthnum=? AND daynum=? AND 1=1";
        Object[] param = new Object[] {wcList.getWcalendaruid(), wcList.getYearnum(), wcList.getMonthnum(), wcList.getDaynum()};
        
        Boolean ret = (Boolean) jtm.queryForObject(sql, Boolean.class, param);

        return ret;
    }
	
	public int save(WorkingCalendarList list) throws DataAccessException{
		Object[] params = new Object[] {list.getWcalendaruid(), list.getYearnum(), 
				list.getMonthnum(), list.getDaynum()};
		
		return jtm.update(insert_sql, params);
	}
	
	public int[] saveBatch(List<WorkingCalendarList> lists) throws DataAccessException{
		int[] insertCounts = jtm.batchUpdate(insert_sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setString(1, lists.get(i).getWcalendaruid());
				ps.setInt(2, lists.get(i).getYearnum());
				ps.setInt(3, lists.get(i).getMonthnum());
				ps.setInt(4, lists.get(i).getDaynum());
			}
			
			@Override
			public int getBatchSize() {
				return lists.size();
			}
		});
		
        return insertCounts;
	}
	
	public int deleteByWCUid(String uid) throws DataAccessException{
		String sql = "DELETE FROM workingcalendarlist WHERE wcalendaruid = ?";
		Object[] param = new Object[] {uid};
		return jtm.update(sql, param);
	}
}
