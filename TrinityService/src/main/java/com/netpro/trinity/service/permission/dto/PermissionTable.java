package com.netpro.trinity.service.permission.dto;

import java.util.Map;

public class PermissionTable {
	private ConnectionFuncPermission connection_func;
	private FrequencyFuncPermission frequency_func;
	private DomainFuncPermission domain_func;
	private AgentFuncPermission agent_func;
	private FileSourceFuncPermission filesource_func;
	private AliasRefFuncPermission alias_func;
	private ExternalRuleFuncPermission extrule_func;
	private PerformanceFuncPermission performance_func;
	private Map<String, ObjectPermission> object_map;
	private boolean isRoot;
	private boolean isAdmin;
	private boolean isAccountManager;
	private boolean isAdminEditMode;

	public ConnectionFuncPermission getConnection_func() {
		return connection_func;
	}

	public void setConnection_func(ConnectionFuncPermission connection_func) {
		this.connection_func = connection_func;
	}
	
	public FrequencyFuncPermission getFrequency_func() {
		return frequency_func;
	}

	public void setFrequency_func(FrequencyFuncPermission frequency_func) {
		this.frequency_func = frequency_func;
	}

	public DomainFuncPermission getDomain_func() {
		return domain_func;
	}

	public void setDomain_func(DomainFuncPermission domain_func) {
		this.domain_func = domain_func;
	}

	public AgentFuncPermission getAgent_func() {
		return agent_func;
	}

	public void setAgent_func(AgentFuncPermission agent_func) {
		this.agent_func = agent_func;
	}

	public FileSourceFuncPermission getFilesource_func() {
		return filesource_func;
	}

	public void setFilesource_func(FileSourceFuncPermission filesource_func) {
		this.filesource_func = filesource_func;
	}

	public AliasRefFuncPermission getAlias_func() {
		return alias_func;
	}

	public void setAlias_func(AliasRefFuncPermission alias_func) {
		this.alias_func = alias_func;
	}

	public ExternalRuleFuncPermission getExtrule_func() {
		return extrule_func;
	}

	public void setExtrule_func(ExternalRuleFuncPermission extrule_func) {
		this.extrule_func = extrule_func;
	}

	public PerformanceFuncPermission getPerformance_func() {
		return performance_func;
	}

	public void setPerformance_func(PerformanceFuncPermission performance_func) {
		this.performance_func = performance_func;
	}
	
	public Map<String, ObjectPermission> getObject_map() {
		return object_map;
	}

	public void setObject_map(Map<String, ObjectPermission> object_map) {
		this.object_map = object_map;
	}
	
	public boolean isRoot() {
		return isRoot;
	}

	public void setRoot(boolean isRoot) {
		this.isRoot = isRoot;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public boolean isAccountManager() {
		return isAccountManager;
	}

	public void setAccountManager(boolean isAccountManager) {
		this.isAccountManager = isAccountManager;
	}

	public boolean isAdminEditMode() {
		return isAdminEditMode;
	}

	public void setAdminEditMode(boolean isAdminEditMode) {
		this.isAdminEditMode = isAdminEditMode;
	}
	
	@Override
	public String toString() {
		return "PermissionTable{" + "connection_func='" + connection_func + '\'' + ", "
				+ "agent_func='" + agent_func + '\'' + ", "
				+ "frequency_func='" + frequency_func + '\'' + ", "
				+ "domain_func='" + domain_func + '\'' + ", "
				+ "filesource_func='" + filesource_func + '\'' + ", "
				+ "alias_func='" + alias_func + '\'' + ", "
				+ "extrule_func='" + extrule_func + '\'' + ", "
				+ "performance_func='" + performance_func + '\'' + ", "
				+ "object_map='" + object_map + '\'' + ", "
				+ "isRoot=" + isRoot + ", "
				+ "isAdmin=" + isAdmin + ", "
				+ "isAccountManager=" + isAccountManager + ", "
				+ "isAdminEditMode=" + isAdminEditMode + ", "
				+ "isAdminEditMode='" + isAdminEditMode + '\'' + '}';
	}
}