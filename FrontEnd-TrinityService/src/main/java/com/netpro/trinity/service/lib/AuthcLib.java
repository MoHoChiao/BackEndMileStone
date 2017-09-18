package com.netpro.trinity.service.lib;

import java.security.Principal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netpro.ac.ACException;
import com.netpro.ac.AuthenticatorModes;
import com.netpro.ac.DefaultAuthenticationService;
import com.netpro.ac.DefaultCredentialsService;
import com.netpro.ac.ErrorCodes;
import com.netpro.ac.TrinityPrincipal;
import com.netpro.ac.UsernamePasswordCredentials;
import com.netpro.ac.dao.DaoFactory;
import com.netpro.ac.dao.JdbcDaoFactoryImpl;
import com.netpro.ac.util.CommonUtils;
import com.netpro.ac.util.CookieUtils;
import com.netpro.ac.util.TrinityWebV2Utils;
import com.netpro.trinity.service.dto.prop.TrinityPropSetting;
import com.netpro.trinity.service.util.entity.dto.ReturnLoginInfo_Dto;
import com.netpro.trinity.service.util.status.TrinityServiceStatus;
import com.netpro.trinity.service.util.status.TrinityServiceStatusMsg;

@Service
public class AuthcLib {
	
	@Autowired
	private TrinityPropSetting trinityProp;
	
	@Autowired	//自動注入DataSource物件
	private DataSource dataSource;
	
	public ReturnLoginInfo_Dto genAuthc(HttpServletResponse response, String ip, String ac, String psw) throws SQLException, IllegalArgumentException, IllegalAccessException, Exception {
		ReturnLoginInfo_Dto info = new ReturnLoginInfo_Dto();
		if(null == ac || ac.trim().equals("")) {
			throw new IllegalArgumentException(TrinityServiceStatusMsg.LOGIN_ERROR+" "+TrinityServiceStatusMsg.ACCOUNT_EMPTY);
		}
		if(null == psw || psw.trim().equals("")) {
			throw new IllegalArgumentException(TrinityServiceStatusMsg.LOGIN_ERROR+" "+TrinityServiceStatusMsg.PSW_EMPTY);
		}
		
		Connection con = null;
		try {
			con = dataSource.getConnection();
			
			Principal principal = null;
			long expireSeconds = -1;
			if(con != null) {
				long timestamp = System.currentTimeMillis();
				DaoFactory factory = new JdbcDaoFactoryImpl(con);
				DefaultAuthenticationService service = new DefaultAuthenticationService(factory);
				String module = "TrinityHome";
				
				try {
					principal = service.login(module, ip, ac, new UsernamePasswordCredentials(ac, psw.toCharArray())
							, timestamp, trinityProp.getEncrypt().getKey(), AuthenticatorModes.NORMAL);
					expireSeconds = service.getAccessTokenMaxAgeInSeconds();
				} catch (ACException e) {
					if(e.getErrorCode() != ErrorCodes.EAC00010) {
						StringBuilder msg = new StringBuilder();
						for(ErrorCodes error : CommonUtils.collectErrorCodes(e)) {
							msg.append(error.getMessage()).append('\n');
						}
						String errMsg = msg.toString().trim();
						throw new IllegalAccessException(errMsg);
					}
					
					TrinityWebV2Utils.issueResetToken(response, e.getResetCredentialsToken(), service.getResetTokenMaxAgeInSeconds());
					info.setUsertype("");
					info.setMsg(TrinityServiceStatusMsg.RESET_PSW);
					info.setStatus(TrinityServiceStatus.CHANGE_CREDENTIALS);
					return info;
				}
			}
			
			if(null == principal) {
				throw new IllegalAccessException(TrinityServiceStatusMsg.LOGIN_ERROR+" "+TrinityServiceStatusMsg.ACCOUNT_CHECK);
			}else {
				TrinityPrincipal trinityPrinc = (TrinityPrincipal) principal;
				String userInfo = trinityPrinc.getName();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				if(null != trinityPrinc.getLastAccessTimeInMillis()) {
					userInfo += " (" + sdf.format(new Date(trinityPrinc.getLastAccessTimeInMillis())) + ")";
				}
				
				if(trinityPrinc.isCredentialsExpirationWarning()) {
					info.setMsg(TrinityServiceStatusMsg.PSW_NEAR_EXPIRE);
					info.setStatus(TrinityServiceStatus.WARN);
				}else {
					info.setMsg(TrinityServiceStatusMsg.LOGIN_SUCCESS);
					info.setStatus(TrinityServiceStatus.SUCCESS);
				}
				
				TrinityWebV2Utils.issueHttpOnlyCookies(response, trinityPrinc, expireSeconds, true);
				info.setUserinfo(userInfo);
				info.setUsertype(trinityPrinc.isPowerUser() ? "R" : "G");
				return info;
			}
		} finally {
			if(con != null)
				try {
					con.close();
				} catch (SQLException e) {}
		}
	}
	
