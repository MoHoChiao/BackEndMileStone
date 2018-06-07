package com.netpro.trinity.repository.configuration.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netpro.trinity.repository.agent.service.JCSAgentService;
import com.netpro.trinity.repository.configuration.dao.MonitorconfigJPADao;
import com.netpro.trinity.repository.configuration.entity.MonitorDisk;
import com.netpro.trinity.repository.configuration.entity.Monitorconfig;
import com.netpro.trinity.repository.util.Item;

@Service
public class MonitorconfigService {
	private static final Logger LOGGER = LoggerFactory.getLogger(MonitorconfigService.class);
	
	@Autowired
	private MonitorconfigJPADao dao;
	
	@Autowired
	private JCSAgentService agentService;
	
	public List<Monitorconfig> getAll() throws Exception{
		List<Monitorconfig> configs = this.dao.findAll();
		setExtraXmlProp(configs);
		return configs;
	}
	
	public Monitorconfig getByUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("Monitor Config UID(Machine UID) can not be empty!");
		
		Monitorconfig config = this.dao.findOne(uid);
		if(null == config) {
			config = new Monitorconfig();
			config.setResourcemonitor(false);
			config.setProcessmonitor(false);
			if(uid.trim().equals("JCSServer"))
				config.setSuspendJob(false);
			config.setUid(uid);
			config.setCpu(0);
			config.setMemory(0);
			config.setDisk(new ArrayList<MonitorDisk>());
		}else {
			setExtraXmlProp(config);
		}

