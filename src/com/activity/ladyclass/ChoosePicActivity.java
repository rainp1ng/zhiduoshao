package com.activity.ladyclass;

import com.activity.adapter.GridPicAdapter;
import com.util.method.ControlApplication;
import com.util.method.SldingFinishActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
/**笔记本封面的选择窗口，静态成员为选择的图片编号
 * @author 雨中树*/
public class ChoosePicActivity extends SldingFinishActivity{
	
	public static int newNotebookPic=101;
	
	private GridView gv;
	//private Button exitButton;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_pic);
		ControlApplication.getInstance().addActivity(this);
		findView();
		initView();
	}
	public void findView(){
		gv=(GridView)findViewById(R.id.grid_pic);
		GridPicAdapter adapter=new GridPicAdapter(ChoosePicActivity.this);
		gv.setAdapter(adapter);
	}
	public void initView(){
		gv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				newNotebookPic=position;
				finish();
			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.choose_pic, menu);
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_back) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
