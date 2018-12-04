package com.nyist.download.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class FtpUtil {


	public static boolean uploadFile(String host, int port, String username, String password, String dir,String basePath,
									 String filename, InputStream input) {
		boolean result = false;
		FTPClient ftp = new FTPClient();

		try {
			int reply;
			ftp.connect(host, port);

			ftp.login(username, password);
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return result;
			}
			ftp.changeWorkingDirectory(basePath);
			String[] pah = dir.split("/");
			// 分层创建目录
			for (String pa : pah) {
				ftp.makeDirectory(new String(pa.getBytes("utf-8"),"iso-8859-1"));
				// 切到到对应目录
				ftp.changeWorkingDirectory(new String(pa.getBytes("utf-8"),"iso-8859-1"));
			}
			ftp.setFileType(FTP.BINARY_FILE_TYPE);

			if (!ftp.storeFile(new String(filename.getBytes("utf-8"),"iso-8859-1"), input)){
				return result;
			}
			input.close();
			ftp.logout();
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		return result;
	}


	public static boolean downloadFile(String host, int port, String username, String password, String remotePath,
									   String fileName, String localPath) {
		boolean result = false;
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			ftp.connect(host, port);

			ftp.login(username, password);
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return result;
			}
			ftp.changeWorkingDirectory(remotePath);
			FTPFile[] fs = ftp.listFiles();
			for (FTPFile ff : fs) {
				if (ff.getName().equals(fileName)) {
					File localFile = new File(localPath + "/" + ff.getName());

					OutputStream is = new FileOutputStream(localFile);
					ftp.retrieveFile(ff.getName(), is);
					is.close();
				}
			}

			ftp.logout();
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		return result;
	}
	//删除文件
	public static boolean deleteFile(String host, int port, String username, String password,String deletePath){
		boolean result = false;
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			ftp.connect(host, port);

			ftp.login(username, password);
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return result;
			}
			//ftp.changeWorkingDirectory(deletePath);
			ftp.deleteFile(new String(deletePath.getBytes("utf-8"),"iso-8859-1"));
			ftp.logout();
			result=true;
		}catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		return result;
	}

/*	public static void main(String[] args) {
		try {  
	        FileInputStream in=new FileInputStream(new File("D:\\temp\\image\\gaigeming.jpg"));  
	        boolean flag = uploadFile("192.168.25.133", 21, "ftpuser", "ftpuser", "/home/ftpuser/www/images","/2015/01/21", "gaigeming.jpg", in);  
	        System.out.println(flag);  
	    } catch (FileNotFoundException e) {  
	        e.printStackTrace();  
	    }  
	}*/
}
