package com.netpro.trinity.repository.notification.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netpro.trinity.repository.member.entity.Trinityuser;
import com.netpro.trinity.repository.member.entity.Usergroup;
import com.netpro.trinity.repository.member.service.TrinityuserService;
import com.netpro.trinity.repository.member.service.UsergroupService;
import com.netpro.trinity.repository.notification.dao.NotificationListJDBCDao;
import com.netpro.trinity.repository.notification.entity.NotificationList;

@Service
public class NotificationListService {
	private static final Logger LOGGER = LoggerFactory.getLogger(NotificationListService.class);
	
	@Autowired
	private NotificationListJDBCDao dao;
	
	@Autowired
	private NotificationService notifyService;
	@Autowired
	private TrinityuserService userService;
	@Autowired
	private UsergroupService groupService;
	
	public List<NotificationList> getByNotifyUid(String notificationUid) throws IllegalArgumentException, Exception{
		if(notificationUid == null || notificationUid.isEmpty())
			throw new IllegalArgumentException("Notification UID can not be empty!");
				
		return this.dao.findByNotifyUid(notificationUid);
	}
	
	public List<NotificationList> getUserExByNotifyUid(String notificationUid) throws IllegalArgumentException, Exception{
		if(notificationUid == null || notificationUid.isEmpty())
			throw new IllegalArgumentException("Notification UID can not be empty!");
				
		return this.dao.findUserExByNotifyUid(notificationUid);
	}
	
	public List<NotificationList> getGroupExByNotifyUid(String notificationUid) throws IllegalArgumentException, Exception{
		if(notificationUid == null || notificationUid.isEmpty())
			throw new IllegalArgumentException("Notification UID can not be empty!");
				
		return this.dao.findGroupExByNotifyUid(notificationUid);
	}
	
	public NotificationList add(NotificationList list) throws IllegalArgumentException, Exception{
		String notificationuid = list.getNotificationuid();
		if(null == notificationuid)
			throw new IllegalArgumentException("Notification UID can not be empty!");
		
		if(!this.notifyService.existByUid(notificationuid))
			throw new IllegalArgumentException("Notification UID does not exist!(" + notificationuid + ")");
		
		String destinationuid = list.getDestinationuid();
		if(null == destinationuid)
			throw new IllegalArgumentException("Destination UID can not be empty!");
		
		String destinationtype = "";
		try {
			Trinityuser user = userService.getByUid(destinationuid);
			destinationtype = "U";
			list.setDestinationname(user.getUsername());
			list.setDestinationid(user.getUserid());
		}catch(IllegalArgumentException e1) {
			try {
				Usergroup group = groupService.getByUid(destinationuid);
				destinationtype = "G";
				list.setDestinationname(group.getGroupname());
			}catch(IllegalArgumentException e2) {
				throw new IllegalArgumentException("Destinationu Type can only be U(User), G(Group)!");
			}
		}
		
		if(destinationtype.equals(""))
			throw new IllegalArgumentException("Destination UID does not exist!(" + destinationuid + ")");
		
		list.setDestinationtype(destinationtype);
		
		if(this.dao.existByAllPKs(list))
			throw new IllegalArgumentException("Duplicate Notification List!");
		
		String activate = list.getActivate();
		if(null == activate || (!activate.equals("1") && !activate.equals("0")))
			list.setActivate("1");
		
		if(this.dao.save(list) > 0)
			return list;
		else
			throw new IllegalArgumentException("Add Notification List Fail!");
	}
	
	public List<NotificationList> add(String notificationUid, List<NotificationList> lists) throws IllegalArgumentException, Exception{
		List<NotificationList> new_lists = new ArrayList<NotificationList>();
		
		if(null == lists)
			return new_lists;
		
		for(NotificationList list: lists) {
			try {
				list.setNotificationuid(notificationUid);
				this.add(list);
				new_lists.add(list);
			}catch(Exception e) {
				NotificationListService.LOGGER.warn("Warning; reason was:", e);
			}
		}
		return new_lists;
	}
	
	public int[] addBatch(String notificationUid, List<NotificationList> lists) throws IllegalArgumentException, Exception{
		if(null == notificationUid)
			throw new IllegalArgumentException("Notification UID can not be empty!");
		
		if(!this.notifyService.existByUid(notificationUid))
			throw new IllegalArgumentException("Notification UID does not exist!(" + notificationUid + ")");
		
		return this.dao.saveBatch(notificationUid, lists);
	}
	
	public void deleteByNotifyUid(String notificationUid) throws IllegalArgumentException, Exception{
		if(null == notificationUid || notificationUid.trim().length() <= 0)
			throw new IllegalArgumentException("Notification Uid can not be empty!");
		
		this.dao.deleteByNotifyUid(notificationUid);
	}
	
	public void deleteByDestinationUid(String destinationUid) throws IllegalArgumentException, Exception{
		if(null == destinationUid || destinationUid.trim().length() <= 0)
			throw new IllegalArgumentException("Destination Uid can not be empty!");
		
		this.dao.deleteByDestinationUid(destinationUid);
	}
}
