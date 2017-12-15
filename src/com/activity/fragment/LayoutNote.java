package com.activity.fragment;

import java.util.*;

import com.activity.adapter.GridItemAdapter;
import com.activity.database.GetInfoFromDatabase;
import com.activity.database.MyDatabaseHelper;
import com.activity.ladyclass.*;
import com.util.method.NoScrollGridView;

import android.app.AlertDialog.Builder;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import static com.activity.database.MyDatabaseHelper.*;
import static com.activity.ladyclass.ChoosePicActivity.newNotebookPic;
/**笔记本的页面
 * @author 雨中树*/
public class LayoutNote extends Fragment {
	//增加笔记本对话框的控件
	private Dialog addDialog; 
	private View linearLayout;
	private Button menuButton;
	private Button cancelAButton;
	private Button addButton;
	private Button chooseButton;
	//修改笔记本对话框的控件
	private Dialog changeDialog;
	private Dialog confirmDialog;
	private EditText notebookName;
	private Button changeButton;
	private Button defaultButton;
	private Button toChangeButton;
	private Button deleteButton;
	private Button cancelCButton;
	private String changeBookName;
	private String table;
	//主页面的控件
	private TextView showCount;
	private TextView showDefault;
	private MainActivity main;
	private NoScrollGridView gv;
	private LayoutInflater inflater;
	GetInfoFromDatabase dbInfo;
	private MyDatabaseHelper myHelper;
	private GridItemAdapter adapter;
	private List<HashMap<String,String>> notebookInfo;
	private Dialog tipDialog;
	private int count=0;
	private String bookName;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_note, null);
		findView(view);
		initView(view);
		return view;
	}
	
	public void findView(View view){
		menuButton=(Button)view.findViewById(R.id.menu_btn);
		showCount=(TextView)view.findViewById(R.id.count);
		showDefault=(TextView)view.findViewById(R.id.default_book);
		main=(MainActivity)LayoutNote.this.getActivity();
		gv=(NoScrollGridView)view.findViewById(R.id.grid_note);
		myHelper=new MyDatabaseHelper(main,DATABASE_NAME,null,1);
		notebookInfo=new ArrayList<HashMap<String,String>>();
		inflater=LayoutInflater.from(main);
		//初始化笔记本情况
		getNotebookInfo();
		setNotebookInfo();
	}
	
	private void setNotebookInfo() {
		// TODO Auto-generated method stub
		adapter=new GridItemAdapter(notebookInfo,main);
		gv.setAdapter(adapter);
		showCount.setText("共"+count+"本笔记本");
		getDefaultBook();
	}
	//查找有无默认笔记本
	private void getDefaultBook(){
		for(int i=0;i<notebookInfo.size();i++){
			int j=Integer.parseInt(notebookInfo.get(i).get(NOTEBOOK_DEFAULT));
			if(j!=0){
				bookName=notebookInfo.get(i).get(NOTEBOOK_NAME);
				break;
			}
		}
		if(bookName!=null)
			showDefault.setText("默认笔记本:《"+bookName+"》");
		else 
			showDefault.setText("未设置默认笔记本");
	}
	private void getNotebookInfo() {
		// TODO Auto-generated method stub
		dbInfo=new GetInfoFromDatabase(myHelper);
		notebookInfo=dbInfo.getNotebookInfo();
		count=dbInfo.getCount();
	}
	public void initView(View view){
		menuButton.setOnClickListener(new OnClickButtonEvent());
		gv.setOnItemClickListener(new OnClickItemEvent());
		gv.setOnItemLongClickListener(new OnItemLongClickEvent());
	}
	public void setText(String text){
		showCount.setText(text);
	}
	//打开一本笔记本或增加一本笔记本
	class OnClickItemEvent implements OnItemClickListener{
			
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// 添加笔记本
			if(position>=notebookInfo.size()){
				findView();
				addDialog=new Dialog(main,R.style.dialog_);
				addDialog.setContentView(linearLayout);
				initView();
				addDialog.show();
			}else{
				Intent intent = new Intent();
				intent.setClass(main, NoteActivity.class);
				intent.putExtra(NOTEBOOK_NAME,notebookInfo.get(position).get(NOTEBOOK_NAME));
				intent.putExtra(NOTEBOOK_ID,notebookInfo.get(position).get(NOTEBOOK_ID));
				//Log.e("传了什么",notebookInfo.get(position).get(NOTEBOOK_NAME)+notebookInfo.get(position).get(NOTEBOOK_ID));
				main.startActivity(intent);
				main.overridePendingTransition(R.anim.slide_right_in,0);  
			}
		}
		void findView(){
			linearLayout=inflater.inflate(R.layout.dialog_add_notebook,null);
			cancelAButton=(Button)linearLayout.findViewById(R.id.cancel_btn);
			addButton=(Button)linearLayout.findViewById(R.id.add_btn);
			chooseButton=(Button)linearLayout.findViewById(R.id.choose_btn);
		}
		void initView(){
			cancelAButton.setOnClickListener(new OnClickButtonEvent());
			addButton.setOnClickListener(new OnClickButtonEvent());
			chooseButton.setOnClickListener(new OnClickButtonEvent());
		}
	}
	//长按笔记本可修改笔记本信息
	class OnItemLongClickEvent implements OnItemLongClickListener{
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			if(position>=notebookInfo.size())
				return false;
			else 
				table="notebook_"+notebookInfo.get(position).get(NOTEBOOK_ID);
				changeBookName=notebookInfo.get(position).get(NOTEBOOK_NAME);
				ChoosePicActivity.newNotebookPic=Integer.parseInt(notebookInfo.get(position).get(NOTEBOOK_PIC));
				openChangeDialog();
				return false;
		}	
		void openChangeDialog(){
			findDialogView();
			notebookName.setText(changeBookName);
			changeDialog=new Dialog(main,R.style.dialog_);
			changeDialog.setContentView(linearLayout);
			initDialogView();
			changeDialog.show();
		}
		void findDialogView(){
			linearLayout=(LinearLayout)inflater.inflate(R.layout.dialog_change_notebook,null);
			notebookName=(EditText)linearLayout.findViewById(R.id.notebook_name);
			changeButton=(Button)linearLayout.findViewById(R.id.change_btn);
			defaultButton=(Button)linearLayout.findViewById(R.id.default_btn);
			toChangeButton=(Button)linearLayout.findViewById(R.id.to_change_btn);
			cancelCButton=(Button)linearLayout.findViewById(R.id.cancel_btn);
			deleteButton=(Button)linearLayout.findViewById(R.id.delete_btn);
		}
		void initDialogView(){
			changeButton.setOnClickListener(new OnClickButtonEvent());
			defaultButton.setOnClickListener(new OnClickButtonEvent());
			toChangeButton.setOnClickListener(new OnClickButtonEvent());
			cancelCButton.setOnClickListener(new OnClickButtonEvent());
			deleteButton.setOnClickListener(new OnClickButtonEvent());
		}
	}
	//各种按键的点击事件
	class OnClickButtonEvent implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v==menuButton){
				main.showView_MenuToogle();
			}else if(v==cancelAButton){
				newNotebookPic=101;
				addDialog.dismiss();
			}else if(v==cancelCButton){
				newNotebookPic=101;
				changeDialog.dismiss();
			}else if(v==addButton){
				String notebookName=((TextView)linearLayout.findViewById(R.id.notebook_name)).getText().toString();
				if(!notebookName.equals("")){
					addNotebook(notebookName);
					addDialog.dismiss();
					Toast toast = Toast.makeText(main, "笔记本添加成功", Toast.LENGTH_SHORT); 
					toast.show();
				}else{
					showFailedDialog();
				}
			}else if(v==chooseButton||v==changeButton){
				//choosePic();
				Intent intent=new Intent();
				intent.setClass(main,ChoosePicActivity.class);
				main.startActivity(intent);
				main.overridePendingTransition(R.anim.slide_right_in,0);  
			}else if(v==defaultButton){
				changeDefaultBook();
				getNotebookInfo();
				getDefaultBook();
				Toast toast = Toast.makeText(main, "默认笔记本已修改为《"+changeBookName+"》", Toast.LENGTH_SHORT); 
				toast.show();
			}else if(v==toChangeButton){
				String changedBookName=notebookName.getText().toString();
				if(!changedBookName.equals("")){
					changeNotebook(changedBookName);
					changeDialog.dismiss();
					Toast toast = Toast.makeText(main, "笔记本信息修改成功", Toast.LENGTH_SHORT); 
					toast.show();
				}else{
					showFailedDialog();
				}
			}else if(v==deleteButton){
				showConfirmDialog();
			}
		}
		//添加笔记本或修改笔记本名失败的提示框
		private void showFailedDialog(){
			tipDialog=null;
			Builder builder=new AlertDialog.Builder(main);
			builder.setTitle("错误");
			builder.setMessage("笔记本名不能为空！");
			builder.setNegativeButton("确定", null);
			tipDialog=builder.create();
			tipDialog.show();
		}
		//确认是否删除笔记本的对话框
		private void showConfirmDialog(){
			confirmDialog = null;
			Builder builder=new AlertDialog.Builder(main);
			builder.setTitle("删除笔记本");
			builder.setMessage("是否删除笔记本《"+changeBookName+"》？");

			builder.setPositiveButton("删除",new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					deleteNotebook();
					changeDialog.dismiss();
					refresh();
					Toast toast = Toast.makeText(main, "笔记本《"+changeBookName+"》删除成功", Toast.LENGTH_SHORT); 
					toast.show();
				}
			}).setNegativeButton("取消", null);
			confirmDialog=builder.create();
			confirmDialog.show();
		}
		//将笔记本对应的表及信息从数据库中删除
		private void deleteNotebook(){
			SQLiteDatabase db=myHelper.getWritableDatabase();
			db.delete(NOTEBOOK_TABLE,NOTEBOOK_NAME+"=?", new String[]{changeBookName});
			String str="DROP TABLE "+table;
			db.execSQL(str);
		}
		private void changeNotebook(String changedBookName){
			String str="UPDATE "+NOTEBOOK_TABLE+" SET "+NOTEBOOK_NAME+"='"+changedBookName+"',"+NOTEBOOK_PIC+"="+newNotebookPic+" where "+NOTEBOOK_NAME+"='"+changeBookName+"'";
			SQLiteDatabase db=myHelper.getWritableDatabase();
			db.execSQL(str);
			refresh();
			newNotebookPic=101;
		}
		private void changeDefaultBook(){
			cancelDefault(bookName);
			setDefault(changeBookName);
		}
		private void cancelDefault(String bookName){
			String str="UPDATE "+NOTEBOOK_TABLE+" SET "+NOTEBOOK_DEFAULT+"=0 where "+NOTEBOOK_NAME+"='"+bookName+"'";
			SQLiteDatabase db=myHelper.getWritableDatabase();
			db.execSQL(str);
		}
		private void setDefault(String changeBookName){
			String str="UPDATE "+NOTEBOOK_TABLE+" SET "+NOTEBOOK_DEFAULT+"=1 where "+NOTEBOOK_NAME+"='"+changeBookName+"'";
			SQLiteDatabase db=myHelper.getWritableDatabase();
			db.execSQL(str);
		}
		private void addNotebook(String notebookName) {
			// TODO Auto-generated method stub
			insertIntoTable(notebookName);
			refresh();
			newNotebookPic=101;
		}
	}
	public void insertIntoTable(String notebookName){
		//将笔记本名和id登记
		SQLiteDatabase db=myHelper.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put(NOTEBOOK_NAME, notebookName);
		values.put(NOTEBOOK_COUNT, 0);
		values.put(NOTEBOOK_PIC,ChoosePicActivity.newNotebookPic);
		values.put(NOTEBOOK_DEFAULT,0);
		//long count = 
		db.insert(NOTEBOOK_TABLE,NOTEBOOK_ID,values);
		
		//创建该笔记本的内容表
		String _id="notebook_"+dbInfo.getNotebookID(notebookName);
		Log.e("debuggg",_id);
		db.execSQL("CREATE TABLE IF NOT EXISTS "+_id+"("+
				"_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
				"_title VARCHAR)");
		
		db.close();
	}
	public void refresh(){
		getNotebookInfo();
		setNotebookInfo();
	}
}
