package com.activity.adapter;

import java.util.List;

import com.activity.ladyclass.R;
import com.util.method.DateMethod;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**记录适配器
 * @author 雨中树*/
public class ListRecordAdapter extends BaseAdapter{
	private List<Integer> recordInfo;
	private boolean isChecked=false;
	private Context context;
	private LayoutInflater inflater;
	
	public ListRecordAdapter(List<Integer> recordInfo,Context context){
		this.recordInfo=recordInfo;
		this.context=context;
		inflater=LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(isChecked){
			return recordInfo.size();
		}else{
			return recordInfo.size()+1;	
		}
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return recordInfo.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		RelativeLayout record=new RelativeLayout(context);
		record=(RelativeLayout)inflater.inflate(R.layout.item_record,null);
		if(position==0){
			TextView recordDate=(TextView)record.findViewById(R.id.record_date);
			recordDate.setText("今天");
		}else if(position<recordInfo.size()){
			TextView recordDate=(TextView)record.findViewById(R.id.record_date);
			recordDate.setText(DateMethod.getYMD(recordInfo.get(position)));
		}else if(position>=recordInfo.size()){
			TextView recordDate=(TextView)record.findViewById(R.id.record_date);
			recordDate.setText("更早");
		}
		return record;
	}
	
}
