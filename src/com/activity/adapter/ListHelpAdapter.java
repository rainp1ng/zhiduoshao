package com.activity.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.activity.ladyclass.R;
/**—°‘Ò∞Ô÷˙µƒƒ⁄»›  ≈‰∆˜
 * @author ”Í÷– ˜*/
public class ListHelpAdapter extends BaseAdapter{
	private List<String> helpInfo;
	private Context context;
	private LayoutInflater inflater;
	
	public ListHelpAdapter(List<String> helpInfo,Context context){
		this.helpInfo=helpInfo;
		this.context=context;
		inflater=LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return helpInfo.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return helpInfo.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		RelativeLayout helpContent=new RelativeLayout(context);
		helpContent=(RelativeLayout)inflater.inflate(R.layout.item_help,null);
		if(position<helpInfo.size()){
			TextView recordDate=(TextView)helpContent.findViewById(R.id.record_date);
			recordDate.setText(helpInfo.get(position));
		}
		return helpContent;
	}
}