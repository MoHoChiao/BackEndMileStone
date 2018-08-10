package com.netpro.trinity.service.filesource.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.service.filesource.entity.FileSourceCategory;
import com.netpro.trinity.service.filesource.entity.FilesourceRelation;

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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public FileSourceCategory findCategoryByFilesourceUid(String uid) throws DataAccessException{

		String sql = "SELECT fc.fscategoryname, fc.fscategoryuid "
        		+ "FROM filesourcerelation fr left join filesourcecategory fc "
				+ "on fr.fscategoryuid = fc.fscategoryuid "
				+ "WHERE fr.filesourceuid = ? ";
        Object[] param = new Object[] {uid};
        
        FileSourceCategory category = null;
        try {
        	category = (FileSourceCategory) jtm.queryForObject(sql, param, new BeanPropertyRowMapper(FileSourceCategory.class));
        } catch (EmptyResultDataAccessException e) {}
        
        return category;
    }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, String> findFilesourceUidAndCategoryNameMap() throws DataAccessException{
        String sql = "SELECT fr.filesourceuid, fc.fscategoryname "
        		+ "FROM filesourcerelation fr left join filesourcecategory fc "
				+ "on fr.fscategoryuid = fc.fscategoryuid";
        
        List<FilesourceRelation> lists = (List<FilesourceRelation>) jtm.query(sql, new BeanPropertyRowMapper(FilesourceRelation.class));
        
        Map<String, String> map = new HashMap<String, String>();
        for(FilesourceRelation list : lists) {
        	map.put(list.getFilesourceuid(), list.getFscategoryname());
        }
        
        return map;
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
	
	public Boolean existByCategoryUid(String uid) throws DataAccessException{
        String sql = "SELECT COUNT(rel) > 0 "
        		+ "FROM filesourcerelation rel "
        		+ "WHERE fscategoryuid=? AND 1=1";
        Object[] param = new Object[] {uid};
        
        Boolean ret = (Boolean) jtm.queryForObject(sql, Boolean.class, param);

        return ret;
    }
}
