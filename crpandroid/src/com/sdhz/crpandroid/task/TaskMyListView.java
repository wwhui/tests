package com.sdhz.crpandroid.task;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.sdhz.crpandroid.R;
import com.sdhz.crpandroid.task.adapter.TaskListViewAdapter;
import com.sdhz.view.XListView;
import com.sdhz.view.XListView.IXListViewListener;

public class TaskMyListView extends Activity  implements OnItemClickListener ,IXListViewListener {
	private XListView info_main_listview;
	private LinkedList<Map<String, Object>> mListItems;
	private TaskListViewAdapter mAdapter;
	private TextView title_bar_text;// ±ÍÃ‚

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.task_my_activity_list_view);
		title_bar_text = (TextView) findViewById(R.id.title_bar);
		Intent intent = getIntent();
		if (intent.getStringExtra("title_name") != null) {
			title_bar_text.setText(intent.getStringExtra("title_name"));
		}
		//º”‘ÿ
		initLoad();
		//º”‘ÿlistVIew
		initlistLoad();

	}

	private void initLoad() {
		// ◊Û≤‡∞¥≈•µƒ ±º‰
		((Button) findViewById(R.id.create_new))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						//Intent intent=new Intent(InfoMyListView.this);
					}
				});
	}
	
	/***
	 * º”‘ÿlistView
	 * 
	 * */
	private void  initlistLoad(){
		info_main_listview = (XListView) findViewById(R.id.xlist_view);
		info_main_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				Map map = mListItems.get(position);
				Intent intent = new Intent(TaskMyListView.this,
						CommentDetailViewActivity.class);
				Log.e("error", "sdsdsdddddddddddddddddd");
				startActivity(intent);
			}
		});

		mListItems = new LinkedList<Map<String, Object>>();
		HashMap map;
		Random ran = new Random();
		// mListItems.addAll(Arrays.asList(mStrings));
		mAdapter = new TaskListViewAdapter(this, null);
		info_main_listview.setAdapter(mAdapter);
	}
	
	
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
			}
			return mStrings;
		}

		@Override
		protected void onPostExecute(String[] result) {
			Map map = new HashMap();
			map.put("person", "ÕıŒƒª·");
			map.put("time", "2014-2-2");
			map.put("content",
					"≤‚ ‘≤‚ ‘≤‚≤‚ ‘≤‚≤‚ ‘≤‚ ‘≤‚≤‚ ‘≤‚≤‚ ‘≤‚ ‘≤‚≤‚ ‘≤‚≤‚ ‘≤‚ ‘≤‚≤‚ ‘≤‚≤‚ ‘≤‚ ‘≤‚≤‚ ‘≤‚≤‚ ‘≤‚ ‘≤‚≤‚ ‘≤‚");
			mListItems.addLast(map);
			mAdapter.notifyDataSetChanged();
			// Call onRefreshComplete when the list has been refreshed.
			//info_main_listview.onRefreshComplete();
			super.onPostExecute(result);
		}
	}

	private String[] mStrings = { "Abbaye de Belloc",
			"Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
			"Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu",
			"Airag", "Airedale", "Aisy Cendre", "Allgauer Emmentaler",
			"Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam",
			"Abondance", "Ackawi", "Acorn", "Adelost", "Affidelice au Chablis",
			"Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
			"Allgauer Emmentaler" };

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}

}
