package com.activity.ladyclass;

import com.activity.database.GetInfoFromDatabase;
import com.activity.database.InitialDatabase;
import com.activity.database.MyDatabaseHelper;
import com.google.analytics.tracking.android.EasyTracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import static com.activity.database.MyDatabaseHelper.DATABASE_NAME;
/**打开应用时出现的界面
 * @author 雨中树*/
public class LaunchActivity extends Activity{
	private MyDatabaseHelper myHelper;
	private GetInfoFromDatabase dbInfo;

	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		InitDatabase();
		myHelper=new MyDatabaseHelper(LaunchActivity.this,DATABASE_NAME,null,1);
		dbInfo=new GetInfoFromDatabase(myHelper);
		NavigationActivity.needHelp=dbInfo.getNeedHelp();
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		setContentView(R.layout.view_launcher);
		delayToMain();
	}
	private void InitDatabase() {
		// TODO Auto-generated method stub
		if(!InitialDatabase.IS_INITIALIZED)
		InitialDatabase.init(this);
	}
	private void delayToMain(){
		// TODO Auto-generated method stub
		new Handler().postDelayed(new Runnable(){ 
			public void run(){ 
				if(NavigationActivity.needHelp)
					startActivity(new Intent(LaunchActivity.this,NavigationActivity.class)); 
				else
					startMain();
					finish();
			} 
		},2000);
	}
	@Override
	  public void onStart() {
	    super.onStart();
	     // The rest of your onStart() code.
	    EasyTracker.getInstance(this).activityStart(this);  // Add this method.
	  }
	  public void startMain(){
		  	startActivity(new Intent(LaunchActivity.this,MainActivity.class));
			overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
			LaunchActivity.this.finish();
	  }
	  @Override
	  public void onStop() {
	    super.onStop();
	     // The rest of your onStop() code.
	    EasyTracker.getInstance(this).activityStop(this);  // Add this method.
	  }
}
