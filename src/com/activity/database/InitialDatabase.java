package com.activity.database;

import java.io.*;

import com.activity.ladyclass.R;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
/**初始化数据库，第一次打开应用时
 * IS_INITIALIZED监听数据库是否存在
 * 否，则复制数据库到用户手机中
 * @author 雨中树*/
public class InitialDatabase {
	public static boolean IS_INITIALIZED=false;
	public final static String DATABASE_NAME="lady_class";
	public static String databasePath;
	public static String packageName;
	//public static String DATABASE_PATH=packageName+"/databases";
	public static String outFileName;

	//synchronized 
	public static void init(Context context){
		packageName=context.getPackageName();
		
		Log.e("database",IS_INITIALIZED+"");
		Log.e("packageName",packageName);
		
		databasePath=Environment.getDataDirectory() +"/data/"+packageName+"/databases/";
		outFileName=databasePath+"/"+DATABASE_NAME;
		
		File dir=new File(outFileName);
		if(dir.exists()){
			return;
		}
		
		dir=new File(databasePath);
			
		if(!dir.exists()){
			dir.mkdir();
		}
		
		InputStream input=null;
		OutputStream output=null;
		
		input=context.getResources().openRawResource(R.raw.lady_class);
		
		try{
			output=new FileOutputStream(outFileName);
			
			byte[] buffer=new byte[2048];
			
			int length;
			
			while((length=input.read(buffer))>0){
				output.write(buffer,0,length);
			}
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try{
				output.flush();
				output.close();
			}catch(IOException e){
				
			}
			
			try{
				input.close();
			}catch(IOException e){
				
			}
		}
			
		IS_INITIALIZED=true;
		Log.e("database",IS_INITIALIZED+"");
		Log.e("database","成功复制database");
	}
}
