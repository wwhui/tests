package com.sdhz.crpandroid.task.adapter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hzsoft.util.AsyncBitmapLoader;
import com.hzsoft.util.Constants;
import com.hzsoft.util.ContentUtil;
import com.hzsoft.util.HttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sdhz.crpandroid.CrpaApplication;
import com.sdhz.crpandroid.R;
import com.sdhz.crpandroid.widget.CustomProgressDialog;
import com.sdhz.crpandroid.widget.PopInput;
import com.sdhz.domain.blog.Reply;
import com.sdhz.domain.group.UserInfo;

/**
 *CommentListViewActivity 的View中内层ListView 适配器 
 * 
 * @author TOSHIBA
 */
public class CommentSecondListViewAdapter extends BaseAdapter implements
		OnClickListener {
	private Context mContext;
	private LayoutInflater listContainer;
	private List<Reply> mListItems = new ArrayList();
	private AsyncBitmapLoader asyncBitmapLoader;
	private PopInput popInput;
	private  CustomProgressDialog customProgressDialog;
	private BaseAdapter  parenAdpater;
	private static int  SUCESSS=1;
	private static int  FAIL=0;
	// 自定义控件集合
	public final class ViewHolder {
		public TextView single_person_name;
		public TextView main_time;
		public TextView main_content;
		public Button moreButton;
		public Reply reply;
	}

	public CommentSecondListViewAdapter(Context context, List listItems,BaseAdapter parenAdpater) {
		asyncBitmapLoader = new AsyncBitmapLoader();
		this.parenAdpater=parenAdpater;
		this.mContext = context;
		this.mListItems = listItems;
		listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
	}

	public void setData(List listItems) {
		this.mListItems = listItems;
	}
	public void addItem(Reply reply){
		this.mListItems.add(reply);
	}
	public void addAll(List list){
		this.mListItems.addAll(list);
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return mListItems.size();
	}

	@Override
	public Object getItem(int position) {
		return mListItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = listContainer.inflate(
					R.layout.task_comment_list_item_second, null);
			holder.single_person_name = (TextView) convertView
					.findViewById(R.id.single_person_name);
			holder.main_content = (TextView) convertView
					.findViewById(R.id.single_content);
			Reply reply = mListItems.get(position);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Reply reply = mListItems.get(position);
		holder.reply=reply;
		if (reply != null) {
			holder.single_person_name.setText(reply.getReply_username());
			holder.main_content.setText( ContentUtil. formatContentNoClick("回复:   "+reply.getReply_content()));
		}
		convertView.setOnClickListener(this);
		convertView.setTag(holder);
		return convertView;
	}
	public void clear() {
		this.mListItems.clear();
	}
	@Override
	public void onClick(View v) {
		//单击单条信息  进行回复
		final ViewHolder holder = (ViewHolder) v.getTag();
		popInput = new PopInput(mContext, new OnClickListener() {
			@Override
			public void onClick(View v) {
				RequestParams params=new RequestParams();
				 Reply  r=holder.reply;
				 params.put("source_operator",r.getReply_id());//回复某人
				 params.put("blog_id", r.getBlog_id());
				 params.put("reply_operator", ( (CrpaApplication)mContext.getApplicationContext()).getAccountString());
				 params.put("reply_content", r.getReply_username()+"    "+ popInput.getContent().getText().toString());
				List<UserInfo> listUser = popInput.getUserInfoList();
				if(listUser!=null&&listUser.size()>0){
					String refer_operator="";
					for (UserInfo userInfo : listUser) {
						refer_operator+=userInfo.getOperator_id()+",";
					}
					params.put("refer_operator", refer_operator);
				}
				params.put("pageSize","5");
				postData(params);
				popInput.dimiss();
			}
		});
		popInput.show(v);
	}
	private void postData(RequestParams  params ){
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
					CommentSecondListViewAdapter.this.mListItems.clear();
					CommentSecondListViewAdapter.this.notifyDataSetChanged();
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
									mListItems.add(reply);
								}
							}
							CommentSecondListViewAdapter.this.notifyDataSetChanged();
							CommentSecondListViewAdapter.this.handler.sendEmptyMessage(SUCESSS);
						}else{
							CommentSecondListViewAdapter.this.handler.sendEmptyMessage(FAIL);
						}
					} catch (JSONException e) {
						CommentSecondListViewAdapter.this.handler.sendEmptyMessage(FAIL);
					}
					customProgressDialog.dismiss();
				}
			});
	}
	private Handler handler =new Handler(){

		@Override
		public void handleMessage(Message msg) {
			CommentSecondListViewAdapter.this.notifyDataSetChanged();
			parenAdpater.notifyDataSetChanged();
			if(SUCESSS==msg.what){
				Toast.makeText(mContext, "评论成功", Toast.LENGTH_SHORT)
				.show();
			}else if(FAIL==msg.what){
				Toast.makeText(mContext, "评论失败", Toast.LENGTH_SHORT)
				.show();
			}
			
			
		}
		
	};
	
	
}
