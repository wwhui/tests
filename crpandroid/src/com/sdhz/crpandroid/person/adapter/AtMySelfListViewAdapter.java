package com.sdhz.crpandroid.person.adapter;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.hzsoft.util.Constants;
import com.hzsoft.util.HttpClient;
import com.hzsoft.util.HttpUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sdhz.crpandroid.R;
import com.sdhz.crpandroid.person.AtMySelfFrament;
import com.sdhz.crpandroid.task.CommentDetailViewActivity;
import com.sdhz.domain.blog.Blog;
import com.sdhz.domain.blog.Reply;

public class AtMySelfListViewAdapter extends BaseAdapter implements OnClickListener
{
	private Context						mContext;
	private LayoutInflater				listContainer;
	private List<Reply>	mListItems;
	ViewHolder	holder;
	private ProgressDialog  progressDialog;
	public AtMySelfListViewAdapter(Context context,
			List<Reply> listItems)
	{
		this.mContext = context;
		this.mListItems = listItems;
		listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
	}

	public void addItem(List<Reply> listItems)
	{
		this.mListItems.addAll(listItems);
	}
	public void setData(List<Reply> listItems)
	{
		this.mListItems=listItems;
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
		ViewHolder	holder=null;
		/** 1.初始化 Item 中的view */
		if (convertView == null)
		{
			holder = new ViewHolder();
			convertView = listContainer.inflate(
					R.layout.atmyselfflow_list_item, null);
			holder.tv_reply_content = (TextView) convertView
					.findViewById(R.id.tv_reply_content);
			holder.tv_soure_operator = (TextView) convertView
					.findViewById(R.id.tv_soure_operator);
			holder.tv_reply_date = (TextView) convertView
					.findViewById(R.id.tv_reply_date);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		/** 2. Item 中的view每个组件赋值 */
		Reply reply = mListItems.get(position);
		holder.reply=reply;
		if (reply != null)
		{
			holder.tv_reply_content.setText(reply.getReply_content());
			holder.tv_soure_operator.setVisibility(View.GONE);
//			holder.tv_soure_operator.setText(reply.getSoure_operator());
			holder.tv_reply_date.setText(reply.getReply_date());
		}
		convertView.setOnClickListener(this);
		convertView.setTag(holder);
		return convertView;
	}

	public void addImageItem(Reply reply)
	{
		mListItems.add(reply);
	}

// 自定义控件集合
	class ViewHolder
	{
		public TextView						tv_reply_date;
		public TextView						tv_reply_content;
		public TextView						tv_soure_operator;
		public Reply reply;
	}

@Override
public void onClick(View v) {
	if(progressDialog==null){
		progressDialog=ProgressDialog.show(mContext, "稍等...", "正在请求服务器，请稍后..", true);
		progressDialog.setCancelable(true);
		progressDialog.setCanceledOnTouchOutside(true);
	}
	ViewHolder vh=(ViewHolder) v.getTag();
	String blog_Id=vh.reply.getBlog_id();
	RequestParams p=new RequestParams();
	p.put("blog_id", blog_Id);
	HttpClient.get(Constants.Blog_Detail, p, new JsonHttpResponseHandler(){
		@Override
		public void onSuccess( JSONObject jo) {
			if(jo!=null){
				Blog blog=new Blog();
				try {
					blog.setBlog_id(jo.getString("BLOG_ID"));
					blog.setSend_operator(jo.getString("SEND_OPERATOR"));
					blog.setSend_username(jo.getString("SEND_USERNAME"));
					blog.setFlow_id(jo.getString("FLOW_ID"));
					blog.setRoute_id(jo.optString("ROUTE_ID", ""));
					blog.setSend_content(jo.getString("BLOG_CONTENT"));
					blog.setIs_redict(jo.getInt("IS_REDICT"));
					blog.setType(jo.getInt("TYPE"));
					blog.setFileName(jo.getString("FILE_NAME"));
					blog.setSend_date(jo.getString("SEND_DATE"));
					Intent intent=new Intent(AtMySelfListViewAdapter.this.mContext,CommentDetailViewActivity.class);
					intent.putExtra("blogInfo", blog);
					intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					progressDialog.dismiss();
					mContext.startActivity(intent);
				} catch (JSONException e) {
				}
		
			}
		}
		
	});
}
}
