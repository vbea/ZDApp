package com.binxin.zdapp.classes;

import java.util.List;

import android.content.Context;
import android.widget.BaseAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.graphics.Bitmap;

import com.binxin.zdapp.R;
import com.binxin.zdapp.view.CircleImageView;

public class ThemeAdapter extends BaseAdapter
{
	private List<ThemeItem> mList;
	private LayoutInflater inflate;
	private int mWidth;
	
	public ThemeAdapter(Context context)
	{
		inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public ThemeAdapter(Context context, List<ThemeItem> items)
	{
		inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mList = items;
	}
	
	public void addItem(ThemeItem i)
	{
		mList.add(i);
	}
	
	public void setList(List<ThemeItem> list)
	{
		mList = list;
	}
	
	public void setWidth(int width)
	{
		mWidth = width + 15;
	}
	
	@Override
	public int getCount()
	{
		return mList.size();
	}

	@Override
	public ThemeItem getItem(int p1)
	{
		return mList.get(p1);
	}

	@Override
	public long getItemId(int p1)
	{
		return 0;
	}

	@Override
	public View getView(int p, View v, ViewGroup g)
	{
		ViewHolder holder;
		if (v == null)
		{
			v = inflate.inflate(R.layout.titmes, null);
			holder = new ViewHolder();
			holder.icon = (CircleImageView) v.findViewById(R.id.item_icon);
			holder.title = (TextView) v.findViewById(R.id.item_title);
			holder.tuse = (ImageView) v.findViewById(R.id.item_use);
			v.setTag(holder);
			//想要注释？
			//没有→_→
		}
		else
			holder = (ViewHolder) v.getTag();
		holder.icon.setImageBitmap(createColorBitmap(mList.get(p).rid));
		//int width = holder.title.getHeight();
		holder.title.setText(mList.get(p).name);
		holder.tuse.setVisibility(p==Common.APP_THEME_ID ? View.VISIBLE: View.INVISIBLE);
		return v;
	}
	
	private Bitmap createColorBitmap(int color)
	{
		Bitmap bmp = Bitmap.createBitmap(mWidth, mWidth, Bitmap.Config.RGB_565);
		bmp.eraseColor(color);
		return bmp;
	}

	public class ViewHolder
	{
		CircleImageView icon;
		TextView title;
		ImageView tuse;
	}
}
