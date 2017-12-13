package com.netpro.trinity.repository.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Constant {
	public static final String[] ORDER_TYPE_VALUES = new String[] { "ASC", "DESC" };
	public static final Set<String> ORDER_TYPE_SET = new HashSet<>(Arrays.asList(ORDER_TYPE_VALUES));
	
	public static final String[] QUERY_TYPE_VALUES = new String[] { "equals", "like" };
	public static final Set<String> QUERY_TYPE_SET = new HashSet<>(Arrays.asList(QUERY_TYPE_VALUES));
	
	public static final String JDBC_JAR			= ".jdbc.jar";
	public static final String JDBC_DRIVER		= ".jdbc.driver";
	public static final String JDBC_URL			= ".jdbc.url";
	public static final String JDBC_OWNER		= ".jdbc.owner";
}
