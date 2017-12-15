package com.activity.ladyclass;

import com.activity.fragment.*;
import com.activity.ladyclass.R;
import com.google.analytics.tracking.android.EasyTracker;
import com.util.method.ControlApplication;
import com.util.method.SlidingLayout;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
/**主页面
 * @author 雨中树*/
public class MainActivity extends FragmentActivity{
	public static boolean showTeach=true;
	private Dialog exitDialog;
	private SlidingLayout slidingLayout;
	private LayoutMenu layoutMenu;
	private LayoutStudy layoutStudy;
	private LayoutNote layoutNote;
	private LayoutRecord layoutRecord;
	private LayoutSearch layoutSearch;
	private LayoutAbout layoutAbout;
	private LayoutHelp layoutHelp;
	private LayoutSug layoutSug;
	private LayoutStat layoutStat;
	private	View menuView;
	private View mainView;
	private FragmentTransaction transaction;
	private int viewWidth_Menu=200;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ControlApplication.getInstance().addActivity(this);
		findView();
		initViewStudy();
		if(showTeach){
			Intent intent=new Intent();
			intent.setClass(this,TeachActivity.class);
			intent.putExtra("origin",1);
			startActivity(intent);
			showTeach=false;
		}
	}
	public void findView(){
		menuView=View.inflate(this, R.layout.container_menu, null);
		mainView=View.inflate(this, R.layout.container_main, null);
		slidingLayout=(SlidingLayout)findViewById(R.id.activity_main);
	}
	public void initViewStudy(){
		slidingLayout.setView(menuView, mainView, viewWidth_Menu);
		transaction = getSupportFragmentManager().beginTransaction();
		layoutMenu = new LayoutMenu();
		transaction.replace(R.id.container_menu, layoutMenu);
		layoutStudy = new LayoutStudy();
		transaction.replace(R.id.container_main, layoutStudy);
		transaction.commit();
		slidingLayout.setViewShowable(true);
	}
	public void showView_ContentToogle(){
		transaction.commit();
		slidingLayout.setViewShowable(true);
		showView_MenuToogle();
	}
	public void showView_MenuToogle() {
		slidingLayout.showView_MenuToogle();
	}
	//菜单栏
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK&&!slidingLayout.showingView_ContentA()&&slidingLayout.showingView_Menu()){
			slidingLayout.setViewShowable(true);
			showView_MenuToogle();
			//slidingLayout.setView_MenuClickable(false);
		}else if(keyCode==KeyEvent.KEYCODE_BACK&&!slidingLayout.showingView_Menu()&&slidingLayout.showingView_ContentA()){
			showExitDialog();
		}
		return false;
	}
	public void showExitDialog(){
		exitDialog = null;
		Builder builder=new AlertDialog.Builder(MainActivity.this);
		builder.setTitle("退出");
		builder.setMessage("是否退出知多少？");

		builder.setPositiveButton("退出",new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				finish();
			}
		}).setNegativeButton("取消", null);
		exitDialog=builder.create();
		exitDialog.show();
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			showView_MenuToogle();
			return true;
		}else if(id==R.id.action_exit){
			executeExit();
		}
		return super.onOptionsItemSelected(item);
	}

	public void executeExit() {
		// TODO Auto-generated method stub
		finish();
	}

	public void executeStudy() {
		// TODO Auto-generated method stub
		transaction = getSupportFragmentManager().beginTransaction();
		layoutNote = new LayoutNote();
		transaction.replace(R.id.container_main, layoutStudy);
		showView_ContentToogle();
	}

	public void executeNote() {
		// TODO Auto-generated method stub
		transaction = getSupportFragmentManager().beginTransaction();
		layoutNote = new LayoutNote();
		transaction.replace(R.id.container_main, layoutNote);
		showView_ContentToogle();
	}

	public void executeSearch() {
		// TODO Auto-generated method stub
		transaction = getSupportFragmentManager().beginTransaction();
		layoutSearch = new LayoutSearch();
		transaction.replace(R.id.container_main, layoutSearch);
		showView_ContentToogle();
	}

	public void executeSug() {
		// TODO Auto-generated method stub
		transaction = getSupportFragmentManager().beginTransaction();
		layoutSug = new LayoutSug();
		transaction.replace(R.id.container_main, layoutSug);
		showView_ContentToogle();
	}
	public void executeHelp() {
		// TODO Auto-generated method stub
		transaction = getSupportFragmentManager().beginTransaction();
		layoutHelp = new LayoutHelp();
		transaction.replace(R.id.container_main, layoutHelp);
		showView_ContentToogle();
	}
	public void executeAbout() {
		// TODO Auto-generated method stub
		transaction = getSupportFragmentManager().beginTransaction();
		layoutAbout = new LayoutAbout();
		transaction.replace(R.id.container_main, layoutAbout);
		showView_ContentToogle();
	}
	public void executeRecord() {
		// TODO Auto-generated method stub
		transaction = getSupportFragmentManager().beginTransaction();
		layoutRecord = new LayoutRecord();
		transaction.replace(R.id.container_main, layoutRecord);
		showView_ContentToogle();
	}
	public void executeModel() {
		// TODO Auto-generated method stub
		transaction = getSupportFragmentManager().beginTransaction();
		layoutStat = new LayoutStat();
		transaction.replace(R.id.container_main, layoutStat);
		showView_ContentToogle();
	}
	@Override
	  public void onStart() {
	    super.onStart();
	     // The rest of your onStart() code.
	    EasyTracker.getInstance(this).activityStart(this);  // Add this method.
	  }

	  @Override
	  public void onStop() {
	    super.onStop();
	     // The rest of your onStop() code.
	    EasyTracker.getInstance(this).activityStop(this);  // Add this method.
	  }
}