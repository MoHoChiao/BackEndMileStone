package com.netpro.trinity.resource.admin.authz.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.resource.admin.authz.entity.AccessRight;

@Repository  //宣告這是一個DAO類別
public class AccessRightJDBCDao {
	public static final String insert_sql = "INSERT INTO accessright "
							+ "(peopleuid, objectuid, flag1, flag2, flag3, flag4, flag5, flag6, flag7, flag8) "
							+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	public static final String update_sql = "UPDATE accessright "
			+ "SET flag1 = ?, flag2 = ?, flag3 = ?, flag4 = ?, flag5 = ?, flag6 = ?, flag7 = ?, flag8 = ? "
			+ "WHERE peopleuid = ? AND objectuid = ?";
	
	@Autowired
    protected JdbcTemplate jtm;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<AccessRight> findByPeopleUid(String uid) throws DataAccessException{

        String sql = "SELECT TRIM(a.peopleuid) as peopleuid, TRIM(a.objectuid) as objectuid, "
        		+ "a.flag1 as view, a.flag2 as add, a.flag3 as edit, a.flag4 as delete, "
        		+ "a.flag5 as run, a.flag6 as reRun, a.flag7 as grant, a.flag8 as import_export "
        		+ "FROM accessright a "
        		+ "WHERE a.peopleuid = ? "
        		+ "ORDER BY a.objectuid";
        Object[] param = new Object[] {uid};

        List<AccessRight> lists = (List<AccessRight>) jtm.query(sql, param,
                new BeanPropertyRowMapper(AccessRight.class));

        return lists;
    }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<AccessRight> findByObjectUid(String uid) throws DataAccessException{

        String sql = "SELECT TRIM(a.peopleuid) as peopleuid, TRIM(a.objectuid) as objectuid, "
        		+ "a.flag1 as view, a.flag2 as add, a.flag3 as edit, a.flag4 as delete, "
        		+ "a.flag5 as run, a.flag6 as reRun, a.flag7 as grant, a.flag8 as import_export "
        		+ "FROM accessright a "
        		+ "WHERE a.objectuid = ? "
        		+ "ORDER BY a.peopleuid";
        Object[] param = new Object[] {uid};

        List<AccessRight> lists = (List<AccessRight>) jtm.query(sql, param,
                new BeanPropertyRowMapper(AccessRight.class));

        return lists;
    }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<AccessRight> findUserExByObjectUid(String uid) throws DataAccessException{

        String sql = "SELECT TRIM(a.peopleuid) as peopleuid, TRIM(a.objectuid) as objectuid, "
        		+ "a.flag1 as view, a.flag2 as add, a.flag3 as edit, a.flag4 as delete, "
        		+ "a.flag5 as run, a.flag6 as reRun, a.flag7 as grant, a.flag8 as import_export, "
        		+ "b.userid as peopleid, b.username as peoplename "
        		+ "FROM accessright a, trinityuser b "
        		+ "WHERE a.peopleuid = b.useruid AND a.objectuid = ? "
        		+ "ORDER BY b.username, b.userid";
        Object[] param = new Object[] {uid};

        List<AccessRight> lists = (List<AccessRight>) jtm.query(sql, param,
                new BeanPropertyRowMapper(AccessRight.class));

        return lists;
    }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<AccessRight> findRoleExByObjectUid(String uid) throws DataAccessException{

        String sql = "SELECT TRIM(a.peopleuid) as peopleuid, TRIM(a.objectuid) as objectuid, "
        		+ "a.flag1 as view, a.flag2 as add, a.flag3 as edit, a.flag4 as delete, "
        		+ "a.flag5 as run, a.flag6 as reRun, a.flag7 as grant, a.flag8 as import_export, "
        		+ "b.rolename as peoplename "
        		+ "FROM accessright a, role b "
        		+ "WHERE a.peopleuid = b.roleuid AND a.objectuid = ? "
        		+ "ORDER BY b.rolename";
        Object[] param = new Object[] {uid};

        List<AccessRight> lists = (List<AccessRight>) jtm.query(sql, param,
                new BeanPropertyRowMapper(AccessRight.class));

        return lists;
    }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<AccessRight> findFunctionalPermissionByPeopleUid(String uid) throws DataAccessException{

        String sql = "SELECT TRIM(a.peopleuid) as peopleuid, TRIM(a.objectuid) as objectuid, "
        		+ "a.flag1 as view, a.flag2 as add, a.flag3 as edit, a.flag4 as delete, "
        		+ "a.flag5 as run, a.flag6 as reRun, a.flag7 as grant, a.flag8 as import_export "
        		+ "FROM accessright a "
        		+ "WHERE a.peopleuid = ? AND a.objectuid like ? "
        		+ "ORDER BY a.objectuid";
        Object[] param = new Object[] {uid, "function-%"};

        List<AccessRight> lists = (List<AccessRight>) jtm.query(sql, param,
                new BeanPropertyRowMapper(AccessRight.class));

        return lists;
    }
	
