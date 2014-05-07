package com.sdhz.crpandroid.person.adapter;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PersonFragmentAdapter  extends FragmentPagerAdapter{
	protected static final String[] CONTENT = new String[] { "This", "Is", "A", "Test", };
	private int mCount = CONTENT.length;
	public PersonFragmentAdapter(FragmentManager fm) {
		super(fm);
	}
	
	public void setCount(int count) {
		if (count > 0 && count <= 10) {
			mCount = count;
			notifyDataSetChanged();
		}
	}
	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

}
