package com.netpro.trinity.service.externalrule.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.netpro.trinity.service.agent.service.JCSAgentService;
import com.netpro.trinity.service.drivermanager.util.FileDetailUtil;
import com.netpro.trinity.service.dto.FilterInfo;
import com.netpro.trinity.service.dto.Ordering;
import com.netpro.trinity.service.dto.Paging;
import com.netpro.trinity.service.externalrule.dao.DmExtPackageJPADao;
import com.netpro.trinity.service.externalrule.dto.Publication;
import com.netpro.trinity.service.externalrule.dto.PublishRule;
import com.netpro.trinity.service.externalrule.entity.DmExtJar;
import com.netpro.trinity.service.externalrule.entity.DmExtRule;
import com.netpro.trinity.service.externalrule.entity.Dmextpackage;
import com.netpro.trinity.service.externalrule.lib.ExernalRuleUtil;
import com.netpro.trinity.service.prop.dto.TrinitySysSetting;
import com.netpro.trinity.service.util.Constant;

@Service
public class DmExtPackageService {
	private static final Logger LOGGER = LoggerFactory.getLogger(DmExtPackageService.class);

	public static final String[] PACKAGE_FIELD_VALUES = new String[] { "packagename" };
	public static final Set<String> PACKAGE_FIELD_SET = new HashSet<>(Arrays.asList(PACKAGE_FIELD_VALUES));

	@Autowired
	private DmExtPackageJPADao dao;

	@Autowired
	private TrinitySysSetting trinitySys;

	@Autowired
	private DmExtJarService jarService;
	@Autowired
	private DmExtRuleService ruleService;
	@Autowired
	private JCSAgentService agentService;

	@Autowired
	private ExernalRuleUtil ruleFileUtil;

	public List<Dmextpackage> getAll(Boolean withoutDetail) throws Exception {
		List<Dmextpackage> packages = this.dao.findAll();
		if (null == withoutDetail || withoutDetail == false)
			getExternalJars(packages);
		return packages;
	}

	public ResponseEntity<?> getByFilter(FilterInfo filter) throws Exception {
		Paging paging = filter.getPaging();
		Ordering ordering = filter.getOrdering();
		String param = filter.getParam();

		if (null == paging)
			paging = new Paging(0, 20);

		if (null == ordering)
			ordering = new Ordering("ASC", "packagename");

		if (null == param || param.trim().isEmpty())
			param = "%%";
		param = param.trim();

		Page<Dmextpackage> page_package = this.dao.findBypackagenameLikeIgnoreCase(param,
				getPagingAndOrdering(paging, ordering));
		// setProfileDataOnly(page_agents.getContent());
		return ResponseEntity.ok(page_package);
	}

	public Dmextpackage getByUid(Boolean withoutDetail, String uid) throws IllegalArgumentException, Exception {
		if (null == uid || uid.isEmpty())
			throw new IllegalArgumentException("External Package UID can not be empty!");

		Dmextpackage p = null;
		try {
			p = this.dao.findById(uid).get();
		} catch (NoSuchElementException e) {
		}

		if (null == p)
			throw new IllegalArgumentException("External Package UID does not exist!(" + uid + ")");

		if (null == withoutDetail || withoutDetail == false)
			getExternalJars(p);

		return p;
	}

	public List<Dmextpackage> getByName(Boolean withoutDetail, String name) throws IllegalArgumentException, Exception {
		if (null == name || name.isEmpty())
			throw new IllegalArgumentException("External Package Name can not be empty!");

		Direction direct = Direction.fromString("ASC");

		List<Dmextpackage> packages = this.dao.findBypackagenameLikeIgnoreCase("%" + name.toUpperCase() + "%",
				Sort.by(direct, "packagename"));

		if (null == withoutDetail || withoutDetail == false)
			getExternalJars(packages);

		return packages;
	}

