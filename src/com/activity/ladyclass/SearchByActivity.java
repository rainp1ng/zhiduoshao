package com.activity.ladyclass;

import java.util.ArrayList;
import java.util.List;

import com.activity.adapter.ListContentAdapter;
import com.activity.database.GetInfoFromDatabase;
import com.activity.database.MyDatabaseHelper;
import com.util.method.ControlApplication;
import com.util.method.ItemContent;
import com.util.method.ItemInfo;
import com.util.method.ListContentView;
import com.util.method.SldingFinishActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import static com.activity.database.MyDatabaseHelper.*;
import static com.util.method.SlidingView.contentClickable;
/**用来搜索知识库内容的页面，在上一个activity中可
 * 选择搜索内容关键词或搜索题目（标题）关键字
 * @author 雨中树
 * */
public class SearchByActivity extends SldingFinishActivity{
	private EditText strText;
	private Button searchButton;
	private Button backButton;
	private String str;
	private boolean byContent;
	private MyDatabaseHelper myHelper;
	private TextView count;
	private ListContentView lv;
	private ListContentAdapter adapter;
	private List<ItemContent> searchInfo;
	private Toast toast;
	private ProgressDialog searchDialog;
	private Handler handler;
	private String _id;
	private int myPosition;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_search_by);
		Intent intent=getIntent();
		byContent=intent.getBooleanExtra("model", false);
		ControlApplication.getInstance().addActivity(this);
		findView();
		initView();
	}

	private void findView() {
		// TODO Auto-generated method stub
		searchButton=(Button)findViewById(R.id.search_btn);
		strText=(EditText)findViewById(R.id.search);
		backButton=(Button)findViewById(R.id.back_btn);
		lv=(ListContentView)findViewById(R.id.list_search);
		count=(TextView)findViewById(R.id.count);
		setHandler();
	}
	private void setHandler() {
		// TODO Auto-generated method stub
		handler=new Handler(){
			public void handleMessage(Message msg){
				super.handleMessage(msg);
				//只要执行到这里就关闭对话框
				searchDialog.dismiss();
			}
		};
	}

	private void initView() {
		// TODO Auto-generated method stub
		backButton.setOnClickListener(new OnClickEvent());
		searchButton.setOnClickListener(new OnClickEvent());
		myHelper=new MyDatabaseHelper(this,DATABASE_NAME,null,1);
		lv.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(contentClickable){
					_id=searchInfo.get(position).id;
					myPosition=position;
					toContent();
				}else{
					lv.shrinkListItem(position);
				}
			}
		});
		setHint();
	}
	private void setHint(){
		if(byContent)
			strText.setHint("请输入内容关键词");
		else
			strText.setHint("请输入题目关键词");
	}
	private void toContent(){
		Intent intent = new Intent();
		intent.setClass(SearchByActivity.this, ContentActivity.class);
		intent.putExtra(CONTENT_ID,_id);
		intent.putExtra("position",myPosition);
		intent.putExtra("origin","搜索");
		ItemInfo.itemInfo=searchInfo;
		startActivity(intent);
	}
	public void toFinish(){
		finish();
		this.overridePendingTransition(0,R.anim.slide_right_out);  
	}
	class OnClickEvent implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v==backButton){
				toFinish();
			}else if(v==searchButton){
				searchThread();
			}
		}
		private void searchThread() {
			// TODO Auto-generated method stub
			searchDialog= ProgressDialog.show(SearchByActivity.this, "搜索", "正在搜索...");
		    new Thread(){
		    	public void run(){
		    		cloaseKeyBoard();
		    		handler.sendEmptyMessage(0);
		    	}
		    }.start();	
		    str=strText.getText().toString();
		    getSearchInfo();
    		adapter=new ListContentAdapter(searchInfo,SearchByActivity.this);
		    setSearchInfo();
		}
		private void cloaseKeyBoard(){
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
			imm.hideSoftInputFromWindow(strText.getWindowToken(), 0);
		}
		private void setSearchInfo() {
			// TODO Auto-generated method stub
			if(searchInfo.size()!=0){
				count.setText("共找到"+searchInfo.size()+"条记录");
			}else{
				toast = Toast.makeText(SearchByActivity.this,"没有找到查找的关键词，请简略查找！", Toast.LENGTH_SHORT); 
				toast.show();
			}
			lv.setAdapter(adapter);
		}
		private void getSearchInfo(){
			searchInfo=new ArrayList<ItemContent>();
			GetInfoFromDatabase dbInfo=new GetInfoFromDatabase(myHelper);
			if(byContent){
				searchInfo=dbInfo.getSearchContentInfo(str);
			}else{
				searchInfo=dbInfo.getSearchTitleInfo(str);
			}
			//Log.e("debuggggg",searchInfo.get(0).get(CONTENT_TITLE));
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
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
