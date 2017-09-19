package com.netpro.trinity.service.controller;

import java.io.FileInputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netpro.trinity.service.feign.DisconfigRepoClient;
import com.netpro.trinity.service.lib.JnlpLib;
import com.netpro.trinity.service.util.entity.Disconfig;
import com.netpro.trinity.service.util.error.exception.TrinityBadResponseWrapper;
import com.netpro.trinity.service.util.tool.ExceptionMsgFormat;

@CrossOrigin
@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/software")
public class JnlpController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JnlpController.class);
		
	@Autowired	//自動注入DisconfigRepoClient物件
	private DisconfigRepoClient repo;
	
	@Autowired	//自動注入JnlpLib物件
	private JnlpLib jnlpLib;
	
	@RequestMapping("/*")
	public ResponseEntity<?> getSoftwareJar(HttpServletRequest request, HttpServletResponse response) {
		String methodKey = "JnlpController#getSoftwareJar(...)";
		try {
			StringBuffer url = request.getRequestURL();
			String softwareName = url.substring(url.lastIndexOf("/")+1);
			InputStreamResource resource = new InputStreamResource(new FileInputStream(jnlpLib.getSoftwareFile(softwareName)));

			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.set("Content-Disposition","attachment;filename="+softwareName);
			
		    return ResponseEntity.ok()
		            .headers(httpHeaders)
		            .contentType(MediaType.parseMediaType("text/plain"))
		            .body(resource);
		} catch (Exception e) {
			JnlpController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionMsgFormat.get(500, methodKey, e.getMessage()));
		}
	}
	
	@PostMapping("/JFDesigner.jnlp")
	public ResponseEntity<?> getJfdesignerJnlp(HttpServletRequest request, HttpServletResponse response) {
		String methodKey = "JnlpController#getJfdesignerJnlp(...)";
		try {
			List<Disconfig> uiapPosition = repo.findServicePosition("server", "uiap", "serverIP", "serverPort");
			
			byte[] content = jnlpLib.getJFDesignerContent(request, uiapPosition).getBytes();
			ByteArrayResource resource = new ByteArrayResource(content);

			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.set("Content-Disposition","attachment;filename=JFDesigner.jnlp");
			
		    return ResponseEntity.ok()
		            .headers(httpHeaders)
		            .contentType(MediaType.parseMediaType("application/x-java-jnlp-file"))
		            .body(resource);
		}catch (TrinityBadResponseWrapper e) {
			JnlpController.LOGGER.error("TrinityBadResponseWrapper; reason was:\n"+e.getBody());
			return ResponseEntity.status(e.getStatus()).body(e.getBody());
		} catch (Exception e) {
			JnlpController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionMsgFormat.get(500, methodKey, e.getMessage()));
		}
	}
	
	@PostMapping("/TaskConsole.jnlp")
	public ResponseEntity<?> getTaskConsoleJnlp(HttpServletRequest request, HttpServletResponse response) {
		String methodKey = "JnlpController#getTaskConsoleJnlp(...)";
		try {
			List<Disconfig> uiapPosition = repo.findServicePosition("server", "uiap", "serverIP", "serverPort");
			
			byte[] content = jnlpLib.getTaskConsoleContent(request, uiapPosition).getBytes();
			ByteArrayResource resource = new ByteArrayResource(content);

			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.set("Content-Disposition","attachment;filename=TaskConsole.jnlp");
			
		    return ResponseEntity.ok()
		            .headers(httpHeaders)
		            .contentType(MediaType.parseMediaType("application/x-java-jnlp-file"))
		            .body(resource);
		} catch (Exception e) {
			JnlpController.LOGGER.error("Exception; reason was:", e.getCause());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionMsgFormat.get(500, methodKey, e.getMessage()));
		}
	}
	
	@PostMapping("/Metaman.jnlp")
	public ResponseEntity<?> getMetamanJnlp(HttpServletRequest request, HttpServletResponse response) {
		String methodKey = "JnlpController#getMetamanJnlp(...)";
		try {
			List<Disconfig> metamanPosition = repo.findServicePosition("server", "metamanserver", "serverIP", "serverPort");
			
			byte[] content = jnlpLib.getMetamanContent(request, metamanPosition).getBytes();
			ByteArrayResource resource = new ByteArrayResource(content);

			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.set("Content-Disposition","attachment;filename=Metaman.jnlp");
			
		    return ResponseEntity.ok()
		            .headers(httpHeaders)
		            .contentType(MediaType.parseMediaType("application/x-java-jnlp-file"))
		            .body(resource);
		} catch (Exception e) {
			JnlpController.LOGGER.error("Exception; reason was:", e.getCause());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionMsgFormat.get(500, methodKey, e.getMessage()));
		}
	}
	
	@PostMapping("/Updater.jnlp")
	public ResponseEntity<?> getUpdaterJnlp(HttpServletRequest request, HttpServletResponse response) {
		String methodKey = "JnlpController#getUpdaterJnlp(...)";
		try {
			List<Disconfig> uiapPosition = repo.findServicePosition("server", "uiap", "serverIP", "serverPort");
			
			byte[] content = jnlpLib.getUpdaterContent(request, uiapPosition).getBytes();
			ByteArrayResource resource = new ByteArrayResource(content);

			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.set("Content-Disposition","attachment;filename=Updater.jnlp");
			
		    return ResponseEntity.ok()
		            .headers(httpHeaders)
		            .contentType(MediaType.parseMediaType("application/x-java-jnlp-file"))
		            .body(resource);
		} catch (Exception e) {
			JnlpController.LOGGER.error("Exception; reason was:", e.getCause());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionMsgFormat.get(500, methodKey, e.getMessage()));
		}
	}
}
