package com.netpro.trinity.service.notification.service;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netpro.trinity.service.job.service.BusentityService;
import com.netpro.trinity.service.job.service.JobFlowService;
import com.netpro.trinity.service.job.service.JobService;
import com.netpro.trinity.service.job.service.JobcategoryService;
import com.netpro.trinity.service.notification.dao.NotificationJPADao;
import com.netpro.trinity.service.notification.entity.Notification;
import com.netpro.trinity.service.notification.entity.NotificationList;
import com.netpro.trinity.service.util.StrUtil;
import com.netpro.trinity.service.util.XMLDataUtility;

@Service
public class NotificationService {
	/*
	 * B:Begin, C:Completed, S:Success, E:Error, T:Timeout, O:Overtime, W:Waiting Long, X:Retry, V:Retry Every Time, R:Has Error Record
	 */
	public static final String[] TIMING_VALUES = new String[] { "B", "C", "S", "E", "T", "O", "W", "X", "V", "R"};
	public static final Set<String> TIMING_SET = new HashSet<>(Arrays.asList(TIMING_VALUES));
	
	@Autowired
	private XMLDataUtility xmlUtil;
	
	@Autowired
	NotificationJPADao dao;
	
	@Autowired
	private NotificationListService listService;
	
	@Autowired
	private JobService jobService;
	@Autowired
	private JobFlowService flowService;
	@Autowired
	private BusentityService entityService;
	@Autowired
	private JobcategoryService categoryService;
	
	public List<Notification> getAll(Boolean withoutDetail) throws Exception{
		List<Notification> notices = this.dao.findAll();
		if(null == withoutDetail || withoutDetail == false) {
			setExtraXmlProp(notices);
			getNotificationList(notices);
		}
			
		return notices;
	}
	
	public Notification getByUid(Boolean withoutDetail, String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.trim().isEmpty())
			throw new IllegalArgumentException("Notification UID can not be empty!");
		
		Notification notice = this.dao.findById(uid).get();
		if(null == notice)
			return new Notification();
		
		if(null == withoutDetail || withoutDetail == false) {
			setExtraXmlProp(notice);
			getNotificationList(notice);
		}
		
