package com.sdhz.crpandroid.group.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hp.hpl.sparta.Sparta.Cache;
import com.sdhz.crpandroid.CrpaApplication;
import com.sdhz.crpandroid.R;
import com.sdhz.crpandroid.widget.PopInput;
import com.sdhz.domain.group.TalkInfo;

public class TalkListAdapter extends BaseAdapter
{
	private Context			mContext;
	private LayoutInflater	listContainer;
	private List<TalkInfo>	mListItems;
	private PopInput		popInput;
	ViewHolder				holder;
	private static final int ITEMCOUNT = 2;// ��Ϣ���͵�����
	public static interface IMsgViewType {
		int IMVT_COM_OTHER = 0;// �յ��Է�����Ϣ
		int IMVT_TO_MY = 1;// �Լ����ͳ�ȥ����Ϣ
	}
	public TalkListAdapter(Context context, List<TalkInfo> listItems)
	{
		this.mContext = context;
		this.mListItems = listItems;
		listContainer = LayoutInflater.from(context); // ������ͼ����������������
	}

	public void addItem(List<TalkInfo> listItems)
	{
		this.mListItems.addAll(listItems);
	}

	public void setData(List<TalkInfo> listItems)
	{
		this.mListItems = listItems;
		this.notifyDataSetChanged();
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
	public int getItemViewType(int position) {
		TalkInfo info=mListItems.get(position);
		String accountId = ( (CrpaApplication)mContext.getApplicationContext()).getAccountString();
		if(accountId.equals(info.getU_id())){
			return IMsgViewType.IMVT_TO_MY;
		}else{
			return IMsgViewType.IMVT_COM_OTHER;
		}
	}

	@Override
	public int getViewTypeCount() {
		return ITEMCOUNT;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder = null;
		final TalkInfo talkInfo = mListItems.get(position);
		String accountId =  ( (CrpaApplication)mContext.getApplicationContext()).getAccountString();
		/** 1.��ʼ�� Item �е�view */
		int type=IMsgViewType.IMVT_COM_OTHER;
		if(accountId.equals(talkInfo.getU_id())){
			type=IMsgViewType.IMVT_TO_MY;
		}
		if (convertView == null)
		{
			holder = new ViewHolder();
			switch (type) {
			case IMsgViewType.IMVT_TO_MY:
				convertView = listContainer.inflate(
						R.layout.talk_list_item_right, null);
				break;
			case IMsgViewType.IMVT_COM_OTHER:
				convertView = listContainer.inflate(R.layout.talk_list_item,
					null);
				break;
			}
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_talk_content = (TextView) convertView
					.findViewById(R.id.tv_talk_content);
			holder.tv_sendtime=(TextView) convertView.findViewById(R.id.tv_sendtime);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		/** 2. Item �е�viewÿ�������ֵ */
			holder.talkInfo = talkInfo;
			holder.tv_name.setText(talkInfo.getName()+":");
			holder.tv_name.setTextColor(Color.BLUE);
			holder.tv_talk_content.setText(talkInfo.getTalk_content());
			holder.tv_sendtime.setText(talkInfo.getCreate_date());
		return convertView;
	}

	public void addItem(TalkInfo talkInfo)
	{
		mListItems.add(talkInfo);
	}

	// �Զ���ؼ�����
	class ViewHolder
	{
		public TextView	tv_name;			// ����
		public TextView	tv_talk_content;	// ��������
		public TextView tv_sendtime;		//����ʱ��
		public TalkInfo	talkInfo;
	}

}
