package com.netpro.trinity.authc.service.authc_lib;

import java.security.Principal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.netpro.ac.ACException;
import com.netpro.ac.AuthenticatorModes;
import com.netpro.ac.DefaultAuthenticationService;
import com.netpro.ac.ErrorCodes;
import com.netpro.ac.TrinityPrincipal;
import com.netpro.ac.UsernamePasswordCredentials;
import com.netpro.ac.dao.DaoFactory;
import com.netpro.ac.dao.JdbcDaoFactoryImpl;
import com.netpro.ac.util.CommonUtils;
import com.netpro.ac.util.CookieUtils;
import com.netpro.ac.util.TrinityWebV2Utils;
import com.netpro.trinity.service.util.entity.ReturnLoginInfo;
import com.netpro.trinity.service.util.status.TrinityServiceStatus;
import com.netpro.trinity.service.util.status.TrinityServiceStatusMsg;

@Service
public class AuthcLib {
	
	@Value("${trinity_key}")
	private String trinity_key;
	
	@Autowired	//自動注入DataSource物件
	private DataSource dataSource;
	
	public ReturnLoginInfo genAuthc(HttpServletResponse response, String ip, String ac, String psw) throws SQLException, IllegalArgumentException, IllegalAccessException, Exception {
		ReturnLoginInfo info = new ReturnLoginInfo();
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
							, timestamp, trinity_key, AuthenticatorModes.NORMAL);
					expireSeconds = service.getAccessTokenMaxAgeInSeconds();
				} catch (ACException e) {
					e.printStackTrace();
					
					if(e.getErrorCode() != ErrorCodes.EAC00010) {
						StringBuilder msg = new StringBuilder();
						for(ErrorCodes error : CommonUtils.collectErrorCodes(e)) {
							msg.append(error.getMessage()).append('\n');
						}
						String errMsg = msg.toString().trim();
						throw new IllegalAccessException(errMsg);
					}
					
					TrinityWebV2Utils.issueResetToken(response, e.getResetCredentialsToken(), service.getResetTokenMaxAgeInSeconds());
					String cookieStr = response.getHeader("Set-Cookie");
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
				String cookieStr = response.getHeader("Set-Cookie");
				info.setUserinfo(userInfo);
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
			return "Logout Success.";
		}catch(Exception e) {
			return "Logout Fail.";
		}
	}
	
	public String findAuthc(HttpServletRequest request) {
		try {
			String accessToken = CookieUtils.getCookieValue(request, TrinityWebV2Utils.CNAME_ACCESS_TOKEN);
			Principal principal = TrinityWebV2Utils.doValidateAccessTokenAndReturnPrincipal(accessToken);
			if(!"".equals(principal.getName()) && principal instanceof TrinityPrincipal) {
				return "Validate Success.";
			}else {
				return "Validate Fail.";
			}
		}catch(Exception e) {
			return "Validate Fail.";
		}
	}
}
