package com.sdhz.crpandroid;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hzsoft.util.Constants;
import com.hzsoft.util.HttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.sdhz.crpandroid.widget.UserSpinner;
import com.sdhz.dao.AccountDao;
import com.sdhz.dao.ConfigDao;
import com.sdhz.dao.impl.AccountDaoImpl;
import com.sdhz.domain.Account;

public class LoginActivity extends BaseActivity {
	private List<Account> list;
	private AccountDao accountDao;
	private ConfigDao configDao;
	private Intent intent;
	private TextView versionText;
	private EditText accountEditText;
	private EditText passwordEditText;
	private CheckBox rememberCheckBox;
	private TextView loginTextView;
	private Button forgetButton;
	private Button loginButton;
	private UserSpinner spinner;
	private ProgressDialog progressBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.crp_login);
		versionText = (TextView) findViewById(R.id.versionText);
		try {
			versionText.setText("版本号:V"
					+ this.getPackageManager().getPackageInfo(
							this.getPackageName(), 0).versionName);
		} catch (NameNotFoundException e) {
			versionText.setText("版本号:V0");
		}
		loginButton = (Button) findViewById(R.id.loginButton);
		loginTextView = (TextView) findViewById(R.id.loginTextView);
		initAccountEditext();
		// 忘记密码
		initforgetBtn();
		if (!isNet()) {
			Toast.makeText(this, "请先连接网络！", Toast.LENGTH_SHORT).show();
		}
			OnClickListener loginListener = new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!isNet()) {
						Toast.makeText(LoginActivity.this, "请先连接网络！",
								Toast.LENGTH_SHORT).show();
						return;
					}
					String account = accountEditText.getText().toString();
					String password = passwordEditText.getText().toString();
					if (!"".equals(account.trim())
							&& !"".equals(password.trim())) {
						checkLogin(account, password);
					} else {
						Toast.makeText(LoginActivity.this, "请输入用户名和密码",
								Toast.LENGTH_SHORT).show();
					}
				}
			};
			loginButton.setOnClickListener(loginListener);
		new UpdateManager(LoginActivity.this)
				.checkUpdate(UpdateManager.SHOW_CHECK_UPDATE_NOTICE_NO);
		
//	startService(new Intent(LoginActivity.this, MessageService.class));
	}

	// 初始化 账户框和密码框
	private void initAccountEditext() {
		accountEditText = (EditText) findViewById(R.id.accountEditText);
		passwordEditText = (EditText) findViewById(R.id.passwordEditText);
		rememberCheckBox = (CheckBox) findViewById(R.id.rememberCheckBox);
		SharedPreferences share = getSharedPreferences("crp_account", 0);
		String account = share.getString("account", "");
		String pwd = share.getString("password", "");
		boolean isRemember = share.getBoolean("isRemember", false);
		accountEditText.setText(account);
		passwordEditText.setText(pwd);
		rememberCheckBox.setChecked(isRemember);
	}

	private void initforgetBtn() {
		// 忘记密码
		forgetButton = (Button) findViewById(R.id.forgetButton);
		forgetButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			LoginActivity.this.startActivity(new Intent(LoginActivity.this,
					ForgetPwdActivity.class).putExtra("account", accountEditText.getText().toString()));
			}
		});
	}

	/***
	 * 校验用户登录
	 * */
	private void checkLogin(String account, String password) {
		if (progressBar == null) {
			progressBar = ProgressDialog
					.show(this, "等待...", "正在登录，请稍后..", true);
			progressBar.setCancelable(true);
			progressBar.setCanceledOnTouchOutside(true);
		} else {
			progressBar.show();
		}
		RequestParams params = new RequestParams();
		params.put("account", account);
		params.put("password", password);
		// myClient.setCookieStore(myCookieStore);
		HttpClient.get(Constants.LOGIN_URL, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onFailure(Throwable arg0) {
						progressBar.dismiss();
						Toast.makeText(LoginActivity.this, "连接服务器失败",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(String string) {
						JSONObject json;
						try {
							json = new JSONObject(string);
							if ("1".equals(json.getString("result"))) {
								JSONObject object = json
										.getJSONObject("userInfo");
								Account account = new Account();
								accountDao = AccountDaoImpl
										.getInstance(LoginActivity.this);
								account.setName(object.getString("NAME"));
								account.setAccount(accountEditText.getText()
										.toString());
								if (rememberCheckBox.isChecked()) {
									account.setRemember(1);
									account.setPassword(passwordEditText
											.getText().toString());
								} else {
									account.setRemember(0);
								}
								accountDao.addAccount(account);
								CrpaApplication crp=(CrpaApplication) getApplication();
								crp.setAccount(account);
								SharedPreferences share = getSharedPreferences(
										"crp_account", 0);
								Editor editor = share.edit();
								editor.putString("account",
										account.getAccount());
								if(rememberCheckBox.isChecked()){
									editor.putString("password",account.getPassword());
								}else{
									editor.putString("password",
											"");
								}
								editor.putString("password",
										account.getPassword());
								editor.putBoolean("isRemember",
										rememberCheckBox.isChecked());
								editor.commit();
								intent = new Intent(LoginActivity.this,
										MainTabActivity.class);
								  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								PersistentCookieStore myCookieStore = new PersistentCookieStore(
										LoginActivity.this);
								HttpClient.mAsyncHttpClient
										.setCookieStore(myCookieStore);
								if (progressBar != null) {
									progressBar.dismiss();
								}
								startActivity(intent);
								LoginActivity.this.finish();

							} else {
								progressBar.dismiss();
								Toast.makeText(LoginActivity.this,
										"用户名或者密码错误!", Toast.LENGTH_LONG).show();
							}
						} catch (JSONException e) {
							progressBar.dismiss();
							Toast.makeText(LoginActivity.this, "用户名或者密码错误!",
									Toast.LENGTH_LONG).show();
						}
					}
				});
	}
}
