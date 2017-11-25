package com.netpro.trinity.repository.jpa.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity(name="Filesource")  //宣告這是一個實體Filesource的類別
public class FileSource {	
	@Id
  	private String filesourceuid;
  	@Column(nullable=false)
  	private String filesourcename;
  	@Column
  	private String description;
  	@Column(nullable=false)
  	private String filetrigger;
  	@Column(nullable=false)
  	private String receivedir;
  	@Column(nullable=false)
  	private String targetdir;
  	@Column(nullable=false)
  	private String completedir;
  	@Column(nullable=false)
  	private String corruptdir;
  	@Column(nullable=false)
  	private String duplicatedir;
  	@Column(nullable=false)
  	private String errordir;
  	@Column(nullable=false)
  	private String filename;
  	@Column(nullable=false)
  	private String pattern;
  	@Column
  	private Integer startposition;
  	@Column
  	private Integer endposition;
  	@Column(nullable=false)
  	private String filetype;
  	@Column
  	private Integer minfile;
  	@Column
  	private Integer maxfile;
  	@Column
  	private Integer timeout;
  	@Column(nullable=false)
  	private String checkduplicate;
  	@Column(nullable=false)
  	private String filterduplicate;
  	@Column(nullable=false)
  	private String checkrow;
  	@Column(nullable=false)
  	private String bypasszero;
  	@Column(nullable=false)
  	private String ftpget;
  	@Column
  	private String xmldata;
  	@Column
  	@Temporal(TemporalType.TIMESTAMP)
  	private Date lastupdatetime;
  
  	
  	
  	@Transient
  	private String ftppostaction;
  	@Transient
  	private String cfImpClass;
  	@Transient
  	private String txdateformat;
  	@Transient
  	private String rootdir;
  	@Transient
  	private String ftpconnectionuid;
  	@Transient
  	private String passive;
  	@Transient
  	private String triggerjobuid;
  	@Transient
  	private String checksumalg;
  	@Transient
  	private Integer txdateendpos;
  	@Transient
  	private String ftpmovedir;
  	@Transient
  	private String datafilecountmode;
  	@Transient
  	private String checksumfe;
  	@Transient
  	private String appendUid;
  	@Transient
  	private String ftpremotedir;
  	@Transient
  	private String checksum;
  	@Transient
  	private String sftp;
  	@Transient
  	private Integer txdatestartpos;
  	@Transient
  	private String ftpbinary;
  	
  	
  	
  	public String getFilesourceuid() {
		return filesourceuid;
	}
	public void setFilesourceuid(String filesourceuid) {
		this.filesourceuid = filesourceuid;
	}
	public String getFilesourcename() {
		return filesourcename;
	}
	public void setFilesourcename(String filesourcename) {
		this.filesourcename = filesourcename;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getLastupdatetime() {
		return lastupdatetime;
	}
	public void setLastupdatetime(Date lastupdatetime) {
		this.lastupdatetime = lastupdatetime;
	}
	public String getFiletrigger() {
		return filetrigger;
	}
	public void setFiletrigger(String filetrigger) {
		this.filetrigger = filetrigger;
	}
	public String getReceivedir() {
		return receivedir;
	}
	public void setReceivedir(String receivedir) {
		this.receivedir = receivedir;
	}
	public String getTargetdir() {
		return targetdir;
	}
	public void setTargetdir(String targetdir) {
		this.targetdir = targetdir;
	}
	public String getCompletedir() {
		return completedir;
	}
	public void setCompletedir(String completedir) {
		this.completedir = completedir;
	}
	public String getCorruptdir() {
		return corruptdir;
	}
	public void setCorruptdir(String corruptdir) {
		this.corruptdir = corruptdir;
	}
	public String getDuplicatedir() {
		return duplicatedir;
	}
	public void setDuplicatedir(String duplicatedir) {
		this.duplicatedir = duplicatedir;
	}
	public String getErrordir() {
		return errordir;
	}
	public void setErrordir(String errordir) {
		this.errordir = errordir;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	public String getFiletype() {
		return filetype;
	}
	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}
	public Integer getEndposition() {
		return endposition;
	}
	public void setEndposition(Integer endposition) {
		this.endposition = endposition;
	}
	public Integer getStartposition() {
		return startposition;
	}
	public void setStartposition(Integer startposition) {
		this.startposition = startposition;
	}
	public Integer getMinfile() {
		return minfile;
	}
	public void setMinfile(Integer minfile) {
		this.minfile = minfile;
	}
	public Integer getMaxfile() {
		return maxfile;
	}
	public void setMaxfile(Integer maxfile) {
		this.maxfile = maxfile;
	}
	public Integer getTimeout() {
		return timeout;
	}
	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}
	public String getCheckduplicate() {
		return checkduplicate;
	}
	public void setCheckduplicate(String checkduplicate) {
		this.checkduplicate = checkduplicate;
	}
	public String getFilterduplicate() {
		return filterduplicate;
	}
	public void setFilterduplicate(String filterduplicate) {
		this.filterduplicate = filterduplicate;
	}
	public String getCheckrow() {
		return checkrow;
	}
	public void setCheckrow(String checkrow) {
		this.checkrow = checkrow;
	}
	public String getBypasszero() {
		return bypasszero;
	}
	public void setBypasszero(String bypasszero) {
		this.bypasszero = bypasszero;
	}
	public String getFtpget() {
		return ftpget;
	}
	public void setFtpget(String ftpget) {
		this.ftpget = ftpget;
	}
	public String getXmldata() {
		return xmldata;
	}
	public void setXmldata(String xmldata) {
		this.xmldata = xmldata;
	}
	
	
	
