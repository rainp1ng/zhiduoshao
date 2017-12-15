package com.activity.ladyclass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TeachActivity extends Activity{
	private Button exitButton;
	private int origin;
	protected void onCreate(Bundle savedInstance){
		super.onCreate(savedInstance);
		Intent intent=getIntent();
		origin=intent.getIntExtra("origin",0);
		//Log.e("origin",origin+"");
		showTeach();
	}
	void showTeach(){
		switch(origin){
		case 1:
			showMainTeach();
			break;
		case 2:
			showHelpTeach();
			break;
		//default:
			//finish();
			//break;
		}
	}
	void showMainTeach(){
		setContentView(R.layout.view_show_teach);
		exitButton=(Button)findViewById(R.id.exit_btn);
		exitButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	void showHelpTeach(){
		setContentView(R.layout.view_show_teach2);
		exitButton=(Button)findViewById(R.id.exit_btn);
		exitButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
}
