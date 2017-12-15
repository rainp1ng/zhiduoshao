package com.activity.ladyclass;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.activity.adapter.ListContentAdapter;
import com.activity.database.GetInfoFromDatabase;
import com.activity.database.MyDatabaseHelper;
import com.util.method.ControlApplication;
import com.util.method.ItemContent;
import com.util.method.ItemInfo;
import com.util.method.ListContentView;
import com.util.method.SldingFinishActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import static com.activity.database.MyDatabaseHelper.*;
import static com.util.method.SlidingView.contentClickable;
/**知识内容的界面的页面，包括了答题进入的
 * 和查询历史记录进入看到的，由origin监听
 * @author 雨中树*/
public class StuActivity extends SldingFinishActivity{
	
	private Button exitButton;
	private TextView title;
	private TextView originTitle;
	private TextView showCount;
	private ListContentView lv;
	private MyDatabaseHelper myHelper;
	private List<ItemContent> contentInfo;
	private ListContentAdapter adapter;
	private String bookName;
	private String name;
	private int count=0;
	private int myPosition;
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_study_content);
		Intent intent=getIntent();
		name=intent.getStringExtra("origin");
		bookName=intent.getStringExtra(STUDY_TYPE);
		ControlApplication.getInstance().addActivity(this);
		findView();
		initView();
	}
	private void findView() {
		// TODO Auto-generated method stub
		exitButton=(Button)findViewById(R.id.back_btn);
		title=(TextView)findViewById(R.id.title);
		showCount=(TextView)findViewById(R.id.count);
		lv=(ListContentView)findViewById(R.id.list_study);
		myHelper=new MyDatabaseHelper(this,DATABASE_NAME,null,1);
		contentInfo=new ArrayList<ItemContent>();
		originTitle=(TextView)findViewById(R.id.name);
		getContentInfo();
		setContentInfo();
	}
	private void getContentInfo() {
		// TODO Auto-generated method stub
		GetInfoFromDatabase dbInfo=new GetInfoFromDatabase(myHelper);
		if(name.equals("答题")){
			contentInfo=dbInfo.getContentInfo(bookName);
		}else if(name.equals("更早")){
			contentInfo=dbInfo.getRestRecord(Integer.parseInt(bookName));
		}else if(name.equals("错题集")){
			contentInfo=dbInfo.getWrongSet();
		}else{
			contentInfo=dbInfo.getRecord(Integer.parseInt(bookName));
		}
		count=dbInfo.getCount();
	}
	private void setContentInfo() {
		// TODO Auto-generated method stub
		adapter=new ListContentAdapter(contentInfo,this);
		lv.setAdapter(adapter);
		if(name.equals("答题"))
			showCount.setText("共"+count+"条知识");
		else if(name.equals("错题集"))
			showCount.setText("共"+count+"条错题");
		else
			showCount.setText("共"+count+"条记录");
	}
	private void initView() {
		// TODO Auto-generated method stub
		originTitle.setText(name);
		if(name.equals("答题"))
			title.setText("("+bookName+")");
		else
			title.setText("");
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
		String _id;
		int date;
		int queue;
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			if(contentClickable){
				getCurrentTime();
				myPosition=position;
				_id=contentInfo.get(position).id;
				titleName=contentInfo.get(position).title;
				//queue排序，让已阅读过的知识会放到后面，未读的可以优先看到
				queue=(Integer.parseInt(contentInfo.get(position).queue)+1)%10;
				if(name.equals("答题")){
					updateContentTime();
				}
				toContent();
			}else{
				lv.shrinkListItem(position);
			}
		}
		private void getCurrentTime() {
			// TODO Auto-generated method stub
			SimpleDateFormat formatter=new SimpleDateFormat("yyyyMMdd");
			Date currentDate=new Date(System.currentTimeMillis());
			date=Integer.parseInt(formatter.format(currentDate));
			//Log.e("Date",date+"");
		}
		private void updateContentTime(){
			String str="UPDATE "+CONTENT_TABLE+" SET "+CONTENT_DATE+"="+date+","+CONTENT_QUEUE+"="+queue+" where "+CONTENT_ID+"="+_id;
			//Log.e("id",str);
			//Log.e("queue",queue+"");
			SQLiteDatabase db=myHelper.getWritableDatabase();
			db.execSQL(str);
		}
		private void toContent(){
			ItemInfo.itemInfo=contentInfo;
			Intent intent = new Intent();
			intent.setClass(StuActivity.this, ContentActivity.class);
			intent.putExtra("origin",name);
			intent.putExtra("position",myPosition);
			//Log.d("debg",titleName);
			StuActivity.this.startActivity(intent);
			StuActivity.this.overridePendingTransition(R.anim.slide_right_in,0);  
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.content, menu);
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if(id==R.id.action_exit){
			ControlApplication.getInstance().exit();
		}
		return super.onOptionsItemSelected(item);
	}
}
