package com.netpro.trinity.repository.service.externalrule;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netpro.trinity.repository.dao.jdbc.externalrule.DmExtRuleJDBCDao;
import com.netpro.trinity.repository.dto.externalrule.Publication;
import com.netpro.trinity.repository.entity.externalrule.jdbc.DmExtJar;
import com.netpro.trinity.repository.entity.externalrule.jdbc.DmExtRule;
import com.netpro.trinity.repository.entity.externalrule.jpa.Dmextpackage;
import com.netpro.trinity.repository.entity.resdoc.jpa.Resdoc;
import com.netpro.trinity.repository.entity.transformrule.jpa.Transformrule;
import com.netpro.trinity.repository.prop.TrinitySysSetting;
import com.netpro.trinity.repository.service.resdoc.ResdocService;
import com.netpro.trinity.repository.service.transformrule.TransformruleService;
import com.netpro.trinity.repository.util.Constant;
import com.netpro.trinity.repository.util.drivermanager.FileDetailUtil;
import com.netpro.trinity.repository.util.externalrule.ExernalRuleUtil;

@Service
public class DmExtRuleService {
	@Autowired
	private DmExtRuleJDBCDao dao;
	
	@Autowired
	private TrinitySysSetting trinitySys;
	
	@Autowired
	private DmExtPackageService packageService;
	@Autowired
	private DmExtJarService jarService;
	@Autowired
	private TransformruleService transService;
	@Autowired
	private ResdocService docService;
	
	@Autowired
	private ExernalRuleUtil ruleUtil;
	
	public List<DmExtRule> getAll() throws IllegalArgumentException, Exception{
		return this.dao.findAll();
	}
	
	public List<DmExtRule> getByExtJarUid(String extJarUid) throws IllegalArgumentException, Exception{
		if(extJarUid == null || extJarUid.isEmpty())
			throw new IllegalArgumentException("External Jar UID can not be empty!");

		return this.dao.findByExtJarUid(extJarUid);
	}
	
	public DmExtRule getByAllPKs(String extJarUid, String ruleName) throws IllegalArgumentException, Exception{
		if(extJarUid == null || extJarUid.isEmpty())
			throw new IllegalArgumentException("External Jar UID can not be empty!");
		
		if(ruleName == null || ruleName.isEmpty())
			throw new IllegalArgumentException("External Rule Name can not be empty!");

		return this.dao.findByAllPKs(extJarUid, ruleName.toUpperCase());
	}
	
	public List<String> getFullClassPathsByExtJarUid(String extJarUid) throws IllegalArgumentException, Exception{
		if(extJarUid == null || extJarUid.isEmpty())
			throw new IllegalArgumentException("External Jar UID can not be empty!");

		return this.dao.findFullClassPathsByExtJarUid(extJarUid);
	}
	
	public List<Publication> getPublishRulesByPackageUid(String packageUid) throws IllegalArgumentException, Exception{
		if(packageUid == null || packageUid.isEmpty())
			throw new IllegalArgumentException("External Package UID can not be empty!");

		return this.dao.findPublishRulesByPackageUid(packageUid);
	}
	
	public List<DmExtRule> getNonSettingRulesByExtJarUid(String extJarUid) throws IllegalArgumentException, 
				ZipException, IOException, Exception {
		if(null == extJarUid || extJarUid.trim().length() <= 0)
			throw new IllegalArgumentException("External Jar UID can not be empty!");
		
		DmExtJar jar = this.jarService.getByUid(extJarUid);
		if(null == jar)
			throw new IllegalArgumentException("External Jar UID does not exist!");
		
		List<DmExtRule> retList = new LinkedList<DmExtRule>();
		List<String> fullClassPaths = this.dao.findFullClassPathsByExtJarUid(extJarUid);
		
		String fileType = jar.getFiletype().trim();
		if(fileType.equals(Constant.RULE_JAR)) {
			List<String> clsNames = ruleUtil.getClsNames(new File(this.trinitySys.getDir().getExtlib(), jar.getFilename()));
			for(String clsName : clsNames) {
				if(!fullClassPaths.contains(clsName)) {
					DmExtRule ret_rule = new DmExtRule();
					ret_rule.setExtjaruid(extJarUid);
					ret_rule.setFullclasspath(clsName);
					
					retList.add(ret_rule);
				}
			}
		}else if(fileType.equals(Constant.RULE_CLASS)) {
			String clsName = ruleUtil.getClsName(jar.getFilename());
			if (clsName.trim().length() != 0 && !fullClassPaths.contains(clsName)){
				DmExtRule ret_rule = new DmExtRule();
				ret_rule.setExtjaruid(extJarUid);
				ret_rule.setFullclasspath(clsName);
				
				retList.add(ret_rule);
			}
		}
		
		return retList;
	}
	
