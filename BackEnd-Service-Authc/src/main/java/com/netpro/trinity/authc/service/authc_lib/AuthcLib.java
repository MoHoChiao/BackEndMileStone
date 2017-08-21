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
import com.netpro.trinity.authc.service.entity.LoginInfo;
import com.netpro.trinity.authc.service.util.LoginFlag;
import com.netpro.trinity.authc.service.util.LoginInfoMsg;

@Service
public class AuthcLib {
	
	@Value("${trinity_key}")
	private String trinity_key;
	
	@Autowired	//自動注入DataSource物件
	private DataSource dataSource;
	
	public LoginInfo authcLogin(HttpServletResponse response, LoginInfo info) {
		if(null == info.getAccount() || info.getAccount().trim().equals("")) {
			info.setInfo(LoginInfoMsg.LOGIN_ERROR+" "+LoginInfoMsg.ACCOUNT_EMPTY);
			info.setFlag(LoginFlag.ERROR);
			return info;
		}
		
		if(null == info.getPsw() || info.getPsw().trim().equals("")) {
			info.setInfo(LoginInfoMsg.LOGIN_ERROR+" "+LoginInfoMsg.PSW_EMPTY);
			info.setFlag(LoginFlag.ERROR);
			return info;
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
					principal = service.login(module, info.getRemoteip(), info.getAccount(), new UsernamePasswordCredentials(info.getAccount(), info.getPsw().toCharArray())
							, timestamp, trinity_key, AuthenticatorModes.NORMAL);
					expireSeconds = service.getAccessTokenMaxAgeInSeconds();
				} catch (ACException e) {
					e.printStackTrace();
					
					if(e.getErrorCode() == ErrorCodes.EAC00010) {
						TrinityWebV2Utils.issueResetToken(response, e.getResetCredentialsToken(), service.getResetTokenMaxAgeInSeconds());
						String cookieStr = response.getHeader("Set-Cookie").replace(";HttpOnly", "");
						info.setInfo(cookieStr);
						info.setFlag(LoginFlag.CHANGE_CREDENTIALS);
					}else {
						StringBuilder msg = new StringBuilder();
						for(ErrorCodes error : CommonUtils.collectErrorCodes(e)) {
							msg.append(error.getMessage()).append('\n');
						}
						String errMsg = msg.toString().trim();
						info.setInfo(LoginInfoMsg.LOGIN_ERROR+" "+errMsg);
						info.setFlag(LoginFlag.ERROR);
					}
					return info;
				}
			}
			
			if(null == principal) {
				info.setInfo(LoginInfoMsg.LOGIN_ERROR+" "+LoginInfoMsg.ACCOUNT_CHECK);
				info.setFlag(LoginFlag.ERROR);
				return info;
			}else {
				TrinityPrincipal trinityPrinc = (TrinityPrincipal) principal;
				String userInfo = trinityPrinc.getName();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				if(null != trinityPrinc.getLastAccessTimeInMillis()) {
					userInfo += " (" + sdf.format(new Date(trinityPrinc.getLastAccessTimeInMillis())) + ")";
				}
				
				if(trinityPrinc.isCredentialsExpirationWarning()) {
					info.setUserinfo(userInfo);
					info.setInfo("");
					info.setFlag(LoginFlag.WARN);
				}else {
					info.setUserinfo(userInfo);
					info.setInfo("");
					info.setFlag(LoginFlag.SUCCESS);
				}
				
				TrinityWebV2Utils.issueHttpOnlyCookies(response, trinityPrinc, expireSeconds, true);
				String cookieStr = response.getHeader("Set-Cookie").replace(";HttpOnly", "");
				info.setInfo(cookieStr);
				return info;
			}
		} catch (Exception e) {
			e.printStackTrace();
			info.setInfo(LoginInfoMsg.LOGIN_ERROR+" "+e.getMessage());
			info.setFlag(LoginFlag.ERROR);
			return info;
		} finally {
			if(con != null)
				try {
					con.close();
				} catch (SQLException e) {}
		}
	}
}
