package com.netpro.trinity.resource.admin.connection.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.resource.admin.connection.entity.Connection;

@Repository // 宣告這是一個DAO類別
public class ConnectionJDBCDao {

	@Autowired
	protected JdbcTemplate jtm;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Connection> findByConnType(String type) throws DataAccessException {
		String sql = "SELECT connectionuid, connectionname FROM connection WHERE connectiontype = ? ORDER BY connectionname";
		Object[] param = new Object[] {type};

		List<Connection> lists = (List<Connection>) jtm.query(sql, param, new BeanPropertyRowMapper(Connection.class));
		return lists;
	}

}
