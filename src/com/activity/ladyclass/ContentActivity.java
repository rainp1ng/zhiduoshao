package com.activity.ladyclass;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.activity.database.GetInfoFromDatabase;
import com.activity.database.MyDatabaseHelper;
import com.util.method.ControlApplication;
import com.util.method.ItemContent;
import com.util.method.ItemInfo;
import com.util.method.SldingFinishActivity;

import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import static com.activity.database.MyDatabaseHelper.*;
/**显示题目内容的页面
 * @author 雨中树*/
public class ContentActivity extends SldingFinishActivity{
	public static ContentActivity contentActivity;
	//显示答案的对话框
	private Dialog answerTip;
	private LinearLayout linearLayout;
	private LayoutInflater inflater;
	private Button toSeeButton;
	private TextView tip1;
	private TextView tip2;
	private ImageView tipView;
	//主页面控件
	private Button exitButton;
	private Button trueButton;
	private Button falseButton;
	private TextView contentTitle;
	private List<ItemContent> titleInfo;
	private String content_title;
	private GetInfoFromDatabase dbInfo;
	private HashMap<String,String> answerInfo;
	private MyDatabaseHelper myHelper;
	private int answer;
	private int myPosition;
	private String content;
	private String origin;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_content_content);
		titleInfo=ItemInfo.itemInfo;
		Intent intent=getIntent();
		origin=intent.getStringExtra("origin");
		myPosition=intent.getIntExtra("position",0);
		content_title=titleInfo.get(myPosition).title;
		ControlApplication.getInstance().addActivity(this);
		findView();
		initView();
		contentActivity=this;
	}
	private void findView() {
		// TODO Auto-generated method stub
		exitButton=(Button)findViewById(R.id.back_btn);
		trueButton=(Button)findViewById(R.id.true_btn);
		falseButton=(Button)findViewById(R.id.false_btn);
		contentTitle=(TextView)findViewById(R.id.content_title);
		myHelper=new MyDatabaseHelper(ContentActivity.this,DATABASE_NAME,null,1);
		getContentInfo();
		setContentInfo();
	}
	private void getContentInfo(){
		dbInfo=new GetInfoFromDatabase(myHelper);
		answerInfo=dbInfo.getAnswerInfo(content_title);
		answer=Integer.parseInt(answerInfo.get(CONTENT_ANSWER));
		Log.e("answer",answer+"");
		content=answerInfo.get(CONTENT_CONTENT);
		Log.e(CONTENT_ANSWER,answer+"");
		Log.e(CONTENT_CONTENT,content);
	}
	private void setContentInfo(){
		contentTitle.setText(content_title);
	}
	private void initView() {
		// TODO Auto-generated method stub
		exitButton.setOnClickListener(new OnClickEvent());
		trueButton.setOnClickListener(new OnClickEvent());
		falseButton.setOnClickListener(new OnClickEvent());
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
			}else if(v==trueButton){
				checkAnswer(1);
			}else if(v==falseButton){
				checkAnswer(0);
			}else if(v==toSeeButton){
				watchRightAnswer();
				answerTip.dismiss();
			}
		}
		void checkAnswer(int a){
			setADialog();
			String date=getCurrentTime();
			if(a==answer){
				if(origin.equals("答题")){
					addOnePoint(date);
				}
				toastRightTip();
			}else{
				if(origin.equals("答题")){
					subOnePoint(date);
				}
				toastWrongTip();
			}
			answerTip.show();
		}
		private void subOnePoint(String date) {
			// TODO Auto-generated method stub
			int wrong=dbInfo.getTodayWrong()+1;
			SQLiteDatabase db=myHelper.getWritableDatabase();
			String str="UPDATE test_statistics SET _false="+wrong+" where _date="+date;
			db.execSQL(str);
			wrong=dbInfo.getContentRight(content_title)+1;
			str="UPDATE "+CONTENT_TABLE+" SET "+CONTENT_FALSE+"="+wrong+" where "+CONTENT_TITLE+"='"+content_title+"'";
			db.execSQL(str);
		}
		private void addOnePoint(String date) {
			// TODO Auto-generated method stub
			int right=dbInfo.getTodayRight()+1;
			SQLiteDatabase db=myHelper.getWritableDatabase();
			String str="UPDATE test_statistics SET _right="+right+" where _date="+date;
			db.execSQL(str);
			right=dbInfo.getContentRight(content_title)+1;
			str="UPDATE "+CONTENT_TABLE+" SET "+CONTENT_RIGHT+"="+right+" where "+CONTENT_TITLE+"='"+content_title+"'";
			db.execSQL(str);
		}
		private String getCurrentTime() {
			// TODO Auto-generated method stub
			SimpleDateFormat formatter=new SimpleDateFormat("yyyyMMdd");
			Date currentDate=new Date(System.currentTimeMillis());
			return formatter.format(currentDate);
		}
		private void setADialog() {
			// TODO Auto-generated method stub
			findTipView();
			answerTip=new Dialog(ContentActivity.this,R.style.dialog_);
			answerTip.setContentView(linearLayout);
			initTipView();
		}
		private void findTipView() {
			// TODO Auto-generated method stub
			inflater=LayoutInflater.from(ContentActivity.this);
			//gifView=(GifView)findViewById(R.id.gif_view);
			linearLayout=(LinearLayout)inflater.inflate(R.layout.dialog_answer,null);
			toSeeButton=(Button)linearLayout.findViewById(R.id.to_see_btn);
			tip1=(TextView)linearLayout.findViewById(R.id.tip1);
			tip2=(TextView)linearLayout.findViewById(R.id.tip2);
			tipView=(ImageView)linearLayout.findViewById(R.id.image_view);
		}
		private void initTipView() {
			// TODO Auto-generated method stub
			toSeeButton.setOnClickListener(new OnClickEvent());
		}
		private void toastRightTip() {
			// TODO Auto-generated method stub
			//gifView.setGifImage(R.drawable.congratulate_or_frame);
			//gifView.setShowDimension(150, 50);  
			//gifView.setGifImageType(GifImageType.COVER);
			tip1.setText("真厉害！...");
			tip2.setText("回答正确！看下解释和你想得是不是一样吧！");
			tipView.setImageResource(R.drawable.tip_right);
		}
		private void toastWrongTip() {
			// TODO Auto-generated method stub
			tip1.setText("很遗憾...");
			tip2.setText("你答错了~快点去看下到底是为什么错了吧！");
		}
		public void watchRightAnswer(){
			Intent intent=new Intent();
			intent.putExtra(CONTENT_TITLE,content_title);
			intent.putExtra(CONTENT_CONTENT,content);
			intent.putExtra("origin",origin);
			intent.putExtra("position",myPosition);
			intent.setClass(ContentActivity.this,AnswerActivity.class);
			ContentActivity.this.startActivity(intent);
			ContentActivity.this.overridePendingTransition(R.anim.slide_right_in,0);  
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.study_content, menu);
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
