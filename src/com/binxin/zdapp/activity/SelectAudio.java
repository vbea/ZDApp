package com.binxin.zdapp.activity;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;

import android.app.AlertDialog;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Bundle;
import android.os.StatFs;
import android.os.Message;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.PopupWindow;
import android.net.Uri;

import com.binxin.zdapp.R;
import com.binxin.zdapp.classes.MD5Util;
import com.binxin.zdapp.classes.FilesText;
import com.binxin.zdapp.classes.FilesTextListAdapter;
import com.binxin.zdapp.classes.ExceptionHandler;
import com.binxin.zdapp.classes.ProgressClass;
import com.binxin.zdapp.classes.FileCommon;
import com.binxin.zdapp.classes.Common;
import com.binxin.zdapp.classes.MyThemes;

//正德应用6.7
//听筒测试文件选择器
//创建时间：2015年06月23日
//创建者：邠心
public class SelectAudio extends Activity
{
	private List<FilesText> mFilesList = new ArrayList<FilesText>();
	private File curDirectory = new File("");
	private LinearLayout titleLayout;
	private ListView mListView;
	private TextView title,hidTxt;
	private final FileCommon filecom = new FileCommon(this);
	private SharedPreferences spf;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setTheme(android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.audio_file);
		
		ImageButton back = (ImageButton) findViewById(R.id.btn_close);
		mListView = (ListView) findViewById(R.id.file_listView);
		titleLayout = (LinearLayout) findViewById(R.id.themeLayout);
		title = (TextView) findViewById(R.id.txt_fileTitle);
		hidTxt = (TextView) findViewById(R.id.audio_hidTxt);
		spf = getSharedPreferences("files", Context.MODE_PRIVATE);
		browseToSD();
		setMyTheme();
		
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> l, View v, int position, long id)
			{
				// 取得选中的一项的文件名
				String selectedFileString = mFilesList.get(position).getPath();
				if (selectedFileString.equals(getResources().getString(R.string.up_one_level)))
				{
					//返回上一级目录
					upOneLevel();
				}
				else
				{	
					File clickedFile = new File(selectedFileString);
					if(clickedFile != null)
						browseTo(clickedFile);
				}
			}
		});
		
		back.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				onDestroy();
				finish();
			}
		});
	}
	
	private void browseTo(final File file)
	{
		try
		{
			if (file.isDirectory())
			{
				setTitle(file);
				hidTxt.setText(file.getAbsolutePath());
				curDirectory = file;
				fill(file.listFiles());
			}
			else
			{
				String path = file.getAbsolutePath().replace(Environment.getExternalStorageDirectory().getAbsolutePath(),"");
				App_audiotest.isSelectFile = true;
				App_audiotest.selectFilePath = path.substring(1);
				savePath(file.getParent());
				finish();
			}
		}
		catch (Exception e)
		{
			ExceptionHandler.log(e.toString());
			setTitle(file);
			curDirectory = file;
			filltemp();
		}
	}
	
	private void savePath(String path)
	{
		SharedPreferences.Editor edt = spf.edit();
		edt.putString("audioPath", path);
		edt.commit();
	}
	
	//这里可以理解为设置ListActivity的源
	private void fill(File[] files)
	{
		List<FilesText> _fileDestry,_filesList = null;
		_fileDestry = new ArrayList<FilesText>();
		_filesList = new ArrayList<FilesText>();
		//清空列表
		this.mFilesList.clear();
		//如果不是根目录则添加上一级目录项
		if (this.curDirectory.getParent() != null && !this.curDirectory.equals(Environment.getExternalStorageDirectory()))
		{
			this.mFilesList.add(new FilesText(getString(R.string.up_one_level), getResources().getDrawable(R.drawable.folder), getString(R.string.up_txt_level)));
		}

		Drawable currentIcon = null;
		for (File currentFile : files)
		{
			if (currentFile.getName().substring(0,1).equals("."))
				continue;
			String details = "";
			/*if (currentFile.isDirectory())
				details = filecom.getFileDete(currentFile);
			else
				details = filecom.getFileDete(currentFile) + " " + formatSize(currentFile.length());*/
			//判断是一个文件夹还是一个文件
			if (currentFile.isDirectory())
			{
				currentIcon = getResources().getDrawable(R.drawable.folder);
				_fileDestry.add(new FilesText(currentFile.getName(), currentFile.getAbsolutePath(), currentIcon, details));
			}
			else
			{
				//取得文件名
				String fileName = currentFile.getName();
				//根据文件名来判断文件类型，设置不同的图标
				
				if (checkEndsWithInStringArray(fileName, getResources().getStringArray(R.array.fileEndingAudio)) || checkEndsWithInStringArray(fileName, getResources().getStringArray(R.array.fileEndingVideo)))
				{
					currentIcon = getResources().getDrawable(R.drawable.audio);
					_filesList.add(new FilesText(currentFile.getName(),currentFile.getAbsolutePath(), currentIcon, details));
				}
			}
		}
		//排序
		Collections.sort(_fileDestry);
		Collections.sort(_filesList);
		//列表显示
		this.mFilesList.addAll(_fileDestry);
		this.mFilesList.addAll(_filesList);
		//清空
		_filesList.clear();
		_fileDestry.clear();

		final FilesTextListAdapter ftla = new FilesTextListAdapter(getApplicationContext(),this);
		//将表设置到ListAdapter中
		ftla.setListItems(this.mFilesList);
		//为ListActivity添加一个ListAdapter
		mListView.setAdapter(ftla);
	}
	
	private void filltemp()
	{
		this.mFilesList.clear();
		this.mFilesList.add(new FilesText(getString(R.string.up_one_level), getResources().getDrawable(R.drawable.folder),getString(R.string.up_txt_level)));
		FilesTextListAdapter ftla = new FilesTextListAdapter(getApplicationContext(),this);
		//将表设置到ListAdapter中
		ftla.setListItems(this.mFilesList);
		//为ListActivity添加一个ListAdapter
		mListView.setAdapter(ftla);
	}
	
	//浏览文件系统SD卡目录
	private void browseToSD()
	{
		if (filecom.ExistSDCard())
		{
			browseTo(new File(spf.getString("audioPath",Environment.getExternalStorageDirectory().getAbsolutePath())));
		}
		else
		{
			Common.showShortToast(getApplicationContext(), "当前未挂载SD卡");
			finish();
		}
	}
	
	//通过文件名判断是什么类型的文件
	private boolean checkEndsWithInStringArray(String checkItsEnd, String[] fileEndings)
	{
		for(String aEnd : fileEndings)
		{
			if(checkItsEnd.endsWith(aEnd))
				return true;
		}
		return false;
	}
	
	/*private String formatSize(long Bytes)
	{
		return filecom.formatSize(Bytes, false);
	}*/
	
	//返回上一级目录
	private void upOneLevel()
	{
		if(this.curDirectory.getParent() != null)
			this.browseTo(this.curDirectory.getParentFile());
		else
			this.browseToSD();
	}
	
	//设置标题
	public void setTitle(File file)
	{
		title.setText(file.getName());
		if (file.getParent() != null)
		{
			if (file.equals(Environment.getExternalStorageDirectory()))
				title.setText(R.string.file_sd);
			else
				title.setText(file.getName());
		}
		else
		{
			title.setText(file.getPath());
		}
	}
	
	public void setMyTheme()
	{
		SharedPreferences sube = getSharedPreferences("zdapp", Context.MODE_PRIVATE);
		int code = sube.getInt("themeCode", 0);
		MyThemes.getThemes(titleLayout, code);
	}

	@Override
	protected void onDestroy()
	{
		App_audiotest.isSelectFile = false;
		savePath(hidTxt.getText().toString());
		super.onDestroy();
	}
}
