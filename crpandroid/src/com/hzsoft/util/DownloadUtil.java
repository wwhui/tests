package com.hzsoft.util;

//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//import com.hzsoft.wxty.MainActivity;
//import com.hzsoft.wxty.config.ConfigMainActivity;
//
//import android.content.Context;
//import android.os.Message;
//import android.util.Log;
//
//public class DownloadUtil {
//	private URL url;
//	private HttpURLConnection conn;
//	private InputStream is;
//	private int fileSize;
//	private Context context;
//	private int type;
//	private MainActivity m = null;
//	private ConfigMainActivity c = null;
//
//	public DownloadUtil(Context context, int type) {
//		this.context = context;
//		this.type = type;
//		if (type == 1) {
//			m = (MainActivity) context;
//		} else {
//			c = (ConfigMainActivity) context;
//		}
//	}
//	
//	private void getInputStreamFromURL(String strUrl) throws IOException {
//		url = new URL(strUrl);
//		conn = (HttpURLConnection)url.openConnection();
//		if(conn.getReadTimeout()==5) {
//			Log.i("---------->", "当前网络有问题");
//		}
//		is = conn.getInputStream();
//		fileSize = conn.getContentLength();
//	}
//	
//	/**
//	 * 下载文件
//	 * @param path		本地文件夹
//	 * @param fileName	本地文件名
//	 * @return
//	 */
//	public int downFile(String path, String fileName) {
//		byte[] buffer = new byte[Constants.BUFFER_SIZE];
//		FileUtil fileUtil = new FileUtil();
//		File file = null;
//		OutputStream os = null;
//		Message msg = Message.obtain();
//		msg.arg1 = 100;
//		try {
//			getInputStreamFromURL(Constants.DOWN_URL);
//			
//			fileUtil.createFolder(path);
//			file = fileUtil.createFile(path+fileName);
//			os = new FileOutputStream(file);
//			int tmp = 0;
//			int temp = 0;
//			while ((temp = is.read(buffer)) != -1) {
//				os.write(buffer, 0, temp);
//				tmp += temp;
//				msg.arg2 = tmp*100/fileSize;
//				if (type == 1) {
//					m.pro2Handler.handleMessage(msg);
//				} else {
//					c.progressDialogHandler.handleMessage(msg);
//				}
//			}
//			os.flush();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				is.close();
//				os.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		return 0;
//	}
//}
