package com.activity.database;

import java.io.*;

import com.activity.ladyclass.R;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
/**��ʼ�����ݿ⣬��һ�δ�Ӧ��ʱ
 * IS_INITIALIZED�������ݿ��Ƿ����
 * ���������ݿ⵽�û��ֻ���
 * @author ������*/
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
		Log.e("database","�ɹ�����database");
	}
}
