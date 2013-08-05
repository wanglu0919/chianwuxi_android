package com.wuxi.app.view;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.wuxi.app.R;
import com.wuxi.app.adapter.DynamicTitleAdapter;
import com.wuxi.app.fragment.homepage.mygoverinteractpeople.GIPChannelContentListFragment;
import com.wuxi.app.listeners.InitializContentLayoutListner;
import com.wuxi.app.listeners.MenuItemInitLayoutListener;
import com.wuxi.domain.Channel;
import com.wuxi.domain.MenuItem;

/**
 * 头部可滑动自定义view
 * 
 * @author wanglu
 * 
 */
public class DynamicTitleLayout extends ViewGroup {
	private static final int PERSCREEN_ITEM_COUNT = 7;// 每一屏item数量
	private int mCurScreen;// 当前屏
	private static final String TAG = "TITLESCROLLLAYOUT";

	private InitializContentLayoutListner initializContentLayoutListner;// 该自定义控件所在的fragment
	private int perscreenCount = PERSCREEN_ITEM_COUNT;// 每屏数量,默认为7
	private int checkPositon;// 选中的坐标
	private MenuItemInitLayoutListener menuItemInitLayoutListener;// 菜单点击与指点界面绑定监听器
	private DynamicTitleAdapter adapter;

	public MenuItemInitLayoutListener getMenuItemInitLayoutListener() {
		return menuItemInitLayoutListener;
	}

	public void setMenuItemInitLayoutListener(
			MenuItemInitLayoutListener menuItemInitLayoutListener) {
		this.menuItemInitLayoutListener = menuItemInitLayoutListener;
	}

	public int getPerscreenCount() {
		return perscreenCount;
	}

	public void setPerscreenCount(int perscreenCount) {
		this.perscreenCount = perscreenCount;
	}

	public void setInitializContentLayoutListner(
			InitializContentLayoutListner initializContentLayoutListner) {
		this.initializContentLayoutListner = initializContentLayoutListner;
	}

	/*
	 * public void setItems(List<Channel> items) { this.items = items; }
	 */
	public DynamicTitleLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public DynamicTitleLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (changed) {
			int childLeft = 0;
			final int childCount = getChildCount();

			for (int i = 0; i < childCount; i++) {
				final View childView = getChildAt(i);
				if (childView.getVisibility() != View.GONE) {
					final int childWidth = childView.getMeasuredWidth();
					childView.layout(childLeft, 0, childLeft + childWidth,
							childView.getMeasuredHeight());
					childLeft += childWidth;
				}
			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		final int width = MeasureSpec.getSize(widthMeasureSpec);
//		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//		 if (widthMode != MeasureSpec.EXACTLY) {
//		 throw new IllegalStateException(
//		 "ScrollLayout only canmCurScreen run at EXACTLY mode!");
//		 }

		final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		 if (heightMode != MeasureSpec.EXACTLY) {
		 throw new IllegalStateException(
		 "ScrollLayout only can run at EXACTLY mode!");
		 }

		// The children are given the same width and height as the scrollLayout
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
		}

		scrollTo(mCurScreen * width, 0);

	}

	public static String getModeStr(int measureSpec) {

		int specMode = MeasureSpec.getMode(measureSpec);
		String str = "null";

		switch (specMode) {
		case MeasureSpec.UNSPECIFIED:
			str = "UNSPECIFIED";
			break;
		case MeasureSpec.AT_MOST:
			str = "AT_MOST";
			break;
		case MeasureSpec.EXACTLY:
			str = "EXACTLY";
			break;
		}
		return str;
	}

	/*
	 * 初始化屏频道
	 */
	public void initChannelScreen(Context context, LayoutInflater inflater,
			List<Channel> chanItems) {
		if (chanItems == null) {
			return;
		}
		checkPositon = 0;// 默认选中第一个Chanel
		GridView child = (GridView) inflater.inflate(
				R.layout.dynamictitle_gridview_layout, null);
		child.setNumColumns(perscreenCount); // 设置GridView的每列显示view个数
		adapter=new DynamicTitleAdapter(context,
				R.layout.title_grid_item_layout,
				new int[] { R.id.tv_actionname }, null,chanItems);
		adapter.notifyDataSetChanged();
		adapter.notifyDataSetInvalidated();
		child.setAdapter(adapter);

		child.setOnItemClickListener(new TitleItemlOnclick());
		addView(child);
		
<<<<<<< HEAD
		loadChannelContentList(chanItems.get(0));

=======
		int i = 0;
		for (Channel item : chanItems) {

			if (i % getPerscreenCount() == 0) {

				if (onScreenItems != null) {
					GridView child = (GridView) inflater.inflate(
							R.layout.title_action_gridview_layout, null);
					child.setNumColumns(perscreenCount); // 设置GridView的每列显示view个数
					child.setAdapter(new TitleChannelAdapter(context,
							R.layout.title_grid_item_layout,
							new int[] { R.id.tv_actionname }, null,
							onScreenItems, currentScreen,0));

					child.setOnItemClickListener(new TitleItemlOnclick());
					currentScreen++;
					addView(child);
				}

				onScreenItems = new ArrayList<Channel>();
			}

			// 最后一屏操作
			if (currentScreen > totalScreenNum + 1) {

				onScreenItems = new ArrayList<Channel>();
			}

			onScreenItems.add(item);

			// add last category screen //最后一屏
			if (i == chanItems.size() - 1) {
				GridView child = (GridView) inflater.inflate(
						R.layout.title_action_gridview_layout, null);
				child.setNumColumns(perscreenCount); // 设置GridView的每列显示view个数
				child.setAdapter(new TitleChannelAdapter(context,
						R.layout.title_grid_item_layout,
						new int[] { R.id.tv_actionname }, null, onScreenItems,
						currentScreen,0));

				child.setOnItemClickListener(new TitleItemlOnclick());
				addView(child);

			}

			i++;
		}
		postInvalidate();// // 此时同样也需要刷新View ，否则效果可能有误差
>>>>>>> a95fad0f8e5fe3f6a4bd97d156b033864ba1686a
	}

