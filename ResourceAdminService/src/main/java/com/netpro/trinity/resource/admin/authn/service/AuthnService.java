package com.netpro.trinity.resource.admin.authn.service;

import java.security.Principal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.netpro.trinity.resource.admin.authn.dto.ReturnLoginInfo;
import com.netpro.trinity.resource.admin.prop.dto.TrinityPropSetting;
import com.zaxxer.hikari.HikariDataSource;

@Service
public class AuthnService {
	
	@Autowired
	private TrinityPropSetting trinityProp;
		
	@Autowired	//自動注入DataSource物件
	private HikariDataSource dataSource;
	
	public ReturnLoginInfo genAuthn(HttpServletResponse response, String ip, String ac, String psw) throws SQLException, IllegalArgumentException, IllegalAccessException, Exception {
		ReturnLoginInfo info = new ReturnLoginInfo();
		if(null == ac || ac.trim().equals("")) {
			throw new IllegalArgumentException("Login Error!"+" "+"User Account can not be empty!");
		}
		if(null == psw || psw.trim().equals("")) {
			throw new IllegalArgumentException("Login Error!"+" "+"User PSW can not be empty!");
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
					info.setMsg("Please Reset your password!");
					info.setStatus("ChangeCredentials");
					return info;
				}
			}
			
			if(null == principal) {
				throw new IllegalAccessException("Login Error!"+" "+"Please Check Your Account!");
			}else {
				TrinityPrincipal trinityPrinc = (TrinityPrincipal) principal;
				String userInfo = trinityPrinc.getName();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				if(null != trinityPrinc.getLastAccessTimeInMillis()) {
					userInfo += " (" + sdf.format(new Date(trinityPrinc.getLastAccessTimeInMillis())) + ")";
				}
				
				if(trinityPrinc.isCredentialsExpirationWarning()) {
					info.setMsg("Password is near expiration date!");
					info.setStatus("Warning");
				}else {
					info.setMsg("Login Success.");
					info.setStatus("Success");
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
	
	public String removeAuthn(HttpServletResponse response) {
		try {
			TrinityWebV2Utils.revokeHttpOnlyCookies(response);
			return "Logout Success.";
		}catch(Exception e) {
			return "Logout Fail.";
		}
	}
	
	public ReturnLoginInfo findAuthn(HttpServletRequest request) {
		ReturnLoginInfo info = new ReturnLoginInfo();
		info.setMsg("Validate Fail.");
		info.setStatus("Error");
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
				
				info.setMsg("Validate Success.");
				info.setStatus("Success");
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
	
	public String resetAuthn(HttpServletRequest request, HttpServletResponse response, String ip, String psw) throws SQLException, IllegalArgumentException, IllegalAccessException, Exception {		
		if(null == psw || psw.trim().equals("")) {
			throw new IllegalArgumentException("Login Error!"+" "+"User PSW can not be empty!");
		}
		
		String resetToken = TrinityWebV2Utils.getResetToken(request);
		TrinityWebV2Utils.revokeHttpOnlyCookies(response);
		
		Principal principal = TrinityWebV2Utils.doValidateAccessTokenAndReturnPrincipal(resetToken);
		if(!(principal instanceof TrinityPrincipal)) {
			throw new IllegalAccessException("Session expired, please login to get reset token again!");
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
					return "Password changed, please login with new password.";
				} catch (ACException e) {					
					StringBuilder msg = new StringBuilder();
					for(ErrorCodes error : CommonUtils.collectErrorCodes(e)) {
						msg.append(error.getMessage()).append('\n');
					}
					String errMsg = msg.toString().trim();
					throw new IllegalAccessException("Change password failed!" + " " + errMsg);
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
