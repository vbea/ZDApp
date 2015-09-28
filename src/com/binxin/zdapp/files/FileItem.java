package com.binxin.zdapp.files;

import android.graphics.drawable.Drawable;

public class FileItem
{
	//新版文件管理器-文件信息类
	//邠心工作室(2015.09.08)
	public String fileName = "";//文件名
	public String filePath = "";//文件路径
	public String fileInfo = "";//文件信息
	public Drawable fileIcon = null;//文件图标
	public String fileType = "";//文件类型
	public long size = 0;
	public long time = 0;
	public boolean checkable = false;//能否选择
	public boolean checked = false;//是否选中
	public boolean isCmd = false;
	public boolean isHidden = false;
	
	public FileItem(String name, String path, String info, String type, long _size, long _time)
	{
		fileName = name;
		filePath = path;
		fileInfo = info;
		//fileIcon = icon;
		fileType = type;
		size = _size;
		time = _time;
		checkable = false;
		isHidden();
	}
	
	public FileItem(String name, String path, String info, String type, long _size, long _time, boolean selected, boolean cmd)
	{
		fileName = name;
		filePath = path;
		fileInfo = info;
		//fileIcon = icon;
		fileType = type;
		size = _size;
		time = _time;
		checkable = selected;
		isCmd = cmd;
		isHidden();
	}
	
	public FileItem(String name, String path, String info, String type, long _time, boolean selected, boolean cmd)
	{
		fileName = name;
		filePath = path;
		fileInfo = info;
		//fileIcon = icon;
		fileType = type;
		size = 0;
		time = _time;
		checkable = selected;
		isCmd = cmd;
		isHidden();
	}
	
	public void toogle()
	{
		//if (checkable)
			checked = !checked;
	}
	
	public void setChecked(boolean ck)
	{
		checked = ck;
	}
	
	private void isHidden()
	{
		isHidden = (fileName.indexOf(".") == 0);
	}
	
	/*public boolean getThumbState()
	{
		if (isCmd)
			return true;
		return isThumb;
	}*/
	
	/*public void setThumbState(boolean b)
	{
		isThumb = b;
	}*/
}
