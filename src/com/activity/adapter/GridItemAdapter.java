package com.activity.adapter;

import java.util.*;

import com.activity.ladyclass.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static com.activity.database.MyDatabaseHelper.*;
/**笔记本内容适配器
 * @author 雨中树*/
public class GridItemAdapter extends BaseAdapter{
	public final static int HEALTH_PIC=0;
	public final static int SECURITY_PIC=1;
	public final static int MEDICINE_PIC=2;
	public final static int DIET_PIC=3;
	public final static int LIFE_PIC=4;
	public final static int FUN_PIC=5;
	//存储标题和数量信息
	private List<HashMap<String,String>> itemInfo;
	//上下文
	private Context context;
	private boolean isChecked=false;
	private LayoutInflater inflater;
	
	private int pictureRes;
	
	public GridItemAdapter(List<HashMap<String,String>> itemInfo,Context context){
		this.itemInfo=itemInfo;
		this.context=context;
		inflater=LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(isChecked){
			return itemInfo.size();
		}else{
			return itemInfo.size()+1;	
		}
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
		TextView bookName=(TextView)item.findViewById(R.id.book_name);
		LinearLayout itemBackground=(LinearLayout)item.findViewById(R.id.item_bg);
		if(position<itemInfo.size()){
			//列出所有笔记本名与内容数量
			pictureRes=Integer.parseInt(itemInfo.get(position).get(NOTEBOOK_PIC));
			bookName.setText(itemInfo.get(position).get(NOTEBOOK_NAME));
			
			switch(pictureRes){
				case HEALTH_PIC:
					itemBackground.setBackgroundResource(R.drawable.health_p);
					break;
				case SECURITY_PIC:
					itemBackground.setBackgroundResource(R.drawable.security_p);
					break;
				case MEDICINE_PIC:
					itemBackground.setBackgroundResource(R.drawable.medicine_p);
					break;
				case DIET_PIC:
					itemBackground.setBackgroundResource(R.drawable.diet_p);
					break;
				case LIFE_PIC:
					itemBackground.setBackgroundResource(R.drawable.life_p);
					break;
				case FUN_PIC:
					itemBackground.setBackgroundResource(R.drawable.fun_p);
					break;
				case 6:
					itemBackground.setBackgroundResource(R.drawable.bg6_p);
					break;
				case 7:
					itemBackground.setBackgroundResource(R.drawable.bg7_p);
					break;
				case 8:
					itemBackground.setBackgroundResource(R.drawable.bg8_p);
					break;
				case 9:
					itemBackground.setBackgroundResource(R.drawable.bg9_p);
					break;
				case 10:
					itemBackground.setBackgroundResource(R.drawable.bg10_p);
					break;
				case 11:
					itemBackground.setBackgroundResource(R.drawable.bg11_p);
					break;
				default:
					itemBackground.setBackgroundResource(R.drawable.bg10_p);
					break;
			}
			//itemBackground.setBackgroundResource(R.drawable.background);
		}else if(position>=itemInfo.size()){
			//可自行添加笔记本
			itemBackground.setBackgroundResource(R.drawable.bg10_p);
			bookName.setText("添加笔记本");
		}
		//动画
		Animation anim = AnimationUtils.loadAnimation(context, R.anim.item_in);  
		anim.setDuration(300*position);
		item.startAnimation(anim);
			
		return item;
	}
}
