package com.activity.ladyclass;

import java.util.ArrayList;
import java.util.List;

import com.activity.adapter.ViewAdapter;
import com.activity.database.MyDatabaseHelper;
import com.util.method.EasyTrackers;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import static com.activity.database.MyDatabaseHelper.DATABASE_NAME;
/**第一次打开应用出现的导航界面，
 * 静态成员needHelp监听是否关闭
 * @author 雨中树*/
public class NavigationActivity extends EasyTrackers{
	public static boolean needHelp=true;
	private ViewPager viewPager;
	private List<View> viewInfo = new ArrayList<View>();
	private SQLiteDatabase db;
	private MyDatabaseHelper myHelper;
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.container_navigation);
		initView();
	}
	private void initView() {
		// TODO Auto-generated method stub
		myHelper=new MyDatabaseHelper(NavigationActivity.this,DATABASE_NAME,null,1);
		db=myHelper.getWritableDatabase();
		viewPager = (ViewPager) findViewById(R.id.container_navigation);
		setView();
		viewPager.setAdapter(new ViewAdapter(this, viewInfo));
	}
	private void setView() {
		// TODO Auto-generated method stub
		View v1 = LayoutInflater.from(this).inflate(R.layout.navigation_1, null);
		View v2 = LayoutInflater.from(this).inflate(R.layout.navigation_2, null);
		View v3 = LayoutInflater.from(this).inflate(R.layout.navigation_3, null);
		View v4 = LayoutInflater.from(this).inflate(R.layout.navigation_4, null);
		viewInfo.add(v1);
		viewInfo.add(v2);
		viewInfo.add(v3);
		viewInfo.add(v4);
		CheckBox checkNeedHelp=(CheckBox)v4.findViewById(R.id.need_help);
		checkNeedHelp.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					String str="UPDATE need_help SET need_help=0 WHERE need_help=1";
					db.execSQL(str);
				}else{
					needHelp=true;
				}
			}
		});
		Button enterButton=(Button)v4.findViewById(R.id.enter_btn);
		enterButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(NavigationActivity.this,MainActivity.class)); 
				NavigationActivity.this.finish();
			}
			
		});
	}
}
