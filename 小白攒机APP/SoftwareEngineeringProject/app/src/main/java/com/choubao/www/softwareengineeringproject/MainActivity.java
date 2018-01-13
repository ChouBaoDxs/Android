/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.choubao.www.softwareengineeringproject;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.choubao.www.softwareengineeringproject.customview.CustomPopupWindowPayMoney;
import com.choubao.www.softwareengineeringproject.customview.CustomPopupWindowShoppingCar;
import com.choubao.www.softwareengineeringproject.customview.LeftMenu;
import com.choubao.www.softwareengineeringproject.touchripplewidget.RippleImageView;
import com.choubao.www.softwareengineeringproject.vo.HardWare;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements View.OnClickListener{

	private final Handler handler = new Handler();

	private PagerSlidingTabStrip tabs;
	private ViewPager pager;
	private MyPagerAdapter adapter;

	private ImageView showleftMenu;

	private Drawable oldBackground = null;
	//private int currentColor =R.color.theme_color;
//	private int currentColor =0xFF00FFFF;
	private int currentColor =0x00FFFFFF;//默认是透明

	private MyHardWareListFragment myCpuListFragment,myHardDiskListFragment,mySSdListFragment,myMonitorListFragment,myMemoryBarListFragment,myCaseListFragment,
	                          myMotherBoardListFragment,myKeyboardListFragment,myMouseListFragment,myCDdriveListFragment,myGraphicsCardListFragment;

	public AssembleAComputerApplication app;

	public ArrayList<HardWare> shoppingCars=new ArrayList<>();
	private TextView sum_Money;
	public void setSumMoneyText() {
		double sum=0;
		for(HardWare h:shoppingCars) {
			sum+=h.getJiage();
		}
		sum_Money.setText(sum+"");
	}

	public LinearLayout main_Linearlayout;
	public RippleImageView RippleImageView_shopping_car;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
		setContentView(R.layout.activity_main_test);
		main_Linearlayout= (LinearLayout) findViewById(R.id.main_Linearlayout);

		RippleImageView_shopping_car= (RippleImageView) findViewById(R.id.RippleImageView_shopping_car);
		sum_Money= (TextView) findViewById(R.id.sum_Money);

		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);

		pager = (ViewPager) findViewById(R.id.pager);
		adapter = new MyPagerAdapter(getSupportFragmentManager());

		pager.setAdapter(adapter);

		final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
				.getDisplayMetrics());
		pager.setPageMargin(pageMargin);

		tabs.setViewPager(pager);

		currentColor= getApplicationContext().getResources().getColor(R.color.theme_color);
		//currentColor= Color.RED;
		//System.out.println(currentColor);
		changeColor(currentColor);

		//设置预加载3个Fragment
		pager.setOffscreenPageLimit(3);
		showleftMenu= (ImageView) findViewById(R.id.include_topbar).findViewById(R.id.showleftMenu);
		showleftMenu.setOnClickListener(this);

		boolean bool=isNetworkConnected(this);
		if (bool) {
//			Toast.makeText(this, "网络可用", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, "当前无网络", Toast.LENGTH_SHORT).show();
		}
	}

	//因为要使用自定义标题栏，所以全都注释掉
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	//右上角的菜单按钮事件
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.action_contact:
			//QuickContactFragment dialog = new QuickContactFragment();
			//dialog.show(getSupportFragmentManager(), "QuickContactFragment");
			return true;

		}

		return super.onOptionsItemSelected(item);
	}*/

	private void changeColor(int newColor) {
		tabs.setIndicatorColor(newColor);
		// change ActionBar color just if an ActionBar is available
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

			Drawable colorDrawable = new ColorDrawable(newColor);
			Drawable bottomDrawable = getResources().getDrawable(R.drawable.actionbar_bottom);
			LayerDrawable ld = new LayerDrawable(new Drawable[] { colorDrawable, bottomDrawable });

			if (oldBackground == null) {

				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
					ld.setCallback(drawableCallback);
				} else {
					//这里是设置标题栏颜色
					//getActionBar().setBackgroundDrawable(ld);
				}

			} else {

				TransitionDrawable td = new TransitionDrawable(new Drawable[] { oldBackground, ld });

				// workaround for broken ActionBarContainer drawable handling on
				// pre-API 17 builds
				// https://github.com/android/platform_frameworks_base/commit/a7cc06d82e45918c37429a59b14545c6a57db4e4
				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
					td.setCallback(drawableCallback);
				} else {
					//getActionBar().setBackgroundDrawable(td);
				}
				td.startTransition(200);
			}

			oldBackground = ld;

			// http://stackoverflow.com/questions/11002691/actionbar-setbackgrounddrawable-nulling-background-from-thread-handler
			//getActionBar().setDisplayShowTitleEnabled(false);
			//getActionBar().setDisplayShowTitleEnabled(true);

		}
		currentColor = newColor;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("currentColor", currentColor);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		currentColor = savedInstanceState.getInt("currentColor");
		changeColor(currentColor);
	}

	private Drawable.Callback drawableCallback = new Drawable.Callback() {
		@Override
		public void invalidateDrawable(Drawable who) {
			getActionBar().setBackgroundDrawable(who);
		}

		@Override
		public void scheduleDrawable(Drawable who, Runnable what, long when) {
			handler.postAtTime(what, when);
		}

		@Override
		public void unscheduleDrawable(Drawable who, Runnable what) {
			handler.removeCallbacks(what);
		}
	};



	public class MyPagerAdapter extends FragmentPagerAdapter {

		private final String[] TITLES = {"处理器", "机械硬盘", "固态硬盘", "显示器", "内存", "机箱", "主板", "键盘", "鼠标", "光驱", "显卡"};

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

		@Override
		public Fragment getItem(int position) {
			//return SuperAwesomeCardFragment.newInstance(position);
			switch(position){
				case 0://处理器
					if (myCpuListFragment == null) {
						myCpuListFragment = MyHardWareListFragment.newInstance("处理器");
					}
					return myCpuListFragment;
			    case 1://机械硬盘
					if (myHardDiskListFragment == null) {
						myHardDiskListFragment= MyHardWareListFragment.newInstance("机械硬盘");
					}
					return myHardDiskListFragment;
				case 2://固态硬盘
					if (mySSdListFragment == null) {
						mySSdListFragment= MyHardWareListFragment.newInstance("固态硬盘");
					}
					return mySSdListFragment;
			    case 3://显示器
					if (myMonitorListFragment== null) {
						myMonitorListFragment= MyHardWareListFragment.newInstance("显示器");
					}
					return myMonitorListFragment;
			    case 4://内存条
					if (myMemoryBarListFragment== null) {
						myMemoryBarListFragment= MyHardWareListFragment.newInstance("内存");
					}
					return myMemoryBarListFragment;
				case 5://机箱
					if (myCaseListFragment== null) {
						myCaseListFragment= MyHardWareListFragment.newInstance("机箱");
					}
					return myCaseListFragment;
				case 6://主板
					if (myMotherBoardListFragment== null) {
						myMotherBoardListFragment= MyHardWareListFragment.newInstance("主板");
					}
					return myMotherBoardListFragment;
				case 7://键盘
					if (myKeyboardListFragment== null) {
						myKeyboardListFragment= MyHardWareListFragment.newInstance("键盘");
					}
					return myKeyboardListFragment;
				case 8://鼠标
					if (myMouseListFragment== null) {
						myMouseListFragment= MyHardWareListFragment.newInstance("鼠标");
					}
					return myMouseListFragment;
				case 9://光驱
					if (myCDdriveListFragment== null) {
						myCDdriveListFragment= MyHardWareListFragment.newInstance("光驱");
					}
					return myCDdriveListFragment;
				case 10://显卡
					if (myGraphicsCardListFragment== null) {
						myGraphicsCardListFragment= MyHardWareListFragment.newInstance("显卡");
					}
					return myGraphicsCardListFragment;
			}
			return null;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			//把销毁方法去掉，防止销毁Fragment 这样就不卡了
			//super.destroyItem(container, position, object);
		}
	}


	//购物车PopupWindow
	private CustomPopupWindowShoppingCar mPop;
	private boolean popPupWindow_is_show=false;

	public void popupWindow_dismiss(View v)
	{
		if(popPupWindow_is_show==true)
		{
			mPop.dismiss();
			popPupWindow_is_show=false;
		}
	}

	public void showShoppingCart(View v) {
/*		View view=getLayoutInflater().inflate(R.layout.popup_window_layout,null);
		listView_popupwindow_shopping_cart= (ListView) view.findViewById(R.id.listView_popupwindow_shopping_cart);
		//清空购物车按钮的设置
//		LinearLayout clear_shopping_cart= (LinearLayout) findViewById(R.id.clear_shopping_cart);
//		clear_shopping_cart.setOnClickListener(this);

		shoppingCarListAdapter =new ShoppingCarListAdapter(MainActivity.this,shoppingCars,MainActivity.this);
		listView_popupwindow_shopping_cart.setAdapter(shoppingCarListAdapter);

		//创建PopupWindow(窗体的视图,宽,高)
		PopupWindow popupWindow=new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

		//设置背景
		popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.btn_default));
		//设置弹出样式 这里的是从左边飞进来
		//popupWindow.setAnimationStyle(android.R.style.Animation_Translucent);

		//下面是设置自己自定义的进出样式
		popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);

		//设置背景透明度
		popupWindow.getBackground().setAlpha(50);
		//设置点击菜单外部时退出菜单
		popupWindow.setOutsideTouchable(true);
		//获取当前窗口焦点
		popupWindow.setFocusable(true);
		//设置可以被触摸
		popupWindow.setTouchable(true);
		//设置软键盘弹出时模式
		popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

		//设置在购物车按钮上方显示
		//int[] location=new int[2];  //0是x坐标 1是y坐标
		//v.getLocationOnScreen(location);
		//System.out.println("x:"+location[0]+"y:"+location[1]);
		//popupWindow.showAtLocation(v, Gravity.BOTTOM,location[0],location[1]-popupWindow.getHeight());
		//System.out.println(""+popupWindow.getHeight());
		//popupWindow.showAtLocation(v,Gravity.BOTTOM,location[0],location[1]);
		popupWindow.showAsDropDown(v,0,5);

		int v_with = v.getWidth();
		int v_height = v.getHeight();
		//设置在底部显示
		//popupWindow.showAtLocation(v, Gravity.BOTTOM,0,0);
		*/

		if(popPupWindow_is_show==false) {
			mPop=new CustomPopupWindowShoppingCar(this);
			//全屏显示
			//View rootView= LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main,null);
			//mPop.showAtLocation(rootView, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0);
			//设置在购物车按钮上方显示
			mPop.showAsDropDown(v,0,5);
			popPupWindow_is_show=true;
		}
		else {
			mPop.dismiss();
			popPupWindow_is_show=false;
		}
	}

	//付钱PopupWindow
	private CustomPopupWindowPayMoney mCustomPopupWindowPayMoney;
	//点击付钱
	public void payMoney(View v)
	{
		mCustomPopupWindowPayMoney=new CustomPopupWindowPayMoney(this);
		View rootView= LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main,null);
		mCustomPopupWindowPayMoney.showAtLocation(rootView, Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL,0,0);
	}

	private LeftMenu mLeftMenu;

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		    case R.id.showleftMenu:
				mLeftMenu = new LeftMenu(this);
				View rootView = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main, null);
				mLeftMenu.showAtLocation(rootView, Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
				break;
		}
	}

	//检查网络是否可用
	public boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo=mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}
}