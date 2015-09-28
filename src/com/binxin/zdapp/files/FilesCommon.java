package com.binxin.zdapp.files;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.Date;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.math.BigDecimal;

import android.app.AlertDialog;
import android.os.StatFs;
import android.os.Environment;
import android.webkit.MimeTypeMap;
import android.view.View;
import android.view.LayoutInflater;
import android.content.Intent;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.graphics.drawable.Drawable;

import com.binxin.zdapp.R;
import com.binxin.zdapp.classes.Common;
import com.binxin.zdapp.classes.ProgressClass;
import com.binxin.zdapp.classes.ExceptionHandler;

public class FilesCommon
{
	//新版文件管理器-文件公共操作方法
	//邠心工作室(2015.09.09)
	private Context mContext;
	private FileIcon mFileIcon;//文件图标类
	private ThumbnailHelper iconHelper;
	private Map<String, String> hasFileId = new HashMap<String, String>();
	private Map<String, String> hasThumbPath = new HashMap<String, String>();
	
	public FilesCommon(Context context)
	{
		mContext = context;
		mFileIcon = new FileIcon(context);
		if (Shipment.SHOW_THUMBNAIL)
		{
			iconHelper = new ThumbnailHelper(context);
			hasFileId.putAll(iconHelper.getFilesIdMap());
			hasThumbPath.putAll(iconHelper.getThumbPathMap());
		}
	}
	
	//计算文件夹内总大小
	public long[] fordersTotalSize(File folder)
	{
		int cfile=0,cfolder=0;
		long tmpSize = 0;
		long[] retr = new long[3];
		long[] _retr = null;
		try
		{
			String childs[] = folder.list();
			if (childs == null || childs.length <= 0)
			{
				retr[0] = tmpSize;
				retr[1] = cfile;
				retr[2] = cfolder;
				return retr;/*folder.length()*/
			}
			else
			{
				for (int i = 0; i < childs.length; i++)
				{
					String childName = childs[i];
					String childPath = folder.getPath() + File.separator + childName;
					File filePath = new File(childPath);
					if (filePath.exists() && filePath.isFile())
					{
						cfile++;
						tmpSize += filePath.length();
					}
					else if (filePath.exists() && filePath.isDirectory())
					{
						cfolder++;
						_retr = fordersTotalSize(filePath);
						tmpSize += _retr[0];
						cfile += _retr[1];
						cfolder += _retr[2];
					}
				}
				retr[0] = tmpSize;
				retr[1] = cfile;
				retr[2] = cfolder;
			}
			return retr;
		}
		catch (Exception e)
		{
			ExceptionHandler.log("FilesCommon-FolderTotalSize(Folder):"+e.toString());
		}
		return retr;
	}
	
	//获取文件大小
	public long getFileSize(File file)
	{
		try
		{
			return file.length();
		}
		catch (Exception e)
		{
			ExceptionHandler.log("getFileSize(" + file.getAbsolutePath() + "):" + e.toString());
		}
		return 0;
	}
	
	//格式化文件大小显示
	public String formatSize(long bytes)
	{
		return formatSize(bytes, false);
	}
	
	//格式化文件大小显示
	public String formatSize(long Bytes, boolean detail)
	{
		String size = "";
		float tempSize = 0;
		if (Bytes >= 1024)
		{
			tempSize = (float)Bytes / 1024;
			if (tempSize >= 1024)
			{
				tempSize = tempSize / 1024;
				if (tempSize >= 1024)
				{
					tempSize = tempSize / 1024;
					size = "GB";
				}
				else
				{
					size = "MB";
				}
			}
			else
			{
				size = "KB";
			}
		}
		else
		{
			size = "B";
		}
		if (tempSize != 0)
		{
			if (detail)
				return Float.parseFloat(new DecimalFormat("#0.00").format(tempSize)) + size + " ("+new DecimalFormat().format(Bytes) + mContext.getResources().getString(R.string.prop_bytes) + ")";
			else
				return Float.parseFloat(new DecimalFormat("#0.00").format(tempSize)) + size;
		}
		else
			return Bytes + size;
	}
	