	public String removeAuthc(HttpServletResponse response) {
		try {
			TrinityWebV2Utils.revokeHttpOnlyCookies(response);
			return TrinityServiceStatusMsg.LOGOUT_SUCCESS;
		}catch(Exception e) {
			return TrinityServiceStatusMsg.LOGOUT_ERROR;
		}
	}
	
	public ReturnLoginInfo_Dto findAuthc(HttpServletRequest request) {
		ReturnLoginInfo_Dto info = new ReturnLoginInfo_Dto();
		info.setMsg(TrinityServiceStatusMsg.VALIDATE_ERROR);
		info.setStatus(TrinityServiceStatus.ERROR);
		info.setUserinfo("");
		info.setUsertype("");
		try {
			String accessToken = CookieUtils.getCookieValue(request, TrinityWebV2Utils.CNAME_ACCESS_TOKEN);
			Principal principal = TrinityWebV2Utils.doValidateAccessTokenAndReturnPrincipal(accessToken);
			if(!"".equals(principal.getName()) && principal instanceof TrinityPrincipal) {
				TrinityPrincipal trinityPrinc = (TrinityPrincipal) principal;
				
				String userInfo = trinityPrinc.getName();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				if(null != trinityPrinc.getLastAccessTimeInMillis()) {
					userInfo += " (" + sdf.format(new Date(trinityPrinc.getLastAccessTimeInMillis())) + ")";
				}
				
				info.setMsg(TrinityServiceStatusMsg.VALIDATE_SUCCESS);
				info.setStatus(TrinityServiceStatus.SUCCESS);
				info.setUserinfo(userInfo);
				info.setUsertype(trinityPrinc.isPowerUser() ? "R" : "G");
				return info;
			}else {
				return info;
			}
		}catch(Exception e) {
			return info;
		}
	}
	
	public String resetAuthc(HttpServletRequest request, HttpServletResponse response, String ip, String psw) throws SQLException, IllegalArgumentException, IllegalAccessException, Exception {		
		if(null == psw || psw.trim().equals("")) {
			throw new IllegalArgumentException(TrinityServiceStatusMsg.LOGIN_ERROR+" "+TrinityServiceStatusMsg.PSW_EMPTY);
		}
		
		String resetToken = TrinityWebV2Utils.getResetToken(request);
		TrinityWebV2Utils.revokeHttpOnlyCookies(response);
		
		Principal principal = TrinityWebV2Utils.doValidateAccessTokenAndReturnPrincipal(resetToken);
		if(!(principal instanceof TrinityPrincipal)) {
			throw new IllegalAccessException(TrinityServiceStatusMsg.Session_Expired);
		}
		
		Connection con = null;
		try {
			con = dataSource.getConnection();
			if(con != null) {
				long timestamp = System.currentTimeMillis();
				DaoFactory factory = new JdbcDaoFactoryImpl(con);
				DefaultCredentialsService service = new DefaultCredentialsService(factory);
				String module = "TrinityHome";
				
				try {
					service.changeCredentials(module, ip, resetToken, principal.getName(), psw.toCharArray(), timestamp, "");
					return TrinityServiceStatusMsg.RESET_PSW_SUCCESS;
				} catch (ACException e) {					
					StringBuilder msg = new StringBuilder();
					for(ErrorCodes error : CommonUtils.collectErrorCodes(e)) {
						msg.append(error.getMessage()).append('\n');
					}
					String errMsg = msg.toString().trim();
					throw new IllegalAccessException(TrinityServiceStatusMsg.RESET_PSW_ERROR + " " + errMsg);
				}
			}else {
				throw new Exception("Error! Database Connection Null.");
			}
		} finally {
			if(con != null)
				try {
					con.close();
				} catch (SQLException e) {}
		}
	}
}
