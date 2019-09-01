package com.binxin.zdapp.classes;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.database.Cursor;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ImageView;
import android.text.Html;

import com.binxin.zdapp.R;

public class ContactAdapter extends BaseAdapter
{
	private LayoutInflater inf;
	private Context	mContext = null;
	private Cursor cursor = null;
	private String keys,name,mobile;
	private boolean isSearch = false;
	public ContactAdapter(Context context)
	{
		mContext = context;
		inf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	public ContactAdapter(Context context,Cursor c)
	{
		mContext = context;
		cursor = c;
		inf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	public ContactAdapter(Context context,Cursor c,String key)
	{
		mContext = context;
		cursor = c;
		keys = key;
		inf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (keys.length() > 0)
			isSearch = true;
	}
	//得到联系人的数目,列表的个数
	public int getCount()
	{
		if (cursor != null)
			return cursor.getCount();
		else
			return 0;
	}
	//得到一个联系人
	public Cursor getItem(int position)
	{
		if (cursor.moveToPosition(position))
			return cursor;
		else
			return null;
	}
	//清空数据
	public void clear()
	{
		if (cursor != null)
		{
			cursor.close();
			cursor = null;
		}
	}
	
	//能否全部选中
	public boolean areAllItemsSelectable() { return false; }
	//得到一个联系人的ID
	public long getItemId(int position) { return position; }
	//得到一个联系人的ID
	//重写getView方法来返回一个FilesTextView（我们自定义的文件布局）对象
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ContactHolder holder;
		if (convertView == null)
		{
			holder = new ContactHolder();
			convertView = inf.inflate(R.layout.sm_layout,null);
			//holder.photo = (ImageView) convertView.findViewById();
			holder.name = (TextView) convertView.findViewById(R.id.sm_txtNameLayout);
			holder.mobile = (TextView) convertView.findViewById(R.id.sm_txtNumberLayout);
				
			convertView.setTag(holder);
		}
		else
			holder = (ContactHolder) convertView.getTag();
		
		if (cursor != null)
		{
			cursor.moveToPosition(position);
			name = cursor.getString(ContactColumn.NAME_COLUMN);
			mobile = cursor.getString(ContactColumn.MOBILENUM_COLUMN);
			if (isSearch)
			{
				if (name.indexOf(keys) == 0)
					name = name.substring(0,keys.length()).replace(keys,"<b><tt>"+keys+"</tt></b>") + name.substring(keys.length());
				else if (mobile.indexOf(keys) == 0)
					mobile = mobile.substring(0,keys.length()).replace(keys,"<b><font color='#555555'>"+keys+"</font></b>") + mobile.substring(keys.length());
				holder.name.setText(Html.fromHtml(name));
				holder.mobile.setText(Html.fromHtml(mobile));
			}
			else
			{
				holder.name.setText(name);
				holder.mobile.setText(mobile);
			}
		}
		return convertView;
	}
	
	public final class ContactHolder
	{
		public ImageView photo;
		public TextView name;
		public TextView mobile;
	}
}
