package com.sdhz.crpandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class WelComeActivity extends Activity {
	private  ProgressBar  progress_loading;
	private TextView  loading_text;
	private String version;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_activity);
		progress_loading=(ProgressBar) findViewById(R.id.progress_loading);
		loading_text=(TextView) findViewById(R.id.loading_text);
		 initButton();
		 initCheckVersion();
	}
	/**
	 *检查版本更新
	 * 
	 * */
	private void initCheckVersion() {
		// TODO Auto-generated method stub
	
	}

	private void initButton() {
		// TODO Auto-generated method stub
		((Button)findViewById(R.id.login)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it=new Intent(WelComeActivity.this,LoginActivity.class);
				startActivity(it);
			}
		});;
		((Button)findViewById(R.id.main)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it=new Intent(WelComeActivity.this,MainTabActivity.class);
				startActivity(it);
			WelComeActivity.this.finish();
			}
		});;
		
	}
	private class  CheckVersion  extends AsyncTask<Void, Boolean, Integer> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			loading_text.setText(" 正在检查版本更新");
		}
		
		@Override
		protected Integer doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	
}