	//获取文件扩展名
	private String getExtension(String name)
	{
		String suffix = "";
		int idx = name.lastIndexOf(".");
		if(idx > 0)
			suffix = name.substring(idx);
		return suffix;
	}
	
	//获取文件扩展名(去掉.)
	private String getExtensionName(String name)
	{
		String suffix = "";
		int idx = name.lastIndexOf(".");
		if(idx > 0)
			suffix = name.substring(idx + 1);
		return suffix;
	}
	
	//获取文件扩展名静态方法
	public static String getExtensions(String name)
	{
		String suffix = "";
		int idx = name.lastIndexOf(".");
		if(idx > 0) 
			suffix = name.substring(idx);
		return suffix;
	}
	
	//获取文件名
	public String getAbsname(String name)
	{
		String suffix = name;
		int idx = name.lastIndexOf(".");
		if(idx > 1) 
			suffix = name.substring(0, idx);
		return suffix;
	}
	
	//得到扩展名MIME类型
	public String getMimeType(String fileName)
	{
		String mime = mContext.getResources().getString(R.string.prop_unknown);
		String tmp = MimeTypeMap.getSingleton().getMimeTypeFromExtension(getExtensionName(fileName));
		if (tmp != null)
			mime = tmp;
		return mime;
	}
	
	//SD卡是否存在
	public boolean ExistSDCard()
	{
		boolean blnState = false;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			blnState = true;
		}
		return blnState;
	}
	
	//获取SD卡路径
	public String getSDCardPath()
	{
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}
	
	//获取SD卡实例
	public File getSDCard()
	{
		return Environment.getExternalStorageDirectory();
	}
	
	//获取文件修改时间
	public String getFileDate(File file)
	{
		try
		{
			Date fileDate = new Date(file.lastModified());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return sdf.format(fileDate);
		}
		catch (Exception e)
		{
			ExceptionHandler.log("getFileDate(File):" + e.toString());
		}
		return "";
	}
	
	//获取文件修改时间
	public String getFileDate(long time)
	{
		try
		{
			Date fileDate = new Date(time);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return sdf.format(fileDate);
		}
		catch (Exception e)
		{
			ExceptionHandler.log("getFileDate(long):" + e.toString());
		}
		return "";
	}
	
	//判断文件类型
	private boolean checkEnd(String fileName, int rid)
	{
		return checkEnds(fileName.toLowerCase(), getStringArray(rid));
	}
	
	//通过文件名判断是什么类型的文件
	public boolean checkEnds(String fileName, String[] fileEndings)
	{
		for(String aEnd : fileEndings)
		{
			if(fileName.endsWith(aEnd))
				return true;
		}
		return false;
	}
	
	//获取文件类型
	private String[] getStringArray(int reid)
	{
		return mContext.getResources().getStringArray(reid);
	}
	//获取文件类型
	public String getFileType(File _file)
	{
		try
		{
			if (!_file.exists())
				return "none";
			if (_file.isDirectory())
				return FileType.FILE_TYPE_FOLDER;
			return getFileType(_file.getName());
		}
		catch (Exception e)
		{
			if (_file.exists() && _file.isFile())
				return FileType.FILE_TYPE_DEFAULT;
			ExceptionHandler.log("GetFileType(File):" + e.toString());
		}
		return "";
	}
	
	//获取文件图标对象
	public FileIcon getFileIcon()
	{
		return mFileIcon;
	}
	
	private String getFileType(String fileName)
	{
		if (checkEnd(fileName, R.array.fileEndingImage))
			return FileType.FILE_TYPE_IMAGE;
		if (checkEnd(fileName, R.array.fileEndingAudio))
			return FileType.FILE_TYPE_AUDIO;
		if (checkEnd(fileName, R.array.fileEndingVideo))
			return FileType.FILE_TYPE_VIDEO;
		if (checkEnd(fileName, R.array.app))
			return FileType.FILE_TYPE_APP;
		if (checkEnd(fileName, R.array.textfile))
			return FileType.FILE_TYPE_TEXT;
		if (checkEnd(fileName, R.array.fileEndingWebText))
			return FileType.FILE_TYPE_WEB;
		if (checkEnd(fileName, R.array.fileEndingPackage))
			return FileType.FILE_TYPE_PACKAGE;
		if (checkEnd(fileName, R.array.word))
			return FileType.FILE_TYPE_WORD;
		if (checkEnd(fileName, R.array.excel))
			return FileType.FILE_TYPE_EXCEL;
		if (checkEnd(fileName, R.array.ppt))
			return FileType.FILE_TYPE_POWERPOINT;
		if (checkEnd(fileName, R.array.pdf))
			return FileType.FILE_TYPE_PDF;
		if (checkEnd(fileName, R.array.database))
			return FileType.FILE_TYPE_DATABASE;
		if (checkEnd(fileName, R.array.fontfile))
			return FileType.FILE_TYPE_FONT;
		if (checkEnd(fileName, R.array.windows))
			return FileType.FILE_TYPE_WINDOWS;
		if (checkEnd(fileName, R.array.ppgame))
			return FileType.FILE_TYPE_GAME;
		return FileType.FILE_TYPE_DEFAULT;
	}
	
	//获取文件图标
	public Drawable getFileIcon(String type)
	{
		Drawable _ic;
		switch (type)
		{
			case FileType.FILE_TYPE_FOLDER://1
				_ic = mFileIcon.FILE_ICON_FOLDER;
				break;
			case FileType.FILE_TYPE_IMAGE://2
				_ic = mFileIcon.FILE_ICON_IMAGE;
				break;
			case FileType.FILE_TYPE_TEXT://3
				_ic = mFileIcon.FILE_ICON_TEXT;
				break;
			case FileType.FILE_TYPE_WEB://4
				_ic = mFileIcon.FILE_ICON_WEB;
				break;
			case FileType.FILE_TYPE_AUDIO://5
				_ic = mFileIcon.FILE_ICON_AUDIO;
				break;
			case FileType.FILE_TYPE_VIDEO://6
				_ic = mFileIcon.FILE_ICON_VIDEO;
				break;
			case FileType.FILE_TYPE_APP://7
				_ic = mFileIcon.FILE_ICON_APP;
				break;
			case FileType.FILE_TYPE_PACKAGE://8
				_ic = mFileIcon.FILE_ICON_PACKAGE;
				break;
			case FileType.FILE_TYPE_WORD://9
				_ic = mFileIcon.FILE_ICON_WORD;
				break;
			case FileType.FILE_TYPE_EXCEL://10
				_ic = mFileIcon.FILE_ICON_EXCEL;
				break;
			case FileType.FILE_TYPE_POWERPOINT://11
				_ic = mFileIcon.FILE_ICON_POWERPOINT;
				break;
			case FileType.FILE_TYPE_PDF://12
				_ic = mFileIcon.FILE_ICON_PDF;
				break;
			case FileType.FILE_TYPE_DATABASE://13
				_ic = mFileIcon.FILE_ICON_DATABASE;
				break;
			case FileType.FILE_TYPE_FONT://14
				_ic = mFileIcon.FILE_ICON_FONT;
				break;
			case FileType.FILE_TYPE_WINDOWS://15
				_ic = mFileIcon.FILE_ICON_WINDOWS;
				break;
			case FileType.FILE_TYPE_GAME://16
				_ic = mFileIcon.FILE_ICON_GAME;
				break;
			case FileType.FILE_TYPE_DEFAULT://17
				_ic = mFileIcon.FILE_ICON_DEFAULT;
				break;
			default:
				_ic = null;
				break;
		}
		return _ic;
	}
	
	//打开指定文件
	public void openFile(File file, String type)
	{
		Intent intent = new Intent();
		intent.setAction(android.content.Intent.ACTION_VIEW);
		try
		{
			String mime = getMimeType(file.getName());
			if (mime.equals(mContext.getResources().getString(R.string.prop_unknown)))
			{
				// 根据不同的文件类型来打开文件
				switch (type)
				{
					case FileType.FILE_TYPE_APP:
						mime = "application/vnd.android.package-archive";
						break;
					case FileType.FILE_TYPE_IMAGE:
						mime = "image/*";
						break;
					case FileType.FILE_TYPE_AUDIO:
						mime = "audio/*";
						break;
					case FileType.FILE_TYPE_VIDEO:
						mime = "video/*";
						break;
					case FileType.FILE_TYPE_TEXT:
						{
							mime = "text/plain";
							if (getExtension(file.getName().toLowerCase()).equals("log"))
								mime = "application/log";
							break;
						}
					case FileType.FILE_TYPE_WEB:
						mime = "text/html";
						break;
					case FileType.FILE_TYPE_PACKAGE:
						mime = "application/zip";
						break;
					case FileType.FILE_TYPE_WORD:
						mime = "application/msword";
						break;
					case FileType.FILE_TYPE_EXCEL:
						mime = "application/vnd.ms-excel";
						break;
					case FileType.FILE_TYPE_POWERPOINT:
						mime = "application/vnd.ms-powerpoint";
						break;
					case FileType.FILE_TYPE_PDF:
						mime = "application/pdf";
						break;
					case FileType.FILE_TYPE_DATABASE:
						mime = "application/database";
						break;
					case FileType.FILE_TYPE_GAME:
						mime = "application/x-" + getExtensionName(file.getName()) + "-rom";
						break;
					default:
						mime = "*/*";
						break;
				}
			}
			intent.setDataAndType(Uri.fromFile(file), mime);
			mContext.startActivity(intent);
		}
		catch (Exception e)
		{
			intent.setDataAndType(Uri.fromFile(file), "*/*");
			mContext.startActivity(intent);
		}
	}
	
	//发送指定文件
	public void sendFile(File file, String type)
	{
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_SEND);
		// 根据不同的文件类型来打开文件
		switch (type)
		{
			case FileType.FILE_TYPE_IMAGE:
				intent.setType("image/*");
				break;
			case FileType.FILE_TYPE_AUDIO:
				intent.setType("audio/*");
				break;
			case FileType.FILE_TYPE_VIDEO:
				intent.setType("video/*");
				break;
			case FileType.FILE_TYPE_TEXT:
				intent.setType("text/plain");
				break;
			case FileType.FILE_TYPE_WEB:
				intent.setType("text/html");
				break;
			case FileType.FILE_TYPE_PACKAGE:
				intent.setType("application/zip");
				break;
			default:
				intent.setType("*/*");
				break;
		}
		intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
		mContext.startActivity(Intent.createChooser(intent, mContext.getResources().getString(R.string.file_sharmode)));
	}
	
	//发送多个文件
	public void sendFiles(File[] files)
	{
		if (files != null && files.length > 0)
		{
			ArrayList<Uri> uris = new ArrayList<Uri>();
			for (File _f:files)
			{
				Uri u = Uri.fromFile(_f);
				uris.add(u);
			}
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_SEND_MULTIPLE);
			intent.setType("*/*");
			intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
			mContext.startActivity(Intent.createChooser(intent, mContext.getResources().getString(R.string.file_sharmode)));
		}
	}
	
	//文件重命名
	public boolean rename(File file, String newName)
	{
		try
		{
			if (file.getName() == newName)
				return true;
			File newFile = new File(file.getParent() + File.separator + newName);
			if (!newFile.exists())
				return file.renameTo(newFile);
		}
		catch (Exception e)
		{
			ExceptionHandler.log("Rename File:" + e.toString());
		}
		return false;
	}
	
	//删除文件
	public boolean deleteFile(File file)
	{
		boolean result = false;
		try
		{
			if (file != null)
				result = file.delete();
		}
		catch (Exception e)
		{
			ExceptionHandler.log("Delete File:" + e.getMessage());
			result = false;
		}
		return result;
	}
	
	//删除文件夹
	public boolean deleteFolder(File folder)
	{
		boolean result = false;
		try
		{
			File[] childs = folder.listFiles();
			if (childs == null || childs.length <= 0)
			{
				if (folder.delete())
					result = true;
			}
			else
			{
				for (File filePath:childs)
				{
					if (filePath.exists())
					{
						if (filePath.isFile())
							result = filePath.delete();
						else
							result = deleteFolder(filePath);
					}
					else
						result = false;
					if (result != true)
						return result;
				}
				result = folder.delete();
			}
		}
		catch (Exception e)
		{
			ExceptionHandler.log("Delete Folder:"+e.toString());
			result = false;
		}
		return result;
	}
	
	//删除多个文件(夹)
	public int[] deleteMoreFiles(File[] files)
	{
		int[] state = new int[2];
		state[0] = 0;
		state[1] = 0;
		if (files == null || files.length == 0)
			return state;
		try
		{
			for (File file:files)
			{
				if (file.isFile())
				{
					if (deleteFile(file))
						state[0]++;
					else
						state[1]++;
				}
				else
				{
					int c = file.list().length + 1;
					if (deleteFolder(file))
						state[0]+= c;
					else
						state[1]+= c;
				}
			}
		}
		catch (Exception e)
		{
			ExceptionHandler.log("Delete File(s):" + e.toString());
		}
		return state;
	}
	
	//新建文件夹
	public boolean newFolder(String newfolder)
	{
		File dirFile = new File(newfolder);
		try
		{
			if (!dirFile.exists())
				return dirFile.mkdirs();
		}
		catch (Exception e)
		{
			ExceptionHandler.log("New Folder:" + e.toString());
		}
		return false;
	}
	
	//新建文件
	public boolean newFile(String newfile)
	{
		File dirFile = new File(newfile);
		try
		{
			if (!dirFile.exists())
				return dirFile.createNewFile();
		}
		catch (Exception e)
		{
			ExceptionHandler.log("New File:" + e.toString());
		}
		return false;
	}
	
	//计算选中的文件
	public int getCheckedFiles(ArrayList<FileItem> fileList)
	{
		int _cou = 0;
		for (FileItem ft : fileList)
		{
			if (ft.checkable && ft.checked)
				_cou++;
		}
		return _cou;
	}
	
	//获取选中文件对象数组
	public ArrayList<FileItem> getCheckedFilesList(ArrayList<FileItem> fileList)
	{
		ArrayList<FileItem> _list = new ArrayList<FileItem>();
		for (FileItem ft : fileList)
		{
			if (ft.checkable && ft.checked)
				_list.add(ft);
		}
		return _list;
	}
	
	//获取能远取的文件数量
	public int getCanSelectFile(ArrayList<FileItem> fileList)
	{
		//这里也可用遍历数组实现
		if (fileList.get(0).fileName.equals(mContext.getString(R.string.up_one_level)) && fileList.get(0).fileInfo.equals(mContext.getString(R.string.up_txt_level)))
			return fileList.size() - 1;
		else
			return fileList.size();
	}
	
	//全选
	public void selectAll(ArrayList<FileItem> fileList)
	{
		for (FileItem ft : fileList)
		{
			if (!ft.checked && ft.checkable)
				ft.setChecked(true);
		}
	}
	
	//全不选
	public void selectNotAll(ArrayList<FileItem> fileList)
	{
		for (FileItem ft : fileList)
		{
			if (ft.checked && ft.checkable)
				ft.setChecked(false);
		}
	}
	
	//获取选中的文件路径
	public ArrayList<String> getCheckedFilePath(ArrayList<FileItem> checkList)
	{
		ArrayList<String> chfiles = new ArrayList<String>();
		for (FileItem item : checkList)
		{
			if (item.checkable && item.checked)
				chfiles.add(item.filePath);
		}
		return chfiles;
	}
	
	//获取选中的文件
	public File[] getCheckedFile(ArrayList<FileItem> checkList)
	{
		ArrayList<String> files = getCheckedFilePath(checkList);
		File[] chfiles = new File[files.size()];
		for (int i= 0;i<files.size();i++)
		{
			chfiles[i] = new File(files.get(i));
		}
		return chfiles;
	}
	
	//获取文件链接路径(过时的)
	public String getCanonicalName(File file)
	{
		try
		{
			if (!file.getCanonicalPath().equals(file.getAbsolutePath()))
				return "->" + file.getCanonicalFile().getName();
		}
		catch (Exception e)
		{
			ExceptionHandler.log("getCanonicalName(File):" + e.toString());
		}
		return "";
	}

	//获取文件路径(过时的)
	public String getCanonicalPath(File file)
	{
		try
		{
			return file.getCanonicalPath();
		}
		catch (Exception e)
		{
			ExceptionHandler.log("getCanonicalPath(File):" + e.toString());
			return file.getAbsolutePath();
		}
	}
	
	//获取最后一个/前的字符串
	public String getParentPath(String path)
	{
		if (path.lastIndexOf(File.separator) > 0)
			return path.substring(0, path.lastIndexOf(File.separator));
		else
			return File.separator;
	}
	
	//获取最后一个/后的字符串
	public String getLastNameforPath(String path)
	{
		if (path.lastIndexOf("/") >= 0)
		{
			return path.substring(path.lastIndexOf(File.separator) + 1);
		}
		return path;
	}
	
	//字符串日期转Long
	public long getStringDateToLong(String date,String time)
	{
		try
		{
			if (date.split("-").length == 3)
			{
				if (time.split(":").length != 3)
					time = time + ":00";
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				Date dates = sdf.parse(date + " " + time);
				return dates.getTime();
			}
		}
		catch (Exception e)
		{
			ExceptionHandler.log("getStringDateToLong(String):" + e.toString());
		}
		return 0;
	}
	
	//获取第一个文件(过时的)
	public String[] getFileFirstList(String path)
	{
		String[] list = null;
		try
		{
			String[] files = ProgressClass.getFileLists(path);
			if (files.length > 0)
			{
				String type = "length:" + files.length + "//";
				type += files[0].replaceAll(" +","//");
				list = type.split("//");
				list[4] = ""+getStringDateToLong(list[4], list[5]);
			}
		}
		catch (Exception e)
		{
			ExceptionHandler.log("getFileList(" + path + "):" + e.toString());
		}
		return list;
	}
	
	//获取CMD文件列表
	public ArrayList<FileCmdItem> getFileList(String path)
	{
		ArrayList<FileCmdItem> list = new ArrayList<FileCmdItem>();
		try
		{
			String[] files = ProgressClass.getFileLists(path);
			if (files.length > 0)
			{
				String type = "";
				String[] st = null;
				for (String file : files)
				{
					file = file.replaceAll(" +","//");
					st = file.split("//");
					if (st.length <= 0)
						continue;
					type = st[0].substring(0, 1);
					if (type.equals("d"))
					{
						//文件夹
						list.add(new FileCmdItem(st[5], (path + File.separator + st[5]), st[0], st[1], st[2], st[3], st[4], ""));
					}
					else if (type.equals("-"))
					{
						//文件
						list.add(new FileCmdItem(st[6], (path + File.separator + st[6]), st[0], st[1], st[2], st[4], st[5], st[3], ""));
					}
					else if (type.equals("l"))
					{
						//链接
						list.add(new FileCmdItem(st[5], (path + File.separator + st[5]), st[0], st[1], st[2], st[3], st[4], st[7]));
					}
					//else
					//list.add(new FileCmdItem(file, "/", "", "", "", "1996-01-01", 0));
				}
			}
		}
		catch (Exception e)
		{
			ExceptionHandler.log("getFileList(" + path + "):" + e.toString());
		}
		return list;
	}
	
	//填充CMD文件列表
	public int getFileListToCmd(String path, ArrayList<FileItem> folder, ArrayList<FileItem> file)
	{
		ArrayList<FileCmdItem> items = getFileList(path);
		try
		{
			if (items.size() > 0)
			{
				String fileType = "";
				String fileInfo = "";
				long time = 0;
				long size = 0;
				for (FileCmdItem item : items)
				{
					//Common.showShortToast(mContext,item.fileAuth.substring(0,1));
					switch (item.fileAuth.substring(0,1))
					{
						case "-":
						{
							fileType = getFileType(item.fileName);
							time = getStringDateToLong(item.fileDate, item.fileTime);
							size = Long.parseLong(item.fileSize);
							fileInfo = getFileDate(time) + " " + formatSize(size) + " " + item.fileAuth.substring(1);
							file.add(new FileItem(item.fileName, item.filePath, fileInfo, fileType, size, time, false, true));
							break;
						}
						case "d":
						{
							fileType = FileType.FILE_TYPE_FOLDER;
							time = getStringDateToLong(item.fileDate, item.fileTime);
							fileInfo = getFileDate(time) + " " + item.fileAuth.substring(1);
							folder.add(new FileItem(item.fileName, item.filePath, fileInfo, fileType, 0, time, false, true));
							break;
						}
						case "l":
						{
							fileType = getFileType(item.fileName);
							time = getStringDateToLong(item.fileDate, item.fileTime);
							fileInfo = getFileDate(time) + " ->" + getLastNameforPath(item.fileLink) + " " + item.fileAuth.substring(1);
							file.add(new FileItem(item.fileName, item.filePath, fileInfo, fileType, 0, time, false, true));
							break;
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			ExceptionHandler.log("getFileListToCmd(" + path + ", ArrayList(2)):" + e.toString());
		}
		return items.size();
	}
	
	//通过文件路径获取缩略图路径
	public String getThumbPath(String filePath)
	{
		//根据文件路径获取id
		String _id = hasFileId.get(filePath);
		if (_id != null)
		{
			//根据id获取缩略图路径
			return hasThumbPath.get(_id);
		}
		return null;
	}
	
	//显示提示框
	public void showDialogTip(AlertDialog.Builder dialogTip, String title, String message, DialogInterface.OnClickListener okClick)
	{
		dialogTip = new AlertDialog.Builder(mContext);
		dialogTip.setTitle(title);
		dialogTip.setMessage(message);
		dialogTip.setPositiveButton(R.string.ok, okClick);
		dialogTip.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface p1, int p2)
			{
				p1.cancel();
			}
		});
		dialogTip.create();
		dialogTip.show();
	}
	
	//设置剪切板目标文件
	public void getTmpFilesNewFile(ArrayList<FileTemp> tmps, String path)
	{
		String newPath = "";
		for (FileTemp tmp : tmps)
		{
			newPath = path + File.separator + tmp.tmpFile.getName();
			if (tmp.tmpFile.getAbsolutePath().equals(newPath))
			{
				if (tmp.action == FileTemp.ACTION_COPY)
					newPath = path + File.separator + getAbsname(tmp.tmpFile.getName()) + "(1)" + getExtension(tmp.tmpFile.getName());
				else if (tmp.action == FileTemp.ACTION_CUT)
				{
					tmp.exist = true;
					tmp.action = FileTemp.ACTION_SKIP;
					tmp.isSure = true;
				}
			}
			tmp.newFile = new File(newPath);
		}
	}
	
	//获取剪切板文件重复数量
	public int getTmpFileExistCount(ArrayList<FileTemp> tmps)
	{
		int count = 0;
		for (FileTemp tmp : tmps)
		{
			if (tmp.newFile.exists())
			{
				tmp.exist = true;
				count++;
			}
		}
		return count;
	}
	
	//获取剪切板文件重复索引
	public ArrayList<Integer> getTmpFileExistIndex(ArrayList<FileTemp> tmps)
	{
		ArrayList<Integer> index = new ArrayList<Integer>();
		for (int i = 0; i < tmps.size(); i++)
		{
			if (tmps.get(i).newFile.exists())
			{
				index.add(i);
			}
		}
		return index;
	}
	
	//设置已存在文件的操作
	public void setExistFileAction(ArrayList<FileTemp> tmpfiles, int action)
	{
		for (FileTemp tmp : tmpfiles)
		{
			if (tmp.exist && !tmp.isSure)
			{
				tmp.action = action;
				tmp.isSure = true;
			}
		}
	}
	//显示剪切板内容
	public String getTempFileList(ArrayList<FileTemp> tmpfiles)
	{
		String li = "";
		for (FileTemp tmp : tmpfiles)
		{
			li += (tmp.tmpFile.getName()
			+ " " + tmp.exist + " | "
			+ gettmpString(tmp.oldAction) + "/" + gettmpString(tmp.action) + "\n");
		}
		return li;
	}
	
	private String gettmpString(int action)
	{
		switch (action)
		{
			case FileTemp.ACTION_COPY:
				return "复制";
			case FileTemp.ACTION_COVER:
				return "覆盖";
			case FileTemp.ACTION_CUT:
				return "剪切";
			case FileTemp.ACTION_SKIP:
				return "跳过";
		}
		return "...";
	}
}
