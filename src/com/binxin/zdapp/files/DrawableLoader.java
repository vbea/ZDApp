package com.binxin.zdapp.files;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import com.binxin.zdapp.classes.ExceptionHandler;

//新版文件管理器-文件缩略图异步加载类
//邠心工作室(2015.09.10)
public class DrawableLoader
{
	private Context mContext;
	public static final int ICON_APK = 1;
	public static final int ICON_IVY = 2;
	
	public DrawableLoader(Context context)
	{
		mContext = context;
	}
	
	public void setOnImageLoade(String path, int type, OnDrawableLoader loader)
	{
		MyAsyncTask task = new MyAsyncTask(mContext, path, type, loader);
		task.executeOnExecutor((ExecutorService)Executors.newSingleThreadExecutor());
	}
	
	Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			
			super.handleMessage(msg);
		}
	};
	
	public class MyAsyncTask extends AsyncTask<String, Void, Drawable>
	{
		private Context sContext;
		
		private String sPath;
		private int sType;
		private OnDrawableLoader sLoader;
		public MyAsyncTask(Context _context, String path, int type, OnDrawableLoader loader)
		{
			sContext = _context;
			sPath = path;
			sLoader = loader;
			sType = type;
		}
		
		@Override
		protected Drawable doInBackground(String[] params)
		{
			switch (sType)
			{
				case ICON_APK:
					return getApkIcon();
				case ICON_IVY:
					return getThumbnail();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Drawable result)
		{
			sLoader.onImageLoade(result);
			super.onPostExecute(result);
		}
		/* 获取缩略图 */
		//获取APK文件缩略图
		public Drawable getApkIcon()
		{
			PackageManager pm = sContext.getPackageManager();
			PackageInfo info = pm.getPackageArchiveInfo(sPath,PackageManager.GET_ACTIVITIES);
			if (info != null)
			{
				ApplicationInfo appInfo = info.applicationInfo;
				appInfo.sourceDir = sPath;
				appInfo.publicSourceDir = sPath;
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
		
		public Drawable getThumbnail()
		{
			Bitmap bit = BitmapFactory.decodeFile(sPath);
			if (bit == null)
				return null;
			int width = bit.getWidth();
			int height = bit.getHeight();
			float scale = 0.0f;
			if (width > height)
			{
				scale = width / 100.0f;
				height = Float.valueOf(height / scale).intValue();
				width = 100;
			}
			else if (width < height)
			{
				scale = height / 100.0f;
				width = Float.valueOf(width /scale).intValue();
				height = 100;
			}
			else
			{
				width = height = 100;
			}
			return new BitmapDrawable(Bitmap.createScaledBitmap(bit, width, height, true));
		}
	}
	
	public interface OnDrawableLoader
	{
		void onImageLoade(Drawable drawable)
	}
}
