package com.activity.ladyclass;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.activity.adapter.ViewAdapter;
import com.util.method.ControlApplication;
import com.util.method.SldingFinishActivity;
/**提供帮助的页面
 * @author 雨中树*/
public class HelpActivity extends SldingFinishActivity{
	public static boolean showTeach2=true;
	public final static int TO_ADD_NOTEBOOK=0;
	public final static int TO_DELETE_NOTEBOOK=1;
	public final static int TO_CHANGE_NOTEBOOK=2;
	public final static int TO_ADD_NOTE=3;
	public final static int TO_DELETE_NOTE=4;
	public final static int TO_DO_SUG=5;
	private ViewPager viewPager;
	private Button backButton;
	private List<View> viewInfo = new ArrayList<View>();
	private int helpNumber;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		ControlApplication.getInstance().addActivity(this);
		setContentView(R.layout.container_help);
		Intent intent=getIntent();
		helpNumber=intent.getIntExtra("HelpNumber",0);
		initView();
		if(showTeach2){
			Intent i=new Intent();
			i.setClass(this,TeachActivity.class);
			i.putExtra("origin",2);
			startActivity(i);
			showTeach2=false;
		}
	}
	private void initView() {
		// TODO Auto-generated method stub
		viewPager = (ViewPager) findViewById(R.id.container_help);
		setView();
		viewPager.setAdapter(new ViewAdapter(this, viewInfo));
		backButton=(Button)findViewById(R.id.back_btn);
		backButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				toFinish();
			}
		});
	}
	public void toFinish(){
		finish();
		this.overridePendingTransition(0,R.anim.slide_right_out);  
	}
	private void setView() {
		// TODO Auto-generated method stub
		switch(helpNumber){
		case TO_ADD_NOTEBOOK:
			toAddNotebook();
			break;
		case TO_DELETE_NOTEBOOK:
			toDeleteNotebook();
			break;
		case TO_CHANGE_NOTEBOOK:
			toChangeNotebook();
			break;
		case TO_ADD_NOTE:
			toAddNote();
			break;
		case TO_DELETE_NOTE:
			toDeleteNote();
			break;
		case TO_DO_SUG:
			toDoSug();
			break;
		}
	}
	private void toAddNote() {
		// TODO Auto-generated method stub
		View v1 = LayoutInflater.from(this).inflate(R.layout.help_addnote_1, null);
		View v2 = LayoutInflater.from(this).inflate(R.layout.help_addnote_2, null);
		View v3 = LayoutInflater.from(this).inflate(R.layout.help_addnote_3, null);
		View v4 = LayoutInflater.from(this).inflate(R.layout.help_addnote_4, null);
		viewInfo.add(v1);
		viewInfo.add(v2);
		viewInfo.add(v3);
		viewInfo.add(v4);
	}
	private void toAddNotebook() {
		// TODO Auto-generated method stub
		View v1 = LayoutInflater.from(this).inflate(R.layout.help_addnotebook_1, null);
		View v2 = LayoutInflater.from(this).inflate(R.layout.help_addnotebook_2, null);
		View v3 = LayoutInflater.from(this).inflate(R.layout.help_addnotebook_3, null);
		View v4 = LayoutInflater.from(this).inflate(R.layout.help_addnotebook_4, null);
		View v5 = LayoutInflater.from(this).inflate(R.layout.help_addnotebook_5, null);
		View v6 = LayoutInflater.from(this).inflate(R.layout.help_addnotebook_6, null);
		View v7 = LayoutInflater.from(this).inflate(R.layout.help_addnotebook_7, null);
		viewInfo.add(v1);
		viewInfo.add(v2);
		viewInfo.add(v3);
		viewInfo.add(v4);
		viewInfo.add(v5);
		viewInfo.add(v6);
		viewInfo.add(v7);
	}
	private void toDeleteNotebook() {
		// TODO Auto-generated method stub
		View v1 = LayoutInflater.from(this).inflate(R.layout.help_deletenotebook_1, null);
		View v2 = LayoutInflater.from(this).inflate(R.layout.help_deletenotebook_2, null);
		View v3 = LayoutInflater.from(this).inflate(R.layout.help_deletenotebook_3, null);
		View v4 = LayoutInflater.from(this).inflate(R.layout.help_deletenotebook_4, null);
		viewInfo.add(v1);
		viewInfo.add(v2);
		viewInfo.add(v3);
		viewInfo.add(v4);
	}
	private void toChangeNotebook() {
		// TODO Auto-generated method stub
		View v1 = LayoutInflater.from(this).inflate(R.layout.help_changenotebook_1, null);
		View v2 = LayoutInflater.from(this).inflate(R.layout.help_changenotebook_2, null);
		View v3 = LayoutInflater.from(this).inflate(R.layout.help_changenotebook_3, null);
		viewInfo.add(v1);
		viewInfo.add(v2);
		viewInfo.add(v3);
	}
	private void toDeleteNote() {
		// TODO Auto-generated method stub
		View v1 = LayoutInflater.from(this).inflate(R.layout.help_deletenote_1, null);
		View v2 = LayoutInflater.from(this).inflate(R.layout.help_deletenote_2, null);
		View v3 = LayoutInflater.from(this).inflate(R.layout.help_deletenote_3, null);
		View v4 = LayoutInflater.from(this).inflate(R.layout.help_deletenote_4, null);
		viewInfo.add(v1);	
		viewInfo.add(v2);
		viewInfo.add(v3);
		viewInfo.add(v4);
	}
	private void toDoSug() {
		// TODO Auto-generated method stub
		View v1 = LayoutInflater.from(this).inflate(R.layout.help_dosug, null);
		viewInfo.add(v1);
	}
}
