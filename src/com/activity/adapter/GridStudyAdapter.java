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
import static com.activity.adapter.GridItemAdapter.*;
/**学习界面的内容适配器
 * @author 雨中树*/
public class GridStudyAdapter extends BaseAdapter{

	//存储标题和数量信息
	private List<HashMap<String,String>> itemInfo;
	//上下文
	private Context context;
	private LayoutInflater inflater;
	
	private int pictureRes;
	
	public GridStudyAdapter(List<HashMap<String,String>> itemInfo,Context context){
		this.itemInfo=itemInfo;
		this.context=context;
		inflater=LayoutInflater.from(context);
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
		
		if(position<itemInfo.size()){
			//列出所有学习类型与内容数量
			TextView bookName=(TextView)item.findViewById(R.id.book_name);
			LinearLayout itemBackground=(LinearLayout)item.findViewById(R.id.item_bg);
			
			pictureRes=Integer.parseInt(itemInfo.get(position).get(STUDY_TYPE_PIC));
			bookName.setText(itemInfo.get(position).get(STUDY_TYPE));
			
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
				default:
					itemBackground.setBackgroundResource(R.drawable.bg11_p);
					break;
			}
		}else if(position==itemInfo.size()){
			
		}
		Animation anim = AnimationUtils.loadAnimation(context, R.anim.item_in);  
		anim.setDuration(300*position);
		item.startAnimation(anim);
		
		return item;
	}

}

