package com.util.method;

import com.activity.fragment.LayoutMenu;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Scroller;
/**ʵ�ֻ����˵��ĳ��ֺ����أ��������˵�����
 * ��fragment,�������ݷ����ұ�fragment��
 * @author ������
 * */
public class SlidingLayout extends RelativeLayout {
	private View view_Menu;
	private View view_ContentA;
	
	private Scroller scroller;
	private VelocityTracker velocityTracker;

	private Context context;
	
	private int movedPostionX;
	// ��Y����ı仯�ж��Ƿ��ȡ�������˵��ģ�����
	private int movedPostionY;
	private boolean slidable=true;
	//�ж��Ƿ���ֲ˵�
	private boolean toShowView_Menu = true;
	//�ж��Ƿ������ҳ��A
	//private boolean toShowView_ContentA = false;
	//�ж��Ƿ������ҳ��B
	//private boolean toShowView_ContentB = true;

	/**
	 * The state before we click the view to show the left or right view.
	 */
	private boolean toShowView_MenuBeforeToogle = true;
	//private boolean toShowview_ContentABeforeToogle = false;
	//private boolean toShowview_ContentBBeforeToogle = true;
	
	private int viewWidth_Menu;
	//private int viewWidth_Content;

	private int windowWidth;

	/**
	 * The min value for scrolling.
	 */
	private int touchSlop;

	//��������С�ٶ�
	private int minVelocity;

	private static final int sDuration = 500;

	/**
	 * Record whether have clicked the toogle, so will recover the
	 * setWhickSideCanSlide state when click two times.
	 */
	private boolean view_MenuToogleClickable;
	//private boolean view_ContentAToogleClickable;

	//��¼��ҳ���Ƿ�Ϊ�ɵ���¼�
	private boolean view_ContentAClickable;

	public SlidingLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	public SlidingLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	public SlidingLayout(Context context) {
		super(context);
		init(context);
	}
	private void init(Context context) {
		this.context = context;
		scroller = new Scroller(context);
		touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		minVelocity = ViewConfiguration.get(context)
				.getScaledMinimumFlingVelocity();
		windowWidth = getWindowWidth(context);
	}
	/**
	 * Set the three view of the SlidingMenu and the width of the left view and
	 * right view.
	 * 
	 * @param veiw_Menu
	 * @param view_ContentA
	 * @param centerView
	 * @param viewWidth_Menu
	 *            Width of the veiw_Menu.
	 * @param viewWidth_Content
	 *            Width of the view_ContentA.
	 */
	public void setView(View veiw_Menu,  View view_ContentA,
			int viewWidth_Menu) {
		RelativeLayout.LayoutParams veiw_MenuParams = new LayoutParams(
				(int) convertDpToPixel(viewWidth_Menu, context),
				LayoutParams.MATCH_PARENT);
		veiw_MenuParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		addView(veiw_Menu, veiw_MenuParams);


		RelativeLayout.LayoutParams centerParams = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		addView(view_ContentA, centerParams);

		this.view_Menu = veiw_Menu;
		this.view_ContentA = view_ContentA;
	}
	//�����ĸ������ǿ�������
	public void setViewShowable(boolean toShowView_Menu) {
		this.toShowView_Menu = toShowView_Menu;
	}
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		viewWidth_Menu = view_Menu.getWidth();
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		int x = (int) ev.getRawX();
		int y = (int) ev.getRawY();

		int action = ev.getAction();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			movedPostionX = x;
			movedPostionY = y;
			
			// ��ָ�����ҳ�沿�ֵ���Ļ�������ز˵���������ҳ��
			if (showingView_Menu()) {
				if (x > viewWidth_Menu) {
					view_ContentAClickable = true;
					slidable=true;
					return true;
				}
			}
			if(x>0.25*windowWidth)
				slidable=false;
			else
				slidable=true;
			
			break;

		case MotionEvent.ACTION_MOVE:
			if(slidable){
				int distance = x - movedPostionX;
				int yDistance = Math.abs(y - movedPostionY);
				// If the Y-axis distance is bigger than X-axis,do not intercept
				// this and parse it to the child view.
				if (Math.abs(distance) < yDistance) {
					break;
				}
				if (toShowView_Menu) {
					// scroll to right or is in scrolling to right.
					if (distance > touchSlop || view_ContentA.getScrollX() < 0) {
						movedPostionX = x;
						return true;
					}
				}
			}
			break;
		case MotionEvent.ACTION_UP:

			break;

