package com.netpro.trinity.repository.service.member;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.netpro.ac.ACException;
import com.netpro.ac.DefaultCredentialsService;
import com.netpro.ac.dao.JdbcDaoFactoryImpl;
import com.netpro.ac.dto.AccessDetailsDto;
import com.netpro.ac.util.CookieUtils;
import com.netpro.ac.util.TrinityWebV2Utils;
import com.netpro.trinity.repository.dao.jpa.member.TrinityuserJPADao;
import com.netpro.trinity.repository.dto.filter.FilterInfo;
import com.netpro.trinity.repository.dto.filter.Ordering;
import com.netpro.trinity.repository.dto.filter.Paging;
import com.netpro.trinity.repository.dto.filter.Querying;
import com.netpro.trinity.repository.entity.member.jpa.Trinityuser;
import com.netpro.trinity.repository.service.notification.NotificationListService;
import com.netpro.trinity.repository.service.permission.AccessRightService;
import com.netpro.trinity.repository.util.Constant;
import com.netpro.trinity.repository.util.Crypto;
import com.netpro.trinity.repository.util.NetworkUtil;
import com.netpro.trinity.repository.util.XMLDataUtility;

@Service
public class TrinityuserService {
	private static final Logger LOGGER = LoggerFactory.getLogger(TrinityuserService.class);
	
	public static final String[] USER_FIELD_VALUES = new String[] { "username", "activate", "usertype", "description"};
	public static final Set<String> USER_FIELD_SET = new HashSet<>(Arrays.asList(USER_FIELD_VALUES));
	
	@Autowired
	private TrinityuserJPADao dao;
	
	@Autowired
	private GroupMemberService g_memberService;
	@Autowired
	private RoleMemberService r_memberService;
	@Autowired
	private NotificationListService n_listService;
	@Autowired
	private AccessRightService accessService;
	@Autowired	//自動注入DataSource物件
	private DataSource dataSource;
	@Autowired
	private XMLDataUtility xmlUtil;
	
	@Value("${encrypt.key}")
	private String encryptKey;
	
	private DefaultCredentialsService credentialsService;
	
	@Autowired
	private void setCredentialsService() {
		try {
			this.credentialsService = new DefaultCredentialsService(new JdbcDaoFactoryImpl(dataSource.getConnection()));
		} catch (SQLException e) {
			TrinityuserService.LOGGER.error("SQLException; reason was:", e);
		}
	}
	
	public List<Trinityuser> getAll() throws Exception{
		List<Trinityuser> users = this.dao.findAll();
		setMailsAndAC(users);
		
		return users;
	}
	
