package com.activity.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
/**数据库操作helper
 * @author 雨中树*/
public class MyDatabaseHelper extends SQLiteOpenHelper{
	public final static String DATABASE_NAME="lady_class";
	
	//记事本名字的表
	public final static String NOTEBOOK_TABLE="notebook";
	public final static String NOTEBOOK_ID="_id";
	public final static String NOTEBOOK_NAME="_name";
	public final static String NOTEBOOK_COUNT="_count";
	public final static String NOTEBOOK_PIC="_picture";
	public final static String NOTEBOOK_DEFAULT="_default";
	
	//学习类型的表
	public final static String STUDYTYPE_TABLE="study_type";
	public final static String STUDY_TYPE_ID="_id";
	public final static String STUDY_TYPE="_type";
	public final static String STUDY_TYPE_COUNT="_count";
	public final static String STUDY_TYPE_PIC="_picture";
	
	//学习内容的表
	public final static String CONTENT_TABLE="study_content";
	public final static String CONTENT_ID="_id";
	public final static String CONTENT_TITLE="_title";
	public final static String CONTENT_CONTENT="_content";
	public final static String CONTENT_ANSWER="_answer"; 
	public final static String CONTENT_BOOK="_book";
	public final static String CONTENT_QUEUE="_queue";
	public final static String CONTENT_DATE="_date";
	public final static String CONTENT_RIGHT="_right";
	public final static String CONTENT_FALSE="_false";
	
	public MyDatabaseHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		//笔记本
		db.execSQL("CREATE TABLE IF NOT EXISTS "+NOTEBOOK_TABLE+"("+
				NOTEBOOK_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
				NOTEBOOK_NAME+" VARCHAR,"+
				NOTEBOOK_COUNT+" INTEGER,"+
				NOTEBOOK_PIC+" INTEGER,"+
				NOTEBOOK_DEFAULT+" INTEGER)");
		//书
		db.execSQL("CREATE TABLE IF NOT EXISTS "+STUDYTYPE_TABLE+"("+
				STUDY_TYPE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
				STUDY_TYPE+" VARCHAR,"+
				STUDY_TYPE_COUNT+" INTEGER,"+
				STUDY_TYPE_PIC+" INTEGER)");
		//知识内容
		db.execSQL("CREATE TABLE IF NOT EXISTS "+CONTENT_TABLE+"("+
				CONTENT_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
				CONTENT_TITLE+" VARCHAR,"+
				CONTENT_CONTENT+" VARCHAR,"+
				CONTENT_ANSWER+" INTEGER,"+
				CONTENT_BOOK+" VARCHAR,"+
				CONTENT_QUEUE+" INTEGER,"+
				CONTENT_DATE+" INTEGER,"+
				CONTENT_RIGHT+" INTEGER,"+
				CONTENT_FALSE+" INTEGER)");
		//是否打开导航的值
		db.execSQL("CREATE TABLE IF NOT EXISTS need_help (need_help INTEGER)");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS test_statistics (_id INTEGER PRIMARY KEY AUTOINCREMENT,_date INTEGER,_right INTEGER,_false _INTEGER)");
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
