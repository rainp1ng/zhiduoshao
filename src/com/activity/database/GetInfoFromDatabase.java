package com.activity.database;

import java.text.SimpleDateFormat;
import java.util.*;

import com.util.method.ItemContent;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import static com.activity.database.MyDatabaseHelper.*;
/**从数据库中获取各种数据信息
 * @author 雨中树
 * */
public class GetInfoFromDatabase {
	private MyDatabaseHelper myHelper;
	private SQLiteDatabase db;
	private Cursor cursor;
	private int count;
	private String date;
	
	public GetInfoFromDatabase(MyDatabaseHelper myHelper){
		this.myHelper=myHelper;
	}
	
	public void openDatabase(){
		db=myHelper.getWritableDatabase();
	}
	
	public void closeDatabase(){
		if(db.isOpen())
			db.close();
		if(!cursor.isClosed())
			cursor.close();
	}
	//获取笔记本的内容(收藏的知识)
	public List<HashMap<String,String>> getNotebookInfo(){
		List<HashMap<String,String>> notebookInfo= new ArrayList<HashMap<String,String>>();
		HashMap<String,String> info;
		openDatabase();
		cursor=db.query(NOTEBOOK_TABLE, new String[]{NOTEBOOK_ID,NOTEBOOK_NAME,NOTEBOOK_COUNT,NOTEBOOK_PIC,NOTEBOOK_DEFAULT}, null, null, null, null, NOTEBOOK_ID);
	
		count = cursor.getCount();
		
		while(cursor.moveToNext()){
			info=new HashMap<String,String>();
			info.put(NOTEBOOK_ID,cursor.getString(0));
			info.put(NOTEBOOK_NAME,cursor.getString(1));
			info.put(NOTEBOOK_COUNT,cursor.getString(2));
			info.put(NOTEBOOK_PIC,cursor.getString(3));
			info.put(NOTEBOOK_DEFAULT,cursor.getString(4));
			notebookInfo.add(info);
		}
		closeDatabase();
		return notebookInfo;
	}
	//获得单个笔记本的id
	public String getNotebookID(String notebookName){
		openDatabase();
		cursor=db.query(NOTEBOOK_TABLE,new String[]{NOTEBOOK_ID},NOTEBOOK_NAME+" LIKE ?",new String[]{notebookName},null,null,null);
		cursor.moveToFirst();
		String notebook_id=cursor.getString(0);
		//closeDatabase();
		
		return notebook_id;
	}
	
