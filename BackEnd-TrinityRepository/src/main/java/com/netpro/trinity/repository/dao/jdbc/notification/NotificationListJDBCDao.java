package com.netpro.trinity.repository.dao.jdbc.notification;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.repository.entity.notification.jdbc.NotificationList;

@Repository  //宣告這是一個DAO類別
public class NotificationListJDBCDao {
	public static final String insert_sql = "INSERT INTO notificationlist "
							+ "(notificationuid, destinationuid, destinationtype, activate) "
							+ "VALUES (?, ?, ?, ?)";
	
	@Autowired
    protected JdbcTemplate jtm;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<NotificationList> findByNotifyUid(String uid) throws DataAccessException{

        String sql = "SELECT a.notificationuid, a.destinationuid, a.destinationtype, a.activate "
        		+ "FROM notificationlist a "
        		+ "WHERE a.notificationuid = ? "
        		+ "ORDER BY a.lastupdatetime";
        Object[] param = new Object[] {uid};

        List<NotificationList> lists = (List<NotificationList>) jtm.query(sql, param,
                new BeanPropertyRowMapper(NotificationList.class));

        return lists;
    }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<NotificationList> findUserExByNotifyUid(String uid) throws DataAccessException{

        String sql = "SELECT a.notificationuid, a.destinationuid, a.destinationtype, a.activate, b.userid as destinationid, b.username as destinationname "
        		+ "FROM notificationlist a, trinityuser b "
        		+ "WHERE a.destinationuid = b.useruid and a.notificationuid = ? "
        		+ "ORDER BY b.username, b.userid";
        Object[] param = new Object[] {uid};

        List<NotificationList> lists = (List<NotificationList>) jtm.query(sql, param,
                new BeanPropertyRowMapper(NotificationList.class));

        return lists;
    }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<NotificationList> findGroupExByNotifyUid(String uid) throws DataAccessException{

        String sql = "SELECT a.notificationuid, a.destinationuid, a.destinationtype, a.activate, b.groupname as destinationname "
        		+ "FROM notificationlist a, usergroup b "
        		+ "WHERE a.destinationuid = b.groupuid and a.notificationuid = ? "
        		+ "ORDER BY b.groupname";
        Object[] param = new Object[] {uid};

        List<NotificationList> lists = (List<NotificationList>) jtm.query(sql, param,
                new BeanPropertyRowMapper(NotificationList.class));

        return lists;
    }
	
	public Boolean existByAllPKs(NotificationList list) throws DataAccessException{
        String sql = "SELECT COUNT(list) > 0 "
        		+ "FROM notificationlist list "
        		+ "WHERE notificationuid=? AND destinationuid=? AND 1=1";
        Object[] param = new Object[] {list.getNotificationuid(), list.getDestinationuid()};
        
        Boolean ret = (Boolean) jtm.queryForObject(sql, Boolean.class, param);

        return ret;
    }
	
	public int save(NotificationList list) throws DataAccessException{
		Object[] params = new Object[] {list.getNotificationuid(), list.getDestinationuid(), 
				list.getDestinationtype(), list.getActivate()};
		
		return jtm.update(insert_sql, params);
	}
	
	public int[] saveBatch(String notificationuid, List<NotificationList> lists) throws DataAccessException{
		int[] insertCounts = jtm.batchUpdate(insert_sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setString(1, notificationuid);
				ps.setString(2, lists.get(i).getDestinationuid());
				ps.setString(3, lists.get(i).getDestinationtype());
				ps.setString(4, lists.get(i).getActivate());
			}
			
			@Override
			public int getBatchSize() {
				return lists.size();
			}
		});
		
        return insertCounts;
	}
	
	public int deleteByNotifyUid(String notificationUid) throws DataAccessException{
		String sql = "DELETE FROM notificationlist WHERE notificationuid = ?";
		Object[] param = new Object[] {notificationUid};
		return jtm.update(sql, param);
	}
	
	public int deleteByDestinationUid(String destinationUid) throws DataAccessException{
		String sql = "DELETE FROM notificationlist WHERE destinationuid = ?";
		Object[] param = new Object[] {destinationUid};
		return jtm.update(sql, param);
	}
}
