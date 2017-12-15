package com.activity.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.activity.ladyclass.MainActivity;
import com.activity.ladyclass.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.*;
/**提交建议的页面
 * @author 雨中树*/
public class LayoutSug extends Fragment{
	ProgressDialog progressDialog;
	private EditText name;
	private EditText suggestions;
	private Button sendButton;
	private Button menuButton;
	private MainActivity main;
	private Handler mHandler;
	private Toast toast;
	private String fkr;
	private String fknr;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_sug, null);
		findView(view);
		initView(view);
		return view;
	}
	public void findView(View view){
		name=(EditText)view.findViewById(R.id.name);
		suggestions=(EditText)view.findViewById(R.id.suggestions);
		sendButton=(Button)view.findViewById(R.id.send_btn);
		menuButton=(Button)view.findViewById(R.id.menu_btn);
		main=(MainActivity)LayoutSug.this.getActivity();
		setHandler();
	}
	private void setHandler() {
		// TODO Auto-generated method stub
		mHandler =new Handler(){
			@Override
			public void handleMessage(Message msg){
				super.handleMessage(msg);
				//只要执行到这里就关闭对话框
				progressDialog.dismiss();
				name.setText("");
				suggestions.setText("");
				showTip("建议提交成功，感谢您的支持！"); 
			}
		};
	}
	private void showTip(String str){
		toast = Toast.makeText(main, str , Toast.LENGTH_SHORT); 
		toast.show();
	}
	public void initView(View view){
		menuButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				main.showView_MenuToogle();
			}
		});
		sendButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(!suggestions.getText().toString().equals("")&&!suggestions.getText().toString().equals(" ")){
					if(checkNetwork(main)){
						sendMessege();
					}
				}else if(suggestions.getText().toString().equals(fknr)){
					showTip("请勿重复提交！");
				}else{
					showTip("没有可提交的建议！");
				}
			}
			private boolean checkNetwork(Context context) { 
				if (context != null) { 
					ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); 
					NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo(); 
					if(mNetworkInfo != null) { 
						return mNetworkInfo.isAvailable(); 
					}else{
						showTip("网络连接异常，请重试！");
					}
				} 
				return false; 
			} 
			private void sendMessege() {
				// TODO Auto-generated method stub
				fkr = name.getText().toString();
	            fknr = suggestions.getText().toString();
	            if(!fknr.equals("")){
	            	progressDialog= ProgressDialog.show(main, "提交建议", "正在提交，请稍等…");
	            	new Thread(new Runnable() {
			        	public void run() {
			            	Message msg = new Message();
			            	HttpPost httpRequest = new HttpPost("http://ftssoft.duapp.com/feedback_api.php");
			            	List<NameValuePair> params = new ArrayList<NameValuePair>();
			            	params.add(new BasicNameValuePair("code","5193566"));
			            	params.add(new BasicNameValuePair("kfz","rainp1ng"));
			            	params.add(new BasicNameValuePair("xmmc","蕾娣课堂"));
			            	params.add(new BasicNameValuePair("fkr", fkr));
			            	params.add(new BasicNameValuePair("fknr", fknr));
			            	try {
			                	httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			                	HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
			                	if (httpResponse.getStatusLine().getStatusCode() == 200) {
			                    	//获取返回的信息
			                    	String strResult = EntityUtils.toString(httpResponse.getEntity());
			                    	msg.obj = strResult;
			                	} else {
			                    	msg.obj = "Error Response";
			                	}
			            	} catch (Exception e) {
			                	msg.obj = e.getMessage().toString();
			                	e.printStackTrace();
			            	}
			            	mHandler.sendMessage(msg);
			        	}
			    	}).start();
	            }
			}
		});
	}
}