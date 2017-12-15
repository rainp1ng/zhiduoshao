package com.activity.ladyclass;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.activity.database.GetInfoFromDatabase;
import com.activity.database.MyDatabaseHelper;
import com.util.method.ControlApplication;
import com.util.method.ItemContent;
import com.util.method.ItemInfo;
import com.util.method.SldingFinishActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import static com.activity.database.MyDatabaseHelper.*;
/**显示上一层活动的题目对应的答案，以及收藏
 * 笔记至默认的笔记本中
 * @author 雨中树
 * */
public class AnswerActivity extends SldingFinishActivity{
	private Button exitButton;
	private Button noteButton;
	private Button nextButton;
	private TextView contentAnswer;
	private String contentText;
	private String contentTitle;
	private MyDatabaseHelper myHelper;
	private GetInfoFromDatabase dbInfo;
	private SQLiteDatabase db;
	private List<ItemContent> noteContentItem;
	private Dialog tipDialog;
	private int myPosition;
	private int date;
	private String origin;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_answer);
		Intent intent=getIntent();
		myPosition=intent.getIntExtra("position",0);
		contentTitle=intent.getStringExtra(CONTENT_TITLE);
		contentText=intent.getStringExtra(CONTENT_CONTENT);
		origin=intent.getStringExtra("origin");
		ControlApplication.getInstance().addActivity(this);
		findView();
		initView();
	}
	private void findView() {
		// TODO Auto-generated method stub
		exitButton=(Button)findViewById(R.id.exit_btn);
		noteButton=(Button)findViewById(R.id.note_btn);
		nextButton=(Button)findViewById(R.id.next_btn);
		contentAnswer=(TextView)findViewById(R.id.content_answer);
		myHelper=new MyDatabaseHelper(this,DATABASE_NAME,null,1);
	}
	private void initView() {
		// TODO Auto-generated method stub
		exitButton.setOnClickListener(new OnClickEvent());
		noteButton.setOnClickListener(new OnClickEvent());
		nextButton.setOnClickListener(new OnClickEvent());
		contentAnswer.setText(contentText);
	}
	public void toFinish(){
		finish();
		this.overridePendingTransition(0,R.anim.slide_right_out);  
	}
	class OnClickEvent implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v==exitButton){
				toFinish();
			}else if(v==noteButton){
				addNote();
			}else if(v==nextButton){
				if(myPosition+1<ItemInfo.itemInfo.size()){
					ContentActivity.contentActivity.finish();
					Intent intent=new Intent();
					if(origin!=null&&origin.equals("答题")){
						getCurrentTime();
						updateContentTime();
					}
					intent.putExtra("origin",origin);
					intent.putExtra("position",myPosition+1);
					intent.setClass(AnswerActivity.this,ContentActivity.class);
					AnswerActivity.this.startActivity(intent);
					AnswerActivity.this.overridePendingTransition(R.anim.slide_right_in,0);  
					finish();
				}else{
					Toast toast = Toast.makeText(AnswerActivity.this, "已经是最后一题了~", Toast.LENGTH_SHORT); 
					toast.show();
				}
			}
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
		String str="UPDATE "+CONTENT_TABLE+" SET "+CONTENT_DATE+"="+date+","+CONTENT_QUEUE+"="+((Integer.parseInt(ItemInfo.itemInfo.get(myPosition+1).queue)+1)%10)+" where "+CONTENT_ID+"="+ItemInfo.itemInfo.get(myPosition+1).id;
		//Log.e("id",str);
		//Log.e("queue",queue+"");
		SQLiteDatabase db=myHelper.getWritableDatabase();
		db.execSQL(str);
	}
	void addNote(){
		String _id=DefaultNoteID();
		if(_id!=null){
			putIntoTable(_id);
		}else{
			showFailedTip1();
		}
	}
	void showFailedTip1(){
		tipDialog=null;
		Builder builder=new AlertDialog.Builder(AnswerActivity.this);
		builder.setTitle("收藏失败");
		builder.setMessage("未设置默认笔记本！请在菜单  ->笔记中选择并设置默认笔记本！");
		builder.setNegativeButton("确定", null);
		tipDialog=builder.create();
		tipDialog.show();
	}
	void putIntoTable(String _id){
		if(checkContent(_id)){
			String table="notebook_"+_id;
			db=myHelper.getWritableDatabase();
			ContentValues values=new ContentValues();
			values.put("_title",contentTitle);
			db.insert(table,"_id",values);
			Toast toast = Toast.makeText(AnswerActivity.this, "笔记收藏成功！", Toast.LENGTH_SHORT); 
			toast.show();
		}else{
			showFailedTip2();
		}
	}
	void showFailedTip2(){
		tipDialog=null;
		Builder builder=new AlertDialog.Builder(AnswerActivity.this);
		builder.setTitle("收藏失败");
		builder.setMessage("笔记本已收藏该条知识！");
		builder.setNegativeButton("确定", null);
		tipDialog=builder.create();
		tipDialog.show();
	}
	boolean checkContent(String _id){
		boolean notSame=true;
		dbInfo=new GetInfoFromDatabase(myHelper);
		noteContentItem=new ArrayList<ItemContent>();
		noteContentItem=dbInfo.getNotebookContent(_id);
		for(int i=0;i<noteContentItem.size();i++){
			if(contentTitle.equals(noteContentItem.get(i).title)){
				notSame=false;
				break;
			}
		}
		return notSame;
	}
	private String DefaultNoteID(){
		dbInfo=new GetInfoFromDatabase(myHelper);
		List<HashMap<String,String>> notebookInfo=new ArrayList<HashMap<String,String>>();
		notebookInfo=dbInfo.getNotebookInfo();
		String bookID=null;
		for(int i=0;i<notebookInfo.size();i++){
			int j=Integer.parseInt(notebookInfo.get(i).get(NOTEBOOK_DEFAULT));
			if(j!=0){
				bookID=notebookInfo.get(i).get(NOTEBOOK_ID);
				break;
			}
		}
		return bookID;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.answer, menu);
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_note) {
			addNote();
			return true;
		}else if(id==R.id.action_exit){
			ControlApplication.getInstance().exit();
		}
		return super.onOptionsItemSelected(item);
	}
}

