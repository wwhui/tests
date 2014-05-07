package com.sdhz.crpandroid.person.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

import com.hzsoft.util.Constants;
import com.hzsoft.util.ContentUtil;
import com.hzsoft.util.cache.ImageLoader;
import com.sdhz.crpandroid.R;
import com.sdhz.crpandroid.task.CommentDetailViewActivity;
import com.sdhz.domain.blog.Blog;
import com.sdhz.view.ChildListView;

/**
 * 信息评论列表 适配器 嵌套啦两个ListView
 * 
 * 
 * */
public class AttentionListViewAdapter extends BaseAdapter implements
		OnClickListener
{

	private Context			mContext;
	private LayoutInflater	listContainer;
	private List			mListItems;
	private ViewHolder		holder	= null;
	private ImageLoader imageLoader;

	public AttentionListViewAdapter(Context context, List listItems)
	{
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
			holder = new ViewHolder();
			convertView = listContainer.inflate(R.layout.attention_list_item,
					null);
			holder.comment_title = (TextView) convertView
					.findViewById(R.id.comment_title);
			holder.comment_time = (TextView) convertView
					.findViewById(R.id.comment_create_time);
			holder.comment_person = (TextView) convertView
					.findViewById(R.id.comment_person);
			holder.imageView=(ImageView) convertView.findViewById(R.id.imageview);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		// 获取BLog(博文)的值
		Blog blog = (Blog) mListItems.get(position);
//		holder.comment_title.setTag(blog);
		holder.blog = blog;
		
		/**图片的加载*/
		if(!"".equals(blog.getFileName())){
			imageLoader.DisplayImage(Constants.ImageURL+blog.getFileName(), holder.imageView,true);
			holder.imageView.setScaleType(ScaleType.FIT_XY);
			holder.imageView.setVisibility(View.VISIBLE);
		}else{
			holder.imageView.setVisibility(View.GONE);
		}

		/** 2. Item 中的view每个组件赋值 + ":" + position */
		holder.comment_person.setText(blog.getSend_username());
		holder.comment_time.setText(blog.getSend_date());
		holder.comment_title.setText(ContentUtil.formatContentNoClick(blog
				.getSend_content()));
		List list = (List) blog.getReplyList();

		// 博文内容添加click事件
//		holder.comment_title.setOnClickListener(this);
		convertView.setOnClickListener(this);
		return convertView;
	}

	public void add(Blog map)
	{
		mListItems.add(map);
	}

	@Override
	public void onClick(View v)
	{
		ViewHolder holder=(ViewHolder)v.getTag();
		Blog blog = holder.blog;
		Intent intent = new Intent(mContext, CommentDetailViewActivity.class);
		intent.putExtra("blogInfo", blog);
		mContext.startActivity(intent);

	}
// 自定义控件集合
	class ViewHolder
	{
		public Blog				blog;
		public TextView			comment_title;
		public TextView			comment_time;
		public TextView			comment_person;
		public ChildListView	xListView;
		public ImageView	imageView;
	}
}

