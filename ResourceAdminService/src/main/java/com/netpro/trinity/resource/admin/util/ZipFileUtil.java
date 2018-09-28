package com.netpro.trinity.resource.admin.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.springframework.stereotype.Component;

@Component
public class ZipFileUtil {
	public String zipFile(String sourcePath, String targetPath) throws IOException {
		FileOutputStream bos = null;
		ZipOutputStream zipOut = null;
		try {
			bos = new FileOutputStream(targetPath);
	        zipOut = new ZipOutputStream(bos);
	        File fileToZip = new File(sourcePath);
	 
	        doZipDir(fileToZip, fileToZip.getName(), zipOut);
		}finally {
			try {
				if(null != zipOut)
					zipOut.close();
				if(null != bos)
					bos.close();
			}catch(Exception e) {}
		}
		
		
		return new File(targetPath).getName();
	}
	
	public String zipFile(String sourcePath) throws IOException {
		FileOutputStream bos = null;
        ZipOutputStream zipOut = null;
        
        try {
        	bos = new FileOutputStream("jdbc.zip");
	        zipOut = new ZipOutputStream(bos);
	        File fileToZip = new File(sourcePath);
	        
        	doZipDir(fileToZip, fileToZip.getName(), zipOut);
        }finally {
        	try {
        		if(null != zipOut)
        			zipOut.close();
        		if(null != bos)
        			bos.close();
        	}catch(IOException e) {}
        }
        
		return "jdbc.zip";
	}
	
	private void doZipDir(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
		if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
            	doZipDir(childFile, fileName + "/" + childFile.getName(), zipOut);
            }
            return;
        }
        
        FileInputStream fis = null;
        try {
        	fis = new FileInputStream(fileToZip);
            ZipEntry zipEntry = new ZipEntry(fileName);
            zipOut.putNextEntry(zipEntry);
            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
        }finally {
        	try {
        		if(null != fis)
        			fis.close();
        	}catch(Exception e) {}
        }
	}
	
	public void unZipFile(String sourcePath, String targetPath) throws IOException, Exception{
		//Open the file
        try(ZipFile file = new ZipFile(sourcePath))
        {
        	FileSystem fileSystem = FileSystems.getDefault();
            //Get file entries
            Enumeration<? extends ZipEntry> entries = file.entries();
             
            //We will unzip files in this folder
            File targetDir = new File(targetPath);
            if(!targetDir.exists())
            	targetDir.mkdirs();
             
            //Iterate over entries
            while (entries.hasMoreElements())
            {
                ZipEntry entry = entries.nextElement();
                
                if(!entry.getName().toLowerCase().endsWith(".jar")
                		&& !entry.getName().toLowerCase().endsWith(".yml") 
                		&& !entry.getName().toLowerCase().endsWith(".properties"))
                	continue;
                
                Files.createDirectories(fileSystem.getPath(targetPath + entry.getName()).getParent());
                
                InputStream is = null;
                BufferedInputStream bis = null;
                FileOutputStream fileOutput = null;
            	try {
            		is = file.getInputStream(entry);
                    bis = new BufferedInputStream(is);
                    
                    String uncompressedFileName = targetPath + entry.getName();
                    Path uncompressedFilePath = fileSystem.getPath(uncompressedFileName);
                    Files.createFile(uncompressedFilePath);
                    fileOutput = new FileOutputStream(uncompressedFileName);
                    while (bis.available() > 0)
                    {
                        fileOutput.write(bis.read());
                    }
                    
                    System.out.println("Written :" + entry.getName());
            	}finally {
            		if(null != is)
            			is.close();
            		if(null != bis)
            			bis.close();
            		if(null != fileOutput)
            			fileOutput.close();	
            	}
            }
        }catch(Exception e) {
        	throw e;
        }
	}
}
