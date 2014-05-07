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
	
	//����ͬ���ļ������ļ�����ʱ���޷������ļ����ļ��У���WINDOWS��ͨ���������ֺ��(2)ע�ļ�δ����׺�����;
	//�ļ����������׺���ļ��Ѿ����ڣ�����null�����򷵻��ļ�
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
	
	//SD��Ŀ¼�´����ļ���,����1���ļ����Ѿ����ڣ�����0���ļ��д����ɹ�
	public int createFolder(String folderName) {
		File file = new File(SDPATH+folderName);
		if(!file.exists()) {
			file.mkdir();
			return 0;
		}
		return 1;
	}
	
	//�ж��ļ����ļ����Ƿ����ͬ���ġ�
	public boolean isFileExists(String name) {
		File file = new File(SDPATH+name);
		return file.exists();
	}
}
