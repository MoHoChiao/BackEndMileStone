package com.netpro.trinity.service.connection.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.netpro.trinity.service.connection.dao.ConnectionJPADao;
import com.netpro.trinity.service.connection.entity.Connection;
import com.netpro.trinity.service.connection.entity.ConnectionRelation;
import com.netpro.trinity.service.connection.entity.DatabaseConnection;
import com.netpro.trinity.service.connection.entity.FTPConnection;
import com.netpro.trinity.service.connection.entity.JDBCConnection;
import com.netpro.trinity.service.connection.entity.MailConnection;
import com.netpro.trinity.service.connection.entity.NotesConnection;
import com.netpro.trinity.service.connection.entity.OSConnection;
import com.netpro.trinity.service.connection.entity.SapConnection;
import com.netpro.trinity.service.drivermanager.dto.DriverInfo;
import com.netpro.trinity.service.drivermanager.util.MetadataDriverMaintain;
import com.netpro.trinity.service.drivermanager.util.MetadataDriverManager;
import com.netpro.trinity.service.dto.FilterInfo;
import com.netpro.trinity.service.dto.Ordering;
import com.netpro.trinity.service.dto.Paging;
import com.netpro.trinity.service.dto.Querying;
import com.netpro.trinity.service.filesource.service.FileSourceService;
import com.netpro.trinity.service.job.service.JobstepService;
import com.netpro.trinity.service.member.service.TrinityuserService;
import com.netpro.trinity.service.objectalias.service.ObjectAliasService;
import com.netpro.trinity.service.permission.dto.AccessRight;
import com.netpro.trinity.service.permission.feign.PermissionClient;
import com.netpro.trinity.service.prop.dto.TrinityDataJDBC;
import com.netpro.trinity.service.util.ACUtil;
import com.netpro.trinity.service.util.Constant;
import com.netpro.trinity.service.util.Crypto;
import com.netpro.trinity.service.util.XMLDataUtility;