		default:
			break;
		}

		return false;
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		int x = (int) event.getRawX();
		int y = (int) event.getRawY();

		createVelocityTracker();
		velocityTracker.addMovement(event);
		velocityTracker.computeCurrentVelocity(1000);
		float velocityX = velocityTracker.getXVelocity();

		int action = event.getAction();
		switch (action) {
		
		case MotionEvent.ACTION_DOWN:
			movedPostionX = x;
			movedPostionY = y;
			
			//����Ļ���ڹ�������ָ����ʱ��ֹ��������
			if (!scroller.isFinished()) {
				scroller.abortAnimation();
			}
			break;
			
		case MotionEvent.ACTION_MOVE:
			// distanceXΪ��ָˮƽ�����ľ���
			int distanceX = x - movedPostionX;
			//��ҳ��ˮƽ�ƶ��ľ���-��ָˮƽ�����ľ���
			int targetPositon = view_ContentA.getScrollX() - distanceX;
			movedPostionX = x;
			//��ָˮƽ�����ľ���Ϊ��,�����ڲ˵���ȣ���ص�0���ꣻΪ��ʱ�򵽲˵����λ��
			if (toShowView_Menu) {
				if (targetPositon > 0) {
					targetPositon = 0;
				}
				if (targetPositon < -viewWidth_Menu) {
					targetPositon = -viewWidth_Menu;
				}
			}
			//��ˮƽ�л���������ҳ������Ϊ���۽������ɵ����ť�¼���
			if (distanceX!= 0) {
				view_ContentAClickable = false;
				view_ContentA.scrollTo(targetPositon, 0);
			}
			break;
			
			//��ָ������Ļ��̧��ʱ����
		case MotionEvent.ACTION_UP:
			if (view_ContentAClickable) {
				view_ContentAClickable = false;
				// �����ǵ����ҳ��Ŀ��Ӳ��֣�����رղ˵��������Ҫ�ָ��˵����Ŀɵ��״̬
				if (showingView_Menu()) {
					resumeView_MenuClickState();
				}
				smoothlyScrollTo(-view_ContentA.getScrollX());
				break;
			}
			int dx = 0;
			if (toShowView_Menu) {
				if (velocityX > minVelocity) {
					//��ʾ�˵���
					dx = -viewWidth_Menu - view_ContentA.getScrollX();
				} else if (velocityX < -minVelocity) {
					//����ʾ�˵���
					dx = -view_ContentA.getScrollX();
					//������˲˵���ť������ֲ˵������ָ��˵���ť״̬
					resumeView_MenuClickState();
				} else if (view_ContentA.getScrollX() <= -viewWidth_Menu / 2) {
					//�ٶȺ������ǻ����˲˵���ȵ�һ�룬����ʾ�˵�
					dx = -viewWidth_Menu - view_ContentA.getScrollX();
				} else {
					//�ٶȺ�����û�г���һ��Ŀ�ȣ��򻬶��س�ʼλ��
					dx = -view_ContentA.getScrollX();

					resumeView_MenuClickState();
				}

			}

			smoothlyScrollTo(dx);
			releaseVelocityTracker();
			break;

		default:
			break;
		}

		return true;
	}
	/**
	 * Scroll the middle view.Scroller can scroll more smoothly.
	 * 
	 * @param distance
	 *            Distance will scroll.
	 */
	public void smoothlyScrollTo(int distance) {
		scroller.startScroll(view_ContentA.getScrollX(), 0, distance, 0,
				sDuration);
		invalidate();
	}

	@Override
	// Called by a parent to request that a child update its values for mScrollX
	// and mScrollY if necessary.
	// This will typically be done if the child is animating a scroll using a
	// Scroller object.
	public void computeScroll() {
		// If do not override the computeScroll method, mScroller.startScroll
		// will have no effect.
		if (scroller.computeScrollOffset()) {
			view_ContentA.scrollTo(scroller.getCurrX(), scroller.getCurrY());
			postInvalidate();
		}
	}
	//���˵��Ƿ���ӣ�������return true.
	public boolean showingView_Menu() {
		LayoutMenu.buttonClickable=(view_ContentA.getScrollX()==-viewWidth_Menu);
		return LayoutMenu.buttonClickable;
	}
	//�����ҳ���Ƿ���ӣ�������return true.
	public boolean showingView_ContentA() {
		boolean viewContentShowing=(view_ContentA.getScrollX() == 0);
		LayoutMenu.buttonClickable=!viewContentShowing;
		return viewContentShowing;
	}
	private void createVelocityTracker() {
		if (velocityTracker == null) {
			velocityTracker = VelocityTracker.obtain();
		}
	}
	private void releaseVelocityTracker() {
		if (velocityTracker != null) {
			velocityTracker.recycle();
			velocityTracker = null;
		}
	}
	//���ò˵��İ�ťΪ�ɵ�����
	public void showView_MenuToogle() {
		if (showingView_ContentA()) {
			toShowView_MenuBeforeToogle = toShowView_Menu;
			setViewShowable(true);
			view_MenuToogleClickable = true;
		} else if (showingView_Menu()) {
			resumeView_MenuClickState();
		}
		if (showingView_Menu()) {
			smoothlyScrollTo(viewWidth_Menu);
		} else {
			smoothlyScrollTo(-viewWidth_Menu);
		}
	}

	/**
	 * Resume the state to before we click the show left view button.
	 */
	private void resumeView_MenuClickState() {
		if (view_MenuToogleClickable) {
			view_MenuToogleClickable = false;
			setViewShowable(toShowView_MenuBeforeToogle);
		}
	}
	/**
	 * This method converts dp unit to equivalent pixels, depending on device
	 * density.
	 * 
	 * @param dp
	 *            A value in dp (density independent pixels) unit. Which we need
	 *            to convert into pixels
	 * @param context
	 *            Context to get resources and device specific display metrics
	 * @return A float value to represent px equivalent to dp depending on
	 *         device density
	 */
	public static float convertDpToPixel(float dp, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return px;
	}
	/**
	 * This method converts device specific pixels to density independent
	 * pixels.
	 * 
	 * @param px
	 *            A value in px (pixels) unit. Which we need to convert into db
	 * @param context
	 *            Context to get resources and device specific display metrics
	 * @return A float value to represent dp equivalent to px value
	 */
	public static float convertPixelsToDp(float px, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float dp = px / (metrics.densityDpi / 160f);
		return dp;
	}
	private int getWindowWidth(Context context) {
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(displayMetrics);
		return displayMetrics.widthPixels;
	}
}
