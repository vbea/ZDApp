package com.binxin.zdapp.files;

import java.util.Map;
import java.util.HashMap;

import android.database.Cursor;
import android.content.Context;
import android.content.ContentResolver;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Video;

import com.binxin.zdapp.classes.ExceptionHandler;

public class ThumbnailHelper
{
	//新版文件管理器-缩略图类
	//邠心工作室(2015.09.23)
	private Context mContext;
	private Cursor cursor;
	private ContentResolver cr;
	private Map<String, String> hasFileId = new HashMap<String, String>();
	private Map<String, String> hasThumbPath = new HashMap<String, String>();
	
	public ThumbnailHelper(Context context)
	{
		mContext = context;
		cr = context.getContentResolver();
	}
	
	public Map<String, String> getFilesIdMap()
	{
		getImageId();
		getVideoId();
		return hasFileId;
	}
	
	public Map<String, String> getThumbPathMap()
	{
		getImagePath();
		getVideoPath();
		return hasThumbPath;
	}
	
	//获取图片文件Id
	private void getImageId()
	{
		cursor = null;
		try
		{
			String [] project = {Images.Media._ID, Images.Media.DATA };
			cursor = cr.query(Images.Media.EXTERNAL_CONTENT_URI, project, null, null, null);
			if (cursor.getCount() > 0)
			{
				while (cursor.moveToNext())
				{
					hasFileId.put(cursor.getString(cursor.getColumnIndex(Images.Media.DATA)), cursor.getString(cursor.getColumnIndex(Images.Media._ID)));
				}
			}
		}
		catch (Exception e)
		{
			ExceptionHandler.log("getImageId():" + e.toString());
		}
		finally
		{
			if (cursor != null)
				cursor.close();
		}
	}
	//获取图片文件缩略图
	private void getImagePath()
	{
		cursor = null;
		try
		{
			String[] data = {Images.Thumbnails._ID, Images.Thumbnails.IMAGE_ID, Images.Thumbnails.DATA};
			cursor = cr.query(Images.Thumbnails.EXTERNAL_CONTENT_URI, data, null, null, null);
			if (cursor.getCount() > 0)
			{
				while (cursor.moveToNext())
				{
					hasThumbPath.put(cursor.getString(cursor.getColumnIndex(Images.Thumbnails.IMAGE_ID)), cursor.getString(cursor.getColumnIndex(Images.Thumbnails.DATA)));
				}
			}
		}
		catch (Exception e)
		{
			ExceptionHandler.log("getImagePath():" + e.toString());
		}
		finally
		{
			if (cursor != null)
				cursor.close();
		}
	}
	//获取视频文件Id
	private void getVideoId()
	{
		cursor = null;
		try
		{
			String [] project = {Video.Media._ID, Video.Media.DATA };
			cursor = cr.query(Video.Media.EXTERNAL_CONTENT_URI, project, null, null, null);
			if (cursor.getCount() > 0)
			{
				while (cursor.moveToNext())
				{
					hasFileId.put(cursor.getString(cursor.getColumnIndex(Video.Media.DATA)), cursor.getString(cursor.getColumnIndex(Video.Media._ID)));
				}
			}
		}
		catch (Exception e)
		{
			ExceptionHandler.log("getVideoId():" + e.toString());
		}
		finally
		{
			if (cursor != null)
				cursor.close();
		}
	}
	//获取视频文件缩略图
	private void getVideoPath()
	{
		cursor = null;
		try
		{
			String[] data = {Video.Thumbnails._ID, Video.Thumbnails.VIDEO_ID, Video.Thumbnails.DATA};
			cursor = cr.query(Video.Thumbnails.EXTERNAL_CONTENT_URI, data, null, null, null);
			if (cursor.getCount() > 0)
			{
				while (cursor.moveToNext())
				{
					hasThumbPath.put(cursor.getString(cursor.getColumnIndex(Video.Thumbnails.VIDEO_ID)), cursor.getString(cursor.getColumnIndex(Video.Thumbnails.DATA)));
				}
			}
		}
		catch (Exception e)
		{
			ExceptionHandler.log("getVideoPath():" + e.toString());
		}
		finally
		{
			if (cursor != null)
				cursor.close();
		}
	}
}
