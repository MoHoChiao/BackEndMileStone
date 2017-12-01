package com.netpro.trinity.repository.service;

import java.util.Arrays;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.netpro.trinity.repository.dto.FilterInfo;
import com.netpro.trinity.repository.dto.Ordering;
import com.netpro.trinity.repository.dto.Paging;
import com.netpro.trinity.repository.dto.Querying;
import com.netpro.trinity.repository.jdbc.entity.FilesourceRelation;
import com.netpro.trinity.repository.jpa.dao.FileSourceJPADao;
import com.netpro.trinity.repository.jpa.entity.FileSource;
import com.netpro.trinity.repository.util.Constant;
import com.netpro.trinity.repository.util.XMLDataUtility;

@Service
public class FileSourceService {
	public static final String[] FS_FIELD_VALUES = new String[] {"filesourcename", "description"};
	public static final Set<String> FS_FIELD_SET = new HashSet<>(Arrays.asList(FS_FIELD_VALUES));
	
	@Autowired
	private XMLDataUtility xmlUtil;
	
	@Autowired
	private FileSourceJPADao dao;
	
	@Autowired
	private FilesourceRelationService relService;
	
	@Autowired
	private JobService jobService;
	
	public List<FileSource> getAll() throws Exception{
		List<FileSource> filesources = this.dao.findAll();
		setExtraXmlProp(filesources);
		return filesources;
	}
	
	public List<FileSource> getAllWithoutInCategory() throws Exception{
		List<FileSource> filesources = this.dao.findByFilesourceuidNotIn(relService.getAllFileSourceUids(), new Sort(new Order("filesourcename")));
		setExtraXmlProp(filesources);
		return filesources;
	}
	
	public boolean existByUid(String uid) {
		return this.dao.exists(uid);
	}
	