	public String getFtppostaction() {
		return ftppostaction;
	}
	public void setFtppostaction(String ftppostaction) {
		this.ftppostaction = ftppostaction;
	}
	public String getTxdateformat() {
		return txdateformat;
	}
	public void setTxdateformat(String txdateformat) {
		this.txdateformat = txdateformat;
	}
	public String getRootdir() {
		return rootdir;
	}
	public void setRootdir(String rootdir) {
		this.rootdir = rootdir;
	}
	public String getCfImpClass() {
		return cfImpClass;
	}
	public void setCfImpClass(String cfImpClass) {
		this.cfImpClass = cfImpClass;
	}
	public String getFtpconnectionuid() {
		return ftpconnectionuid;
	}
	public void setFtpconnectionuid(String ftpconnectionuid) {
		this.ftpconnectionuid = ftpconnectionuid;
	}
	public String getPassive() {
		return passive;
	}
	public void setPassive(String passive) {
		this.passive = passive;
	}
	public String getTriggerjobuid() {
		return triggerjobuid;
	}
	public void setTriggerjobuid(String triggerjobuid) {
		this.triggerjobuid = triggerjobuid;
	}
	public String getChecksumalg() {
		return checksumalg;
	}
	public void setChecksumalg(String checksumalg) {
		this.checksumalg = checksumalg;
	}
	public Integer getTxdateendpos() {
		return txdateendpos;
	}
	public void setTxdateendpos(Integer txdateendpos) {
		this.txdateendpos = txdateendpos;
	}
	public String getFtpmovedir() {
		return ftpmovedir;
	}
	public void setFtpmovedir(String ftpmovedir) {
		this.ftpmovedir = ftpmovedir;
	}
	public String getChecksumfe() {
		return checksumfe;
	}
	public void setChecksumfe(String checksumfe) {
		this.checksumfe = checksumfe;
	}
	public String getFtpremotedir() {
		return ftpremotedir;
	}
	public void setFtpremotedir(String ftpremotedir) {
		this.ftpremotedir = ftpremotedir;
	}
	public String getAppendUid() {
		return appendUid;
	}
	public void setAppendUid(String appendUid) {
		this.appendUid = appendUid;
	}
	public String getSftp() {
		return sftp;
	}
	public void setSftp(String sftp) {
		this.sftp = sftp;
	}
	public String getChecksum() {
		return checksum;
	}
	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}
	public Integer getTxdatestartpos() {
		return txdatestartpos;
	}
	public void setTxdatestartpos(Integer txdatestartpos) {
		this.txdatestartpos = txdatestartpos;
	}
	public String getFtpbinary() {
		return ftpbinary;
	}
	public void setFtpbinary(String ftpbinary) {
		this.ftpbinary = ftpbinary;
	}
	public String getDatafilecountmode() {
		return datafilecountmode;
	}
	public void setDatafilecountmode(String datafilecountmode) {
		this.datafilecountmode = datafilecountmode;
	}
}
