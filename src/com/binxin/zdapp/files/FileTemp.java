package com.binxin.zdapp.files;

import java.io.File;

public class FileTemp
{
	//新版文件管理器-文件剪贴板
	//邠心工作室(2015.09.17)
	public static final int ACTION_COPY = 0;
	public static final int ACTION_CUT = 1;
	public static final int ACTION_COVER = 2;
	//public static final int ACTION_MERGE = 3;
	public static final int ACTION_SKIP = 4;
	//public static final int ACTION_SUCCESS = 5;
	public static final int ACTION_NULL = -1;
	public File tmpFile = null;
	public File newFile = null;
	public int oldAction;
	public int action;
	public boolean exist = false;
	public boolean isSure = false;
	
	public FileTemp(File f, int a)
	{
		tmpFile = f;
		oldAction = a;
		action = a;
	}
}
