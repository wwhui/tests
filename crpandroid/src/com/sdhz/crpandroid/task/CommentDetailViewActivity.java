package com.sdhz.crpandroid.task;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hp.hpl.sparta.Sparta.Cache;
import com.hzsoft.util.Constants;
import com.hzsoft.util.ContentUtil;
import com.hzsoft.util.HttpClient;
import com.hzsoft.util.cache.ImageLoader;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sdhz.crpandroid.BaseActivity;
import com.sdhz.crpandroid.CrpaApplication;
import com.sdhz.crpandroid.R;
import com.sdhz.crpandroid.task.adapter.CommentListDetailViewAdapter;
import com.sdhz.crpandroid.widget.PopInput;
import com.sdhz.crpandroid.widget.PopupDialog;
import com.sdhz.domain.blog.Blog;
import com.sdhz.domain.blog.Reply;
import com.sdhz.domain.group.UserInfo;
import com.sdhz.view.XListView;
import com.sdhz.view.XListView.IXListViewListener;

/**
 * 博文的详细信息（包括回复）
 * 
 * @author Administrator
 * 
 */
public class CommentDetailViewActivity extends BaseActivity implements
		IXListViewListener
{
	private XListView						comment_list_view;
	private LinkedList<Reply>				mListItems	= new LinkedList<Reply>();
	private CommentListDetailViewAdapter	mAdapter;
	private Blog							blog;
	private TextView title_bar, contentText,person_name,send_time;
	private ImageView imageView;
	private RequestParams params;
	private int  pageIndex=0;
	private PopInput		popInput;
	private PopupDialog					popupDialog;				// 加载popupwindow
	private Handler						mHandler=new Handler();					// 用于检测activity的初始化完毕没有，popupDialog如果直接初始化，会报空指针
	private int							detachtime	= 5;			// 每5秒检测一次
	private ImageLoader imageLoader;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.task_comment_detail_view);
		comment_list_view = (XListView) findViewById(com.sdhz.crpandroid.R.id.xlist_view);
		blog = (Blog) getIntent().getSerializableExtra("blogInfo");
		String blog_id=blog.getBlog_id();
		params=new RequestParams();
		params.put("blog_id", blog_id);
		getData(params);
		initButton();
		initView();	// 初始化评论相关信息
		initImageView();
	}

	private void initImageView() {
		imageLoader = new ImageLoader(this);
		imageView=(ImageView) findViewById(R.id.imageview);
		if(!"".equals(blog.getFileName())){
			imageLoader.DisplayImage(Constants.ImageURL+blog.getFileName(), imageView,true);
			imageView.setVisibility(View.VISIBLE);
		}else{
			imageView.setVisibility(View.GONE);
		}
	}

	// 初始化评论相关信息
	private void initView()
	{
		title_bar=(TextView) findViewById(R.id.title_bar);
		title_bar.setText("评论详情");
		title_bar.setTextColor(Color.WHITE);
		
		person_name=(TextView)findViewById(R.id.person_name);
		person_name.setText(blog.getSend_username());
		
		contentText = (TextView) findViewById(R.id.content);
		contentText.setText(ContentUtil.formatContent(blog.getSend_content(),
				this, null));
		send_time=(TextView)findViewById(R.id.send_time);
		send_time.setText(blog.getSend_date());
		
		mAdapter = new CommentListDetailViewAdapter(this, mListItems);
		comment_list_view.setAdapter(mAdapter);
		comment_list_view.setPullLoadEnable(true);
		comment_list_view.setPullRefreshEnable(true);
		comment_list_view.setXListViewListener(this);
	}
	
	// 用来循环检测activity是否初始化完毕，一旦初始化完毕就再初始化popupwindow
	private void showPopupDialog()
	{
		// TODO Auto-generated method stub
		popupDialog = new PopupDialog(CommentDetailViewActivity.this);

		Runnable runnable = new Runnable()
		{

			@Override
			public void run()
			{
				View view = findViewById(R.id.layout_main);// activity中根元素
				if (view != null && view.getWidth() > 0 && view.getHeight() > 0)
				{
					popupDialog.show(view);
					mHandler.removeCallbacks(this);
				}
				else
				{
					mHandler.postDelayed(this, detachtime);
				}
			}
		};
		// 开始检测
		mHandler.post(runnable);
	}
	
	private void initButton()
	{
		((Button) findViewById(R.id.left_button))
				.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						CommentDetailViewActivity.this.finish();
					}
				});
		((Button) findViewById(R.id.right_button)).setVisibility(View.GONE);
		((TextView)findViewById(R.id.comment)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				popInput = new PopInput(CommentDetailViewActivity
						.this, new OnClickListener(){

							@Override
							public void onClick(View v) {
								RequestParams p = new RequestParams();
								p.put("source_operator",blog.getSend_operator());// 回复某人
								p.put("blog_id", blog.getBlog_id());
								p.put("reply_operator",  ( (CrpaApplication)getApplication()).getAccountString());
								p.put("reply_content", popInput.getContent().getText()
										.toString());
								List<UserInfo> listUser = popInput.getUserInfoList();
								if (listUser != null && listUser.size() > 0)
								{
									String refer_operator = "";
									for (UserInfo userInfo : listUser)
									{
										refer_operator += userInfo.getOperator_id() + ",";
									}
									p.put("refer_operator", refer_operator);
								}
								postData(p);		
								popInput.dimiss();
							}
				});
				popInput.show(v);
		}
		});
		
	}

	@Override
	public void onRefresh()
	{
		// TODO Auto-generated method stub
		mListItems.clear();
		pageIndex=0;
		params.put("pageIndex", "0");
		getData(params);
		onLoad();
	}

	@Override
	public void onLoadMore()
	{
		pageIndex++;
		params.put("pageIndex", String.valueOf(pageIndex));
		getData(params);
		onLoad();
	}

	// 获取数据
	private void getData(RequestParams params)
	{
		showPopupDialog();
		HttpClient.get(Constants.Blog_Reply_List, params,
				new AsyncHttpResponseHandler()
				{
			
				
					@Override
					@Deprecated
					public void onFailure(Throwable error) {
						super.onFailure(error);
						popupDialog.dismiss();
					}

					@Override
					public void onSuccess(String result)
					{
						JSONArray json_question;
						try
						{
							json_question = new JSONArray(result);
							for (int i = 0; i < json_question.length(); i++)
							{
								JSONObject jo = json_question.getJSONObject(i);
								Reply reply = new Reply();
								reply.setReply_id(jo.getString("REPLY_ID"));
								reply.setBlog_id(jo.getString("BLOG_ID"));
								reply.setReply_content(jo
										.getString("REPLY_CONTENT"));
								reply.setReply_username(jo
										.getString("REPLY_USERNAME"));
								reply.setReply_operator(jo
										.getString("REPLY_OPERATOR"));
								reply.setReply_date(jo.getString("REPLY_DATE"));
								reply.setSoure_operator(jo
										.getString("SOURE_OPERATOR"));
								mListItems.add(reply);
							}
							mAdapter.setData(mListItems);
							mAdapter.notifyDataSetChanged();
						}
						catch (JSONException e)
						{
							Toast.makeText(CommentDetailViewActivity.this, "暂无数据",Toast.LENGTH_SHORT).show();
						}
						popupDialog.dismiss();
					}
				});
	}
	private void postData(RequestParams params)
	{
		HttpClient.post(Constants.NewReply, params,
				new JsonHttpResponseHandler()
				{
					@Override
					public void onSuccess(JSONObject json) {
						try {
							String result=json.getString("result");
							if("1".equals(result)){
								JSONArray json_replyArray=json.getJSONArray("replylist"); 
								if(json_replyArray!=null&&json_replyArray.length()>0){
									mListItems.clear();
									for(int j=0;j<json_replyArray.length();j++){
										JSONObject jo2 = json_replyArray.getJSONObject(j);
										Reply reply=new Reply();
										reply.setReply_id(jo2.getString("REPLY_ID"));
										reply.setReply_operator(jo2.getString("REPLY_OPERATOR"));
										reply.setReply_username(jo2.getString("REPLY_USERNAME"));
										reply.setReply_content(jo2.getString("REPLY_CONTENT"));
										reply.setSoure_operator(jo2.getString("SOURE_OPERATOR"));
										reply.setBlog_id(jo2.getString("BLOG_ID"));
										reply.setReply_date(jo2.getString("REPLY_DATE"));
										mListItems.add(reply);
									}
									mAdapter.notifyDataSetChanged();
								}
								Toast.makeText(CommentDetailViewActivity.this, "评论成功", Toast.LENGTH_SHORT)
								.show();
							}else{
								Toast.makeText(CommentDetailViewActivity.this, "评论失败", Toast.LENGTH_SHORT)
								.show();
							}
						} catch (JSONException e) {
							Toast.makeText(CommentDetailViewActivity.this, "评论失败", Toast.LENGTH_SHORT)
							.show();
						}
					}
	});
	}

	private void onLoad()
	{
		comment_list_view.stopRefresh();
		comment_list_view.stopLoadMore();
		comment_list_view.setRefreshTime("刚刚");
	}
}