	public List<PublishRule> getPublicationsByAgentUid(String agentUid) throws IllegalArgumentException, Exception {
		if (null == agentUid || agentUid.trim().isEmpty())
			throw new IllegalArgumentException("Agent Uid can not be empty!!");

		if (!this.agentService.existByUid(agentUid))
			throw new IllegalArgumentException("Agent Uid does not exist!!");

		List<String> rulePKStringList = ruleFileUtil.getRulePKStringByAgentUID(agentUid);

		List<PublishRule> publicationRules = this.ruleService.getAllExt();
		for (PublishRule publicationRule : publicationRules) {
			String jarUid = publicationRule.getExtjaruid();
			String ruleName = publicationRule.getRulename();
			if (rulePKStringList.contains(jarUid + ":" + ruleName)) {
				publicationRule.setPublished(true);
			} else {
				publicationRule.setPublished(false);
			}
		}

		return publicationRules;
	}

	public List<PublishRule> getPublicationsByAgentUid(String agentUid, Boolean hasPublished)
			throws IllegalArgumentException, Exception {
		if (null == agentUid || agentUid.trim().isEmpty())
			throw new IllegalArgumentException("Agent Uid can not be empty!!");

		if (!this.agentService.existByUid(agentUid))
			throw new IllegalArgumentException("Agent Uid does not exist!!");

		if (null == hasPublished)
			hasPublished = true;

		List<String> rulePKStringList = ruleFileUtil.getRulePKStringByAgentUID(agentUid);

		List<PublishRule> publicationRules = this.ruleService.getAllExt();
		List<PublishRule> onlyPublishedRules = new LinkedList<PublishRule>();
		for (PublishRule publicationRule : publicationRules) {
			String jarUid = publicationRule.getExtjaruid();
			String ruleName = publicationRule.getRulename();
			if (rulePKStringList.contains(jarUid + ":" + ruleName)) {
				if (hasPublished) {
					publicationRule.setPublished(true);
					onlyPublishedRules.add(publicationRule);
				}
			} else {
				if (!hasPublished) {
					publicationRule.setPublished(false);
					onlyPublishedRules.add(publicationRule);
				}
			}
		}

		return onlyPublishedRules;
	}

	public Dmextpackage addPackage(Dmextpackage p) throws IllegalArgumentException, Exception {
		p.setPackageuid(UUID.randomUUID().toString());

		String packageName = p.getPackagename();
		if (null == packageName || packageName.trim().isEmpty())
			throw new IllegalArgumentException("Package Name can not be empty!");
		p.setPackagename(packageName.toUpperCase());

		if (this.dao.existByName(p.getPackagename()))
			throw new IllegalArgumentException("Duplicate Package Name!");

		if (null == p.getDescription())
			p.setDescription("");

		/*
		 * Because lastupdatetime column is auto created value, it can not be
		 * reload new value. here, we force to give value to lastupdatetime
		 * column.
		 */
		p.setLastupdatetime(new Date());

		this.dao.save(p);

		return p;
	}

