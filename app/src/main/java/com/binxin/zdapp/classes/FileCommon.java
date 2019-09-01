package com.binxin.zdapp.classes;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.math.BigDecimal;

import android.os.Environment;
import android.webkit.MimeTypeMap;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ApplicationInfo;
import android.media.ThumbnailUtils;

import com.binxin.zdapp.R;

public class FileCommon
{
	public final int MODE_IMG = 10;
	public final int MODE_APK = 11;
	private Context mContext;
	
	public FileCommon(Context context)
	{
		mContext = context;
	}
	//计算文件夹内总大小
	public long[] fordersTotalSize(File folder)
	{
		int cfile=0,cfolder=0;
		long tmpSize = 0;
		long[] retr = new long[3];
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
						long[] _retr = fordersTotalSize(filePath);
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
			ExceptionHandler.log(e.toString());
		}
		return retr;
	}
	//计算文件夹内总大小
	public long forderTotalSize(File folder)
	{
		long tmpSize = 0;
		try
		{
			String childs[] = folder.list();
			if (childs == null || childs.length <= 0)
			{
				return tmpSize;
				/*folder.length()*/
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
						tmpSize += filePath.length();
					}
					else if (filePath.exists() && filePath.isDirectory())
					{
						tmpSize += forderTotalSize(filePath);
					}
				}
			}
			//return tmpSize;
		}
		catch (Exception e)
		{
			ExceptionHandler.log(e.toString());
		}
		return tmpSize;
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
			if (!detail)
				return Float.parseFloat(new DecimalFormat("#0.00").format(tempSize)) + size;
			else
				return Float.parseFloat(new DecimalFormat("#0.00").format(tempSize)) + size + " ("+new DecimalFormat().format(Bytes) + mContext.getResources().getString(R.string.prop_bytes) + ")";
		}
		else
			return Bytes + size;
	}
	//获取文件扩展名
	private String getExtension(String name)
	{
		String suffix = "";
		int idx = name.lastIndexOf(".");
		if(idx > 0) {
			suffix = name.substring(idx + 1);
		}
		return suffix;
	}
	//得到扩展名MIME类型
	public String getMimeType(File file)
	{
		String mime = mContext.getResources().getString(R.string.prop_unknown);
		if (file.isFile())
		{
			String tmp = MimeTypeMap.getSingleton().getMimeTypeFromExtension(getExtension(file.getName()));
			if (tmp != null)
				mime = tmp;
		}
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
	//获取文件修改时间
	public String getFileDete(File file)
	{
		Date fileDate = new Date(file.lastModified());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(fileDate);
	}
	//是否为数字
	public boolean isNumber(String s)
	{
		try
		{
			Integer.parseInt(s);
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
	//获取图片缩略图
	public Drawable getThumbFromBitmap(String filePath,int max)
	{
		int width = 0,height = 0;
		Bitmap bitmap = null;
		try
		{
			//Bitmap bitmap = BitmapFactory.decodeFile(filePath);//Drawable转Bitmap
			InputStream is = new FileInputStream(filePath);
			bitmap = BitmapFactory.decodeStream(is);
			if (bitmap == null)
				return null;
			float scale = 0;
			boolean isWidth = (bitmap.getWidth() > bitmap.getHeight());
			if (isWidth)
				scale = (float)bitmap.getWidth() / (float)max;
			else
				scale = (float)bitmap.getHeight() / (float)max;
			if (scale <= 0) scale = 1.0f;
			if (isWidth)
			{
				width = max;
				height = (int)((float)bitmap.getHeight() / scale);
			}
			else
			{
				height = max;
				width = (int)((float)bitmap.getHeight() / scale);
			}
			Drawable draw = new BitmapDrawable(ThumbnailUtils.extractThumbnail(bitmap,width,height));//Bitmap转Drawable
			is.close();
			return draw;
		}
		catch (OutOfMemoryError e)
		{
			ExceptionHandler.log("getThumbFromBitmap(OOM):"+e.toString());
			return null;
		}
		catch (Exception e)
		{
			ExceptionHandler.log("getThumbFromBitmap:"+e.toString());
			return null;
		}
		finally
		{
			if (bitmap != null)
				bitmap.recycle();//释放Bitmap回收内存
		}
	}
	
	//获取APK图标
	public Drawable getApkIcon(Context context,String apkPath)
	{
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkPath,PackageManager.GET_ACTIVITIES);
        if (info != null)
		{
            ApplicationInfo appInfo = info.applicationInfo;
            appInfo.sourceDir = apkPath;
            appInfo.publicSourceDir = apkPath;
			try
			{
				return appInfo.loadIcon(pm);
			}
			catch (OutOfMemoryError e)
			{
				ExceptionHandler.log("getApkIcon(OOM):"+e.toString());
			}
			catch (Exception e)
			{
				ExceptionHandler.log("getApkIcon:"+e.toString());
			}
		}
		return null;
	}
}