@Service
public class ConnectionService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionService.class);
	
	public static final String[] CONNECTION_FIELD_VALUES = new String[] { "connectionname", "connectiontype", "description" };
	public static final Set<String> CONNECTION_FIELD_SET = new HashSet<>(Arrays.asList(CONNECTION_FIELD_VALUES));
	
	/*
	 * connectiontype的值,對應如下
	 * J : JDBC Connection
	 * D : Database Connection
	 * S : Sap
	 * N : notes
	 * F : FTP
	 * O : OS
	 * M : Mail
	 */
	public static final String[] CONNECTION_TYPE_VALUES = new String[] { "J", "D", "S", "N", "F", "O", "M" };
	public static final Set<String> CONNECTION_TYPE_SET = new HashSet<>(Arrays.asList(CONNECTION_TYPE_VALUES));
	
	@Autowired
	private ConnectionJPADao dao;
	
	@Autowired
	private TrinityuserService userService;
	@Autowired
	private ConnectionRelationService relService;
	@Autowired
	private FileSourceService filesourceService;
	@Autowired
	private JobstepService jobstepService;
	@Autowired
	private ObjectAliasService objectAliasService;
	
	@Autowired
	private PermissionClient permissionClient;
	
	@Autowired
	private TrinityDataJDBC jdbcInfo;
	
	@Autowired
	MetadataDriverManager manager;
	@Autowired
	MetadataDriverMaintain maintain;
	
	@Autowired
	private XMLDataUtility xmlUtil;
	
	@Value("${encrypt.key}")
	private String encryptKey;
	
	public List<Connection> getAll() throws Exception{
		List<Connection> conns = this.dao.findAll();
		return getExtraXmlProp(conns);
	}
	
	public List<Connection> getAllWithoutInCategory() throws Exception{
		List<String> conn_uids = relService.getAllConnectionUids();
		if(conn_uids.isEmpty()) {
			/*
			 * 當系統沒有建立任何conn和conn category之間的relation時, 則任意給一個空字串值
			 * 以免not in當中的值為empty, 導致搜尋不到任何不在conn category中的conn
			 */
			conn_uids.add("");
		}
		List<Connection> conns = this.dao.findByConnectionuidNotIn(conn_uids, Sort.by("connectionname"));
		return getExtraXmlProp(conns);
	}
	
	public Connection getByUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.trim().isEmpty())
			throw new IllegalArgumentException("Connection UID can not be empty!");
		
		Connection conn = null;
		try {
			conn = this.dao.findById(uid).get();
		}catch(NoSuchElementException e) {}
		 
		if(conn == null)
			throw new IllegalArgumentException("Connection UID does not exist!(" + uid + ")");
		
		return getExtraXmlProp(conn);
	}
	
	public List<Connection> getByName(String name) throws IllegalArgumentException, Exception{
		if(name == null || name.trim().isEmpty())
			throw new IllegalArgumentException("Connection Name can not be empty!");
		
		List<Connection> conns = this.dao.findByconnectionname(name.toUpperCase());
		return getExtraXmlProp(conns);
	}
	
	public List<Connection> getByType(String type) throws IllegalArgumentException, Exception{
		if(type == null || type.isEmpty())
			throw new IllegalArgumentException("Connection Type can not be empty!");
		
		type = type.toUpperCase();
		
		/*
		 * connectiontype的值,對應如下
		 * J : JDBC Connection
		 * D : Database Connection
		 * S : Sap
		 * N : notes
		 * F : FTP
		 * O : OS
		 * M : Mail
		 */
		if(!CONNECTION_TYPE_SET.contains(type))
				throw new IllegalArgumentException("Illegal connection type! "+ CONNECTION_TYPE_SET.toString());
		
		List<Connection> conns = this.dao.findByconnectiontype(type);
		
		return getExtraXmlProp(conns);
	}
	
	public List<Connection> getByCategoryUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.trim().isEmpty())
			throw new IllegalArgumentException("Connection Category UID can not be empty!");
		
		List<Connection> conns = this.dao.findByConnectionuidIn(relService.getConnectionUidsByCategoryUid(uid), Sort.by("connectionname"));
		return getExtraXmlProp(conns);
	}
	
	@SuppressWarnings("unchecked")
	public ResponseEntity<?> getByFilter(String categoryUid, FilterInfo filter) throws SecurityException, NoSuchMethodException, 
								IllegalArgumentException, IllegalAccessException, InvocationTargetException, Exception{
		if(filter == null) {
			List<Connection> conns;
			if(categoryUid == null) {
				conns = this.dao.findAll();
			}else {
				if(categoryUid.trim().isEmpty()) {
					List<String> conn_uids = relService.getAllConnectionUids();
					if(conn_uids.isEmpty()) {
						/*
						 * 當系統沒有建立任何conn和conn category之間的relation時, 則任意給一個空字串值
						 * 以免not in當中的值為empty, 導致搜尋不到任何不在conn category中的conn
						 */
						conn_uids.add("");
					}
					conns = this.dao.findByConnectionuidNotIn(conn_uids);
				}else {
					conns = this.dao.findByConnectionuidIn(relService.getConnectionUidsByCategoryUid(categoryUid));
				}
			}
			return ResponseEntity.ok(getExtraXmlProp(conns));
		}
		
		Paging paging = filter.getPaging();
		Ordering ordering = filter.getOrdering();
		Querying querying = filter.getQuerying();
		
		if(paging == null && ordering == null && querying == null) {
			List<Connection> conns;
			if(categoryUid == null) {
				conns = this.dao.findAll();
			}else {
				if(categoryUid.trim().isEmpty()) {
					List<String> conn_uids = relService.getAllConnectionUids();
					if(conn_uids.isEmpty()) {
						/*
						 * 當系統沒有建立任何conn和conn category之間的relation時, 則任意給一個空字串值
						 * 以免not in當中的值為empty, 導致搜尋不到任何不在conn category中的conn
						 */
						conn_uids.add("");
					}
					conns = this.dao.findByConnectionuidNotIn(conn_uids);
				}else {
					conns = this.dao.findByConnectionuidIn(relService.getConnectionUidsByCategoryUid(categoryUid));
				}
			}
			return ResponseEntity.ok(getExtraXmlProp(conns));
		}
		
		PageRequest pageRequest = null;
		Sort sort = null;
		
		if(paging != null) {
			pageRequest = getPagingAndOrdering(paging, ordering);
		}else {
			if(ordering != null) {
				sort = getOrdering(ordering);
			}
		}
		
		if(querying == null) {
			if(pageRequest != null) {
				Page<Connection> page_conn;
				if(categoryUid == null) {
					page_conn = this.dao.findAll(pageRequest);
				}else {
					if(categoryUid.trim().isEmpty()) {
						List<String> conn_uids = relService.getAllConnectionUids();
						if(conn_uids.isEmpty()) {
							/*
							 * 當系統沒有建立任何conn和conn category之間的relation時, 則任意給一個空字串值
							 * 以免not in當中的值為empty, 導致搜尋不到任何不在conn category中的conn
							 */
							conn_uids.add("");
						}
						page_conn = this.dao.findByConnectionuidNotIn(conn_uids, pageRequest);
					}else {
						page_conn = this.dao.findByConnectionuidIn(relService.getConnectionUidsByCategoryUid(categoryUid), pageRequest);
					}
				}
				
				/*
				 * 因為page_conn取出來的內容全是Connection物件, 需要視connection type來轉換為不同的connection物件
				 * connection物件如JDBCConnection,DatabaseConnection...等等
				 * 而page_conn的內容是不可更動的(沒有所謂setContent方法),因此只能使用下面方法來替換其內容
				 */
				final Page<Connection> new_page_conn = page_conn.map(source -> {
					try {
						return getExtraXmlProp(source);
					} catch (Exception e) {
						ConnectionService.LOGGER.error("Exception; reason was:", e);
						return source;
					}
				});
				
				return ResponseEntity.ok(new_page_conn);
			}else if(sort != null) {
				List<Connection> conns;
				if(categoryUid == null) {
					conns = this.dao.findAll(sort);
				}else {
					if(categoryUid.trim().isEmpty()) {
						List<String> conn_uids = relService.getAllConnectionUids();
						if(conn_uids.isEmpty()) {
							/*
							 * 當系統沒有建立任何conn和conn category之間的relation時, 則任意給一個空字串值
							 * 以免not in當中的值為empty, 導致搜尋不到任何不在conn category中的conn
							 */
							conn_uids.add("");
						}
						conns = this.dao.findByConnectionuidNotIn(conn_uids, sort);
					}else {
						conns = this.dao.findByConnectionuidIn(relService.getConnectionUidsByCategoryUid(categoryUid), sort);
					}
				}
				return ResponseEntity.ok(getExtraXmlProp(conns));
			}else {
				/*
				 * The paging and ordering both objects are null.
				 * it means pageRequest and sort must be null too.
				 * then return default
				 */
				List<Connection> conns;
				if(categoryUid == null) {
					conns = this.dao.findAll();
				}else {
					if(categoryUid.trim().isEmpty()) {
						List<String> conn_uids = relService.getAllConnectionUids();
						if(conn_uids.isEmpty()) {
							/*
							 * 當系統沒有建立任何conn和conn category之間的relation時, 則任意給一個空字串值
							 * 以免not in當中的值為empty, 導致搜尋不到任何不在conn category中的conn
							 */
							conn_uids.add("");
						}
						conns = this.dao.findByConnectionuidNotIn(conn_uids);
					}else {
						conns = this.dao.findByConnectionuidIn(relService.getConnectionUidsByCategoryUid(categoryUid));
					}
				}
				return ResponseEntity.ok(getExtraXmlProp(conns));
			}
		}else {
			if(querying.getQueryType() == null || !Constant.QUERY_TYPE_SET.contains(querying.getQueryType().toLowerCase()))
				throw new IllegalArgumentException("Illegal query type! "+Constant.QUERY_TYPE_SET.toString());
			if(querying.getQueryField() == null || !CONNECTION_FIELD_SET.contains(querying.getQueryField().toLowerCase()))
				throw new IllegalArgumentException("Illegal query field! "+ CONNECTION_FIELD_SET.toString());
			if(querying.getIgnoreCase() == null)
				querying.setIgnoreCase(false);
			
			String queryType = querying.getQueryType().toLowerCase();
			String queryField = querying.getQueryField().toLowerCase(); //Must be lower case for jpa method
			String queryString = querying.getQueryString();
			
			StringBuffer methodName = new StringBuffer("findBy");
			methodName.append(queryField);
			if(queryType.equals("like")) {
				methodName.append("Like");
				queryString = "%" + queryString + "%";
			}
			if(querying.getIgnoreCase()) {
				methodName.append("IgnoreCase");
			}	
			if(categoryUid != null) {
				if(categoryUid.trim().isEmpty()) {
					methodName.append("AndConnectionuidNotIn");
				}else {
					methodName.append("AndConnectionuidIn");
				}
			}
			
			Method method = null;
			if(pageRequest != null){
				Page<Connection> page_conn;
				if(categoryUid == null) {
					method = this.dao.getClass().getMethod(methodName.toString(), String.class, Pageable.class);
					page_conn = (Page<Connection>) method.invoke(this.dao, queryString, pageRequest);
				}else {
					method = this.dao.getClass().getMethod(methodName.toString(), String.class, Pageable.class, List.class);
					if(categoryUid.trim().isEmpty()) {
						List<String> conn_uids = relService.getAllConnectionUids();
						if(conn_uids.isEmpty()) {
							/*
							 * 當系統沒有建立任何conn和conn category之間的relation時, 則任意給一個空字串值
							 * 以免not in當中的值為empty, 導致搜尋不到任何不在conn category中的conn
							 */
							conn_uids.add("");
						}
						page_conn = (Page<Connection>) method.invoke(this.dao, queryString, pageRequest, conn_uids);
					}else {
						page_conn = (Page<Connection>) method.invoke(this.dao, queryString, pageRequest, relService.getConnectionUidsByCategoryUid(categoryUid));
					}
				}
				
				/*
				 * 因為page_conn取出來的內容全是Connection物件, 需要視connection type來轉換為不同的connection物件
				 * connection物件如JDBCConnection,DatabaseConnection...等等
				 * 而page_conn的內容是不可更動的(沒有所謂setContent方法),因此只能使用下面方法來替換其內容
				 */
				final Page<Connection> new_page_conn = page_conn.map(source -> {
					try {
						return getExtraXmlProp(source);
					} catch (Exception e) {
						ConnectionService.LOGGER.error("Exception; reason was:", e);
						return source;
					}
				});
				return ResponseEntity.ok(new_page_conn);
			}else if(sort != null) {
				List<Connection> conns;
				if(categoryUid == null) {
					method = this.dao.getClass().getMethod(methodName.toString(), String.class, Sort.class);
					conns = (List<Connection>) method.invoke(this.dao, queryString, sort);
				}else {
					method = this.dao.getClass().getMethod(methodName.toString(), String.class, Sort.class, List.class);
					if(categoryUid.trim().isEmpty()) {
						List<String> conn_uids = relService.getAllConnectionUids();
						if(conn_uids.isEmpty()) {
							/*
							 * 當系統沒有建立任何conn和conn category之間的relation時, 則任意給一個空字串值
							 * 以免not in當中的值為empty, 導致搜尋不到任何不在conn category中的conn
							 */
							conn_uids.add("");
						}
						conns = (List<Connection>) method.invoke(this.dao, queryString, sort, conn_uids);
					}else {
						conns = (List<Connection>) method.invoke(this.dao, queryString, sort, relService.getConnectionUidsByCategoryUid(categoryUid));
					}
				}
				return ResponseEntity.ok(getExtraXmlProp(conns));
			}else {
				List<Connection> conns;
				if(categoryUid == null) {
					method = this.dao.getClass().getMethod(methodName.toString(), String.class);
					conns = (List<Connection>) method.invoke(this.dao, queryString);
				}else {
					method = this.dao.getClass().getMethod(methodName.toString(), String.class, List.class);
					if(categoryUid.trim().isEmpty()) {
						List<String> conn_uids = relService.getAllConnectionUids();
						if(conn_uids.isEmpty()) {
							/*
							 * 當系統沒有建立任何conn和conn category之間的relation時, 則任意給一個空字串值
							 * 以免not in當中的值為empty, 導致搜尋不到任何不在conn category中的conn
							 */
							conn_uids.add("");
						}
						conns = (List<Connection>) method.invoke(this.dao, queryString, conn_uids);
					}else {
						conns = (List<Connection>) method.invoke(this.dao, queryString, relService.getConnectionUidsByCategoryUid(categoryUid));
					}
				}
				return ResponseEntity.ok(getExtraXmlProp(conns));
			}
		}
	}
	
	public Connection add(HttpServletRequest request, String categoryUid, Map<String, String> connMap) throws IllegalArgumentException, Exception{
		if(null == connMap || connMap.size() <= 0)
			throw new IllegalArgumentException("Connection Information can not be empty!");
		
		String newUid = UUID.randomUUID().toString();
		
		String connectionname = connMap.get("connectionname");
		if(null == connectionname || connectionname.trim().length() <= 0)
			throw new IllegalArgumentException("Connection Name can not be empty!");
		connectionname = connectionname.toUpperCase();
		
		if(this.dao.existByName(connectionname))
			throw new IllegalArgumentException("Duplicate Connection Name!");
		
		String description = connMap.get("description");
		if(null == description)
			description = "";
		
		String connectiontype = connMap.get("connectiontype");
		if(null == connectiontype || connectiontype.trim().isEmpty() || !ConnectionService.CONNECTION_TYPE_SET.contains(connectiontype))
			throw new IllegalArgumentException("Illegal connection type! "+ ConnectionService.CONNECTION_TYPE_SET.toString());
		
		Connection conn = new Connection();
		conn.setConnectionuid(newUid);
		conn.setConnectionname(connectionname);
		conn.setConnectiontype(connectiontype);
		conn.setDescription(description);
		setExtraXmlProp(conn, connMap);
		conn.setLastupdatetime(new Date());
		
		Connection new_conn = this.dao.save(conn);
		
		new_conn = getExtraXmlProp(new_conn);
		
		//如果所附帶的url參數中有categoryUid的話, 表示是要把Connection新增至某個category
		if(null != categoryUid && !categoryUid.trim().isEmpty()) {
			ConnectionRelation rel = new ConnectionRelation();
			rel.setConncategoryuid(categoryUid);
			rel.setConnectionuid(new_conn.getConnectionuid());
			this.relService.add(rel);
		}
		
		//default permission insert
		this.modifyPermissionByObjectUid(new_conn.getConnectionuid(), request);
		
		return new_conn;
	}
	
	public Connection edit(String categoryUid, Map<String, String> connMap) throws IllegalArgumentException, Exception{
		String connectionuid = connMap.get("connectionuid");
		if(null == connectionuid || connectionuid.trim().length() <= 0)
			throw new IllegalArgumentException("Connection Uid can not be empty!");
		
		Connection old_conn = null;
		try {
			old_conn = this.dao.findById(connectionuid).get();
		}catch(NoSuchElementException e) {}
		
		if(null == old_conn)
			throw new IllegalArgumentException("Connection Uid does not exist!(" + connectionuid + ")");
		
		String connectionname = connMap.get("connectionname");
		if(null == connectionname || connectionname.trim().length() <= 0)
			throw new IllegalArgumentException("Connection Name can not be empty!");
		connectionname = connectionname.toUpperCase();
		
		if(this.dao.existByName(connectionname) && !old_conn.getConnectionname().equalsIgnoreCase(connectionname))
			throw new IllegalArgumentException("Duplicate Connection Name!");
		
		String description = connMap.get("description");
		if(null == description)
			description = "";
		
		String connectiontype = connMap.get("connectiontype");
		if(null == connectiontype || connectiontype.trim().isEmpty() || !ConnectionService.CONNECTION_TYPE_SET.contains(connectiontype))
			throw new IllegalArgumentException("Illegal connection type! "+ ConnectionService.CONNECTION_TYPE_SET.toString());
		
		Connection conn = new Connection();
		conn.setConnectionuid(connectionuid);
		conn.setConnectionname(connectionname);
		conn.setConnectiontype(connectiontype);
		conn.setDescription(description);
		setExtraXmlProp(conn, connMap);
		conn.setLastupdatetime(new Date());
		
		Connection new_conn = this.dao.save(conn);
		
		new_conn = getExtraXmlProp(new_conn);
		
		//如果所附帶的url參數中有categoryUid的話, 表示是要把Connection編輯至某個category或root
		if(categoryUid != null) {
			this.relService.deleteByConnectionUid(new_conn.getConnectionuid());
			if(!categoryUid.trim().equals("")) {	//如果categoryUid不是空值, 表示是要把Connection編輯到某一個category底下
				ConnectionRelation rel = new ConnectionRelation();
				rel.setConncategoryuid(categoryUid);
				rel.setConnectionuid(new_conn.getConnectionuid());
				this.relService.add(rel);
			}
		}
		
		return new_conn;
	}
	
	public void deleteByUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("Connection Uid can not be empty!");
		
		if(filesourceService.existByConnectionuid(uid))
			throw new IllegalArgumentException("Referenced By File Source!");
		
		if(jobstepService.existByConnectionuid(uid))
			throw new IllegalArgumentException("Referenced By JobStep!");
		
		if(objectAliasService.existByObjectuid(uid))
			throw new IllegalArgumentException("Referenceing by Object Alias");
		
		this.relService.deleteByConnectionUid(uid);
		
		this.permissionClient.deleteByObjectUid(uid);	//刪掉該connection所有的permission
		
		this.dao.deleteById(uid);
	}
	
	public String testJDBCConnection(String schema, JDBCConnection jdbcConn) throws SQLException, ClassNotFoundException, Exception {
		String result = "Test JDBC Connection Success";
		java.sql.Connection conn = null;
		
		try {
			if(null == schema || schema.trim().isEmpty())
				schema = "";
			
			String dbtype = jdbcConn.getJdbc_dbType();
			
			String driver = jdbcConn.getJdbc_driver();
			if(null == driver || driver.trim().isEmpty())
				throw new IllegalArgumentException("JDBC Driver can not be empty!");
			
			String pwd = jdbcConn.getJdbc_password();
			if(null == pwd || pwd.trim().isEmpty())
				throw new IllegalArgumentException("Password can not be empty!");
			
			String url = jdbcConn.getJdbc_url();
			if(null == url || url.trim().isEmpty())
				throw new IllegalArgumentException("URL can not be empty!");
			
			String user = jdbcConn.getJdbc_userid();
			if(null == user || user.trim().isEmpty())
				throw new IllegalArgumentException("User ID can not be empty!");
			
			if (!manager.hasLoad(dbtype)) {
				maintain.load(dbtype);
			}
		
			manager.forName(driver);
					
			if (dbtype == null) {
				conn = manager.getConnection(url, user, pwd);
			} else if (MetadataDriverManager.MSACCESS.equals(dbtype)){
				Properties prop = new Properties();
				prop.put("charSet", "big5");
				prop.put("user", user);
				prop.put("password", pwd);
				
				conn = manager.getConnection(url, prop);
			} else {
				conn = manager.getConnection(dbtype, url, user, pwd);
			}
			
			if(conn==null){
				throw new SQLException("Database Type Error");
			}else{
				ResultSet rs = conn.getMetaData().getSchemas();
				List<String> schemaList = new ArrayList<String>();
				while (rs.next()){
					schemaList.add(rs.getString(1));
				}
				
				if (schema.trim().length() != 0 && !schemaList.contains(schema)){
					throw new SQLException("Schema Not Exist");
				}
			}
		} catch (SQLException e) {
			ConnectionService.LOGGER.error("SQLException; reason was:", e);
			
			String errmsg = e.getMessage();	
			if (errmsg.indexOf("does not exist") > -1) {
				throw new SQLException("Target Database Does Not Exist");
			} else if (errmsg.indexOf("password authentication failed") > -1) {
				throw new SQLException("Invalid User Or Password");
			} else {
				throw new SQLException(errmsg);
			}
		} catch (ClassNotFoundException e) {
			ConnectionService.LOGGER.error("ClassNotFoundException; reason was:", e);
			throw e;
		} catch (Exception e){
			ConnectionService.LOGGER.error("Exception; reason was:", e);
			throw e;
		} finally {
			if (conn != null) {
				try { 
					conn.close();
				} catch (SQLException e) {
					ConnectionService.LOGGER.error("SQLException; reason was:", e);
				}
			}
		}
		
		return result;
	}
	
	public boolean existByUid(String uid) throws Exception {
		return this.dao.existsById(uid);
	}
	
	public Map<String, DriverInfo> getJDBCDriverInfo() throws Exception {
		return this.jdbcInfo.getInfo();
	}
	
	private PageRequest getPagingAndOrdering(Paging paging, Ordering ordering) throws Exception{	
		if(paging.getNumber() == null)
			paging.setNumber(0);
		
		if(paging.getSize() == null)
			paging.setSize(10);
		
		if(ordering != null) {
			return PageRequest.of(paging.getNumber(), paging.getSize(), getOrdering(ordering));
		}else {
			return PageRequest.of(paging.getNumber(), paging.getSize());
		}
	}
	
	private Sort getOrdering(Ordering ordering) throws Exception{
		Direction direct = Direction.fromString("DESC");
		if(ordering.getOrderType() != null && Constant.ORDER_TYPE_SET.contains(ordering.getOrderType().toUpperCase()))
			direct = Direction.fromString(ordering.getOrderType());
		
		if(ordering.getOrderField() != null && !ordering.getOrderField().isEmpty())
			return Sort.by(direct, ordering.getOrderField());
		else
			return Sort.by(direct, "lastupdatetime");
	}
	
	private List<Connection> getExtraXmlProp(List<Connection> conns) throws Exception{
		List<Connection> new_conns = new ArrayList<Connection>();
		for(Connection conn : conns) {
			new_conns.add(getExtraXmlProp(conn));
		}
		return new_conns;
	}
	
