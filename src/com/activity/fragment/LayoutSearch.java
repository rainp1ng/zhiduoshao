package com.activity.fragment;

import com.activity.ladyclass.MainActivity;
import com.activity.ladyclass.R;
import com.activity.ladyclass.SearchByActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
/**查询的页面
 * @author 雨中树*/
public class LayoutSearch extends Fragment{
	private Button menuButton;
	private MainActivity main;
	private LinearLayout contentButton;
	private LinearLayout titleButton;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_search, null);
		findView(view);
		initView(view);
		return view;
	}
	private void findView(View view) {
		// TODO Auto-generated method stub
		menuButton=(Button)view.findViewById(R.id.menu_btn);
		main=(MainActivity)LayoutSearch.this.getActivity();
		contentButton=(LinearLayout)view.findViewById(R.id.search_by_content);
		titleButton=(LinearLayout)view.findViewById(R.id.search_by_title);
	}
	private void initView(View view){
		contentButton.setOnClickListener(new OnClickEvent());
		titleButton.setOnClickListener(new OnClickEvent());
		menuButton.setOnClickListener(new OnClickEvent());
	}
	class OnClickEvent implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v==menuButton){
				main.showView_MenuToogle();
			}else if(v==contentButton){
				Intent intent=new Intent();
				intent.setClass(main,SearchByActivity.class);
				intent.putExtra("model",true);//1为内容搜索
				main.startActivity(intent);
				main.overridePendingTransition(R.anim.slide_right_in,0);  
			}else if(v==titleButton){
				Intent intent=new Intent();
				intent.setClass(main,SearchByActivity.class);
				intent.putExtra("model",false);//0为标题搜索
				main.startActivity(intent);
				main.overridePendingTransition(R.anim.slide_right_in,0);  
			}
		}
	}
}