	public FileSource getByUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.trim().isEmpty())
			throw new IllegalArgumentException("File Source UID can not be empty!");
		
		FileSource filesource = this.dao.findOne(uid);
		if(filesource == null)
			throw new IllegalArgumentException("File Source UID does not exist!(" + uid + ")");
		setExtraXmlProp(filesource);
		return filesource;
	}
	
	public List<FileSource> getByName(String name) throws IllegalArgumentException, Exception{
		if(name == null || name.trim().isEmpty())
			throw new IllegalArgumentException("File Source Name can not be empty!");
		
		List<FileSource> filesources = this.dao.findByfilesourcename(name.toUpperCase());
		setExtraXmlProp(filesources);
		return filesources;
	}
	
	public List<FileSource> getByCategoryUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.trim().isEmpty())
			throw new IllegalArgumentException("File Source Category UID can not be empty!");
		
		List<FileSource> filesources = this.dao.findByFilesourceuidIn(relService.getFileSourceUidsByCategoryUid(uid), new Sort(new Order("filesourcename")));
		setExtraXmlProp(filesources);
		return filesources;
	}
	
	@SuppressWarnings("unchecked")
	public ResponseEntity<?> getByFilter(String categoryUid, FilterInfo filter) throws SecurityException, NoSuchMethodException, 
								IllegalArgumentException, IllegalAccessException, InvocationTargetException, Exception{
		
		if(filter == null) {
			List<FileSource> filesources;
			if(categoryUid == null) {
				filesources = this.dao.findAll();
			}else {
				if(categoryUid.trim().isEmpty()) {
					filesources = this.dao.findByFilesourceuidNotIn(relService.getAllFileSourceUids());
				}else {
					filesources = this.dao.findByFilesourceuidIn(relService.getFileSourceUidsByCategoryUid(categoryUid));
				}
			}
			setExtraXmlProp(filesources);
			return ResponseEntity.ok(filesources);
		}
		
		Paging paging = filter.getPaging();
		Ordering ordering = filter.getOrdering();
		Querying querying = filter.getQuerying();
		
		if(paging == null && ordering == null && querying == null) {
			List<FileSource> filesources;
			if(categoryUid == null) {
				filesources = this.dao.findAll();
			}else {
				if(categoryUid.trim().isEmpty()) {
					filesources = this.dao.findByFilesourceuidNotIn(relService.getAllFileSourceUids());
				}else {
					filesources = this.dao.findByFilesourceuidIn(relService.getFileSourceUidsByCategoryUid(categoryUid));
				}
			}
			setExtraXmlProp(filesources);
			return ResponseEntity.ok(filesources);
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
				Page<FileSource> page_filesource;
				if(categoryUid == null) {
					page_filesource = this.dao.findAll(pageRequest);
				}else {
					if(categoryUid.trim().isEmpty()) {
						page_filesource = this.dao.findByFilesourceuidNotIn(relService.getAllFileSourceUids(), pageRequest);
					}else {
						page_filesource = this.dao.findByFilesourceuidIn(relService.getFileSourceUidsByCategoryUid(categoryUid), pageRequest);
					}
				}
				setExtraXmlProp(page_filesource.getContent());
				return ResponseEntity.ok(page_filesource);
			}else if(sort != null) {
				List<FileSource> filesources;
				if(categoryUid == null) {
					filesources = this.dao.findAll(sort);
				}else {
					if(categoryUid.trim().isEmpty()) {
						filesources = this.dao.findByFilesourceuidNotIn(relService.getAllFileSourceUids(), sort);
					}else {
						filesources = this.dao.findByFilesourceuidIn(relService.getFileSourceUidsByCategoryUid(categoryUid), sort);
					}
				}
				setExtraXmlProp(filesources);
				return ResponseEntity.ok(filesources);
			}else {
				/*
				 * The paging and ordering both objects are null.
				 * it means pageRequest and sort must be null too.
				 * then return default
				 */
				List<FileSource> filesources;
				if(categoryUid == null) {
					filesources = this.dao.findAll();
				}else {
					if(categoryUid.trim().isEmpty()) {
						filesources = this.dao.findByFilesourceuidNotIn(relService.getAllFileSourceUids());
					}else {
						filesources = this.dao.findByFilesourceuidIn(relService.getFileSourceUidsByCategoryUid(categoryUid));
					}
				}
				setExtraXmlProp(filesources);
				return ResponseEntity.ok(filesources);
			}
		}else {
			if(querying.getQueryType() == null || !Constant.QUERY_TYPE_SET.contains(querying.getQueryType().toLowerCase()))
				throw new IllegalArgumentException("Illegal query type! "+Constant.QUERY_TYPE_SET.toString());
			if(querying.getQueryField() == null || !FS_FIELD_SET.contains(querying.getQueryField().toLowerCase()))
				throw new IllegalArgumentException("Illegal query field! "+ FS_FIELD_SET.toString());
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
					methodName.append("AndFilesourceuidNotIn");
				}else {
					methodName.append("AndFilesourceuidIn");
				}
			}
			
			Method method = null;
			if(pageRequest != null){
				Page<FileSource> page_filesource;
				if(categoryUid == null) {
					method = this.dao.getClass().getMethod(methodName.toString(), String.class, Pageable.class);
					page_filesource = (Page<FileSource>) method.invoke(this.dao, queryString, pageRequest);
				}else {
					method = this.dao.getClass().getMethod(methodName.toString(), String.class, Pageable.class, List.class);
					if(categoryUid.trim().isEmpty()) {
						page_filesource = (Page<FileSource>) method.invoke(this.dao, queryString, pageRequest, relService.getAllFileSourceUids());
					}else {
						page_filesource = (Page<FileSource>) method.invoke(this.dao, queryString, pageRequest, relService.getFileSourceUidsByCategoryUid(categoryUid));
					}
				}
				setExtraXmlProp(page_filesource.getContent());
				return ResponseEntity.ok(page_filesource);
			}else if(sort != null) {
				List<FileSource> filesources;
				if(categoryUid == null) {
					method = this.dao.getClass().getMethod(methodName.toString(), String.class, Sort.class);
					filesources = (List<FileSource>) method.invoke(this.dao, queryString, sort);
				}else {
					method = this.dao.getClass().getMethod(methodName.toString(), String.class, Sort.class, List.class);
					if(categoryUid.trim().isEmpty()) {
						filesources = (List<FileSource>) method.invoke(this.dao, queryString, sort, relService.getAllFileSourceUids());
					}else {
						filesources = (List<FileSource>) method.invoke(this.dao, queryString, sort, relService.getFileSourceUidsByCategoryUid(categoryUid));
					}
				}
				setExtraXmlProp(filesources);
				return ResponseEntity.ok(filesources);
			}else {
				List<FileSource> filesources;
				if(categoryUid == null) {
					method = this.dao.getClass().getMethod(methodName.toString(), String.class);
					filesources = (List<FileSource>) method.invoke(this.dao, queryString);
				}else {
					method = this.dao.getClass().getMethod(methodName.toString(), String.class, List.class);
					if(categoryUid.trim().isEmpty()) {
						filesources = (List<FileSource>) method.invoke(this.dao, queryString, relService.getAllFileSourceUids());
					}else {
						filesources = (List<FileSource>) method.invoke(this.dao, queryString, relService.getFileSourceUidsByCategoryUid(categoryUid));
					}
				}
				setExtraXmlProp(filesources);
				return ResponseEntity.ok(filesources);
			}
		}
	}
	
	public FileSource add(String categoryUid, FileSource filesource) throws IllegalArgumentException, Exception{
		String newUid = UUID.randomUUID().toString();
		
		String filesourcename = filesource.getFilesourcename();
		if(null == filesourcename || filesourcename.length() <= 0)
			throw new IllegalArgumentException("File Source Name can not be empty!");
		filesourcename = filesourcename.toUpperCase();
		
		if(this.dao.existByName(filesourcename))
			throw new IllegalArgumentException("Duplicate File Source Name!");
		
		String description = filesource.getDescription();
		if(null == description)
			description = "";
		
		String root_dir = filesource.getRootdir();
		if(null == root_dir)
			root_dir = "";
		
		String receive_dir = filesource.getReceivedir();
		if(null == receive_dir || receive_dir.length() <= 0)
			throw new IllegalArgumentException("Receive Directory can not be empty!");
		
		String target_dir = filesource.getTargetdir();
		if(null == target_dir || target_dir.length() <= 0)
			throw new IllegalArgumentException("Target Directory can not be empty!");
		
		String complete_dir = filesource.getCompletedir();
		if(null == complete_dir || complete_dir.length() <= 0)
			throw new IllegalArgumentException("Complete Directory can not be empty!");
		
		String corrupt_dir = filesource.getCorruptdir();
		if(null == corrupt_dir || corrupt_dir.length() <= 0)
			throw new IllegalArgumentException("Corrupt Directory can not be empty!");
		
		String duplicate_dir = filesource.getDuplicatedir();
		if(null == duplicate_dir || duplicate_dir.length() <= 0)
			throw new IllegalArgumentException("Duplicate Directory can not be empty!");
		
		String error_dir = filesource.getErrordir();
		if(null == error_dir || error_dir.length() <= 0)
			throw new IllegalArgumentException("Error Directory can not be empty!");
		
		String filename = filesource.getFilename();
		if(null == filename || filename.length() <= 0)
			throw new IllegalArgumentException("File Name Pattern can not be empty!");
		
		String pattern = filesource.getPattern();
		if(null == pattern || (!pattern.equals("1") && !pattern.equals("2") && !pattern.equals("3") && !pattern.equals("4")))
			pattern = "1";
		
		Integer start_position = filesource.getStartposition();
		if(null == start_position || start_position < 0)
			start_position = 0;
		
		Integer end_position = filesource.getEndposition();
		if(null == end_position || end_position < 0)
			end_position = 0;
		
		if(pattern.equals("3")) {
			if(end_position <= start_position)
				throw new IllegalArgumentException("End Position can not be equals or smaller than Start Position!");
		}
		
		String filetype = filesource.getFiletype();
		if(null == filetype || (!filetype.equals("D") && !filetype.equals("C")))
			filetype = "D";
		
		String datafilecountmode = filesource.getDatafilecountmode();
		if(null == datafilecountmode || (!datafilecountmode.equals("R") && !datafilecountmode.equals("C")))
			datafilecountmode = "R";
		
		String cfImpClass = filesource.getCfImpClass();
		if(null == cfImpClass)
			cfImpClass = "com.netpro.filesource.ctrl.MatchFileSizeCtrlFileHandler";
		
		//如果是選擇Regular Data時, 則cfImpClass及datafilecountmode一律代回預設值
		if(filetype.equals("D")){
			datafilecountmode = "R";
            cfImpClass = "com.netpro.filesource.ctrl.MatchFileSizeCtrlFileHandler";
        }
		
		String check_duplicate = filesource.getCheckduplicate();
		if(null == check_duplicate || (!check_duplicate.equals("1") && !check_duplicate.equals("0")))
			check_duplicate = "0";
		
		String filter_duplicate = filesource.getFilterduplicate();
		if(null == filter_duplicate || (!filter_duplicate.equals("1") && !filter_duplicate.equals("0")) || check_duplicate.equals("0"))
			filter_duplicate = "0";
		
		String filetrigger = filesource.getFiletrigger();
		if(null == filetrigger || (!filetrigger.equals("1") && !filetrigger.equals("0")))
			filetrigger = "0";
		
		String trigger_job_uid = filesource.getTriggerjobuid();
		if(null == trigger_job_uid || trigger_job_uid.trim().length() <= 0){
			if(filetrigger.equals("1")){
				throw new IllegalArgumentException("When Choose 'File Trigger', must be select one job uid!");
			}else{
				trigger_job_uid = "";
			}
		}
		
		String txdate__format = filesource.getTxdateformat();
		if(null == txdate__format)
			txdate__format = "";
		
		Integer txdate_start_pos = filesource.getTxdatestartpos();
		if(null == txdate_start_pos || txdate_start_pos < 0)
			txdate_start_pos = 0;
		
		Integer txdate_end_pos = filesource.getTxdateendpos();
		if(null == txdate_end_pos || txdate_end_pos < 0)
			txdate_end_pos = 0;
		
		//當沒有勾選filetrigger時, 要強制讓txdateformat='',txdatestartpos=0,txdateendpos=0
        if(filetrigger.equals("0")){
        	txdate__format = "";
        	txdate_start_pos = 0;
        	txdate_end_pos = 0;
        }
		
		String checksum = filesource.getChecksum();
		if(null == checksum || (!checksum.equals("1") && !checksum.equals("0")))
			checksum = "0";
		
		String checksum_fe = filesource.getChecksumfe();
		if(null == checksum_fe || checksum_fe.trim().length() <= 0)
			checksum_fe = ".checksum";
		
		String checksum_alg = filesource.getChecksumalg();
		if(null == checksum_alg || (!checksum_alg.equals("M") && !checksum_alg.equals("S")))
			checksum_alg = "M";
		
		//如果不勾選check sum時, 則checksumalg及checksumfe一律代回預設值
		if(checksum.equals("0")) {
			checksum_fe = ".checksum";
			checksum_alg = "M";
		}
			
		Integer min_file = filesource.getMinfile();
		if(null == min_file || min_file < 0)
			min_file = 1;
		
		Integer max_file = filesource.getMaxfile();
		if(null == max_file || max_file < 0)
			max_file = 5;
		
		Integer timeout = filesource.getTimeout();
		if(null == timeout || timeout < 0)
			timeout = 3;
		
		String bypass_zero = filesource.getBypasszero();
		if(null == bypass_zero || (!bypass_zero.equals("1") && !bypass_zero.equals("0")))
			bypass_zero = "0";
		
		String appendUid = filesource.getAppendUid();
		if(null == appendUid || (!appendUid.equals("1") && !appendUid.equals("0")))
			bypass_zero = "0";
		
		String ftpget = filesource.getFtpget();
		if(null == ftpget || (!ftpget.equals("1") && !ftpget.equals("0")))
			ftpget = "0";
		
		String sftp = filesource.getSftp();
		if(null == sftp || (!sftp.equals("1") && !sftp.equals("0")))
			sftp = "0";
		
		String ftp_binary = filesource.getFtpbinary();
		if(null == ftp_binary || (!ftp_binary.equals("1") && !ftp_binary.equals("0")))
			ftp_binary = "0";
		
		String passive = filesource.getPassive();
		if(null == passive || (!passive.equals("1") && !passive.equals("0")))
			passive = "0";
		
		String ftp_connection_uid = filesource.getFtpconnectionuid();
		if(null == ftp_connection_uid || ftp_connection_uid.length() <= 0){
			if(ftpget.equals("1")){
				throw new Exception("When Choose 'FTP Get', must be select one FTP Connection uid!");
			}else{
				ftp_connection_uid = "";
			}
		}
		
		String ftp_remote_dir = filesource.getFtpremotedir();
		if(null == ftp_remote_dir || ftp_remote_dir.length() <= 0){
			if(ftpget.equals("1")){
				throw new Exception("When Choose 'FTP Get', must be have FTP Remote Directory!");
			}else{
				ftp_remote_dir = "";
			}
		}
		
		String ftp_post_action = filesource.getFtppostaction();
		if(null == ftp_post_action || (!ftp_post_action.equals("0") && !ftp_post_action.equals("1") && !ftp_post_action.equals("2")))
			ftp_post_action = "0";
			
		String ftp_move_dir = filesource.getFtpmovedir();
		if(null == ftp_move_dir || ftp_move_dir.length() <= 0){
			if(ftp_post_action.equals("1")){
				throw new Exception("When 'ftp_post_action=1', must be have FTP Move Directory!");
			}else{
				ftp_move_dir = "";
			}
		}
		
		String checkrow = "0";
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("txdateendpos", txdate_end_pos.toString());
		map.put("ftpconnectionuid", ftp_connection_uid);
		map.put("ftppostaction", ftp_post_action);
		map.put("triggerjobuid", trigger_job_uid);
		map.put("sftp", sftp);
		map.put("checksum", checksum);
		map.put("ftpmovedir", ftp_move_dir);
		map.put("txdateformat", txdate__format);
		map.put("checksumfe", checksum_fe);
		map.put("passive", passive);
		map.put("ftpbinary", ftp_binary);
		map.put("txdatestartpos", txdate_start_pos.toString());
		map.put("checksumalg", checksum_alg);
		map.put("ftpremotedir", ftp_remote_dir);
		map.put("appendUid", appendUid);
		map.put("rootdir", root_dir);
		map.put("cfImpClass", cfImpClass);
		map.put("datafilecountmode", datafilecountmode);
		String xmldata = xmlUtil.parseHashMapToXMLString(map, false);
		
		filesource.setFilesourceuid(newUid);
		filesource.setFilesourcename(filesourcename);
		filesource.setDescription(description);
		filesource.setFiletrigger(filetrigger);
		filesource.setReceivedir(receive_dir);
		filesource.setTargetdir(target_dir);
		filesource.setCompletedir(complete_dir);
		filesource.setCorruptdir(corrupt_dir);
		filesource.setDuplicatedir(duplicate_dir);
		filesource.setErrordir(error_dir);
		filesource.setFilename(filename);
		filesource.setPattern(pattern);
		filesource.setStartposition(start_position);
		filesource.setEndposition(end_position);
		filesource.setFiletype(filetype);
		filesource.setMinfile(min_file);
		filesource.setMaxfile(max_file);
		filesource.setTimeout(timeout);
		filesource.setCheckduplicate(check_duplicate);
		filesource.setFilterduplicate(filter_duplicate);
		filesource.setCheckrow(checkrow);
		filesource.setBypasszero(bypass_zero);
		filesource.setFtpget(ftpget);
		filesource.setXmldata(xmldata);
		
		/*
		 * because lastupdatetime column is auto created value, it can not be reload new value.
		 * here, we force to give value to lastupdatetime column.
		 */
		filesource.setLastupdatetime(new Date());
		
		FileSource new_filesource = this.dao.save(filesource);
		/*
		 * Because All fields associated with xml are defined by @Transient, it can not be reload new value.
		 * The fields associated with xml is very suck design!
		 */
		setExtraXmlProp(new_filesource);
		
		//如果所附帶的url參數中有categoryUid的話, 表示是要把file source新增至某個category
		if(categoryUid != null && !categoryUid.trim().equals("")) {
			FilesourceRelation rel = new FilesourceRelation();
			rel.setFscategoryuid(categoryUid);
			rel.setFilesourceuid(new_filesource.getFilesourceuid());
			this.relService.add(rel);
		}
		
		return new_filesource;
	}
	
	public FileSource edit(String categoryUid, FileSource filesource) throws IllegalArgumentException, Exception{
		String filesourceuid = filesource.getFilesourceuid();
		if(null == filesourceuid || filesourceuid.trim().length() <= 0)
			throw new IllegalArgumentException("File Source Uid can not be empty!");
		
		FileSource old_filesource = this.dao.findOne(filesourceuid);
		if(null == old_filesource)
			throw new IllegalArgumentException("File Source Uid does not exist!(" + filesourceuid + ")");
		
		String filesourcename = filesource.getFilesourcename();
		if(null == filesourcename || filesourcename.length() <= 0)
			throw new IllegalArgumentException("File Source Name can not be empty!");
		filesourcename = filesourcename.toUpperCase();
		
		if(this.dao.existByName(filesourcename) && !old_filesource.getFilesourcename().equalsIgnoreCase(filesourcename))
			throw new IllegalArgumentException("Duplicate File Source Name!");
		
		String description = filesource.getDescription();
		if(null == description)
			description = "";
		
		String root_dir = filesource.getRootdir();
		if(null == root_dir)
			root_dir = "";
		
		String receive_dir = filesource.getReceivedir();
		if(null == receive_dir || receive_dir.length() <= 0)
			throw new IllegalArgumentException("Receive Directory can not be empty!");
		
		String target_dir = filesource.getTargetdir();
		if(null == target_dir || target_dir.length() <= 0)
			throw new IllegalArgumentException("Target Directory can not be empty!");
		
		String complete_dir = filesource.getCompletedir();
		if(null == complete_dir || complete_dir.length() <= 0)
			throw new IllegalArgumentException("Complete Directory can not be empty!");
		
		String corrupt_dir = filesource.getCorruptdir();
		if(null == corrupt_dir || corrupt_dir.length() <= 0)
			throw new IllegalArgumentException("Corrupt Directory can not be empty!");
		
		String duplicate_dir = filesource.getDuplicatedir();
		if(null == duplicate_dir || duplicate_dir.length() <= 0)
			throw new IllegalArgumentException("Duplicate Directory can not be empty!");
		
		String error_dir = filesource.getErrordir();
		if(null == error_dir || error_dir.length() <= 0)
			throw new IllegalArgumentException("Error Directory can not be empty!");
		
		String filename = filesource.getFilename();
		if(null == filename || filename.length() <= 0)
			throw new IllegalArgumentException("File Name Pattern can not be empty!");
		
		String pattern = filesource.getPattern();
		if(null == pattern || (!pattern.equals("1") && !pattern.equals("2") && !pattern.equals("3") && !pattern.equals("4")))
			pattern = "1";
		
		Integer start_position = filesource.getStartposition();
		if(null == start_position || start_position < 0)
			start_position = 0;
		
		Integer end_position = filesource.getEndposition();
		if(null == end_position || end_position < 0)
			end_position = 0;
		
		if(pattern.equals("3")) {
			if(end_position <= start_position)
				throw new IllegalArgumentException("End Position can not be equals or smaller than Start Position!");
		}
		
		String filetype = filesource.getFiletype();
		if(null == filetype || (!filetype.equals("D") && !filetype.equals("C")))
			filetype = "D";
		
		String datafilecountmode = filesource.getDatafilecountmode();
		if(null == datafilecountmode || (!datafilecountmode.equals("R") && !datafilecountmode.equals("C")))
			datafilecountmode = "R";
		
		String cfImpClass = filesource.getCfImpClass();
		if(null == cfImpClass)
			cfImpClass = "com.netpro.filesource.ctrl.MatchFileSizeCtrlFileHandler";
		
		//如果是選擇Regular Data時, 則cfImpClass及datafilecountmode一律代回預設值
		if(filetype.equals("D")){
			datafilecountmode = "R";
            cfImpClass = "com.netpro.filesource.ctrl.MatchFileSizeCtrlFileHandler";
        }
		
		String check_duplicate = filesource.getCheckduplicate();
		if(null == check_duplicate || (!check_duplicate.equals("1") && !check_duplicate.equals("0")))
			check_duplicate = "0";
		
		String filter_duplicate = filesource.getFilterduplicate();
		if(null == filter_duplicate || (!filter_duplicate.equals("1") && !filter_duplicate.equals("0")) || check_duplicate.equals("0"))
			filter_duplicate = "0";
		
		String filetrigger = filesource.getFiletrigger();
		if(null == filetrigger || (!filetrigger.equals("1") && !filetrigger.equals("0")))
			filetrigger = "0";
		
		String trigger_job_uid = filesource.getTriggerjobuid();
		if(null == trigger_job_uid || trigger_job_uid.trim().length() <= 0){
			if(filetrigger.equals("1")){
				throw new IllegalArgumentException("When Choose 'File Trigger', must be select one job uid!");
			}else{
				trigger_job_uid = "";
			}
		}
		
		String txdate__format = filesource.getTxdateformat();
		if(null == txdate__format)
			txdate__format = "";
		
		Integer txdate_start_pos = filesource.getTxdatestartpos();
		if(null == txdate_start_pos || txdate_start_pos < 0)
			txdate_start_pos = 0;
		
		Integer txdate_end_pos = filesource.getTxdateendpos();
		if(null == txdate_end_pos || txdate_end_pos < 0)
			txdate_end_pos = 0;
		
		//當沒有勾選filetrigger時, 要強制讓txdateformat='',txdatestartpos=0,txdateendpos=0
        if(filetrigger.equals("0")){
        	txdate__format = "";
        	txdate_start_pos = 0;
        	txdate_end_pos = 0;
        }
		
		String checksum = filesource.getChecksum();
		if(null == checksum || (!checksum.equals("1") && !checksum.equals("0")))
			checksum = "0";
		
		String checksum_fe = filesource.getChecksumfe();
		if(null == checksum_fe || checksum_fe.trim().length() <= 0)
			checksum_fe = ".checksum";
		
		String checksum_alg = filesource.getChecksumalg();
		if(null == checksum_alg || (!checksum_alg.equals("M") && !checksum_alg.equals("S")))
			checksum_alg = "M";
		
		//如果不勾選check sum時, 則checksumalg及checksumfe一律代回預設值
		if(checksum.equals("0")) {
			checksum_fe = ".checksum";
			checksum_alg = "M";
		}
			
		Integer min_file = filesource.getMinfile();
		if(null == min_file || min_file < 0)
			min_file = 1;
		
		Integer max_file = filesource.getMaxfile();
		if(null == max_file || max_file < 0)
			max_file = 5;
		
		Integer timeout = filesource.getTimeout();
		if(null == timeout || timeout < 0)
			timeout = 3;
		
		String bypass_zero = filesource.getBypasszero();
		if(null == bypass_zero || (!bypass_zero.equals("1") && !bypass_zero.equals("0")))
			bypass_zero = "0";
		
		String appendUid = filesource.getAppendUid();
		if(null == appendUid || (!appendUid.equals("1") && !appendUid.equals("0")))
			bypass_zero = "0";
		
		String ftpget = filesource.getFtpget();
		if(null == ftpget || (!ftpget.equals("1") && !ftpget.equals("0")))
			ftpget = "0";
		
		String sftp = filesource.getSftp();
		if(null == sftp || (!sftp.equals("1") && !sftp.equals("0")))
			sftp = "0";
		
		String ftp_binary = filesource.getFtpbinary();
		if(null == ftp_binary || (!ftp_binary.equals("1") && !ftp_binary.equals("0")))
			ftp_binary = "0";
		
		String passive = filesource.getPassive();
		if(null == passive || (!passive.equals("1") && !passive.equals("0")))
			passive = "0";
		
		String ftp_connection_uid = filesource.getFtpconnectionuid();
		if(null == ftp_connection_uid || ftp_connection_uid.length() <= 0){
			if(ftpget.equals("1")){
				throw new Exception("When Choose 'FTP Get', must be select one FTP Connection uid!");
			}else{
				ftp_connection_uid = "";
			}
		}
		
		String ftp_remote_dir = filesource.getFtpremotedir();
		if(null == ftp_remote_dir || ftp_remote_dir.length() <= 0){
			if(ftpget.equals("1")){
				throw new Exception("When Choose 'FTP Get', must be have FTP Remote Directory!");
			}else{
				ftp_remote_dir = "";
			}
		}
		
		String ftp_post_action = filesource.getFtppostaction();
		if(null == ftp_post_action || (!ftp_post_action.equals("0") && !ftp_post_action.equals("1") && !ftp_post_action.equals("2")))
			ftp_post_action = "0";
			
		String ftp_move_dir = filesource.getFtpmovedir();
		if(null == ftp_move_dir || ftp_move_dir.length() <= 0){
			if(ftp_post_action.equals("1")){
				throw new Exception("When 'ftp_post_action=1', must be have FTP Move Directory!");
			}else{
				ftp_move_dir = "";
			}
		}
		
		String checkrow = "0";
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("txdateendpos", txdate_end_pos.toString());
		map.put("ftpconnectionuid", ftp_connection_uid);
		map.put("ftppostaction", ftp_post_action);
		map.put("triggerjobuid", trigger_job_uid);
		map.put("sftp", sftp);
		map.put("checksum", checksum);
		map.put("ftpmovedir", ftp_move_dir);
		map.put("txdateformat", txdate__format);
		map.put("checksumfe", checksum_fe);
		map.put("passive", passive);
		map.put("ftpbinary", ftp_binary);
		map.put("txdatestartpos", txdate_start_pos.toString());
		map.put("checksumalg", checksum_alg);
		map.put("ftpremotedir", ftp_remote_dir);
		map.put("appendUid", appendUid);
		map.put("rootdir", root_dir);
		map.put("cfImpClass", cfImpClass);
		map.put("datafilecountmode", datafilecountmode);
		String xmldata = xmlUtil.parseHashMapToXMLString(map, false);
		
		filesource.setFilesourcename(filesourcename);
		filesource.setDescription(description);
		filesource.setFiletrigger(filetrigger);
		filesource.setReceivedir(receive_dir);
		filesource.setTargetdir(target_dir);
		filesource.setCompletedir(complete_dir);
		filesource.setCorruptdir(corrupt_dir);
		filesource.setDuplicatedir(duplicate_dir);
		filesource.setErrordir(error_dir);
		filesource.setFilename(filename);
		filesource.setPattern(pattern);
		filesource.setStartposition(start_position);
		filesource.setEndposition(end_position);
		filesource.setFiletype(filetype);
		filesource.setMinfile(min_file);
		filesource.setMaxfile(max_file);
		filesource.setTimeout(timeout);
		filesource.setCheckduplicate(check_duplicate);
		filesource.setFilterduplicate(filter_duplicate);
		filesource.setCheckrow(checkrow);
		filesource.setBypasszero(bypass_zero);
		filesource.setFtpget(ftpget);
		filesource.setXmldata(xmldata);
		
		/*
		 * because lastupdatetime column is auto created value, it can not be reload new value.
		 * here, we force to give value to lastupdatetime column.
		 */
		filesource.setLastupdatetime(new Date());
		
		FileSource new_filesource = this.dao.save(filesource);
		/*
		 * Because All fields associated with xml are defined by @Transient, it can not be reload new value.
		 * The fields associated with xml is very suck design!
		 */
		setExtraXmlProp(new_filesource);
		
		//如果所附帶的url參數中有categoryUid的話, 表示是要把file source編輯至某個category或root
		if(categoryUid != null) {
			this.relService.deleteByFileSourceUid(new_filesource.getFilesourceuid());
			if(!categoryUid.trim().equals("")) {	//如果categoryUid不是空值, 表示是要把filesource編輯到某一個category底下
				FilesourceRelation rel = new FilesourceRelation();
				rel.setFscategoryuid(categoryUid);
				rel.setFilesourceuid(new_filesource.getFilesourceuid());
				this.relService.add(rel);
			}
		}
		
		return new_filesource;
	}
	
	public void deleteByUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("File Source Uid can not be empty!");
		
		if(!jobService.existByFilesourceuid(uid)) {
			this.dao.delete(uid);
			this.relService.deleteByFileSourceUid(uid);
		}else {
			throw new IllegalArgumentException("Referenceing by job");
		}
		
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
	
	private void setExtraXmlProp(List<FileSource> filesources) throws Exception{
		for(FileSource filesource : filesources) {
			setExtraXmlProp(filesource);
		}
	}
	
	private void setExtraXmlProp(FileSource filesource) throws Exception{
		HashMap<String, String> map = xmlUtil.parseXMLDataToHashMap(filesource.getXmldata());
		if(map != null) {
			filesource.setFtppostaction(map.get("ftppostaction"));
			filesource.setCfImpClass(map.get("cfImpClass"));
			filesource.setTxdateformat(map.get("txdateformat"));
			filesource.setRootdir(map.get("rootdir"));
			filesource.setFtpconnectionuid(map.get("ftpconnectionuid"));
			filesource.setPassive(map.get("passive"));
			filesource.setTriggerjobuid(map.get("triggerjobuid"));
			filesource.setChecksumalg(map.get("checksumalg"));
			filesource.setFtpmovedir(map.get("ftpmovedir"));
			filesource.setDatafilecountmode(map.get("datafilecountmode"));
			filesource.setChecksumfe(map.get("checksumfe"));
			filesource.setAppendUid(map.get("appendUid"));
			filesource.setFtpremotedir(map.get("ftpremotedir"));
			filesource.setChecksum(map.get("checksum"));
			filesource.setSftp(map.get("sftp"));
			
			String txdatestartpos =  map.get("txdatestartpos");
			Integer txdatestartpos_int = 0;
			try {
				txdatestartpos_int = Integer.valueOf(txdatestartpos);
			}catch(Exception e) {}
			String txdateendpos= map.get("txdateendpos");
			Integer txdateendpos_int = 0;
			try {
				txdateendpos_int = Integer.valueOf(txdateendpos);
			}catch(Exception e) {}
			
			filesource.setTxdatestartpos(txdatestartpos_int);
			filesource.setTxdateendpos(txdateendpos_int);
			filesource.setFtpbinary(map.get("ftpbinary"));
		}
	}
}
