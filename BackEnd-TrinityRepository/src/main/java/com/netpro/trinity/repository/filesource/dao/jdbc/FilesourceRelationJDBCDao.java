package com.netpro.trinity.repository.filesource.dao.jdbc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.repository.filesource.entity.jdbc.FilesourceRelation;

@Repository  //宣告這是一個DAO類別
public class FilesourceRelationJDBCDao {
	public static final String insert_sql = "INSERT INTO filesourcerelation "
							+ "(fscategoryuid, filesourceuid) "
							+ "VALUES (?, ?)";
	
	@Autowired
    protected JdbcTemplate jtm;
	
	public List<String> findAllFileSourceUids() throws DataAccessException{

		String sql = "SELECT distinct(fr.filesourceuid) "
        		+ "FROM filesourcerelation fr";

        List<String> lists = (List<String>) jtm.queryForList(sql, String.class);
        return lists;
    }
	
	public List<String> findFileSourceUidsByCategoryUid(String uid) throws DataAccessException{

        String sql = "SELECT fr.filesourceuid "
        		+ "FROM filesourcerelation fr "
				+ "WHERE fr.fscategoryuid = ? "
        		+ "ORDER BY fr.lastupdatetime DESC";
        Object[] param = new Object[] {uid};

        List<String> lists = (List<String>) jtm.queryForList(sql, param, String.class);
        return lists;
    }
	
	public int save(FilesourceRelation rel) throws DataAccessException{
		Object[] params = new Object[] {rel.getFscategoryuid(), rel.getFilesourceuid()};
		
		return jtm.update(insert_sql, params);
	}
	
	public int deleteByFileSourceUid(String uid) throws DataAccessException{
		String sql = "DELETE FROM filesourcerelation WHERE filesourceuid = ?";
		Object[] param = new Object[] {uid};
		return jtm.update(sql, param);
	}
	
	public Boolean exitByCategoryUid(String uid) throws DataAccessException{
        String sql = "SELECT COUNT(rel) > 0 "
        		+ "FROM filesourcerelation rel "
        		+ "WHERE fscategoryuid=? AND 1=1";
        Object[] param = new Object[] {uid};
        
        Boolean ret = (Boolean) jtm.queryForObject(sql, Boolean.class, param);

        return ret;
    }
}
