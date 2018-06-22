package com.netpro.trinity.service.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public interface Constant {
	//about filter
	public static final String[] ORDER_TYPE_VALUES = new String[] { "ASC", "DESC" };
	public static final Set<String> ORDER_TYPE_SET = new HashSet<>(Arrays.asList(ORDER_TYPE_VALUES));
	
	public static final String[] QUERY_TYPE_VALUES = new String[] { "equals", "like" };
	public static final Set<String> QUERY_TYPE_SET = new HashSet<>(Arrays.asList(QUERY_TYPE_VALUES));
	
	//about driver manager
	public static final String JDBC_JAR			= ".jdbc.jar";
	public static final String JDBC_DRIVER		= ".jdbc.driver";
	public static final String JDBC_URL			= ".jdbc.url";
	public static final String JDBC_OWNER		= ".jdbc.owner";
	
	//file separator
	public static final String SEPARATOR = "/";
	
	//about external rule
	public static final String TAG_AGENT = "agent";
	public static final String TAG_RULE = "rule";
	public static final String TAG_ATTR_AGENTUID = "uuid";
	public static final String TAG_ATTR_FILENAME = "filename";
	public static final String TAG_ATTR_MD5 = "md5";
	public static final String TAG_ATTR_ACTIVE = "active";
	public static final String TAG_ATTR_NAME = "name";
	public static final String TAG_ATTR_CLASSPATH = "classpath";
	public static final String TAG_ATTR_JARUID = "jaruid";
	
	public static final String EXT_RULE_CFG		= "extrule.xml";
	public static final String RULE_JAR 		= "RuleJar";
	public static final String RULE_CLASS		= "RuleClass";
	public static final String LIBRARY			= "Library";
	public static final String MODULE_EXTRULE 	= "extrule";
	public static final String CLASSES = ".class";
	public static final String FOLDER_CLASS = "classes";
	
	//resdoc
	public static final String LANGCODE_EN		= "en_us";
	public static final String LANGCODE_TW		= "zh_tw";
	public static final String LANGCODE_CN		= "zh_cn";
	public static final String MODULE_DPHANDLER	= "dphandler";
	public static final String MODULE_DPHANDLER_DESC = "-description";
	public static final String MODULE_DPHANDLER_HN = "-handlername";
	
}
