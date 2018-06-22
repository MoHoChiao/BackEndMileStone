package com.netpro.trinity.service.jnlp.controller;

import java.io.FileInputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netpro.trinity.service.configuration.entity.Disconfig;
import com.netpro.trinity.service.configuration.service.DisconfigService;
import com.netpro.trinity.service.jnlp.service.JnlpService;
import com.netpro.trinity.service.util.ExceptionMsgFormat;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/software")
public class JnlpController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JnlpController.class);
		
	@Autowired	//自動注入DisconfigService物件
	private DisconfigService configService;
	
	@Autowired	//自動注入JnlpService物件
	private JnlpService jnlpService;
	
	@RequestMapping("/*")
	public ResponseEntity<?> getSoftwareJar(HttpServletRequest request) {
		String methodKey = "JnlpController#getSoftwareJar(...)";
		try {
			StringBuffer url = request.getRequestURL();
			String softwareName = url.substring(url.lastIndexOf("/")+1);
			InputStreamResource resource = new InputStreamResource(new FileInputStream(jnlpService.getSoftwareFile(softwareName)));

			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.set("Content-Disposition","attachment;filename="+softwareName);
			
		    return ResponseEntity.ok()
		            .headers(httpHeaders)
		            .contentType(MediaType.parseMediaType("text/plain"))
		            .body(resource);
		} catch (Exception e) {
			JnlpController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionMsgFormat.get(500, methodKey, e.getCause().toString()));
		}
	}
	
	@PostMapping("/JFDesigner.jnlp")
	public ResponseEntity<?> getJfdesignerJnlp(HttpServletRequest request) {
		String methodKey = "JnlpController#getJfdesignerJnlp(...)";
		try {
			List<Disconfig> uiapPosition = configService.getUiapPosition("server", "uiap", "serverIP", "serverPort");
			
			byte[] content = jnlpService.getJFDesignerContent(request, uiapPosition).getBytes();
			ByteArrayResource resource = new ByteArrayResource(content);

			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.set("Content-Disposition","attachment;filename=JFDesigner.jnlp");
			
		    return ResponseEntity.ok()
		            .headers(httpHeaders)
		            .contentType(MediaType.parseMediaType("application/x-java-jnlp-file"))
		            .body(resource);
		} catch (Exception e) {
			JnlpController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionMsgFormat.get(500, methodKey, e.getCause().toString()));
		}
	}
	
	@PostMapping("/TaskConsole.jnlp")
	public ResponseEntity<?> getTaskConsoleJnlp(HttpServletRequest request) {
		String methodKey = "JnlpController#getTaskConsoleJnlp(...)";
		try {
			List<Disconfig> uiapPosition = configService.getUiapPosition("server", "uiap", "serverIP", "serverPort");
			
			byte[] content = jnlpService.getTaskConsoleContent(request, uiapPosition).getBytes();
			ByteArrayResource resource = new ByteArrayResource(content);

			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.set("Content-Disposition","attachment;filename=TaskConsole.jnlp");
			
		    return ResponseEntity.ok()
		            .headers(httpHeaders)
		            .contentType(MediaType.parseMediaType("application/x-java-jnlp-file"))
		            .body(resource);
		} catch (Exception e) {
			JnlpController.LOGGER.error("Exception; reason was:", e.getCause());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionMsgFormat.get(500, methodKey, e.getCause().toString()));
		}
	}
	
	@PostMapping("/Metaman.jnlp")
	public ResponseEntity<?> getMetamanJnlp(HttpServletRequest request) {
		String methodKey = "JnlpController#getMetamanJnlp(...)";
		try {
			List<Disconfig> metamanPosition = configService.getUiapPosition("server", "metamanserver", "serverIP", "serverPort");
			
			byte[] content = jnlpService.getMetamanContent(request, metamanPosition).getBytes();
			ByteArrayResource resource = new ByteArrayResource(content);

			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.set("Content-Disposition","attachment;filename=Metaman.jnlp");
			
		    return ResponseEntity.ok()
		            .headers(httpHeaders)
		            .contentType(MediaType.parseMediaType("application/x-java-jnlp-file"))
		            .body(resource);
		} catch (Exception e) {
			JnlpController.LOGGER.error("Exception; reason was:", e.getCause());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionMsgFormat.get(500, methodKey, e.getCause().toString()));
		}
	}
	
	@PostMapping("/Updater.jnlp")
	public ResponseEntity<?> getUpdaterJnlp(HttpServletRequest request) {
		String methodKey = "JnlpController#getUpdaterJnlp(...)";
		try {
			List<Disconfig> uiapPosition = configService.getUiapPosition("server", "uiap", "serverIP", "serverPort");
			
			byte[] content = jnlpService.getUpdaterContent(request, uiapPosition).getBytes();
			ByteArrayResource resource = new ByteArrayResource(content);

			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.set("Content-Disposition","attachment;filename=Updater.jnlp");
			
		    return ResponseEntity.ok()
		            .headers(httpHeaders)
		            .contentType(MediaType.parseMediaType("application/x-java-jnlp-file"))
		            .body(resource);
		} catch (Exception e) {
			JnlpController.LOGGER.error("Exception; reason was:", e.getCause());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionMsgFormat.get(500, methodKey, e.getCause().toString()));
		}
	}
}
