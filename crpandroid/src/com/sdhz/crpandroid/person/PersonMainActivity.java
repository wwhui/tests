package com.sdhz.crpandroid.person;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.sdhz.crpandroid.R;

/**
 * �㳡ҳ��
 *
 */
public class PersonMainActivity extends FragmentActivity
{
	private FragmentManager	fragmentManager;
	private Button			atMyButton;			//�������
	private Button			myCreateBtn;		//�ҷ����
	private Button		left_button,attention;		// ��ע�Ķ���

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.crp_person_main_activity);
		initView();//	��ʼ��view
		initAttention(); // ��ע����
		fragmentManager = getSupportFragmentManager();//��ʼ��fragment������
		show(0);//	��ʼ����ʾfragment
	}
//	��ʼ��view
	private void initView()
	{
		((TextView) findViewById(R.id.title_bar)).setText("�㳡");
		((TextView) findViewById(R.id.title_bar)).setTextColor(Color.WHITE);
		atMyButton = (Button) findViewById(R.id.atmy);
		atMyButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				show(1);
				atMyButton
						.setBackgroundResource(R.drawable.tabbutton_left_color);
				myCreateBtn
						.setBackgroundResource(R.drawable.tabbutton_right_color);
				atMyButton.setTextColor(Color.WHITE);
				myCreateBtn.setTextColor(Color.BLACK);
			}
		});
		myCreateBtn = (Button) findViewById(R.id.mycreate);
		myCreateBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				show(0);
				atMyButton
						.setBackgroundResource(R.drawable.tabbutton_right_color);
				myCreateBtn
						.setBackgroundResource(R.drawable.tabbutton_left_color);
				atMyButton.setTextColor(Color.BLACK);
				myCreateBtn.setTextColor(Color.WHITE);
			}
		});
	}
//	��ʼ����ע
	private void initAttention()
	{
		left_button=(Button)findViewById(R.id.left_button);
		left_button.setVisibility(View.GONE);
		attention = (Button) findViewById(R.id.right_button);
		attention.setText("��ע");
		attention.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(PersonMainActivity.this,
						AttentionActivity.class);
				startActivity(intent);
			}
		});
	}
//	��ʼ����ʾfragment
	private void show(int i)
	{
		// TODO Auto-generated method stub
		Fragment fragment = null;
		switch (i)
		{
		case 0:
			fragment = new MyCreateFrament();
			break;
		case 1:
			fragment = new AtMySelfFrament();
			break;
		default:
			break;
		}
		//��ʼ����ύ
		FragmentTransaction beginTransaction = fragmentManager
				.beginTransaction();
		beginTransaction.replace(R.id.fragment1, fragment);
		beginTransaction.commit();
	}

}
