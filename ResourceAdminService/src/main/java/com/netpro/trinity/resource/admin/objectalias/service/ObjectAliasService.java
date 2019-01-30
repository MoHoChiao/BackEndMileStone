package com.netpro.trinity.resource.admin.objectalias.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netpro.trinity.resource.admin.agent.entity.JCSAgent;
import com.netpro.trinity.resource.admin.agent.entity.VRAgent;
import com.netpro.trinity.resource.admin.agent.service.JCSAgentService;
import com.netpro.trinity.resource.admin.agent.service.VRAgentService;
import com.netpro.trinity.resource.admin.authz.service.AuthzService;
import com.netpro.trinity.resource.admin.connection.entity.Connection;
import com.netpro.trinity.resource.admin.connection.service.ConnectionService;
import com.netpro.trinity.resource.admin.domain.entity.Domain;
import com.netpro.trinity.resource.admin.domain.service.DomainService;
import com.netpro.trinity.resource.admin.filesource.entity.FileSource;
import com.netpro.trinity.resource.admin.filesource.service.FileSourceService;
import com.netpro.trinity.resource.admin.frequency.entity.Frequency;
import com.netpro.trinity.resource.admin.frequency.service.FrequencyService;
import com.netpro.trinity.resource.admin.job.service.BusentityService;
import com.netpro.trinity.resource.admin.objectalias.dao.ObjectAliasJDBCDao;
import com.netpro.trinity.resource.admin.objectalias.entity.ObjectAlias;

