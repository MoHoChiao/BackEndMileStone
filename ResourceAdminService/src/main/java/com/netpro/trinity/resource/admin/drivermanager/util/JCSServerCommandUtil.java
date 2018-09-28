package com.netpro.trinity.resource.admin.drivermanager.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.netpro.trinity.resource.admin.configuration.entity.Trinityconfig;
import com.netpro.trinity.resource.admin.configuration.service.TrinityconfigService;

@Component
public class JCSServerCommandUtil {
	@Autowired
	private TrinityconfigService configService;
	
	private String serverIP;
	private Integer serverPort;

	private void initJCSServerIP() throws Exception{
		List<Trinityconfig> configData = configService.getAll();
		for(Trinityconfig index_config : configData){
			serverIP = index_config.getPrimaryhost();
			serverPort = index_config.getPrimaryport();
		}
	}
	
	public String sendCommandToServer(String command) throws UnknownHostException,IOException,Exception{
		initJCSServerIP();
		
		Socket s = null;
		String output = "";
		
		try {
			// Create socket connection to specified ip and port
			s = new Socket(serverIP, serverPort.intValue());
			s.setSoLinger(true, 0);

			InputStream in = s.getInputStream();
			OutputStreamWriter out = new OutputStreamWriter(s.getOutputStream());
			out.write(command + "\n");
			out.flush();
			
			int ch = 0;
			try {
				while (true) {
					ch = in.read();
					if (ch == -1)
						break;

					output = output + ((char) ch);
				}

			} catch (IOException ex) {}

			out.close();
			in.close();
		} finally {
			if (s != null) {
				// Close socket connection
				try {
					s.close();
				} catch (IOException ex) {
				}
			}
		}
		
		return output;
	}
}