	public List<DmExtRule> getSettingRulesByPackageUid(String packageUid) throws IllegalArgumentException, Exception{
		if(null == packageUid || packageUid.trim().isEmpty())
			throw new IllegalArgumentException("Package Uid can not be empty!!");
		
		List<DmExtRule> retList = new LinkedList<DmExtRule>();
		List<DmExtJar> list = this.jarService.getByPackageUid(packageUid);
		for (DmExtJar jar : list){
			List<DmExtRule> ruleList = this.dao.findByExtJarUid(jar.getExtjaruid());
			retList.addAll(ruleList);
		}
		return retList;
	}
	
	public DmExtRule addRule(DmExtRule rule) throws IllegalArgumentException, IOException, Exception{
		String extjaruid = rule.getExtjaruid();
		if(null == extjaruid || extjaruid.trim().isEmpty())
			throw new IllegalArgumentException("External Jar UID can not be empty!");
		
		String rulename = rule.getRulename();
		if(null == rulename || rulename.trim().isEmpty())
			throw new IllegalArgumentException("External Rule Name can not be empty!!");
		rule.setRulename(rulename.toUpperCase());
		
		if(this.dao.existByAllPKs(rule))
			throw new IllegalArgumentException("Duplicate External Rule!");
		
		String activate = rule.getActive();
		if(null == activate || (!activate.equals("1") && !activate.equals("0")))
			throw new IllegalArgumentException("External Rule activate value can only be 1 or 0!");
		
		String fullclasspath = rule.getFullclasspath();
		if(null == fullclasspath || fullclasspath.trim().isEmpty())
			throw new IllegalArgumentException("Full class path can not be empty!");
		
		String description = rule.getDescription();
		if(null == description)
			rule.setDescription("");
		
		if(this.dao.save(rule) > 0) {
			DmExtJar jar = this.jarService.getByUid(extjaruid);
			Dmextpackage p = this.packageService.getByUid(true, jar.getPackageuid());
			String pName = p.getPackagename();
			
			String ruleDesc = "";
			if (!jar.getFilename().endsWith(Constant.CLASSES))	//如果不是class檔, 即jar檔才可能有說明文件
				ruleDesc = ruleUtil.getRuleExtDesc(jar.getFilename(), rule.getRulename(), fullclasspath);	//取得說明文件內容
			
			insertExtDesc(pName + "." + rulename, ruleDesc);	//對resdoc及transformrule表做插入
			
			return rule;
		}else {
			throw new IllegalArgumentException("Add External Rule Fail!");
		}
	}
	
	public DmExtRule editRule(String oldRuleName, DmExtRule rule) throws IllegalArgumentException, NoSuchAlgorithmException, Exception{
		if(null == oldRuleName || oldRuleName.trim().isEmpty())
			throw new IllegalArgumentException("Target External Rule Name can not be empty!!");
		oldRuleName = oldRuleName.toUpperCase();
		
		String extjaruid = rule.getExtjaruid();
		if(null == extjaruid || extjaruid.trim().isEmpty())
			throw new IllegalArgumentException("External Jar UID can not be empty!");
		
		DmExtRule old_rule = this.dao.findByAllPKs(extjaruid, oldRuleName);
		if(null == old_rule)
			throw new IllegalArgumentException("Target External Rule Name does not exist!");
		
		String rulename = rule.getRulename();
		if(null == rulename || rulename.trim().isEmpty())
			throw new IllegalArgumentException("External Rule Name can not be empty!!");
		rule.setRulename(rulename.toUpperCase());
		
		if(this.dao.existByAllPKs(rule) && !old_rule.getRulename().equalsIgnoreCase(rule.getRulename()))
			throw new IllegalArgumentException("Duplicate External Rule Name!");
		
		String activate = rule.getActive();
		if(null == activate || (!activate.equals("1") && !activate.equals("0")))
			throw new IllegalArgumentException("External Rule activate value can only be 1 or 0!");
		
		//可能因為某種原因, 反正edit不能改fullclass
//		String fullclasspath = rule.getFullclasspath();
//		if(null == fullclasspath || fullclasspath.trim().isEmpty())
//			throw new IllegalArgumentException("Full class path can not be empty!");
		
		String description = rule.getDescription();
		if(null == description)
			rule.setDescription("");
		
		//修改Extrule.xml 檔案內有使用到的 Rule,並且更新到DmExtJar的表
		String cfg = ruleUtil.updateRuleNameOrActiveInCfg(rule.getExtjaruid(), oldRuleName, rule.getRulename(), rule.getActive());
		ruleUtil.writeEextruleCfgFile(cfg);
		upLoadCfgToDB(cfg);
		
		if(this.dao.update(old_rule.getRulename(), rule) > 0) {
			DmExtJar jar = this.jarService.getByUid(extjaruid);
			Dmextpackage p = this.packageService.getByUid(true, jar.getPackageuid());
			String pName = p.getPackagename();
			 
			String oldFullRuleName = pName + "." + oldRuleName;
			String newFullRuleName = pName + "." + rule.getRulename();
			updateExtDesc(oldFullRuleName, newFullRuleName);
			
			return rule;
		}else {
			throw new IllegalArgumentException("Edit External Rule Fail!");
		}
	}
	
