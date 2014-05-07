package com.sdhz.crpandroid.config;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.sdhz.crpandroid.R;
import com.sdhz.crpandroid.message.MessageService;
import com.sdhz.dao.ConfigDao;
import com.sdhz.dao.impl.ConfigDaoImpl;
import com.sdhz.domain.Config;

public class PushConfigActivity extends Activity
{
	private TextView	textView1, textView2, textView3;
	private TextView	sound;
	private TextView	shake;
	private Intent		intent;
	private ConfigDao	configDao;
	private Config		config1, config2, config3;
	private Config		soundConf;
	private Config		shakeConf;
	private boolean		isPush1	= false, isPush2 = false, isPush3 = false,
			isSound = false, isShake = false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_config_pushconfig);

		textView1 = (TextView) findViewById(R.id.pushMessageTextView);
		textView2 = (TextView) findViewById(R.id.pushPhpVodTextView);
		textView3 = (TextView) findViewById(R.id.pushWebCalTextView);
		sound = (TextView) findViewById(R.id.pushSoundConfTextView);
		shake = (TextView) findViewById(R.id.pushShakeConfTextView);

		soundConf = getConfigWithSound();
		shakeConf = getConfigWithShake();
		config1 = getConfigWithMessagePush();
		config2 = getConfigWithPhpVodPush();
		config3 = getConfigWithWebCalPush();
		if (null == config1)
		{
			config1 = new Config();
			config1.setConfigName("公告推送设置");
			config1.setConfigType(1);
			config1.setConfigResult(0);
			addConfig(config1);
		}
		if (null == config2)
		{
			config2 = new Config();
			config2.setConfigName("新闻推送设置");
			config2.setConfigType(2);
			config2.setConfigResult(0);
			addConfig(config2);
		}
		if (null == config3)
		{
			config3 = new Config();
			config3.setConfigName("日程推送设置");
			config3.setConfigType(3);
			config3.setConfigResult(0);
			addConfig(config3);
		}
		if (null == soundConf)
		{
			soundConf = new Config();
			soundConf.setConfigName("推送声音设置");
			soundConf.setConfigType(4);
			soundConf.setConfigResult(0);
			addConfig(soundConf);
		}
		if (null == shakeConf)
		{
			shakeConf = new Config();
			shakeConf.setConfigName("推送震动设置");
			shakeConf.setConfigType(5);
			shakeConf.setConfigResult(0);
			addConfig(shakeConf);
		}
		if (config1.getConfigResult() == 1)
		{
			isPush1 = true;
			textView1.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.bn_yes_bg, 0);
		}
		else
		{
			isPush1 = false;
			textView1.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.bn_no_bg, 0);
		}
		if (config2.getConfigResult() == 1)
		{
			isPush2 = true;
			textView2.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.bn_yes_bg, 0);
		}
		else
		{
			isPush2 = false;
			textView2.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.bn_no_bg, 0);
		}
		if (config3.getConfigResult() == 1)
		{
			isPush3 = true;
			textView3.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.bn_yes_bg, 0);
		}
		else
		{
			isPush3 = false;
			textView3.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.bn_no_bg, 0);
		}
		if (soundConf.getConfigResult() == 1)
		{
			isSound = true;
			sound.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.bn_yes_bg, 0);
		}
		else
		{
			isSound = false;
			sound.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.bn_no_bg, 0);
		}
		if (shakeConf.getConfigResult() == 1)
		{
			isShake = true;
			shake.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.bn_yes_bg, 0);
		}
		else
		{
			isShake = false;
			shake.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.bn_no_bg, 0);
		}

		textView1.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				intent = new Intent(PushConfigActivity.this,
						MessageService.class);
				isPush1 = !isPush1;
				if (!isPush1)
				{
					textView1.setCompoundDrawablesWithIntrinsicBounds(0, 0,
							R.drawable.bn_no_bg, 0);
					config1.setConfigResult(0);
				}
				else
				{
					textView1.setCompoundDrawablesWithIntrinsicBounds(0, 0,
							R.drawable.bn_yes_bg, 0);
					config1.setConfigResult(1);
				}
				intent.putExtra("isPush1", isPush1);
				intent.putExtra("isPush2", isPush2);
				intent.putExtra("isPush3", isPush3);
				intent.putExtra("isSound", isSound);
				intent.putExtra("isShake", isShake);
				modConfig(config1);
				startService(intent);
			}
		});

		textView2.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				intent = new Intent(PushConfigActivity.this,
						MessageService.class);
				isPush2 = !isPush2;
				if (!isPush2)
				{
					textView2.setCompoundDrawablesWithIntrinsicBounds(0, 0,
							R.drawable.bn_no_bg, 0);
					config2.setConfigResult(0);
				}
				else
				{
					textView2.setCompoundDrawablesWithIntrinsicBounds(0, 0,
							R.drawable.bn_yes_bg, 0);
					config2.setConfigResult(1);
				}
				intent.putExtra("isPush1", isPush1);
				intent.putExtra("isPush2", isPush2);
				intent.putExtra("isPush3", isPush3);
				intent.putExtra("isSound", isSound);
				intent.putExtra("isShake", isShake);
				modConfig(config2);
				startService(intent);
			}
		});

		textView3.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				intent = new Intent(PushConfigActivity.this,
						MessageService.class);
				isPush3 = !isPush3;
				if (!isPush3)
				{
					textView3.setCompoundDrawablesWithIntrinsicBounds(0, 0,
							R.drawable.bn_no_bg, 0);
					config3.setConfigResult(0);
				}
				else
				{
					textView3.setCompoundDrawablesWithIntrinsicBounds(0, 0,
							R.drawable.bn_yes_bg, 0);
					config3.setConfigResult(1);
				}
				intent.putExtra("isPush1", isPush1);
				intent.putExtra("isPush2", isPush2);
				intent.putExtra("isPush3", isPush3);
				intent.putExtra("isSound", isSound);
				intent.putExtra("isShake", isShake);
				modConfig(config3);
				startService(intent);
			}
		});

		sound.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				intent = new Intent(PushConfigActivity.this,
						MessageService.class);
				isSound = !isSound;
				if (!isSound)
				{
					sound.setCompoundDrawablesWithIntrinsicBounds(0, 0,
							R.drawable.bn_no_bg, 0);
					soundConf.setConfigResult(0);
				}
				else
				{
					sound.setCompoundDrawablesWithIntrinsicBounds(0, 0,
							R.drawable.bn_yes_bg, 0);
					soundConf.setConfigResult(1);
				}
				intent.putExtra("isPush1", isPush1);
				intent.putExtra("isPush2", isPush2);
				intent.putExtra("isPush3", isPush3);
				intent.putExtra("isSound", isSound);
				intent.putExtra("isShake", isShake);
				modConfig(soundConf);
				startService(intent);
			}
		});

		shake.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				intent = new Intent(PushConfigActivity.this,
						MessageService.class);
				isShake = !isShake;
				if (!isShake)
				{
					shake.setCompoundDrawablesWithIntrinsicBounds(0, 0,
							R.drawable.bn_no_bg, 0);
					shakeConf.setConfigResult(0);
				}
				else
				{
					shake.setCompoundDrawablesWithIntrinsicBounds(0, 0,
							R.drawable.bn_yes_bg, 0);
					shakeConf.setConfigResult(1);
				}
				intent.putExtra("isPush1", isPush1);
				intent.putExtra("isPush2", isPush2);
				intent.putExtra("isPush3", isPush3);
				intent.putExtra("isSound", isSound);
				intent.putExtra("isShake", isShake);
				modConfig(shakeConf);
				startService(intent);
			}
		});
	}

	private void addConfig(Config config)
	{
		configDao = ConfigDaoImpl.getInstance(this);
		configDao.addConfig(config);
	}

	private void modConfig(Config config)
	{
		configDao = ConfigDaoImpl.getInstance(this);
		configDao.modConfig(config, String.valueOf(config.getConfigType()));
	}

	private Config getConfigWithMessagePush()
	{
		configDao = ConfigDaoImpl.getInstance(this);
		return configDao.getConfigWithConfigType("1");
	}

	private Config getConfigWithPhpVodPush()
	{
		configDao = ConfigDaoImpl.getInstance(this);
		return configDao.getConfigWithConfigType("2");
	}

	private Config getConfigWithWebCalPush()
	{
		configDao = ConfigDaoImpl.getInstance(this);
		return configDao.getConfigWithConfigType("3");
	}

	private Config getConfigWithSound()
	{
		configDao = ConfigDaoImpl.getInstance(this);
		return configDao.getConfigWithConfigType("4");
	}

	private Config getConfigWithShake()
	{
		configDao = ConfigDaoImpl.getInstance(this);
		return configDao.getConfigWithConfigType("5");
	}
}
