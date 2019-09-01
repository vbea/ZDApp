package com.binxin.zdapp.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.DialogInterface;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.view.KeyEvent;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.widget.ImageView;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;

import com.binxin.zdapp.R;
import com.binxin.zdapp.style.MyPageTransformer;
import com.binxin.zdapp.view.VerticalViewPager;
import com.binxin.zdapp.view.VerticalPagerAdapter;
import com.binxin.zdapp.view.viewpager.VerPageIndicator;
import com.binxin.zdapp.classes.ExceptionHandler;

public class Welcome extends Activity 
{
	private View view1, view2, view3, view4, view5, /*view6,*/ currentViewPager;
	private VerticalViewPager viewPager;
	private ArrayList<View> viewList;
	private ImageView mWelcomeImg,wel1_img,wel2_img1,wel2_img2,wel3_img1,wel3_img2,wel3_img3,wel3_img4;
	private TextView wel1_txt1,wel1_txt2,wel1_txt3,wel1_txt4,wel2_txt1,wel2_txt2,wel3_txt1,wel3_txt2,wel4_txt1,wel4_txt2,wel4_txt3,wel4_txt4;
	private Button /*btn_goon,*/btn_skip;
	private VerPageIndicator wel_cpi;
	private boolean anim_state_1 = true,anim_state_2 = true,anim_state_3 = true,anim_state_4 = true;
	private Animation img_slump,img_shake1,img_shake2,img_tran1,img_tran2,img_tran3,img_tran4,img_trans1,img_trans2,txt_trans1,txt_trans2,txt_alpha1,txt_alpha2,txt_scale;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		viewPager = (VerticalViewPager) findViewById(R.id.viewpager);
		wel_cpi = (VerPageIndicator) findViewById(R.id.wel_indicator);
		mWelcomeImg = (ImageView) findViewById(R.id.welcome_img);
		//welText = (TextView) findViewById(R.id.txt_welText);
		//btn_goon = (Button) findViewById(R.id.btn_welgo);
		btn_skip = (Button) findViewById(R.id.btn_skip);
		//btn_goon.setOnClickListener(new OnClick());
		btn_skip.setOnClickListener(new OnClick());
		//AssetManager mgr = getAssets();//得到AssetManager
		//Typeface tf = Typeface.createFromAsset(mgr, "fonts/welcome.ttc");//根据路径得到Typeface
		//welText.setTypeface(tf);//设置字体 
		WelcomeView();
		//viewPager.setPageTransformer(true,new MyPageTransformer());
		initAnimation();
	}
	//定义viewpager
	private void WelcomeView()
	{
		LayoutInflater lf = getLayoutInflater().from(Welcome.this);
		view1 = lf.inflate(R.layout.ic_vbea_zdapp_1, null);
		view2 = lf.inflate(R.layout.ic_vbea_zdapp_2, null);
		view3 = lf.inflate(R.layout.ic_vbea_zdapp_3, null);
		view4 = lf.inflate(R.layout.ic_vbea_zdapp_4, null);
		view5 = lf.inflate(R.layout.ic_vbea_zdapp_5, null);
		
		// 将要分页显示的View装入数组中
		viewList = new ArrayList<View>(6);
		viewList.add(view1);
		viewList.add(view2);
		viewList.add(view3);
		viewList.add(view4);
		viewList.add(view5);
		//viewList.add(view6);
		viewPager.setCurrentItem(0);
		//viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		
		//定义页面设置
		VerticalPagerAdapter pagerAdapter = new VerticalPagerAdapter()
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
			public void setPrimaryItem(ViewGroup container, int position, Object object)
			{
				currentViewPager = (View) object;
			}
			/*public CharSequence getPageTitle(int position)
			{
				return viewList.get(position);
			}*/
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
				wel1_img = (ImageView) view1.findViewById(R.id.wel1_img1);
				wel1_txt1 = (TextView) view1.findViewById(R.id.wel1_txt1);
				wel1_txt2 = (TextView) view1.findViewById(R.id.wel1_txt2);
				wel1_txt3 = (TextView) view1.findViewById(R.id.wel1_txt3);
				wel1_txt4 = (TextView) view1.findViewById(R.id.wel1_txt4);
				wel2_img1 = (ImageView) view2.findViewById(R.id.wel2_img1);
				wel2_img2 = (ImageView) view2.findViewById(R.id.wel2_img2);
				wel2_txt1 = (TextView) view2.findViewById(R.id.wel2_txt1);
				wel2_txt2 = (TextView) view2.findViewById(R.id.wel2_txt2);
				wel3_img1 = (ImageView) view3.findViewById(R.id.wel3_img1);
				wel3_img2 = (ImageView) view3.findViewById(R.id.wel3_img2);
				wel3_img3 = (ImageView) view3.findViewById(R.id.wel3_img3);
				wel3_img4 = (ImageView) view3.findViewById(R.id.wel3_img4);
				wel3_txt1 = (TextView) view3.findViewById(R.id.wel3_txt1);
				wel3_txt2 = (TextView) view3.findViewById(R.id.wel3_txt2);
				wel4_txt1 = (TextView) view4.findViewById(R.id.wel4_txt1);
				wel4_txt2 = (TextView) view4.findViewById(R.id.wel4_txt2);
				wel4_txt3 = (TextView) view4.findViewById(R.id.wel4_txt3);
				wel4_txt4 = (TextView) view4.findViewById(R.id.wel4_txt4);
				return viewList.get(position);
			}
		};
		viewPager.setAdapter(pagerAdapter);
		wel_cpi.setViewPager(viewPager);
		wel_cpi.setOnPageChangeListener(new MyOnPageChangeListener());
	}
	
	@Override
	public class MyOnPageChangeListener implements VerticalViewPager.OnPageChangeListener
	{
		public void onPageScrollStateChanged(int arg0) 
		{
			
		}
		public void onPageScrolled(int arg0, float arg1, int arg2) 
		{
			
		}
		public void onPageSelected(int arg0) 
		{
			switch (arg0)
			{
				case 0:
					
					break;
				case 1:
					if (anim_state_2)
					{
						anim_02();
						anim_state_2 = false;
					}
					break;
				case 2:
					if (anim_state_3)
					{
						anim_03();
						anim_state_3 = false;
					}
					break;
				case 3:
					if (anim_state_4)
					{
						anim_04();
						anim_state_4 = false;
					}
					break;
				case 4:
					welcome();
					break;
				default:
					anim_state_1 = anim_state_2 = anim_state_3 = anim_state_4 = false;
					break;
			}
		}
    }
	
	//初始化动画
	private void initAnimation()
	{
		mWelcomeImg.setBackgroundResource(R.anim.welcome_down);
		AnimationDrawable anim = (AnimationDrawable) mWelcomeImg.getBackground();
		anim.start();
		//AnimationUtils
		img_slump = AnimationUtils.loadAnimation(this, R.anim.slump);
		img_shake1 = AnimationUtils.loadAnimation(this,R.anim.shake1);
		img_shake2 = AnimationUtils.loadAnimation(this,R.anim.shake2);
		img_trans1 = AnimationUtils.loadAnimation(this,R.anim.ltright);
		img_trans2 = AnimationUtils.loadAnimation(this,R.anim.gtleft);
		img_tran1 = AnimationUtils.loadAnimation(this,R.anim.ltdown);
		img_tran2 = AnimationUtils.loadAnimation(this,R.anim.gtdown);
		img_tran3 = AnimationUtils.loadAnimation(this,R.anim.leftup);
		img_tran4 = AnimationUtils.loadAnimation(this,R.anim.rightup);
		//TextView Animation
		txt_trans1 = AnimationUtils.loadAnimation(this,R.anim.translate);
		txt_trans2 = AnimationUtils.loadAnimation(this,R.anim.translate);
		txt_alpha1 = AnimationUtils.loadAnimation(this,R.anim.ui_alpha);
		txt_alpha2 = AnimationUtils.loadAnimation(this,R.anim.ui_alpha);
		txt_scale = AnimationUtils.loadAnimation(this,R.anim.scale);
		if (anim_state_1)
		{
			anim_01();
			anim_state_1 = false;
		}
	}

	private class OnClick implements OnClickListener
	{
		public void onClick(View v)
		{
			welcome();
		}
	}
	
	private void welcome()
	{
		SharedPreferences welcome = getSharedPreferences("zdapp", Context.MODE_PRIVATE);
		int velc = welcome.getInt("verCode", 0);
		if (velc == getResources().getInteger(R.integer.versionCode))
		{
			finish();
		}
		else
		{
			Intent in = new Intent(Welcome.this,Main.class);
			startActivity(in);
			Welcome.this.finish();
			//overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
		}
	}
	
	private void anim_01()
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					Thread.sleep(500);
					mHandler.sendEmptyMessage(1);
					Thread.sleep(400);
					int w = 2;
					for (int i = 0;i < 5;i++)
					{
						mHandler.sendEmptyMessage(w);
						w = (w==2?3:2);
						Thread.sleep(90);
					}
					mHandler.sendEmptyMessage(4);
					Thread.sleep(800);
					mHandler.sendEmptyMessage(5);
					Thread.sleep(800);
					mHandler.sendEmptyMessage(6);
					Thread.sleep(500);
					mHandler.sendEmptyMessage(7);
				}
				catch (InterruptedException e)
				{
					ExceptionHandler.log("welcome_page1:"+e.toString());
				}
			}
		}).start();
	}
	
	private void anim_02()
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					Thread.sleep(200);
					mHandler.sendEmptyMessage(8);
					mHandler.sendEmptyMessage(9);
					Thread.sleep(800);
					mHandler.sendEmptyMessage(10);
					Thread.sleep(500);
					mHandler.sendEmptyMessage(11);
				}
				catch (InterruptedException e)
				{
					ExceptionHandler.log("welcome_page2:"+e.toString());
				}
			}
		}).start();
	}
	
	private void anim_03()
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					Thread.sleep(200);
					mHandler.sendEmptyMessage(12);
					mHandler.sendEmptyMessage(13);
					mHandler.sendEmptyMessage(14);
					mHandler.sendEmptyMessage(15);
					Thread.sleep(800);
					mHandler.sendEmptyMessage(16);
					Thread.sleep(500);
					mHandler.sendEmptyMessage(17);
				}
				catch (InterruptedException e)
				{
					ExceptionHandler.log("welcome_page3:"+e.toString());
				}
			}
		}).start();
	}
	
	private void anim_04()
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					Thread.sleep(200);
					mHandler.sendEmptyMessage(18);
					Thread.sleep(500);
					mHandler.sendEmptyMessage(19);
					Thread.sleep(500);
					mHandler.sendEmptyMessage(20);
					Thread.sleep(800);
					mHandler.sendEmptyMessage(21);
				}
				catch (InterruptedException e)
				{
					ExceptionHandler.log("welcome_page4:"+e.toString());
				}
			}
		}).start();
	}
	
	Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case 1:
					wel1_img.setVisibility(View.VISIBLE);
					wel1_img.startAnimation(img_slump);
					break;
				case 2:
					wel1_img.startAnimation(img_shake1);
					break;
				case 3:
					wel1_img.startAnimation(img_shake2);
					break;
				case 4:
					wel1_txt1.setVisibility(View.VISIBLE);
					wel1_txt1.startAnimation(txt_alpha1);
					break;
				case 5:
					wel1_txt2.setVisibility(View.VISIBLE);
					wel1_txt2.startAnimation(txt_alpha2);
					break;
				case 6:
					wel1_txt3.setVisibility(View.VISIBLE);
					wel1_txt3.startAnimation(txt_trans1);
					break;
				case 7:
					wel1_txt4.setVisibility(View.VISIBLE);
					wel1_txt4.startAnimation(txt_trans2);
					break;
				case 8:
					wel2_img1.setVisibility(View.VISIBLE);
					wel2_img1.startAnimation(img_trans1);
					break;
				case 9:
					wel2_img2.setVisibility(View.VISIBLE);
					wel2_img2.startAnimation(img_trans2);
					break;
				case 10:
					wel2_txt1.setVisibility(View.VISIBLE);
					wel2_txt1.startAnimation(txt_trans1);
					break;
				case 11:
					wel2_txt2.setVisibility(View.VISIBLE);
					wel2_txt2.startAnimation(txt_trans2);
					break;
				case 12:
					wel3_img1.setVisibility(View.VISIBLE);
					wel3_img1.startAnimation(img_tran1);
					break;
				case 13:
					wel3_img2.setVisibility(View.VISIBLE);
					wel3_img2.startAnimation(img_tran2);
					break;
				case 14:
					wel3_img3.setVisibility(View.VISIBLE);
					wel3_img3.startAnimation(img_tran3);
					break;
				case 15:
					wel3_img4.setVisibility(View.VISIBLE);
					wel3_img4.startAnimation(img_tran4);
					break;
				case 16:
					wel3_txt1.setVisibility(View.VISIBLE);
					wel3_txt1.startAnimation(txt_trans1);
					break;
				case 17:
					wel3_txt2.setVisibility(View.VISIBLE);
					wel3_txt2.startAnimation(txt_trans2);
					break;
				case 18:
					wel4_txt1.setVisibility(View.VISIBLE);
					wel4_txt1.startAnimation(txt_trans1);
					break;
				case 19:
					wel4_txt2.setVisibility(View.VISIBLE);
					wel4_txt2.startAnimation(txt_trans2);
					break;
				case 20:
					wel4_txt3.setVisibility(View.VISIBLE);
					wel4_txt3.startAnimation(txt_alpha1);
					break;
				case 21:
					wel4_txt4.setVisibility(View.VISIBLE);
					wel4_txt4.startAnimation(txt_scale);
					break;
			}
			super.handleMessage(msg);
		}
	};
	
	@Override
	protected void onPause()
	{
		super.onPause();
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == event.KEYCODE_BACK && event.getAction() == event.ACTION_DOWN)
		{
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
}
