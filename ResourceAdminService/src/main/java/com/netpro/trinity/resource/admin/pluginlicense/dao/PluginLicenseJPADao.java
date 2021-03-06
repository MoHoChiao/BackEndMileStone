package com.netpro.trinity.resource.admin.pluginlicense.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.resource.admin.pluginlicense.entity.Plugin;

@Repository
public interface PluginLicenseJPADao extends JpaRepository<Plugin, String> {
//	@Query("select NEW com.netpro.trinity.resource.admin.pluginlicense.entity.PluginLicenseStatus(pl.pluginid, p.pluginname, pl.licensekey)"
//			+ " from plugin p join p.pluginlicense pl")
//	List<PluginLicenseStatus> getPluginLicenseStatus();

}