	public Dmextpackage editPackage(Dmextpackage new_p)
			throws IllegalArgumentException, NoSuchAlgorithmException, IOException, Exception {
		String packageuid = new_p.getPackageuid();
		// if(null == packageuid || packageuid.trim().isEmpty())
		// throw new IllegalArgumentException("External Package Uid can not be
		// empty!");

		String packageName = new_p.getPackagename();
		if (null == packageName || packageName.trim().isEmpty())
			throw new IllegalArgumentException("Package Name can not be empty!");
		packageName = packageName.toUpperCase();

		String description = new_p.getDescription();
		if (null == description || description.trim().isEmpty())
			description = "";

		Dmextpackage p = null;
		try {
			p = this.dao.findById(packageuid).get();
		} catch (NoSuchElementException e) {
		}

		if (null == p)
			throw new IllegalArgumentException("External Package UID does not exist!(" + packageuid + ")");
		String oldPackageName = p.getPackagename();

		// 改變所有影響到的resdoc及transformrule表中的值(改變resname及rule欄位值)
		List<DmExtRule> settingRules = this.ruleService.getSettingRulesByPackageUid(packageuid);
		for (DmExtRule settingRule : settingRules) {
			String oldFullRuleName = oldPackageName + "." + settingRule.getRulename();
			String newFullRuleName = packageName + "." + settingRule.getRulename();
			this.ruleService.updateExtDesc(oldFullRuleName, newFullRuleName);
		}

		// 改變所有影響到的DmExtJar表中的值(改變File Name欄位)
		List<String> jarUids = new ArrayList<String>();
		List<DmExtJar> jars = this.jarService.getByPackageUid(packageuid);
		for (DmExtJar jar : jars) {
			String filename = jar.getFilename();
			int index = filename.indexOf(Constant.SEPARATOR);
			String newFileName = packageName + filename.substring(index);
			this.jarService.editFileNameOnly(jar.getExtjaruid(), newFileName);

			jarUids.add(jar.getExtjaruid());
		}

		// 改變extrule.xml檔案中的package name
		String newCFG = ruleFileUtil.updateCfgPackageName(jarUids, packageName);
		ruleFileUtil.writeEextruleCfgFile(newCFG);
		// 再把新的cfg定義回存至DB中
		this.ruleService.upLoadCfgToDB(newCFG);

		// 對Package本身而言, 其實只是改變package name及description欄位之值
		p.setPackagename(packageName);
		p.setDescription(description);
		p = this.dao.save(p);

		// 最後去改變package資料夾的名稱
		File file = new File(this.trinitySys.getDir().getExtlib(), oldPackageName);
		file.renameTo(new File(this.trinitySys.getDir().getExtlib(), packageName));

		return this.dao.save(p);
	}

	public Boolean importPackage(MultipartFile file)
			throws IllegalArgumentException, NoSuchAlgorithmException, FileNotFoundException, IOException, Exception {
		if (null == file)
			throw new IllegalArgumentException("Import File can not be empty!");

		String fileName = file.getOriginalFilename();
		byte[] bytes = file.getBytes();
		String tempRootPath = this.trinitySys.getDir().getExtlib() + File.separator + "temp";
		ruleFileUtil.writeImportJarFile(bytes, tempRootPath, fileName);

		try {
			List<Map<String, String>> infoList = ruleFileUtil.getInfoFromJarConfigFile(tempRootPath, fileName);
			if (null == infoList || infoList.isEmpty())
				throw new FileNotFoundException("config.xml file can not be found in import jar file!");

			Map<String, String> packageInfo = infoList.get(0);// 0的位置是取出package的name及desc資訊
			String packageName = packageInfo.get("packageName").toUpperCase();
			String packageDesc = packageInfo.get("packageDesc");
			// 從設定檔中取出package的name及desc資訊後, 把它刪掉, 只留Rule資訊(如果有的話)
			infoList.remove(0);

			List<Dmextpackage> depList = this.dao.findBypackagenameIgnoreCase(packageName);// 正常來說,
																							// 依設計只會有一筆資料

			if (depList.isEmpty()) { // 表示系統中沒有同名的External Package
				// 建立 Package
				Dmextpackage new_dep = new Dmextpackage();
				new_dep.setPackageuid(UUID.randomUUID().toString());
				new_dep.setPackagename(packageName);
				new_dep.setDescription(packageDesc);
				this.dao.save(new_dep);

				// 把原本在 temp 資料夾的檔案搬到此 Package 資料夾底下
				File tempJarFile = new File(tempRootPath, fileName);

				fileName = ruleFileUtil.fullFileName(packageName, fileName);
				ruleFileUtil.checkDir(fileName);
				File jarFile = new File(this.trinitySys.getDir().getExtlib(), fileName);

				Files.move(Paths.get(tempJarFile.getPath()), Paths.get(jarFile.getPath()),
						StandardCopyOption.REPLACE_EXISTING);

				// 取得新檔案的資料
				String md5 = FileDetailUtil.getFileMD5(jarFile);
				String fileType = ruleFileUtil.checkFileType(jarFile);
				// 把新的檔案儲存至DB(DmExtJar)
				DmExtJar new_dej = this.jarService.uploadToDb(fileName, new_dep.getPackageuid(), bytes, md5, fileType,
						packageDesc);

				// 從設定檔中取出rule的相關資訊, 再儲存至DB
				for (Map<String, String> ruleInfo : infoList) {
					String fullClass = ruleInfo.get("fullClass");
					String ruleName = ruleInfo.get("ruleName");
					String description = ruleInfo.get("description");

					DmExtRule newRule = new DmExtRule();
					newRule.setExtjaruid(new_dej.getExtjaruid());
					newRule.setActive("1");
					newRule.setDescription(description);
					newRule.setFullclasspath(fullClass);
					newRule.setRulename(ruleName);
					try {
						this.ruleService.addRule(newRule);
					} catch (Exception e) {
						DmExtPackageService.LOGGER.error("Exception; reason was:", e);
					}
				}

				return true;
			} else {
				throw new IllegalArgumentException(
						"Package Name - '" + packageName + "' already exists. Please delete it first!");
			}
		} finally {
			try {
				File tempJarFile = new File(tempRootPath, fileName);
				if (tempJarFile.exists())
					tempJarFile.delete();
			} catch (Exception e) {
			}
		}
	}

