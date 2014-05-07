package com.sdhz.crpandroid.person.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hzsoft.util.AsyncBitmapLoader;
import com.hzsoft.util.Constants;
import com.hzsoft.util.cache.ImageLoader;
import com.sdhz.crpandroid.R;
import com.sdhz.crpandroid.task.CommentDetailViewActivity;
import com.sdhz.crpandroid.task.adapter.CommentSecondListViewAdapter;
import com.sdhz.crpandroid.widget.PopInput;
import com.sdhz.domain.blog.Blog;
import com.sdhz.view.ChildListView;

public class MyCreateListViewAdapter extends BaseAdapter  implements OnClickListener
{
	private Context						mContext;
	private LayoutInflater				listContainer;
	private List<Blog>	mListItems;
	private AsyncBitmapLoader			asyncBitmapLoader;
	private PopInput					popInput;
	ViewHolder	holder;
	private ImageLoader imageLoader;
	public MyCreateListViewAdapter(Context context,
			List<Blog> listItems)
	{
		asyncBitmapLoader = new AsyncBitmapLoader();

		this.mContext = context;
		this.mListItems = listItems;
		listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
		imageLoader = new ImageLoader(context);
	}

	public void addItem(List<Blog> listItems)
	{
		this.mListItems.addAll(listItems);
	}
	public void setData(List<Blog> listItems)
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
					R.layout.mycreateflow_list_item, null);
			holder.comment_title = (TextView) convertView
					.findViewById(R.id.comment_title);
			holder.tv_flowname = (TextView) convertView
					.findViewById(R.id.tv_flowname);
			holder.tv_flowid = (TextView) convertView
					.findViewById(R.id.tv_flowid);
			holder.imageView=(ImageView) convertView.findViewById(R.id.imageview);
//			holder.addbtn = (Button) convertView
//					.findViewById(R.id.addbtn);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
//		holder.addbtn.setTag(holder);
//		holder.addbtn.setOnClickListener(new OnClickListener()
//		{
//
//			@Override
//			public void onClick(View v)
//			{
//				// TODO Auto-generated method stub
//				ViewHolder holder=(ViewHolder) v.getTag();
//				Toast.makeText(mContext,"",Toast.LENGTH_SHORT).show();
//				holder.addbtn.setBackgroundResource(R.drawable.button_add_color);
//				holder.addbtn.setTextColor(Color.BLUE);
//				holder.addbtn.setClickable(false);
//			}
//		});
		/** 2. Item 中的view每个组件赋值 */
		Blog blog = mListItems.get(position);
		holder.blog=blog;
		/**图片的加载*/
		if(!"".equals(blog.getFileName())){
			imageLoader.DisplayImage(Constants.ImageURL+blog.getFileName(), holder.imageView,true);
			holder.imageView.setScaleType(ScaleType.FIT_XY);
			holder.imageView.setVisibility(View.VISIBLE);
		}else{
			holder.imageView.setVisibility(View.GONE);
		}
		if (blog != null)
		{
			holder.comment_title.setText(blog.getSend_date());
			holder.tv_flowname.setText(blog.getSend_content());
			holder.tv_flowid.setVisibility(View.GONE);
//			holder.tv_flowid.setText(blog.getFlow_id());
//			holder.tv_projectname.setText(taskInfo.getProject_name());
		}
		convertView.setOnClickListener(this);
//		convertView.setTag(holder);
		return convertView;
	}

	public void addImageItem(Blog blog)
	{
		mListItems.add(blog);
	}

	@Override
	public void onClick(View v) {
		ViewHolder holdler=(ViewHolder) v.getTag();
		Blog blog=holdler.blog;
		Intent intent = new Intent(mContext, CommentDetailViewActivity.class);
		intent.putExtra("blogInfo", blog);
		mContext.startActivity(intent);
	}
	
// 自定义控件集合
	class ViewHolder
	{
		public TextView						comment_title;
		public TextView						comment_time;
		public TextView						tv_flowname;
		public TextView						tv_flowid;
		public Button						moreButton;
		public ChildListView				xListView;
		public Button						addbtn;
		public CommentSecondListViewAdapter	secondListAdapter;
		public Blog blog;
		public ImageView	imageView;
	}


}
