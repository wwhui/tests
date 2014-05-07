package com.sdhz.thread;
//
//import android.content.Context;
//import android.os.Handler;
//import android.os.Looper;
//import android.os.Message;
//
//import com.hzsoft.dao.ImageDao;
//import com.hzsoft.dao.impl.ImageDaoImpl;
//import com.hzsoft.web.RemoteService;
//
//public class ImageThread extends Thread {
//	private int result = 0;
//	private String imageType;
//	private String maxId;
//	private Handler handler;
//	private RemoteService remoteService;
//	private ImageDao imageDao;
//	private Context context;
//
//	public ImageThread(String imageType, Context context,String maxId) {
//		this.imageType = imageType;
//		this.context = context;
//		this.maxId=maxId;
//	}
//	
//	@Override
//	public void run() {
//		Looper looper = Looper.getMainLooper();
//		handler = new MyHandler(looper);
//		remoteService = RemoteService.getInstance();
//		
//		String imageJson = remoteService.getImageData(imageType,maxId);
//		if(null != imageJson && !"".equals(imageJson)) {
//			imageDao = ImageDaoImpl.getInstance(context);
//			result = imageDao.addImageListData(imageJson, imageType);
//		} else {
//			result = -1;
//		}
//		Message msg = Message.obtain();
//		msg.arg1 = result;
//		handler.sendMessage(msg);
//	}
//	
//	class MyHandler extends Handler {
//		public MyHandler(Looper looper) {
//			super(looper);
//		}
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			if(msg.arg1 == 1) {
//	//			progressBar.setVisibility(View.GONE);
////				galleryShow();
//			} else if(msg.arg1 == -1) {
////				//连接服务器失败
//			}
//		}
//	}
//}
