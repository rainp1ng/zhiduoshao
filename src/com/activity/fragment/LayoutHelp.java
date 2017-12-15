package com.activity.fragment;

import java.util.ArrayList;
import java.util.List;

import com.activity.adapter.ListHelpAdapter;
import com.activity.ladyclass.HelpActivity;
import com.activity.ladyclass.MainActivity;
import com.activity.ladyclass.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
/**选择帮助的页面
 * @author 雨中树*/
public class LayoutHelp extends Fragment{
	private Button menuButton;
	private MainActivity main;
	private List<String> helpInfo;
	private GridView gv;
	private ListHelpAdapter adapter;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_help, null);
		findView(view);
		initView(view);
		return view;
	}

	private void findView(View view) {
		// TODO Auto-generated method stub
		menuButton=(Button)view.findViewById(R.id.menu_btn);
		main=(MainActivity)LayoutHelp.this.getActivity();
		helpInfo=new ArrayList<String>();
		gv=(GridView)view.findViewById(R.id.list_help);
		setHelpContent();
		adapter=new ListHelpAdapter(helpInfo,main);
	}
	
	private void initView(View view){
		gv.setAdapter(adapter);
		gv.setOnItemClickListener(new OnItemClickEvent());
		menuButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				main.showView_MenuToogle();
			}
		});
	}
	private void setHelpContent() {
		// TODO Auto-generated method stub
		helpInfo.add("1.我要怎样添加笔记本？");
		helpInfo.add("2.怎么删除不想要的笔记本？");
		helpInfo.add("3.怎么给笔记本改名字或换封面？");
		helpInfo.add("4.如何收藏想要标记的题目？");
		helpInfo.add("5.怎样删除一条看完,不想要的笔记？");
		helpInfo.add("6.为什么我提交建议时提示失败？");
	}
	class OnItemClickEvent implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			Intent intent=new Intent();
			intent.setClass(main,HelpActivity.class);
			intent.putExtra("HelpNumber",position);
			main.startActivity(intent);
			main.overridePendingTransition(R.anim.slide_right_in,0);  
		}
	}
}
