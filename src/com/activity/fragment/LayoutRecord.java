package com.activity.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.activity.adapter.ListRecordAdapter;
import com.activity.database.GetInfoFromDatabase;
import com.activity.database.MyDatabaseHelper;
import com.activity.ladyclass.MainActivity;
import com.activity.ladyclass.R;
import com.activity.ladyclass.StuActivity;

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
import static com.activity.database.MyDatabaseHelper.*;
import static com.util.method.DateMethod.*;
/**查询阅读、学习记录的功能，实现了日期的天数递减，
 * 可以查询8日内的确切阅读记录以及更早的阅读记录
 * @author 雨中树
 * */
public class LayoutRecord extends Fragment{
	private Button menuButton;
	private MainActivity main;
	private Date currentDate;
	private int date;
	private MyDatabaseHelper myHelper;
	private GetInfoFromDatabase dbInfo;
 	private List<Integer> record;
	private GridView gv;
	private ListRecordAdapter adapter;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_record, null);
		findView(view);
		getCurDate();
		try {
			getRecord();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		showRecord();
		initView(view);
		return view;
	}
	private void getCurDate(){
		// TODO Auto-generated method stub
		SimpleDateFormat formatter=new SimpleDateFormat("yyyyMMdd");
		currentDate=new Date(System.currentTimeMillis());
		date=Integer.parseInt(formatter.format(currentDate));
		//Log.e("CurTime",date);
		//Log.e("datedate",year+"年"+month+"月"+day+"日");
	}
	private void getRecord() throws ParseException {
		// TODO Auto-generated method stub
		record.add(date);
		//将8天内有阅读记录的日期记录下来
		for(int myDate=subDay2Date(date,-1);myDate>subDay2Date(date,-8);myDate=subDay2Date(myDate,-1)){
			dbInfo=new GetInfoFromDatabase(myHelper);
			if(dbInfo.checkRecord(myDate)){
				record.add(myDate);
				//Log.e(i+++"",myDate+"");
			}
		}
	}
	public void showRecord(){
		adapter=new ListRecordAdapter(record,main);
		gv.setAdapter(adapter);
	}
	private void findView(View view) {
		// TODO Auto-generated method stub
		menuButton=(Button)view.findViewById(R.id.menu_btn);
		main=(MainActivity)LayoutRecord.this.getActivity();
		myHelper=new MyDatabaseHelper(main,DATABASE_NAME,null,1);
		gv=(GridView)view.findViewById(R.id.list_record);
		record=new ArrayList<Integer>();
	}
	private void initView(View view){
		menuButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				main.showView_MenuToogle();
			}
		});
		gv.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(position<record.size()){
					int date=record.get(position);
					//Log.e("position",position+"");
					Intent intent=new Intent();
					intent.setClass(main,StuActivity.class);
					intent.putExtra("origin",getYMD(date));
					intent.putExtra(STUDY_TYPE,""+date);
					//Log.e("日期",date+"");
					main.startActivity(intent);
					main.overridePendingTransition(R.anim.slide_right_in,0);  
				}else if(position>=record.size()){
					Intent intent=new Intent();
					intent.setClass(main,StuActivity.class);
					intent.putExtra("origin","更早");
					intent.putExtra(STUDY_TYPE,""+date);
					startActivity(intent);
				}
			}
		});
	}
}
