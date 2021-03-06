package com.netpro.trinity.repository.member.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.Principal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.EnumSet;
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
import com.netpro.ac.ErrorCodes;
import com.netpro.ac.TrinityPrincipal;
import com.netpro.ac.dao.JdbcDaoFactoryImpl;
import com.netpro.ac.dto.AccessDetailsDto;
import com.netpro.ac.util.CookieUtils;
import com.netpro.ac.util.TrinityWebV2Utils;
import com.netpro.trinity.repository.configuration.entity.Trinityconfig;
import com.netpro.trinity.repository.configuration.service.TrinityconfigService;
import com.netpro.trinity.repository.dto.inquire.FilterInfo;
import com.netpro.trinity.repository.dto.inquire.Ordering;
import com.netpro.trinity.repository.dto.inquire.Paging;
import com.netpro.trinity.repository.dto.inquire.Querying;
import com.netpro.trinity.repository.member.dao.TrinityuserJPADao;
import com.netpro.trinity.repository.member.entity.Trinityuser;
import com.netpro.trinity.repository.notification.service.NotificationListService;
import com.netpro.trinity.repository.permission.service.AccessRightService;
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
	@Autowired
	private TrinityconfigService configService;
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
	
	public Trinityuser getByID(String id) throws IllegalArgumentException, Exception{
		if(id == null || id.trim().isEmpty())
			throw new IllegalArgumentException("Trinity User ID can not be empty!");
		
		Trinityuser user = this.dao.findByuserid(id);
		if(null == user)
			throw new IllegalArgumentException("Trinity User ID does not exist!(" + id + ")");
		
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
//		if(null == password || password.trim().isEmpty())
//			throw new Exception("User Password can not be empty!");
		
		boolean localAcc = true;
		if(user.getLocalaccount().equals("0"))
			localAcc = false;
		boolean exeAcc = true;
		if(user.getOnlyforexecution().equals("0"))
			exeAcc = false;
		Trinityconfig config = configService.getByUid("4.0");
		char[] secret = password == null ? new char[0] : password.toCharArray();
		String msg = this.validatePassword(request, false, userid, secret, localAcc, exeAcc);
		if(!"LDAP".equals(msg) && !"OK".equals(msg) && 
				(!("Password can not be empty").equals(msg) || !config.getAuthmode().equals("1")) || localAcc)
			throw new ACException(msg);
		
		user.setPassword(Crypto.getEncryptString(password, encryptKey));
		
		if(null ==  user.getSsoid())
			user.setSsoid("");
		
		if(null == user.getHomedir())
			user.setHomedir("");
		
		user.setUsertype("G");
		Trinityuser reqUser = getUserFormRequest(request);
		user.setCreateduseruid(reqUser.getUseruid());
		
		/*
		 * because lastupdatetime column is auto created value, it can not be reload new value.
		 * here, we force to give value to lastupdatetime column.
		 */
		user.setLastupdatetime(new Date());
		
		user.setPwdchangetime("");
		
		Trinityuser new_user = this.dao.save(user);
		new_user.setXmldata(null);//不需要回傳
		
		saveCredentials(request, userid, secret);
		
		return new_user;
	}
	
	public Trinityuser edit(HttpServletRequest request, Trinityuser user) throws ACException, SQLException, IllegalArgumentException, Exception{
		String uid = user.getUseruid();
		if(null == uid || uid.trim().isEmpty())
			throw new IllegalArgumentException("Trinity User Uid can not be empty!");

		Trinityuser old_user = this.dao.findOne(uid);
		if(null == old_user)
			throw new IllegalArgumentException("Trinity User Uid does not exist!(" + uid + ")");
		
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
		
		boolean localAcc = true;
		if(user.getLocalaccount().equals("0"))
			localAcc = false;
		boolean exeAcc = true;
		if(user.getOnlyforexecution().equals("0"))
			exeAcc = false;
		String password = user.getPassword();
		char[] secret = password == null ? new char[0] : password.toCharArray();
		String msg = this.validatePassword(request, true, old_user.getUserid(), secret, localAcc, exeAcc);
		if(!"LDAP".equals(msg) && !"OK".equals(msg) && !("Password can not be empty").equals(msg))
			throw new ACException(msg);
		
		if(null ==  user.getSsoid())
			user.setSsoid("");
		
		if(null == user.getHomedir())
			user.setHomedir("");
		
		old_user.setUsername(user.getUsername());
		old_user.setActivate(user.getActivate());
		old_user.setMobile(user.getMobile());
		old_user.setEmail(user.getEmail());
		old_user.setDefaultlang(user.getDefaultlang());
		old_user.setDescription(user.getDescription());
		old_user.setSsoid(user.getSsoid());
		old_user.setLocalaccount(user.getLocalaccount());
		old_user.setOnlyforexecution(user.getOnlyforexecution());
		old_user.setHomedir(user.getHomedir());
		old_user.setXmldata(user.getXmldata());
		old_user.setLastupdatetime(new Date());
		
		Trinityuser new_user = this.dao.save(old_user);
		new_user.setXmldata(null);//不需要回傳

		saveCredentials(request, old_user.getUserid(), secret);
		
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
	
	public String validatePassword(HttpServletRequest request, Boolean isEdit, String userid, char[] secret, 
			Boolean localAcc, Boolean exeAcc) throws Exception{
		
		if(secret.length == 0) {
			return "Password can not be empty";
		}
		
		EnumSet<ErrorCodes> errors = EnumSet.noneOf(ErrorCodes.class);
		
		try {
			if(isEdit) {
				String token = CookieUtils.getCookieValue(request, TrinityWebV2Utils.CNAME_ACCESS_TOKEN);
				credentialsService.validateCredentialsPolicy(token, userid, secret, encryptKey);
			} else {
				credentialsService.validateCredentialsPolicy(userid, secret, false);
			}
		}catch(ACException e) {
			if(!e.getErrorCode().isWarning()) {
				errors.add(e.getErrorCode());
				for(Throwable t : e.getSuppressed()) {
					if(t instanceof ACException) {
						ErrorCodes err = ((ACException) t).getErrorCode();
						if(!err.isWarning()) {
							errors.add(err);
						}
					}
				}
			}
		}
		
		if(errors.isEmpty()) {
			return "OK";
		}else if(errors.contains(ErrorCodes.EAC63001)) {
			if(!localAcc) {
				return "LDAP";
			}else {
				return "OK";
			}
		}else if(errors.contains(ErrorCodes.EAC62000)) {
			return "Duplicate password";
		}else if(errors.contains(ErrorCodes.EAC62001)) {
			return "Password length is too short";
		}else if(errors.contains(ErrorCodes.EAC62002) || errors.contains(ErrorCodes.EAC62003) 
				|| errors.contains(ErrorCodes.EAC62004) || errors.contains(ErrorCodes.EAC62005)){
			return "Weak password";
		}else {
			StringBuilder msg = new StringBuilder();
			for(ErrorCodes code : errors) {
				msg.append(code.getMessage()).append('\n');
			}
			msg.setLength(msg.length() == 0 ? 0 : msg.length() - 1);
			return msg.toString();
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
		if(ordering.getOrderField() != null && USER_FIELD_SET.contains(ordering.getOrderField().toLowerCase()))
			order = new Order(direct, ordering.getOrderField());
		
		return new Sort(order);
	}
	
	private Trinityuser getUserFormRequest(HttpServletRequest request) throws ACException, SQLException, IllegalArgumentException, Exception{
		Trinityuser user = new Trinityuser();
		
		String accessToken = CookieUtils.getCookieValue(request, TrinityWebV2Utils.CNAME_ACCESS_TOKEN);
		Principal principal = TrinityWebV2Utils.doValidateAccessTokenAndReturnPrincipal(accessToken);
		if(!"".equals(principal.getName()) && principal instanceof TrinityPrincipal) {
			TrinityPrincipal trinityPrinc = (TrinityPrincipal) principal;
			
			String userid = trinityPrinc.getName();
			user = this.dao.findByuserid(userid);
		}
		
		return user;
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
