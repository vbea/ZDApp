package com.binxin.zdapp.classes;
//文件管理
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.graphics.drawable.Drawable;

import com.binxin.zdapp.R;

//使用BaseAdapter来存储取得的文件
public class FilesTextListAdapter extends BaseAdapter
{
	private LayoutInflater inf;
	private Context	mContext = null;
	private Activity mActivity = null;
	private CompoundButton.OnCheckedChangeListener chListener;
	// 用于显示文件的列表
	private List<FilesText>	mItems	= new ArrayList<FilesText>();
	
	public FilesTextListAdapter(Context context,Activity v)
	{
		mContext = context;
		mActivity = v;
		inf = mActivity.getLayoutInflater().from(mActivity);
	}
	//添加一项（一个文件）
	public void addItem(FilesText it) { mItems.add(it); }
	//设置文件列表
	public void setListItems(List<FilesText> lit) { mItems = lit; }
	//得到文件的数目,列表的个数
	public int getCount() { return mItems.size(); }
	//得到一个文件
	public FilesText getItem(int position) { return mItems.get(position); }
	//能否全部选中
	public boolean areAllItemsSelectable() { return false; }
	//判断指定文件是否被选中
	public boolean isSelectable(int position) 
	{ 
		return mItems.get(position).isSelectable();
	}
	//得到一个文件的ID
	public long getItemId(int position) { return position; }
	//重写getView方法来返回一个FilesTextView（我们自定义的文件布局）对象
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder;
		if (convertView == null)
		{
			convertView = inf.inflate(R.layout.file_layout, parent, false);
			holder = new ViewHolder();
			holder.icon = (ImageView) convertView.findViewById(R.id.file_imgLayout);
			holder.name = (TextView) convertView.findViewById(R.id.file_txtNameLayout);
			holder.detail = (TextView) convertView.findViewById(R.id.file_txtDetailLayout);
			holder.checkd = (CheckBox) convertView.findViewById(R.id.file_chkFile);
			convertView.setTag(holder);
		}
		else
			holder = (ViewHolder) convertView.getTag();
			
		Drawable icon = mItems.get(position).getIcon();
		holder.icon.setImageDrawable(icon);
		holder.name.setText(mItems.get(position).getText());
		holder.detail.setText(mItems.get(position).getDetail());
		if (mItems.get(position).isSelectable())
		{
			holder.checkd.setVisibility(View.VISIBLE);
			holder.checkd.setTag(position);
			holder.checkd.setOnCheckedChangeListener(chListener);
			holder.checkd.setChecked(mItems.get(position).checked());
			if (holder.checkd.isChecked())
				holder.detail.setTextColor(mActivity.getResources().getColor(R.color.black));
			else
				holder.detail.setTextColor(mActivity.getResources().getColor(R.color.grey));
		}
		else
			holder.checkd.setVisibility(View.GONE);
		return convertView;
	}
	
	public class mHandler extends Handler
	{
		ImageView mImage;
		Drawable draw;
		public mHandler(ImageView v,Drawable d)
		{
			mImage = v;
			draw = d;
		}
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case 1:
					mImage.setImageDrawable(draw);
					break;
			}
			super.handleMessage(msg);
		}
	};
	
	public final class ViewHolder
	{
		public ImageView icon;
		public TextView name;
		public TextView detail;
		public CheckBox checkd;
	}
	public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener)
	{
		chListener = listener;
	}
	
}

