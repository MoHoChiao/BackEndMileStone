package com.netpro.trinity.resource.admin.job.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netpro.trinity.resource.admin.dto.FilterInfo;
import com.netpro.trinity.resource.admin.job.entity.Busentity;
import com.netpro.trinity.resource.admin.job.entity.Job;
import com.netpro.trinity.resource.admin.job.entity.Jobcategory;
import com.netpro.trinity.resource.admin.job.service.BusentityService;
import com.netpro.trinity.resource.admin.job.service.JobService;
import com.netpro.trinity.resource.admin.job.service.JobcategoryService;
import com.netpro.trinity.resource.admin.util.JsTreeItem;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/job")
public class JobController {
	private static final Logger LOGGER = LoggerFactory.getLogger(JobController.class);
		
	@Autowired
	private JobService service;
	@Autowired
	private BusentityService busService;
	@Autowired
	private JobcategoryService cateService;
	
	@GetMapping("/findAll")
	public ResponseEntity<?> findAllJobs() {
		try {
			return ResponseEntity.ok(this.service.getAll());
		}catch(Exception e) {
			JobController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findByUid")
	public ResponseEntity<?> findJobByUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.getByUid(uid));
		}catch(IllegalArgumentException e) {
			JobController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			JobController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
  
	@GetMapping("/findByName")
	public ResponseEntity<?> findJobsByName(String name) {
		try {
			return ResponseEntity.ok(this.service.getByName(name));
		}catch(IllegalArgumentException e) {
			JobController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			JobController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findByCategoryUid")
	public ResponseEntity<?> findJobsByCategoryUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.getByCategoryUid(uid));
		}catch(IllegalArgumentException e) {
			JobController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			JobController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findJobFullPathByUid")
	public ResponseEntity<?> findJobFullPathByUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.getJobFullPathByUid(uid));
		}catch(IllegalArgumentException e) {
			JobController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			JobController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/findByFilter")
	public ResponseEntity<?> findJobsByFilter(String categoryUid, @RequestBody FilterInfo filter) {
		try {
			return this.service.getByFilter(categoryUid, filter);
		}catch(Exception e) {
			JobController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("/findForTree")
	public ResponseEntity<?> findForTree(HttpServletRequest request, @RequestBody Map<String, Object> map) {
		String bus_icon = (String) map.get("l1_icon");
		String cate_icon = (String) map.get("l2_icon");
		String job_icon = (String) map.get("l3_icon");
		List<String> jobUidList = (List<String>) map.get("jobUids");
		
		try {
			List<JsTreeItem> nodeList = new ArrayList<JsTreeItem>();
			List<Busentity> busList = this.busService.getAll(false);
			
			for (Busentity bus : busList) {
				JsTreeItem busNode = new JsTreeItem();
				busNode.setId(bus.getBusentityuid());
				busNode.setName(bus.getBusentityname());
				busNode.setIcon(bus_icon);
				
				List<JsTreeItem> childCateList = new ArrayList<JsTreeItem>();
				List<Jobcategory> cateList = this.cateService.getByEntityUid(bus.getBusentityuid());
				for (Jobcategory cate : cateList) {
					JsTreeItem cateNode = new JsTreeItem();
					cateNode.setId(cate.getCategoryuid());
					cateNode.setName(cate.getCategoryname());
					cateNode.setIcon(cate_icon);
					
					List<JsTreeItem> childJobList = new ArrayList<JsTreeItem>();
					List<Job> jobList = this.service.getByCategoryUid(cate.getCategoryuid());
					for (Job job : jobList) {
						JsTreeItem jobNode = new JsTreeItem();
						jobNode.setId(job.getJobuid());
						jobNode.setName(job.getJobname());
						jobNode.setIcon(job_icon);
						
						if (jobUidList.contains(job.getJobuid())) {
							jobNode.setDisabled(true);
						}
						
						childJobList.add(jobNode);
					}
					
					cateNode.setChildren(childJobList);
					childCateList.add(cateNode);
				}
				
				busNode.setChildren(childCateList);
				nodeList.add(busNode);
			}
			
			return ResponseEntity.ok(nodeList);
		} catch (IllegalArgumentException e) {
			JobController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		} catch (Exception e) {
			JobController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
  
//	@PostMapping("/add")
//	public ResponseEntity<?> addFileSource(@RequestBody FileSource filesource) {
//		try {
//			return ResponseEntity.ok(this.service.add(filesource));
//		}catch(IllegalArgumentException e) {
//			JobController.LOGGER.error("IllegalArgumentException; reason was:", e);
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
//		}catch(Exception e) {
//			JobController.LOGGER.error("Exception; reason was:", e);
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
//		}
//	}
//	
//	@PostMapping("/edit")
//	public ResponseEntity<?> editFileSource(@RequestBody FileSource filesource) {
//		try {
//			return ResponseEntity.ok(this.service.edit(filesource));
//		}catch(IllegalArgumentException e) {
//			JobController.LOGGER.error("IllegalArgumentException; reason was:", e);
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
//		}catch(Exception e) {
//			JobController.LOGGER.error("Exception; reason was:", e);
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
//		}
//	}
//  
//	@GetMapping("/delete")
//	public ResponseEntity<?> deleteFileSourceByUid(String uid) {
//		try {
//			this.service.deleteByUid(uid);
//		}catch(IllegalArgumentException e) {
//			JobController.LOGGER.error("IllegalArgumentException; reason was:", e);
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
//		}catch(Exception e) {
//			JobController.LOGGER.error("Exception; reason was:", e);
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
//		}
//		return ResponseEntity.ok(uid);
//	}
	
	@GetMapping("/isExistByUid")
	public ResponseEntity<?> isJobExistByUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.existByUid(uid));
		}catch(IllegalArgumentException e) {
			JobController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			JobController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/isExistByName")
	public ResponseEntity<?> isJobExistByName(String name) {
		try {
			return ResponseEntity.ok(this.service.existByName(name));
		}catch(IllegalArgumentException e) {
			JobController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			JobController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/isExistByFilesourceuid")
	public ResponseEntity<?> isJobExistByFilesourceuid(String filesourceuid) {
		try {
			return ResponseEntity.ok(this.service.existByFilesourceuid(filesourceuid));
		}catch(IllegalArgumentException e) {
			JobController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			JobController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/isExistByFrequencyuid")
	public ResponseEntity<?> isJobExistByFrequencyuid(String frequencyuid) {
		try {
			return ResponseEntity.ok(this.service.existByFrequencyuid(frequencyuid));
		}catch(IllegalArgumentException e) {
			JobController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			JobController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/isExistByDomainuid")
	public ResponseEntity<?> isJobExistByDomainuid(String domainuid) {
		try {
			return ResponseEntity.ok(this.service.existByDomainuid(domainuid));
		}catch(IllegalArgumentException e) {
			JobController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			JobController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
