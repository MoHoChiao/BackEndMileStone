package com.netpro.trinity.repository.connection.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.netpro.trinity.repository.connection.dao.jpa.ConnectionJPADao;
import com.netpro.trinity.repository.connection.entity.DatabaseConnection;
import com.netpro.trinity.repository.connection.entity.FTPConnection;
import com.netpro.trinity.repository.connection.entity.JDBCConnection;
import com.netpro.trinity.repository.connection.entity.MailConnection;
import com.netpro.trinity.repository.connection.entity.NotesConnection;
import com.netpro.trinity.repository.connection.entity.OSConnection;
import com.netpro.trinity.repository.connection.entity.SapConnection;
import com.netpro.trinity.repository.connection.entity.jpa.Connection;
import com.netpro.trinity.repository.dto.FilterInfo;
import com.netpro.trinity.repository.dto.Ordering;
import com.netpro.trinity.repository.dto.Paging;
import com.netpro.trinity.repository.dto.Querying;
import com.netpro.trinity.repository.filesource.entity.jdbc.FilesourceRelation;
import com.netpro.trinity.repository.filesource.entity.jpa.FileSource;
import com.netpro.trinity.repository.filesource.service.FileSourceService;
import com.netpro.trinity.repository.filesource.service.FilesourceRelationService;
import com.netpro.trinity.repository.jobstep.service.JobstepService;
import com.netpro.trinity.repository.prop.TrinityDataJDBC;
import com.netpro.trinity.repository.prop.TrinityDataJDBC.JDBCDriverInfo;
import com.netpro.trinity.repository.util.Constant;
import com.netpro.trinity.repository.util.Crypto;
import com.netpro.trinity.repository.util.XMLDataUtility;

@Service
public class ConnectionService {
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
	private XMLDataUtility xmlUtil;
	
	@Autowired
	private ConnectionJPADao dao;
	
	@Autowired
	private ConnectionRelationService relService;
	@Autowired
	private FileSourceService filesourceService;
	@Autowired
	private JobstepService jobstepService;
	
	@Autowired
	private TrinityDataJDBC jdbcInfo;
	
	@Value("${encrypt.key}")
	private String encryptKey;
	
	public List<Connection> getAll() throws Exception{
		List<Connection> conns = this.dao.findAll();
		return getExtraXmlProp(conns);
	}
	
	public List<Connection> getAllWithoutInCategory() throws Exception{
		List<Connection> conns = this.dao.findByConnectionuidNotIn(relService.getAllConnectionUids(), new Sort(new Order("connectionname")));
		return getExtraXmlProp(conns);
	}
	
