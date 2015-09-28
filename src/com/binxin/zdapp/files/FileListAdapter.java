package com.binxin.zdapp.files;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.graphics.drawable.Drawable;

import com.binxin.zdapp.R;

public class FileListAdapter extends BaseAdapter
{
	private ArrayList<FileItem> list;
	private Context mContext;
	private LayoutInflater inflate;
	private Drawable[] icons = null;
	private OnCheckedChange listener;
	private FilesCommon mCommon;
	private boolean isHidden = false;

	public FileListAdapter(Context context, ArrayList<FileItem> _items, FilesCommon comn, String parent, OnCheckedChange listen)
	{
		list = _items;
		mContext = context;
		inflate = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		icons = new Drawable[_items.size()];
		listener = listen;
		mCommon = comn;
		isHidden = (parent.indexOf(".") == 0);
	}

	@Override
	public int getCount()
	{
		return list.size();
	}

	@Override
	public FileItem getItem(int p1)
	{
		return list.get(p1);
	}

	@Override
	public long getItemId(int p1)
	{
		return p1;
	}
	
	Drawable _tmp;
	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		final ViewHolder holder;
		final FileItem item;
		if (convertView == null)
		{
			convertView = inflate.inflate(R.layout.file_layout, null);
			holder = new ViewHolder();
			holder.icon = (ImageView) convertView.findViewById(R.id.file_imgLayout);
			holder.name = (TextView) convertView.findViewById(R.id.file_txtNameLayout);
			holder.detail = (TextView) convertView.findViewById(R.id.file_txtDetailLayout);
			holder.checkd = (CheckBox) convertView.findViewById(R.id.file_chkFile);
			convertView.setTag(holder);
		}
		else
			holder = (ViewHolder) convertView.getTag();
		item = list.get(position);
		holder.icon.setTag(item.fileName);
		holder.name.setText(item.fileName);
		holder.detail.setText(item.fileInfo);
		if (item.checkable)
		{
			holder.checkd.setVisibility(View.VISIBLE);
			holder.checkd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
			{
				@Override
				public void onCheckedChanged(CompoundButton p1, boolean checkd)
				{
					listener.onCheckeFinish(position, checkd);
				}
			});
			holder.checkd.setChecked(item.checked);
			if (holder.checkd.isChecked())
				holder.detail.setTextColor(mContext.getResources().getColor(R.color.file_decolor));
			else
				holder.detail.setTextColor(mContext.getResources().getColor(R.color.grey));
		}
		else
			holder.checkd.setVisibility(View.GONE);
		if (item.fileIcon == null)
		{
			if (Shipment.SHOW_THUMBNAIL)
			{
				_tmp = mCommon.getFileIcon(item.fileType);
				holder.icon.setImageDrawable(_tmp);
				if (item.fileType != FileType.FILE_TYPE_FOLDER)
					getFileIcon(holder.icon, _tmp, item);
				else
				{
					item.fileIcon = _tmp;
					holder.icon.setImageDrawable(item.fileIcon);
				}
			}
			else
			{
				item.fileIcon = mCommon.getFileIcon(item.fileType);
				holder.icon.setImageDrawable(item.fileIcon);
			}
		}
		else
			holder.icon.setImageDrawable(item.fileIcon);
		return convertView;
	}

	public final class ViewHolder
	{
		public ImageView icon;
		public TextView name;
		public TextView detail;
		public CheckBox checkd;
	}
	
	public interface OnCheckedChange
	{
		void onCheckeFinish(int position, boolean check)
	}
	
	private void getFileIcon(final ImageView v, final Drawable _tmp, final FileItem file)
	{
		
		if (file.isCmd || file.isHidden || isHidden)
		{
			file.fileIcon = _tmp;
			//v.setImageDrawable(_tmp);
			return;
		}
		switch (file.fileType)
		{
			case FileType.FILE_TYPE_APP:
			case FileType.FILE_TYPE_IMAGE:
			case FileType.FILE_TYPE_VIDEO:
				break;
			default:
				file.fileIcon = _tmp;
		}
		if (Shipment.FILE_IS_SCROLL)
		{
			return;
		}
		int fileType = 0;
		String path = "";
		switch (file.fileType)
		{
			case FileType.FILE_TYPE_APP:
				fileType = DrawableLoader.ICON_APK;
				path = file.filePath;
				break;
			case FileType.FILE_TYPE_IMAGE:
			case FileType.FILE_TYPE_VIDEO:
				fileType = DrawableLoader.ICON_IVY;
				path = mCommon.getThumbPath(file.filePath);
				break;
		}
		if (fileType > 0 && path != null)
		{
			DrawableLoader load = new DrawableLoader(mContext);
			load.setOnImageLoade(path, fileType, new DrawableLoader.OnDrawableLoader()
			{
				@Override
				public void onImageLoade(Drawable drawable)
				{
					if (drawable != null)
						file.fileIcon = drawable;
					else
						file.fileIcon = _tmp;
					if (v.getTag().toString().equals(file.fileName))
						v.setImageDrawable(file.fileIcon);
				}
			});
		}
		else
			file.fileIcon = _tmp;
	}
}
