package com.netpro.trinity.resource.admin.filesource.service;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.netpro.trinity.resource.admin.dto.FilterInfo;
import com.netpro.trinity.resource.admin.dto.Ordering;
import com.netpro.trinity.resource.admin.dto.Paging;
import com.netpro.trinity.resource.admin.filesource.dao.FileSourceJPADao;
import com.netpro.trinity.resource.admin.filesource.entity.FileSource;
import com.netpro.trinity.resource.admin.filesource.entity.FileSourceCategory;
import com.netpro.trinity.resource.admin.filesource.entity.FilesourceRelation;
import com.netpro.trinity.resource.admin.job.service.JobService;
import com.netpro.trinity.resource.admin.objectalias.service.ObjectAliasService;
import com.netpro.trinity.resource.admin.util.Constant;
import com.netpro.trinity.resource.admin.util.XMLDataUtility;

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
	@Autowired
	private ObjectAliasService objectAliasService;
	
	public List<FileSource> getAll() throws Exception{
		List<FileSource> filesources = this.dao.findAll();
		setProfileDataOnly(filesources);
		return filesources;
	}
	
	public List<FileSource> getAllWithoutInCategory() throws Exception{
		List<String> fs_uids = relService.getAllFileSourceUids();
		if(fs_uids.isEmpty()) {
			/*
			 * 當系統沒有建立任何fs和fs category之間的relation時, 則任意給一個空字串值
			 * 以免not in當中的值為empty, 導致搜尋不到任何不在fs category中的fs
			 */
			fs_uids.add("");
		}
		List<FileSource> filesources = this.dao.findByFilesourceuidNotIn(fs_uids, Sort.by("filesourcename"));
		setProfileDataOnly(filesources);
		return filesources;
	}
	
	public List<FileSource> getByCategoryUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.trim().isEmpty())
			throw new IllegalArgumentException("File Source Category UID can not be empty!");
		
		List<FileSource> filesources = this.dao.findByFilesourceuidIn(relService.getFileSourceUidsByCategoryUid(uid), Sort.by("filesourcename"));
		setProfileDataOnly(filesources);
		return filesources;
	}
	
	public FileSource getByUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.trim().isEmpty())
			throw new IllegalArgumentException("File Source UID can not be empty!");
		
		FileSource filesource = null;
		try {
			filesource = this.dao.findById(uid).get();
		}catch(NoSuchElementException e) {}
		 
		if(filesource == null)
			throw new IllegalArgumentException("File Source UID does not exist!(" + uid + ")");
		
		FileSourceCategory category = this.relService.getCategoryByFilesourceUid(uid);
		setExtraXmlPropAndCategoryInfo(filesource, category);
		return filesource;
	}
	
	public ResponseEntity<?> getByFilter(String categoryUid, FilterInfo filter) throws Exception{
		Paging paging = filter.getPaging();
		Ordering ordering = filter.getOrdering();
		String param = filter.getParam();
		
		if(null == paging) 
			paging = new Paging(0, 20);
		
		if(null == ordering) 
			ordering = new Ordering("ASC", "filesourcename");
		
		if(null == param || param.trim().isEmpty())
			param = "%%";
		param = param.trim();
		
		Page<FileSource> page_files = null;
		if(null == categoryUid) {
			page_files = this.dao.findByFilesourcenameLikeIgnoreCase(param, getPagingAndOrdering(paging, ordering));
			
			/*
			 * 當category uid為null時, 表示沒有指定任何的category, 包括連root也沒指定
			 * 此時需要取得connection uid及category name的對映表, 用來多回傳一個category name, 照作,沒什麼意義的需求
			 */
			Map<String, String> mapping = this.relService.getFilesourceUidAndCategoryNameMap();
			setProfileDataAndSetCategoryName(page_files.getContent(), mapping);
		}else {
			List<String> filesource_uids = null;
			if(categoryUid.trim().isEmpty() || "root".equals(categoryUid.trim())) {
				filesource_uids = this.relService.getAllFileSourceUids();
				if(filesource_uids.isEmpty()) {
					/*
					 * 當系統沒有建立任何conn和conn category之間的relation時, 則任意給一個空字串值
					 * 以免not in當中的值為empty, 導致搜尋不到任何不在conn category中的conn
					 */
					filesource_uids.add("");
				}
				page_files = this.dao.findByFilesourcenameLikeIgnoreCaseAndFilesourceuidNotIn(param, getPagingAndOrdering(paging, ordering), filesource_uids);
				setProfileDataOnly(page_files.getContent());
			}else {
				filesource_uids = this.relService.getFileSourceUidsByCategoryUid(categoryUid);
				page_files = this.dao.findByFilesourcenameLikeIgnoreCaseAndFilesourceuidIn(param, getPagingAndOrdering(paging, ordering), filesource_uids);
				setProfileDataOnly(page_files.getContent());
			}
		}
		
		return ResponseEntity.ok(page_files);
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
			appendUid = "0";
		
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
		
		setExtraXmlPropToString(filesource);
		
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
		setExtraXmlPropAndCategoryInfo(new_filesource, null);
		
		//如果所附帶的url參數中有categoryUid的話, 表示是要把file source新增至某個category
		if(categoryUid != null && !categoryUid.trim().equals("") && !categoryUid.trim().equals("root")) {
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
		
		FileSource old_filesource = null;
		try {
			old_filesource = this.dao.findById(filesourceuid).get();
		}catch(NoSuchElementException e) {}
		 
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
		
		setExtraXmlPropToString(filesource);
		
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
		setExtraXmlPropAndCategoryInfo(new_filesource, null);
		
		//如果所附帶的url參數中有categoryUid的話, 表示是要把file source編輯至某個category或root
		if(categoryUid != null) {
			this.relService.deleteByFileSourceUid(new_filesource.getFilesourceuid());
			if(!categoryUid.trim().equals("") && !categoryUid.trim().equals("root")) {	//如果categoryUid不是空值, 表示是要把filesource編輯到某一個category底下
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
		
		if(jobService.existByFilesourceuid(uid)) {
			throw new IllegalArgumentException("Referenceing by job");
		}else if(objectAliasService.existByObjectuid(uid)) {
			throw new IllegalArgumentException("Referenceing by Object Alias");
		}else {
			this.relService.deleteByFileSourceUid(uid);
			this.dao.deleteById(uid);
		}
	}
	
	public boolean existByUid(String uid) throws Exception{
		return this.dao.existsById(uid);
	}
	
	public boolean existByConnectionuid(String connectionuid) throws IllegalArgumentException, Exception{
		if(null == connectionuid || connectionuid.trim().length() <= 0)
			throw new IllegalArgumentException("Connection Uid can not be empty!");
		
		boolean ret = false;
		
		List<FileSource> filesources = this.dao.findAll();
		for(FileSource filesource : filesources){
			String xmldata = filesource.getXmldata();
			if(null != xmldata && xmldata.indexOf(connectionuid) > -1)
				return true;
		}
		
		return ret;
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
	
	@SuppressWarnings("unused")
	private void setExtraXmlPropAndCategoryInfo(List<FileSource> filesources, FileSourceCategory category) throws Exception{
		for(FileSource filesource : filesources) {
			setExtraXmlPropAndCategoryInfo(filesource, category);
		}
	}
	
	private void setExtraXmlPropAndCategoryInfo(FileSource filesource, FileSourceCategory category) throws Exception{
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
			
			if(null != category) {
				filesource.setCategoryuid(category.getFscategoryuid());
				filesource.setCategoryname(category.getFscategoryname());
			}
			
			filesource.setXmldata("");	//不再需要xml欄位的資料, 已經parsing
		}
	}
	
	private void setExtraXmlPropToString(FileSource filesource) throws Exception{
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
	
	private void setProfileDataOnly(List<FileSource> files) {
		for(FileSource file : files) {
			setProfileDataOnly(file);
		}
	}
	
	private void setProfileDataOnly(FileSource file) {
		file.setBypasszero(null);
		file.setCheckduplicate(null);
		file.setCheckrow(null);
		file.setCompletedir(null);
		file.setCorruptdir(null);
		file.setDuplicatedir(null);
		file.setEndposition(null);
		file.setErrordir(null);
		file.setFilename(null);
		file.setFiletrigger(null);
		file.setFiletype(null);
		file.setFilterduplicate(null);
		file.setFtpget(null);
		file.setMaxfile(null);
		file.setMinfile(null);
		file.setPattern(null);
		file.setReceivedir(null);
		file.setStartposition(null);
		file.setTargetdir(null);
		file.setTimeout(null);
		file.setXmldata(null);
	}
	
	private void setProfileDataAndSetCategoryName(List<FileSource> files, Map<String, String> mapping) {
		for(FileSource file : files) {
			setProfileDataAndSetCategoryName(file, mapping);
		}
	}
	
	private void setProfileDataAndSetCategoryName(FileSource file, Map<String, String> mapping) {
		setProfileDataOnly(file);
		
		String categoryName = mapping.get(file.getFilesourceuid());
		if(null != categoryName)
			file.setCategoryname(categoryName);
		else {
			file.setCategoryname("/");
		}
	}
}