	public Boolean publishPackage(List<Publication> publications)
			throws NoSuchAlgorithmException, IOException, Exception {
		// 取得系統中存在的所有agents
		List<String> allAgentUids = this.agentService.getAllAgentUids();

		// 這裡處理前端傳來的publications
		for (Publication publication : publications) {
			if (allAgentUids.contains(publication.getAgentuid()))
				allAgentUids.remove(publication.getAgentuid());// 剩下的都是未在前端點選設定的agents
		}

		// 把前端有點選設定的agent,以及所有其它未點選設定的agent,通通放到publications裡面
		for (String agentUid : allAgentUids) {
			List<PublishRule> rules = this.getPublicationsByAgentUid(agentUid);
			if (rules.size() > 0) {
				Publication publication = new Publication();
				publication.setAgentuid(agentUid);
				publication.setPublishRule(rules);
				publications.add(publication);
			}
		}

		String new_cfg = ruleFileUtil.writeEextruleCfg(publications); // 組成新的extrule.xml
		ruleFileUtil.writeEextruleCfgFile(new_cfg); // 寫出成檔案, 替換原本的extrule.xml
		this.ruleService.upLoadCfgToDB(new_cfg); // 把新的extrule.xml寫入DB
		return true;
	}

	public void deleteByUid(String uid) throws IllegalArgumentException, IOException, Exception {
		if (null == uid || uid.isEmpty())
			throw new IllegalArgumentException("External Package UID can not be empty!");

		this.jarService.deleteByPackageUid(uid);
		this.dao.deleteById(uid);
	}

	public boolean existByUid(String uid) throws Exception {
		return this.dao.existsById(uid);
	}

	public Boolean existByName(String packageName) throws IllegalArgumentException, Exception {
		if (packageName == null || packageName.isEmpty())
			throw new IllegalArgumentException("Package Name can not be empty!");

		return this.dao.existByName(packageName.toUpperCase());
	}

	private void getExternalJars(List<Dmextpackage> packages) throws Exception {
		for (Dmextpackage p : packages) {
			getExternalJars(p);
		}
	}

	private void getExternalJars(Dmextpackage p) throws Exception {
		p.setFiles(this.jarService.getByPackageUid(p.getPackageuid()));
	}

	private PageRequest getPagingAndOrdering(Paging paging, Ordering ordering) throws Exception {
		if (paging.getNumber() == null)
			paging.setNumber(0);

		if (paging.getSize() == null)
			paging.setSize(10);

		if (null != ordering) {
			return PageRequest.of(paging.getNumber(), paging.getSize(), getOrdering(ordering));
		} else {
			return PageRequest.of(paging.getNumber(), paging.getSize());
		}
	}

	private Sort getOrdering(Ordering ordering) throws Exception {
		Direction direct = Direction.fromString("ASC");
		if (ordering.getOrderType() != null && Constant.ORDER_TYPE_SET.contains(ordering.getOrderType().toUpperCase()))
			direct = Direction.fromString(ordering.getOrderType());

		if (ordering.getOrderField() != null && !ordering.getOrderField().isEmpty())
			return Sort.by(direct, ordering.getOrderField());
		else
			return Sort.by(direct, "packagename");
	}

}
