package com.binxin.zdapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.DialogInterface;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.binxin.zdapp.classes.ContactColumn;
import com.binxin.zdapp.classes.MyThemes;
import com.binxin.zdapp.classes.Common;
import com.binxin.zdapp.R;

public class ContactView extends Activity
{
	private LinearLayout titleLayout;
	private TextView mTextViewName, mTextViewMobile, mTextViewHome,mTextViewAddress, mTextViewEmail, mTextViewBlog, mTextViewQQ;
	private ImageButton btn_close,btn_edit;
    private Cursor mCursor;
    private Uri mUri;
	private int mid = -1;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_Holo_Light_NoActionBar);
		setContentView(R.layout.viewuser);
		
		mTextViewName = (TextView) findViewById(R.id.TextView_Name);
		mTextViewMobile = (TextView) findViewById(R.id.TextView_Mobile);
		mTextViewHome = (TextView) findViewById(R.id.TextView_Home);
		mTextViewQQ = (TextView) findViewById(R.id.TextView_qq);
		mTextViewAddress = (TextView) findViewById(R.id.TextView_Address);
		mTextViewEmail = (TextView) findViewById(R.id.TextView_Email);
		mTextViewBlog = (TextView) findViewById(R.id.TextView_Blog);
		titleLayout = (LinearLayout) findViewById(R.id.themeLayout);
		btn_close = (ImageButton)findViewById(R.id.btn_close);
		btn_edit = (ImageButton) findViewById(R.id.btn_edit);
		
		//获取对应联系人的信息
		btn_edit.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				toEdit();
			}
		});
		btn_close.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				finish();
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
	}
	
	private void toEdit()
	{
		ContactEditor.EDITOR_STATE = 0;
		Intent intent1 = new Intent();
		intent1.setClass(ContactView.this,ContactEditor.class);
		intent1.setData(mUri);
		Bundle b1 = new Bundle();
		b1.putInt("id",mid);
		intent1.putExtras(b1);
		startActivity(intent1);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}

	@Override
	protected void onResume()
	{
		mid = getIntent().getExtras().getInt("id");
		mUri = getIntent().getData();
		if (mid < 0)
		{
			btn_edit.setVisibility(View.INVISIBLE);
		}
		else
		{
			btn_edit.setVisibility(View.VISIBLE);
        	mCursor = managedQuery(mUri, ContactColumn.PROJECTION, ContactColumn.ID + "=" + mid, null, ContactColumn.ID);
			if(mCursor != null && mCursor.moveToFirst())
			{
				mTextViewName.setText(mCursor.getString(ContactColumn.NAME_COLUMN));
				mTextViewMobile.setText(mCursor.getString(ContactColumn.MOBILENUM_COLUMN));
				mTextViewHome.setText(mCursor.getString(ContactColumn.HOMENUM_COLUMN));
				mTextViewQQ.setText(mCursor.getString(ContactColumn.QQ_COLUMN));
				mTextViewAddress.setText(mCursor.getString(ContactColumn.ADDRESS_COLUMN));
				mTextViewEmail.setText(mCursor.getString(ContactColumn.EMAIL_COLUMN));
				mTextViewBlog.setText(mCursor.getString(ContactColumn.BLOG_COLUMN));
			}
		}
		MyThemes.setThemes(this, titleLayout);
		super.onResume();
	}
	
	// 删除联系人信息
	private void deleteContact()
	{
		if (mCursor != null)
		{
			ContentValues values = new ContentValues();
			values.put(ContactColumn.DELETED, 1);
			getContentResolver().update(mUri, values, ContactColumn.ID + "=" + mid, null);
			setResult(RESULT_CANCELED);
			Common.showShortToast(getApplicationContext(),R.string.delete_success);
		}
	}
	
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		//添加菜单
		//menu.add(0, REVERT_ID, 0, R.string.revert);
		menu.add(0, 0, 0, R.string.delete_user);
		menu.add(0, 1, 0, R.string.sm_editor);
		menu.add(0, 2, 0, R.string.call_user).setTitle(this.getResources().getString(R.string.call_user)+" \""+mTextViewName.getText()+"\"");
		menu.add(0, 3, 0, R.string.sendsms_user).setTitle(this.getResources().getString(R.string.sendsms_user)+" \""+mTextViewName.getText()+"\"");
		return true;
	}

    public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
				//删除
			case 0:
				Builder builder = new Builder (ContactView.this);
				builder.setTitle(R.string.delete_user);
				builder.setMessage(R.string.delete_warn);
				builder.setPositiveButton(android.R.string.ok,new AlertDialog.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which)
					{
						//删除一条记录
						deleteContact();
						finish();
						overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
					}
				});
				builder.setNegativeButton(android.R.string.cancel,new AlertDialog.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.cancel();
					}
				});
				builder.create();
				builder.show();
				break;
			case 1:
				//编辑联系人
				toEdit();
				break;
			case 2:
				//呼叫联系人
		        Intent call = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+mTextViewMobile.getText()));
		        startActivity(call);
				break;
			case 3:
				//发短信给联系人
		        Intent sms = new Intent(Intent.ACTION_SENDTO,Uri.parse("smsto:"+mTextViewMobile.getText()));
		        startActivity(sms);
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy()
	{
		mCursor.close();
		super.onDestroy();
	}
}
