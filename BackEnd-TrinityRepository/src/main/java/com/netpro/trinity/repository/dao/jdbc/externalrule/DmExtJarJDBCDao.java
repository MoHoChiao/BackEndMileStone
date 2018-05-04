package com.netpro.trinity.repository.dao.jdbc.externalrule;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.repository.entity.externalrule.jdbc.DmExtJar;

@Repository  //宣告這是一個DAO類別
public class DmExtJarJDBCDao {
	public static final String insert_sql = "INSERT INTO dmextjar "
							+ "(extjaruid, filename, packageuid, data, md5, uploadtime, filetype, description) "
							+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	
	@Autowired
    protected JdbcTemplate jtm;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public DmExtJar findByUid(String uid) throws DataAccessException{

        String sql = "SELECT jar.extjaruid, jar.filename, jar.packageuid, jar.md5, jar.uploadtime, jar.filetype, jar.description "
        		+ "FROM dmextjar jar "
        		+ "WHERE jar.extjaruid = ? "
        		+ "ORDER BY jar.filetype, jar.filename";
        Object[] param = new Object[] {uid};

        DmExtJar jar = (DmExtJar) jtm.query(sql, param,
                new BeanPropertyRowMapper(DmExtJar.class));

        return jar;
    }
	
	public Boolean existByUid(String uid) throws DataAccessException{

        String sql = "SELECT COUNT(list) > 0 "
        		+ "FROM dmextjar list "
        		+ "WHERE extjaruid=? AND 1=1";
        Object[] param = new Object[] {uid};
        
        Boolean ret = (Boolean) jtm.queryForObject(sql, Boolean.class, param);

        return ret;
    }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<DmExtJar> findByPackageUid(String packageUid) throws DataAccessException{

        String sql = "SELECT jar.extjaruid, jar.filename, jar.packageuid, jar.md5, jar.uploadtime, jar.filetype, jar.description "
        		+ "FROM dmextjar jar "
        		+ "WHERE jar.packageuid = ? "
        		+ "ORDER BY jar.filetype, jar.filename";
        Object[] param = new Object[] {packageUid};

        List<DmExtJar> lists = (List<DmExtJar>) jtm.query(sql, param,
                new BeanPropertyRowMapper(DmExtJar.class));

        return lists;
    }
	
	public int save(DmExtJar jar) throws DataAccessException{
		Object[] params = new Object[] {jar.getExtjaruid(), jar.getFilename(), 
				jar.getPackageuid(), jar.getData(), jar.getMd5(), jar.getUploadtime(), jar.getFiletype(), jar.getDescription()};
		
		return jtm.update(insert_sql, params);
	}
	
	public int deleteByUid(String uid) throws DataAccessException{
		String sql = "DELETE FROM dmextjar WHERE extjaruid = ?";
		Object[] param = new Object[] {uid};
		return jtm.update(sql, param);
	}
	
	public int deleteByPackageUid(String packageUid) throws DataAccessException{
		String sql = "DELETE FROM dmextjar WHERE packageuid = ?";
		Object[] param = new Object[] {packageUid};
		return jtm.update(sql, param);
	}
}