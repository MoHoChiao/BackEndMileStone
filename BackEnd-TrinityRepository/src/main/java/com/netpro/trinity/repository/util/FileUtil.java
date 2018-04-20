package com.netpro.trinity.repository.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.springframework.stereotype.Component;

@Component
public class FileUtil {
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
	
	public void unZipFile(String sourcePath, String targetPath) throws IOException{
		File dir = new File(targetPath);
        // create output directory if it doesn't exist
        if(!dir.exists()) dir.mkdirs();
        
        FileInputStream fis = null;
        ZipInputStream zis = null;
        //buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(sourcePath);
            zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while(ze != null){
            	String fileName = ze.getName();
                File newFile = null;
                FileOutputStream fos = null;
            	try {
            		newFile = new File(targetPath + File.separator + fileName);
            		System.out.println("Unzipping to "+newFile.getAbsolutePath());
            		
            		//create directories for sub directories in zip
                    new File(newFile.getParent()).mkdirs();
            		fos = new FileOutputStream(newFile);
                    
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                    	fos.write(buffer, 0, len);
                    }
            	}finally {
            		if(null != fos)
            			fos.close();
                    //close this ZipEntry
            		if(null != zis)
            			zis.closeEntry();
            		
                    ze = zis.getNextEntry();
            	}
            }
        } finally {
        	//close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
        }
	}
}
