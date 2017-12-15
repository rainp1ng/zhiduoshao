package com.activity.fragment;

import com.activity.ladyclass.MainActivity;
import com.activity.ladyclass.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
/**关于界面
 * @author 雨中树*/
public class LayoutAbout extends Fragment{
	private Button menuButton;
	private MainActivity main;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_about, null);
		findView(view);
		initView(view);
		return view;
	}

	private void findView(View view) {
		// TODO Auto-generated method stub
		menuButton=(Button)view.findViewById(R.id.menu_btn);
		main=(MainActivity)LayoutAbout.this.getActivity();
		
	}
	
	private void initView(View view){
		menuButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				main.showView_MenuToogle();
			}
		});
	}
}
