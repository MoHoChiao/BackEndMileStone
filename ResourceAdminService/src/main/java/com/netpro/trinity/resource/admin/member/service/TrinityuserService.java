package com.netpro.trinity.resource.admin.member.service;

import java.security.Principal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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
import com.netpro.trinity.resource.admin.configuration.entity.Trinityconfig;
import com.netpro.trinity.resource.admin.configuration.service.TrinityconfigService;
import com.netpro.trinity.resource.admin.dto.FilterInfo;
import com.netpro.trinity.resource.admin.dto.Ordering;
import com.netpro.trinity.resource.admin.dto.Paging;
import com.netpro.trinity.resource.admin.member.dao.TrinityuserJPADao;
import com.netpro.trinity.resource.admin.member.entity.Trinityuser;
import com.netpro.trinity.resource.admin.notification.service.NotificationListService;
import com.netpro.trinity.resource.admin.util.Constant;
import com.netpro.trinity.resource.admin.util.Crypto;
import com.netpro.trinity.resource.admin.util.NetworkUtil;
import com.netpro.trinity.resource.admin.util.XMLDataUtility;
import com.zaxxer.hikari.HikariDataSource;

@Service
public class TrinityuserService {
	private static final Logger LOGGER = LoggerFactory.getLogger(TrinityuserService.class);
		
	@Autowired
	private TrinityuserJPADao dao;
	
	@Autowired
	private GroupMemberService g_memberService;
	@Autowired
	private RoleMemberService r_memberService;
	@Autowired
	private NotificationListService n_listService;
//	@Autowired
//	private AccessRightService accessService;
	@Autowired
	private TrinityconfigService configService;
	@Autowired	//自動注入DataSource物件
	private HikariDataSource dataSource;
	@Autowired
	private XMLDataUtility xmlUtil;
	
	@Value("${trinity.encrypt.key}")
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
		setProfileDataOnly(users);
		return users;
	}
	
	public ResponseEntity<?> getByFilter(FilterInfo filter) throws Exception{
		Paging paging = filter.getPaging();
		Ordering ordering = filter.getOrdering();
		String param = filter.getParam();
		
		if(null == paging) 
			paging = new Paging(0, 20);
		
		if(null == ordering) 
			ordering = new Ordering("ASC", "username");
		
		if(null == param || param.trim().isEmpty())
			param = "%%";
		param = param.trim();
		
		Page<Trinityuser> page_users = this.dao.findByUsernameLikeIgnoreCaseOrUseridLikeIgnoreCase(param, param, getPagingAndOrdering(paging, ordering));
		setMailsAndAC(page_users.getContent());
		setProfileDataOnly(page_users.getContent());
		return ResponseEntity.ok(page_users);
	}
	
	public Trinityuser getByUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.isEmpty())
			throw new IllegalArgumentException("Trinity User UID can not be empty!");
		
		Trinityuser user = null;
		try {
			user = this.dao.findById(uid).get();
		}catch(NoSuchElementException e) {}
		 
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
		setProfileDataOnly(user);
		
		return user;
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

		Trinityuser old_user = null;
		try {
			old_user = this.dao.findById(uid).get();
		}catch(NoSuchElementException e) {}
		 
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
		System.out.println(old_user.getPassword()+"/////////////////");
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
//		this.accessService.deleteByPeopleUid(uid);
		this.dao.deleteById(uid);
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
		return this.dao.existsById(uid);
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
			return PageRequest.of(paging.getNumber(), paging.getSize(), getOrdering(ordering));
		}else {
			return PageRequest.of(paging.getNumber(), paging.getSize());
		}
	}
	
	private Sort getOrdering(Ordering ordering) throws Exception{
		Direction direct = Direction.fromString("ASC");
		if(ordering.getOrderType() != null && Constant.ORDER_TYPE_SET.contains(ordering.getOrderType().toUpperCase()))
			direct = Direction.fromString(ordering.getOrderType());
		
		if(ordering.getOrderField() != null && !ordering.getOrderField().isEmpty())
			return Sort.by(direct, ordering.getOrderField());
		else
			return Sort.by(direct, "username");
	}
	
	public Trinityuser getUserFormRequest(HttpServletRequest request) throws ACException, SQLException, IllegalArgumentException, Exception{
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
	
	@SuppressWarnings("unused")
	private void setMailsAndAC(List<Trinityuser> users) throws Exception{
		for(Trinityuser user : users) {
			setMailsAndAC(user);
		}
	}
	
	private void setMailsAndAC(Trinityuser user) throws Exception {
		//For Mail List
		String mail = user.getEmail();
		String xmldata = user.getXmldata();
		if(null != xmldata && !xmldata.isEmpty()) {
			String mails = xmlUtil.parseXMLStringToMailListStringForTrinityUser(xmldata);
			mail += mails;
		}
		user.setEmail(mail);
				
		//For AC
		AccessDetailsDto accessDetail = this.credentialsService.getAccessDetails(user.getUserid());
		user.setLock(String.valueOf(accessDetail.getLocked()));
		user.setResetCred(String.valueOf(accessDetail.getResetcred()));
	}
	
	private void setProfileDataOnly(List<Trinityuser> users) {
		for(Trinityuser user : users) {
			setProfileDataOnly(user);
		}
	}
	
	private void setProfileDataOnly(Trinityuser user) {
		user.setDefaultlang(null);
		user.setEmail(null);
		user.setHomedir(null);
		user.setLocalaccount(null);
		user.setMobile(null);
		user.setOnlyforexecution(null);
		user.setPassword(null);
		user.setPwdchangetime(null);
		user.setSsoid(null);
		user.setXmldata(null);
	}
}
