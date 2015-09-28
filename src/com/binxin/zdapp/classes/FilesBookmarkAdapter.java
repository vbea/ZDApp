package com.binxin.zdapp.classes;

//文件管理-书签
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.drawable.Drawable;

import com.binxin.zdapp.R;

public class FilesBookmarkAdapter extends BaseAdapter
{
	private LayoutInflater inf;
	private List<FilesBookmark> mItems = new ArrayList<FilesBookmark>();
	
	public FilesBookmarkAdapter(Context context)
	{
		inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	//添加一项（一个书签）
	public void addItem(FilesBookmark it)
	{
		mItems.add(it);
	}
	
	//设置书签列表
	public void setListItems(List<FilesBookmark> lit)
	{
		mItems = lit;
	}
	
	//得到书签的数目,列表的个数
	public int getCount()
	{
		return mItems.size();
	}
	
	//得到一个书签
	public FilesBookmark getItem(int position)
	{
		return mItems.get(position);
	}
	
	//能否全部选中
	public boolean areAllItemsSelectable()
	{
		return false;
	}
	
	//得到一个书签的ID
	public long getItemId(int position)
	{
		return position;
	}
	
	//重写getView方法来返回一个FilesTextView（我们自定义的文件布局）对象
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder;
		if (convertView == null)
		{
			convertView = inf.inflate(R.layout.file_book,null);
			holder = new ViewHolder();
			holder.icon = (ImageView) convertView.findViewById(R.id.book_imgLayout);
			holder.name = (TextView) convertView.findViewById(R.id.file_bmname);

			convertView.setTag(holder);
		}
		else
			holder = (ViewHolder) convertView.getTag();

		Drawable icon = mItems.get(position).getIcon();
		holder.icon.setImageDrawable(icon);
		holder.name.setText(mItems.get(position).getBookName());
		return convertView;
	}

	public final class ViewHolder
	{
		public ImageView icon;
		public TextView name;
	}
}
