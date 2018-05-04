package com.netpro.trinity.repository.service.externalrule;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.netpro.trinity.repository.dao.jpa.externalrule.DmExtPackageJPADao;
import com.netpro.trinity.repository.entity.externalrule.jpa.Dmextpackage;

@Service
public class DmExtPackageService {
	public static final String[] PACKAGE_FIELD_VALUES = new String[] { "packagename" };
	public static final Set<String> PACKAGE_FIELD_SET = new HashSet<>(Arrays.asList(PACKAGE_FIELD_VALUES));
	
	@Autowired
	private DmExtPackageJPADao dao;
	
	@Autowired
	private DmExtJarService jarService;
	
	public List<Dmextpackage> getAll(Boolean withoutDetail) throws Exception{
		List<Dmextpackage> packages = this.dao.findAll();
		if(null == withoutDetail || withoutDetail == false)
			getExternalJars(packages);
		return packages;
	}
	
	public Dmextpackage getByUid(Boolean withoutDetail, String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.isEmpty())
			throw new IllegalArgumentException("External Package UID can not be empty!");
		
		Dmextpackage p = this.dao.findOne(uid);
		if(null == p)
			throw new IllegalArgumentException("External Package UID does not exist!(" + uid + ")");
		
		if(null == withoutDetail || withoutDetail == false)
			getExternalJars(p);
		
		return p;
	}
	
	public List<Dmextpackage> getByName(Boolean withoutDetail, String name) throws IllegalArgumentException, Exception{
		if(null == name || name.isEmpty())
			throw new IllegalArgumentException("External Package Name can not be empty!");
		
		Direction direct = Direction.fromStringOrNull("ASC");
		Order order = new Order(direct, "packagename");
		Sort sort = new Sort(order);
		
		List<Dmextpackage> packages = this.dao.findBypackagenameLikeIgnoreCase(name.toUpperCase(), sort);
		
		if(null == withoutDetail || withoutDetail == false)
			getExternalJars(packages);
		
		return packages;
	}
	
	public Dmextpackage add(String packageName, String description, MultipartFile file) throws IllegalArgumentException, Exception{
		Dmextpackage p = new Dmextpackage();
		
		p.setPackageuid(UUID.randomUUID().toString());
		
		if(null == packageName || packageName.trim().isEmpty())
			throw new IllegalArgumentException("Package Name can not be empty!");
		p.setPackagename(packageName.toUpperCase());
		
		if(this.dao.existByName(p.getPackagename()))
			throw new IllegalArgumentException("Duplicate Package Name!");
				
		if(null == description)
			p.setDescription("");
		
		/*
		 * because lastupdatetime column is auto created value, it can not be reload new value.
		 * here, we force to give value to lastupdatetime column.
		 */
		p.setLastupdatetime(new Date());
		
		this.dao.save(p);
//		this.jarService.add(jar);
		
		
//		List<DmExtJar> files = p.getFiles();
//		if(null != files && files.size() > 0) {
//			int[] returnValue = this.jarService.addBatch(p.getPackageuid(), files);
//			for(int i=0; i<returnValue.length; i++) {//只有插入成功的會留下來傳回前端
//				if(returnValue[i] == 0) {
//					files.remove(i);
//				}
//			}
//			p.setFiles(files);
//		}
		
		return p;
	}
	
//	public Domain edit(Domain domain) throws IllegalArgumentException, Exception{
//		String domainuid = domain.getDomainuid();
//		if(null == domainuid || domainuid.trim().isEmpty())
//			throw new IllegalArgumentException("Domain Uid can not be empty!");
//
//		Domain old_domain = this.dao.findOne(domainuid);
//		if(null == old_domain)
//			throw new IllegalArgumentException("Domain Uid does not exist!(" + domainuid + ")");
//		
//		String domainname = domain.getName();
//		if(null == domainname || domainname.trim().isEmpty())
//			throw new IllegalArgumentException("Domain Name can not be empty!");
//		domain.setName(domainname.toUpperCase());
//		
//		if(this.dao.existByName(domain.getName()) && !old_domain.getName().equalsIgnoreCase(domain.getName()))
//			throw new IllegalArgumentException("Duplicate Domain Name!");
//		
//		if(null == domain.getDescription())
//			domain.setDescription("");
//		
//		/*
//		 * because lastupdatetime column is auto created value, it can not be reload new value.
//		 * here, we force to give value to lastupdatetime column.
//		 */
//		domain.setLastupdatetime(new Date());
//		
//		this.dao.save(domain);
//		
//		List<DomainVariable> vars = domain.getDomainVars();
//		if(null != vars && vars.size() > 0) {
//			this.varService.deleteByDomainUid(domain.getDomainuid());
//			int[] returnValue = this.varService.addBatch(domain.getDomainuid(), vars);
//			for(int i=0; i<returnValue.length; i++) {//重設Domain Vars, 只有插入成功的會留下來傳回前端
//				if(returnValue[i] == 0) {
//					vars.remove(i);
//				}
//			}
//			domain.setDomainVars(vars);
//		}
//		
//		List<DomainResource> resources = domain.getDomainResources();
//		this.resourceService.deleteByDomainUid(domain.getDomainuid());
//		if(null != resources && resources.size() > 0) {
//			int[] returnValue = this.resourceService.addBatch(domain.getDomainuid(), resources);
//			for(int i=0; i<returnValue.length; i++) {//重設Domain Resources, 只有插入成功的會留下來傳回前端
//				if(returnValue[i] == 0) {
//					resources.remove(i);
//				}
//			}
//		}else {
//			DomainResource resource = new DomainResource();
//			resource.setDomainuid(domain.getDomainuid());
//			resource.setResourcename("TD-BULKLOAD");
//			resource.setResourcevalue("1");
//			this.resourceService.add(resource);
//			resources.add(resource);
//		}
//		domain.setDomainResources(resources);
//		
//		return domain;
//	}
	
//	public void deleteByUid(String uid) throws IllegalArgumentException, Exception{
//		if(null == uid || uid.trim().length() <= 0)
//			throw new IllegalArgumentException("Domain Uid can not be empty!");
//		
//		if(uid.trim().equalsIgnoreCase("default"))
//			throw new IllegalArgumentException("Default Domain can not be removed!");
//		
//		if(uid.trim().equalsIgnoreCase("global"))
//			throw new IllegalArgumentException("Global Domain can not be removed!");
//		
//		if(jobService.existByDomainuid(uid)) {
//			throw new IllegalArgumentException("Referenceing by job");
//		}else if(objectAliasService.existByObjectuid(uid)) {
//			throw new IllegalArgumentException("Referenceing by Object Alias");
//		}else {
//			resourceService.deleteByDomainUid(uid);
//			varService.deleteByDomainUid(uid);
//			this.dao.delete(uid);
//		}
//	}
	
	public boolean existByUid(String uid) throws Exception {
		return this.dao.exists(uid);
	}
	
	private void getExternalJars(List<Dmextpackage> packages) throws Exception {
		for(Dmextpackage p : packages) {
			getExternalJars(p);
		}
	}
	
	private void getExternalJars(Dmextpackage p) throws Exception {
		p.setFiles(this.jarService.getByPackageUid(p.getPackageuid()));
	}
}
