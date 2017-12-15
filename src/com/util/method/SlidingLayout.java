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
/**实现滑动菜单的出现和隐藏，将滑动菜单放在
 * 左fragment,其余内容放在右边fragment中
 * @author 雨中树
 * */
public class SlidingLayout extends RelativeLayout {
	private View view_Menu;
	private View view_ContentA;
	
	private Scroller scroller;
	private VelocityTracker velocityTracker;

	private Context context;
	
	private int movedPostionX;
	// 用Y坐标的变化判断是否截取（拉出菜单的）动作
	private int movedPostionY;
	private boolean slidable=true;
	//判断是否出现菜单
	private boolean toShowView_Menu = true;
	//判断是否出现主页面A
	//private boolean toShowView_ContentA = false;
	//判断是否出现主页面B
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

	//滑动的最小速度
	private int minVelocity;

	private static final int sDuration = 500;

	/**
	 * Record whether have clicked the toogle, so will recover the
	 * setWhickSideCanSlide state when click two times.
	 */
	private boolean view_MenuToogleClickable;
	//private boolean view_ContentAToogleClickable;

	//记录主页面是否为可点击事件
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
	//设置哪个界面是可拉出的
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
			
			// 手指点击主页面部分的屏幕，将隐藏菜单，出现主页面
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
			
			//若屏幕正在滚动，手指触碰时阻止滚动继续
			if (!scroller.isFinished()) {
				scroller.abortAnimation();
			}
			break;
			
		case MotionEvent.ACTION_MOVE:
			// distanceX为手指水平滑动的距离
			int distanceX = x - movedPostionX;
			//主页面水平移动的距离-手指水平滑动的距离
			int targetPositon = view_ContentA.getScrollX() - distanceX;
			movedPostionX = x;
			//手指水平滑动的距离为正,并大于菜单宽度，则回到0坐标；为负时则到菜单宽度位置
			if (toShowView_Menu) {
				if (targetPositon > 0) {
					targetPositon = 0;
				}
				if (targetPositon < -viewWidth_Menu) {
					targetPositon = -viewWidth_Menu;
				}
			}
			//若水平有滑动，则主页面设置为不聚焦（不可点击按钮事件）
			if (distanceX!= 0) {
				view_ContentAClickable = false;
				view_ContentA.scrollTo(targetPositon, 0);
			}
			break;
			
			//手指触碰屏幕后抬起时，若
		case MotionEvent.ACTION_UP:
			if (view_ContentAClickable) {
				view_ContentAClickable = false;
				// 当我们点击主页面的可视部分，将会关闭菜单栏，因此要恢复菜单栏的可点击状态
				if (showingView_Menu()) {
					resumeView_MenuClickState();
				}
				smoothlyScrollTo(-view_ContentA.getScrollX());
				break;
			}
			int dx = 0;
			if (toShowView_Menu) {
				if (velocityX > minVelocity) {
					//显示菜单栏
					dx = -viewWidth_Menu - view_ContentA.getScrollX();
				} else if (velocityX < -minVelocity) {
					//不显示菜单栏
					dx = -view_ContentA.getScrollX();
					//若点击了菜单按钮，则出现菜单，并恢复菜单按钮状态
					resumeView_MenuClickState();
				} else if (view_ContentA.getScrollX() <= -viewWidth_Menu / 2) {
					//速度很慢但是滑动了菜单宽度的一半，则显示菜单
					dx = -viewWidth_Menu - view_ContentA.getScrollX();
				} else {
					//速度很慢又没有超过一半的宽度，则滑动回初始位置
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
	//检查菜单是否可视，可视则return true.
	public boolean showingView_Menu() {
		LayoutMenu.buttonClickable=(view_ContentA.getScrollX()==-viewWidth_Menu);
		return LayoutMenu.buttonClickable;
	}
	//检查主页面是否可视，可视则return true.
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
	//设置菜单的按钮为可点击或否
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
