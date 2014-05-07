package com.sdhz.crpandroid.widget.adpter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.hzsoft.util.PinyingUtil;
import com.sdhz.crpandroid.R;
import com.sdhz.domain.group.UserInfo;
public class PopPersonAdapter  extends BaseAdapter implements OnClickListener {
	private List<UserInfo>   itemsList;
	private Context mcontext;
	private LinkedList<UserInfo> selectedListUser;
	private HashMap<String, Integer> alphaIndexer=new  HashMap<String, Integer>() ;// ����ÿ��������list�е�λ�á�#-0��A-4��B-10��
	public PopPersonAdapter(List<UserInfo> itemsList, Context mcontext) {
		super();
		this.itemsList = itemsList;
		this.mcontext = mcontext;
		sortitemsList(this.itemsList);
		selectedListUser=new LinkedList<UserInfo>();
	}
	@SuppressWarnings("unchecked")
	public ArrayList<String> getAphl(){
		ArrayList list=new ArrayList<String>();
		  for (String o : alphaIndexer.keySet()) {
			  list.add(o);
			  }
		  Collections.sort(list, new Comparator<String>(){
				@Override
				public int compare(String info1, String info2) {
					String key1 = PinyingUtil.stringArrayToString(PinyingUtil
							.getHeadByString(info1));
					String key2 = PinyingUtil.stringArrayToString(PinyingUtil
							.getHeadByString(info2));
					return key1.compareTo(key2);
				}
		  });
		return  list;
	}

	private void sortitemsList(List  itemList) {
		if(itemList==null||itemList.size()==0)
			return ;
		Collections.sort(itemList, new Comparator<UserInfo>(){
			@Override
			public int compare(UserInfo info1, UserInfo info2) {
				String key1 = PinyingUtil.stringArrayToString(PinyingUtil
						.getHeadByString(info1.getName()));
				String key2 = PinyingUtil.stringArrayToString(PinyingUtil
						.getHeadByString(info2.getName()));
				return key1.compareTo(key2);
			}
		});
		for (int i=0;i< itemsList.size();i++) {
			UserInfo  userInfo=itemsList.get(i);
			String name = getAlpha(com.hzsoft.util.PinyingUtil
					.stringArrayToString(PinyingUtil.getHeadByString(userInfo.getName())));
			if (!alphaIndexer.containsKey(name)) {// ֻ��¼��list���״γ��ֵ�λ��
				alphaIndexer.put(name, i);
			}
		}
	}
	public void setDataList(List<UserInfo> itemsList){
		selectedListUser.clear();
		this.itemsList=itemsList;
		sortitemsList(this.itemsList);
		this.notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return itemsList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return itemsList.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	public int getAlphaI(String alpha){
		if(alphaIndexer.containsKey(alpha)){
			return alphaIndexer.get(alpha);
		}
		return -1;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(convertView==null){
			holder=new ViewHolder();
			convertView=LayoutInflater.from(mcontext).inflate(R.layout.pop_winow_select_user_item, null);
			holder.alpha=(TextView) convertView.findViewById(R.id.alpha);
			holder.name=(TextView) convertView.findViewById(R.id.name);		
			holder.checkBox=(CheckBox) convertView.findViewById(R.id.option_checkbox);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		UserInfo info=itemsList.get(position);
		holder.info=info;
		holder.name.setText(info.getName());
		// ��ǰ��ϵ�˵�sortKey
		String currentStr = getAlpha(PinyingUtil
				.stringArrayToString(PinyingUtil.getHeadByString(info.getName())));
		// ��һ����ϵ�˵�sortKey
		String previewStr = (position - 1) >= 0 ? getAlpha(PinyingUtil
				.stringArrayToString(PinyingUtil.getHeadByString(itemsList
						.get(position - 1).getName()))) : " ";
		/**
		 * �ж���ʾ#��A-Z��TextView������ɼ�
		 */
		if (!previewStr.equals(currentStr)) { // ��ǰ��ϵ�˵�sortKey��=��һ����ϵ�˵�sortKey��˵����ǰ��ϵ�������顣
			holder.alpha.setVisibility(View.VISIBLE);
			holder.alpha.setText(currentStr);
		} else {
			holder.alpha.setVisibility(View.GONE);
		}
		convertView.setOnClickListener(this);
		convertView.setTag(holder);
		return convertView;
	}
	class ViewHolder{
		public TextView  alpha;
		public TextView  name; 
		public CheckBox checkBox;
		public UserInfo info;
	}
	
	/**
	 * ��ȡӢ�ĵ�����ĸ����Ӣ����ĸ��#����
	 * 
	 * @param str
	 * @return
	 */
	private String getAlpha(String str1) {
		///��ȡ�ַ������ѡ����������������ȥ
		String msg[] = str1.split("\\(");
		String str = msg[0];
		if (str == null) {
			return "#";
		}

		if (str.trim().length() == 0) {
			return "#";
		}
		///ȡ���ַ����еĵĵ�һ���ַ�
		char c = str.trim().substring(0, 1).charAt(0);
		// ������ʽ���ж�����ĸ�Ƿ���Ӣ����ĸ
		Pattern pattern = Pattern.compile("^[A-Za-z]");
		if (pattern.matcher(c + "").matches()) {
			return (c + "").toUpperCase(); // ��д���
		} else {
			return "#";
		}
	}
	public List<UserInfo> getSelectedUser(){
		return selectedListUser;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		ViewHolder child=(ViewHolder) v.getTag();
		if(selectedListUser.contains(child.info)){
			selectedListUser.remove(child.info);
			}else{
			selectedListUser.add(child.info);
			}
		child.checkBox.toggle();
	}
}
