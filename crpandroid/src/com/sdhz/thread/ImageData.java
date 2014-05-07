package com.sdhz.thread;
//
//import com.hzsoft.dao.ImageDao;
//import com.hzsoft.dao.impl.ImageDaoImpl;
//import com.hzsoft.web.RemoteService;
//import com.hzsoft.wxty.phpvod.PhpvodMainActivity;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.AsyncTask;
///**
// * 同步服务器端的视频信息,并保存至数据库,缺少进度条等信息的UI显示
// */
//public class ImageData extends AsyncTask<String, Integer, String> {
//	private RemoteService remoteService;
//	private ImageDao imageDao;
//	private Context context;
//	private Intent intent;
//	
//	public ImageData(Context context) {
//		this.context = context;
//	}
//
//	@Override
//	protected String doInBackground(String... params) {
//		String imageJson = getImageJson(params[0],params[1]);
//		if(null!=imageJson && !"".equals(imageJson)) {
//			addDataSource(imageJson);
//			return "success";
//		} else {
//			return "timeout";
//		}
//	}
//	@Override
//	protected void onPostExecute(String result) {
//	//	loadingTextView.setText(null);
//	//	progressBar3.setVisibility(View.GONE);
//		if(result.equals("success")) {
//			intent = new Intent(context, PhpvodMainActivity.class);
//			context.startActivity(intent);
//		} else {
//	//		vodApp.showToast("连接服务器失败,请先检查网络！");
//		}
//	}
//	
//	/** 获取视频JSON */
//	private String getImageJson(String imageType,String maxId) {
//		remoteService = RemoteService.getInstance();
//		return remoteService.getImageData(imageType,maxId);
//	}
//	/** 保存视频信息 */
//	private void addDataSource(String imageJson) {
//		imageDao = new ImageDaoImpl(context);
//	//	imageDao.addImageListData(imageJson, context);
//	}
//}
