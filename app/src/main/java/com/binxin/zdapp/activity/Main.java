package com.binxin.zdapp.activity;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.MotionEvent;
import android.view.KeyEvent;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ListView.LayoutParams;
import android.widget.PopupWindow;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toolbar;
import android.graphics.Color;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;

import com.qq.e.ads.banner.ADSize;
import com.qq.e.ads.banner.AbstractBannerADListener;
import com.qq.e.ads.banner.BannerView;
import com.qq.e.ads.banner.BannerADListener;
import com.tencent.stat.StatService;

import com.binxin.zdapp.R;
import com.binxin.zdapp.classes.MyThemes;
import com.binxin.zdapp.view.BadgeView;
import com.binxin.zdapp.view.viewpager.CirclePageIndicator;
import com.binxin.zdapp.classes.ExceptionHandler;
import com.binxin.zdapp.style.MyPageTransformer;
import com.binxin.zdapp.classes.Common;
import com.umeng.update.UmengUpdateAgent;

public class Main extends Activity 
{
	private View view1, view2;
	private ViewPager viewPager;
	//private PagerTitleStrip pagerTitleStrip;
	private PagerTabStrip pagerTabStrip;
	private ArrayList<String> titleList;
	private ArrayList<View> viewList;
	private Button filem, smtxl, compass, calculator, app_light, app_hard, app_timer, app_widget, app_sweep,app_cdecode,countdown;
	private ImageButton mbtn,btn_skin;
	private CirclePageIndicator indicator;
	private long exitTime = 0;
	private PopupWindow popWindow;
	private RelativeLayout bannerContainer,homePage;
	private LinearLayout titleLayout,homepage1,homepage2,menuBarView;
	private GestureDetector mGestureDetector = null;
	private BadgeView badge1;
	private int popCode = 0;
	private boolean blnTouch;
	private AlertDialog.Builder update;
	private BannerView bannerView;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		try
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.home);
			StatService.trackCustomEvent(this, "onCreate", "");
			bannerContainer = (RelativeLayout)findViewById(R.id.ad_container);
			indicator = (CirclePageIndicator) findViewById(R.id.indicator);
			homePage = (RelativeLayout) findViewById(R.id.homeRelativeLayout);
			titleLayout = (LinearLayout) findViewById(R.id.themeLayout);
			menuBarView = (LinearLayout) findViewById(R.id.homeMenuBarLayout);
			btn_skin = (ImageButton) findViewById(R.id.btn_skins);
			//Toolbar tool = (Toolbar) findViewById(R.id.themeLayout);
			//setActionBar(tool);
			initView();
			poppWindow();
			onAdStart();
			setBackground();
		}
		catch(Exception e)
		{
			onDestroys();
			Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
		}
		btn_skin.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				startActivity(new Intent(Main.this,Set_theme.class));
				//overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			}
		});
	}
	public void onAdStart()
	{
		final SharedPreferences sube6 = getSharedPreferences("zdapp", Context.MODE_PRIVATE);
		if(sube6.getBoolean("banner", true))
		{
			if (bannerView == null)
			{
				bannerContainer.setVisibility(View.VISIBLE);
				bannerView = new BannerView(this, ADSize.BANNER, "1150078134", "9079537216925334074");
				bannerView.setRefresh(30);
				bannerContainer.addView(bannerView);
			}
			bannerView.loadAD();
		}
		else
		{
			bannerContainer.setVisibility(View.GONE);
		}
		if (sube6.getInt("verCode", 0) != getResources().getInteger(R.integer.versionCode))
		{
			Editor edt = sube6.edit();
			edt.putInt("verCode", getResources().getInteger(R.integer.versionCode));
			if (edt.commit())
				mHandler.sendEmptyMessageDelayed(0,2000);
		}
		else
			mHandler.sendEmptyMessageDelayed(1,2000);
	}
	//定义viewpager
	private void initView()
	{
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		//pagerTitleStrip = (PagerTitleStrip) findViewById(R.id.pagertitle);
		pagerTabStrip=(PagerTabStrip) findViewById(R.id.pagertab);
		pagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.title)); 
		pagerTabStrip.setDrawFullUnderline(false);
		//pagerTabStrip.setBackgroundColor(getResources().getColor(R.color.azure));
		pagerTabStrip.setTextSpacing(50);
		pagerTabStrip.setTextColor(getResources().getColor(R.color.white));
		/*
		*注：此处不能定义viewpager的控件
		*/
		//viewPager.setPageTransformer(true,new MyPageTransformer());
		
		LayoutInflater lf = getLayoutInflater().from(Main.this);
		view1 = lf.inflate(R.layout.ic_zdapp_1, null);
		view2 = lf.inflate(R.layout.ic_zdapp_2, null);
		//view3 = lf.inflate(R.layout.ic_zdapp_3, null);
		// 将要分页显示的View装入数组中
		viewList = new ArrayList<View>(2);
		viewList.add(view1);
		viewList.add(view2);
		//viewList.add(view3);
		viewPager.setCurrentItem(0);
		//viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		// 每个页面的Title数据
		titleList = new ArrayList<String>(2);
		titleList.add("日常应用");
		//titleList.add("生活实用");
		titleList.add("工具箱");
		
		//定义页面设置
		PagerAdapter pagerAdapter = new PagerAdapter()
		{
			@Override
			public boolean isViewFromObject(View arg0, Object arg1)
			{
				return arg0 == arg1;
			}
			@Override
			public int getCount()
			{
				return viewList.size();
			}
			@Override
			public void destroyItem(ViewGroup container, int position, Object object)
			{
				container.removeView(viewList.get(position));
			}
			@Override
			public int getItemPosition(Object object)
			{
				return super.getItemPosition(object);
			}
			@Override
			public CharSequence getPageTitle(int position)
			{
				return titleList.get(position);
			}
			//定义页面切换函数
			//@Override
			public ViewPager setOnPageChangeListener()
			{
				return setOnPageChangeListener();
			}
			//配置页面控件
			@Override
			public Object instantiateItem(ViewGroup container, int position)
			{
				container.addView(viewList.get(position));
				filem = (Button) view1.findViewById(R.id.files);
				smtxl = (Button) view1.findViewById(R.id.smtxl);
				compass = (Button)view1.findViewById(R.id.compass);
				calculator = (Button)view2.findViewById(R.id.calculator);
				app_light = (Button)view1.findViewById(R.id.light);
				app_sweep = (Button)view2.findViewById(R.id.appbmi);
				app_hard = (Button)view2.findViewById(R.id.hard);
				app_timer = (Button)view2.findViewById(R.id.timer);
				app_widget = (Button)view1.findViewById(R.id.app_widget);
				app_cdecode = (Button)view2.findViewById(R.id.credecode);
				countdown = (Button) view2.findViewById(R.id.app_countdown);
				homepage1 = (LinearLayout)view1.findViewById(R.id.homePage_layout1);
				homepage2 = (LinearLayout)view2.findViewById(R.id.homePage_layout2);
				
				touchmode();
				//TextView title = (TextView) findViewById(R.id.textView01);
				//Animation main_in = AnimationUtils.loadAnimation(Main.this, R.anim.push_left_in);
				//title.startAnimation(main_in);
				app_light.setOnClickListener(new OnClickListener()
				{
					public void onClick(View v)
					{
						SharedPreferences sube = getSharedPreferences("torch", Context.MODE_PRIVATE);
						int mFlash = sube.getInt("flashCode", 1);
						if (mFlash == 0)
						{
							beginActivity(new Intent(Main.this,App_torch.class));
						}
						else
						{
							beginActivity(new Intent(Main.this,App_colorLight.class));
						}
					}
				});
				app_sweep.setOnClickListener(new OnClickListener()
				{
					public void onClick(View v)
					{
						beginActivity(new Intent(Main.this,App_bmi.class));
					}
				});
				app_hard.setOnClickListener(new OnClickListener()
				{
					public void onClick(View v)
					{
						beginActivity(new Intent(Main.this,App_hardware.class));
					}
				});
				app_timer.setOnClickListener(new OnClickListener()
				{
					public void onClick(View v)
					{
						beginActivity(new Intent(Main.this,App_timer.class));
					}
				});
				filem.setOnClickListener(new OnClickListener()
				{
					public void onClick(View v)
					{
						beginActivity(new Intent(Main.this,App_filemanager.class));
					}
				});
				smtxl.setOnClickListener(new OnClickListener()
				{
					public void onClick(View v)
					{
						SharedPreferences sube2 = getSharedPreferences("Zdapp_MyContact", Context.MODE_PRIVATE);
						boolean mPsd = sube2.getBoolean("passWord", false);
						if (mPsd)
							startActivity(new Intent(Main.this,Password_on.class));
								//overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
						else
							beginActivity(new Intent(Main.this,App_contact.class));
					}
				});
				compass.setOnClickListener(new OnClickListener()
				{
					public void onClick(View v)
					{
						beginActivity(new Intent(Main.this,App_compass.class));
					}
				});
				calculator.setOnClickListener(new OnClickListener()
				{
					public void onClick(View v)
					{
						beginActivity(new Intent(Main.this,App_scale.class));
					}
				});
				app_widget.setOnClickListener(new OnClickListener()
				{
					public void onClick(View v)
					{
						beginActivity(new Intent(Main.this,App_robotChat.class));
					}
				});
				app_cdecode.setOnClickListener(new OnClickListener()
				{
					public void onClick(View v)
					{
						beginActivity(new Intent(Main.this,App_cdecode.class));
					}
				});
				countdown.setOnClickListener(new OnClickListener()
				{
					public void onClick(View v)
					{
						beginActivity(new Intent(Main.this,App_countDown.class));
					}
				});
				
				return viewList.get(position);
			}
		};
		viewPager.setAdapter(pagerAdapter);
		indicator.setViewPager(viewPager);
	}

	public void beginActivity(Intent _intent)
	{
		try
		{
			startActivity(_intent);
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			//overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		}
		catch (Exception e)
		{
			Common.showShortToast(getApplicationContext(), "抱歉，暂时不能运行该程序！");
		}
	}
	
	public void popActivity(Intent _intent)
	{
		try
		{
			startActivity(_intent);
			//overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			popDismiss();
		}
		catch (Exception e)
		{
			Common.showShortToast(getApplicationContext(), "抱歉，暂时不能运行该程序！");
		}
	}
	
	public void touchmode()
	{
		mGestureDetector = new GestureDetector(this,new myOnGestureListener());
		homepage1.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				mGestureDetector.onTouchEvent(event);
				return blnTouch;
			}
		});
		homepage2.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				mGestureDetector.onTouchEvent(event);
				return blnTouch;
			}
		});
	}
	public void poppWindow()
	{
		mbtn = (ImageButton) findViewById(R.id.btn_title);
		LayoutInflater inflater = LayoutInflater.from(this);
		// 引入窗口配置文件   
		View popview = inflater.inflate(R.layout.options, null);
		// 创建PopupWindow对象
		popWindow = new PopupWindow(popview, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, false);
		// 需要设置一下此参数，点击外边可消失 
		popWindow.setBackgroundDrawable(new BitmapDrawable());
		//设置点击窗口外边窗口消失 
		popWindow.setOutsideTouchable(true);
		// 设置此参数获得焦点，否则无法点击   
		popWindow.setFocusable(true);
		popWindow.setOnDismissListener(new PopupWindow.OnDismissListener()
		{
			@Override
			public void onDismiss()
			{
				menuBarView.setVisibility(View.VISIBLE);
			}
		});
		
		badge1 = new BadgeView(this, titleLayout);
		badge1.setText("•");
		badge1.setBackgroundColor(Color.TRANSPARENT);
		badge1.setTextColor(Color.RED);
		badge1.setBadgeMargin(3);
		badge1.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		mbtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(popWindow.isShowing())
				{
					// 隐藏窗口，如果设置了点击窗口外小时即不需要此方式隐藏
					popDismiss();
				}
				else
				{
					// 显示窗口   
					popShow();
				}
				//badge1.toggle();
			}
		});
		mbtn.setOnLongClickListener(new View.OnLongClickListener()
		{
			@Override
			public boolean onLongClick(View p1)
			{
				if(!popWindow.isShowing())
				{
					popShow();
				}
				return true;
			}
		});

		Button btn_setting = (Button) popview.findViewById(R.id.opt_settings);
		Button btn_about = (Button) popview.findViewById(R.id.opt_about);
		//Button btn_bxa = (Button) popview.findViewById(R.id.opt_bxastudy);
		Button btn_swetty = (Button) popview.findViewById(R.id.opt_sweety);
		Button btn_help = (Button) popview.findViewById(R.id.opt_help);
		Button btn_addi = (Button) popview.findViewById(R.id.opt_addition);
		//Button btn_myapp2 = (Button) popview.findViewById(R.id.opt_myapp_btn);
		Button btn_exit = (Button) popview.findViewById(R.id.opt_exit);
		LinearLayout popcv = (LinearLayout) popview.findViewById(R.id.popChildView);

		btn_setting.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				popActivity(new Intent(Main.this,Settings.class));
			}
		});
		btn_swetty.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				popActivity(new Intent(Main.this, App_decode.class));
			}
		});
		btn_addi.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				popActivity(new Intent(Main.this, App_additional.class));
			}
		});

		btn_help.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				popActivity(new Intent(Main.this,Help.class));
			}
		});
		btn_about.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				popActivity(new Intent(Main.this,About.class));
			}
		});

		btn_exit.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				onDestroys();
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
		popcv.setFocusableInTouchMode(true);//获取焦点
		popcv.setOnKeyListener(new OnKeyListener()
		{
			@Override
			public boolean onKey(View v,int keyCode,KeyEvent event)
			{
				if(keyCode == KeyEvent.KEYCODE_MENU && popWindow.isShowing())
				{
					if (popCode == 1)
						popDismiss();
					popCode = 1;
					return true;
				}
				return false;
			}
		});
	}
	public void popShow()
	{
		//设置位置
		popWindow.showAsDropDown(this.findViewById(R.id.homeHideView));//, Gravity.NO_GRAVITY,0,0); //设置在屏幕中的显示位置
		//popWindow.getSoftInputMode();
		menuBarView.setVisibility(View.INVISIBLE);
	}
	public void popDismiss()
	{
		popWindow.dismiss();
		menuBarView.setVisibility(View.VISIBLE);
	}
	/*@Override
	public class MyOnPageChangeListener implements OnPageChangeListener
	{
		public void onPageScrollStateChanged(int arg0) 
		{

		}
		public void onPageScrolled(int arg0, float arg1, int arg2) 
		{

		}
		public void onPageSelected(int arg0) 
		{
			
		}
    }*/
	Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
				case 0:
				{
					setTheme(android.R.style.Theme_DeviceDefault_Light_NoActionBar);
					update = new AlertDialog.Builder(Main.this);
					update.setTitle("新版特性");
					update.setMessage(R.string.newupdate);
					update.setPositiveButton(R.string.ok,new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog,int swich)
						{
							//dialog.cancel();
						}
					});
					update.create();
					update.show();
					break;
				}
				case 1:
					UmengUpdateAgent.update(Main.this);
					break;
			}
			super.handleMessage(msg);
		}
	};
	
	@Override
	protected void onResume()
	{
		super.onResume();
		onAdStart();
		setBackground();
		MyThemes.setThemes(this,titleLayout);
		SharedPreferences tochsp = getSharedPreferences("zdapp",Context.MODE_PRIVATE);
		blnTouch = tochsp.getBoolean("touch",true);
		StatService.onResume(this);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		StatService.onPause(this);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}
	
	protected void onDestroys()
	{
		//释放占用的内存资源
		finish();
		android.os.Process.killProcess(android.os.Process.myPid());
		super.onDestroy();
		//System.exit(0);
	}
	public int getBackgroundId()
	{
		SharedPreferences subes = getSharedPreferences("zdapp", Context.MODE_PRIVATE);
		return subes.getInt("imageCode", 0);
	}
	public void setBackgroundId(int id)
	{
		SharedPreferences subes = getSharedPreferences("zdapp", Context.MODE_PRIVATE);
		Editor edits = subes.edit();
		edits.putInt("imageCode", id);
		edits.commit();
	}
	public void setBackground()
	{
		int img = getBackgroundId();
		switch (img)
		{
			case 0:
				homePage.setBackgroundResource(R.drawable.background);
				break;
			case 1:
				homePage.setBackgroundResource(R.drawable.bg_image01);
				break;
			case 2:
				homePage.setBackgroundResource(R.drawable.bg_image02);
				break;
			case 3:
				homePage.setBackgroundResource(R.drawable.bg_image03);
				break;
			case 4:
				homePage.setBackgroundResource(R.drawable.bg_image04);
				break;
			case 5:
				homePage.setBackgroundResource(R.drawable.bg_image05);
				break;
			case 6:
				homePage.setBackgroundResource(R.drawable.bg_image06);
				break;
			case 7:
				homePage.setBackgroundResource(R.drawable.bg_image07);
				break;
			case 8:
				homePage.setBackgroundResource(R.drawable.bg_image08);
				break;
			case 9:
				homePage.setBackgroundResource(R.drawable.bg_image09);
				break;
			case 10:
				homePage.setBackgroundResource(R.drawable.bg_image10);
				break;
			case 11:
				homePage.setBackgroundResource(R.drawable.bg_image11);
				pagerTabStrip.setTextColor(getResources().getColor(R.color.tianyiblue));
				break;
		}
	}
	
	final class myOnGestureListener implements OnGestureListener
	{
		@Override
		public boolean onFling(MotionEvent p1, MotionEvent p2, float p3, float p4)
		{
			if (p2.getY() - p1.getY() >= 50)
			{
				//Toast.makeText(getApplicationContext(),"向下滑动",Toast.LENGTH_SHORT).show();
				if(getBackgroundId() == 0)
					setBackgroundId(11);
				else
					setBackgroundId(getBackgroundId()-1);
				setBackground();
				return true;
			}
			else if (p1.getY() - p2.getY() >= 50)
			{
				//Toast.makeText(getApplicationContext(),"向上滑动",Toast.LENGTH_SHORT).show();
				if(getBackgroundId() == 11)
					setBackgroundId(0);
				else
					setBackgroundId(getBackgroundId()+1);
				setBackground();
				return true;
			}
			//Toast.makeText(getApplicationContext(),p1.getX()+"|"+p2.getY()+"|"+p3+"|"+p4,Toast.LENGTH_SHORT).show();
			return true;
		}

		@Override
		public boolean onDown(MotionEvent p1)
		{
			// TODO: Implement this method
			return false;
		}

		@Override
		public boolean onScroll(MotionEvent p1, MotionEvent p2, float p3, float p4)
		{
			/*if (p2.getY() - p1.getY() > 100)
			{
				Toast.makeText(getApplicationContext(),"向上滑动1",Toast.LENGTH_SHORT).show();
				return true;
			}
			if (p2.getY() - p1.getY() < -100)
			{
				Toast.makeText(getApplicationContext(),"向下滑动1",Toast.LENGTH_SHORT).show();
				return true;
			}
			if (p4 - p3 > 100)
			{
				Toast.makeText(getApplicationContext(),"向下滑动2",Toast.LENGTH_SHORT).show();
				return true;
			}*/
			return false;
		}

		@Override
		public boolean onSingleTapUp(MotionEvent p1)
		{
			// TODO: Implement this method
			return false;
		}

		@Override
		public void onShowPress(MotionEvent p1)
		{
			// TODO: Implement this method
		}

		@Override
		public void onLongPress(MotionEvent p1)
		{
			// TODO: Implement this method
		}
		
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			if((System.currentTimeMillis() - exitTime) > 2000)
			{
				Common.showShortToast(Main.this, R.string.home_exit);
				exitTime = System.currentTimeMillis();
			}
			else
			{
				onDestroys();
			}
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_MENU)
		{
			//poppWindow();
			if(!popWindow.isShowing())
			{
				popCode = 0;
				popShow();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
