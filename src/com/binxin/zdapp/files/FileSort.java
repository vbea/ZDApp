package com.binxin.zdapp.files;

import java.util.Comparator;

public class FileSort implements Comparator<FileItem>
{
	//新版文件管理器-文件排序类
	//邠心工作室(2015.09.15)
	public static final String SORT_NAME = "s_name";
	public static final String SORT_TIME = "s_time";
	public static final String SORT_SIZE = "s_size";
	public static final String SORT_TYPE = "s_type";
	public static final String SORT_NAME_DOWN = "sd_name";
	public static final String SORT_TIME_DOWN = "sd_time";
	public static final String SORT_SIZE_DOWN = "sd_size";
	public String CURRENT_SORT = "";
	
	public FileSort(String method)
	{
		CURRENT_SORT = method;
	}
	
	public int sortName(FileItem p1, FileItem p2)
	{
		return p1.fileName.compareToIgnoreCase(p2.fileName);
	}
	
	@Override
	public int compare(FileItem p1, FileItem p2)
	{
		int r = 0;
		switch (CURRENT_SORT)
		{
			case SORT_NAME:
				return sortName(p1, p2);
			case SORT_NAME_DOWN:
				return sortName(p2, p1);
			case SORT_SIZE:
				return getIntCompareToLong(p1.size - p2.size);
			case SORT_SIZE_DOWN:
				return getIntCompareToLong(p2.size - p1.size);
			case SORT_TIME:
				return getIntCompareToLong(p1.time - p2.time);
			case SORT_TIME_DOWN:
				return getIntCompareToLong(p2.time - p1.time);
			case SORT_TYPE:
				r = FilesCommon.getExtensions(p1.fileName).compareToIgnoreCase(FilesCommon.getExtensions(p2.fileName));
				if (r != 0)
					return r;
				return sortName(p1, p2);
		}
		return 0;
	}
	
	private int getIntCompareToLong(long re)
	{
		return re > 0 ? 1 : (re < 0 ? -1 : 0);
	}
}
