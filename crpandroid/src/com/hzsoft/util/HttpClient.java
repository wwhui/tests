package com.hzsoft.util;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class HttpClient {
	public  static AsyncHttpClient mAsyncHttpClient = new AsyncHttpClient();
	 static
     {
		 mAsyncHttpClient.setTimeout(20000);  
		 mAsyncHttpClient.setMaxRetriesAndTimeout(5, 1000);
     }
		public static void get(String url, RequestParams params,
				AsyncHttpResponseHandler responseHandler) {
			mAsyncHttpClient = new AsyncHttpClient();
			mAsyncHttpClient.get(url, params, responseHandler);
		}
		public static void get(String url, RequestParams params,
				JsonHttpResponseHandler jsonresponseHandler) {
			mAsyncHttpClient = new AsyncHttpClient();
			mAsyncHttpClient.get(url, params, jsonresponseHandler);
		}
		
		public static void post(String url, RequestParams params,
				AsyncHttpResponseHandler responseHandler) {
			mAsyncHttpClient = new AsyncHttpClient();
			mAsyncHttpClient.post(url, params, responseHandler);
		}
		
		
}