	public void deleteByExtJarUid(String extJarUid) throws IllegalArgumentException, IOException, Exception{
		if(null == extJarUid || extJarUid.trim().length() <= 0)
			throw new IllegalArgumentException("External Jar UID can not be empty!");
		
		if(ruleUtil.haveRulePublishByJar(extJarUid))
			throw new IllegalArgumentException("External Rule(s) have been published to JCS Agnet!");
		
		DmExtJar jar = jarService.getByUid(extJarUid);
		Dmextpackage p = packageService.getByUid(true, jar.getPackageuid());
		
		List<DmExtRule> rules = this.dao.findByExtJarUid(extJarUid);
		for(DmExtRule rule : rules) {
			String ruleFullName = (p.getPackagename() + "." + rule.getRulename()).toLowerCase();
			
			docService.deleteByName(ruleFullName);
			transService.deleteByRule(ruleFullName);
		}
		
		this.dao.deleteByExtJarUid(extJarUid);
	}
	
	public void deleteByAllPKs(String extJarUid, String ruleName) throws IllegalArgumentException, IOException, Exception{
		if(null == extJarUid || extJarUid.trim().length() <= 0)
			throw new IllegalArgumentException("External Jar UID can not be empty!");
		
		if(null == ruleName || ruleName.trim().length() <= 0)
			throw new IllegalArgumentException("External Rule Name can not be empty!");
		
		if(ruleUtil.haveRulePublishByRule(extJarUid, ruleName))
			throw new IllegalArgumentException("External Rule has been published to JCS Agnet!");
		
		DmExtJar jar = jarService.getByUid(extJarUid);
		Dmextpackage p = packageService.getByUid(true, jar.getPackageuid());
		
		String ruleFullName = (p.getPackagename() + "." + ruleName).toLowerCase();
		
		docService.deleteByName(ruleFullName);
		transService.deleteByRule(ruleFullName);
		
		this.dao.deleteByAllPKs(extJarUid, ruleName);
	}
	
	//此方法不對外提供, 不可能單單改變這些值, 因為external rule模組, 可謂牽一髮而動全身啊@@"
	public void insertExtDesc(String fullRuleName, String doc) throws IllegalArgumentException, Exception{
		Resdoc resdoc = new Resdoc();			
		resdoc.setLangcode(Constant.LANGCODE_EN);
		resdoc.setModule(Constant.MODULE_EXTRULE);
		resdoc.setResname(fullRuleName.toLowerCase());
		resdoc.setDocument(doc);
		this.docService.modify(resdoc);
		
		Transformrule trans = new Transformrule();
		trans.setRule(fullRuleName.toLowerCase());
		trans.setNeedargument("1");
		trans.setRuledescription(doc);
		this.transService.modify(trans);
	}
	
	//此方法不對外提供, 不可能單單改變這些值, 因為external rule模組, 可謂牽一髮而動全身啊@@"
	public void updateExtDesc(String oldFullRuleName, String newFullRuleName) throws IllegalArgumentException, Exception{
		Resdoc resdoc = new Resdoc();
		resdoc.setLangcode(Constant.LANGCODE_EN);
		resdoc.setModule(Constant.MODULE_EXTRULE);
		resdoc.setResname(oldFullRuleName.toLowerCase());
		this.docService.editResNameOnly(newFullRuleName.toLowerCase(), resdoc);
		
		this.transService.editRuleOnly(newFullRuleName.toLowerCase(), oldFullRuleName.toLowerCase());
	}
	
	//此方法不對外提供, 不可能單單改變這些值, 因為external rule模組, 可謂牽一髮而動全身啊@@"
	public void upLoadCfgToDB(String cfg) throws NoSuchAlgorithmException, Exception{
		String fileName = Constant.EXT_RULE_CFG;
		String pUID = "def";
		byte[] bytes = cfg.getBytes();
		String md5 = FileDetailUtil.getFileMD5(new File(this.trinitySys.getDir().getExtlib(), Constant.EXT_RULE_CFG));
		this.jarService.uploadToDb(fileName, pUID, bytes, md5, "CFG", "");
	}
}
