package com.netpro.trinity.repository.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.repository.entity.VRAgentList;

@Repository  //宣告這是一個DAO類別
public class VRAgentListDao {
	@Autowired
    protected JdbcTemplate jtm;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<VRAgentList> findExByVRAgentUid(String uid) {

        String sql = "Select a.virtualagentuid, a.agentuid, b.agentname, a.activate, a.description, a.seq "
        		+ "FROM jcsvirtualagentlist a, jcsagent b "
        		+ "WHERE a.agentuid = b.agentUID and a.virtualagentUID = ? "
        		+ "ORDER BY a.seq";
        Object[] param = new Object[] {uid};

        List<VRAgentList> lists = (List<VRAgentList>) jtm.query(sql, param,
                new BeanPropertyRowMapper(VRAgentList.class));

        return lists;
    }
	
	public int[] batchSave(List<VRAgentList> lists) {
		String insert_sql = "INSERT INTO jcsvirtualagentlist " +
				"(virtualagentuid, agentuid, activate, description, seq) "
				+ "VALUES (?, ?, ?, ?, ?)";
		
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
	
	public int deleteByVRAgentUid(String uid) {
		String sql = "DELETE FROM jcsvirtualagentlist WHERE virtualagentuid = ?";
		Object[] param = new Object[] {uid};
		return jtm.update(sql, param);
	}
}