	public void initMenuItemScreen(Context context, LayoutInflater inflater,
			List<MenuItem> menuItems) {
		if (menuItems == null) {
			return;
		}
<<<<<<< HEAD
=======
		int i = 0;
		totalScreenNum = menuItems.size() / getPerscreenCount();// 屏数

		checkPositons = new int[totalScreenNum + 1];
		for (int j = 0; j < checkPositons.length; j++) {// 初始化头部安选中的下标
			checkPositons[j] = -1;
		}

		checkPositons[0] = 0;// 默认选中第一屏第一个Chanel
		int currentScreen = 0;// 当前屏

		List<MenuItem> onScreenItems = null;// 一个屏上的图标

		for (MenuItem item : menuItems) {

			if (i % getPerscreenCount() == 0) {

				if (onScreenItems != null) {
					GridView child = (GridView) inflater.inflate(
							R.layout.title_action_gridview_layout, null);
					child.setNumColumns(perscreenCount); // 设置GridView的每列显示view个数
					child.setAdapter(new TitleChannelAdapter(context,
							R.layout.title_grid_item_layout,
							new int[] { R.id.tv_actionname }, null,
							onScreenItems, currentScreen,0));

					child.setOnItemClickListener(new TitleItemlOnclick());
					currentScreen++;
					addView(child);
				}

				onScreenItems = new ArrayList<MenuItem>();
			}

			// 最后一屏操作
			if (currentScreen > totalScreenNum + 1) {

				onScreenItems = new ArrayList<MenuItem>();
			}

			onScreenItems.add(item);

			// add last category screen //最后一屏
			if (i == menuItems.size() - 1) {
				GridView child = (GridView) inflater.inflate(
						R.layout.title_action_gridview_layout, null);
				child.setNumColumns(perscreenCount); // 设置GridView的每列显示view个数
				child.setAdapter(new TitleChannelAdapter(context,
						R.layout.title_grid_item_layout,
						new int[] { R.id.tv_actionname }, null, onScreenItems,
						currentScreen,0));

				child.setOnItemClickListener(new TitleItemlOnclick());
				addView(child);

			}

			i++;
		}

>>>>>>> a95fad0f8e5fe3f6a4bd97d156b033864ba1686a

		checkPositon = 0;// 默认选中第一个Chanel
		
		GridView child = (GridView) inflater.inflate(
				R.layout.dynamictitle_gridview_layout, null);
		child.setNumColumns(perscreenCount); // 设置GridView的每列显示view个数
		adapter=new DynamicTitleAdapter(context,
				R.layout.title_grid_item_layout,
				new int[] { R.id.tv_actionname }, null,menuItems);
		adapter.notifyDataSetChanged();
		adapter.notifyDataSetInvalidated();
		child.setAdapter(adapter);
		child.setOnItemClickListener(new TitleItemlOnclick());
		addView(child);



	}

	private class TitleItemlOnclick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {

			Channel channel = null;
			MenuItem menuItem = null;
			Object item = parent.getItemAtPosition(position);
			if (item instanceof Channel) {
				channel = (Channel) item;
			} else if (item instanceof MenuItem) {
				menuItem = (MenuItem) item;
			}

			/**
			 * 切换选中与未选择的样式
			 */
			if (checkPositon!= position) {
				View checkView = parent.getChildAt(position);

				TextView tv_Check = (TextView) checkView
						.findViewById(R.id.tv_actionname);
				tv_Check.setBackgroundResource(R.drawable.title_item_select_bg);

				tv_Check.setTextColor(Color.WHITE);

				View oldCheckView = parent
						.getChildAt(checkPositon);
				if (null != oldCheckView) {

					TextView tv_oldCheck = (TextView) oldCheckView
							.findViewById(R.id.tv_actionname);
					tv_oldCheck.setBackgroundResource(R.drawable.title_item_bg);

					tv_oldCheck.setTextColor(Color.parseColor("#177CCA"));

				}

				checkPositon = position;
			}

			/**
			 * 频道处理
			 */
			if (channel != null) {
				loadChannelContentList(channel);
			}

			/**
			 * 普通菜单处理
			 * */

			if (menuItem != null && initializContentLayoutListner != null
					&& menuItemInitLayoutListener != null) {

				menuItemInitLayoutListener.bindMenuItemLayout(
						initializContentLayoutListner, menuItem);

			}

		}
	}
	
	public void loadChannelContentList(Channel channel){
		GIPChannelContentListFragment gIPContentListFragment = new GIPChannelContentListFragment();					
		gIPContentListFragment.setChannel(channel);
		if (initializContentLayoutListner != null) {
			if (gIPContentListFragment != null) {
				initializContentLayoutListner.bindContentLayout(gIPContentListFragment);
			}
		}
	}

}