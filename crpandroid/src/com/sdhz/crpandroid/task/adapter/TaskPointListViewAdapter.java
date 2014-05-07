package com.sdhz.crpandroid.task.adapter;

import java.util.List;
import java.util.Map;

import com.hzsoft.util.AsyncBitmapLoader;
import com.sdhz.crpandroid.R;
import com.sdhz.crpandroid.task.CommentDetailViewActivity;
import com.sdhz.crpandroid.task.CommentListViewActivity;
import com.sdhz.crpandroid.task.CreateNewComentActivity;
import com.sdhz.crpandroid.task.GlobalParams;
import com.sdhz.crpandroid.task.PointMainListActivity;
import com.sdhz.crpandroid.task.adapter.TaskListViewAdapter.ViewHolder;
import com.sdhz.domain.task.TaskRoute;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/***
 *  route  ·����Ϣ ��adapter ������
 *  
 * 
 * 
 * */

public class TaskPointListViewAdapter extends BaseAdapter implements OnClickListener {

//  public int count = 10;
  private Context mContext;
  private LayoutInflater listContainer;
  private List<TaskRoute> mListItems;
  private AsyncBitmapLoader asyncBitmapLoader;

  // �Զ���ؼ�����
  public final class ViewHolder
  {
      public TextView audit_content;//���������
      public TextView route_name;//�ڵ�����
      public TextView cur_role;//������
      private TextView route_date;//ʱ��
      public TaskRoute route;
      public TextView main_content;
      public Button main_item_redirect;
      public Button main_item_comment;
      public int i;
  }

  public TaskPointListViewAdapter(Context context, List<TaskRoute> listItems)
  {
      asyncBitmapLoader = new AsyncBitmapLoader();

      this.mContext = context;
      this.mListItems = listItems;
      listContainer = LayoutInflater.from(context); // ������ͼ����������������
  }
  
  public void setData(List<TaskRoute> listItems)
  {
      this.mListItems = listItems;
  }
  public void clear(){
	  this.mListItems.clear();
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
      ViewHolder holder = null;
      //���ص����� �����ļ�
      if (convertView == null)
      {
          holder = new ViewHolder();
          convertView = listContainer.inflate(R.layout.task_point_list_item, null);
          holder.audit_content = (TextView) convertView.findViewById(R.id.audit_content);
          holder.main_item_comment = (Button) convertView.findViewById(R.id.btn_comment);
          holder.route_name=(TextView) convertView.findViewById(R.id.route_name);
          holder.cur_role=(TextView) convertView.findViewById(R.id.cur_role);
          holder.route_date=(TextView) convertView.findViewById(R.id.route_date);
          holder.main_item_redirect=(Button) convertView.findViewById(R.id.btn_redirect);
      } else
      {
          holder = (ViewHolder) convertView.getTag();
      }
      holder.i=position;
      TaskRoute  route = mListItems.get(position);
      holder.route=route;
      if (route != null)
      {
    	  holder.audit_content .setText(route.getAudit_content());
    	  String cur_task_state=route.getCur_task_state();
    	  int state=Integer.valueOf(cur_task_state);
    	  if(state==0)
    	  {
    		  holder.route_name.setTextColor(Color.RED);
    	  }
    	  else
    	  {
    		  holder.route_name.setTextColor(Color.GREEN);
    	  }
    	  holder.route_name.setText(route.getCur_task());
    	  holder.route_name.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//����»���
    	  holder.cur_role.setText(route.getCur_name());
    	  holder.route_date.setText(route.getCommit_date());
      }
      //ת��
      holder.main_item_redirect.setTag(route);
      holder.main_item_redirect.setText("ת��("+route.getZfNum()+")");
      holder.main_item_redirect.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			TaskRoute routeInfo=(TaskRoute) v.getTag();
			//��ת�� ��� ��·�ɽڵ��  �����б�
	          Intent intent2 = new Intent(mContext, CommentListViewActivity.class);
	          intent2.putExtra(GlobalParams.Data,routeInfo);//CommentListViewActivity ͳһ����data�������
	          intent2.putExtra(GlobalParams.IS_Direct, "1");
	          mContext.startActivity(intent2);
		}
	});
      
      
      //����
      holder.main_item_comment.setTag(route);
      holder.main_item_comment.setText("����("+route.getPlNum()+")");
      holder.main_item_comment.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TaskRoute routeInfo=(TaskRoute) v.getTag();
					//��ת�� ��� ��·�ɽڵ��  �����б�
		          Intent intent2 = new Intent(mContext, CommentListViewActivity.class);
		          intent2.putExtra(GlobalParams.IS_Direct, "0");
		          intent2.putExtra(GlobalParams.Data,routeInfo);//CommentListViewActivity ͳһ����data�������
		          mContext.startActivity(intent2);
			}
		});
      // ���ÿؼ�����convertView
       convertView.setTag(holder);
      convertView.setOnClickListener(this);//��Item ��OnClick �¼� 
      return convertView;
  }
  public void add(TaskRoute route)
  {
      mListItems.add(route);
  }
	@Override
	public void onClick(View v) {
		ViewHolder     holder = (ViewHolder) v.getTag();
		// ��ת��   ���� ·�ɽڵ�� �����б�
		 Intent intent=new Intent(mContext,CommentListViewActivity.class);
		 intent.putExtra("data", holder.route);
		 mContext.startActivity(intent);
	}

}
