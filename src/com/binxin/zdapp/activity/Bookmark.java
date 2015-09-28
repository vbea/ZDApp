package com.binxin.zdapp.activity;

import java.io.File;
import java.util.List;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.ImageButton;
import android.view.View;
import android.view.LayoutInflater;

import com.binxin.zdapp.R;
import com.binxin.zdapp.files.FileType;
import com.binxin.zdapp.files.FileIcon;
import com.binxin.zdapp.files.FilesCommon;
import com.binxin.zdapp.classes.Common;
import com.binxin.zdapp.classes.MyThemes;
import com.binxin.zdapp.classes.FilesBookmark;
import com.binxin.zdapp.classes.FilesBookmarkAdapter;

public class Bookmark extends Activity
{
	private AlertDialog.Builder menuDialog;
	private SharedPreferences spf_book;
	private SharedPreferences.Editor spf_edt;
	private LinearLayout titleLayout;
	private ListView mListView;
	private List<FilesBookmark> mBookList = new ArrayList<FilesBookmark>();
	private FileIcon fileicon;
	private FilesCommon fileComm;
	private LayoutInflater inflat;
	private EditText edt_book;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setTheme(android.R.style.Theme_DeviceDefault_Light_NoActionBar);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookmark);
		fileicon = new FileIcon(this);
		fileComm = new FilesCommon(this);
		ImageButton back = (ImageButton) findViewById(R.id.btn_close);
		titleLayout = (LinearLayout) findViewById(R.id.themeLayout);
		mListView = (ListView) findViewById(R.id.book_listView);
		
		back.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				finish();
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
		
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
			{
				openBook(mBookList.get(p3).getBookId());
			}
		});
		
		mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
		{
			@Override
			public boolean onItemLongClick(AdapterView<?> p1, View p2, int position, long id)
			{
				int bid = mBookList.get(position).getBookId();
				bookOptMenu(bid);
				return true;
			}
		});
	}
	
	//获取书签列表
	public void getBookmark()
	{
		mBookList.clear();
		int bookcount = spf_book.getInt("count", 0);
		if (bookcount > 0)
		{
			String _book;
			String _type;
			for (int i=0; i<bookcount; i++)
			{
				_book = spf_book.getString("bk_"+i, "");
				if (!_book.equals(""))
				{
					_type = spf_book.getString("tp_"+i, "");
					mBookList.add(new FilesBookmark(i,_book, getIcon(_type)));
				}
			}
		}
		FilesBookmarkAdapter adapter = new FilesBookmarkAdapter(this);
		adapter.setListItems(mBookList);
		mListView.setAdapter(adapter);
	}
	
	public Drawable getIcon(String type)
	{
		Drawable _ic;
		switch (type)
		{
			case FileType.FILE_TYPE_FOLDER://1
				_ic = fileicon.FILE_ICON_FOLDER;
				break;
			case FileType.FILE_TYPE_IMAGE://2
				_ic = fileicon.FILE_ICON_IMAGE;
				break;
			case FileType.FILE_TYPE_TEXT://3
				_ic = fileicon.FILE_ICON_TEXT;
				break;
			case FileType.FILE_TYPE_WEB://4
				_ic = fileicon.FILE_ICON_WEB;
				break;
			case FileType.FILE_TYPE_AUDIO://5
				_ic = fileicon.FILE_ICON_AUDIO;
				break;
			case FileType.FILE_TYPE_VIDEO://6
				_ic = fileicon.FILE_ICON_VIDEO;
				break;
			case FileType.FILE_TYPE_APP://7
				_ic = fileicon.FILE_ICON_APP;
				break;
			case FileType.FILE_TYPE_PACKAGE://8
				_ic = fileicon.FILE_ICON_PACKAGE;
				break;
			case FileType.FILE_TYPE_WORD://9
				_ic = fileicon.FILE_ICON_WORD;
				break;
			case FileType.FILE_TYPE_EXCEL://10
				_ic = fileicon.FILE_ICON_EXCEL;
				break;
			case FileType.FILE_TYPE_POWERPOINT://11
				_ic = fileicon.FILE_ICON_POWERPOINT;
				break;
			case FileType.FILE_TYPE_PDF://12
				_ic = fileicon.FILE_ICON_PDF;
				break;
			case FileType.FILE_TYPE_DATABASE://13
				_ic = fileicon.FILE_ICON_DATABASE;
				break;
			case FileType.FILE_TYPE_FONT://14
				_ic = fileicon.FILE_ICON_FONT;
				break;
			case FileType.FILE_TYPE_WINDOWS://15
				_ic = fileicon.FILE_ICON_WINDOWS;
				break;
			case FileType.FILE_TYPE_GAME://16
				_ic = fileicon.FILE_ICON_GAME;
				break;
			case FileType.FILE_TYPE_DEFAULT://17
				_ic = fileicon.FILE_ICON_DEFAULT;
				break;
			default:
				_ic = null;
				break;
		}
		return _ic;
	}
	
	public void bookOptMenu(final int bid)
	{
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				switch(which)
				{
					case 0://打开
						openBook(bid);
						break;
					case 1://修改
						amendBook(bid);
						break;
					case 2://删除
						deleteBook(bid);
						break;
				}
			}
		};
		//显示操作菜单
		String[] menu={getString(R.string.book_open), getString(R.string.book_edit), getString(R.string.book_del)};
	    menuDialog = new AlertDialog.Builder(Bookmark.this);
		menuDialog.setTitle(R.string.file_book);
		menuDialog.setItems(menu,listener);
		menuDialog.create();
		menuDialog.show();
	}
	
	private void openBook(int _id)
	{
		App_filemanager.BOOKMARK_STATUS = true;
		App_filemanager.BOOKMARK_PATH = spf_book.getString("bk_"+_id, "/");
		this.finish();
	}
	
	private void amendBook(final int _id)
	{
		inflat = LayoutInflater.from(this);
		final View dialogview = inflat.inflate(R.layout.editview, null);
		edt_book = (EditText) dialogview.findViewById(R.id.txt_editView);
		edt_book.setText(spf_book.getString("bk_"+_id, ""));
		AlertDialog.Builder builder = new AlertDialog.Builder(Bookmark.this);
		builder.setTitle(R.string.book_edit);
		builder.setView(dialogview);
		builder.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				String strBook = edt_book.getText().toString();
				spf_edt.putString("bk_"+_id, strBook);
				spf_edt.putString("tp_"+_id, fileComm.getFileType(new File(strBook)));
				if (spf_edt.commit())
				{
					Common.showShortToast(getApplicationContext(),R.string.operate_success);
					getBookmark();
				}
				else
					Common.showShortToast(getApplicationContext(),R.string.operate_fail);
			}
		});
		builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.cancel();
			}
		});
		builder.setCancelable(false);
		builder.create();
		builder.show();
	}
	
	private void deleteBook(final int _id)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(Bookmark.this);
		builder.setTitle(R.string.book_del);
		builder.setMessage(R.string.delete_warn);
		builder.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				spf_edt.putString("bk_"+_id, "");
				spf_edt.putString("tp_"+_id, "");
				if (spf_edt.commit())
				{
					Common.showShortToast(getApplicationContext(),R.string.delete_success);
					getBookmark();
				}
				else
					Common.showShortToast(getApplicationContext(),R.string.delete_fail);
			}
		});
		builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.cancel();
			}
		});
		builder.setCancelable(false);
		builder.create();
		builder.show();
	}

	public void setMyTheme()
	{
		SharedPreferences sube = getSharedPreferences("zdapp", Context.MODE_PRIVATE);
		int code = sube.getInt("themeCode", 0);
		MyThemes.setThemes(this,titleLayout, code);
	}
	
	@Override
	protected void onResume()
	{
		setMyTheme();
		spf_book = getSharedPreferences("bookmarker", Context.MODE_PRIVATE);
		spf_edt = spf_book.edit();
		getBookmark();
		super.onResume();
	}
	
	
}
