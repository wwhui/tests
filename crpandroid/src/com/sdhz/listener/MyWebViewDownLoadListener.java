package com.sdhz.listener;

import java.io.File;
import java.io.InputStream;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.Gravity;
import android.webkit.DownloadListener;
import android.widget.Toast;

import com.hzsoft.util.FileUtil;
import com.hzsoft.util.HttpUtils;

public class MyWebViewDownLoadListener implements DownloadListener {
	private Context context;
	public MyWebViewDownLoadListener(Context context) {
		this.context = context;
	}

	@Override
	public void onDownloadStart(String url, String userAgent,
			String contentDisposition, String mimetype, long contentLength) {
		//WebViewActivity w = (WebViewActivity) context;
	//	w.showProgressBar();
		DownloaderTask task = new DownloaderTask();
		task.execute(url);
	}
	
	class DownloaderTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
			String url = params[0];
			
			String fileName = url.substring(url.lastIndexOf("/")+1);
	//		fileName = URLDecoder.decode(fileName);
			try {
				InputStream is = HttpUtils.getStreamFromURLWithHttpClient(url);
				FileUtil fileUtil = new FileUtil();
				fileUtil.write2SDFromInputStream("download/", fileName, is);
				is.close();
				return fileName;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
		/*	WebViewActivity w = (WebViewActivity) context;
			w.closeProgressBar();*/
			if(result == null) {
				toastShow(context, "连接错误！请稍后再试！");
				return;
			}
			toastShow(context, "已保存到SD卡。");
			File file = new File(FileUtil.SDPATH+"download/", result);
			Intent intent = getFileIntent(file);
			context.startActivity(intent);
		}
	}
	
	private void toastShow(Context context, String content) {
		Toast toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	
	public Intent getFileIntent(File file) {
		Uri uri = Uri.fromFile(file);
		Intent intent = new Intent("android.intent.action.VIEW");
	//	intent.addCategory("android.intent.category.DEFAULT");
	//	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(uri, "application/vnd.android.package-archive");
		return intent;
	}

}
