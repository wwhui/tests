package com.sdhz.crpandroid.task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hzsoft.util.Constants;
import com.hzsoft.util.HttpClient;
import com.hzsoft.util.HttpUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sdhz.crpandroid.BaseActivity;
import com.sdhz.crpandroid.R;
import com.sdhz.crpandroid.task.adapter.CommentListViewAdapter;
import com.sdhz.domain.Account;
import com.sdhz.domain.blog.Blog;
import com.sdhz.domain.blog.Reply;
import com.sdhz.domain.task.TaskInfo;
import com.sdhz.domain.task.TaskRoute;
import com.sdhz.view.XListView;
import com.sdhz.view.XListView.IXListViewListener;
/**
 * 
 * ��Ϣ�����б�(�� TaskMainListView(���۰�ť��ת)��PointMainListActivity(onClick�¼� ��ת��) )
 * �漰����  ����ListViewǶ��
 *  �����ļ�  R.layout.task_comment_list_view
 *  ���������ļ�:  R.layout.task_comment_list_item
 *  			��������Ĳ����ļ��� R.layout.task_comment_list_item_second
 * ���ݶ���  TaskRoute(·�ɵ�)������ TaskInfo 
 * 
 * **/
public class CommentListViewActivity extends BaseActivity  implements IXListViewListener{
	private XListView commentListView;
	private CommentListViewAdapter commentListViewAdapter;
	private List dataList;
	private int  page=0;
	private Intent  intent;//ȫ��Intent ����ҳ�����ת
	private ProgressBar progressBar_loading;
	private RequestParams params;//ȫ�ֲ����Ĵ���
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.task_comment_list_view);
		intent=new Intent();
		params = new RequestParams();
		dataList = new LinkedList();
		progressBar_loading=(ProgressBar) findViewById(R.id.progressBar_loading);
		//��ʼ��titleBar
		initTitleBar();
		initListData();
	}

	private void initTitleBar() {
		//��ʼ�� title
		((TextView) findViewById(R.id.title_bar)).setTextColor(Color.WHITE);
		((Button) findViewById(R.id.left_button))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						CommentListViewActivity.this.finish();
					}
				});
		
		((Button) findViewById(R.id.right_button))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// ����һ���µ����� ��Դ� ·����Ϣ
						intent.setClass(
								CommentListViewActivity.this,
								CreateNewComentActivity.class);
						startActivity(intent);
					}
				});
	}

	private void initListData() {
		commentListView = (XListView) findViewById(R.id.xlist_view);
		commentListViewAdapter = new CommentListViewAdapter(this, dataList);
		commentListView.setAdapter(commentListViewAdapter);
		commentListView.setPullLoadEnable(true);//ע�� �Ƿ��ҳ
		commentListView.setPullRefreshEnable(true);
		commentListView.setXListViewListener(this);
	}
	public void  initData(){
		dataList.clear();
		//intent.putExtra("data", route);//ͳһ����data ��CreateNewComment��������
		Object object =  getIntent().getSerializableExtra(GlobalParams.Data);//data ������TaksInfo ������routeInfo
		TextView title= (TextView) findViewById(R.id.title_bar);
	
		if(object!=null ){
			 String is_direct=getIntent().getStringExtra(GlobalParams.IS_Direct);
			 ///�����ж��Ƿ���ת�������ҳ��
			//��ѯ����������Ϣ�� ����  is_direct ������ʾת��ҳ��
			if(object instanceof TaskInfo){
				TaskInfo info=(TaskInfo) object;
				title.setText("����:"+info.getProject_name());//
				if("1".equals(is_direct)){
					title.setText("ת��  :"+info.getProject_name());
				}
				params.put("is_direct",is_direct);//����ʾת��������
				intent.putExtra(GlobalParams.Data, info);
				params.put("flow_id", info.getFlow_id());//
			}else if(object instanceof TaskRoute){
				//��ѯ ·����Ϣ�Ĳ���   is_direct ������ʾת��ҳ��
				TaskRoute route=(TaskRoute)object;	
				title.setText("����:"+route.getCur_task());//
				if("1".equals(is_direct)){
					title.setText("ת�� "+route.getCur_task());//
				}
				params.put("is_direct",is_direct);//����ʾת��������
				intent.putExtra(GlobalParams.Data, route);
				params.put("route_id", route.getRoute_id());
			}
		}
		getData();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		dataList.clear();
		commentListViewAdapter.notifyDataSetChanged();
		initData();
	}
		// ��ȡ����
	private void getData() {
			progressBar_loading.setVisibility(View.VISIBLE);
			params.put("pageIndex", String.valueOf(page));
			HttpClient.get(Constants.Route_Blog_List, params,
					new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(String result) {
							JSONArray json_question;
							try {
								json_question = new JSONArray(result);
								for (int i = 0; i < json_question.length(); i++) {
									JSONObject jo = json_question.getJSONObject(i);
									Blog blog = new Blog();
									blog.setBlog_id(jo.getString("BLOG_ID"));
									blog.setSend_operator(jo.getString("SEND_OPERATOR"));
									blog.setSend_username(jo.getString("SEND_USERNAME"));
									blog.setFlow_id(jo.getString("FLOW_ID"));
									blog.setRoute_id(jo.optString("ROUTE_ID", ""));
									blog.setSend_content(jo.getString("BLOG_CONTENT"));
									blog.setIs_redict(jo.getInt("IS_REDICT"));
									blog.setType(jo.getInt("TYPE"));
									blog.setSend_date(jo.getString("SEND_DATE"));
									blog.setFileName(jo.getString("FILE_NAME"));
									List replyList=new ArrayList();
									JSONArray json_replyArray=jo.getJSONArray("replylist"); 
									//�ظ���ǰ5����Ϣ
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
											replyList.add(reply);
										}
									}
									blog.setReplyList(replyList);
									dataList.add(blog);
								}
							//	commentListViewAdapter.setData(dataList);
								progressBar_loading.setVisibility(View.GONE);
								commentListViewAdapter.notifyDataSetChanged();
							} catch (JSONException e) {
								progressBar_loading.setVisibility(View.GONE);
								Toast.makeText(CommentListViewActivity.this, "��ʱ������", Toast.LENGTH_SHORT).show();
							}
							super.onSuccess(result);
						}
					});
		}
	//ˢ��
	@Override
	public void onRefresh() {
		page=0;
		dataList.clear();
		getData();
		onLoad();
	}
	//��������
	@Override
	public void onLoadMore() {
		page++;
		getData();
		onLoad();
	}
	private void onLoad() {
		commentListView.stopRefresh();
		commentListView.stopLoadMore();
		commentListView.setRefreshTime("�ո�");
	}

}
