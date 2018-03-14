package com.netpro.trinity.repository.service.member;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

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
import com.netpro.ac.util.CookieUtils;
import com.netpro.ac.util.TrinityWebV2Utils;
import com.netpro.trinity.repository.dao.jpa.member.TrinityuserJPADao;
import com.netpro.trinity.repository.dto.FilterInfo;
import com.netpro.trinity.repository.dto.Ordering;
import com.netpro.trinity.repository.dto.Paging;
import com.netpro.trinity.repository.dto.Querying;
import com.netpro.trinity.repository.entity.member.jpa.Trinityuser;
import com.netpro.trinity.repository.service.notification.NotificationListService;
import com.netpro.trinity.repository.service.permission.AccessRightService;
import com.netpro.trinity.repository.util.Constant;
import com.netpro.trinity.repository.util.Crypto;
import com.netpro.trinity.repository.util.NetworkUtil;

@Service
public class TrinityuserService {
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
	@Autowired
	private DefaultCredentialsService credentialsService;
	
	@Value("${encrypt.key}")
	private String encryptKey;
	
	public List<Trinityuser> getAll() throws Exception{
		List<Trinityuser> users = this.dao.findAll();
		return users;
	}
	
	public Trinityuser getByUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.isEmpty())
			throw new IllegalArgumentException("Trinity User UID can not be empty!");
		
		Trinityuser user = this.dao.findOne(uid);
		if(null == user)
			throw new IllegalArgumentException("Trinity User UID does not exist!(" + uid + ")");
		return user;
	}
	
	public List<Trinityuser> getByName(String name) throws IllegalArgumentException, Exception{
		if(name == null || name.trim().isEmpty())
			throw new IllegalArgumentException("Trinity User Name can not be empty!");
		
		return this.dao.findByusername(name);
	}
	
	public List<Trinityuser> getByUserType(String userType) throws IllegalArgumentException, Exception{
		if(userType == null || userType.trim().isEmpty())
			throw new IllegalArgumentException("Trinity User Type can not be empty!");
		
		return this.dao.findByusertype(userType.toUpperCase());
	}
	
	@SuppressWarnings("unchecked")
	public ResponseEntity<?> getByFilter(FilterInfo filter) throws SecurityException, NoSuchMethodException, 
								IllegalArgumentException, IllegalAccessException, InvocationTargetException, Exception{
		if(null == filter) {
			List<Trinityuser> users = this.dao.findAll();
			return ResponseEntity.ok(users);
		}
			
		Paging paging = filter.getPaging();
		Ordering ordering = filter.getOrdering();
		Querying querying = filter.getQuerying();
		
		if(null == paging && null == ordering && null == querying) {
			List<Trinityuser> users = this.dao.findAll();
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
				return ResponseEntity.ok(page_user);
			}else if(sort != null) {
				List<Trinityuser> users = this.dao.findAll(sort);
				return ResponseEntity.ok(users);
			}else {
				/*
				 * The paging and ordering both objects are null.
				 * it means pageRequest and sort must be null too.
				 * then return default
				 */
				List<Trinityuser> users = this.dao.findAll(sort);
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
				return ResponseEntity.ok(page_user);
			}else if(sort != null) {
				method = this.dao.getClass().getMethod(methodName.toString(), String.class, Sort.class);
				List<Trinityuser> users = (List<Trinityuser>) method.invoke(this.dao, queryString, sort);
				return ResponseEntity.ok(users);
			}else {
				method = this.dao.getClass().getMethod(methodName.toString(), String.class);
				List<Trinityuser> users = (List<Trinityuser>) method.invoke(this.dao, queryString);
				return ResponseEntity.ok(users);
			}
		}
	}
	
	public Trinityuser add(HttpServletRequest request, Trinityuser user) throws ACException, IllegalArgumentException, Exception{
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
		
		saveCredentials(request, userid, password == null ? new char[0] : password.toCharArray());
		
		return new_user;
	}
	
	public Trinityuser edit(Trinityuser user) throws IllegalArgumentException, Exception{
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
						
		return this.dao.save(user);
	}
	
	public void deleteByUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("Trinity User Uid can not be empty!");
		
		this.g_memberService.deleteByUserUid(uid);
		this.r_memberService.deleteByUserUid(uid);
		this.n_listService.deleteByDestinationUid(uid);
		this.accessService.deleteByPeopleUid(uid);
		this.dao.delete(uid);
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
	
	private void saveCredentials(HttpServletRequest request, String userid, char[] secret) throws ACException{
		if(null == secret || secret.length == 0)
			return;
		
		String ip = NetworkUtil.getRemoteIP(request);
		String token = CookieUtils.getCookieValue(request, TrinityWebV2Utils.CNAME_ACCESS_TOKEN);
		credentialsService.changeCredentials("FlexWebUI", ip, token, userid, secret, System.currentTimeMillis(), encryptKey);
	}
}
