package com.hzsoft.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.Display;
import android.view.WindowManager;

public class CompressImage {
	    private Bitmap bm;
	    private String filePath;
	    private Activity context;
	    private File  file;
	    private ProgressDialog progressDialog;
	   
	    public CompressImage(Activity context,String filePath) {
	        this.filePath = filePath;
	        this.context=context;
	    }
	  
	    public void  proess(){
	     	 Bitmap bitmap;
				try {
					bitmap = getBitmap();
				} catch (Exception e1) {
					return ;
				} 
	             //���ɱ������ļ���
	             String path = Environment.getExternalStorageDirectory()
	             .getAbsolutePath() + "/"+System.currentTimeMillis()+".jpg";
	             //��ѹ��֮���ͼƬ���浽����
	             FileOutputStream fileOutputStream;
				try {
					fileOutputStream = new FileOutputStream(new File(path));
				} catch (FileNotFoundException e) {
					return ;
				}
	             bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fileOutputStream);
	              file = new File(path);  
	    }
	    
	    public File getFile() {
	  
			return file;
		}
	    public void delete(){
	    	if(file!=null&&file.exists()){
	    		file.delete();
	    	}
	    }

		public void setFile(File file) {
			this.file = file;
		}


		public Bitmap getBitmap() throws Exception{
	        BitmapFactory.Options opt = new BitmapFactory.Options();
	        // ���isjustdecodebounds����Ҫ
	        opt.inJustDecodeBounds = true;
	        bm = BitmapFactory.decodeFile(filePath, opt);

	        // ��ȡ�����ͼƬ��ԭʼ��Ⱥ͸߶�
	        int picWidth = opt.outWidth;
	        int picHeight = opt.outHeight;

	        // ��ȡ���Ŀ�Ⱥ͸߶�
	        WindowManager windowManager = context.getWindowManager();
	        Display display = windowManager.getDefaultDisplay();
	        int screenWidth = display.getWidth();
	        int screenHeight = display.getHeight();

	        // isSampleSize�Ǳ�ʾ��ͼƬ�����ų̶ȣ�����ֵΪ2ͼƬ�Ŀ�Ⱥ͸߶ȶ���Ϊ��ǰ��1/2
	        opt.inSampleSize = 1;
	        // �������Ĵ�С��ͼƬ��С��������ű���
	        if (picWidth > picHeight) {
	            if (picWidth > screenWidth)
	                opt.inSampleSize = picWidth / screenWidth;
	        } else {
	            if (picHeight > screenHeight)

	                opt.inSampleSize = picHeight / screenHeight;
	        }
	        // ���������������һ�������صģ����������˵�bitmap
	        opt.inJustDecodeBounds = false;
	        bm = BitmapFactory.decodeFile(filePath, opt);

	        return compressImage(bm,50);
	    }

	    private Bitmap compressImage(Bitmap image,int size) {
	        try {
	            ByteArrayOutputStream baos = new ByteArrayOutputStream();
	            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// ����ѹ������������100��ʾ��ѹ������ѹ��������ݴ�ŵ�baos��
	            int options = 100;
	            while (baos.toByteArray().length/1024 > size) { // ѭ���ж����ѹ����ͼƬ�Ƿ����50kb,���ڼ���ѹ��
	                baos.reset();// ����baos�����baos
	                image.compress(Bitmap.CompressFormat.JPEG, options, baos);// ����ѹ����options=50����ѹ��������ݴ�ŵ�baos��              
	                options -= 10;// ÿ�ζ�����10
	            }
	            ByteArrayInputStream isBm = new ByteArrayInputStream(
	                    baos.toByteArray());// ��ѹ���������baos��ŵ�ByteArrayInputStream��
	            Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// ��ByteArrayInputStream��������ͼƬ
//	          String path = Environment.getExternalStorageDirectory()
//	                  .getAbsolutePath() + "/compress.jpg";
//	          FileOutputStream fileOutputStream = new FileOutputStream(new File(
//	                  path));
//	          bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fileOutputStream);
	            return bitmap;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        } 
	    }
}
