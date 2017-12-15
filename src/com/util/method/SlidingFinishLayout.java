package com.util.method;

import java.util.LinkedList;
import java.util.List;

import com.activity.ladyclass.R;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Scroller;
/**一个将可滑动的View放进来的容器，让在里面的容器可以
 * 滑动，结合SlidingFinishActivity实现左滑退出的功能
 * @author 雨中树
 * */
public class SlidingFinishLayout extends FrameLayout {
	//private static final String TAG = SlidingFinishLayout.class.getSimpleName();
	private View contentView;
	private int touchSlop;
	private int downX;
	private int downY;
	private int tempX;
	private Scroller mScroller;
	private int viewWidth;
	private boolean isSilding;
	private boolean isFinish;
	private Drawable shadowDrawable;
	private Activity activity;
	private boolean slidable=true;
	private int windowWidth;
	private List<ViewPager> mViewPagers = new LinkedList<ViewPager>();
	
	public SlidingFinishLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		windowWidth = getWindowWidth(context);
	}
	public SlidingFinishLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		windowWidth = getWindowWidth(context);
		touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		mScroller = new Scroller(context);

		shadowDrawable = getResources().getDrawable(R.drawable.shadow_left);
	}
	private int getWindowWidth(Context context) {
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(displayMetrics);
		return displayMetrics.widthPixels;
	}
	public void attachToActivity(Activity activity) {
		this.activity = activity;
		TypedArray a = activity.getTheme().obtainStyledAttributes(
				new int[] { android.R.attr.windowBackground });
		int background = a.getResourceId(0, 0);
		a.recycle();

		ViewGroup decor = (ViewGroup) activity.getWindow().getDecorView();
		ViewGroup decorChild = (ViewGroup) decor.getChildAt(0);
		decorChild.setBackgroundResource(background);
		decor.removeView(decorChild);
		addView(decorChild);
		setContentView(decorChild);
		decor.addView(this);
	}
	private void setContentView(View decorChild) {
		contentView = (View) decorChild.getParent();
	}

	//事件拦截操作
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		//处理ViewPager冲突问题
		ViewPager mViewPager = getTouchViewPager(mViewPagers, ev);
		//Log.i(TAG, "mViewPager = " + mViewPager);
		
		if(mViewPager != null && mViewPager.getCurrentItem() != 0){
			return super.onInterceptTouchEvent(ev);
		}

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX = tempX = (int) ev.getRawX();
			downY = (int) ev.getRawY();
			if(downX>0.25*windowWidth)
				slidable=false;
			else
				slidable=true;
			
			break;
		case MotionEvent.ACTION_MOVE:
			if(slidable){
			int moveX = (int) ev.getRawX();
			// 满足此条件屏蔽SildingFinishLayout里面子类的touch事件
			if (moveX - downX > touchSlop
					&& Math.abs((int) ev.getRawY() - downY) < touchSlop) {
				return true;
			}
			}
			break;
		}

		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			int moveX = (int) event.getRawX();
			int deltaX = tempX - moveX;
			tempX = moveX;
			if (moveX - downX > touchSlop
					&& Math.abs((int) event.getRawY() - downY) < touchSlop) {
				isSilding = true;
			}

			if (moveX - downX >= 0 && isSilding) {
				contentView.scrollBy(deltaX, 0);
			}
			break;
		case MotionEvent.ACTION_UP:
			isSilding = false;
			if (contentView.getScrollX() <= -viewWidth / 2) {
				isFinish = true;
				scrollRight();
			} else {
				scrollOrigin();
				isFinish = false;
			}
			break;
		}

		return true;
	}
	
	// 获取SlidingActivity里面的ViewPager的集合
	private void getAlLViewPager(List<ViewPager> mViewPagers, ViewGroup parent){
		int childCount = parent.getChildCount();
		for(int i=0; i<childCount; i++){
			View child = parent.getChildAt(i);
			if(child instanceof ViewPager){
				mViewPagers.add((ViewPager)child);
			}else if(child instanceof ViewGroup){
				getAlLViewPager(mViewPagers, (ViewGroup)child);
			}
		}
	}
	
	
	//返回我们touch的ViewPager
	private ViewPager getTouchViewPager(List<ViewPager> mViewPagers, MotionEvent ev){
		if(mViewPagers == null || mViewPagers.size() == 0){
			return null;
		}
		Rect mRect = new Rect();
		for(ViewPager v : mViewPagers){
			v.getHitRect(mRect);
			
			if(mRect.contains((int)ev.getX(), (int)ev.getY())){
				return v;
			}
		}
		return null;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed) {
			viewWidth = this.getWidth();
			
			getAlLViewPager(mViewPagers, this);
			//Log.i(TAG, "ViewPager size = " + mViewPagers.size());
		}
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if (shadowDrawable != null && contentView != null) {

			int left = contentView.getLeft()
					- shadowDrawable.getIntrinsicWidth();
			int right = left + shadowDrawable.getIntrinsicWidth();
			int top = contentView.getTop();
			int bottom = contentView.getBottom();

			shadowDrawable.setBounds(left, top, right, bottom);
			shadowDrawable.draw(canvas);
		}

	}


	//滚动出界面
	private void scrollRight() {
		final int delta = (viewWidth + contentView.getScrollX());
		// 调用startScroll方法来设置一些滚动的参数，在computeScroll()方法中调用scrollTo来滚动item
		mScroller.startScroll(contentView.getScrollX(), 0, -delta + 1, 0,
				Math.abs(delta));
		postInvalidate();
	}

	//滚动到起始位置
	private void scrollOrigin() {
		int delta = contentView.getScrollX();
		mScroller.startScroll(contentView.getScrollX(), 0, -delta, 0,
				Math.abs(delta));
		postInvalidate();
	}

	@Override
	public void computeScroll() {
		// 调用startScroll的时候scroller.computeScrollOffset()返回true，
		if (mScroller.computeScrollOffset()) {
			contentView.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();

			if (mScroller.isFinished() && isFinish) {
				activity.finish();
			}
		}
	}
}