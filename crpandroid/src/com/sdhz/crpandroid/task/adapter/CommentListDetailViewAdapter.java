package com.sdhz.crpandroid.task.adapter;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hzsoft.util.Constants;
import com.hzsoft.util.HttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sdhz.crpandroid.CrpaApplication;
import com.sdhz.crpandroid.R;
import com.sdhz.crpandroid.widget.PopInput;
import com.sdhz.domain.blog.Reply;
import com.sdhz.domain.group.UserInfo;

/**
 * 评论详情
 * 
 * 
 */
public class CommentListDetailViewAdapter extends BaseAdapter implements
		OnClickListener
{
	private Context			mContext;
	private LayoutInflater	listContainer;
	private LinkedList<Reply>		mListItems;
	private ViewHolder		holder	= null;
	private PopInput		popInput;

	// 自定义控件集合
	class ViewHolder
	{
		public TextView	reply_username;
		public TextView	reply_time;
		public TextView	reply_content;
		public Button	moreButton;
		public Reply	reply;
	}

	public CommentListDetailViewAdapter(Context context, LinkedList<Reply> listItems)
	{
		this.mContext = context;
		this.mListItems = listItems;
		listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
	}

	public void setData(LinkedList listItems)
	{
		this.mListItems = listItems;
	}

	public void addItem(Reply reply)
	{
		this.mListItems.add(reply);
	}

	@Override
	public int getCount()
	{
		return mListItems.size();
	}
	public void addFirst(Reply reply){
		this.mListItems.addFirst(reply);
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
		if (convertView == null)
		{
			holder = new ViewHolder();
			convertView = listContainer.inflate(
					R.layout.task_comment_detail_item, null);
			holder.reply_username = (TextView) convertView
					.findViewById(R.id.reply_username);
			holder.reply_content = (TextView) convertView
					.findViewById(R.id.reply_content);
			holder.reply_time = (TextView) convertView
					.findViewById(R.id.reply_time);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		Reply reply = mListItems.get(position);
		if (reply != null)
		{ // 回复人姓名
			holder.reply = reply;
			holder.reply_username.setText(reply.getReply_username());
			holder.reply_content.setText(reply.getReply_content());
			holder.reply_time.setText(reply.getReply_date());
		}
		convertView.setOnClickListener(this);
		convertView.setTag(holder);
		return convertView;
	}

	@Override
	public void onClick(View v)
	{
		holder = (ViewHolder) v.getTag();
		final String operatot_id=holder .reply.getReply_operator();
		final String  blog_id=holder .reply.getBlog_id();
		final String reply_username=holder.reply.getReply_username();
		popInput = new PopInput(mContext, new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				RequestParams params = new RequestParams();
				params.put("source_operator", operatot_id);// 回复某人
				params.put("blog_id", blog_id);
				params.put("reply_operator",  ( (CrpaApplication)mContext.getApplicationContext()).getAccountString());
				params.put("reply_content", reply_username+"   "+ popInput.getContent().getText()
						.toString());
				List<UserInfo> listUser = popInput.getUserInfoList();
				if (listUser != null && listUser.size() > 0)
				{
					String refer_operator = "";
					for (UserInfo userInfo : listUser)
					{
						refer_operator += userInfo.getOperator_id() + ",";
					}
					params.put("refer_operator", refer_operator);
				}
				postData(params);
				popInput.dimiss();
			}
		});
		popInput.show(v);
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
									CommentListDetailViewAdapter.this.notifyDataSetChanged();
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
					}
	});
	}
}
