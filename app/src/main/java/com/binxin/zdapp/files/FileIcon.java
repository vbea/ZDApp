package com.binxin.zdapp.files;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.binxin.zdapp.R;

public class FileIcon
{
	//新版文件管理器-文件图标类
	//邠心工作室(2015.09.08)
	private Context mContext;
	public Drawable FILE_ICON_FOLDER = null;
	public Drawable FILE_ICON_IMAGE = null;
	public Drawable FILE_ICON_TEXT = null;
	public Drawable FILE_ICON_AUDIO = null;
	public Drawable FILE_ICON_VIDEO = null;
	public Drawable FILE_ICON_APP = null;
	public Drawable FILE_ICON_WEB = null;
	public Drawable FILE_ICON_PACKAGE = null;
	public Drawable FILE_ICON_DATABASE = null;
	public Drawable FILE_ICON_WORD = null;
	public Drawable FILE_ICON_EXCEL = null;
	public Drawable FILE_ICON_POWERPOINT = null;
	public Drawable FILE_ICON_PDF = null;
	public Drawable FILE_ICON_FONT = null;
	public Drawable FILE_ICON_WINDOWS = null;
	public Drawable FILE_ICON_GAME = null;
	public Drawable FILE_ICON_DEFAULT = null;
	
	public FileIcon(Context context)
	{
		mContext = context;
		if (mContext != null)
			getFileIcon();
	}
	
	private void getFileIcon()
	{
		FILE_ICON_FOLDER = mContext.getResources().getDrawable(R.drawable.folder);
		FILE_ICON_IMAGE = mContext.getResources().getDrawable(R.drawable.image);
		FILE_ICON_TEXT = mContext.getResources().getDrawable(R.drawable.text);
		FILE_ICON_AUDIO = mContext.getResources().getDrawable(R.drawable.audio);
		FILE_ICON_VIDEO = mContext.getResources().getDrawable(R.drawable.video);
		FILE_ICON_APP = mContext.getResources().getDrawable(R.drawable.ic_app);
		FILE_ICON_WEB = mContext.getResources().getDrawable(R.drawable.webtext);
		FILE_ICON_PACKAGE = mContext.getResources().getDrawable(R.drawable.packed);
		FILE_ICON_DATABASE = mContext.getResources().getDrawable(R.drawable.database);
		FILE_ICON_WORD = mContext.getResources().getDrawable(R.drawable.word_icon);
		FILE_ICON_EXCEL = mContext.getResources().getDrawable(R.drawable.excel_icon);
		FILE_ICON_POWERPOINT = mContext.getResources().getDrawable(R.drawable.ppt_icon);
		FILE_ICON_PDF = mContext.getResources().getDrawable(R.drawable.pdf_icon);
		FILE_ICON_FONT = mContext.getResources().getDrawable(R.drawable.ic_font);
		FILE_ICON_WINDOWS = mContext.getResources().getDrawable(R.drawable.ic_windows);
		FILE_ICON_GAME = mContext.getResources().getDrawable(R.drawable.ic_game);
		FILE_ICON_DEFAULT = mContext.getResources().getDrawable(R.drawable.default2);
	}
}
