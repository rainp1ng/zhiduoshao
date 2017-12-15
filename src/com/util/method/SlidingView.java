package com.util.method;

import com.activity.ladyclass.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

/**实现控件的滑动功能
 * @author 雨中树*/
public class SlidingView extends LinearLayout {

    //private static final String TAG = "SlideView";
	public static boolean contentClickable=false;
	
    private Context mContext;
    private LinearLayout mViewContent;
    private Scroller mScroller;
    private OnSlideListener mOnSlideListener;

    private int mHolderWidth = 120;

    private int mLastX = 0;
    private int mLastY = 0;
    private static final int TAN = 2;

    public interface OnSlideListener {
        public static final int SLIDE_STATUS_OFF = 0;
        public static final int SLIDE_STATUS_START_SCROLL = 1;
        public static final int SLIDE_STATUS_ON = 2;

    public void onSlide(View view, int status);
    }

    public SlidingView(Context context) {
        super(context);
        initView();
    }

    public SlidingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        mContext = getContext();
        mScroller = new Scroller(mContext);

        setOrientation(LinearLayout.HORIZONTAL);
        View.inflate(mContext, R.layout.holder_delete_btn, this);
        mViewContent = (LinearLayout) findViewById(R.id.view_content);
        mHolderWidth = Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, mHolderWidth, getResources()
                        .getDisplayMetrics()));
    }

    public void setButtonText(CharSequence text) {
        ((TextView)findViewById(R.id.delete_btn)).setText(text);
    }

    public void setContentView(View view) {
        mViewContent.addView(view);
    }

    public void setOnSlideListener(OnSlideListener onSlideListener) {
        mOnSlideListener = onSlideListener;
    }

    public void shrink() {
        if (getScrollX() != 0) {
            this.smoothScrollTo(0, 0);
        }
    }

    public void onRequireTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        int scrollX = getScrollX();
        //Log.d(TAG, "x=" + x + "  y=" + y);

        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN: {
            if (!mScroller.isFinished()) {
                mScroller.abortAnimation();
            }
            if (mOnSlideListener != null) {
                mOnSlideListener.onSlide(this,
                        OnSlideListener.SLIDE_STATUS_START_SCROLL);
            }
            break;
        }
        case MotionEvent.ACTION_MOVE: {
            int deltaX = x - mLastX;
            int deltaY = y - mLastY;
            if (Math.abs(deltaX) < Math.abs(deltaY) * TAN) {
                break;
            }

            int newScrollX = scrollX - deltaX;
            if (deltaX != 0) {
                if (newScrollX < 0) {
                    newScrollX = 0;
                    contentClickable=true;
                    Log.e("moveEvent","right");
                } else if (newScrollX > mHolderWidth) {
                    newScrollX = mHolderWidth;
                    contentClickable=false;
                    Log.e("moveEvent","left");
                }else{
                	contentClickable=false;
                	Log.e("moveEvent","moving");
                }
                this.scrollTo(newScrollX, 0);
            }
            break;
        }
        case MotionEvent.ACTION_UP: {
            int newScrollX = 0;
            if (scrollX - mHolderWidth * 0.5 > 0) {
                newScrollX = mHolderWidth;
            	contentClickable=false;
            	smoothScrollTo(newScrollX,0);
            }else{
            	this.smoothScrollToRight();
            }
            if (mOnSlideListener != null) {
                mOnSlideListener.onSlide(this,
                        newScrollX == 0 ? OnSlideListener.SLIDE_STATUS_OFF
                                : OnSlideListener.SLIDE_STATUS_ON);
            }
            break;
        }
        default:
            break;
        }

        mLastX = x;
        mLastY = y;
    }
    private void smoothScrollToRight(){
    	smoothScrollTo(0, 0);
    	contentClickable=true;
    }
    private void smoothScrollTo(int destX, int destY) {     
    	int scrollX = getScrollX();
        int delta = destX - scrollX;
        mScroller.startScroll(scrollX, 0, delta, 0, Math.abs(delta) * 3);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }
}
