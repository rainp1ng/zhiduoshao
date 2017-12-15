package com.activity.ladyclass;

import java.util.ArrayList;
import java.util.List;

import com.activity.adapter.ListNoteContentAdapter;
import com.activity.database.GetInfoFromDatabase;
import com.activity.database.MyDatabaseHelper;
import com.util.method.ControlApplication;
import com.util.method.ItemContent;
import com.util.method.ItemInfo;
import com.util.method.ListContentView;
import com.util.method.SldingFinishActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import static com.activity.database.MyDatabaseHelper.*;
import static com.util.method.SlidingView.contentClickable;
/**笔记本内容的界面
 * @author 雨中树*/
public class NoteActivity extends SldingFinishActivity{
	
	private Button exitButton;
	private TextView notebookName;
	private TextView showCount;
	private ListContentView lv;
	private MyDatabaseHelper myHelper;
	private List<ItemContent> contentItem;
	private ListNoteContentAdapter adapter;
	private String _id;
	private String name;
	private int count=0;
	private int myPosition;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_note_content);
		Intent intent=getIntent();
		name=intent.getStringExtra(NOTEBOOK_NAME);
		_id=intent.getStringExtra(NOTEBOOK_ID);
		ControlApplication.getInstance().addActivity(this);
		findView();
		initView();
	}
	private void findView() {
		// TODO Auto-generated method stub
		exitButton=(Button)findViewById(R.id.back_btn);
		showCount=(TextView)findViewById(R.id.count);
		lv=(ListContentView)findViewById(R.id.list_note);
		myHelper=new MyDatabaseHelper(this,DATABASE_NAME,null,1);
		contentItem=new ArrayList<ItemContent>();
		notebookName=(TextView)findViewById(R.id.name);
		initContentInfo();
	}
	public void initContentInfo(){
		getContentInfo();
		setContentInfo();
	}
	private void getContentInfo() {
		// TODO Auto-generated method stub
		GetInfoFromDatabase dbInfo=new GetInfoFromDatabase(myHelper);
		contentItem=dbInfo.getNotebookContent(_id);
		count=dbInfo.getCount();
	}
	private void setContentInfo() {
		// TODO Auto-generated method stub
		adapter=new ListNoteContentAdapter(contentItem,this,_id);
		lv.setAdapter(adapter);
		showCount.setText("共"+count+"条笔记");
	}
	private void initView() {
		// TODO Auto-generated method stub
		notebookName.setText(name);
		exitButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				toFinish();
			}
		});
		lv.setOnItemClickListener(new OnItemClickEvent());
	}
	public void toFinish(){
		finish();
		this.overridePendingTransition(0,R.anim.slide_right_out);  
	}
	class OnItemClickEvent implements OnItemClickListener{
		String titleName;
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			if(contentClickable){
				myPosition=position;
				toContent();
			}else{
				lv.shrinkListItem(position);
			}
		}
		private void toContent(){
			Intent intent = new Intent();
			intent.setClass(NoteActivity.this, ContentActivity.class);
			intent.putExtra("position",myPosition);
			intent.putExtra("origin",name);
			ItemInfo.itemInfo=contentItem;
			//Log.d("debg",titleName);
			NoteActivity.this.startActivity(intent);
			NoteActivity.this.overridePendingTransition(R.anim.slide_right_in,0);  
		}
	}
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.note_content, menu);
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_delete) {
			showDelete();
			return true;
		}else if(id==R.id.action_exit){
			ControlApplication.getInstance().exit();
		}
		return super.onOptionsItemSelected(item);
	}
	private void showDelete(){
		
	}
}
