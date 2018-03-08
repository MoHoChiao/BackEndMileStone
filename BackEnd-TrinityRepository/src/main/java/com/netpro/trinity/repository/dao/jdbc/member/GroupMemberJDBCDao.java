package com.netpro.trinity.repository.dao.jdbc.member;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.repository.entity.member.jdbc.GroupMember;

@Repository  //宣告這是一個DAO類別
public class GroupMemberJDBCDao {
	public static final String insert_sql = "INSERT INTO groupmember "
							+ "(groupuid, useruid) "
							+ "VALUES (?, ?)";
	
	@Autowired
    protected JdbcTemplate jtm;
	
	public List<String> findAllUserUids() throws DataAccessException{

		String sql = "SELECT distinct(gm.useruid) "
        		+ "FROM groupmember gm";

        List<String> lists = (List<String>) jtm.queryForList(sql, String.class);
        return lists;
    }
	
	public List<String> findUserUidsByGroupUid(String uid) throws DataAccessException{

        String sql = "SELECT gm.useruid "
        		+ "FROM groupmember gm "
				+ "WHERE gm.groupuid = ? "
        		+ "ORDER BY gm.lastupdatetime DESC";
        Object[] param = new Object[] {uid};

        List<String> lists = (List<String>) jtm.queryForList(sql, param, String.class);
        return lists;
    }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<GroupMember> findUserFullNameByGroupUid(String uid) throws DataAccessException{

        String sql = "SELECT u.useruid, u.username, u.userid FROM groupmember gm "
        		+ "LEFT JOIN trinityuser u ON gm.useruid=u.useruid "
				+ "WHERE gm.groupuid = ? "
        		+ "ORDER BY u.username, u.userid";
        Object[] param = new Object[] {uid};

        List<GroupMember> lists = (List<GroupMember>) jtm.query(sql, param,
                new BeanPropertyRowMapper(GroupMember.class));
        return lists;
    }
	
	public int save(GroupMember gm) throws DataAccessException{
		Object[] params = new Object[] {gm.getGroupuid(), gm.getUseruid()};
		
		return jtm.update(insert_sql, params);
	}
	
	public int[] saveBatch(String groupUid, List<GroupMember> lists) throws DataAccessException{
		int[] insertCounts = jtm.batchUpdate(insert_sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setString(1, groupUid);
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
		String sql = "DELETE FROM groupmember WHERE useruid = ?";
		Object[] param = new Object[] {uid};
		return jtm.update(sql, param);
	}
	
	public int deleteByGroupUid(String uid) throws DataAccessException{
		String sql = "DELETE FROM groupmember WHERE groupuid = ?";
		Object[] param = new Object[] {uid};
		return jtm.update(sql, param);
	}
	
	public int deleteByPKUids(String groupUid, String userUid) throws DataAccessException{
		String sql = "DELETE FROM groupmember WHERE groupuid = ? AND useruid = ?";
		Object[] param = new Object[] {groupUid, userUid};
		return jtm.update(sql, param);
	}
	
	public Boolean existByGroupUid(String uid) throws DataAccessException{
        String sql = "SELECT COUNT(gm) > 0 "
        		+ "FROM groupmember gm "
        		+ "WHERE gm.groupuid=? AND 1=1";
        Object[] param = new Object[] {uid};
        
        Boolean ret = (Boolean) jtm.queryForObject(sql, Boolean.class, param);

        return ret;
    }
}