	public Trinityuser getByUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.isEmpty())
			throw new IllegalArgumentException("Trinity User UID can not be empty!");
		
		Trinityuser user = this.dao.findOne(uid);
		if(null == user)
			throw new IllegalArgumentException("Trinity User UID does not exist!(" + uid + ")");
		
		setMailsAndAC(user);
		
		return user;
	}
	
	public List<Trinityuser> getByName(String name) throws IllegalArgumentException, Exception{
		if(name == null || name.trim().isEmpty())
			throw new IllegalArgumentException("Trinity User Name can not be empty!");
		
		List<Trinityuser> users = this.dao.findByusername(name);
		setMailsAndAC(users);
		
		return users;
	}
	
	public List<Trinityuser> getByUserType(String userType) throws IllegalArgumentException, Exception{
		if(userType == null || userType.trim().isEmpty())
			throw new IllegalArgumentException("Trinity User Type can not be empty!");
		
		List<Trinityuser> users = this.dao.findByusertype(userType.toUpperCase());
		setMailsAndAC(users);
		
		return users;
	}
	
	@SuppressWarnings("unchecked")
	public ResponseEntity<?> getByFilter(FilterInfo filter) throws SecurityException, NoSuchMethodException, 
								IllegalArgumentException, IllegalAccessException, InvocationTargetException, Exception{
		if(null == filter) {
			List<Trinityuser> users = this.dao.findAll();
			setMailsAndAC(users);
			return ResponseEntity.ok(users);
		}
			
		Paging paging = filter.getPaging();
		Ordering ordering = filter.getOrdering();
		Querying querying = filter.getQuerying();
		
		if(null == paging && null == ordering && null == querying) {
			List<Trinityuser> users = this.dao.findAll();
			setMailsAndAC(users);
			return ResponseEntity.ok(users);
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
		
		if(null == querying) {
			if(pageRequest != null) {
				Page<Trinityuser> page_user = this.dao.findAll(pageRequest);
				setMailsAndAC(page_user.getContent());
				return ResponseEntity.ok(page_user);
			}else if(sort != null) {
				List<Trinityuser> users = this.dao.findAll(sort);
				setMailsAndAC(users);
				return ResponseEntity.ok(users);
			}else {
				/*
				 * The paging and ordering both objects are null.
				 * it means pageRequest and sort must be null too.
				 * then return default
				 */
				List<Trinityuser> users = this.dao.findAll(sort);
				setMailsAndAC(users);
				return ResponseEntity.ok(users);
			}
		}else {
			if(null == querying.getQueryType() || !Constant.QUERY_TYPE_SET.contains(querying.getQueryType().toLowerCase()))
				throw new IllegalArgumentException("Illegal query type! "+Constant.QUERY_TYPE_SET.toString());
			if(null == querying.getQueryField() || !USER_FIELD_SET.contains(querying.getQueryField().toLowerCase()))
				throw new IllegalArgumentException("Illegal query field! "+ USER_FIELD_SET.toString());
			if(null == querying.getIgnoreCase())
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

			Method method = null;
			if(pageRequest != null){
				method = this.dao.getClass().getMethod(methodName.toString(), String.class, Pageable.class);
				Page<Trinityuser> page_user = (Page<Trinityuser>) method.invoke(this.dao, queryString, pageRequest);
				setMailsAndAC(page_user.getContent());
				return ResponseEntity.ok(page_user);
			}else if(sort != null) {
				method = this.dao.getClass().getMethod(methodName.toString(), String.class, Sort.class);
				List<Trinityuser> users = (List<Trinityuser>) method.invoke(this.dao, queryString, sort);
				setMailsAndAC(users);
				return ResponseEntity.ok(users);
			}else {
				method = this.dao.getClass().getMethod(methodName.toString(), String.class);
				List<Trinityuser> users = (List<Trinityuser>) method.invoke(this.dao, queryString);
				setMailsAndAC(users);
				return ResponseEntity.ok(users);
			}
		}
	}
	
	public Trinityuser add(HttpServletRequest request, Trinityuser user) throws ACException, SQLException, IllegalArgumentException, Exception{
		user.setUseruid(UUID.randomUUID().toString());
		
		String userid = user.getUserid();
		if(null == userid || userid.trim().isEmpty())
			throw new IllegalArgumentException("Trinity User Id can not be empty!");
		
		if(this.dao.existByID(userid))
			throw new IllegalArgumentException("Duplicate Trinity User ID!");
		
		String username = user.getUsername();
		if(null == username || username.trim().isEmpty())
			throw new IllegalArgumentException("Trinity User Name can not be empty!");
		
		String activate = user.getActivate();
		if(null == activate || (!activate.equals("1") && !activate.equals("0")))
			throw new Exception("Trinity User activate value can only be 1 or 0!");
		
		String defaultlang = user.getDefaultlang();
		if(null == defaultlang || (!defaultlang.equals("en_US") && !defaultlang.equals("zh_TW") && !defaultlang.equals("zh_CN")))
			user.setDefaultlang("en_US");
		
		if(null == user.getDescription())
			user.setDescription("");
		
		if(null == user.getEmail())
			user.setEmail("");
		
		String xmldata = "";
		if(null != user.getEmail() && !user.getEmail().trim().isEmpty()) {
			String[] mails = user.getEmail().split(";");
			if(mails.length > 1) {
				user.setEmail(mails[0]);
				List<String> maillist = new ArrayList<String>();
				for(int i=1;i<mails.length;i++) {
					maillist.add(mails[i].trim());
				}
				xmldata = xmlUtil.parseMailListToXMLStringForTrinityUser(maillist);
			}
		}
		user.setXmldata(xmldata);
		
		String localaccount = user.getLocalaccount();
		if(null == localaccount || (!localaccount.equals("1") && !localaccount.equals("0")))
			user.setLocalaccount("0");
		
		if(null == user.getMobile())
			user.setMobile("");
		
		String onlyforexecution = user.getOnlyforexecution();
		if(null == onlyforexecution || (!onlyforexecution.equals("1") && !onlyforexecution.equals("0")))
			user.setOnlyforexecution("0");
		
		String password = user.getPassword();
		if(null == password || password.trim().length() < 1)
			throw new Exception("User Password can not be empty!");
		password = Crypto.getEncryptString(password, encryptKey);
		user.setPassword(password);
		
		if(null ==  user.getSsoid())
			user.setSsoid("");
		
		user.setUsertype("G");
		user.setCreateduseruid("root");
		
		/*
		 * because lastupdatetime column is auto created value, it can not be reload new value.
		 * here, we force to give value to lastupdatetime column.
		 */
		user.setLastupdatetime(new Date());
		
		Trinityuser new_user = this.dao.save(user);
		new_user.setXmldata(null);//不需要回傳
		
		saveCredentials(request, userid, password == null ? new char[0] : password.toCharArray());
		
		return new_user;
	}
	
	public Trinityuser edit(HttpServletRequest request, Trinityuser user) throws ACException, SQLException, IllegalArgumentException, Exception{
		String uid = user.getUseruid();
		if(null == uid || uid.trim().isEmpty())
			throw new IllegalArgumentException("Trinity User Uid can not be empty!");

		Trinityuser old_user = this.dao.findOne(uid);
		if(null == old_user)
			throw new IllegalArgumentException("Trinity User Uid does not exist!(" + uid + ")");
		
		String userid = user.getUserid();
		if(null == userid || userid.trim().isEmpty())
			throw new IllegalArgumentException("Trinity User Id can not be empty!");
		
		if(this.dao.existByID(userid) && !old_user.getUserid().equalsIgnoreCase(userid))
			throw new IllegalArgumentException("Duplicate Trinity User ID!");
		
		String username = user.getUsername();
		if(null == username || username.trim().isEmpty())
			throw new IllegalArgumentException("Trinity User Name can not be empty!");
		
		String activate = user.getActivate();
		if(null == activate || (!activate.equals("1") && !activate.equals("0")))
			throw new Exception("Trinity User activate value can only be 1 or 0!");
		
		String defaultlang = user.getDefaultlang();
		if(null == defaultlang || (!defaultlang.equals("en_US") && !defaultlang.equals("zh_TW") && !defaultlang.equals("zh_CN")))
			user.setDefaultlang("en_US");
		
		if(null == user.getDescription())
			user.setDescription("");
		
		if(null == user.getEmail())
			user.setEmail("");
		
		String xmldata = "";
		if(null != user.getEmail() && !user.getEmail().trim().isEmpty()) {
			String[] mails = user.getEmail().split(";");
			if(mails.length > 1) {
				user.setEmail(mails[0]);
				List<String> maillist = new ArrayList<String>();
				for(int i=1;i<mails.length;i++) {
					maillist.add(mails[i].trim());
				}
				xmldata = xmlUtil.parseMailListToXMLStringForTrinityUser(maillist);
			}
		}
		user.setXmldata(xmldata);
		
		String localaccount = user.getLocalaccount();
		if(null == localaccount || (!localaccount.equals("1") && !localaccount.equals("0")))
			user.setLocalaccount("0");
		
		if(null == user.getMobile())
			user.setMobile("");
		
		String onlyforexecution = user.getOnlyforexecution();
		if(null == onlyforexecution || (!onlyforexecution.equals("1") && !onlyforexecution.equals("0")))
			user.setOnlyforexecution("0");
		
		String password = user.getPassword();
		if(null == password || password.trim().length() < 1)
			throw new Exception("User Password can not be empty!");
		password = Crypto.getEncryptString(password, encryptKey);
		user.setPassword(password);
		
		if(null ==  user.getSsoid())
			user.setSsoid("");
		
		user.setUsertype("G");
		user.setCreateduseruid("root");
		
		/*
		 * because lastupdatetime column is auto created value, it can not be reload new value.
		 * here, we force to give value to lastupdatetime column.
		 */
		user.setLastupdatetime(new Date());
		
		Trinityuser new_user = this.dao.save(user);
		new_user.setXmldata(null);//不需要回傳

		saveCredentials(request, userid, password == null ? new char[0] : password.toCharArray());
		
		return new_user;
	}
	
	public void deleteByUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.trim().isEmpty())
			throw new IllegalArgumentException("Trinity User Uid can not be empty!");
		
		this.g_memberService.deleteByUserUid(uid);
		this.r_memberService.deleteByUserUid(uid);
		this.n_listService.deleteByDestinationUid(uid);
		this.accessService.deleteByPeopleUid(uid);
		this.dao.delete(uid);
	}
	
	public Boolean lockByUserID(HttpServletRequest request, String userid, boolean lock) throws IllegalArgumentException, Exception{
		if(null == userid || userid.trim().isEmpty())
			throw new IllegalArgumentException("Trinity User ID can not be empty!");
		
		String token = CookieUtils.getCookieValue(request, TrinityWebV2Utils.CNAME_ACCESS_TOKEN);
		String ip = NetworkUtil.getRemoteIP(request);
		
		Integer result = 0;
		if(lock) {
			result = credentialsService.lockAccount("FlexWebUI", ip, token, userid);
			if(result < 1) {
				throw new IllegalArgumentException("Lock Operation Fail");
			}else {
				return lock;
			}
		}else {
			result = credentialsService.unlockAccount("FlexWebUI", ip, token, userid);
			if(result < 1) {
				throw new IllegalArgumentException("Unlock Operation Fail");
			}else {
				return lock;
			}
		}
	}
	
	public boolean existByUid(String uid) throws Exception {
		return this.dao.exists(uid);
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
		if(ordering.getOrderField() != null && USER_FIELD_SET.contains(ordering.getOrderField().toLowerCase()))
			order = new Order(direct, ordering.getOrderField());
		
		return new Sort(order);
	}
	
	private void saveCredentials(HttpServletRequest request, String userid, char[] secret) throws ACException, SQLException, Exception{
		if(null == secret || secret.length == 0)
			return;
		
		String ip = NetworkUtil.getRemoteIP(request);
		String token = CookieUtils.getCookieValue(request, TrinityWebV2Utils.CNAME_ACCESS_TOKEN);
		
		this.credentialsService.changeCredentials("FlexWebUI", ip, token, userid, secret, System.currentTimeMillis(), encryptKey);
	}
	
	private void setMailsAndAC(List<Trinityuser> users) throws Exception{
		for(Trinityuser user : users) {
			setMailsAndAC(user);
		}
	}
	
	private void setMailsAndAC(Trinityuser user) throws Exception{
		//For Mail List
		String mail = user.getEmail();
		String xmldata = user.getXmldata();
		if(null != xmldata && !xmldata.isEmpty()) {
			String mails = xmlUtil.parseXMLStringToMailListStringForTrinityUser(xmldata);
			mail += mails;
		}
		user.setEmail(mail);
		
		user.setPassword(null);//順便的, 不需要回傳password
		
		//For AC
		AccessDetailsDto accessDetail = this.credentialsService.getAccessDetails(user.getUserid());
		user.setLock(String.valueOf(accessDetail.getLocked()));
		user.setResetCred(String.valueOf(accessDetail.getResetcred()));
	}
}
