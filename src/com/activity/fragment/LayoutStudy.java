package com.activity.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.activity.adapter.GridStudyAdapter;
import com.activity.database.GetInfoFromDatabase;
import com.activity.database.MyDatabaseHelper;
import com.activity.ladyclass.*;
import com.util.method.NoScrollGridView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import static com.activity.database.MyDatabaseHelper.*;
/**��Ҫѧϰ���ݵ�ҳ��
 * @author ������*/
public class LayoutStudy extends Fragment {
	private Button menuButton;
	private TextView showCount;
	private TextView toastStat;
	private MainActivity main;
	private NoScrollGridView gv;
	private MyDatabaseHelper myHelper;
	private GetInfoFromDatabase dbInfo;
	private GridStudyAdapter adapter;
	//private LayoutInflater inflater;
	private List<HashMap<String,String>> bookInfo;
	
	private int count=0;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_study, null);
		findView(view);
		initView(view);
		return view;
	}
	
	public void findView(View view){
		menuButton=(Button)view.findViewById(R.id.menu_btn);
		showCount=(TextView)view.findViewById(R.id.count);
		toastStat=(TextView)view.findViewById(R.id.toast_stat);
		main=(MainActivity)LayoutStudy.this.getActivity();
		gv=(NoScrollGridView)view.findViewById(R.id.grid_study);
		myHelper=new MyDatabaseHelper(main, DATABASE_NAME, null, 1);
		bookInfo=new ArrayList<HashMap<String,String>>();
		//inflater=LayoutInflater.from(main);
		getbookInfo();
		setbookInfo();
	}
	private void getbookInfo() {
		// TODO Auto-generated method stub
		dbInfo=new GetInfoFromDatabase(myHelper);
		bookInfo=dbInfo.getBookInfo();
		count=dbInfo.getCount();
	}
	private void setbookInfo() {
		// TODO Auto-generated method stub
		adapter=new GridStudyAdapter(bookInfo,main);
		gv.setAdapter(adapter);	
		setText("��"+count+"����");
	}
	public void initView(View view){
		menuButton.setOnClickListener(new OnClickButtonEvent());
		gv.setOnItemClickListener(new OnItemClickEvent());
		((ScrollView)view.findViewById(R.id.study_scroll)).smoothScrollTo(10,10);
		setToast();
	}
	private void setToast() {
		// TODO Auto-generated method stub
		int count=dbInfo.getStatToday();
		Log.e("check",count+"");
		if(count>0)
			toastStat.setText("����������"+count+"���⿩~���ٽ�������~");
		else if(count==0)
			toastStat.setText("�����컹û�д��һ����Ŷ~�Ͻ�ȥ����ѧ֪ʶ��~");
		else 
			toastStat.setText("�����컹û������~�Ͻ�ѡ����Ŀ���Ϳ�ʼ�����~��");
	}
	public void onResume(){
		super.onResume();
		setToast();
	}
	public void setText(String text){
		showCount.setText(text);
	}
	class OnClickButtonEvent implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v==menuButton){
				main.showView_MenuToogle();
			}
		}
	}
	class OnItemClickEvent implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			//�򿪵�����������
			Intent intent = new Intent();
			intent.setClass(main, StuActivity.class);
			intent.putExtra("origin","����");
			intent.putExtra(STUDY_TYPE,bookInfo.get(position).get(STUDY_TYPE));
			//Log.d("debg",bookInfo.get(position).get(STUDY_TYPE));
			main.startActivity(intent);
			main.overridePendingTransition(R.anim.slide_right_in,0);  
		}
	}
}
