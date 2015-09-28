package com.binxin.zdapp.activity;

import java.io.File;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.view.Gravity;
import android.view.ViewTreeObserver;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.graphics.Color;

import com.binxin.zdapp.R;
import com.binxin.zdapp.classes.MyThemes;
import com.binxin.zdapp.view.CustomTextView;
import com.binxin.zdapp.classes.ExceptionHandler;

public class App_audiotest extends Activity 
{
	private EditText nameText;
	private CustomTextView musicName;
	private TextView Textpaint,musicText,duration1,duration2;
	private Button btn_play, btn_stop;
	private String path;
	private MediaPlayer mediaPlayer;
	private LinearLayout titleLayout;
	private ProgressBar pbDuration;
	private int param = 1;
	private boolean tree = false,isPause = false,isPlay = false;
	private final SimpleDateFormat msd = new SimpleDateFormat("mm:ss");
	private mThread mthread;
	private String mTest = "00:00";
	public static boolean isSelectFile = false;
	public static String selectFilePath = "";
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.audiotest);

		mediaPlayer = new MediaPlayer();
		nameText = (EditText) findViewById(R.id.filename);
		Textpaint = (TextView)findViewById(R.id.TextTest1);
		musicName = (CustomTextView)findViewById(R.id.TextTest2);
		musicText = (TextView) findViewById(R.id.txtMusicName);
		duration1 = (TextView) findViewById(R.id.adtxt_duration);
		duration2 = (TextView) findViewById(R.id.duration_total);
		pbDuration = (ProgressBar) findViewById(R.id.audiotestProgressBar);
		ImageButton back = (ImageButton) findViewById(R.id.btn_close);
		ImageButton btn_selectfile = (ImageButton) findViewById(R.id.atbtnFile);
		btn_play = (Button) findViewById(R.id.playbutton);
		btn_stop = (Button) findViewById(R.id.stopbutton);
		titleLayout = (LinearLayout) findViewById(R.id.themeLayout);
		musicName.init(this.getWindowManager());
		//musicName.setEnabled(false);
		//setMyTheme();
		back.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				onDestroy();
				finish();
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
		btn_selectfile.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				startActivity(new Intent(App_audiotest.this, SelectAudio.class));
			}
		});
		btn_play.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				isSelectFile = false;
				if (!isPlay)
				{
					String filename = nameText.getText().toString().trim();
					File audio = new File(Environment.getExternalStorageDirectory(), filename);
					if (filename.length() == 0)
					{
						Toast.makeText(getApplicationContext(),"文件名不能为空！",Toast.LENGTH_SHORT).show();
					}
					else if (audio.exists())
					{
						mthread = new mThread(true);
						AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
						audioManager.setMode(AudioManager.MODE_IN_CALL);// 把模式调成听筒放音模式
						path = audio.getAbsolutePath();
						play(false);
						// 设置音频循环播放
						mediaPlayer.setLooping(true);
						if(mediaPlayer.isPlaying() || mediaPlayer.isLooping())
						{
							Textpaint.setText("正在通过听筒播放音乐……");
							Textpaint.setTextColor(getResources().getColor(R.color.limegreen));
							musicName.setText(path);
							musicText.setText(path);
							//musicName.setGravity(Gravity.CENTER);
							musicName.init(getWindowManager());
							//musicName.setTxtColor(getResources().getColor(R.color.dodgerblue));
							musicName.setEnabled(false);
							btn_play.setText(R.string.pause);
							isPlay = true;
							getScoll();
						}
					}
					else
					{
						String errText = getResources().getString(R.string.filenoexist);
						path = audio.getAbsolutePath();
						Toast.makeText(getApplicationContext(), "文件“"+path+"”"+errText, Toast.LENGTH_SHORT).show();
						path = null;
					}
				}
				else
				{
					if (isPause)
					{
						btn_play.setText(R.string.pause);
						isPause = false;
						play(true);
						if (tree)
							musicName.startScroll();
					}
					else
					{
						isPause = true;
						mediaPlayer.pause();
						btn_play.setText(R.string.Continue);
						mthread.setPlayed(false);
						if (tree)
							musicName.stopScroll();
					}
				}
			}
		});
		btn_stop.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				if (isPlay)
				{
					mediaPlayer.stop();
					Textpaint.setText("已停止播放");
					Textpaint.setTextColor(android.graphics.Color.RED);
					AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
					audioManager.setMode(AudioManager.MODE_NORMAL);
					musicName.setText("");
					musicText.setText("");
					musicName.stopScroll();
					mthread.interrupt();
					mthread = null;
					//Txtduration.setText("");
					btn_play.setText(R.string.playbutton);
					duration1.setText(mTest);
					duration2.setText(mTest);
					pbDuration.setProgress(0);
				}
				else
				{
					Textpaint.setText("请输入文件名并点击播放按钮来测试");
					Textpaint.setTextColor(android.graphics.Color.BLUE);
					//Toast.makeText(getApplicationContext(),"当前未在播放，无需停止",Toast.LENGTH_SHORT).show();
				}
				isPause = false;
				isPlay = false;
			}
		});
	}
	private void play(boolean isPaused) 
	{
		try
		{
			if (!isPaused)
			{
				mediaPlayer.reset();// 把各项参数恢复到初始状态
				mediaPlayer.setDataSource(path);
				mediaPlayer.prepare();// 进行缓冲
				mediaPlayer.setOnPreparedListener(new PrepareListener(0));
			}
			else
			{
				mediaPlayer.start();
			}
			mthread.setPlayed(true);
			mthread.start();
		}
		catch (Exception e)
		{
			ExceptionHandler.log("Audio Test:Play - "+e.toString());
		}
	}
	private final class PrepareListener implements OnPreparedListener
	{
		private int position;
		public PrepareListener(int position)
		{
			this.position = position;
		}
		public void onPrepared(MediaPlayer mp)
		{
			mediaPlayer.start();// 开始播放
			if (position > 0)
			{
				mediaPlayer.seekTo(position);
			}
		}
	}

	private void getScoll()
	{
		ViewTreeObserver vto = musicName.getViewTreeObserver();
		vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener()
		{
			public boolean onPreDraw()
			{
				//int height = musicName.getMeasuredHeight();
				int width = musicName.getMeasuredWidth();
				//Toast.makeText(getApplicationContext(),"实际宽度:"+musicText.getMeasuredWidth()+"滚动宽度:"+width+","+tree,Toast.LENGTH_SHORT).show();
				tree = (width >= Textpaint.getWidth());
				if (tree)
				{
					musicName.setVisibility(View.VISIBLE);
					musicText.setText("");
					musicName.startScroll();
				}
				else
				{
					musicName.stopScroll();
					musicName.setVisibility(View.INVISIBLE);
				}
				return true;
			}
		});
	}
	Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case 1:
					if (isPlay)
					{
						if (pbDuration.getMax() != mediaPlayer.getDuration())
							pbDuration.setMax(mediaPlayer.getDuration());
						duration1.setText(msd.format(mediaPlayer.getCurrentPosition()));
						duration2.setText(msd.format(mediaPlayer.getDuration()));
						pbDuration.setProgress(mediaPlayer.getCurrentPosition());
					}
					else
					{
						duration1.setText(mTest);
						duration2.setText(mTest);
						pbDuration.setProgress(0);
						//mTest += "已经停止";
					}
					break;
			}
		}	
	};
	
	public class mThread extends Thread
	{
		boolean _play = false;
		boolean cancellable = true;
		public mThread(boolean thm)
		{
			_play = thm;
		}
		public void setPlayed(boolean pl)
		{
			_play = pl;
		}
		@Override
		public void run()
		{
			while (cancellable)
			{
				try
				{
					if (_play)
					{
						handler.sendEmptyMessage(1);
						sleep(500);
					}
				}
				catch (InterruptedException e)
				{
					ExceptionHandler.log("Audio Test:Thread - "+e.toString());
				}
			}
		}

		@Override
		public void interrupt()
		{
			cancellable = false;
			super.interrupt();
		}
	};
	
	public void setMyTheme()
	{
		SharedPreferences sube = getSharedPreferences("zdapp", Context.MODE_PRIVATE);
		int code = sube.getInt("themeCode", 0);
		MyThemes.setThemes(this, titleLayout, code);
	}
	@Override
	protected void onResume()
	{
		setMyTheme();
		if (isSelectFile && !selectFilePath.equals(""))
		{
			if (isPlay || isPause)
			{
				mediaPlayer.stop();
				Textpaint.setText("请点击播放按钮来播放");
				Textpaint.setTextColor(android.graphics.Color.BLUE);
				AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
				audioManager.setMode(AudioManager.MODE_NORMAL);
				musicName.setText("");
				musicText.setText("");
				musicName.stopScroll();
				mthread.interrupt();
				mthread = null;
				//Txtduration.setText("");
				btn_play.setText(R.string.playbutton);
				duration1.setText(mTest);
				duration2.setText(mTest);
				pbDuration.setProgress(0);
				isPause = false;
				isPlay = false;
			}
			nameText.setText(selectFilePath);
		}
		super.onResume();
	}
	@Override
	protected void onPause()
	{
		super.onPause();
	}

	@Override
	protected void onStop()
	{
		// TODO: Implement this method
		super.onStop();
	}

	@Override
	protected void onRestart()
	{
		// TODO: Implement this method
		super.onRestart();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		outState.putInt("param", param);
		// TODO: Implement this method
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onDestroy()
	{
		isSelectFile = false;
		selectFilePath = "";
		if (isPlay)
			mediaPlayer.stop();
		if (mediaPlayer != null)
		{
			mediaPlayer.release();
			mediaPlayer = null;
		}
		isPlay = false;
		if (mthread != null)
			mthread.interrupt();
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == event.KEYCODE_BACK && event.getAction() == event.ACTION_DOWN)
		{
			onDestroy();
			finish();
			overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
		}
		return super.onKeyDown(keyCode, event);
	}

}
