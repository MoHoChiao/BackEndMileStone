package com.netpro.trinity.resource.admin.objectalias.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.resource.admin.objectalias.entity.ObjectAlias;

@Repository  //宣告這是一個DAO類別
public class ObjectAliasJDBCDao {
	public static final String insert_sql = "INSERT INTO objectalias "
			+ "(parentuid, aliasname, aliastype, objectuid, description) "
			+ "VALUES (?, ?, ?, ?, ?)";
	public static final String update_sql = "UPDATE objectalias SET aliastype = ?, objectuid = ?, "
			+ "description = ? where parentuid = ? AND aliasname = ?";
	
	@Autowired
    protected JdbcTemplate jtm;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<ObjectAlias> findByParentUid(String uid) throws DataAccessException{

        String sql = "SELECT oa.parentuid, oa.aliasname, oa.aliastype, oa.objectuid, oa.description "
        		+ "FROM objectalias oa "
        		+ "WHERE oa.parentuid = ? "
        		+ "ORDER BY oa.aliasname";
        Object[] param = new Object[] {uid};

        List<ObjectAlias> lists = (List<ObjectAlias>) jtm.query(sql, param,
                new BeanPropertyRowMapper(ObjectAlias.class));

        return lists;
    }
	
	public Boolean existByObjectUid(String objectUid) throws DataAccessException{

        String sql = "SELECT COUNT(list) > 0 "
        		+ "FROM objectalias list "
        		+ "WHERE objectuid=? AND 1=1";
        Object[] param = new Object[] {objectUid};
        
        Boolean ret = (Boolean) jtm.queryForObject(sql, Boolean.class, param);

        return ret;
    }
	
	public Boolean existByPKs(String parentUid, String aliasName) throws DataAccessException{

        String sql = "SELECT COUNT(list) > 0 "
        		+ "FROM objectalias list "
        		+ "WHERE parentuid=? AND aliasname=? AND 1=1";
        Object[] param = new Object[] {parentUid, aliasName};
        
        Boolean ret = (Boolean) jtm.queryForObject(sql, Boolean.class, param);

        return ret;
    }
	
	public int save(ObjectAlias list) throws DataAccessException{
		Object[] params = new Object[] {list.getParentuid(), list.getAliasname().trim().toUpperCase(), 
				list.getAliastype(), list.getObjectuid(), list.getDescription()};
		
		return jtm.update(insert_sql, params);
	}
	
	public int update(ObjectAlias list) throws DataAccessException{
		Object[] params = new Object[] {list.getAliastype(), list.getObjectuid(), 
				list.getDescription(), list.getParentuid(), list.getAliasname().trim().toUpperCase()};
		
		return jtm.update(update_sql, params);
	}
	
	public int deleteByParentUid(String uid) throws DataAccessException{
		String sql = "DELETE FROM objectalias WHERE parentuid = ?";
		Object[] param = new Object[] {uid};
		return jtm.update(sql, param);
	}
	
	public int deleteByPKs(String parentUid, String aliasName) throws DataAccessException{
		String sql = "DELETE FROM objectalias WHERE parentuid = ? AND aliasname = ?";
		Object[] param = new Object[] {parentUid, aliasName.trim().toUpperCase()};
		return jtm.update(sql, param);
	}
}
