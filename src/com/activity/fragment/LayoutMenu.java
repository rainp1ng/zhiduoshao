package com.activity.fragment;

import com.activity.ladyclass.R;
import com.activity.ladyclass.MainActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
/**菜单的页面
 * @author 雨中树*/
public class LayoutMenu extends Fragment{
	private MainActivity main;
	private LinearLayout menuButtonStudy;
	private LinearLayout menuButtonNote;
	private LinearLayout menuButtonSearch;
	private LinearLayout menuButtonSug;
	private LinearLayout menuButtonHelp;
	private LinearLayout menuButtonModel;
	private LinearLayout menuButtonAbout;
	private LinearLayout menuButtonExit;
	private LinearLayout menuButtonRecord;
	public static boolean buttonClickable=true;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_menu, null);
		findView(view);
		initView();
		
		return view;
	}
	//初始化菜单栏的按钮
	public void findView(View view){
		main=(MainActivity)LayoutMenu.this.getActivity();
		menuButtonStudy=(LinearLayout)view.findViewById(R.id.menu_btn_study);
		menuButtonNote=(LinearLayout)view.findViewById(R.id.menu_btn_note);
		menuButtonSearch=(LinearLayout)view.findViewById(R.id.menu_btn_search);
		menuButtonSug=(LinearLayout)view.findViewById(R.id.menu_btn_suggestion);
		menuButtonHelp=(LinearLayout)view.findViewById(R.id.menu_btn_help);
		menuButtonModel=(LinearLayout)view.findViewById(R.id.menu_btn_model);
		menuButtonAbout=(LinearLayout)view.findViewById(R.id.menu_btn_about);
		menuButtonExit=(LinearLayout)view.findViewById(R.id.menu_btn_exit);
		menuButtonRecord=(LinearLayout)view.findViewById(R.id.menu_btn_record);
	}
	//为按钮设置点击事件
	public void initView(){
		menuButtonStudy.setOnClickListener(new OnClickItemEvent());
		menuButtonNote.setOnClickListener(new OnClickItemEvent());
		menuButtonSearch.setOnClickListener(new OnClickItemEvent());
		menuButtonSug.setOnClickListener(new OnClickItemEvent());
		menuButtonHelp.setOnClickListener(new OnClickItemEvent());
		menuButtonModel.setOnClickListener(new OnClickItemEvent());
		menuButtonAbout.setOnClickListener(new OnClickItemEvent());
		menuButtonExit.setOnClickListener(new OnClickItemEvent());
		menuButtonRecord.setOnClickListener(new OnClickItemEvent());
	}
	
	class OnClickItemEvent implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(buttonClickable){
				if(v==menuButtonStudy){
					main.executeStudy();
				}else if(v==menuButtonNote){
					main.executeNote();
				}else if(v==menuButtonSearch){
					main.executeSearch();
				}else if(v==menuButtonSug){
					main.executeSug();
				}else if(v==menuButtonHelp){
					main.executeHelp();
				}else if(v==menuButtonModel){
					main.executeModel();
				}else if(v==menuButtonAbout){
					main.executeAbout();
				}else if(v==menuButtonExit){
					main.executeExit();
				}else if(v==menuButtonRecord){
					main.executeRecord();
				}
			}
			buttonClickable=false;
		}
	}
}