	public Boolean existByAllPKs(AccessRight list) throws DataAccessException{
        String sql = "SELECT COUNT(list) > 0 "
        		+ "FROM accessright list "
        		+ "WHERE peopleuid=? AND objectuid=? AND 1=1";
        Object[] param = new Object[] {list.getPeopleuid(), list.getObjectuid()};
        
        Boolean ret = (Boolean) jtm.queryForObject(sql, Boolean.class, param);

        return ret;
    }
	
	public Boolean existByObjectUid(String objectUid) throws DataAccessException{
        String sql = "SELECT COUNT(list) > 0 "
        		+ "FROM accessright list "
        		+ "WHERE objectuid=? AND 1=1";
        Object[] param = new Object[] {objectUid};
        
        Boolean ret = (Boolean) jtm.queryForObject(sql, Boolean.class, param);

        return ret;
    }
	
	public int add(AccessRight list) throws DataAccessException{
		Object[] params = new Object[] {list.getPeopleuid(), list.getObjectuid(), 
				list.getView(), list.getAdd(), list.getEdit(), list.getDelete(), list.getRun(), list.getReRun(), list.getGrant(), list.getImport_export()};
		
		return jtm.update(insert_sql, params);
	}
	
	public int update(AccessRight list) throws DataAccessException{
		Object[] params = new Object[] {list.getView(), list.getAdd(), list.getEdit(), list.getDelete(), 
				list.getRun(), list.getReRun(), list.getGrant(), list.getImport_export(), list.getPeopleuid(), list.getObjectuid(),};
		
		return jtm.update(update_sql, params);
	}
	
	public int deleteByPeopleUid(String uid) throws DataAccessException{
		String sql = "DELETE FROM accessright WHERE peopleuid = ?";
		Object[] param = new Object[] {uid};
		return jtm.update(sql, param);
	}
	
	public int deleteByObjectUid(String uid) throws DataAccessException{
		String sql = "DELETE FROM accessright WHERE objectuid = ?";
		Object[] param = new Object[] {uid};
		return jtm.update(sql, param);
	}
	
	public int deleteByAliasParentUid(String parentUid) throws DataAccessException{
		String sql = "DELETE FROM accessright WHERE objectuid like ?";
		Object[] param = new Object[] {parentUid + "$%"};
		return jtm.update(sql, param);
	}
	
	public int deleteByPKs(String peopleUid, String objectUid) throws DataAccessException{
		String sql = "DELETE FROM accessright WHERE peopleuid = ? AND objectuid = ?";
		Object[] param = new Object[] {peopleUid, objectUid};
		return jtm.update(sql, param);
	}
	
	public Boolean isAdminEditMode() throws DataAccessException{
        String sql = "SELECT Option3 "
        		+ "FROM FunctionSetting";
        
        String ret = (String) jtm.queryForObject(sql, String.class);
        if("1".equals(ret))
        	return true;
        else
        	return false;
    }
}
