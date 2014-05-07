package com.sdhz.crpandroid.task.adapter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

import com.hp.hpl.sparta.Sparta.Cache;
import com.hzsoft.util.AsyncBitmapLoader;
import com.hzsoft.util.Constants;
import com.hzsoft.util.ContentUtil;
import com.hzsoft.util.HttpClient;
import com.hzsoft.util.cache.ImageLoader;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sdhz.crpandroid.CrpaApplication;
import com.sdhz.crpandroid.R;
import com.sdhz.crpandroid.task.CommentDetailViewActivity;
import com.sdhz.crpandroid.task.CreateNewComentActivity;
import com.sdhz.crpandroid.widget.CustomProgressDialog;
import com.sdhz.crpandroid.widget.PopInput;
import com.sdhz.domain.blog.Blog;
import com.sdhz.domain.blog.Reply;
import com.sdhz.domain.group.UserInfo;
import com.sdhz.view.ChildListView;

/**
 * 信息评论列表 适配器 嵌套啦两个ListView
 * 
 * 
 * */
public class CommentListViewAdapter extends BaseAdapter implements
		OnClickListener
{

	private Context			mContext;
	private LayoutInflater	listContainer;
	private List			mListItems;
	private ViewComment		holder	= null;
	private PopInput		popInput;
	private  CustomProgressDialog customProgressDialog;
	private AsyncBitmapLoader asyncBitmapLoader;
	private ImageLoader imageLoader;
	public CommentListViewAdapter(Context context, List listItems)
	{
		asyncBitmapLoader=new AsyncBitmapLoader();
		this.mContext = context;
		this.mListItems = listItems;
		listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
		imageLoader = new ImageLoader(context);
	}

	public void setData(List listItems)
	{
		this.mListItems = listItems;
	}

	@Override
	public int getCount()
	{
		return mListItems.size();
	}

	@Override
	public Object getItem(int position)
	{
		return mListItems.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		/**
		 * 1.初始化 Item 中的view 2.给View中listView 进行配置 重写ListView 实现ChildListView
		 * 实现ListVIew的高度 自适应 注意 Item 中的ListView(ChildListView) 必须用LinerLayout 进行
		 * 包裹，其他布局标签不适用
		 * */
		// 初始话Item 中的组件
		if (convertView == null)
		{
			holder = new ViewComment();
			convertView = listContainer.inflate(
					R.layout.task_comment_list_item, null);
			holder.comment_title = (TextView) convertView
					.findViewById(R.id.comment_title);
			holder.comment_time = (TextView) convertView
					.findViewById(R.id.comment_create_time);
			holder.comment_person = (TextView) convertView
					.findViewById(R.id.comment_person);
			holder.main_item_comment = (Button) convertView
					.findViewById(R.id.btn_comment);
			holder.xListView = (ChildListView) convertView
					.findViewById(R.id.xlist_view);
			holder.btn_redirect = (Button) convertView
					.findViewById(R.id.btn_redirect);
			holder.btn_attention = (Button) convertView
					.findViewById(R.id.btn_attention);
			holder.imageView=(ImageView) convertView.findViewById(R.id.imageview);
		}
		else
		{
			holder = (ViewComment) convertView.getTag();
		}
		// 获取BLog(博文)的值
		Blog blog = (Blog) mListItems.get(position);
		holder.comment_title.setTag(blog);
		holder.blog = blog;
		///图片的加载
		/**图片的加载*/
		if(!"".equals(blog.getFileName())){
			imageLoader.DisplayImage(Constants.ImageURL+blog.getFileName(), holder.imageView,true);
			holder.imageView.setScaleType(ScaleType.FIT_XY);
			holder.imageView.setVisibility(View.VISIBLE);
		}else{
			holder.imageView.setVisibility(View.GONE);
		}
		
		
		
		/** 2. Item 中的view每个组件赋值 */
		holder.comment_person.setText(blog.getSend_username());
		holder.comment_time.setText(blog.getSend_date());
		holder.comment_title.setText(ContentUtil.formatContentNoClick(blog
				.getSend_content()));
		List list = (List) blog.getReplyList();

		
		
		/** 3. Item 中的view中的ListView */
		holder.secondListAdapter = new CommentSecondListViewAdapter(mContext,
				list,this);
		holder.xListView.setAdapter(holder.secondListAdapter);
		//4  关注事件	
		// 关注事件
		holder.btn_attention.setTag(blog);
		holder.btn_attention.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Blog blog = (Blog) v.getTag();
				Button btn = (Button) v;
				RequestParams p = new RequestParams();
				p.put("blog_id", blog.getBlog_id());
				p.put("operator_id", ( (CrpaApplication)mContext.getApplicationContext()).getAccountString());
				p.put("type", "0");
				attenttion(p, btn);
			}
		});
		// 转发传递参数
		holder.btn_redirect.setTag(blog);
		holder.btn_redirect.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Blog blog = (Blog) v.getTag();
				Intent intent = new Intent(mContext,
						CreateNewComentActivity.class);
				intent.putExtra("data", blog);
				mContext.startActivity(intent);
			}
		});

		// 给评论按钮添加Tagd对象用来传递参数
		Tag tag = new Tag();
		tag.secondListAdapter = holder.secondListAdapter;
		tag.blog = blog;

		holder.main_item_comment.setTag(tag);
		// /评论事件
		holder.main_item_comment.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// 初始化popInput对象 此处OnClickListener 是绑定 给popInput中的发送按钮的
				final Tag tag = (Tag) v.getTag();
				popInput = new PopInput(mContext, holder, new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						RequestParams params = new RequestParams();
						params.put("blog_id", tag.blog.getBlog_id());
						params.put("source_operator", tag.blog.getSend_operator());
						params.put("reply_operator",  ( (CrpaApplication)mContext.getApplicationContext()).getAccountString());
						params.put("reply_content", popInput.getContent()
								.getText().toString());
						List<UserInfo> listUser = popInput.getUserInfoList();
						if (listUser != null && listUser.size() > 0)
						{
							String refer_operator = "";
							for (UserInfo userInfo : listUser)
							{
								refer_operator += userInfo.getOperator_id()
										+ ",";
							}
							params.put("refer_operator", refer_operator);
						}
						params.put("pageSize","5");
						postData(params,tag.secondListAdapter);
						popInput.dimiss();
					}
				});
				popInput.show(v);
			}
		});
		convertView.setOnClickListener(this);
		convertView.setTag(holder);
		
		return convertView;
	}

	public void add(Blog map)
	{
		mListItems.add(map);
	}

	@Override
	public void onClick(View v)
	{
		ViewComment viewComment = (ViewComment) v.getTag();
		Intent intent = new Intent(mContext, CommentDetailViewActivity.class);
		intent.putExtra("blogInfo", viewComment.blog);
		mContext.startActivity(intent);

	}

	private void postData(RequestParams  params, final CommentSecondListViewAdapter secondListAdapter ){
		if(customProgressDialog==null){
			customProgressDialog=CustomProgressDialog.createDialog(mContext);
			customProgressDialog.setMessage("正在提交数据......");
		}
		customProgressDialog.show();
		HttpClient.post(Constants.NewReply, params, new JsonHttpResponseHandler()
			{
				@Override
				public void onFailure(Throwable arg0, JSONObject arg1) {
					customProgressDialog.dismiss();
					Toast.makeText(mContext, "连接服务器失败,请重试.......", Toast.LENGTH_SHORT)
					.show();
				}
				@Override
				public void onSuccess(JSONObject json) {
					customProgressDialog.dismiss();
					List list=new ArrayList();
					secondListAdapter.clear();
					try {
						String result=json.getString("result");
						if("1".equals(result)){
							JSONArray json_replyArray=json.getJSONArray("replylist"); 
							if(json_replyArray!=null&&json_replyArray.length()>0){
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
									list.add(reply);
								}
								secondListAdapter.addAll(list);
								secondListAdapter.notifyDataSetChanged();
								CommentListViewAdapter.this.notifyDataSetChanged();
							}
							Toast.makeText(mContext, "评论成功", Toast.LENGTH_SHORT)
							.show();
						}else{
							Toast.makeText(mContext, "评论失败", Toast.LENGTH_SHORT)
							.show();
						}
					} catch (JSONException e) {
						Toast.makeText(mContext, "评论失败", Toast.LENGTH_SHORT)
						.show();
					}
					customProgressDialog.dismiss();
				}
			});
	}
