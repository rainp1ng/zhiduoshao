package com.activity.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.activity.ladyclass.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import static com.activity.adapter.GridItemAdapter.*;
/**选择的封面适配器
 * @author 雨中树*/
public class GridPicAdapter extends BaseAdapter{
	
	private List<HashMap<String,String>> itemInfo;
	//上下文
	private Context context;
	private LayoutInflater inflater;

	public GridPicAdapter(Context context){
		this.context=context;
		inflater=LayoutInflater.from(context);
		setPictureRes();
	}
	public void setPictureRes(){
		itemInfo=new ArrayList<HashMap<String,String>>();
		HashMap<String,String> item;
		for(int i=0;i<12;i++){
			item=new HashMap<String,String>();
			item.put("picRes",i+"");
			itemInfo.add(item);
		}
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return itemInfo.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return itemInfo.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		RelativeLayout item=new RelativeLayout(context);
		item=(RelativeLayout)inflater.inflate(R.layout.item_grid,null);
		
		LinearLayout itemBackground=(LinearLayout)item.findViewById(R.id.item_bg);
		
		if(position<itemInfo.size()){
			switch(position){
			case HEALTH_PIC:
				itemBackground.setBackgroundResource(R.drawable.health_pic);
				break;
			case SECURITY_PIC:
				itemBackground.setBackgroundResource(R.drawable.security_pic);
				break;
			case MEDICINE_PIC:
				itemBackground.setBackgroundResource(R.drawable.medicine_pic);
				break;
			case DIET_PIC:
				itemBackground.setBackgroundResource(R.drawable.diet_pic);
				break;
			case LIFE_PIC:
				itemBackground.setBackgroundResource(R.drawable.life_pic);
				break;
			case FUN_PIC:
				itemBackground.setBackgroundResource(R.drawable.fun_pic);
				break;
			case 6:
				itemBackground.setBackgroundResource(R.drawable.bg6_pic);
				break;
			case 7:
				itemBackground.setBackgroundResource(R.drawable.bg7_pic);
				break;
			case 8:
				itemBackground.setBackgroundResource(R.drawable.bg8_pic);
				break;
			case 9:
				itemBackground.setBackgroundResource(R.drawable.bg9_pic);
				break;
			case 10:
				itemBackground.setBackgroundResource(R.drawable.bg10_pic);
				break;
			case 11:
				itemBackground.setBackgroundResource(R.drawable.bg11_pic);
				break;
			default:
				itemBackground.setBackgroundResource(R.drawable.bg10_pic);
				break;
			}
		}
		return item;
	}

}
