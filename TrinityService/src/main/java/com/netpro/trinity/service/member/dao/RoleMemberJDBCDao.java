package com.netpro.trinity.service.member.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.service.member.entity.RoleMember;

@Repository  //宣告這是一個DAO類別
public class RoleMemberJDBCDao {
	public static final String insert_sql = "INSERT INTO rolemember "
							+ "(roleuid, useruid) "
							+ "VALUES (?, ?)";
	
	@Autowired
    protected JdbcTemplate jtm;
	
	public List<String> findAllUserUids() throws DataAccessException{

		String sql = "SELECT distinct(rm.useruid) "
        		+ "FROM rolemember rm";

        List<String> lists = (List<String>) jtm.queryForList(sql, String.class);
        return lists;
    }
	
	public List<String> findUserUidsByRoleUid(String uid) throws DataAccessException{

        String sql = "SELECT rm.useruid "
        		+ "FROM rolemember rm "
				+ "WHERE rm.roleuid = ? "
        		+ "ORDER BY rm.lastupdatetime DESC";
        Object[] param = new Object[] {uid};

        List<String> lists = (List<String>) jtm.queryForList(sql, param, String.class);
        return lists;
    }
	
	public List<String> findRoleUidsByUserUid(String uid) throws DataAccessException{

        String sql = "SELECT rm.roleuid "
        		+ "FROM rolemember rm "
				+ "WHERE rm.useruid = ? "
        		+ "ORDER BY rm.lastupdatetime DESC";
        Object[] param = new Object[] {uid};

        List<String> lists = (List<String>) jtm.queryForList(sql, param, String.class);
        return lists;
    }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<RoleMember> findUserFullNameByRoleUid(String uid) throws DataAccessException{

        String sql = "SELECT u.useruid, u.username, u.userid FROM rolemember rm "
        		+ "LEFT JOIN trinityuser u ON rm.useruid=u.useruid "
				+ "WHERE rm.roleuid = ? "
        		+ "ORDER BY u.username, u.userid";
        Object[] param = new Object[] {uid};

        List<RoleMember> lists = (List<RoleMember>) jtm.query(sql, param,
                new BeanPropertyRowMapper(RoleMember.class));
        return lists;
    }
	
	public int save(RoleMember rm) throws DataAccessException{
		Object[] params = new Object[] {rm.getRoleuid(), rm.getUseruid()};
		
		return jtm.update(insert_sql, params);
	}
	
	public int[] saveBatch(String roleUid, List<RoleMember> lists) throws DataAccessException{
		int[] insertCounts = jtm.batchUpdate(insert_sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setString(1, roleUid);
				ps.setString(2, lists.get(i).getUseruid());
			}
			
			@Override
			public int getBatchSize() {
				return lists.size();
			}
		});
		
        return insertCounts;
	}
	
	public int deleteByUserUid(String uid) throws DataAccessException{
		String sql = "DELETE FROM rolemember WHERE useruid = ?";
		Object[] param = new Object[] {uid};
		return jtm.update(sql, param);
	}
	
	public int deleteByRoleUid(String uid) throws DataAccessException{
		String sql = "DELETE FROM rolemember WHERE roleuid = ?";
		Object[] param = new Object[] {uid};
		return jtm.update(sql, param);
	}
	
	public int deleteByPKUids(String roleUid, String userUid) throws DataAccessException{
		String sql = "DELETE FROM rolemember WHERE roleuid = ? AND useruid = ?";
		Object[] param = new Object[] {roleUid, userUid};
		return jtm.update(sql, param);
	}
	
	public Boolean existByRoleUid(String uid) throws DataAccessException{
        String sql = "SELECT COUNT(rm) > 0 "
        		+ "FROM rolemember rm "
        		+ "WHERE rm.roleuid=? AND 1=1";
        Object[] param = new Object[] {uid};
        
        Boolean ret = (Boolean) jtm.queryForObject(sql, Boolean.class, param);

        return ret;
    }
}