//	关注博文
	private void attenttion(RequestParams params, final Button btn)
	{
		HttpClient.post(Constants.Blog_Attention_Add, params,
				new AsyncHttpResponseHandler()
				{
					@Override
					public void onSuccess(String arg0)
					{
						// TODO Auto-generated method stub
						if("2".equals(arg0))
						{
							btn.setClickable(false);
							btn.setTextColor(mContext.getResources().getColor(
									R.color.graySecond));
							Toast.makeText(mContext, "您已关注", Toast.LENGTH_SHORT)
							.show();
						}
						if ("1".equals(arg0))
						{
							btn.setClickable(false);
							btn.setTextColor(mContext.getResources().getColor(
									R.color.graySecond));
							Toast.makeText(mContext, "关注成功", Toast.LENGTH_SHORT)
									.show();
						}
					}
				});
	}
}

class Tag
{
	public Blog							blog;
	public CommentSecondListViewAdapter	secondListAdapter;
}

// 自定义控件集合
class ViewComment
{
	public Blog							blog;
	public TextView						comment_title;
	public TextView						comment_time;
	public TextView						comment_person;
	public ImageView	imageView;
	public Button						moreButton;
	public ChildListView				xListView;
	public Button						main_item_comment;	// 评论
	public Button						btn_redirect;		// 转发
	public Button						btn_attention;		// 关注
	public CommentSecondListViewAdapter	secondListAdapter;
}
