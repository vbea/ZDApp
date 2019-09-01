package com.binxin.zdapp.activity;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.content.Context;
import android.content.Intent;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.TextWatcher;
import android.text.Editable;

import com.binxin.zdapp.R;
import com.binxin.zdapp.classes.DBHelper;
import com.binxin.zdapp.classes.ContactColumn;
import com.binxin.zdapp.provider.ContactsProvider;
import com.binxin.zdapp.classes.ContactAdapter;
import com.binxin.zdapp.classes.MyThemes;
import com.binxin.zdapp.classes.Common;
import com.binxin.zdapp.classes.ExceptionHandler;

public class App_contact extends Activity
{
	private ListView mListView;
	private ContactAdapter mAdapter;
	private LinearLayout titleLayout,noneLayout,iconLayout,nonsLayout;
	private RelativeLayout searchLayout;
	private ImageButton back,btn_search;
	private ImageView titimage;
	private EditText txtEdit;
	private Cursor mCursor;
	private AlertDialog.Builder menuDialog;
	private int mCount = 0;
	private Uri mUri;
	private boolean isSearch = false;
	private String mKey;
	private String mSearchWhere;
	private InputMethodManager imm;
	private SharedPreferences spfc;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setTheme(android.R.style.Theme_DeviceDefault_Light_NoActionBar);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contacts);
		imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		
		back = (ImageButton) findViewById(R.id.btn_back);
		btn_search = (ImageButton) findViewById(R.id.btn_search);
		titimage = (ImageView) findViewById(R.id.cont_titImage);
		Button btn_newUser = (Button) findViewById(R.id.sm_newUser);
		txtEdit = (EditText) findViewById(R.id.cont_editext);
		mListView = (ListView) findViewById(R.id.ContactList);
		titleLayout = (LinearLayout) findViewById(R.id.themeLayout);
		noneLayout = (LinearLayout) findViewById(R.id.sm_noLayout);
		nonsLayout = (LinearLayout) findViewById(R.id.sm_noSLayout);
		iconLayout = (LinearLayout) findViewById(R.id.cont_titLayout);
		searchLayout = (RelativeLayout) findViewById(R.id.cont_searchLayout);
		spfc = getSharedPreferences("Contacts", Context.MODE_PRIVATE);
		init(); //运行程序
		back.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View view)
			{
				mCursor.close();
				finish();
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
		btn_newUser.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				ContactEditor.EDITOR_STATE = 1;
				Intent intent2 = new Intent();
				intent2.setClass(App_contact.this,ContactEditor.class);
				startActivity(intent2);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});
		btn_search.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View view)
			{
				searchLayout.setVisibility(View.VISIBLE);
				iconLayout.setVisibility(View.GONE);
				titimage.setImageResource(R.drawable.ic_title_back);
				isSearch = true;
				txtEdit.setFocusable(true);
				txtEdit.setFocusableInTouchMode(true);
				txtEdit.requestFocus();
				imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
			}
		});
		titimage.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (isSearch)
				{
					searchLayout.setVisibility(View.GONE);
					iconLayout.setVisibility(View.VISIBLE);
					titimage.setImageResource(R.drawable.ic_icon);
					nonsLayout.setVisibility(View.GONE);
					txtEdit.setText("");
					imm.hideSoftInputFromWindow(txtEdit.getWindowToken(), 0);
					isSearch = false;
					onResume();
				}
			}
		});
		txtEdit.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4)
			{
				
			}

			@Override
			public void onTextChanged(CharSequence p1, int p2, int p3, int p4)
			{
			
			}

			@Override
			public void afterTextChanged(Editable p1)
			{
				searchContacts(txtEdit.getText().toString().trim());
			}
		});
	}
	
	private void init()
	{
		Intent intent = getIntent();
        if (intent.getData() == null) {
            intent.setData(ContactsProvider.CONTENT_URI);
        }
		mUri = intent.getData();
		if (!spfc.getBoolean("updated", false))
		{
			if (getDatabasePath("ZDApp_Contacts.db").exists())
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("数据库升级");
				builder.setMessage("系统检测到您有旧数据库存在，是否要同步到新数据库中？");
				builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int s)
					{
						updateDatabase();
					}
				});
				builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int s)
					{
						dialog.cancel();
					}
				});
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		}
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> adapter, View v, int position, long id)
			{
				Intent intent = new Intent();
				intent.setClass(App_contact.this,ContactView.class);
				Bundle b = new Bundle();
				b.putInt("id",mAdapter.getItem(position).getInt(ContactColumn.ID_COLUMN));
				intent.putExtras(b);
				intent.setData(getIntent().getData());
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});
		mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
		{
			@Override
			public boolean onItemLongClick(AdapterView<?> adaper, View v, int position, long id)
			{
				OpenMenu(mAdapter.getItem(position));
				return true;
			}
		});
	}
	
	private void searchContacts(String key)
	{
		if (mKey == key)
			return;
		if (mAdapter != null)
			mAdapter.clear();
		mSearchWhere = ContactColumn.DELETED + "=0 and (" + ContactColumn.NAME + " like '%" + key + "%' or " + ContactColumn.KEYS + " like '%" + key + "%' or " + ContactColumn.MOBILENUM + " like '" + key + "%' or " + ContactColumn.SORT + " like '%" + key + "%')";
		mCursor = managedQuery(getIntent().getData(), ContactColumn.PROJECTION,mSearchWhere, null,ContactColumn.SORT);
		mCount = mCursor.getCount();
		if (mCount <= 0)
			nonsLayout.setVisibility(View.VISIBLE);
		else
		{
			nonsLayout.setVisibility(View.GONE);
			mAdapter = new ContactAdapter(this,mCursor,key);
		}
		mKey = key;
		mListView.setAdapter(mAdapter);
	}
	
	public void OpenMenu(final Cursor curcur)
	{
		//Common.showShortToast(getApplicationContext(),"id:" + id);
		if (curcur == null)
			return;
		final int id = curcur.getInt(ContactColumn.ID_COLUMN);
		final String txtMobile = curcur.getString(ContactColumn.MOBILENUM_COLUMN);
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				switch(which)
				{
					case 0://呼叫联系人
					{
						Intent call = new Intent(Intent.ACTION_CALL,mUri.parse("tel:"+ txtMobile));
						startActivity(call);
						break;
					}
					case 1://查看联系人
						Intent intent = new Intent();
						intent.setClass(App_contact.this,ContactView.class);
						Bundle b = new Bundle();
						b.putInt("id",id);
						intent.putExtras(b);
						intent.setData(getIntent().getData());
						startActivity(intent);
						overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
						break;
					case 2://发送短消息
						Intent sms = new Intent(Intent.ACTION_SENDTO,mUri.parse("smsto:"+ txtMobile));
						startActivity(sms);
						break;
					case 3://编辑联系人
					{
						ContactEditor.EDITOR_STATE = 0;
						Intent intent1 = new Intent();
						intent1.setClass(App_contact.this,ContactEditor.class);
						Bundle b1 = new Bundle();
						b1.putInt("id",id);
						intent1.putExtras(b1);
						intent1.setData(getIntent().getData());
						startActivity(intent1);
						overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
						break;
					}
					case 4://添加联系人
					{
						ContactEditor.EDITOR_STATE = 1;
						Intent intent2 = new Intent();
						intent2.setClass(App_contact.this,ContactEditor.class);
						startActivity(intent2);
						overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
						break;
					}
					case 5://删除联系人
					{
						AlertDialog.Builder builder = new AlertDialog.Builder (App_contact.this);
						builder.setTitle(R.string.delete_user);
						builder.setMessage(R.string.delete_warn);
						builder.setPositiveButton(R.string.ok, new AlertDialog.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int which)
							{
								int tmpCount = mCount;
								//删除一条记录
								ContentValues values = new ContentValues();
								values.put(ContactColumn.DELETED, 1);
								getContentResolver().update(mUri, values, ContactColumn.ID + "=" + id, null);
								onResume();
								if (tmpCount != mCount)
									Common.showShortToast(getApplicationContext(),getString(R.string.delete_success));
								else
									Common.showShortToast(getApplicationContext(),getString(R.string.delete_fail));
							}
						});
						builder.setNegativeButton(R.string.cancel, new AlertDialog.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int which)
							{
								dialog.cancel();
							}
						});
						builder.create();
						builder.show();
						break;
					}
				}
			}
		};
		//显示操作菜单
		String[] menu={getString(R.string.call_user),getString(R.string.view_user),getString(R.string.send_sms),getString(R.string.editor_user),getString(R.string.add_user),getString(R.string.delete)};
	    menuDialog = new AlertDialog.Builder(App_contact.this);
		menuDialog.setTitle(curcur.getString(ContactColumn.NAME_COLUMN));
		menuDialog.setItems(menu,listener);
		menuDialog.create();
		menuDialog.show();
	}
	
	//升级数据库
	private void updateDatabase()
	{
		Common.showShortToast(this,"开始更新");
		try
		{
			java.io.File path = getDatabasePath("ZDApp_Contacts.db");
			SQLiteDatabase sqldb = SQLiteDatabase.openOrCreateDatabase(path,null);
			Cursor oldcursor = sqldb.query("contacts",ContactColumn.PROJECTION,null,null,null,null,ContactColumn.ID);
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date curdate = new Date(System.currentTimeMillis());
			String date = format.format(curdate);
			while (oldcursor.moveToNext())
			{
				ContentValues values = new ContentValues();
				values.put(ContactColumn.NAME, oldcursor.getString(ContactColumn.NAME_COLUMN));
				values.put(ContactColumn.SORT, oldcursor.getString(ContactColumn.SORT_COLUMN));
				values.put(ContactColumn.MOBILENUM, oldcursor.getString(ContactColumn.MOBILENUM_COLUMN));
				values.put(ContactColumn.HOMENUM, oldcursor.getString(ContactColumn.HOMENUM_COLUMN));
				values.put(ContactColumn.QQ, oldcursor.getString(ContactColumn.QQ_COLUMN));
				values.put(ContactColumn.ADDRESS, oldcursor.getString(ContactColumn.ADDRESS_COLUMN));
				values.put(ContactColumn.EMAIL, oldcursor.getString(ContactColumn.EMAIL_COLUMN));
				values.put(ContactColumn.BLOG, oldcursor.getString(ContactColumn.BLOG_COLUMN));
				//values.put(ContactColumn.KEYS, oldcursor.getString(ContactColumn.KEYS_COLUMN));
				values.put(ContactColumn.DELETED, 0);
				values.put(ContactColumn.CREATE, date);
				values.put(ContactColumn.MODIFY, date);
				getContentResolver().insert(mUri, values);
				//setResult(RESULT_CANCELED);
			}
			Common.showShortToast(this,"更新完成，请重新打开通讯录");
			SharedPreferences.Editor edt = spfc.edit();
			edt.putBoolean("updated",true);
			edt.commit();
		}
		catch (Exception e)
		{
			ExceptionHandler.log("Update SQLite:"+e.toString());
			Common.showShortToast(this,"更新失败");
		}
	}

	@Override
	protected void onResume()
	{
		if (!isSearch)
		{
			if (mAdapter != null)
				mAdapter.clear();
			mCursor = managedQuery(mUri, ContactColumn.PROJECTION, ContactColumn.DELETED + "=0", null,ContactColumn.SORT);
			mCount = mCursor.getCount();
			if (mCount <= 0)
			{
				noneLayout.setVisibility(View.VISIBLE);
				btn_search.setEnabled(false);
			}
			else
			{
				noneLayout.setVisibility(View.GONE);
				mAdapter = new ContactAdapter(this,mCursor);
				btn_search.setEnabled(true);
			}
			mListView.setAdapter(mAdapter);
		}
		else
			searchContacts(txtEdit.getText().toString().trim());
		MyThemes.setThemes(this, titleLayout);
		super.onResume();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
	}

	@Override
	protected void onDestroy()
	{
		mCursor.close();
		super.onDestroy();
	}
}
