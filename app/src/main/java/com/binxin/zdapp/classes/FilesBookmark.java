package com.binxin.zdapp.classes;

import android.content.Context;
import android.graphics.drawable.Drawable;

public class FilesBookmark implements Comparable<FilesBookmark>
{
	private int id; //ID
	private String name; //Name
	private Drawable icon; //Icon
	//private Context mContext;
	
	public FilesBookmark(int _id, String _name, Drawable _icon)
	{
		//mContext = context;
		id = _id;
		name = _name;
		if (_icon != null)
			icon = _icon;
	}
	
	public String getBookName()
	{
		return name;
	}
	
	public int getBookId()
	{
		return id;
	}
	
	public Drawable getIcon()
	{
		return icon;
	}
	
	@Override
	public int compareTo(FilesBookmark p1)
	{
		return 0;
	}
}
