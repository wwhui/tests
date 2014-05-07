package com.hzsoft.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class HttpUtils {
	public static final int timeoutConnection = 3000;
	public static final int timeoutSocket = 5000;
	public static InputStream getStreamFromURL(String imageURL) {
		InputStream in = null;
		try {
			URL url = new URL(imageURL);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			if (connection.getReadTimeout() == 5) {
				Log.i("---------->", "当前网络有问题");
			}
			in = connection.getInputStream();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return in;
	}

	public static int getSizeFromURL(String imageURL) {
		int size = 0;
		try {
			URL url = new URL(imageURL);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			if (connection.getReadTimeout() == 5) {
				Log.i("---------->", "当前网络有问题");
			}
			size = connection.getContentLength();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return size;
	}

	public static Bitmap loadRmoteImage(String uri) {
		Bitmap bitmap = null;
		try {
			URL url = new URL(uri);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream is = connection.getInputStream();
			int length = (int) connection.getContentLength();
			if (length != -1) {
				byte[] imgData = new byte[length];
				byte[] buffer = new byte[512];
				int readLen = 0;
				int destPos = 0;
				while ((readLen = is.read(buffer)) > 0) {
					System.arraycopy(buffer, 0, imgData, destPos, readLen);
					destPos += readLen;
				}
				bitmap = BitmapFactory.decodeByteArray(imgData, 0,
						imgData.length);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	public static InputStream getStreamFromURLWithHttpClient(String uri) {
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, timeoutConnection);
		HttpConnectionParams.setSoTimeout(params, timeoutSocket);
		InputStream in = null;
		try {
			HttpGet get = new HttpGet(uri);

			HttpClient client = new DefaultHttpClient(params);
			HttpResponse response = client.execute(get);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				BufferedHttpEntity bufferedHttpEntity = new BufferedHttpEntity(
						entity);
				in = bufferedHttpEntity.getContent();
			} else {
				return null;
			}
		} catch (IOException e) {
			Log.d("IOException", "服务器连接失败!");
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return in;
	}

	public static String getServerData(String uri) {
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, timeoutConnection);
		HttpConnectionParams.setSoTimeout(params, timeoutSocket);
		InputStream is = null;
		String out = null;
		try {
			HttpPost post = new HttpPost(uri);
			HttpClient client = new DefaultHttpClient(params);
			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					// is = entity.getContent();
					// EntityUtils.toString(entity, "gbk");
					out = EntityUtils.toString(entity, "UTF-8");
				}
			}
		} catch (IOException e) {
			Log.d("IOException", "服务器连接失败!");
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return out;
	}

	public static String postRequest(String uri, Map<String, String> rawParams) {
		HttpClient client = new DefaultHttpClient();
		try {
			HttpPost post = new HttpPost(uri);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			for (String key : rawParams.keySet()) {
				params.add(new BasicNameValuePair(key, rawParams.get(key)));
			}
			post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse httpResponse = client.execute(post);

			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				// 获取服务器响应字符串
				String result = EntityUtils.toString(httpResponse.getEntity());
				return result;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			client.getConnectionManager().shutdown();
		}
		return null;
	}

	public static boolean checkNetworkIsAvailable(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = manager.getActiveNetworkInfo();
		if (networkinfo == null || !networkinfo.isAvailable()) {
			return false;
		}
		return true;
	}
}