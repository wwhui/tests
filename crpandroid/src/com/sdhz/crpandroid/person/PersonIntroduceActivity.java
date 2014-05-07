package com.sdhz.crpandroid.person;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdhz.crpandroid.R;



/**
 * 个人介绍
 *
 */
public class PersonIntroduceActivity extends Activity
{
	private ImageView ivPerson_al;
	private TextView tvPerson_name;
	private Button btnBtn1,btnBtn2,btnBtn3;
	private TextView detail;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.crp_person_introduce);
		ivPerson_al=(ImageView) findViewById(R.id.person_al);
		tvPerson_name=(TextView) findViewById(R.id.person_name);
		detail=(TextView)findViewById(R.id.detail);
		detail.setOnClickListener(onClickListener);
//		btnBtn1=(Button)findViewById(R.id.btn1);
//		btnBtn2=(Button)findViewById(R.id.btn2);
//		btnBtn3=(Button)findViewById(R.id.btn3);
		
//		btnBtn1.setOnClickListener(onClickListener);
//		btnBtn2.setOnClickListener(onClickListener);
//		btnBtn3.setOnClickListener(onClickListener);
	}
	//Button 点击事件
	private OnClickListener onClickListener=new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			Intent intent=new Intent();
			switch (v.getId())
			{
//			case R.id.btn1:
//				showToast("我是btn1");
//				break;
			case R.id.detail:
				intent.setClass(PersonIntroduceActivity.this, PersonInfoActivity.class);
				startActivity(intent);
//				showToast("我是btn2");
				break;
//			case R.id.btn3:
//				intent.setClass(PersonIntroduceActivity.this, PersonPswActivity.class);
//				startActivity(intent);
//				showToast("我是btn3");
//				break;
//
			default:
				break;
			}
		}
	};
	
}
