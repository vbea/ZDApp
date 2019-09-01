package com.binxin.zdapp.files;

public class FileCmdItem
{
	//新版文件管理器-系统文件信息类
	//邠心工作室(2015.09.16)
	public static final String CMDFILE_FOLDER = "d";
	public static final String CMDFILE_FILE = "-";
	public static final String CMDFILE_LINK = "l";
	public String fileName = "";//文件名
	public String filePath = "";//文件路径
	public String fileAuth = "";//文件权限
	public String fileOwner = "";//所有者
	public String fileUserg = "";//用户组
	public String fileLink = "";//文件链接
	public String fileSize = "";//文件大小
	public String fileDate = "";//文件日期
	public String fileTime = "";//文件时间
	
	public FileCmdItem(String name, String path, String auth, String owner, String group, String date, String time, String size, String link)
	{
		fileName = name;
		filePath = path;
		fileAuth = auth;
		fileOwner = owner;
		fileUserg = group;
		fileDate = date;
		fileTime = time;
		fileSize = size;
		fileLink = link;
	}
	
	public FileCmdItem(String name, String path, String auth, String owner, String group, String date, String time, String link)
	{
		fileName = name;
		filePath = path;
		fileAuth = auth;
		fileOwner = owner;
		fileUserg = group;
		fileDate = date;
		fileTime = time;
		fileSize = "0";
		fileLink = link;
	}
}