	public Connection getByUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.trim().isEmpty())
			throw new IllegalArgumentException("Connection UID can not be empty!");
		
		Connection conn = this.dao.findOne(uid);
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
		
		List<Connection> conns = this.dao.findByConnectionuidIn(relService.getConnectionUidsByCategoryUid(uid), new Sort(new Order("connectionname")));
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
					conns = this.dao.findByConnectionuidNotIn(relService.getAllConnectionUids());
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
					conns = this.dao.findByConnectionuidNotIn(relService.getAllConnectionUids());
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
						page_conn = this.dao.findByConnectionuidNotIn(relService.getAllConnectionUids(), pageRequest);
					}else {
						page_conn = this.dao.findByConnectionuidIn(relService.getConnectionUidsByCategoryUid(categoryUid), pageRequest);
					}
				}
				return ResponseEntity.ok(getExtraXmlProp(page_conn.getContent()));
			}else if(sort != null) {
				List<Connection> conns;
				if(categoryUid == null) {
					conns = this.dao.findAll(sort);
				}else {
					if(categoryUid.trim().isEmpty()) {
						conns = this.dao.findByConnectionuidNotIn(relService.getAllConnectionUids(), sort);
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
						conns = this.dao.findByConnectionuidNotIn(relService.getAllConnectionUids());
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
						page_conn = (Page<Connection>) method.invoke(this.dao, queryString, pageRequest, relService.getAllConnectionUids());
					}else {
						page_conn = (Page<Connection>) method.invoke(this.dao, queryString, pageRequest, relService.getConnectionUidsByCategoryUid(categoryUid));
					}
				}
				return ResponseEntity.ok(getExtraXmlProp(page_conn.getContent()));
			}else if(sort != null) {
				List<Connection> conns;
				if(categoryUid == null) {
					method = this.dao.getClass().getMethod(methodName.toString(), String.class, Sort.class);
					conns = (List<Connection>) method.invoke(this.dao, queryString, sort);
				}else {
					method = this.dao.getClass().getMethod(methodName.toString(), String.class, Sort.class, List.class);
					if(categoryUid.trim().isEmpty()) {
						conns = (List<Connection>) method.invoke(this.dao, queryString, sort, relService.getAllConnectionUids());
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
						conns = (List<Connection>) method.invoke(this.dao, queryString, relService.getAllConnectionUids());
					}else {
						conns = (List<Connection>) method.invoke(this.dao, queryString, relService.getConnectionUidsByCategoryUid(categoryUid));
					}
				}
				return ResponseEntity.ok(getExtraXmlProp(conns));
			}
		}
	}
	
	public Connection add(String categoryUid, Connection conn) throws IllegalArgumentException, Exception{
//		String newUid = UUID.randomUUID().toString();
//		
//		String filesourcename = filesource.getFilesourcename();
//		if(null == filesourcename || filesourcename.length() <= 0)
//			throw new IllegalArgumentException("File Source Name can not be empty!");
//		filesourcename = filesourcename.toUpperCase();
//		
//		if(this.dao.existByName(filesourcename))
//			throw new IllegalArgumentException("Duplicate File Source Name!");
//		
//		String description = filesource.getDescription();
//		if(null == description)
//			description = "";
//		
//		String root_dir = filesource.getRootdir();
//		if(null == root_dir)
//			root_dir = "";
//		
//		String receive_dir = filesource.getReceivedir();
//		if(null == receive_dir || receive_dir.length() <= 0)
//			throw new IllegalArgumentException("Receive Directory can not be empty!");
//		
//		String target_dir = filesource.getTargetdir();
//		if(null == target_dir || target_dir.length() <= 0)
//			throw new IllegalArgumentException("Target Directory can not be empty!");
//		
//		String complete_dir = filesource.getCompletedir();
//		if(null == complete_dir || complete_dir.length() <= 0)
//			throw new IllegalArgumentException("Complete Directory can not be empty!");
//		
//		String corrupt_dir = filesource.getCorruptdir();
//		if(null == corrupt_dir || corrupt_dir.length() <= 0)
//			throw new IllegalArgumentException("Corrupt Directory can not be empty!");
//		
//		String duplicate_dir = filesource.getDuplicatedir();
//		if(null == duplicate_dir || duplicate_dir.length() <= 0)
//			throw new IllegalArgumentException("Duplicate Directory can not be empty!");
//		
//		String error_dir = filesource.getErrordir();
//		if(null == error_dir || error_dir.length() <= 0)
//			throw new IllegalArgumentException("Error Directory can not be empty!");
//		
//		String filename = filesource.getFilename();
//		if(null == filename || filename.length() <= 0)
//			throw new IllegalArgumentException("File Name Pattern can not be empty!");
//		
//		String pattern = filesource.getPattern();
//		if(null == pattern || (!pattern.equals("1") && !pattern.equals("2") && !pattern.equals("3") && !pattern.equals("4")))
//			pattern = "1";
//		
//		Integer start_position = filesource.getStartposition();
//		if(null == start_position || start_position < 0)
//			start_position = 0;
//		
//		Integer end_position = filesource.getEndposition();
//		if(null == end_position || end_position < 0)
//			end_position = 0;
//		
//		if(pattern.equals("3")) {
//			if(end_position <= start_position)
//				throw new IllegalArgumentException("End Position can not be equals or smaller than Start Position!");
//		}
//		
//		String filetype = filesource.getFiletype();
//		if(null == filetype || (!filetype.equals("D") && !filetype.equals("C")))
//			filetype = "D";
//		
//		String datafilecountmode = filesource.getDatafilecountmode();
//		if(null == datafilecountmode || (!datafilecountmode.equals("R") && !datafilecountmode.equals("C")))
//			datafilecountmode = "R";
//		
//		String cfImpClass = filesource.getCfImpClass();
//		if(null == cfImpClass)
//			cfImpClass = "com.netpro.filesource.ctrl.MatchFileSizeCtrlFileHandler";
//		
//		//如果是選擇Regular Data時, 則cfImpClass及datafilecountmode一律代回預設值
//		if(filetype.equals("D")){
//			datafilecountmode = "R";
//            cfImpClass = "com.netpro.filesource.ctrl.MatchFileSizeCtrlFileHandler";
//        }
//		
//		String check_duplicate = filesource.getCheckduplicate();
//		if(null == check_duplicate || (!check_duplicate.equals("1") && !check_duplicate.equals("0")))
//			check_duplicate = "0";
//		
//		String filter_duplicate = filesource.getFilterduplicate();
//		if(null == filter_duplicate || (!filter_duplicate.equals("1") && !filter_duplicate.equals("0")) || check_duplicate.equals("0"))
//			filter_duplicate = "0";
//		
//		String filetrigger = filesource.getFiletrigger();
//		if(null == filetrigger || (!filetrigger.equals("1") && !filetrigger.equals("0")))
//			filetrigger = "0";
//		
//		String trigger_job_uid = filesource.getTriggerjobuid();
//		if(null == trigger_job_uid || trigger_job_uid.trim().length() <= 0){
//			if(filetrigger.equals("1")){
//				throw new IllegalArgumentException("When Choose 'File Trigger', must be select one job uid!");
//			}else{
//				trigger_job_uid = "";
//			}
//		}
//		
//		String txdate__format = filesource.getTxdateformat();
//		if(null == txdate__format)
//			txdate__format = "";
//		
//		Integer txdate_start_pos = filesource.getTxdatestartpos();
//		if(null == txdate_start_pos || txdate_start_pos < 0)
//			txdate_start_pos = 0;
//		
//		Integer txdate_end_pos = filesource.getTxdateendpos();
//		if(null == txdate_end_pos || txdate_end_pos < 0)
//			txdate_end_pos = 0;
//		
//		//當沒有勾選filetrigger時, 要強制讓txdateformat='',txdatestartpos=0,txdateendpos=0
//        if(filetrigger.equals("0")){
//        	txdate__format = "";
//        	txdate_start_pos = 0;
//        	txdate_end_pos = 0;
//        }
//		
//		String checksum = filesource.getChecksum();
//		if(null == checksum || (!checksum.equals("1") && !checksum.equals("0")))
//			checksum = "0";
//		
//		String checksum_fe = filesource.getChecksumfe();
//		if(null == checksum_fe || checksum_fe.trim().length() <= 0)
//			checksum_fe = ".checksum";
//		
//		String checksum_alg = filesource.getChecksumalg();
//		if(null == checksum_alg || (!checksum_alg.equals("M") && !checksum_alg.equals("S")))
//			checksum_alg = "M";
//		
//		//如果不勾選check sum時, 則checksumalg及checksumfe一律代回預設值
//		if(checksum.equals("0")) {
//			checksum_fe = ".checksum";
//			checksum_alg = "M";
//		}
//			
//		Integer min_file = filesource.getMinfile();
//		if(null == min_file || min_file < 0)
//			min_file = 1;
//		
//		Integer max_file = filesource.getMaxfile();
//		if(null == max_file || max_file < 0)
//			max_file = 5;
//		
//		Integer timeout = filesource.getTimeout();
//		if(null == timeout || timeout < 0)
//			timeout = 3;
//		
//		String bypass_zero = filesource.getBypasszero();
//		if(null == bypass_zero || (!bypass_zero.equals("1") && !bypass_zero.equals("0")))
//			bypass_zero = "0";
//		
//		String appendUid = filesource.getAppendUid();
//		if(null == appendUid || (!appendUid.equals("1") && !appendUid.equals("0")))
//			appendUid = "0";
//		
//		String ftpget = filesource.getFtpget();
//		if(null == ftpget || (!ftpget.equals("1") && !ftpget.equals("0")))
//			ftpget = "0";
//		
//		String sftp = filesource.getSftp();
//		if(null == sftp || (!sftp.equals("1") && !sftp.equals("0")))
//			sftp = "0";
//		
//		String ftp_binary = filesource.getFtpbinary();
//		if(null == ftp_binary || (!ftp_binary.equals("1") && !ftp_binary.equals("0")))
//			ftp_binary = "0";
//		
//		String passive = filesource.getPassive();
//		if(null == passive || (!passive.equals("1") && !passive.equals("0")))
//			passive = "0";
//		
//		String ftp_connection_uid = filesource.getFtpconnectionuid();
//		if(null == ftp_connection_uid || ftp_connection_uid.length() <= 0){
//			if(ftpget.equals("1")){
//				throw new Exception("When Choose 'FTP Get', must be select one FTP Connection uid!");
//			}else{
//				ftp_connection_uid = "";
//			}
//		}
//		
//		String ftp_remote_dir = filesource.getFtpremotedir();
//		if(null == ftp_remote_dir || ftp_remote_dir.length() <= 0){
//			if(ftpget.equals("1")){
//				throw new Exception("When Choose 'FTP Get', must be have FTP Remote Directory!");
//			}else{
//				ftp_remote_dir = "";
//			}
//		}
//		
//		String ftp_post_action = filesource.getFtppostaction();
//		if(null == ftp_post_action || (!ftp_post_action.equals("0") && !ftp_post_action.equals("1") && !ftp_post_action.equals("2")))
//			ftp_post_action = "0";
//			
//		String ftp_move_dir = filesource.getFtpmovedir();
//		if(null == ftp_move_dir || ftp_move_dir.length() <= 0){
//			if(ftp_post_action.equals("1")){
//				throw new Exception("When 'ftp_post_action=1', must be have FTP Move Directory!");
//			}else{
//				ftp_move_dir = "";
//			}
//		}
//		
//		String checkrow = "0";
//		
//		filesource.setFilesourceuid(newUid);
//		filesource.setFilesourcename(filesourcename);
//		filesource.setDescription(description);
//		filesource.setFiletrigger(filetrigger);
//		filesource.setReceivedir(receive_dir);
//		filesource.setTargetdir(target_dir);
//		filesource.setCompletedir(complete_dir);
//		filesource.setCorruptdir(corrupt_dir);
//		filesource.setDuplicatedir(duplicate_dir);
//		filesource.setErrordir(error_dir);
//		filesource.setFilename(filename);
//		filesource.setPattern(pattern);
//		filesource.setStartposition(start_position);
//		filesource.setEndposition(end_position);
//		filesource.setFiletype(filetype);
//		filesource.setMinfile(min_file);
//		filesource.setMaxfile(max_file);
//		filesource.setTimeout(timeout);
//		filesource.setCheckduplicate(check_duplicate);
//		filesource.setFilterduplicate(filter_duplicate);
//		filesource.setCheckrow(checkrow);
//		filesource.setBypasszero(bypass_zero);
//		filesource.setFtpget(ftpget);
//		
//		setExtraXmlPropToString(filesource);
//		
//		/*
//		 * because lastupdatetime column is auto created value, it can not be reload new value.
//		 * here, we force to give value to lastupdatetime column.
//		 */
//		filesource.setLastupdatetime(new Date());
//		
//		FileSource new_filesource = this.dao.save(filesource);
//		/*
//		 * Because All fields associated with xml are defined by @Transient, it can not be reload new value.
//		 * The fields associated with xml is very suck design!
//		 */
//		setExtraXmlProp(new_filesource);
//		
//		//如果所附帶的url參數中有categoryUid的話, 表示是要把file source新增至某個category
//		if(categoryUid != null && !categoryUid.trim().equals("")) {
//			FilesourceRelation rel = new FilesourceRelation();
//			rel.setFscategoryuid(categoryUid);
//			rel.setFilesourceuid(new_filesource.getFilesourceuid());
//			this.relService.add(rel);
//		}
		
		return this.dao.save(conn);
	}
	
	public Connection edit(String categoryUid, Connection conn) throws IllegalArgumentException, Exception{
//		String filesourceuid = filesource.getFilesourceuid();
//		if(null == filesourceuid || filesourceuid.trim().length() <= 0)
//			throw new IllegalArgumentException("File Source Uid can not be empty!");
//		
//		FileSource old_filesource = this.dao.findOne(filesourceuid);
//		if(null == old_filesource)
//			throw new IllegalArgumentException("File Source Uid does not exist!(" + filesourceuid + ")");
//		
//		String filesourcename = filesource.getFilesourcename();
//		if(null == filesourcename || filesourcename.length() <= 0)
//			throw new IllegalArgumentException("File Source Name can not be empty!");
//		filesourcename = filesourcename.toUpperCase();
//		
//		if(this.dao.existByName(filesourcename) && !old_filesource.getFilesourcename().equalsIgnoreCase(filesourcename))
//			throw new IllegalArgumentException("Duplicate File Source Name!");
//		
//		String description = filesource.getDescription();
//		if(null == description)
//			description = "";
//		
//		String root_dir = filesource.getRootdir();
//		if(null == root_dir)
//			root_dir = "";
//		
//		String receive_dir = filesource.getReceivedir();
//		if(null == receive_dir || receive_dir.length() <= 0)
//			throw new IllegalArgumentException("Receive Directory can not be empty!");
//		
//		String target_dir = filesource.getTargetdir();
//		if(null == target_dir || target_dir.length() <= 0)
//			throw new IllegalArgumentException("Target Directory can not be empty!");
//		
//		String complete_dir = filesource.getCompletedir();
//		if(null == complete_dir || complete_dir.length() <= 0)
//			throw new IllegalArgumentException("Complete Directory can not be empty!");
//		
//		String corrupt_dir = filesource.getCorruptdir();
//		if(null == corrupt_dir || corrupt_dir.length() <= 0)
//			throw new IllegalArgumentException("Corrupt Directory can not be empty!");
//		
//		String duplicate_dir = filesource.getDuplicatedir();
//		if(null == duplicate_dir || duplicate_dir.length() <= 0)
//			throw new IllegalArgumentException("Duplicate Directory can not be empty!");
//		
//		String error_dir = filesource.getErrordir();
//		if(null == error_dir || error_dir.length() <= 0)
//			throw new IllegalArgumentException("Error Directory can not be empty!");
//		
//		String filename = filesource.getFilename();
//		if(null == filename || filename.length() <= 0)
//			throw new IllegalArgumentException("File Name Pattern can not be empty!");
//		
//		String pattern = filesource.getPattern();
//		if(null == pattern || (!pattern.equals("1") && !pattern.equals("2") && !pattern.equals("3") && !pattern.equals("4")))
//			pattern = "1";
//		
//		Integer start_position = filesource.getStartposition();
//		if(null == start_position || start_position < 0)
//			start_position = 0;
//		
//		Integer end_position = filesource.getEndposition();
//		if(null == end_position || end_position < 0)
//			end_position = 0;
//		
//		if(pattern.equals("3")) {
//			if(end_position <= start_position)
//				throw new IllegalArgumentException("End Position can not be equals or smaller than Start Position!");
//		}
//		
//		String filetype = filesource.getFiletype();
//		if(null == filetype || (!filetype.equals("D") && !filetype.equals("C")))
//			filetype = "D";
//		
//		String datafilecountmode = filesource.getDatafilecountmode();
//		if(null == datafilecountmode || (!datafilecountmode.equals("R") && !datafilecountmode.equals("C")))
//			datafilecountmode = "R";
//		
//		String cfImpClass = filesource.getCfImpClass();
//		if(null == cfImpClass)
//			cfImpClass = "com.netpro.filesource.ctrl.MatchFileSizeCtrlFileHandler";
//		
//		//如果是選擇Regular Data時, 則cfImpClass及datafilecountmode一律代回預設值
//		if(filetype.equals("D")){
//			datafilecountmode = "R";
//            cfImpClass = "com.netpro.filesource.ctrl.MatchFileSizeCtrlFileHandler";
//        }
//		
//		String check_duplicate = filesource.getCheckduplicate();
//		if(null == check_duplicate || (!check_duplicate.equals("1") && !check_duplicate.equals("0")))
//			check_duplicate = "0";
//		
//		String filter_duplicate = filesource.getFilterduplicate();
//		if(null == filter_duplicate || (!filter_duplicate.equals("1") && !filter_duplicate.equals("0")) || check_duplicate.equals("0"))
//			filter_duplicate = "0";
//		
//		String filetrigger = filesource.getFiletrigger();
//		if(null == filetrigger || (!filetrigger.equals("1") && !filetrigger.equals("0")))
//			filetrigger = "0";
//		
//		String trigger_job_uid = filesource.getTriggerjobuid();
//		if(null == trigger_job_uid || trigger_job_uid.trim().length() <= 0){
//			if(filetrigger.equals("1")){
//				throw new IllegalArgumentException("When Choose 'File Trigger', must be select one job uid!");
//			}else{
//				trigger_job_uid = "";
//			}
//		}
//		
//		String txdate__format = filesource.getTxdateformat();
//		if(null == txdate__format)
//			txdate__format = "";
//		
//		Integer txdate_start_pos = filesource.getTxdatestartpos();
//		if(null == txdate_start_pos || txdate_start_pos < 0)
//			txdate_start_pos = 0;
//		
//		Integer txdate_end_pos = filesource.getTxdateendpos();
//		if(null == txdate_end_pos || txdate_end_pos < 0)
//			txdate_end_pos = 0;
//		
//		//當沒有勾選filetrigger時, 要強制讓txdateformat='',txdatestartpos=0,txdateendpos=0
//        if(filetrigger.equals("0")){
//        	txdate__format = "";
//        	txdate_start_pos = 0;
//        	txdate_end_pos = 0;
//        }
//		
//		String checksum = filesource.getChecksum();
//		if(null == checksum || (!checksum.equals("1") && !checksum.equals("0")))
//			checksum = "0";
//		
//		String checksum_fe = filesource.getChecksumfe();
//		if(null == checksum_fe || checksum_fe.trim().length() <= 0)
//			checksum_fe = ".checksum";
//		
//		String checksum_alg = filesource.getChecksumalg();
//		if(null == checksum_alg || (!checksum_alg.equals("M") && !checksum_alg.equals("S")))
//			checksum_alg = "M";
//		
//		//如果不勾選check sum時, 則checksumalg及checksumfe一律代回預設值
//		if(checksum.equals("0")) {
//			checksum_fe = ".checksum";
//			checksum_alg = "M";
//		}
//			
//		Integer min_file = filesource.getMinfile();
//		if(null == min_file || min_file < 0)
//			min_file = 1;
//		
//		Integer max_file = filesource.getMaxfile();
//		if(null == max_file || max_file < 0)
//			max_file = 5;
//		
//		Integer timeout = filesource.getTimeout();
//		if(null == timeout || timeout < 0)
//			timeout = 3;
//		
//		String bypass_zero = filesource.getBypasszero();
//		if(null == bypass_zero || (!bypass_zero.equals("1") && !bypass_zero.equals("0")))
//			bypass_zero = "0";
//		
//		String appendUid = filesource.getAppendUid();
//		if(null == appendUid || (!appendUid.equals("1") && !appendUid.equals("0")))
//			bypass_zero = "0";
//		
//		String ftpget = filesource.getFtpget();
//		if(null == ftpget || (!ftpget.equals("1") && !ftpget.equals("0")))
//			ftpget = "0";
//		
//		String sftp = filesource.getSftp();
//		if(null == sftp || (!sftp.equals("1") && !sftp.equals("0")))
//			sftp = "0";
//		
//		String ftp_binary = filesource.getFtpbinary();
//		if(null == ftp_binary || (!ftp_binary.equals("1") && !ftp_binary.equals("0")))
//			ftp_binary = "0";
//		
//		String passive = filesource.getPassive();
//		if(null == passive || (!passive.equals("1") && !passive.equals("0")))
//			passive = "0";
//		
//		String ftp_connection_uid = filesource.getFtpconnectionuid();
//		if(null == ftp_connection_uid || ftp_connection_uid.length() <= 0){
//			if(ftpget.equals("1")){
//				throw new Exception("When Choose 'FTP Get', must be select one FTP Connection uid!");
//			}else{
//				ftp_connection_uid = "";
//			}
//		}
//		
//		String ftp_remote_dir = filesource.getFtpremotedir();
//		if(null == ftp_remote_dir || ftp_remote_dir.length() <= 0){
//			if(ftpget.equals("1")){
//				throw new Exception("When Choose 'FTP Get', must be have FTP Remote Directory!");
//			}else{
//				ftp_remote_dir = "";
//			}
//		}
//		
//		String ftp_post_action = filesource.getFtppostaction();
//		if(null == ftp_post_action || (!ftp_post_action.equals("0") && !ftp_post_action.equals("1") && !ftp_post_action.equals("2")))
//			ftp_post_action = "0";
//			
//		String ftp_move_dir = filesource.getFtpmovedir();
//		if(null == ftp_move_dir || ftp_move_dir.length() <= 0){
//			if(ftp_post_action.equals("1")){
//				throw new Exception("When 'ftp_post_action=1', must be have FTP Move Directory!");
//			}else{
//				ftp_move_dir = "";
//			}
//		}
//		
//		String checkrow = "0";
//		
//		filesource.setFilesourcename(filesourcename);
//		filesource.setDescription(description);
//		filesource.setFiletrigger(filetrigger);
//		filesource.setReceivedir(receive_dir);
//		filesource.setTargetdir(target_dir);
//		filesource.setCompletedir(complete_dir);
//		filesource.setCorruptdir(corrupt_dir);
//		filesource.setDuplicatedir(duplicate_dir);
//		filesource.setErrordir(error_dir);
//		filesource.setFilename(filename);
//		filesource.setPattern(pattern);
//		filesource.setStartposition(start_position);
//		filesource.setEndposition(end_position);
//		filesource.setFiletype(filetype);
//		filesource.setMinfile(min_file);
//		filesource.setMaxfile(max_file);
//		filesource.setTimeout(timeout);
//		filesource.setCheckduplicate(check_duplicate);
//		filesource.setFilterduplicate(filter_duplicate);
//		filesource.setCheckrow(checkrow);
//		filesource.setBypasszero(bypass_zero);
//		filesource.setFtpget(ftpget);
//		
//		setExtraXmlPropToString(filesource);
//		
//		/*
//		 * because lastupdatetime column is auto created value, it can not be reload new value.
//		 * here, we force to give value to lastupdatetime column.
//		 */
//		filesource.setLastupdatetime(new Date());
//		
//		FileSource new_filesource = this.dao.save(filesource);
//		/*
//		 * Because All fields associated with xml are defined by @Transient, it can not be reload new value.
//		 * The fields associated with xml is very suck design!
//		 */
//		setExtraXmlProp(new_filesource);
//		
//		//如果所附帶的url參數中有categoryUid的話, 表示是要把file source編輯至某個category或root
//		if(categoryUid != null) {
//			this.relService.deleteByFileSourceUid(new_filesource.getFilesourceuid());
//			if(!categoryUid.trim().equals("")) {	//如果categoryUid不是空值, 表示是要把filesource編輯到某一個category底下
//				FilesourceRelation rel = new FilesourceRelation();
//				rel.setFscategoryuid(categoryUid);
//				rel.setFilesourceuid(new_filesource.getFilesourceuid());
//				this.relService.add(rel);
//			}
//		}
		
		return this.dao.save(conn);
	}
	
	public void deleteByUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("Connection Uid can not be empty!");
		
		if(filesourceService.existByConnectionuid(uid))
			throw new IllegalArgumentException("Referenced By File Source!");
		
		if(jobstepService.existByConnectionuid(uid))
			throw new IllegalArgumentException("Referenced By JobStep!");
		
		this.dao.delete(uid);
		this.relService.deleteByConnectionUid(uid);
	}
	
	public boolean existByUid(String uid) throws Exception {
		return this.dao.exists(uid);
	}
	
	public List<JDBCDriverInfo> getJDBCDriverInfo() throws Exception {
		List<JDBCDriverInfo> info = this.jdbcInfo.getInfo();
		Collections.sort(info, new JDBCDriverNameComparator());
		return info;
	}
	
	private PageRequest getPagingAndOrdering(Paging paging, Ordering ordering) throws Exception{	
		if(paging.getNumber() == null)
			paging.setNumber(0);
		
		if(paging.getSize() == null)
			paging.setSize(10);
		
		if(ordering != null) {
			return new PageRequest(paging.getNumber(), paging.getSize(), getOrdering(ordering));
		}else {
			return new PageRequest(paging.getNumber(), paging.getSize());
		}
	}
	
	private Sort getOrdering(Ordering ordering) throws Exception{
		Direction direct = Direction.fromStringOrNull("DESC");
		if(ordering.getOrderType() != null && Constant.ORDER_TYPE_SET.contains(ordering.getOrderType().toUpperCase()))
			direct = Direction.fromStringOrNull(ordering.getOrderType());
		
		Order order = new Order(direct, "lastupdatetime");
		if(ordering.getOrderField() != null && !ordering.getOrderField().isEmpty())
			order = new Order(direct, ordering.getOrderField());
		
		return new Sort(order);
	}
	
	private List<Connection> getExtraXmlProp(List<Connection> conns) throws Exception{
		List<Connection> new_conns = new ArrayList<Connection>();
		for(Connection conn : conns) {
			new_conns.add(getExtraXmlProp(conn));
		}
		return new_conns;
	}
	
	private Connection getExtraXmlProp(Connection conn) throws Exception{
		String connectionuid = conn.getConnectionuid();
		String connectionname = conn.getConnectionname();
		String type = conn.getConnectiontype();
		String description = conn.getDescription();
		String withpim = conn.getWithpim();
		String pimendpointtype = conn.getPimendpointtype();
		String pimendpointname = conn.getPimendpointname();
		String pimaccountcontainer = conn.getPimaccountcontainer();
		String pimaccountname = conn.getPimaccountname();
		String xmldata = conn.getXmldata();
		Map<String, String> map = xmlUtil.parseXMLDataToHashMap(xmldata);
		if("J".equalsIgnoreCase(type)){
			JDBCConnection jdbc = new JDBCConnection();
			jdbc.setConnectionuid(connectionuid);
			jdbc.setConnectionname(connectionname);
			jdbc.setDescription(description);
			jdbc.setConnectiontype(type);
			jdbc.setWithpim(withpim);
			jdbc.setPimendpointtype(pimendpointtype);
			jdbc.setPimendpointname(pimendpointname);
			jdbc.setPimaccountcontainer(pimaccountcontainer);
			jdbc.setPimaccountname(pimaccountname);
			jdbc.setJdbc_dbType(map.get("jdbc_dbType"));
			jdbc.setJdbc_driver(map.get("jdbc_driver"));
			jdbc.setJdbc_password(Crypto.getDecryptString(map.get("jdbc_password"), encryptKey));
			jdbc.setJdbc_url(map.get("jdbc_url"));
			jdbc.setJdbc_userid(map.get("jdbc_userid"));
			return jdbc;
		}else if("D".equalsIgnoreCase(type)){
			DatabaseConnection database = new DatabaseConnection();
			database.setConnectionuid(connectionuid);
			database.setConnectionname(connectionname);
			database.setDescription(description);
			database.setConnectiontype(type);
			database.setWithpim(withpim);
			database.setPimendpointtype(pimendpointtype);
			database.setPimendpointname(pimendpointname);
			database.setPimaccountcontainer(pimaccountcontainer);
			database.setPimaccountname(pimaccountname);
			database.setUserid(map.get("userid"));
			database.setServer(map.get("server"));
			database.setPassword(Crypto.getDecryptString(map.get("password"), encryptKey));
			return database;
		}else if("F".equalsIgnoreCase(type)){
			FTPConnection ftp = new FTPConnection();
			ftp.setConnectionuid(connectionuid);
			ftp.setConnectionname(connectionname);
			ftp.setDescription(description);
			ftp.setConnectiontype(type);
			ftp.setWithpim(withpim);
			ftp.setPimendpointtype(pimendpointtype);
			ftp.setPimendpointname(pimendpointname);
			ftp.setPimaccountcontainer(pimaccountcontainer);
			ftp.setPimaccountname(pimaccountname);
			ftp.setUserid(map.get("userid"));
			ftp.setTargetdir(map.get("targetdir"));
			ftp.setServer(map.get("server"));
			ftp.setPassword(Crypto.getDecryptString(map.get("password"), encryptKey));
			return ftp;
		}else if("M".equalsIgnoreCase(type)){
			MailConnection mail = new MailConnection();
			mail.setConnectionuid(connectionuid);
			mail.setConnectionname(connectionname);
			mail.setDescription(description);
			mail.setConnectiontype(type);
			mail.setWithpim(withpim);
			mail.setPimendpointtype(pimendpointtype);
			mail.setPimendpointname(pimendpointname);
			mail.setPimaccountcontainer(pimaccountcontainer);
			mail.setPimaccountname(pimaccountname);
			mail.setUser(map.get("user"));
			mail.setHost(map.get("host"));
			try {
				mail.setPort(Integer.valueOf(map.get("port")));
			}catch(Exception e) {}
			mail.setPassword(Crypto.getDecryptString(map.get("password"), encryptKey));
			mail.setMailssl(map.get("mailssl"));
			mail.setMailtls(map.get("mailtls"));
			return mail;
		}else if("N".equalsIgnoreCase(type)){
			NotesConnection notes = new NotesConnection();
			notes.setConnectionuid(connectionuid);
			notes.setConnectionname(connectionname);
			notes.setDescription(description);
			notes.setConnectiontype(type);
			notes.setWithpim(withpim);
			notes.setPimendpointtype(pimendpointtype);
			notes.setPimendpointname(pimendpointname);
			notes.setPimaccountcontainer(pimaccountcontainer);
			notes.setPimaccountname(pimaccountname);
			notes.setNotesHostIP(map.get("notesHostIP"));
			notes.setNotesIor(map.get("notesIor"));
			notes.setUserid(map.get("userid"));
			notes.setPassword(Crypto.getDecryptString(map.get("password"), encryptKey));
			notes.setNotesDBName(map.get("notesDBName"));
			notes.setNotesServerName(map.get("notesServerName"));
			return notes;
		}else if("O".equalsIgnoreCase(type)){
			OSConnection os = new OSConnection();
			os.setConnectionuid(connectionuid);
			os.setConnectionname(connectionname);
			os.setDescription(description);
			os.setConnectiontype(type);
			os.setWithpim(withpim);
			os.setPimendpointtype(pimendpointtype);
			os.setPimendpointname(pimendpointname);
			os.setPimaccountcontainer(pimaccountcontainer);
			os.setPimaccountname(pimaccountname);
			os.setPort(map.get("port"));
			os.setUserid(map.get("userid"));
			os.setPassword(Crypto.getDecryptString(map.get("password"), encryptKey));
			return os;
		}else if("S".equalsIgnoreCase(type)){
			SapConnection sap = new SapConnection();
			sap.setConnectionuid(connectionuid);
			sap.setConnectionname(connectionname);
			sap.setDescription(description);
			sap.setConnectiontype(type);
			sap.setWithpim(withpim);
			sap.setPimendpointtype(pimendpointtype);
			sap.setPimendpointname(pimendpointname);
			sap.setPimaccountcontainer(pimaccountcontainer);
			sap.setPimaccountname(pimaccountname);
			sap.setSAPLANGUAGE(map.get("SAPLANGUAGE"));
			sap.setSapSystemName(map.get("sapSystemName"));
			sap.setSapSystemNumber(map.get("sapSystemNumber"));
			sap.setUserid(map.get("userid"));
			sap.setSapHostIP(map.get("sapHostIP"));
			sap.setPassword(Crypto.getDecryptString(map.get("password"), encryptKey));
			sap.setSapCodePage(map.get("sapCodePage"));
			sap.setSapClient(map.get("sapClient"));
			return sap;
		}else{
			throw new Exception("No Such Connection Type = " + type);
		}
	}
	
	private void getExtraXmlString(FileSource filesource) throws Exception{
		Map<String, String> map = new HashMap<String, String>();
		map.put("txdateendpos",filesource.getTxdateendpos()+"");
		map.put("ftpconnectionuid", filesource.getFtpconnectionuid());
		map.put("ftppostaction", filesource.getFtppostaction());
		map.put("triggerjobuid", filesource.getTriggerjobuid());
		map.put("sftp", filesource.getSftp());
		map.put("checksum", filesource.getChecksum());
		map.put("ftpmovedir", filesource.getFtpmovedir());
		map.put("txdateformat", filesource.getTxdateformat());
		map.put("checksumfe", filesource.getChecksumfe());
		map.put("passive", filesource.getPassive());
		map.put("ftpbinary", filesource.getFtpbinary());
		map.put("txdatestartpos", filesource.getTxdatestartpos()+"");
		map.put("checksumalg", filesource.getChecksumalg());
		map.put("ftpremotedir", filesource.getFtpremotedir());
		map.put("appendUid", filesource.getAppendUid());
		map.put("rootdir", filesource.getRootdir());
		map.put("cfImpClass", filesource.getCfImpClass());
		map.put("datafilecountmode", filesource.getDatafilecountmode());
		String xmldata = xmlUtil.parseHashMapToXMLString(map, false);
		filesource.setXmldata(xmldata);
	}
	
	
	private class JDBCDriverNameComparator implements Comparator<JDBCDriverInfo> {
	    @Override
	    public int compare(JDBCDriverInfo o1, JDBCDriverInfo o2) {
	        return o1.getName().compareTo(o2.getName());
	    }
	}
}
