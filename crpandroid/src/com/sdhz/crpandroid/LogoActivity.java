package com.sdhz.crpandroid;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hzsoft.util.Constants;
import com.hzsoft.util.HttpClient;
import com.hzsoft.util.HttpUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.sdhz.dao.TaskTypeDao;
import com.sdhz.dao.impl.AccountDaoImpl;
import com.sdhz.dao.impl.TasktypeDaoImpl;
import com.sdhz.dao.support.DaoSupport;
import com.sdhz.domain.task.TaskInfo;
import com.sdhz.domain.task.TaskTypeInfo;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class LogoActivity extends Activity {
	private ProgressBar progressBar2;
	private boolean isflag = false;
	private String username;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 取消标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		// 取消状态
//		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.setContentView(R.layout.activity_logo);
		ImageView iv = (ImageView) this.findViewById(R.id.logo_bg);
		iv.setVisibility(View.GONE);
		initTypeData();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(LogoActivity.this,LoginActivity.class);
				startActivity(intent);
				LogoActivity.this.finish();
			}
		}, 2000);

		/*渐变动画
		 * AlphaAnimation ap = new AlphaAnimation(0.1f, 1.0f);
		ap.setDuration(3000);
		iv.startAnimation(ap);
		ap.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {
				Intent intent = new Intent(LogoActivity.this,
						LoginActivity.class);
				startActivity(intent);
				finish();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationStart(Animation animation) {

			}

		});*/
	}

	private void initTypeData() {
		// TODO Auto-generated method stub
		HttpClient.get(Constants.Task_Type, null,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String result) {
						TaskTypeDao dao = TasktypeDaoImpl
								.getInstance(LogoActivity.this);
						JSONArray json_question;
						try {
							json_question = new JSONArray(result);
							List list = new ArrayList();
							if (json_question != null
									&& json_question.length() > 0) {
								dao.delete();
								for (int i = 0; i < json_question.length(); i++) {
									JSONObject jo = json_question
											.getJSONObject(i);
									TaskTypeInfo type = new TaskTypeInfo();
									type.setTypeName(jo.getString("TYPENAME"));
									list.add(type);
									dao.replace(type);
								}
								// dao.addList(list);
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						super.onSuccess(result);
					}

				});
	}

}