		return notice;
	}
	
	public List<Notification> getByName(Boolean withoutDetail, String name) throws IllegalArgumentException, Exception{
		if(name == null || name.trim().isEmpty())
			throw new IllegalArgumentException("Notification Name can not be empty!");
		
		List<Notification> notices = this.dao.findBynotificationname(name.toUpperCase());
		
		if(null == withoutDetail || withoutDetail == false) {
			setExtraXmlProp(notices);
			getNotificationList(notices);
		}
			
		return notices;
	}
	
	public Notification modify(Notification notice) throws IllegalArgumentException, Exception{
		String uid = notice.getNotificationuid();
		if(null == uid || uid.trim().isEmpty())
			return add(notice);
		
		Notification old_notice = this.dao.findById(uid).get();
		if(null == old_notice)
			return add(notice);
		else
			return edit(notice);
	}
	
	public Notification add(Notification notice) throws IllegalArgumentException, Exception{
		String uid = notice.getNotificationuid();
		if(null == uid || !uid.trim().equals("JCSServer"))
			notice.setNotificationuid(UUID.randomUUID().toString());
		uid = notice.getNotificationuid().trim();
		
		if(null == notice.getTargetuid() || notice.getTargetuid().trim().isEmpty())
			throw new IllegalArgumentException("Target UID can not be empty!");
		
		if(uid.equals("JCSServer")) {
			notice.setTargetuid("JCSServer");
			notice.setTargettype("G");
			notice.setNotificationname("");
			notice.setNotificationtiming("B");
			notice.setAttachlog("1");
			notice.setDescription("");
			notice.setSubject("");
			notice.setContent("");
		}else {
			if(null == notice.getTargettype() || notice.getTargettype().trim().equals(""))
				notice.setTargettype("G");
			notice.setTargettype(notice.getTargettype().toUpperCase());
			
			/*
			 * J:Job, G:Category or JCSServer, E:Entity, F:Flow
			 */
			if(notice.getTargettype().equals("J")) {
				if(!jobService.existByUid(notice.getTargetuid()))
					throw new IllegalArgumentException("Target Job UID does not exist!(" + uid + ")");
			}else if(notice.getTargettype().equals("G")) {
				if(!categoryService.existByUid(notice.getTargetuid()))
					throw new IllegalArgumentException("Target Category UID does not exist!(" + uid + ")");
			}else if(notice.getTargettype().equals("E")) {
				if(!entityService.existByUid(notice.getTargetuid()))
					throw new IllegalArgumentException("Target Entity UID does not exist!(" + uid + ")");
			}else if(notice.getTargettype().equals("F")) {
				if(!flowService.existByUid(notice.getTargetuid()))
					throw new IllegalArgumentException("Target Flow UID does not exist!(" + uid + ")");
			}else {
				throw new IllegalArgumentException("Target Type value can only be J(Job), G(Category or JCSServer), "
						+ "E(Entity), F(Flow)!");
			}
			
			if((null == notice.getNotificationname() || notice.getNotificationname().trim().isEmpty()))
				throw new IllegalArgumentException("Notification Name can not be empty!");
			notice.setNotificationname(notice.getNotificationname().toUpperCase());
			
			if(null == notice.getNotificationtiming() || notice.getNotificationtiming().trim().isEmpty())
				notice.setNotificationtiming("B");
			
			if(!TIMING_SET.contains(notice.getNotificationtiming()))
				throw new IllegalArgumentException("Notification Timing value can only be B(When Task Begin), C(When Task Completed), "
						+ "S(When Task Success), E(When Task Error), T(When Task Timeout), O(When Task Overtime), W(When Task Waiting Log), "
						+ "X(When Task Retry), V(When Task Retry Every Time), R(When Task Has Error Record)!");
			
			notice.setNotificationtiming(notice.getNotificationtiming().toUpperCase());
			
			if(null == notice.getAttachlog() || notice.getAttachlog().trim().equals(""))
				notice.setAttachlog("0");
			
			if(null == notice.getDescription())
				notice.setDescription("");
			
			if(null == notice.getSubject() || notice.getSubject().trim().isEmpty())
				notice.setSubject("");
			
			if(null == notice.getContent() || notice.getContent().trim().isEmpty())
				notice.setContent("");
		}
			
		if(this.dao.existByNameAndTargetUid(notice.getNotificationname(), notice.getTargetuid()))
			throw new IllegalArgumentException("Duplicate Notification Name in this target object!");
		
		String notiType = notice.getNotificationtype();
		if(null == notiType || notiType.trim().isEmpty())
			notiType = "M";
		notiType = notiType.toUpperCase();
		
		if(!notiType.equals("M") && !notiType.equals("S") && !notiType.equals("B"))
			throw new IllegalArgumentException("Notification Type value can only be M(E-Mail), S(SMS), "
					+ "B(E-Mail and SMS)!");
		
		notice.setNotificationtype(notiType);
		
		setExtraXmlPropToString(notice);
		
		notice.setLastupdatetime(new Date());
				
		this.dao.save(notice);
		
		setExtraXmlProp(notice);
		
		List<NotificationList> userLists = notice.getUserlist();
		if(null != userLists && userLists.size() > 0) {
			int[] returnValue = this.listService.addBatch(notice.getNotificationuid(), userLists);
			for(int i=0; i<returnValue.length; i++) {//重設list, 只有插入成功的會留下來傳回前端
				if(returnValue[i] == 0) {
					userLists.remove(i);
				}
			}
			notice.setUserlist(userLists);
		}
		List<NotificationList> groupLists = notice.getGrouplist();
		if(null != groupLists && groupLists.size() > 0) {
			int[] returnValue = this.listService.addBatch(notice.getNotificationuid(), groupLists);
			for(int i=0; i<returnValue.length; i++) {//重設list, 只有插入成功的會留下來傳回前端
				if(returnValue[i] == 0) {
					groupLists.remove(i);
				}
			}
			notice.setGrouplist(groupLists);
		}
		
		return notice;
	}
	
	public Notification edit(Notification notice) throws IllegalArgumentException, Exception{
		String uid = notice.getNotificationuid();
		if(null == uid || uid.trim().isEmpty())
			throw new IllegalArgumentException("Notification Uid can not be empty!");
		uid = uid.trim();
		
		Notification old_notice = this.dao.findById(uid).get();
		if(null == old_notice)
			throw new IllegalArgumentException("Notification Uid does not exist!(" + uid + ")");
		
		if(null == notice.getTargetuid() || notice.getTargetuid().trim().isEmpty())
			throw new IllegalArgumentException("Target UID can not be empty!");
		
		if(uid.equals("JCSServer")) {
			notice.setTargetuid("JCSServer");
			notice.setTargettype("G");
			notice.setNotificationname("");
			notice.setNotificationtiming("B");
			notice.setAttachlog("1");
			notice.setDescription("");
			notice.setSubject("");
			notice.setContent("");
		}else {
			if(null == notice.getTargettype() || notice.getTargettype().trim().equals(""))
				notice.setTargettype("G");
			notice.setTargettype(notice.getTargettype().toUpperCase());
			
			/*
			 * J:Job, G:Category or JCSServer, E:Entity, F:Flow
			 */
			if(notice.getTargettype().equals("J")) {
				if(!jobService.existByUid(notice.getTargetuid()))
					throw new IllegalArgumentException("Target Job UID does not exist!(" + uid + ")");
			}else if(notice.getTargettype().equals("G")) {
				if(!categoryService.existByUid(notice.getTargetuid()))
					throw new IllegalArgumentException("Target Category UID does not exist!(" + uid + ")");
			}else if(notice.getTargettype().equals("E")) {
				if(!entityService.existByUid(notice.getTargetuid()))
					throw new IllegalArgumentException("Target Entity UID does not exist!(" + uid + ")");
			}else if(notice.getTargettype().equals("F")) {
				if(!flowService.existByUid(notice.getTargetuid()))
					throw new IllegalArgumentException("Target Flow UID does not exist!(" + uid + ")");
			}else {
				throw new IllegalArgumentException("Target Type value can only be J(Job), G(Category or JCSServer), "
						+ "E(Entity), F(Flow)!");
			}
			
			if((null == notice.getNotificationname() || notice.getNotificationname().trim().isEmpty()))
				throw new IllegalArgumentException("Notification Name can not be empty!");
			notice.setNotificationname(notice.getNotificationname().toUpperCase());
			
			if(null == notice.getNotificationtiming() || notice.getNotificationtiming().trim().isEmpty())
				notice.setNotificationtiming("B");
			notice.setNotificationtiming(notice.getNotificationtiming().toUpperCase());

			if(!TIMING_SET.contains(notice.getNotificationtiming()))
				throw new IllegalArgumentException("Notification Timing value can only be B(When Task Begin), C(When Task Completed), "
						+ "S(When Task Success), E(When Task Error), T(When Task Timeout), O(When Task Overtime), W(When Task Waiting Log), "
						+ "X(When Task Retry), V(When Task Retry Every Time), R(When Task Has Error Record)!");
			
			notice.setNotificationtiming(notice.getNotificationtiming());
			
			if(null == notice.getAttachlog() || notice.getAttachlog().trim().equals(""))
				notice.setAttachlog("0");
			
			if(null == notice.getDescription())
				notice.setDescription("");
			
			if(null == notice.getSubject() || notice.getSubject().trim().isEmpty())
				notice.setSubject("");
			
			if(null == notice.getContent() || notice.getContent().trim().isEmpty())
				notice.setContent("");
		}
		
		System.out.println(old_notice.getNotificationname() + ":" + notice.getNotificationname());
		if(this.dao.existByNameAndTargetUid(notice.getNotificationname(), notice.getTargetuid()) && 
				!old_notice.getNotificationname().equalsIgnoreCase(notice.getNotificationname()))
			throw new IllegalArgumentException("Duplicate Notification Name in this target object!");
		
		if(null == notice.getNotificationtype() || notice.getNotificationtype().trim().isEmpty())
			notice.setNotificationtype("M");
		notice.setNotificationtype(notice.getNotificationtype().toUpperCase());
		
		if(!notice.getNotificationtype().equals("M") && !notice.getNotificationtype().equals("S") && !notice.getNotificationtype().equals("B"))
			throw new IllegalArgumentException("Notification Type value can only be M(E-Mail), S(SMS), "
					+ "B(E-Mail and SMS)!");
		
		setExtraXmlPropToString(notice);
		
		notice.setLastupdatetime(new Date());
		
		this.dao.save(notice);
		
		setExtraXmlProp(notice);
		
		this.listService.deleteByNotifyUid(uid);
		List<NotificationList> userLists = notice.getUserlist();
		if(null != userLists && userLists.size() > 0) {
			int[] returnValue = this.listService.addBatch(notice.getNotificationuid(), userLists);
			for(int i=0; i<returnValue.length; i++) {//重設list, 只有插入成功的會留下來傳回前端
				if(returnValue[i] == 0) {
					userLists.remove(i);
				}
			}
			notice.setUserlist(userLists);
		}
		List<NotificationList> groupLists = notice.getGrouplist();
		if(null != groupLists && groupLists.size() > 0) {
			int[] returnValue = this.listService.addBatch(notice.getNotificationuid(), groupLists);
			for(int i=0; i<returnValue.length; i++) {//重設list, 只有插入成功的會留下來傳回前端
				if(returnValue[i] == 0) {
					groupLists.remove(i);
				}
			}
			notice.setGrouplist(groupLists);
		}
		return notice;
	}
	
	public void deleteByUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("Frequency Uid can not be empty!");
		
		this.listService.deleteByNotifyUid(uid);
		this.dao.deleteById(uid);
	}
	
	public boolean existByUid(String uid) throws Exception {
		return this.dao.existsById(uid);
	}
	
	private void getNotificationList(List<Notification> notices) throws Exception {
		for(Notification notice : notices) {
			getNotificationList(notice);
		}
	}
	
	private void getNotificationList(Notification notice) throws Exception {
		notice.setUserlist(this.listService.getUserExByNotifyUid(notice.getNotificationuid()));
		notice.setGrouplist(this.listService.getGroupExByNotifyUid(notice.getNotificationuid()));
	}
	
	private void setExtraXmlProp(List<Notification> notices) throws Exception{
		for(Notification notice : notices) {
			setExtraXmlProp(notice);
		}
	}
	
	private void setExtraXmlProp(Notification notice) throws Exception{
		HashMap<String, String> map = xmlUtil.parseXMLDataToHashMap(notice.getXmldata());
		if(map != null) {
			notice.setSubject(map.get("subject"));
			notice.setContent(map.get("content"));
			
			notice.setXmldata("");	//不再需要xml欄位的資料, 已經parsing
		}
	}
	
	private void setExtraXmlPropToString(Notification notice) throws Exception{
		notice.setContent(StrUtil.outputXMLText(notice.getContent()));
		Map<String, String> xmlMap = new HashMap<String, String>();
		xmlMap.put("subject", notice.getSubject());
		xmlMap.put("content", notice.getContent());
		String xmldata = xmlUtil.parseHashMapToXMLString(xmlMap, false);
		notice.setXmldata(xmldata);
	}
}
