package com.netpro.trinity.repository.dao.jdbc.permission;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.repository.entity.permission.jdbc.AccessRight;

@Repository  //宣告這是一個DAO類別
public class AccessRightJDBCDao {
	public static final String insert_sql = "INSERT INTO accessright "
							+ "(peopleuid, objectuid, flag1, flag2, flag3, flag4, flag5, flag6, flag7, flag8) "
							+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	@Autowired
    protected JdbcTemplate jtm;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<AccessRight> findByPeopleUid(String uid) throws DataAccessException{

        String sql = "SELECT a.peopleuid, a.objectuid, a.flag1, a.flag2, a.flag3, a.flag4, a.flag5, a.flag6, a.flag7, a.flag8 "
        		+ "FROM accessright a "
        		+ "WHERE a.peopleuid = ? "
        		+ "ORDER BY a.lastupdatetime";
        Object[] param = new Object[] {uid};

        List<AccessRight> lists = (List<AccessRight>) jtm.query(sql, param,
                new BeanPropertyRowMapper(AccessRight.class));

        return lists;
    }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<AccessRight> findByObjectUid(String uid) throws DataAccessException{

        String sql = "SELECT a.peopleuid, a.objectuid, a.flag1, a.flag2, a.flag3, a.flag4, a.flag5, a.flag6, a.flag7, a.flag8 "
        		+ "FROM accessright a "
        		+ "WHERE a.objectuid = ? "
        		+ "ORDER BY a.lastupdatetime";
        Object[] param = new Object[] {uid};

        List<AccessRight> lists = (List<AccessRight>) jtm.query(sql, param,
                new BeanPropertyRowMapper(AccessRight.class));

        return lists;
    }
	
	public Boolean existByAllPKs(AccessRight list) throws DataAccessException{
        String sql = "SELECT COUNT(list) > 0 "
        		+ "FROM accessright list "
        		+ "WHERE peopleuid=? AND objectuid=? AND 1=1";
        Object[] param = new Object[] {list.getPeopleuid(), list.getObjectuid()};
        
        Boolean ret = (Boolean) jtm.queryForObject(sql, Boolean.class, param);

        return ret;
    }
	
	public int save(AccessRight list) throws DataAccessException{
		Object[] params = new Object[] {list.getPeopleuid(), list.getObjectuid(), 
				list.getFlag1(), list.getFlag2(), list.getFlag3(), list.getFlag4(), list.getFlag5(), list.getFlag6(), list.getFlag7(), list.getFlag8()};
		
		return jtm.update(insert_sql, params);
	}
	
	public int[] saveBatch(List<AccessRight> lists) throws DataAccessException{
		int[] insertCounts = jtm.batchUpdate(insert_sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setString(1, lists.get(i).getPeopleuid());
				ps.setString(2, lists.get(i).getObjectuid());
				ps.setString(3, lists.get(i).getFlag1());
				ps.setString(4, lists.get(i).getFlag2());
				ps.setString(5, lists.get(i).getFlag3());
				ps.setString(6, lists.get(i).getFlag4());
				ps.setString(7, lists.get(i).getFlag5());
				ps.setString(8, lists.get(i).getFlag6());
				ps.setString(9, lists.get(i).getFlag7());
				ps.setString(10, lists.get(i).getFlag8());
			}
			
			@Override
			public int getBatchSize() {
				return lists.size();
			}
		});
		
        return insertCounts;
	}
	
	public int deleteByPeopleUid(String uid) throws DataAccessException{
		String sql = "DELETE FROM accessright WHERE peopleuid = ?";
		Object[] param = new Object[] {uid};
		return jtm.update(sql, param);
	}
	
	public int deleteByObjectUid(String uid) throws DataAccessException{
		String sql = "DELETE FROM accessright WHERE objectuid = ?";
		Object[] param = new Object[] {uid};
		return jtm.update(sql, param);
	}
}
