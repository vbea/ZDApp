package com.binxin.zdapp.activity;

import java.util.Date;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.LinearLayout;

import com.binxin.zdapp.R;

import com.binxin.zdapp.classes.ContactColumn;
import com.binxin.zdapp.classes.ContactsProvider;
import com.binxin.zdapp.classes.MyThemes;
import com.binxin.zdapp.classes.Common;
import com.binxin.zdapp.classes.Hanyu;

public class ContactEditor extends Activity
{
	public static int EDITOR_STATE = 0;
	private static final int STATE_EDIT = 0;
    private static final int STATE_INSERT = 1;
	private int mid = -1;
    private Cursor mCursor;
    private int mState;
    private Uri mUri;
    private EditText nameText, mobileText, homeText, addressText, emailText, blogText, qqtext;
	private TextView titleText;
    private Button okButton;
    private ImageButton cancelButton;
	private LinearLayout titleLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editorcontacts);
		
		nameText = (EditText) findViewById(R.id.EditText01);
        mobileText = (EditText) findViewById(R.id.EditText02);
        homeText = (EditText) findViewById(R.id.EditText03);
        addressText = (EditText) findViewById(R.id.EditText04);
        emailText = (EditText) findViewById(R.id.EditText05);
        blogText = (EditText) findViewById(R.id.EditText06);
		qqtext = (EditText) findViewById(R.id.EditText08);
		titleText = (TextView) findViewById(R.id.edit_text);
		okButton = (Button)findViewById(R.id.Button01);
        cancelButton = (ImageButton)findViewById(R.id.btn_close);
		titleLayout = (LinearLayout) findViewById(R.id.themeLayout);
		
		Intent intent = getIntent();
		if (intent.getData() == null) {
			intent.setData(ContactsProvider.CONTENT_URI);
		}
		mUri = intent.getData();
		
		if (EDITOR_STATE == STATE_INSERT)
		{
			mState = STATE_INSERT;
			titleText.setText(R.string.sm_add);
		}
		else if (EDITOR_STATE == STATE_EDIT)
		{
			mState = STATE_EDIT;
			titleText.setText(R.string.sm_editor);
			mid = (int)getIntent().getExtras().getLong("id");
		}
		
		okButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{
				String text = nameText.getText().toString();
				if(text.length()==0)
				{
					//如果没有输入内容，则不添加记录
					Common.showShortToast(getApplicationContext(),R.string.operate_fail);
					finish();
					overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
				}
				else
				{
					//添加一条数据
					updateContact();
					Common.showShortToast(getApplicationContext(),R.string.operate_success);
				}
			}
		});
        cancelButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{
				finish();
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
	}
	
	private void updateContact() 
	{
		Hanyu han = new Hanyu();
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date curdate = new Date(System.currentTimeMillis());
		String date = format.format(curdate);
		ContentValues values = new ContentValues();
		String name = nameText.getText().toString();
		values.put(ContactColumn.NAME, name);
		values.put(ContactColumn.SORT, han.getPinYinSoryKey(name));
		values.put(ContactColumn.MOBILENUM, mobileText.getText().toString());
		values.put(ContactColumn.HOMENUM, homeText.getText().toString());
		values.put(ContactColumn.QQ, qqtext.getText().toString());
		values.put(ContactColumn.ADDRESS, addressText.getText().toString());
		values.put(ContactColumn.EMAIL, emailText.getText().toString());
		values.put(ContactColumn.BLOG, blogText.getText().toString());
		values.put(ContactColumn.KEYS, han.getPinYinSoryKeys(name));
		values.put(ContactColumn.MODIFY, date);
		values.put(ContactColumn.DELETED, 0);
		if (mState == STATE_EDIT && mCursor != null)
           	getContentResolver().update(mUri, values, ContactColumn.ID + "=" + mid, null);
		else if (mState == STATE_INSERT)
		{
			values.put(ContactColumn.CREATE, date);
			getContentResolver().insert(mUri, values);
		}
		setResult(RESULT_CANCELED);
        finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	public void setMyTheme()
	{
		android.content.SharedPreferences sube = getSharedPreferences("zdapp", android.content.Context.MODE_PRIVATE);
		int code = sube.getInt("themeCode", 0);
		MyThemes.setThemes(this, titleLayout, code);
	}
	
	@Override
	protected void onResume()
	{
		setMyTheme();
		if (mState == STATE_EDIT)
		{
			mid = getIntent().getExtras().getInt("id");
			if (mid > 0)
			{
				mCursor = managedQuery(mUri, ContactColumn.PROJECTION, ContactColumn.ID + "=" + mid, null, ContactColumn.ID);
				// 读取并显示联系人信息
				if (mCursor != null && mCursor.moveToFirst())
				{
					String name = mCursor.getString(ContactColumn.NAME_COLUMN);
					String moblie = mCursor.getString(ContactColumn.MOBILENUM_COLUMN);
					String home = mCursor.getString(ContactColumn.HOMENUM_COLUMN);
					String qq = mCursor.getString(ContactColumn.QQ_COLUMN);
					String address = mCursor.getString(ContactColumn.ADDRESS_COLUMN);
					String email = mCursor.getString(ContactColumn.EMAIL_COLUMN);
					String blog = mCursor.getString(ContactColumn.BLOG_COLUMN);

					nameText.setText(name);
					mobileText.setText(moblie);
					homeText.setText(home);
					qqtext.setText(qq);
					addressText.setText(address);
					emailText.setText(email);
					blogText.setText(blog);
				}
			}
		}
		super.onResume();
	}

	@Override
	protected void onDestroy()
	{
		if (mCursor != null)
			mCursor.close();
		super.onDestroy();
	}
}
