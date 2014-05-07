package com.hzsoft.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

public class AsyncBitmapLoader {
	/**
	 * �ڴ�ͼƬ�����û���
	 */
	private HashMap<String, SoftReference<Bitmap>> imageCache = null;
	public AsyncBitmapLoader() {
		imageCache = new HashMap<String, SoftReference<Bitmap>>();
	}
	 public static Bitmap small(Bitmap bitmap) {
		  Matrix matrix = new Matrix(); 
		  matrix.postScale(0.08f,0.08f); //���Ϳ�Ŵ���С�ı���
		  Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
		  return resizeBmp;
	 }
	public Bitmap loadBitmap(final ImageView imageView, final String imageURL, final ImageCallBack imageCallBack) {
		// ���ڴ滺���У��򷵻�Bitmap����
		if (imageCache.containsKey(imageURL)) {
			SoftReference<Bitmap> reference = imageCache.get(imageURL);
			Bitmap bitmap = reference.get();
			if (bitmap != null) {
				return bitmap;
			}
		} else {
			/** ����һ���Ա��ػ���Ĳ��� */
			String bitmapName = imageURL.substring(imageURL.lastIndexOf("/") + 1);
			File cacheDir = new File(FileUtil.SDPATH + "crp/");
			File[] cacheFiles = cacheDir.listFiles();
			int i = 0;
			if (null != cacheFiles) {
				for (; i < cacheFiles.length; i++) {
					if (bitmapName.equals(cacheFiles[i].getName())) {
						break;
					}
				}
				if (i < cacheFiles.length) {
					Bitmap bitmap = BitmapFactory.decodeFile(FileUtil.SDPATH + "crp/" + bitmapName);
					if(bitmap != null) {
						return bitmap;
					}
				}
			}
		}

		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				imageCallBack.imageLoad(imageView, (Bitmap) msg.obj);
			}
		};
		// ��������ڴ滺���У�Ҳ���ڱ��أ���jvm���յ����������߳�����ͼƬ
		new Thread() {
			@Override
			public void run() {
				InputStream is = HttpUtils.getStreamFromURLWithHttpClient(imageURL);
				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inSampleSize = 1;
				Bitmap bitmap = BitmapFactory.decodeStream(is, null, opts);
				if(bitmap != null) {
					imageCache.put(imageURL, new SoftReference<Bitmap>(bitmap));
					Message msg = handler.obtainMessage(0, bitmap);
					handler.sendMessage(msg);
					File dir = new File(FileUtil.SDPATH + "crp/");
					if (!dir.exists()) {
						dir.mkdirs();
					}
					File bitmapFile = new File(FileUtil.SDPATH + "crp/" + imageURL.substring(imageURL.lastIndexOf("/") + 1));
					if (!bitmapFile.exists()) {
						try {
							bitmapFile.createNewFile();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					FileOutputStream fos;
					try {
						fos = new FileOutputStream(bitmapFile);
						if(bitmap!=null) {
							bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
						}
						fos.flush();
						fos.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					Log.d("ͼƬ����ʧ��", imageURL);
				}
			}
		}.start();
		return null;
	}

	public interface ImageCallBack {
		public void imageLoad(ImageView imageView, Bitmap bitmap);
	}

}
