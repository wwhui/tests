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
	             //生成本来的文件名
	             String path = Environment.getExternalStorageDirectory()
	             .getAbsolutePath() + "/"+System.currentTimeMillis()+".jpg";
	             //将压缩之后的图片保存到本地
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
	        // 这个isjustdecodebounds很重要
	        opt.inJustDecodeBounds = true;
	        bm = BitmapFactory.decodeFile(filePath, opt);

	        // 获取到这个图片的原始宽度和高度
	        int picWidth = opt.outWidth;
	        int picHeight = opt.outHeight;

	        // 获取屏的宽度和高度
	        WindowManager windowManager = context.getWindowManager();
	        Display display = windowManager.getDefaultDisplay();
	        int screenWidth = display.getWidth();
	        int screenHeight = display.getHeight();

	        // isSampleSize是表示对图片的缩放程度，比如值为2图片的宽度和高度都变为以前的1/2
	        opt.inSampleSize = 1;
	        // 根据屏的大小和图片大小计算出缩放比例
	        if (picWidth > picHeight) {
	            if (picWidth > screenWidth)
	                opt.inSampleSize = picWidth / screenWidth;
	        } else {
	            if (picHeight > screenHeight)

	                opt.inSampleSize = picHeight / screenHeight;
	        }
	        // 这次再真正地生成一个有像素的，经过缩放了的bitmap
	        opt.inJustDecodeBounds = false;
	        bm = BitmapFactory.decodeFile(filePath, opt);

	        return compressImage(bm,50);
	    }

	    private Bitmap compressImage(Bitmap image,int size) {
	        try {
	            ByteArrayOutputStream baos = new ByteArrayOutputStream();
	            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
	            int options = 100;
	            while (baos.toByteArray().length/1024 > size) { // 循环判断如果压缩后图片是否大于50kb,大于继续压缩
	                baos.reset();// 重置baos即清空baos
	                image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩比options=50，把压缩后的数据存放到baos中              
	                options -= 10;// 每次都减少10
	            }
	            ByteArrayInputStream isBm = new ByteArrayInputStream(
	                    baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
	            Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
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