@Service
public class ObjectAliasService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ObjectAliasService.class);
	
	@Autowired
	private ObjectAliasJDBCDao dao;
	
	@Autowired
	private BusentityService entityService;
	@Autowired
	private ConnectionService connService;
	@Autowired
	private FrequencyService freqService;
	@Autowired
	private FileSourceService fsService;
	@Autowired
	private DomainService domainService;
	@Autowired
	private JCSAgentService agentService;
	@Autowired
	private VRAgentService vragentService;
	
	@Autowired
	private AuthzService authzService;
	
	public List<ObjectAlias> getExByParentUid(String parentUid) throws IllegalArgumentException, Exception{
		if(parentUid == null || parentUid.isEmpty())
			throw new IllegalArgumentException("Parent/Entity UID can not be empty!");
		
		List<ObjectAlias> lists = this.dao.findByParentUid(parentUid);
		for(ObjectAlias list: lists) {
			list.setObjectname(getObjectNameByObjectUidAndType(list.getObjectuid(), list.getAliastype()));
		}
		return lists;
	}
	
	public List<ObjectAlias> getByParentUid(String parentUid) throws IllegalArgumentException, Exception{
		if(parentUid == null || parentUid.isEmpty())
			throw new IllegalArgumentException("Parent/Entity UID can not be empty!");
		
		return this.dao.findByParentUid(parentUid);
	}
	
	public ObjectAlias add(ObjectAlias list) throws IllegalArgumentException, Exception{
		String parentuid = list.getParentuid();
		if(null == parentuid || parentuid.trim().isEmpty())
			throw new IllegalArgumentException("Parent/Entity UID can not be empty!");
		
		if(!this.entityService.existByUid(parentuid) && !parentuid.trim().equals("global"))
			throw new IllegalArgumentException("Parent/Entity UID does not exist!(" + parentuid + ")");
		
		String aliasName = list.getAliasname();
		if(null == aliasName || aliasName.trim().isEmpty())
			throw new IllegalArgumentException("Alias Name can not be empty!");
		
		if(aliasName.indexOf("$") != 0)
			aliasName = "$" + aliasName;
		if(aliasName.length() > 32)
			throw new IllegalArgumentException("Alias Name can not be more than 32 chars!(" + aliasName + ")");
		list.setAliasname(aliasName.toUpperCase());
		
		if(this.dao.existByPKs(parentuid, aliasName))
			throw new IllegalArgumentException("Duplicate Object Alias!");
		
		String aliasType = list.getAliastype();
		if(aliasType == null || aliasType.isEmpty())
			throw new IllegalArgumentException("Alias Type can not be empty!");
		
		if(!aliasType.equals("Connection") && !aliasType.equals("Agent") && !aliasType.equals("Filesource") && 
				!aliasType.equals("Frequency") && !aliasType.equals("Domain"))
			throw new IllegalArgumentException("Alias Type value can only be Connection, Agent, Filesource, Frequency, or Domain!");
		
		String objectuid = list.getObjectuid();
		if(null == objectuid || objectuid.trim().isEmpty())
			throw new IllegalArgumentException("Object UID can not be empty!");
		
		if(!existByObjectUidAndType(objectuid, aliasType))
			throw new IllegalArgumentException("Object UID does not exist!(" + aliasType + ":" + objectuid + ")");
		
		if(null == list.getDescription())
			list.setDescription("");
		
		if(this.dao.save(list) > 0)
			return list;
		else
			throw new IllegalArgumentException("Add Object Alias Fail!");
	}
	
	public ObjectAlias update(ObjectAlias list) throws IllegalArgumentException, Exception{
		String parentuid = list.getParentuid();
		if(null == parentuid || parentuid.trim().isEmpty())
			throw new IllegalArgumentException("Parent/Entity UID can not be empty!");
		
		String aliasName = list.getAliasname();
		if(null == aliasName || aliasName.trim().isEmpty())
			throw new IllegalArgumentException("Alias Name can not be empty!");
		
		String aliasType = list.getAliastype();
		if(aliasType == null || aliasType.isEmpty())
			throw new IllegalArgumentException("Alias Type can not be empty!");
		
		if(!aliasType.equals("Connection") && !aliasType.equals("Agent") && !aliasType.equals("Filesource") && 
				!aliasType.equals("Frequency") && !aliasType.equals("Domain"))
			throw new IllegalArgumentException("Alias Type value can only be Connection, Agent, Filesource, Frequency, or Domain!");
		
		String objectuid = list.getObjectuid();
		if(null == objectuid || objectuid.trim().isEmpty())
			throw new IllegalArgumentException("Object UID can not be empty!");
		
		if(!existByObjectUidAndType(objectuid, aliasType))
			throw new IllegalArgumentException("Object UID does not exist!(" + aliasType + ":" + objectuid + ")");
		
		if(null == list.getDescription())
			list.setDescription("");
		
		if(this.dao.update(list) > 0)
			return list;
		else
			throw new IllegalArgumentException("Update Object Alias Fail!");
	}
	
	public List<ObjectAlias> modify(String parentUid, List<ObjectAlias> lists) throws IllegalArgumentException, Exception{
		List<ObjectAlias> new_lists = new ArrayList<ObjectAlias>();
		if(null == lists)
			return new_lists;
		
		Map<String, ObjectAlias> oldAliasMap = new HashMap<String, ObjectAlias>();
		Map<String, ObjectAlias> newAliasMap = new HashMap<String, ObjectAlias>();		
		
		List<ObjectAlias> oldAliasList = this.dao.findByParentUid(parentUid);
		for(ObjectAlias alias : oldAliasList) {
			oldAliasMap.put(alias.getAliasname().trim().toUpperCase(), alias);
		}
		
		for(ObjectAlias alias : lists) {
			newAliasMap.put(alias.getAliasname().trim().toUpperCase(), alias);
			
			ObjectAlias oldAlias = oldAliasMap.get(alias.getAliasname().trim().toUpperCase());
			if(oldAlias == null) {
				try {
					alias.setParentuid(parentUid);
					this.add(alias);
					new_lists.add(alias);
				}catch(Exception e) {
					ObjectAliasService.LOGGER.warn("Warning; reason was:", e);
				}
			}else {
				try {
					String aliasType = alias.getAliastype();
					String objectUid = alias.getObjectuid();
					String description = alias.getDescription();
					String oldAliasType = oldAlias.getAliastype();
					String oldObjectUid = oldAlias.getObjectuid();
					String oldDescription = oldAlias.getDescription();
					if(!aliasType.equals(oldAliasType) || !objectUid.equals(oldObjectUid) || !description.equals(oldDescription)) {
						alias.setParentuid(parentUid);
						this.update(alias);
						new_lists.add(alias);
					}else {
						new_lists.add(alias);
					}
				}catch(Exception e) {
					ObjectAliasService.LOGGER.warn("Warning; reason was:", e);
				}
			}
		}
		
		for(ObjectAlias alias : oldAliasList) {
			if(null == newAliasMap.get(alias.getAliasname().trim().toUpperCase()) ) {
				this.deleteByPKs(parentUid, alias.getAliasname().trim().toUpperCase());
			}
		}	
		
		return new_lists;
	}
	
	public void deleteByParentUid(String parentUid) throws IllegalArgumentException, Exception{
		if(null == parentUid || parentUid.trim().length() <= 0)
			throw new IllegalArgumentException("Parent/Entity UID can not be empty!");
		
		this.authzService.deleteByAliasParentUid(parentUid);
		this.dao.deleteByParentUid(parentUid);
	}
	
	public void deleteByPKs(String parentUid, String aliasName) throws IllegalArgumentException, Exception{
		if(null == parentUid || parentUid.trim().length() <= 0)
			throw new IllegalArgumentException("Parent/Entity UID can not be empty!");
		
		if(null == aliasName || aliasName.trim().length() <= 0)
			throw new IllegalArgumentException("Alias Name can not be empty!");
		
		this.authzService.deleteByObjectUid(parentUid+"$"+aliasName);
		this.dao.deleteByPKs(parentUid, aliasName);
	}
	
	public boolean existByObjectuid(String objectUid) throws IllegalArgumentException, Exception {
		if(null == objectUid || objectUid.trim().length() <= 0)
			throw new IllegalArgumentException("Object Uid can not be empty!");
		
		return this.dao.existByObjectUid(objectUid);
	}
	
	public boolean existByPKs(String parentUid, String aliasName) throws IllegalArgumentException, Exception {
		if(null == parentUid || parentUid.trim().length() <= 0)
			throw new IllegalArgumentException("Parent Uid can not be empty!");
		
		if(null == aliasName || aliasName.trim().length() <= 0)
			throw new IllegalArgumentException("Alias Name can not be empty!");
		
		if(aliasName.indexOf("$") != 0)
			aliasName = "$" + aliasName;
		
		return this.dao.existByPKs(parentUid, aliasName);
	}
	
	public boolean existByObjectUidAndType(String objectUid, String aliasType) throws Exception{
		try {
			if(objectUid == null || objectUid.isEmpty())
				throw new IllegalArgumentException("Object UID can not be empty!");
			
			if(aliasType == null || aliasType.isEmpty())
				throw new IllegalArgumentException("Alias Type can not be empty!");
			
			if(!aliasType.equals("Connection") && !aliasType.equals("Agent") && !aliasType.equals("Filesource") && 
					!aliasType.equals("Frequency") && !aliasType.equals("Domain"))
				throw new IllegalArgumentException("Alias Type value can only be Connection, Agent, Filesource, Frequency, or Domain!");
			
			if(aliasType.equals("Connection")){
				return connService.existByUid(objectUid);
			}else if(aliasType.equals("Domain")){
				return domainService.existByUid(objectUid);
			}else if(aliasType.equals("Agent")){
				if(!agentService.existByUid(objectUid)){
					return vragentService.existByUid(objectUid);
				}else {
					return true;
				}
			}else if(aliasType.equals("Frequency")){
				return freqService.existByUid(objectUid);
			}else if(aliasType.equals("Filesource")){
				return fsService.existByUid(objectUid);
			}else{
				return false;
			}
		}catch(Exception e) {
			ObjectAliasService.LOGGER.warn("Warning; reason was:", e);
			return false;
		}
	}
	
	public String getObjectNameByObjectUidAndType(String objectUid, String aliasType) {
		try {
			if(objectUid == null || objectUid.isEmpty())
				throw new IllegalArgumentException("Object UID can not be empty!");
			
			if(aliasType == null || aliasType.isEmpty())
				throw new IllegalArgumentException("Alias Type can not be empty!");
			
			if(!aliasType.equals("Connection") && !aliasType.equals("Agent") && !aliasType.equals("Filesource") && 
					!aliasType.equals("Frequency") && !aliasType.equals("Domain"))
				throw new IllegalArgumentException("Alias Type value can only be Connection, Agent, Filesource, Frequency, or Domain!");
			
			if(aliasType.equals("Connection")){
				Connection conn = connService.getByUid(objectUid);
				return conn.getConnectionname();
			}else if(aliasType.equals("Domain")){
				Domain domain = domainService.getByUid(true, objectUid);
				return domain.getName();
			}else if(aliasType.equals("Agent")){
				try {
					JCSAgent agent = agentService.getByUid(objectUid);
					return agent.getAgentname();
				}catch(Exception e) {
					VRAgent vragent = vragentService.getByUid(true, objectUid);
					return vragent.getVirtualagentname() + "(Virtual)";
				}
			}else if(aliasType.equals("Frequency")){
				Frequency freq = freqService.getByUid(objectUid);
				if(null == freq)
					return "";
				else
					return freq.getFrequencyname();
			}else if(aliasType.equals("Filesource")){
				FileSource fs = fsService.getByUid(objectUid);
				if(null == fs)
					return "";
				else
					return fs.getFilesourcename();
			}else{
				return "";
			}
		}catch(Exception e) {
			ObjectAliasService.LOGGER.warn("Warning; reason was:", e);
			return "";
		}
	}
	
	public String checkIsRefBy(String parentUid, String aliasType, String aliasName) {
		return dao.checkIsRefBy(parentUid, aliasType, aliasName);
	}
	
}
