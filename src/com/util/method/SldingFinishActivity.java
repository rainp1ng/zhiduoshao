package com.util.method;

import com.activity.ladyclass.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
/**
 * �̳и��࣬ʵ�����һ����˳���Ч��
 * Container�е�ViewPage�����SlidingFinishLayout��setViewPager();
 * 
 * @author ������
 *
 */
public class SldingFinishActivity extends EasyTrackers {
	protected SlidingFinishLayout layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		layout = (SlidingFinishLayout) LayoutInflater.from(this).inflate(
				R.layout.container_activity, null);
		layout.attachToActivity(this);
	}
	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		overridePendingTransition(R.anim.slide_right_in, R.anim.slide_remain);
	}
	// Press the back button in mobile phone
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(0, R.anim.slide_right_out);
	}

	
}