//	private ContactDto convertToContactDto(final Connection conn) {
//		JDBCConnection jdbc = new JDBCConnection();
//	    //get values from contact entity and set them in contactDto
//	    //e.g. contactDto.setContactId(contact.getContactId());
//	    return contactDto;
//	}
	
	private Connection getExtraXmlProp(Connection conn) throws Exception{
		String connectionuid = conn.getConnectionuid();
		String connectionname = conn.getConnectionname();
		String type = conn.getConnectiontype();
		String description = conn.getDescription();
		Date lastupdatetime = conn.getLastupdatetime();
		String xmldata = conn.getXmldata();
		Map<String, String> map = xmlUtil.parseXMLDataToHashMap(xmldata);
		if("J".equalsIgnoreCase(type)){
			JDBCConnection jdbc = new JDBCConnection();
			jdbc.setConnectionuid(connectionuid);
			jdbc.setConnectionname(connectionname);
			jdbc.setDescription(description);
			jdbc.setConnectiontype(type);
			jdbc.setWithpim(map.get("withpim"));
			jdbc.setPimendpointtype(map.get("pimendpointtype"));
			jdbc.setPimendpointname(map.get("pimendpointname"));
			jdbc.setPimaccountcontainer(map.get("pimaccountcontainer"));
			jdbc.setPimaccountname(map.get("pimaccountname"));
			jdbc.setLastupdatetime(lastupdatetime);
			jdbc.setJdbc_dbType(map.get("jdbc_dbType"));
			jdbc.setJdbc_driver(map.get("jdbc_driver"));
			jdbc.setJdbc_password(Crypto.getDecryptString(map.get("jdbc_password"), encryptKey));
			jdbc.setJdbc_url(map.get("jdbc_url"));
			jdbc.setJdbc_userid(map.get("jdbc_userid"));
			jdbc.setXmldata("");	//不再需要xml欄位的資料, 已經parsing
			return jdbc;
		}else if("D".equalsIgnoreCase(type)){
			DatabaseConnection database = new DatabaseConnection();
			database.setConnectionuid(connectionuid);
			database.setConnectionname(connectionname);
			database.setDescription(description);
			database.setConnectiontype(type);
			database.setWithpim(map.get("withpim"));
			database.setPimendpointtype(map.get("pimendpointtype"));
			database.setPimendpointname(map.get("pimendpointname"));
			database.setPimaccountcontainer(map.get("pimaccountcontainer"));
			database.setPimaccountname(map.get("pimaccountname"));
			database.setLastupdatetime(lastupdatetime);
			database.setUserid(map.get("userid"));
			database.setServer(map.get("server"));
			database.setPassword(Crypto.getDecryptString(map.get("password"), encryptKey));
			database.setXmldata("");	//不再需要xml欄位的資料, 已經parsing
			return database;
		}else if("F".equalsIgnoreCase(type)){
			FTPConnection ftp = new FTPConnection();
			ftp.setConnectionuid(connectionuid);
			ftp.setConnectionname(connectionname);
			ftp.setDescription(description);
			ftp.setConnectiontype(type);
			ftp.setWithpim(map.get("withpim"));
			ftp.setPimendpointtype(map.get("pimendpointtype"));
			ftp.setPimendpointname(map.get("pimendpointname"));
			ftp.setPimaccountcontainer(map.get("pimaccountcontainer"));
			ftp.setPimaccountname(map.get("pimaccountname"));
			ftp.setLastupdatetime(lastupdatetime);
			ftp.setUserid(map.get("userid"));
			ftp.setTargetdir(map.get("targetdir"));
			ftp.setServer(map.get("server"));
			ftp.setPassword(Crypto.getDecryptString(map.get("password"), encryptKey));
			ftp.setXmldata("");	//不再需要xml欄位的資料, 已經parsing
			return ftp;
		}else if("M".equalsIgnoreCase(type)){
			MailConnection mail = new MailConnection();
			mail.setConnectionuid(connectionuid);
			mail.setConnectionname(connectionname);
			mail.setDescription(description);
			mail.setConnectiontype(type);
			mail.setWithpim(map.get("withpim"));
			mail.setPimendpointtype(map.get("pimendpointtype"));
			mail.setPimendpointname(map.get("pimendpointname"));
			mail.setPimaccountcontainer(map.get("pimaccountcontainer"));
			mail.setPimaccountname(map.get("pimaccountname"));
			mail.setLastupdatetime(lastupdatetime);
			mail.setUser(map.get("user"));
			mail.setHost(map.get("host"));
			try {
				mail.setPort(Integer.valueOf(map.get("port")));
			}catch(Exception e) {}
			mail.setPassword(Crypto.getDecryptString(map.get("password"), encryptKey));
			mail.setMailssl(map.get("mailssl"));
			mail.setMailtls(map.get("mailtls"));
			mail.setXmldata("");	//不再需要xml欄位的資料, 已經parsing
			return mail;
		}else if("N".equalsIgnoreCase(type)){
			NotesConnection notes = new NotesConnection();
			notes.setConnectionuid(connectionuid);
			notes.setConnectionname(connectionname);
			notes.setDescription(description);
			notes.setConnectiontype(type);
			notes.setWithpim(map.get("withpim"));
			notes.setPimendpointtype(map.get("pimendpointtype"));
			notes.setPimendpointname(map.get("pimendpointname"));
			notes.setPimaccountcontainer(map.get("pimaccountcontainer"));
			notes.setPimaccountname(map.get("pimaccountname"));
			notes.setLastupdatetime(lastupdatetime);
			notes.setNotesHostIP(map.get("notesHostIP"));
			notes.setNotesIor(map.get("notesIor"));
			notes.setUserid(map.get("userid"));
			notes.setPassword(Crypto.getDecryptString(map.get("password"), encryptKey));
			notes.setNotesDBName(map.get("notesDBName"));
			notes.setNotesServerName(map.get("notesServerName"));
			notes.setXmldata("");	//不再需要xml欄位的資料, 已經parsing
			return notes;
		}else if("O".equalsIgnoreCase(type)){
			OSConnection os = new OSConnection();
			os.setConnectionuid(connectionuid);
			os.setConnectionname(connectionname);
			os.setDescription(description);
			os.setConnectiontype(type);
			os.setWithpim(map.get("withpim"));
			os.setPimendpointtype(map.get("pimendpointtype"));
			os.setPimendpointname(map.get("pimendpointname"));
			os.setPimaccountcontainer(map.get("pimaccountcontainer"));
			os.setPimaccountname(map.get("pimaccountname"));
			os.setLastupdatetime(lastupdatetime);
			os.setPort(map.get("port"));
			os.setUserid(map.get("userid"));
			os.setPassword(Crypto.getDecryptString(map.get("password"), encryptKey));
			os.setXmldata("");	//不再需要xml欄位的資料, 已經parsing
			return os;
		}else if("S".equalsIgnoreCase(type)){
			SapConnection sap = new SapConnection();
			sap.setConnectionuid(connectionuid);
			sap.setConnectionname(connectionname);
			sap.setDescription(description);
			sap.setConnectiontype(type);
			sap.setWithpim(map.get("withpim"));
			sap.setPimendpointtype(map.get("pimendpointtype"));
			sap.setPimendpointname(map.get("pimendpointname"));
			sap.setPimaccountcontainer(map.get("pimaccountcontainer"));
			sap.setPimaccountname(map.get("pimaccountname"));
			sap.setLastupdatetime(lastupdatetime);
			sap.setSAPLANGUAGE(map.get("SAPLANGUAGE"));
			sap.setSapSystemName(map.get("sapSystemName"));
			sap.setSapSystemNumber(map.get("sapSystemNumber"));
			sap.setUserid(map.get("userid"));
			sap.setSapHostIP(map.get("sapHostIP"));
			sap.setPassword(Crypto.getDecryptString(map.get("password"), encryptKey));
			sap.setSapCodePage(map.get("sapCodePage"));
			sap.setSapClient(map.get("sapClient"));
			sap.setXmldata("");	//不再需要xml欄位的資料, 已經parsing
			return sap;
		}else{
			throw new Exception("No Such Connection Type = " + type);
		}
	}
	
	private void setExtraXmlProp(Connection conn, Map<String, String> connMap) throws IllegalArgumentException, Exception{
		Map<String, String> map = new HashMap<String, String>();
		
		String withpim = connMap.get("withpim");
		if(null == withpim || withpim.trim().isEmpty())
			withpim = "0";
		
		String pimendpointname = connMap.get("pimendpointname");
		if(null == pimendpointname)
			pimendpointname = "";
		
		String pimaccountcontainer = connMap.get("pimaccountcontainer");
		if(null == pimaccountcontainer)
			pimaccountcontainer = "";
		
		String pimendpointtype = connMap.get("pimendpointtype");
		if(null == pimendpointtype)
			pimendpointtype = "";
		
		String pimaccountname = connMap.get("pimaccountname");
		if(null == pimaccountname)
			pimaccountname = "";
		
		map.put("withpim", withpim);
		map.put("pimendpointname", pimendpointname);
		map.put("pimaccountcontainer", pimaccountcontainer);
		map.put("pimendpointtype", pimendpointtype);
		map.put("pimaccountname", pimaccountname);
		
		if(conn.getConnectiontype().equals("J")){
			String jdbc_dbType = connMap.get("jdbc_dbType");
			if(null == jdbc_dbType || jdbc_dbType.trim().isEmpty())
				throw new IllegalArgumentException("JDBC Database Type can not be empty!");
			
			String jdbc_driver = connMap.get("jdbc_driver");
			if(null == jdbc_driver || jdbc_driver.trim().isEmpty())
				throw new IllegalArgumentException("JDBC Driver can not be empty!");
			
			String jdbc_password = connMap.get("jdbc_password");
			if(null == jdbc_password || jdbc_password.trim().isEmpty())
				throw new IllegalArgumentException("JDBC Password can not be empty!");
			
			String jdbc_url = connMap.get("jdbc_url");
			if(null == jdbc_url || jdbc_url.trim().isEmpty())
				throw new IllegalArgumentException("JDBC URL can not be empty!");
			
			String jdbc_userid = connMap.get("jdbc_userid");
			if(null == jdbc_userid || jdbc_userid.trim().isEmpty())
				throw new IllegalArgumentException("JDBC User ID can not be empty!");
			
			map.put("jdbc_dbType", jdbc_dbType);
			map.put("jdbc_driver", jdbc_driver);
			map.put("jdbc_password", Crypto.getEncryptString(jdbc_password, encryptKey));
			map.put("jdbc_url", jdbc_url);
			map.put("jdbc_userid", jdbc_userid);
		}else if(conn.getConnectiontype().equals("D")){
			String userid = connMap.get("userid");
			if(null == userid || userid.trim().isEmpty())
				throw new IllegalArgumentException("Database User ID can not be empty!");
			
			String server = connMap.get("server");
			if(null == server || server.trim().isEmpty())
				throw new IllegalArgumentException("Database Server can not be empty!");
			
			String password = connMap.get("password");
			if(null == password || password.trim().isEmpty())
				throw new IllegalArgumentException("Database Password can not be empty!");
			
			map.put("userid", userid);
			map.put("server", server);
			map.put("password", Crypto.getEncryptString(password, encryptKey));
		}else if(conn.getConnectiontype().equals("F")){
			String userid = connMap.get("userid");
			if(null == userid || userid.trim().isEmpty())
				throw new IllegalArgumentException("FTP User ID can not be empty!");
			
			String server = connMap.get("server");
			if(null == server || server.trim().isEmpty())
				throw new IllegalArgumentException("FTP Server can not be empty!");
			
			String password = connMap.get("password");
			if(null == password || password.trim().isEmpty())
				throw new IllegalArgumentException("FTP Password can not be empty!");
			
			String targetdir = connMap.get("targetdir");
			if(null == targetdir)
				targetdir = "";
			
			map.put("userid", userid);
			map.put("targetdir", targetdir);
			map.put("server", server);
			map.put("password", Crypto.getEncryptString(password, encryptKey));
		}else if(conn.getConnectiontype().equals("M")){
			String user = connMap.get("user");
			if(null == user || user.trim().isEmpty())
				throw new IllegalArgumentException("Mail User can not be empty!");
			
			String password = connMap.get("password");
			if(null == password || password.trim().isEmpty())
				throw new IllegalArgumentException("Mail Password can not be empty!");
			
			String host = connMap.get("host");
			if(null == host || host.trim().isEmpty())
				host = "";
			
			String port = connMap.get("port");
			if(null == port)
				port = "";
			
			String mailssl = connMap.get("mailssl");
			if(null == mailssl)
				mailssl = "0";
			
			String mailtls = connMap.get("mailtls");
			if(null == mailtls)
				mailtls = "0";
			
			map.put("mailssl", mailssl);
			map.put("password", Crypto.getEncryptString(password, encryptKey));
			map.put("mailtls", mailtls);
			map.put("port", port);
			map.put("host", host);
			map.put("user", user);
		}else if(conn.getConnectiontype().equals("N")){
			String notesIor = connMap.get("notesIor");
			if(null == notesIor)
				notesIor = "";
			
			String notesHostIP = connMap.get("notesHostIP");
			if(null == notesHostIP || notesHostIP.trim().isEmpty())
				throw new IllegalArgumentException("Notes Host IP can not be empty!");
			
			String notesServerName = connMap.get("notesServerName");
			if(null == notesServerName)
				notesServerName = "";
			
			String password = connMap.get("password");
			if(null == password || password.trim().isEmpty())
				throw new IllegalArgumentException("Notes Password can not be empty!");
			
			String notesDBName = connMap.get("notesDBName");
			if(null == notesDBName || notesDBName.trim().isEmpty())
				throw new IllegalArgumentException("Notes Database Name can not be empty!");
			
			String userid = connMap.get("userid");
			if(null == userid || userid.trim().isEmpty())
				throw new IllegalArgumentException("Notes User ID can not be empty!");
			
			map.put("notesIor", notesIor);
			map.put("notesHostIP", notesHostIP);
			map.put("notesServerName", notesServerName);
			map.put("password", Crypto.getEncryptString(password, encryptKey));
			map.put("notesDBName", notesDBName);
			map.put("userid", userid);
		}else if(conn.getConnectiontype().equals("O")){
			String password = connMap.get("password");
			if(null == password || password.trim().isEmpty())
				throw new IllegalArgumentException("OS Password can not be empty!");
			
			String userid = connMap.get("userid");
			if(null == userid || userid.trim().isEmpty())
				throw new IllegalArgumentException("OS User ID can not be empty!");
			
			map.put("userid", userid);
			map.put("port", "");
			map.put("password", Crypto.getEncryptString(password, encryptKey));
		}else if(conn.getConnectiontype().equals("S")){
			String sapCodePage = connMap.get("sapCodePage");
			if(null == sapCodePage)
				sapCodePage = "";
			
			String sapSystemNumber = connMap.get("sapSystemNumber");
			if(null == sapSystemNumber || sapSystemNumber.trim().isEmpty())
				throw new IllegalArgumentException("Sap System Number can not be empty!");
			
			String sapSystemName = connMap.get("sapSystemName");
			if(null == sapSystemName || sapSystemName.trim().isEmpty())
				throw new IllegalArgumentException("Sap System Name can not be empty!");
			
			String sapHostIP = connMap.get("sapHostIP");
			if(null == sapHostIP || sapHostIP.trim().isEmpty())
				throw new IllegalArgumentException("Sap Host IP can not be empty!");
			
			String sapClient = connMap.get("sapClient");
			if(null == sapClient || sapClient.trim().isEmpty())
				throw new IllegalArgumentException("Sap Client can not be empty!");
			
			String userid = connMap.get("userid");
			if(null == userid || userid.trim().isEmpty())
				throw new IllegalArgumentException("OS User ID can not be empty!");
			
			String password = connMap.get("password");
			if(null == password || password.trim().isEmpty())
				throw new IllegalArgumentException("Notes Password can not be empty!");
			
			String SAPLANGUAGE = connMap.get("saplanguage");
			if(null == SAPLANGUAGE)
				SAPLANGUAGE = "";
			
			map.put("sapCodePage", sapCodePage);
			map.put("sapSystemNumber", sapSystemNumber);
			map.put("sapSystemName", sapSystemName);
			map.put("sapHostIP", sapHostIP);
			map.put("sapClient", sapClient);
			map.put("userid", userid);
			map.put("password", Crypto.getEncryptString(password, encryptKey));
			map.put("SAPLANGUAGE", SAPLANGUAGE);
		}
		
		conn.setXmldata(xmlUtil.parseHashMapToXMLString(map, false));
	}
	
	private void modifyPermissionByObjectUid(String objectUid, HttpServletRequest request) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			String peopleUid = userService.getByID(peopleId).getUseruid();
			if(null == peopleUid || peopleUid.trim().isEmpty() || "trinity".equals(peopleUid.trim()))
				return;
			
			List<AccessRight> accessRights = new ArrayList<AccessRight>();
			AccessRight accessRight = new AccessRight();
			accessRight.setPeopleuid(peopleUid);
			accessRight.setObjectuid(objectUid);
			accessRight.setView("1");
			accessRight.setAdd("1");
			accessRight.setEdit("1");
			accessRight.setDelete("1");
			accessRight.setGrant("1");
			accessRight.setImport_export("0");
			accessRight.setReRun("0");
			accessRight.setRun("0");
			
			accessRights.add(accessRight);
			
			this.permissionClient.modifyByObjectUid(objectUid, accessRights);
		} catch (Exception e) {
			ConnectionService.LOGGER.error("Exception; reason was:", e);
		}
	}
}
