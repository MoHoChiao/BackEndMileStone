package com.netpro.trinity.resource.admin.job.controller;

import java.lang.reflect.InvocationTargetException;
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
import com.netpro.trinity.resource.admin.job.entity.JobFlow;
import com.netpro.trinity.resource.admin.job.entity.Jobcategory;
import com.netpro.trinity.resource.admin.job.service.BusentityService;
import com.netpro.trinity.resource.admin.job.service.JobFlowService;
import com.netpro.trinity.resource.admin.job.service.JobcategoryService;
import com.netpro.trinity.resource.admin.util.JsTreeItem;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/jobflow")
public class JobFlowController {
	private static final Logger LOGGER = LoggerFactory.getLogger(JobFlowController.class);
		
	@Autowired
	private JobFlowService service;
	@Autowired
	private BusentityService busService;
	@Autowired
	private JobcategoryService cateService;
	
	@GetMapping("/findAll")
	public ResponseEntity<?> findAllJobFlows() {
		try {
			return ResponseEntity.ok(this.service.getAll());
		}catch(Exception e) {
			JobFlowController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findByUid")
	public ResponseEntity<?> findJobFlowByUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.getByUid(uid));
		}catch(IllegalArgumentException e) {
			JobFlowController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			JobFlowController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
  
	@GetMapping("/findByName")
	public ResponseEntity<?> findJobFlowsByName(String name) {
		try {
			return ResponseEntity.ok(this.service.getByName(name));
		}catch(IllegalArgumentException e) {
			JobFlowController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			JobFlowController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findByCategoryUid")
	public ResponseEntity<?> findJobFlowsByCategoryUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.getByCategoryUid(uid));
		}catch(IllegalArgumentException e) {
			JobFlowController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			JobFlowController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/findByFilter")
	public ResponseEntity<?> findJobsByFilter(String categoryUid, @RequestBody FilterInfo filter) {
		try {
			return this.service.getByFilter(categoryUid, filter);
		}catch(SecurityException e) {
			JobFlowController.LOGGER.error("SecurityException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(NoSuchMethodException e) {
			JobFlowController.LOGGER.error("NoSuchMethodException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(IllegalAccessException e) {
			JobFlowController.LOGGER.error("IllegalAccessException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(InvocationTargetException e) {
			JobFlowController.LOGGER.error("InvocationTargetException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(IllegalArgumentException e) {
			JobFlowController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			JobFlowController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("/findForTree")
	public ResponseEntity<?> findForTree(HttpServletRequest request, @RequestBody Map<String, Object> map) {
		String bus_icon = (String) map.get("l1_icon");
		String cate_icon = (String) map.get("l2_icon");
		String flow_icon = (String) map.get("l3_icon");
		List<String> flowUidList = (List<String>) map.get("flowUids");
		
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
					
					List<JsTreeItem> childFlowList = new ArrayList<JsTreeItem>();
					List<JobFlow> flowList = this.service.getByCategoryUid(cate.getCategoryuid());
					for (JobFlow flow : flowList) {
						JsTreeItem flowNode = new JsTreeItem();
						flowNode.setId(flow.getJobflowuid());
						flowNode.setName(flow.getFlowname());
						flowNode.setIcon(flow_icon);
						
						if (flowUidList.contains(flow.getJobflowuid())) {
							flowNode.setDisabled(true);
						}
						
						childFlowList.add(flowNode);
					}
					
					cateNode.setChildren(childFlowList);
					childCateList.add(cateNode);
				}
				
				busNode.setChildren(childCateList);
				nodeList.add(busNode);
			}
			
			return ResponseEntity.ok(nodeList);
		} catch (IllegalArgumentException e) {
			JobFlowController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		} catch (Exception e) {
			JobFlowController.LOGGER.error("Exception; reason was:", e);
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
	public ResponseEntity<?> isJobFlowExistByUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.existByUid(uid));
		}catch(IllegalArgumentException e) {
			JobFlowController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			JobFlowController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/isExistByName")
	public ResponseEntity<?> isJobFlowExistByName(String name) {
		try {
			return ResponseEntity.ok(this.service.existByName(name));
		}catch(IllegalArgumentException e) {
			JobFlowController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			JobFlowController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/isExistByFrequencyuid")
	public ResponseEntity<?> isJobFlowExistByFrequencyuid(String frequencyuid) {
		try {
			return ResponseEntity.ok(this.service.existByFrequencyuid(frequencyuid));
		}catch(IllegalArgumentException e) {
			JobFlowController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			JobFlowController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
