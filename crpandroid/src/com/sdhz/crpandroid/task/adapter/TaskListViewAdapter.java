package com.sdhz.crpandroid.task.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.hzsoft.util.AsyncBitmapLoader;
import com.sdhz.crpandroid.R;
import com.sdhz.crpandroid.task.CommentListViewActivity;
import com.sdhz.crpandroid.task.GlobalParams;
import com.sdhz.crpandroid.task.PointMainListActivity;
import com.sdhz.domain.blog.Blog;
import com.sdhz.domain.task.TaskInfo;

/**
 * ������Ϣ adapter
 * 
 * @author TOSHIBA �����Ĳ����ļ� R.layout. task_main_activity_list_item ���ڵ��¼� (1) ת��
 *         (2)���� (3)Item ��OnClickʱ��
 */
public class TaskListViewAdapter extends BaseAdapter implements OnClickListener
{
	// public int count = 10;
	private Context					mContext;
	private LayoutInflater			listContainer;
	private List<TaskInfo>			mListItems;
	private PopupWindow				mPopupWindow;
	private CommentListViewActivity	comm;

	// �Զ���ؼ�����
	public final class ViewHolder
	{
		public TextView		main_person_name;
		public TextView		main_time;
		public TextView		main_content;
		public TextView		main_title;
		public TextView		flow_name;	//��������
		public TaskInfo		info;
		private ImageView	person_image;
		public Button		btn_comment;	// ����
		public Button		btn_redirect;		// /ת��
	}

	public TaskListViewAdapter(Context context, List<TaskInfo> listItems)
	{
		this.mContext = context;
		this.mListItems = listItems;
		listContainer = LayoutInflater.from(context); // ������ͼ����������������
	}

	public void add(List<TaskInfo> listItems)
	{
		this.mListItems.addAll(listItems);
	}

	public void setData(List<TaskInfo> listItems)
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
		ViewHolder holder = null;
		// �˴�ʹ��if else �ж� ����������ListView�Ļ����
		// view ���� ֻ��������Tag() ���Լ��ɣ������� �����´���
		if (convertView == null)
		{
			holder = new ViewHolder();
			// ���ز����ļ�
			convertView = listContainer.inflate(
					R.layout.task_main_activity_list_item, null);
			holder.main_person_name = (TextView) convertView
					.findViewById(R.id.main_person_name);
			holder.main_time = (TextView) convertView
					.findViewById(R.id.main_time);
			holder.main_content = (TextView) convertView
					.findViewById(R.id.main_content);
			holder.main_title = (TextView) convertView
					.findViewById(R.id.main_title);
			holder.flow_name = (TextView) convertView
					.findViewById(R.id.flow_name);
			holder.btn_comment = (Button) convertView
					.findViewById(R.id.btn_comment);
			holder.btn_redirect = (Button) convertView
					.findViewById(R.id.btn_redirect);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		TaskInfo info = mListItems.get(position);// postion��λ��
		holder.info = info;
		if (info != null)
		{
			holder.main_person_name.setText(info.getName());
			holder.main_time.setText(info.getBegin_date());
			holder.main_content.setText("����ID��" + info.getFlow_id());
			holder.main_title.setText("�������ƣ�" + info.getItem_name());
			holder.flow_name.setText("�������ͣ�" + info.getFlow_name());
			//����»���
			holder.flow_name.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		}
		// ת��
		holder.btn_redirect.setTag(info);
		holder.btn_redirect.setText("ת��(" + info.getZfnum() + ")");
		holder.btn_redirect.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				TaskInfo info = (TaskInfo) v.getTag();
				Intent intent2 = new Intent(mContext,
						CommentListViewActivity.class);// CommentListViewActivity
														// �����б� ���ݴ���ֵ�Ĳ�ͬ ������
				// ��comment����ʾ�Ĳ�ͬ������
				intent2.putExtra(GlobalParams.Data, info);
				intent2.putExtra(GlobalParams.IS_Direct, "1");
				mContext.startActivity(intent2);
			}
		});

		// ���۴�ֵ
		holder.btn_comment.setTag(info);
		holder.btn_comment.setText("����(" + info.getPlnum() + ")");
		// ����
		holder.btn_comment.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				TaskInfo info = (TaskInfo) v.getTag();
				Intent intent2 = new Intent(mContext,
						CommentListViewActivity.class);// CommentListViewActivity
														// �����б� ���ݴ���ֵ�Ĳ�ͬ ������
				// ��comment����ʾ�Ĳ�ͬ������
				intent2.putExtra(GlobalParams.Data, info);
				intent2.putExtra(GlobalParams.IS_Direct, "0");
				mContext.startActivity(intent2);
			}
		});
		// ���ÿؼ�����convertView
		convertView.setTag(holder);// ��������ļ���ʱ���ȡ�� ���õĵĶ���
		convertView.setOnClickListener(this);// ����ǰ��Item (ListView �еĵ���) ��Ӽ����¼�
		return convertView;
	}

	public void addItem(TaskInfo info)
	{
		mListItems.add(info);
	}

	// ���item
	@Override
	public void onClick(View v)
	{
		ViewHolder holder = (ViewHolder) v.getTag();
		Intent intent = new Intent(mContext, PointMainListActivity.class);
		// ��taskInfo ���ݸ�·��PointMainListActivity��ע�⴫�ݵĶ������ʵ�����л��ӿ�
		intent.putExtra("taskInfo", holder.info);
		intent.putExtra("name", holder.info.getItem_name());
		mContext.startActivity(intent);
	}

}
