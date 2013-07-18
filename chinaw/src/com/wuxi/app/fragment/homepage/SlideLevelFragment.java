package com.wuxi.app.fragment.homepage;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.wuxi.app.BaseFragment;
import com.wuxi.app.R;
import com.wuxi.app.adapter.LeftMenuAdapter;
import com.wuxi.app.fragment.BaseSlideFragment;
import com.wuxi.app.fragment.MainMineFragment;
import com.wuxi.app.fragment.homepage.fantasticwuxi.ChannelFragment;
import com.wuxi.app.fragment.homepage.goverpublicmsg.PublicGoverMsgFragment;
import com.wuxi.app.fragment.homepage.goversaloon.GoverSaloonFragment;
import com.wuxi.app.fragment.homepage.informationcenter.InformationCenterFragment;
import com.wuxi.app.fragment.homepage.logorregister.LoginFragment;
import com.wuxi.app.fragment.homepage.logorregister.RegisterFragment;
import com.wuxi.app.fragment.homepage.publicservice.PublicServiceFragment;
import com.wuxi.app.listeners.SlideLinstener;
import com.wuxi.app.util.CacheUtil;
import com.wuxi.app.view.SlideMenuLayout;
import com.wuxi.domain.MenuItem;

public class SlideLevelFragment extends BaseFragment implements SlideLinstener,
<<<<<<< HEAD
OnItemClickListener {
=======
		OnItemClickListener, OnClickListener, OnCheckedChangeListener {
>>>>>>> f24275e6658fd6fa43783db49b93cc1bea32404f

	protected static final int FRAME_CONTENT = R.id.slide_main_content;
	private View view;
	private SlideMenuLayout mSlideMenuLayout;

	private MenuItem menuItem;// 首页选中的菜单
	private ListView mlvMenu;// 左侧菜单列表

	private static final String MENUITEM_CACKE_KEY = "man_menu_item";
	private static final String TAG = "SlideLevelFragment";
	private TextView login_tv_userlogin;
	private TextView login_tv_user_regisster;
	private RadioGroup right_menu_rg;
	private RadioButton right_menu_rb_collect;
	private RadioButton right_menu_rb_mzhd;
	private RadioButton right_menu_rb_mydownload;
	private RadioButton right_menu_rb_myset;
	private LeftMenuAdapter leftMenuAdapter;

	public void setMenuItem(MenuItem menuItem) {
		this.menuItem = menuItem;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.slide_level_layout, null);
		mSlideMenuLayout = (SlideMenuLayout) view
				.findViewById(R.id.slide_menu_layout);
		mSlideMenuLayout.setLeftSlideMenuId(R.id.left_menu);
		mSlideMenuLayout.setRightSlideMenuId(R.id.right_menu);
		mSlideMenuLayout.reset();

		login_tv_userlogin = (TextView) view
				.findViewById(R.id.login_tv_userlogin);
		login_tv_user_regisster = (TextView) view
				.findViewById(R.id.login_tv_user_regisster);
		right_menu_rg = (RadioGroup) view.findViewById(R.id.right_menu_rg);
		right_menu_rb_collect = (RadioButton) view
				.findViewById(R.id.right_menu_rb_collect);
		right_menu_rb_mzhd = (RadioButton) view
				.findViewById(R.id.right_menu_rb_mzhd);
		right_menu_rb_mydownload = (RadioButton) view
				.findViewById(R.id.right_menu_rb_mydownload);
		right_menu_rb_myset = (RadioButton) view
				.findViewById(R.id.right_menu_rb_myset);

		login_tv_userlogin.setOnClickListener(this);
		login_tv_user_regisster.setOnClickListener(this);
		right_menu_rg.setOnCheckedChangeListener(this);

		mlvMenu = (ListView) view.findViewById(R.id.lv_menu);
		mlvMenu.setOnItemClickListener(this);

		initLeftMenu();// 初始化左侧菜单数据
		init();

		return view;
	}

	@SuppressWarnings("unchecked")
	private void initLeftMenu() {

		List<MenuItem> leftMenuItems = (List<MenuItem>) CacheUtil
				.get(MENUITEM_CACKE_KEY);// 直接从缓存中取出菜单

		leftMenuAdapter = new LeftMenuAdapter(getActivity(),
				R.layout.slide_navigator_item, new int[] {
			R.id.tv_left_menu_name, R.id.left_iv_icon },
			leftMenuItems, null, position);
		mlvMenu.setAdapter(leftMenuAdapter);
	}

	private void init() {
		if (menuItem != null) {//
			switch (menuItem.getType()) {// 菜单类型

			case MenuItem.CUSTOM_MENU:

				if (menuItem.getName().equals("咨询中心")) {
					InformationCenterFragment informationCenterFragment = new InformationCenterFragment();
					informationCenterFragment.setMenuItem(menuItem);
					onTransaction(informationCenterFragment);
			
				} else if (menuItem.getName().equals("政府信息公开")) {
		
					PublicGoverMsgFragment publicGoverMsgFragment = new PublicGoverMsgFragment();
					publicGoverMsgFragment.setMenuItem(menuItem);
					onTransaction(publicGoverMsgFragment);
					
				} else if (menuItem.getName().equals("公共服务")) {
					PublicServiceFragment publicServiceFragment = new PublicServiceFragment();
					publicServiceFragment.setMenuItem(menuItem);
					onTransaction(publicServiceFragment);
				} else if (menuItem.getName().equals("政务大厅")) {
					GoverSaloonFragment saloonFragment = new GoverSaloonFragment();
					saloonFragment.setParentMenuItem(menuItem);
					onTransaction(saloonFragment);
				}

				break;

			case MenuItem.CHANNEL_MENU:// 如果点击的频道菜单
				ChannelFragment ch = new ChannelFragment();
				ch.setMenuItem(menuItem);
				onTransaction(ch);
				break;
			case 2:

				break;

			}
		}

	}

	private void onTransaction(BaseSlideFragment fragment) {
		FragmentManager manager = getActivity().getSupportFragmentManager();
		FragmentTransaction ft = manager.beginTransaction();
		ft.replace(FRAME_CONTENT, fragment);
		fragment.setFragment(this);
		ft.addToBackStack(null);
		fragment.setManagers(managers);// 传递管理器
		ft.commit();

	}

	/**
	 * 
	 * wanglu 泰得利通 返回
	 */
	@Override
	public void onBack() {
		managers.BackPress(this);
	}

	public void openLeftSlideMenu() {
		mSlideMenuLayout.openLeftSlideMenu();
	}

	public void openRightSlideMenu() {
		mSlideMenuLayout.openRightSlideMenu();
	}

	public void closeSlideMenu() {
		mSlideMenuLayout.reset();
	}

	public boolean isLeftMenuEnabled() {
		return mSlideMenuLayout.getLeftSlideMenuEnabled();
	}

	public boolean isRightMenuEnabled() {
		return mSlideMenuLayout.getRightSlideMenuEnabled();
	}

	public void setUnTouchableOffset(float start, float end) {
		mSlideMenuLayout.setUnTouchableOffset(start, end);
	}

	public void OpearnLeft() {
		if (isLeftMenuEnabled())
			closeSlideMenu();
		else
			openLeftSlideMenu();
	}

	public void OpearnRight() {
		if (isRightMenuEnabled())
			closeSlideMenu();
		else
			openRightSlideMenu();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View arg1, int position,
			long id) {

		MenuItem checkMenuItem = (MenuItem) parent.getItemAtPosition(position);
		if (checkMenuItem.getName().equals("政民互动")) {// 为回退特殊处理
//			managers.BackPress(this);
			MainMineFragment mainMineFragment = new MainMineFragment();
			mainMineFragment.setMenuItem(checkMenuItem);
			//			mainMineFragment.setMenuItem(checkMenuItem);//
			mainMineFragment.setPosition(position);
			managers.IntentFragment(mainMineFragment);

//			new Thread(
//					new Runnable(){
//
//						@Override
//						public void run() {
//							// TODO Auto-generated method stub
//							try {
//								Thread.sleep(200);
//								if(SlideLevelFragment.this!=null)
//									managers.BackPress(SlideLevelFragment.this);
//							} catch (InterruptedException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//							
//						}}
//					).start();
			


		} else {

			this.menuItem = checkMenuItem;
			this.position = position;
			leftMenuAdapter.setSelectPosition(position);
			leftMenuAdapter.notifyDataSetChanged();

			init();
		}

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.login_tv_userlogin:// 登录处理
			LoginFragment loginFragment = new LoginFragment();
			managers.IntentFragment(loginFragment);

			break;
		case R.id.login_btn_regist:// 注册
			RegisterFragment registerFragment = new RegisterFragment();
			managers.IntentFragment(registerFragment);
			break;

		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

		switch (checkedId) {

		case R.id.right_menu_rb_collect:// 我的收藏
			break;
		case R.id.right_menu_rb_mzhd:// 民政互动
			break;

		case R.id.right_menu_rb_mydownload:// 我的下载
			break;

		case R.id.right_menu_rb_myset:// 我的设置
			break;
		}
	}

}