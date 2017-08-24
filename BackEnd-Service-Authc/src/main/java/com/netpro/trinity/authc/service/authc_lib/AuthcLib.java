package com.netpro.trinity.authc.service.authc_lib;

import java.security.Principal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import com.netpro.ac.util.TrinityWebV2Utils;
import com.netpro.trinity.service.util.entity.dto.Return_LoginInfo;
import com.netpro.trinity.service.util.status.TrinityServiceStatus;
import com.netpro.trinity.service.util.status.TrinityServiceStatusMsg;

@Service
public class AuthcLib {
	
	@Value("${trinity_key}")
	private String trinity_key;
	
	@Autowired	//自動注入DataSource物件
	private DataSource dataSource;
	
	public Return_LoginInfo authcLogin(HttpServletResponse response, String ip, String ac, String psw) throws SQLException, IllegalArgumentException, IllegalAccessException, Exception {
		Return_LoginInfo info = new Return_LoginInfo();
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
					String cookieStr = response.getHeader("Set-Cookie").replace(";HttpOnly", "");
					info.setToken(cookieStr);
					info.setMsg(TrinityServiceStatusMsg.RESET_PSW);
					info.setStatus(TrinityServiceStatus.CHANGE_CREDENTIALS);
					return info;
//					if(e.getErrorCode() == ErrorCodes.EAC00010) {
//						TrinityWebV2Utils.issueResetToken(response, e.getResetCredentialsToken(), service.getResetTokenMaxAgeInSeconds());
//						String cookieStr = response.getHeader("Set-Cookie").replace(";HttpOnly", "");
//						ret_info.setTokenstr(cookieStr);
//						ret_info.setMsg(TrinityServiceStatusMsg.RESET_PSW);
//						ret_info.setStatus(TrinityServiceStatus.CHANGE_CREDENTIALS);
//					}else {
//						StringBuilder msg = new StringBuilder();
//						for(ErrorCodes error : CommonUtils.collectErrorCodes(e)) {
//							msg.append(error.getMessage()).append('\n');
//						}
//						String errMsg = msg.toString().trim();
//						ret_info.setMsg(TrinityServiceStatusMsg.LOGIN_ERROR+" "+errMsg);
//						ret_info.setStatus(TrinityServiceStatus.ERROR);
//					}
//					return ret_info;
				}
			}
			
			if(null == principal) {
				throw new IllegalAccessException(TrinityServiceStatusMsg.LOGIN_ERROR+" "+TrinityServiceStatusMsg.ACCOUNT_CHECK);
//				ret_info.setMsg(TrinityServiceStatusMsg.LOGIN_ERROR+" "+TrinityServiceStatusMsg.ACCOUNT_CHECK);
//				ret_info.setStatus(TrinityServiceStatus.ERROR);
//				return ret_info;
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
				String cookieStr = response.getHeader("Set-Cookie").replace(";HttpOnly", "");
				info.setUserinfo(userInfo);
				info.setToken(cookieStr);
				return info;
			}
		} finally {
			if(con != null)
				try {
					con.close();
				} catch (SQLException e) {}
		}
	}
}
