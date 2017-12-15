package com.util.method;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

public class ListContentView extends ListView{
	
	private static final String TAG = "ListContentView";

    private SlidingView mFocusedItemView;
    
	public ListContentView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public ListContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListContentView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    public void shrinkListItem(int position) {
        View item = getChildAt(position);

        if (item != null) {
            try {
                ((SlidingView) item).shrink();
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN: {
            int x = (int) event.getX();
            int y = (int) event.getY();
            int position = pointToPosition(x, y);
            Log.e(TAG, "postion=" + position);
            if (position != INVALID_POSITION) {
                ItemContent data = (ItemContent) getItemAtPosition(position);
                mFocusedItemView = data.slideView;
                //Log.e(TAG, "FocusedItemView=" + mFocusedItemView);
            }
        }
        default:
            break;
        }

        if (mFocusedItemView != null) {
            mFocusedItemView.onRequireTouchEvent(event);
        }

        return super.onTouchEvent(event);
    }
}
