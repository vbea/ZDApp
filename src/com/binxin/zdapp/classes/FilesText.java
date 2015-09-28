package com.binxin.zdapp.classes;

//import java.text.Collator;
//import java.text.CollationKey;
import android.os.Handler;
import android.os.Message;
import android.content.Context;
import android.graphics.drawable.Drawable;
//邠心工作室_文件管理器
public class FilesText implements Comparable<FilesText>
{
	/* 文件名 */
	private String mText = "";
	/* 文件的图标ICNO */
	private Drawable mIcon = null;
	/* 文件时间&大小 */
	private String detail = "";
	/* 路径 */
	private String mPath = "";
	/* 文件类型 */
	private String type = "";
	/* 能否选中 */
	private boolean	mSelectable	= true;
	/* 是否选中 */
	private boolean mChecked = false;
	/* 图片是否加载 */
	private boolean loadImage = false;
	//缩略图相关
	private int thmode = 0;
	public final int MODE_IMG = 10;
	public final int MODE_APK = 11;
	private FileCommon file_common;
	private Context mContext;
	//private Collator colla = Collator.getInstance();
	public FilesText(String text, String path, Drawable bullet, String deta)
	{
		mIcon = bullet;
		mText = text;
		mPath = path;
		detail = deta;
		mSelectable = false;
	}
	
	public FilesText(String text, String path, Drawable bullet, String deta, String _type)
	{
		mIcon = bullet;
		mText = text;
		mPath = path;
		detail = deta;
		type = _type;
		mSelectable = true;
	}
	
	public FilesText(Context context,String text, String path, Drawable bullet, String deta, int mode, String _type)
	{
		mContext = context;
		mIcon = bullet;
		mText = text;
		mPath = path;
		detail = deta;
		thmode = mode;
		type = _type;
		mSelectable = true;
		file_common = new FileCommon(context);
		if (thmode == MODE_APK)
			mhandler.sendEmptyMessage(MODE_APK);
	}
	
	public FilesText(String text, Drawable bullet, String deta)
	{
		mIcon = bullet;
		mText = text;
		mPath = text;
		detail = deta;
		mSelectable = false;
	}
	//是否可以选中
	public boolean isSelectable()
	{
		return mSelectable;
	}
	//设置是否可用选中
	public void setSelectable(boolean selectable)
	{
		mSelectable = selectable;
	}
	//是否选择
	public boolean checked()
	{
		return mChecked;
	}
	public void setChecked(boolean check)
	{
		mChecked = check;
	}
	//得到文件名
	public String getText()
	{
		return mText;
	}
	public String getPath()
	{
		return mPath;
	}
	public void setPath(String path)
	{
		this.mPath = path;
	}
	public int getMode()
	{
		return thmode;
	}
	public void setType(String ty)
	{
		type = ty;
	}
	public String getType()
	{
		return type;
	}
	//设置文件名
	public void setText(String text)
	{
		if (text.substring(0,1).equals("/"))
		{
			mText = text.substring(1);
		}
		else
			mText = text;
	}
	//设置图标
	public void setIcon(Drawable icon)
	{
		mIcon = icon;
	}
	//得到图标
	public Drawable getIcon()
	{
		return mIcon;
	}
	//设置大小
	public void setDetail(String s)
	{
		detail = s;
	}
	//得到大小
	public String getDetail()
	{
		return detail;
	}
	//图片是否加载完成
	public boolean getImgLoadState()
	{
		return loadImage;
	}
	//加载缩略图
	public void loadThumbnail()
	{
		mhandler.sendEmptyMessage(MODE_IMG);
	}
	//比较文件名是否相同
	public int compareTo(FilesText other)
	{
		if (this.mText != null)
		{
			//CollationKey key1 = colla.getCollationKey(this.mText);
			//CollationKey key2 = colla.getCollationKey(other.getText());
			return this.mText.toLowerCase().compareTo(other.getText().toLowerCase());
			//return key1.compareTo(key2);
		}
		else
			throw new IllegalArgumentException();
	}
	
	Handler mhandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			Drawable _icon;
			switch (msg.what)
			{
				case MODE_APK:
					_icon = file_common.getApkIcon(mContext,mPath);
					if (_icon != null)
						mIcon = _icon;
					loadImage = true;
					break;
				case MODE_IMG:
					_icon = file_common.getThumbFromBitmap(mPath,500);
					if (_icon != null)
						mIcon = _icon;
					loadImage = true;
					break;
			}
			super.handleMessage(msg);
		}
	};
}
