package com.netpro.trinity.repository.jdbc.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.repository.jdbc.entity.VRAgentList;

@Repository  //宣告這是一個DAO類別
public class VRAgentListDao {
	public static final String insert_sql = "INSERT INTO jcsvirtualagentlist "
							+ "(virtualagentuid, agentuid, activate, description, seq) "
							+ "VALUES (?, ?, ?, ?, ?)";
	
	@Autowired
    protected JdbcTemplate jtm;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<VRAgentList> findExByVRAgentUid(String uid) throws DataAccessException{

        String sql = "SELECT a.virtualagentuid, a.agentuid, b.agentname, a.activate, a.description, a.seq "
        		+ "FROM jcsvirtualagentlist a, jcsagent b "
        		+ "WHERE a.agentuid = b.agentUID and a.virtualagentUID = ? "
        		+ "ORDER BY a.seq";
        Object[] param = new Object[] {uid};

        List<VRAgentList> lists = (List<VRAgentList>) jtm.query(sql, param,
                new BeanPropertyRowMapper(VRAgentList.class));

        return lists;
    }
	
	public Boolean exitByVRAgentUidAndSeq(String vragentuid, Integer seq) throws DataAccessException{

        String sql = "SELECT COUNT(*) "
        		+ "FROM jcsvirtualagentlist "
        		+ "WHERE virtualagentuid=? AND SEQ=?";
        Object[] param = new Object[] {vragentuid, seq};
        
        Integer ret = (Integer) jtm.queryForObject(sql, Integer.class, param);

        return ret > 0 ? true : false;
    }
	
	public int save(VRAgentList list) throws DataAccessException{
		Object[] params = new Object[] {list.getVirtualagentuid(), list.getAgentuid(), 
				list.getActivate(), list.getDescription(), list.getSeq()};
		
		return jtm.update(insert_sql, params);
	}
	
	public int[] saveBatch(List<VRAgentList> lists) throws DataAccessException{
		int[] insertCounts = jtm.batchUpdate(insert_sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setString(1, lists.get(i).getVirtualagentuid());
				ps.setString(2, lists.get(i).getAgentuid());
				ps.setString(3, lists.get(i).getActivate());
				ps.setString(4, lists.get(i).getDescription());
				ps.setInt(5, lists.get(i).getSeq());
			}
			
			@Override
			public int getBatchSize() {
				return lists.size();
			}
		});
		
        return insertCounts;
	}
	
	public int deleteByVRAgentUid(String uid) throws DataAccessException{
		String sql = "DELETE FROM jcsvirtualagentlist WHERE virtualagentuid = ?";
		Object[] param = new Object[] {uid};
		return jtm.update(sql, param);
	}
}