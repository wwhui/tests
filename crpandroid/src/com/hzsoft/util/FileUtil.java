package com.hzsoft.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.os.Environment;

public class FileUtil {
	public static String SDPATH = Environment.getExternalStorageDirectory() + "/";
	private static final int BUFFER_SIZE = 400 * 1024;
	
	public int write2SDFromInputStream(String path, String fileName, InputStream is) {
		File file = null;
		OutputStream os = null;
		try {
			createFolder(path);
			file = createFile(path+fileName);
			os = new FileOutputStream(file);
			byte[] buffer = new byte[BUFFER_SIZE];
			int temp;
			while ((temp = is.read(buffer)) != -1) {
				os.write(buffer,0,temp);
			}
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}
	
	//有相同的文件名或文件夹名时，无法创建文件或文件夹，在WINDOWS中通常是在名字后加(2)注文件未带后缀的情况;
	//文件名必须带后缀。文件已经存在，返回null，否则返回文件
	public File createFile(String fileName) throws IOException {
		File file = new File(SDPATH+fileName);
	//	if(!file.exists()) {
			if(file.createNewFile()) return file;
	//	}
		return file;
	}
	
	public File createFile(String dir,String fileName) throws IOException {
		File file = new File(SDPATH+dir+File.separator+fileName);
	//	if(!file.exists()) {
			if(file.createNewFile()) return file;
	//	}
		return null;
	}
	
	//SD卡目录下创建文件夹,返回1，文件夹已经存在，返回0，文件夹创建成功
	public int createFolder(String folderName) {
		File file = new File(SDPATH+folderName);
		if(!file.exists()) {
			file.mkdir();
			return 0;
		}
		return 1;
	}
	
	//判断文件或文件夹是否存在同名的。
	public boolean isFileExists(String name) {
		File file = new File(SDPATH+name);
		return file.exists();
	}
}
