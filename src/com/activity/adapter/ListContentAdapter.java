package com.activity.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.activity.database.GetInfoFromDatabase;
import com.activity.database.MyDatabaseHelper;
import com.activity.ladyclass.R;
import com.util.method.ItemContent;
import com.util.method.SlidingView;
import com.util.method.SlidingView.OnSlideListener;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import static com.activity.database.MyDatabaseHelper.*;
/**学习的内容列表适配器
 * @author 雨中树*/
public class ListContentAdapter extends BaseAdapter{
	
	private List<ItemContent> contentItem;
	//上下文
	private Context context;
	private LayoutInflater inflater;
	private MyDatabaseHelper myHelper;
	private SlidingView lastSlideViewWithStatusOn;
	private GetInfoFromDatabase dbInfo;
	
	public ListContentAdapter(List<ItemContent> contentItem,Context context){
		this.contentItem=contentItem;
		this.context=context;
		myHelper=new MyDatabaseHelper(context,DATABASE_NAME,null,1);
		inflater=LayoutInflater.from(context);
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
		ContentHolder holder;
        SlidingView slideView = (SlidingView) convertView;
        if (slideView == null) {
            View itemView = inflater.inflate(R.layout.item_title_content, null);

            slideView = new SlidingView(context);
            slideView.setContentView(itemView);

            holder = new ContentHolder(slideView);
            slideView.setOnSlideListener(new OnSlideListener(){
				@Override
				public void onSlide(View view, int status) {
					// TODO Auto-generated method stub
					if (lastSlideViewWithStatusOn != null && lastSlideViewWithStatusOn != view) {
			            lastSlideViewWithStatusOn.shrink();
			            //Log.e("这是干嘛","滑动吗？");
			        }
			        if (status == SLIDE_STATUS_ON) {
			            lastSlideViewWithStatusOn = (SlidingView) view;
			        }
				}
            });
            slideView.setTag(holder);
        } else {
            holder = (ContentHolder) slideView.getTag();
        }
        ItemContent item = contentItem.get(position);
        item.slideView = slideView;
        item.slideView.shrink();
        slideView.shrink();

        holder.content.setText(item.title);
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
        
        holder.noteHolder.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub	
				doNote(contentItem.get(position).title);
			}
		});
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.item_in);  
		anim.setDuration(130);
		slideView.startAnimation(anim);
		
        return slideView;
    }
	void setIcon(ContentHolder holder,int icon){
		holder.icon.setImageResource(icon);
	}
	void setColor(ContentHolder holder,int color){
		holder.line.setBackgroundResource(color);
	}
	private void doNote(String title){
		String _id=DefaultNoteID();
		if(_id!=null){
			putNoteIntoTable(_id,title);
		}else{
			showFailedTip1();
		}
	}
	void showFailedTip1(){
		Dialog tipDialog=null;
		Builder builder=new AlertDialog.Builder(context);
		builder.setTitle("收藏失败");
		builder.setMessage("未设置默认笔记本！请在菜单  ->笔记中选择并设置默认笔记本！");
		builder.setNegativeButton("确定", null);
		tipDialog=builder.create();
		tipDialog.show();
	}
	private void putNoteIntoTable(String _id,String contentTitle){
		if(checkContent(_id,contentTitle)){
			String table="notebook_"+_id;
			SQLiteDatabase db=myHelper.getWritableDatabase();
			ContentValues values=new ContentValues();
			values.put("_title",contentTitle);
			db.insert(table,"_id",values);
			Toast toast = Toast.makeText(context, "笔记收藏成功！", Toast.LENGTH_SHORT); 
			toast.show();
		}else{
			showFailedTip2();
		}
	}
	void showFailedTip2(){
		Toast toast = Toast.makeText(context, "笔记本已收藏该条知识！", Toast.LENGTH_SHORT); 
		toast.show();
		
	}
	boolean checkContent(String _id,String contentTitle){
		boolean notSame=true;
		dbInfo=new GetInfoFromDatabase(myHelper);
		List<ItemContent> noteContentItem=new ArrayList<ItemContent>();
		noteContentItem=dbInfo.getNotebookContent(_id);	
		for(int i=0;i<noteContentItem.size();i++){
			if(contentTitle.equals(noteContentItem.get(i).title)){
				notSame=false;
				break;
			}
		}
		return notSame;
	}
	private String DefaultNoteID(){
		dbInfo=new GetInfoFromDatabase(myHelper);
		List<HashMap<String,String>> notebookInfo=new ArrayList<HashMap<String,String>>();
		notebookInfo=dbInfo.getNotebookInfo();
		String bookID=null;
		for(int i=0;i<notebookInfo.size();i++){
			int j=Integer.parseInt(notebookInfo.get(i).get(NOTEBOOK_DEFAULT));
			if(j!=0){
				bookID=notebookInfo.get(i).get(NOTEBOOK_ID);
				break;
			}
		}
		return bookID;
	}
	private static class ContentHolder {
        public TextView content;
        public ViewGroup noteHolder;
        public ImageView icon;
        //public ImageView icon1;
        public TextView line;
        ContentHolder(View view) {
        	icon=(ImageView)view.findViewById(R.id.icon);
        	//icon1=(ImageView)view.findViewById(R.id.icon1);
            line=(TextView)view.findViewById(R.id.line);
        	content = (TextView) view.findViewById(R.id.content);
            noteHolder = (ViewGroup)view.findViewById(R.id.holder);
            TextView text=(TextView)view.findViewById(R.id.btn);
            text.setText("收藏");
        }
	}
}
