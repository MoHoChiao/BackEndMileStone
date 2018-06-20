package com.netpro.trinity.auth.authz.feign.util;

public class TrinityServiceStatusMsg {
	public static final String LOGIN_ERROR = "Login Error!";
	public static final String LOGIN_SUCCESS = "Login Success.";
	public static final String LOGOUT_ERROR = "Logout Fail.";
	public static final String LOGOUT_SUCCESS = "Logout Success.";
	public static final String VALIDATE_ERROR = "Validate Fail.";
	public static final String VALIDATE_SUCCESS = "Validate Success.";
	public static final String ACCOUNT_EMPTY = "User Account can not be empty!";
	public static final String PSW_EMPTY = "User PSW can not be empty!";
	public static final String PSW_NEAR_EXPIRE = "Password is near expiration date!";
	public static final String ACCOUNT_CHECK = "Please Check Your Account!";
	public static final String RESET_PSW = "Please Reset your password!";
	public static final String RESET_PSW_SUCCESS = "Password changed, please login with new password.";
	public static final String RESET_PSW_ERROR = "Change password failed!";
	public static final String Session_Expired = "Session expired, please login to get reset token again!";
}