		return config;
	}
	
	public Monitorconfig modify(Monitorconfig config) throws IllegalArgumentException, Exception{
		
		Monitorconfig new_config = this.dao.save(checkConfigData(config));
		
		/*
		 * Because All fields associated with xml are defined by @Transient, it can not be reload new value.
		 * The fields associated with xml is very suck design!
		 */
		setExtraXmlProp(new_config);
		
		return new_config;
	}
	
	public List<Monitorconfig> modify(List<Monitorconfig> configs) throws IllegalArgumentException, Exception{
		List<Monitorconfig> new_configs = new ArrayList<Monitorconfig>();
		
		if(null == configs)
			return new_configs;
		
		for(Monitorconfig config: configs) {
			try {
				Monitorconfig new_config = checkConfigData(config);
				new_configs.add(new_config);
			}catch(Exception e) {
				MonitorconfigService.LOGGER.warn("Warning; reason was:", e);
			}
		}
		this.dao.save(new_configs);
		setExtraXmlProp(new_configs);
		return new_configs;
	}
	
	public boolean existByUid(String uid) throws Exception {
		return this.dao.exists(uid);
	}
	
	private Monitorconfig checkConfigData(Monitorconfig config) throws Exception {
		String uid = config.getUid();
		if(null == uid || uid.trim().isEmpty())
			throw new IllegalArgumentException("Monitor Config UID(Machine UID) can not be empty!");
		
		Boolean resourcemonitor = config.getResourcemonitor();
		if(null == resourcemonitor)
			config.setResourcemonitor(false);
		
		Boolean processmonitor = config.getProcessmonitor();
		if(null == processmonitor)
			config.setProcessmonitor(false);
		
		if(uid.trim().equals("JCSServer")) {
			config.setProcessmonitor(false);
			
			Boolean SuspendJob = config.getSuspendJob();
			if(null == SuspendJob)
				config.setSuspendJob(false);
		}else {
			if(!agentService.existByUid(uid))
				throw new IllegalArgumentException("Monitor Config UID(Machine UID) does not exist!(" + uid + ")");
		}
		
		if(resourcemonitor) {
			Integer cpu = config.getCpu();
			if(null == cpu || cpu < 0)
				config.setCpu(0);
			
			Integer memory = config.getMemory();
			if(null == memory || memory < 0)
				config.setMemory(0);
			
			List<MonitorDisk> disks = config.getDisk();
			if(null == disks)
				config.setDisk(new ArrayList<MonitorDisk>());
		}else {
			config.setCpu(0);
			config.setMemory(0);
			config.setDisk(new ArrayList<MonitorDisk>());
		}
		
		String xmlData = parseToItemXml(config);
		config.setXml(xmlData);

		/*
		 * because lastupdatetime column is auto created value, it can not be reload new value.
		 * here, we force to give value to lastupdatetime column.
		 */
		config.setLastupdatetime(new Date());
		
		return config;
	}
	
	private String parseToItemXml(Monitorconfig config) throws Exception{
		Item root = new Item();
		List<Item> rootItemList = new ArrayList<Item>();
		
		if(config.getUid().trim().equals("JCSServer")) {
			Item suspendItem = new Item();
			suspendItem.setName("SuspendJob");
			suspendItem.setValue(String.valueOf(config.getSuspendJob()));
			rootItemList.add(suspendItem);
		}
		
		Item cpuItem = new Item();
		cpuItem.setName("cpu");
		
		Item memoryItem = new Item();
		memoryItem.setName("memory");
		
		if (config.getCpu() > 0){
			cpuItem.setValue(String.valueOf(config.getCpu()));
		} else {
			cpuItem.setValue("0");
		}
		
		if (config.getMemory() > 0){
			memoryItem.setValue(String.valueOf(config.getMemory()));
		} else {
			memoryItem.setValue("0");
		}
		
		rootItemList.add(cpuItem);
		rootItemList.add(memoryItem);
		
		if (config.getDisk().size() > 0){
			Item diskItem = new Item();
			diskItem.setName("disk");
			
			List<Item> pathList = new ArrayList<Item>();
			
			for (MonitorDisk disk : config.getDisk()){
				if(null == disk.getPath())
					disk.setPath("");
				
				if(null == disk.getValue() || disk.getValue() < 0)
					disk.setValue(0);
				
				Item pathItem = new Item();
				pathItem.setName("path");
				
				if (disk.getValue() > 0){
					pathItem.setValue(String.valueOf(disk.getValue()));
					pathItem.setType(disk.getPath());
				} else {
					pathItem.setValue("0");
					pathItem.setType(disk.getPath());
				}
				
				pathList.add(pathItem);
			}
			diskItem.setItems(pathList);
			rootItemList.add(diskItem);
		}
		
		root.setItems(rootItemList);
		
		return root.toString();
	}
	
	private void setExtraXmlProp(List<Monitorconfig> configs) throws Exception{
		for(Monitorconfig config : configs) {
			setExtraXmlProp(config);
		}
	}
	
	private void setExtraXmlProp(Monitorconfig config) throws Exception{
		String uid = config.getUid();
		String xmlData = config.getXml();
		if(!config.getResourcemonitor() || xmlData == null || xmlData.trim().length() == 0) {
			config.setCpu(0);
			config.setMemory(0);
			config.setDisk(new ArrayList<MonitorDisk>());
		}else {
			Item root = Item.valueOf(config.getXml());
			Item cpuItem = root.getChild("cpu");
			Item memoryItem = root.getChild("memory");
			Item diskItem = root.getChild("disk");
			Item suspendJobItem = root.getChild("SuspendJob");
			
			if ("0".equals(cpuItem.getValue())){
				config.setCpu(0);
			} else {
				config.setCpu(Integer.valueOf(cpuItem.getValue()));
			}
			
			if ("0".equals(memoryItem.getValue())){
				config.setMemory(0);
			} else {
				config.setMemory(Integer.valueOf(memoryItem.getValue()));
			}
			
			if (diskItem != null){
				if (diskItem.getChildren("path").size() == 0){
					config.setDisk(new ArrayList<MonitorDisk>());
				} else {
					List<MonitorDisk> disks = new ArrayList<MonitorDisk>();
					List<Item> pathItemList = diskItem.getChildren("path");
					for(Item pathItem : pathItemList){
						MonitorDisk disk = new MonitorDisk();
						Integer value = Integer.valueOf(pathItem.getValue());
						String path = pathItem.getType();
						disk.setPath(path);
						disk.setValue(value);
						disks.add(disk);
					}
					config.setDisk(disks);
				}
			} else {
				config.setDisk(new ArrayList<MonitorDisk>());
			}
			
			if(uid.trim().equals("JCSServer")) {
				if (null == suspendJobItem || "false".equals(suspendJobItem.getValue())){
					config.setSuspendJob(false);
				} else {
					config.setSuspendJob(Boolean.valueOf(suspendJobItem.getValue()));
				}
			}
		}
		
		config.setXml("");	//不再需要xml欄位的資料, 已經parsing
	}
}
