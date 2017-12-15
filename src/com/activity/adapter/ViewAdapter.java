package com.activity.adapter;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
/**提供帮助以及导航的内容适配器
 * @author 雨中树*/
public class ViewAdapter extends PagerAdapter{
	private List<View> list;
	
	public ViewAdapter(Context context, List<View> list){
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager)container).removeView(list.get(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View v = list.get(position);
		((ViewPager)container).addView(v);
		return v;
	}
}
