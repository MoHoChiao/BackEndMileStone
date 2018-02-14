package com.netpro.trinity.repository.service.configuration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netpro.trinity.repository.dao.jpa.configuration.MonitorconfigJPADao;
import com.netpro.trinity.repository.entity.configuration.MonitorDisk;
import com.netpro.trinity.repository.entity.configuration.jpa.Monitorconfig;
import com.netpro.trinity.repository.util.Item;

@Service
public class MonitorconfigService {
	@Autowired
	private MonitorconfigJPADao dao;
	
	public List<Monitorconfig> getAll() throws Exception{
		List<Monitorconfig> configs = this.dao.findAll();
		setExtraXmlProp(configs);
		return configs;
	}
	
	public Monitorconfig getByUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("Monitor Config UID(Machine UID) can not be empty!");
		
		Monitorconfig config = this.dao.findOne(uid);
		if(config == null)
			throw new IllegalArgumentException("Monitor Config UID(Machine UID) does not exist!(" + uid + ")");

		setExtraXmlProp(config);
		
		return config;
	}
	
	public Monitorconfig edit(Monitorconfig config) throws IllegalArgumentException, Exception{
		String uid = config.getUid();
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("Monitor Config UID(Machine UID) can not be empty!");

		Monitorconfig old_config = this.dao.findOne(uid);
		if(null == old_config)
			throw new IllegalArgumentException("Monitor Config UID(Machine UID) does not exist!(" + uid + ")");
		
		Boolean resourcemonitor = config.getResourcemonitor();
		if(null == resourcemonitor)
			config.setResourcemonitor(false);
		
		Boolean processmonitor = config.getProcessmonitor();
		if(null == processmonitor)
			config.setProcessmonitor(false);
		
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
		
		if(uid.trim().equals("JCSServer")) {
			Boolean SuspendJob = config.getSuspendJob();
			if(null == SuspendJob)
				config.setSuspendJob(false);
		}
		
		String xmlData = parseToItemXml(config);
		config.setXml(xmlData);
		
		/*
		 * because lastupdatetime column is auto created value, it can not be reload new value.
		 * here, we force to give value to lastupdatetime column.
		 */
		config.setLastupdatetime(new Date());
		
		/*
		 * Because All fields associated with xml are defined by @Transient, it can not be reload new value.
		 * The fields associated with xml is very suck design!
		 */
		setExtraXmlProp(config);
		
		return this.dao.save(config);
	}
	
	public boolean existByUid(String uid) throws Exception {
		return this.dao.exists(uid);
	}
	
	private String parseToItemXml(Monitorconfig config) throws Exception{
		Item root = new Item();
		List<Item> rootItemList = new ArrayList<Item>();
		
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
				if ("false".equals(suspendJobItem.getValue())){
					config.setSuspendJob(false);
				} else {
					config.setSuspendJob(Boolean.valueOf(suspendJobItem.getValue()));
				}
			}
		}
		
		config.setXml("");	//不再需要xml欄位的資料, 已經parsing
	}
}
