package com.netpro.trinity.repository.service.externalrule;

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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.netpro.trinity.repository.dao.jpa.externalrule.DmExtPackageJPADao;
import com.netpro.trinity.repository.entity.externalrule.jdbc.DmExtJar;
import com.netpro.trinity.repository.entity.externalrule.jdbc.DmExtRule;
import com.netpro.trinity.repository.entity.externalrule.jpa.Dmextpackage;
import com.netpro.trinity.repository.prop.TrinitySysSetting;
import com.netpro.trinity.repository.util.Constant;
import com.netpro.trinity.repository.util.drivermanager.FileDetailUtil;
import com.netpro.trinity.repository.util.externalrule.ExernalRuleUtil;

@Service
public class DmExtPackageService {
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
	private ExernalRuleUtil ruleFileUtil;
	
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
		
		List<Dmextpackage> packages = this.dao.findBypackagenameLikeIgnoreCase("%"+name.toUpperCase()+"%", sort);
		
		if(null == withoutDetail || withoutDetail == false)
			getExternalJars(packages);
		
		return packages;
	}
	
	public Dmextpackage addPackage(Dmextpackage p) throws IllegalArgumentException, Exception{
		p.setPackageuid(UUID.randomUUID().toString());
		
		String packageName = p.getPackagename();
		if(null == packageName || packageName.trim().isEmpty())
			throw new IllegalArgumentException("Package Name can not be empty!");
		p.setPackagename(packageName.toUpperCase());
		
		if(this.dao.existByName(p.getPackagename()))
			throw new IllegalArgumentException("Duplicate Package Name!");
				
		if(null == p.getDescription())
			p.setDescription("");
		
		/*
		 * Because lastupdatetime column is auto created value, it can not be reload new value.
		 * here, we force to give value to lastupdatetime column.
		 */
		p.setLastupdatetime(new Date());
		
		this.dao.save(p);
		
		return p;
	}
	
	public Dmextpackage editPackage(Dmextpackage new_p) throws IllegalArgumentException, NoSuchAlgorithmException, IOException, Exception{
		String packageuid = new_p.getPackageuid();
//		if(null == packageuid || packageuid.trim().isEmpty())
//			throw new IllegalArgumentException("External Package Uid can not be empty!");
				
		String packageName = new_p.getPackagename();
		if(null == packageName || packageName.trim().isEmpty())
			throw new IllegalArgumentException("Package Name can not be empty!");
		packageName = packageName.toUpperCase();
		
		String description = new_p.getDescription();
		if(null == description || description.trim().isEmpty())
			description = "";
		
		Dmextpackage p = this.dao.findOne(packageuid);
		if(null == p)
			throw new IllegalArgumentException("External Package UID does not exist!(" + packageuid + ")");
		String oldPackageName = p.getPackagename();
		
		//改變所有影響到的resdoc及transformrule表中的值(改變resname及rule欄位值)
		List<DmExtRule> settingRules = this.ruleService.getSettingRulesByPackageUid(packageuid);
		for(DmExtRule settingRule: settingRules) {
			String oldFullRuleName = oldPackageName + "." + settingRule.getRulename();
			String newFullRuleName = packageName + "." + settingRule.getRulename();
			this.ruleService.updateExtDesc(oldFullRuleName, newFullRuleName);
		}
		
		//改變所有影響到的DmExtJar表中的值(改變File Name欄位)
		List<String> jarUids = new ArrayList<String>();
		List<DmExtJar> jars = this.jarService.getByPackageUid(packageuid);
		for(DmExtJar jar : jars) {
			String filename = jar.getFilename();
			int index = filename.indexOf(Constant.SEPARATOR);
			String newFileName = packageName + filename.substring(index);
			this.jarService.editFileNameOnly(jar.getExtjaruid(), newFileName);
			
			jarUids.add(jar.getExtjaruid());
		}
		
		//改變extrule.xml檔案中的package name
		String newCFG = ruleFileUtil.updateCfgPackageName(jarUids, packageName);
		ruleFileUtil.writeEextruleCfgFile(newCFG);
		//再把新的cfg定義回存至DB中
		this.ruleService.upLoadCfgToDB(newCFG);
		
		//對Package本身而言, 其實只是改變package name及description欄位之值
		p.setPackagename(packageName);
		p.setDescription(description);
		p = this.dao.save(p);
		
		//最後去改變package資料夾的名稱
		File file = new File(this.trinitySys.getDir().getExtlib(), oldPackageName);
		file.renameTo(new File(this.trinitySys.getDir().getExtlib(), packageName));
		
		return this.dao.save(p);
	}
	
	public Boolean importPackage(MultipartFile file) throws IllegalArgumentException, NoSuchAlgorithmException, FileNotFoundException,  
			IOException, Exception{
		if(null == file)
			throw new IllegalArgumentException("Import File can not be empty!");
		
		String fileName = file.getOriginalFilename();
		byte[] bytes = file.getBytes();
		String tempRootPath = this.trinitySys.getDir().getExtlib() + File.separator + "temp";
		ruleFileUtil.writeImportJarFile(bytes, tempRootPath, fileName);
		
		List<Map<String, String>> infoList = ruleFileUtil.getInfoFromJarConfigFile(tempRootPath, fileName);
		if(null == infoList || infoList.isEmpty())
			throw new FileNotFoundException("config.xml file can not be found in import jar file!");
		
		Map<String, String> packageInfo = infoList.get(0);//0的位置是取出package的name及desc資訊
		String packageName = packageInfo.get("packageName").toUpperCase();
		String packageDesc = packageInfo.get("packageDesc");
		//取出package的name及desc資訊後, 把它刪掉, 只留Rule資訊(如果有的話)
		infoList.remove(0);
		
		List<Dmextpackage> depList = this.dao.findBypackagenameIgnoreCase(packageName);//正常來說, 依設計只會有一筆資料
		
		if(depList.isEmpty()) {	//表示系統中沒有同名的External Package
			//建立 Package
			Dmextpackage new_dep = new Dmextpackage();
			new_dep.setPackageuid(UUID.randomUUID().toString());
			new_dep.setPackagename(packageName);
			new_dep.setDescription(packageDesc);
			this.dao.save(new_dep);
			
			//把原本在 temp 資料夾的檔案搬到此  Package 資料夾底下
			File tempJarFile = new File(tempRootPath, fileName);
			
			fileName = ruleFileUtil.fullFileName(packageName, fileName);
			ruleFileUtil.checkDir(fileName);
			File jarFile = new File(this.trinitySys.getDir().getExtlib(), fileName);
			
			Files.move(Paths.get(tempJarFile.getPath()), 
					Paths.get(jarFile.getPath()), StandardCopyOption.REPLACE_EXISTING);
			
			//取得新檔案的資料
			String md5 = FileDetailUtil.getFileMD5(jarFile);
			String fileType = ruleFileUtil.checkFileType(jarFile);
			//把新的檔案儲存至DB(DmExtJar)
			DmExtJar new_dej = this.jarService.uploadToDb(fileName, new_dep.getPackageuid(), bytes, md5, fileType, packageDesc);
			
			for(Map<String, String> ruleInfo : infoList) {
				String fullClass = ruleInfo.get("fullclass");
				String ruleName = ruleInfo.get("ruleName");
				String description = ruleInfo.get("description");
				
				DmExtRule newRule = new DmExtRule();
				newRule.setExtjaruid(new_dej.getExtjaruid());
				newRule.setActive("1");
				newRule.setDescription(description);
				newRule.setFullclasspath(fullClass);
				newRule.setRulename(ruleName);
				this.ruleService.addRule(newRule);
			}
		}else {
			
		}
		
//		try {
//			Document doc = getConfigDoc(fileName);
//			if (doc == null)
//				return NO_CONFIG + "";
//			
//			return parseConfig(doc, fileName) + "";
//		} catch (Exception e) {
//			return e.getMessage();
//		}
		return true;
	}
	
	
	public void deleteByUid(String uid) throws IllegalArgumentException, IOException, Exception{
		if(null == uid || uid.isEmpty())
			throw new IllegalArgumentException("External Package UID can not be empty!");
		
		this.jarService.deleteByPackageUid(uid);
		this.dao.delete(uid);
	}
	
	public boolean existByUid(String uid) throws Exception {
		return this.dao.exists(uid);
	}
	
	public Boolean existByName(String packageName) throws IllegalArgumentException, Exception{
		if(packageName == null || packageName.isEmpty())
			throw new IllegalArgumentException("Package Name can not be empty!");

		return this.dao.existByName(packageName.toUpperCase());
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
