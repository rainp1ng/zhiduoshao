package com.activity.adapter;

import static com.activity.database.MyDatabaseHelper.*;

import java.util.List;

import com.activity.database.MyDatabaseHelper;
import com.activity.ladyclass.NoteActivity;
import com.activity.ladyclass.R;
import com.util.method.ItemContent;
import com.util.method.SlidingView;
import com.util.method.SlidingView.OnSlideListener;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/**笔记本的内容适配器
 * @author 雨中树*/
public class ListNoteContentAdapter extends BaseAdapter{
	private List<ItemContent> contentItem;
	private Context context;
	private LayoutInflater inflater;
	private SlidingView lastSlideViewWithStatusOn;
	private MyDatabaseHelper myHelper;
	private String _id;
	
	public ListNoteContentAdapter(List<ItemContent> contentItem,Context context,String _id){
		this.contentItem=contentItem;
		this.context=context;
		this._id=_id;
		inflater=LayoutInflater.from(context);
		myHelper=new MyDatabaseHelper(context,DATABASE_NAME,null,1);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return contentItem.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return contentItem.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
        SlidingView slideView = (SlidingView) convertView;
        if (slideView == null) {
            View itemView = inflater.inflate(R.layout.item_title_content, null);

            slideView = new SlidingView(context);
            slideView.setContentView(itemView);

            holder = new ViewHolder(slideView);
            switch(position%10){
            case 0:
            	setColor(holder,R.color.color0);
            	setIcon(holder,R.drawable.title_img0);
            	break;
            case 1:
            	setColor(holder,R.color.color1);
            	setIcon(holder,R.drawable.title_img1);
            	break;
            case 2:
            	setColor(holder,R.color.color2);
            	setIcon(holder,R.drawable.title_img2);
            	break;
            case 3:
            	setColor(holder,R.color.color3);
            	setIcon(holder,R.drawable.title_img3);
            	break;
            case 4:
            	setColor(holder,R.color.color4);
            	setIcon(holder,R.drawable.title_img4);
            	break;
            case 5:
            	setColor(holder,R.color.color5);
            	setIcon(holder,R.drawable.title_img5);
            	break;
            case 6:
            	setColor(holder,R.color.color6);
            	setIcon(holder,R.drawable.title_img6);
            	break;
            case 7:
            	setColor(holder,R.color.color7);
            	setIcon(holder,R.drawable.title_img7);
            	break;
            case 8:
            	setColor(holder,R.color.color8);
            	setIcon(holder,R.drawable.title_img8);
            	break;
            case 9:
            	setColor(holder,R.color.color9);
            	setIcon(holder,R.drawable.title_img9);
            	break;
            }
            slideView.setOnSlideListener(new OnSlideListener(){
				@Override
				public void onSlide(View view, int status) {
					// TODO Auto-generated method stub
					if (lastSlideViewWithStatusOn != null && lastSlideViewWithStatusOn != view) {
			            lastSlideViewWithStatusOn.shrink();
			        }

			        if (status == SLIDE_STATUS_ON) {
			            lastSlideViewWithStatusOn = (SlidingView) view;
			        }
				}
            });
            slideView.setTag(holder);
        } else {
            holder = (ViewHolder) slideView.getTag();
        }
        ItemContent item = contentItem.get(position);
        item.slideView = slideView;
        item.slideView.shrink();
        slideView.shrink();

        holder.content.setText(item.title);
        holder.deleteHolder.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				deleteNote(contentItem.get(position).title);
			}
		});
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.item_in);  
		anim.setDuration(130);
		slideView.startAnimation(anim);
		
        return slideView;
    }
	void setColor(ViewHolder holder,int color){
		holder.line.setBackgroundResource(color);
	}
	void setIcon(ViewHolder holder,int icon){
		holder.icon.setImageResource(icon);
	}
	private void deleteNote(String title){
		SQLiteDatabase db=myHelper.getWritableDatabase();
		String str="DELETE FROM notebook_"+_id+" WHERE _title='"+title+"'";
		db.execSQL(str);
		Toast toast = Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT); 
		toast.show();
		((NoteActivity) context).initContentInfo();
	}
	private static class ViewHolder {
        public TextView content;
        public ViewGroup deleteHolder;
        public ImageView icon;
        public TextView line;
        ViewHolder(View view) {
        	line=(TextView)view.findViewById(R.id.line);
        	icon=(ImageView)view.findViewById(R.id.icon);
            content = (TextView) view.findViewById(R.id.content);
            deleteHolder = (ViewGroup)view.findViewById(R.id.holder);
        }
	}
}
