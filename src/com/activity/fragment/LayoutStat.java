package com.activity.fragment;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.activity.database.GetInfoFromDatabase;
import com.activity.database.MyDatabaseHelper;
import com.activity.ladyclass.MainActivity;
import com.activity.ladyclass.R;
import com.activity.ladyclass.StuActivity;

import static com.activity.database.MyDatabaseHelper.*;
public class LayoutStat extends Fragment{
	private Button menuButton;
	private Button wrongSetButton;
	private Button swapButton;
	private Button swapStatButton;
	private MainActivity main;
	private TextView todayStat;
	private TextView bestStat;
	private TextView totalAnswer;
	private TextView totalRight;
	private TextView totalWrong;
	private Dialog confirmDialog;
	private MyDatabaseHelper myHelper;
	private GetInfoFromDatabase dbInfo;
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_stat, null);
		findView(view);
		initView(view);
		return view;
	}

	private void findView(View view) {
		// TODO Auto-generated method stub
		menuButton=(Button)view.findViewById(R.id.menu_btn);
		wrongSetButton=(Button)view.findViewById(R.id.wrong_set);
		swapButton=(Button)view.findViewById(R.id.delete_wrong_set);
		swapStatButton=(Button)view.findViewById(R.id.delete_stat);
		main=(MainActivity)LayoutStat.this.getActivity();
		todayStat=(TextView)view.findViewById(R.id.today_stat_);
		bestStat=(TextView)view.findViewById(R.id.best_stat_);
		totalAnswer=(TextView)view.findViewById(R.id.total_answer_);
		totalRight=(TextView)view.findViewById(R.id.total_right_);
		totalWrong=(TextView)view.findViewById(R.id.total_wrong_);
		myHelper=new MyDatabaseHelper(main,DATABASE_NAME,null,1);
		dbInfo=new GetInfoFromDatabase(myHelper);
	}
	
	private void initView(View view){
		setInfo();
		menuButton.setOnClickListener(new OnClickEvent());
		wrongSetButton.setOnClickListener(new OnClickEvent());
		swapButton.setOnClickListener(new OnClickEvent());
		swapStatButton.setOnClickListener(new OnClickEvent());
	}
	private void setInfo(){
		setText(todayStat,dbInfo.getTodayRight());
		setText(bestStat,dbInfo.getBestStat());
		setText(totalAnswer,dbInfo.getTotalAnswer());
		setText(totalRight,dbInfo.getTotalRight());
		setText(totalWrong,dbInfo.getTotalWrong());
	}
	private void showConfirmDialog(){
		confirmDialog = null;
		Builder builder=new AlertDialog.Builder(main);
		builder.setTitle("清空错题集");
		builder.setMessage("确定清空错题集？");

		builder.setPositiveButton("清空",new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				deleteWrongSet();
				Toast toast = Toast.makeText(main, "错题集已清空", Toast.LENGTH_SHORT); 
				toast.show();
			}
		}).setNegativeButton("取消", null);
		confirmDialog=builder.create();
		confirmDialog.show();
	}
	private void showConfirmSwapDialog(){
		confirmDialog = null;
		Builder builder=new AlertDialog.Builder(main);
		builder.setTitle("清空答题信息");
		builder.setMessage("确定清空所有答题纪录？");

		builder.setPositiveButton("清空",new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				deleteStat();
				Toast toast = Toast.makeText(main, "纪录已清空", Toast.LENGTH_SHORT); 
				toast.show();
			}
		}).setNegativeButton("取消", null);
		confirmDialog=builder.create();
		confirmDialog.show();
	}
	private void deleteWrongSet(){
		SQLiteDatabase db=myHelper.getWritableDatabase();
		String str="UPDATE "+CONTENT_TABLE+" SET "+CONTENT_FALSE+"=0 ";
		db.execSQL(str);
		str="UPDATE test_statistics SET "+CONTENT_FALSE+"=0 ";
		db.execSQL(str);
		setInfo();
	}
	private void deleteStat(){
		SQLiteDatabase db=myHelper.getWritableDatabase();
		String str="UPDATE "+CONTENT_TABLE+" SET "+CONTENT_FALSE+"=0, "+CONTENT_RIGHT+"=0 ";
		db.execSQL(str);
		str="UPDATE test_statistics SET "+CONTENT_FALSE+"=0, "+CONTENT_RIGHT+"=0 ";
		db.execSQL(str);
		setInfo();
	}
	private void setText(TextView textView,int text){
		textView.setText(text+"题");
	}
	class OnClickEvent implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v==menuButton){
				main.showView_MenuToogle();
			}else if(v==wrongSetButton){
				Intent intent=new Intent();
				intent.setClass(main,StuActivity.class);
				intent.putExtra("origin","错题集");
				main.startActivity(intent);
				main.overridePendingTransition(R.anim.slide_right_in,0); 
			}else if(v==swapButton){
				showConfirmDialog();
			}else if(v==swapStatButton){
				showConfirmSwapDialog();
			}
		}
	}
}