	//获取书的内容（即知识类型的部分）
	public List<HashMap<String,String>> getBookInfo(){
		List<HashMap<String,String>> bookInfo= new ArrayList<HashMap<String,String>>();
		HashMap<String,String> info;
		openDatabase();
		cursor=db.query(STUDYTYPE_TABLE, new String[]{STUDY_TYPE_ID,STUDY_TYPE,STUDY_TYPE_COUNT,STUDY_TYPE_PIC}, null, null, null, null, STUDY_TYPE_ID);
		
		count = cursor.getCount();
		
		while(cursor.moveToNext()){
			info=new HashMap<String,String>();
			info.put(STUDY_TYPE_ID,cursor.getString(0));
			info.put(STUDY_TYPE,cursor.getString(1));
			info.put(STUDY_TYPE_COUNT,cursor.getString(2));
			info.put(STUDY_TYPE_PIC,cursor.getString(3));
			bookInfo.add(info);
		}
		closeDatabase();
		return bookInfo;
	}
	//读取所在书的知识库
	public List<ItemContent> getContentInfo(String book){
		List<ItemContent> contentInfo= new ArrayList<ItemContent>();
		ItemContent item;
		openDatabase();
		cursor=db.query(CONTENT_TABLE, new String[]{CONTENT_ID,CONTENT_TITLE,CONTENT_QUEUE},CONTENT_BOOK+" like ?", new String[]{book}, null, null, CONTENT_QUEUE+" ASC");
		
		count = cursor.getCount();
		
		while(cursor.moveToNext()){
			item=new ItemContent();
			item.id=cursor.getString(0);
			item.title=cursor.getString(1);
			item.queue=cursor.getString(2);
			contentInfo.add(item);
		}
		closeDatabase();
		return contentInfo;
	}
	//获得单条知识的内容
	public HashMap<String,String> getAnswerInfo(String title){
		HashMap<String,String> answerInfo;
		openDatabase();
		cursor=db.query(CONTENT_TABLE, new String[]{CONTENT_ANSWER,CONTENT_CONTENT},CONTENT_TITLE+" like ?", new String[]{title}, null, null, null);
		cursor.moveToFirst();
		answerInfo=new HashMap<String,String>();
		answerInfo.put(CONTENT_ANSWER,cursor.getString(0));
		answerInfo.put(CONTENT_CONTENT,cursor.getString(1));
		
		closeDatabase();
		return answerInfo;
	}
	//搜索内容关键词的结果
	public List<ItemContent> getSearchContentInfo(String content){
		List<ItemContent> searchInfo=new ArrayList<ItemContent>();
		ItemContent item;
		openDatabase();
		
		cursor=db.query(CONTENT_TABLE, new String[]{CONTENT_ID,CONTENT_TITLE,CONTENT_QUEUE},CONTENT_CONTENT+" like ?", new String[]{"%"+content+"%"}, null, null, CONTENT_QUEUE+" ASC");
		count = cursor.getCount();
		
		while(cursor.moveToNext()){
			item=new ItemContent();
			item.id=cursor.getString(0);
			item.title=cursor.getString(1);
			item.queue=cursor.getString(2);
			searchInfo.add(item);
		}
		closeDatabase();
		return searchInfo;
	}
	//搜索题目关键词的结果
	public List<ItemContent> getSearchTitleInfo(String title){
		List<ItemContent> searchInfo=new ArrayList<ItemContent>();
		ItemContent item;
		openDatabase();
		
		cursor=db.query(CONTENT_TABLE, new String[]{CONTENT_ID,CONTENT_TITLE,CONTENT_QUEUE},CONTENT_TITLE+" like ?", new String[]{"%"+title+"%"}, null, null, CONTENT_QUEUE+" ASC");
		count = cursor.getCount();
		
		while(cursor.moveToNext()){
			item=new ItemContent();
			item.id=cursor.getString(0);
			item.title=cursor.getString(1);
			item.queue=cursor.getString(2);
			searchInfo.add(item);
		}
		closeDatabase();
		return searchInfo;
	}
	//date那天的阅读记录（8天内）
	public List<ItemContent> getRecord(int myDate){
		List<ItemContent> recordInfo= new ArrayList<ItemContent>();
		ItemContent item;
		String date=""+myDate;
		openDatabase();
		Cursor cursor=db.query(CONTENT_TABLE, new String[]{CONTENT_ID,CONTENT_TITLE,CONTENT_QUEUE},CONTENT_DATE+" like ?", new String[]{date}, null, null, null);
		count = cursor.getCount();
		
		while(cursor.moveToNext()){
			item=new ItemContent();
			item.id=cursor.getString(0);
			item.title=cursor.getString(1);
			item.queue=cursor.getString(2);
			recordInfo.add(item);
		}
		
		return recordInfo;
	}
	//查询该日期(myDate)下的阅读记录，若无则返回false
	public boolean checkRecord(int myDate){
		String date=""+myDate;
		openDatabase();
		Cursor cursor=db.query(CONTENT_TABLE, new String[]{CONTENT_ID,CONTENT_TITLE,CONTENT_QUEUE},CONTENT_DATE+" like ?", new String[]{date}, null, null, null);
		if(cursor.getCount()!=0){
			
			return true;
		}else{
			
			return false;
		}
	}
	//获取更早的阅读记录
	public List<ItemContent> getRestRecord(int myDate){
		List<ItemContent> restRecordInfo=new ArrayList<ItemContent>();
		ItemContent item;
		String date=""+(myDate-7);
		openDatabase();
		Cursor cursor=db.query(CONTENT_TABLE, new String[]{CONTENT_ID,CONTENT_TITLE,CONTENT_QUEUE},CONTENT_DATE+" > 0 AND "+CONTENT_DATE+" < ? ", new String[]{date}, null, null, null);
		count = cursor.getCount();
		while(cursor.moveToNext()){
			item=new ItemContent();
			item.id=cursor.getString(0);
			item.title=cursor.getString(1);
			item.queue=cursor.getString(2);
			restRecordInfo.add(item);
		}
		
		return restRecordInfo;
	}
	//获取笔记本中的内容
	public List<ItemContent> getNotebookContent(String _id){
		List<ItemContent> noteContentInfo=new ArrayList<ItemContent>();
		String table="notebook_"+_id;
		openDatabase();
		cursor=db.query(table,new String[]{"_title"},null,null,null,null,null);
		count=cursor.getCount();
		while(cursor.moveToNext()){
			ItemContent item=new ItemContent();
			item.title=cursor.getString(0);
			noteContentInfo.add(item);
		}
		
		return noteContentInfo;
	}
	public boolean getNeedHelp(){
		boolean needHelp=true;
		openDatabase();
		cursor=db.query("need_help",new String[]{"need_help"},null,null,null,null,null);
		if(cursor.getCount()==0){
			return true;
		}else{
			cursor.moveToFirst();
			if(Integer.parseInt(cursor.getString(0))==0)
				needHelp=false;
			else if(Integer.parseInt(cursor.getString(0))==1)
				needHelp=true;
		
				return needHelp;
		}
	}
	public int getStatToday(){
		List<Integer> stat=new ArrayList<Integer>();
		stat=getTodayStat();
		if(stat.get(0)==0&&stat.get(1)==0)
			return -1;
		else
			return stat.get(0);

	}
	public List<Integer> getTodayStat(){
		List<Integer> todayStat=new ArrayList<Integer>();
		openDatabase();
		getCurrentTime();
		cursor=db.query("test_statistics",new String[]{"_right","_false"},"_date like ?",new String[]{date},null,null,null);
		cursor.moveToFirst();
		if(cursor.getCount()>0){
			todayStat.add(Integer.parseInt(cursor.getString(0)));
			todayStat.add(Integer.parseInt(cursor.getString(1)));
		}else{
			ContentValues value=new ContentValues();
			value.put("_right",0);
			value.put("_false",0);
			value.put("_date",date);
			db.insert("test_statistics","_id", value);
			
			todayStat.add(0);
			todayStat.add(0);
		}
		
		return todayStat;
	}
	public int getTodayWrong(){
		List<Integer> todayStat=new ArrayList<Integer>();
		todayStat=getTodayStat();
		return todayStat.get(1);
	}
	public int getTodayRight(){
		List<Integer> todayStat=new ArrayList<Integer>();
		todayStat=getTodayStat();
		return todayStat.get(0);
	}
	public List<Integer> getContentStat(String title){
		List<Integer> contentStat=new ArrayList<Integer>();
		openDatabase();
		getCurrentTime();
		cursor=db.query(CONTENT_TABLE,new String[]{CONTENT_RIGHT,CONTENT_FALSE},CONTENT_TITLE+" like ?",new String[]{title},null,null,null);
		Log.e("cursor0","kakakka");
		cursor.moveToFirst();
		Log.e("cursor0",cursor.getString(0));
		contentStat.add(Integer.parseInt(cursor.getString(0)));
		contentStat.add(Integer.parseInt(cursor.getString(1)));
		closeDatabase();
		
		return contentStat;
	}
	public int getContentRight(String title){
		List<Integer> contentStat=new ArrayList<Integer>();
		contentStat=getTodayStat();
		return contentStat.get(0);
	}
	public int getContentWrong(String title){
		List<Integer> contentStat=new ArrayList<Integer>();
		contentStat=getTodayStat();
		return contentStat.get(1);
	}
	public List<ItemContent> getWrongSet(){
		List<ItemContent> wrongSet=new ArrayList<ItemContent>();
		ItemContent item;
		openDatabase();
		Cursor cursor=db.query(CONTENT_TABLE, new String[]{CONTENT_ID,CONTENT_TITLE,CONTENT_QUEUE},CONTENT_FALSE+" > ? ", new String[]{"0"}, null, null, null);
		count = cursor.getCount();
		while(cursor.moveToNext()){
			item=new ItemContent();
			item.id=cursor.getString(0);
			item.title=cursor.getString(1);
			item.queue=cursor.getString(2);
			wrongSet.add(item);
		}
		return wrongSet;
	}
	public int getBestStat(){
		openDatabase();
		Cursor cursor=db.query("test_statistics", new String[]{CONTENT_RIGHT},null, null, null, null, null);
		cursor.moveToFirst();
		int bestStat=Integer.parseInt(cursor.getString(0));
		for(cursor.moveToFirst();cursor.moveToNext();){
			int stat=Integer.parseInt(cursor.getString(0));
			if(stat>bestStat)
				bestStat=stat;
		}
		return bestStat;
	}
	public int getTotalAnswer(){
		openDatabase();
		Cursor cursor=db.query("test_statistics", new String[]{CONTENT_RIGHT,CONTENT_FALSE},null, null, null, null, null);
		cursor.moveToFirst();
		int totalStat=Integer.parseInt(cursor.getString(0))+Integer.parseInt(cursor.getString(1));
		for(cursor.moveToFirst();cursor.moveToNext();){
			totalStat+=Integer.parseInt(cursor.getString(0))+Integer.parseInt(cursor.getString(1));
		}
		return totalStat;
	}
	public int getTotalRight(){
		openDatabase();
		Cursor cursor=db.query("test_statistics", new String[]{CONTENT_RIGHT},null, null, null, null, null);
		cursor.moveToFirst();
		int totalRight=Integer.parseInt(cursor.getString(0));
		for(cursor.moveToFirst();cursor.moveToNext();){
			totalRight+=Integer.parseInt(cursor.getString(0));
		}
		return totalRight;
	}
	public int getTotalWrong(){
		openDatabase();
		Cursor cursor=db.query("test_statistics", new String[]{CONTENT_FALSE},null, null, null, null, null);
		cursor.moveToFirst();
		int totalWrong=Integer.parseInt(cursor.getString(0));
		for(cursor.moveToFirst();cursor.moveToNext();){
			totalWrong+=Integer.parseInt(cursor.getString(0));
		}
		return totalWrong;
	}
	public int getCount(){
		return count;
	}
	private void getCurrentTime() {
		// TODO Auto-generated method stub
		SimpleDateFormat formatter=new SimpleDateFormat("yyyyMMdd");
		Date currentDate=new Date(System.currentTimeMillis());
		date=formatter.format(currentDate);
	}
}
