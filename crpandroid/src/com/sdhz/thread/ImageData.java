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
// * ͬ���������˵���Ƶ��Ϣ,�����������ݿ�,ȱ�ٽ���������Ϣ��UI��ʾ
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
//	//		vodApp.showToast("���ӷ�����ʧ��,���ȼ�����磡");
//		}
//	}
//	
//	/** ��ȡ��ƵJSON */
//	private String getImageJson(String imageType,String maxId) {
//		remoteService = RemoteService.getInstance();
//		return remoteService.getImageData(imageType,maxId);
//	}
//	/** ������Ƶ��Ϣ */
//	private void addDataSource(String imageJson) {
//		imageDao = new ImageDaoImpl(context);
//	//	imageDao.addImageListData(imageJson, context);
//	}
//}
