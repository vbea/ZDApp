package com.binxin.zdapp.files;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentResolver;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Thumbnails;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import com.binxin.zdapp.classes.ExceptionHandler;

//新版文件管理器-文件缩略图异步加载类
//邠心工作室(2015.09.21)
public class BitmapLoader
{
	private Context mContext;
	private ContentResolver cr;

	public BitmapLoader(Context context)
	{
		mContext = context;
		cr = context.getContentResolver();
	}

	public void setOnImageLoade(String path, OnBitmapLoader loader)
	{
		MyAsyncTask task = new MyAsyncTask(path, loader);
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
		private String sPath;
		private OnBitmapLoader sLoader;
		public MyAsyncTask(String path, OnBitmapLoader loader)
		{
			sPath = path;
			sLoader = loader;
		}

		@Override
		protected Drawable doInBackground(String[] params)
		{
			return getImageIcon();
		}
		
		public String getImageId()
		{
			Cursor cursor = null;
			try
			{
				String [] project = {Images.Media._ID, Images.Media.DATA };
				cursor = cr.query(Images.Media.EXTERNAL_CONTENT_URI, project, Images.Media.DATA + "='" + sPath + "'", null, null);
				if (cursor.getCount() > 0)
				{
					cursor.moveToFirst();
					return cursor.getString(cursor.getColumnIndex(Images.Media._ID));
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
			return null;
		}

		public Drawable getImageIcon()
		{
			Cursor cursor = null;
			try
			{
				String[] data = {Thumbnails._ID, Thumbnails.IMAGE_ID, Thumbnails.DATA};
				cursor = cr.query(Thumbnails.EXTERNAL_CONTENT_URI, data, Thumbnails.IMAGE_ID + "=" + getImageId(), null, null);
				if (cursor.getCount() > 0)
				{
					cursor.moveToFirst();
					String value = cursor.getString(cursor.getColumnIndex(Thumbnails.DATA));
					Bitmap bit = BitmapFactory.decodeFile(value);
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
			catch (Exception e)
			{
				ExceptionHandler.log("getImageIcon[Bitmap]():" + e.toString());
			}
			finally
			{
				if (cursor != null)
					cursor.close();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Drawable result)
		{
			if (result != null)
			{
				sLoader.onImageLoade(result);
			}
			super.onPostExecute(result);
		}
	}

	public interface OnBitmapLoader
	{
		void onImageLoade(Drawable drawable)
	}
}
