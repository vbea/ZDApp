package com.binxin.zdapp.activity;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;

import android.app.Dialog;
import android.app.AlertDialog;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Bundle;
import android.os.StatFs;
import android.os.Message;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.BaseAdapter;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.PopupWindow;
import android.widget.ImageView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TableRow;
import android.net.Uri;

import com.binxin.zdapp.R;
import com.binxin.zdapp.classes.MD5Util;
import com.binxin.zdapp.files.FileIcon;
import com.binxin.zdapp.files.FileItem;
import com.binxin.zdapp.files.FileType;
import com.binxin.zdapp.files.FileSort;
import com.binxin.zdapp.files.DrawableLoader;
import com.binxin.zdapp.files.FilesCommon;
import com.binxin.zdapp.files.FileTemp;
import com.binxin.zdapp.files.FileListAdapter;
import com.binxin.zdapp.files.Shipment;
import com.binxin.zdapp.files.FileOperate;
import com.binxin.zdapp.classes.ExceptionHandler;
import com.binxin.zdapp.classes.ProgressClass;
import com.binxin.zdapp.classes.Common;
import com.binxin.zdapp.classes.MyThemes;

//正德应用6-新版文件管理器
//邠心工作室制作
//开始时间：2015.09.11
//结束时间：2015.09.25
public class App_filemanager extends Activity
{
	private FileListAdapter mAdapter;//适配器
	private ArrayList<FileItem> mFileList = new ArrayList<FileItem>();//文件列表
	private ArrayList<FileItem> tFolderList = new ArrayList<FileItem>();//临时文件夹列表
	private ArrayList<FileItem> tFileList = new ArrayList<FileItem>();//临时文件列表
	private FilesCommon mCommon;//文件操作类
	private ArrayList<FileTemp> tmpFilesList = new ArrayList<FileTemp>();//文件剪切板
	private Dialog dialogWait;
	private AlertDialog.Builder dialogTip;//提示对话框
	private AlertDialog.Builder dialogOpr;//文件操作对话框
	private AlertDialog.Builder menuDialog;//菜单对话框
	private AlertDialog.Builder attrDialog;//属性对话框
	private ProgressDialog progressDialog;//进度对话框
	private FolderSizeThread folderThread = null;
	private FileMD5Thread md5thread = null;
	private ListView mListview;
	private TextView attr_md5,attr_sha1,attr_size,attr_name,attr_path,attr_time,
					attr_mime,attr_md5t,attr_attr,attr_owner,attr_user,attr_link;//属性控件
	private TableRow attr_sha1l,attr_mimel,attr_linkl;
	private TextView txt_title, txt_titPath, txt_storage, txt_exFilemsg, copy_fileName, copy_Percent;
	private EditText txt_edit;
	private CheckBox chk_exsit;
	private ProgressBar fileProgress;
	private SharedPreferences file_spf,book_spf;
	private LinearLayout menuBar, copyBar, titleLayout, selectBar;
	private ImageButton menu_add,menu_refresh,menu_sort,menu_home,menu_sdcard,menu_bookmark,btn_bookmark;//MenuBar
	private ImageButton selc_copy,selc_cut,selc_delete,selc_all,selc_share,selc_cancel,selc_more;//SelectBar
	private Button btn_paste, btn_cancel;//CopyBar
	private RelativeLayout footLayout;//底部工具栏
	private final String ROOT_PATH = "/";//根目录
	private boolean IS_INIT = true;
	private boolean SHOW_HIDE = false;//显示隐藏文件
	private boolean SHOW_DETAIL = false;//显示文件详细信息
	private boolean SELECTED = false;//选择器状态
	private String ATTR_PATH = "";
	//private boolean TIP_STATUS = false;
	private boolean IS_CMD = false;
	private String FILE_SORT;//文件排序
	private int FILE_CHECK_COUNT = 0;
	private File CurrentDirectory = null;//当前目录
	private FileOperate fileOperate;
	private LayoutInflater inflate;
	public static boolean BOOKMARK_STATUS = false;
	public static String BOOKMARK_PATH = "/";
	//private int SCROLL_Y = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setTheme(android.R.style.Theme_DeviceDefault_Light_NoActionBar);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.file_manage);
		
		titleLayout = (LinearLayout) findViewById(R.id.themeLayout);
		ImageButton back = (ImageButton) findViewById(R.id.btn_close);
		menuBar = (LinearLayout) findViewById(R.id.file_menuBar);
		copyBar = (LinearLayout) findViewById(R.id.file_copyBar);
		selectBar = (LinearLayout) findViewById(R.id.file_selectBar);
		footLayout = (RelativeLayout) findViewById(R.id.file_footLayout);
		mListview = (ListView) findViewById(R.id.file_listview);
		txt_title = (TextView) findViewById(R.id.file_titleName);
		txt_titPath = (TextView) findViewById(R.id.file_titlePath);
		txt_storage = (TextView) findViewById(R.id.file_txtstorage);
		file_spf = getSharedPreferences("files", Context.MODE_PRIVATE);
		book_spf = getSharedPreferences("bookmarker", Context.MODE_PRIVATE);
		SHOW_HIDE = file_spf.getBoolean("hide",false);
		SHOW_DETAIL = file_spf.getBoolean("detail", false);
		Shipment.SGOW_DETAILS = SHOW_DETAIL;
		Shipment.SHOW_THUMBNAIL = file_spf.getBoolean("thumb", false);
		initialization();
		back.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				saveFilePath();
				finish();
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
	}
	
	private void initialization()
	{
		mCommon = new FilesCommon(this);
		mListview.setOnScrollListener(new FastScrollListener());
		mListview.setLongClickable(true);
		inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		menu_add = (ImageButton) findViewById(R.id.file_imbtnAdd);
		menu_refresh =  (ImageButton) findViewById(R.id.file_imbtnRefresh);
		menu_sort =  (ImageButton) findViewById(R.id.file_imbtnSort);
		menu_sdcard =  (ImageButton) findViewById(R.id.file_imbtnSd);
		menu_home =  (ImageButton) findViewById(R.id.file_imbtnHome);
		menu_bookmark =  (ImageButton) findViewById(R.id.file_imbtnBookmark);
		selc_copy =  (ImageButton) findViewById(R.id.file_imbtnCopy);
		selc_cut =  (ImageButton) findViewById(R.id.file_imbtnCut);
		selc_delete =  (ImageButton) findViewById(R.id.file_imbtnDelete);
		selc_all =  (ImageButton) findViewById(R.id.file_imbtnSelectAll);
		selc_share =  (ImageButton) findViewById(R.id.file_imbtnShare);
		selc_cancel =  (ImageButton) findViewById(R.id.file_imbtnCancel);
		selc_more =  (ImageButton) findViewById(R.id.file_imbtnMore);
		btn_paste =  (Button) findViewById(R.id.file_btnPaste);
		btn_cancel =  (Button) findViewById(R.id.file_btnCancel);
		btn_bookmark = (ImageButton) findViewById(R.id.file_btnBookmark);
		getSortMethod(true);
		browseTo(new File(getSavePath()));
		mListview.setOnItemClickListener(new AbsListView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> l, View v, int position, long id)
			{
				// 取得选中的一项的文件名
				FileItem selectedFile = mFileList.get(position);
				if (selectedFile.fileName.equals(getResources().getString(R.string.up_one_level)))
				{
					if (!SELECTED)
					//返回上一级目录
						upOneLevel(selectedFile.isCmd);
				}
				else
				{
					if (SELECTED)
					{
						mFileList.get(position).toogle();
						refreshListView();
					}
					else
					{
						File clickedFile = new File(selectedFile.filePath);
						if(clickedFile != null)
						{
							browseTo(clickedFile, mFileList.get(position).fileType, mFileList.get(position).isCmd);
						}
					}
				}
			}
		});
		mListview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
		{
			public boolean onItemLongClick(AdapterView<?> p1, View p2, int position, long id)
			{
				//Common.showShortToast(getApplicationContext(), "long");
				if (!SELECTED)
				{
					FileItem tmpFile = mFileList.get(position);
					if (!tmpFile.fileName.equals(getResources().getString(R.string.up_one_level)))
					{
						//显示操作菜单
						String[] menu;
						menuDialog = new AlertDialog.Builder(App_filemanager.this);
						menuDialog.setTitle(tmpFile.fileName);
						if (tmpFile.isCmd)
						{
							menu = new String[]{getString(R.string.open), getString(R.string.attr), getString(R.string.bookmark)};
							menuDialog.setItems(menu,new FileCmdListener(tmpFile));
						}
						else {
							if (tmpFile.fileType.equals(FileType.FILE_TYPE_FOLDER))
							{
								menu = new String[]{getString(R.string.open), getString(R.string.rename), getString(R.string.delete),
									getString(R.string.dc_copy), getString(R.string.cut), getString(R.string.attr), getString(R.string.bookmark)};
								menuDialog.setItems(menu,new FolderListener(tmpFile));
							}
							else
							{
								menu = new String[]{getString(R.string.open), getString(R.string.rename), getString(R.string.delete), getString(R.string.dc_copy),
									getString(R.string.cut), getString(R.string.send), getString(R.string.attr), getString(R.string.bookmark)};
								menuDialog.setItems(menu,new FileListener(tmpFile));
							}
						}
						AlertDialog dialog = menuDialog.create();
						dialog.show();
					}
					return true;
				}
				else
					return false;
			}
		});
		menu_add.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int witch)
					{
						switch (witch)
						{
							case 0:
							{
								showEditDialog(getString(R.string.newfolder), getString(R.string.newfolder), new AlertDialog.OnClickListener()
								{
									public void onClick(DialogInterface dialog, int which)
									{
										String value = txt_edit.getText().toString();
										if (value.trim().length() <= 0)
										{
											Common.showShortToast(getApplicationContext(),R.string.folder_new_empty);
											return;
										}
										if (mCommon.newFolder(CurrentDirectory.getAbsolutePath() + File.separator + value))
										{
											Common.showShortToast(getApplicationContext(),R.string.operate_success);
											refreshList();
										}
										else
											Common.showShortToast(getApplicationContext(),R.string.operate_fail);
									}
								});
								break;
							}
							case 1:
							{
								showEditDialog(getString(R.string.newfile), getString(R.string.newfile), new AlertDialog.OnClickListener()
								{
									public void onClick(DialogInterface dialog, int which)
									{
										String value = txt_edit.getText().toString();
										if (value.trim().length() <= 0)
										{
											Common.showShortToast(getApplicationContext(),R.string.file_new_empty);
											return;
										}
										if (mCommon.newFile(CurrentDirectory.getAbsolutePath() + File.separator + value))
										{
											Common.showShortToast(getApplicationContext(),R.string.operate_success);
											refreshList();
										}
										else
											Common.showShortToast(getApplicationContext(),R.string.operate_fail);
									}
								});
								break;
							}
						}
					}
				};
				String[] menu = {getString(R.string.newfolder), getString(R.string.newfile)};
				menuDialog = new AlertDialog.Builder(App_filemanager.this);
				menuDialog.setTitle(R.string.file_new);
				menuDialog.setItems(menu,listener);
				menuDialog.create();
				menuDialog.show();
			}
		});
		menu_home.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				browseTo(new File(ROOT_PATH));
			}
		});
		menu_sdcard.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (mCommon.ExistSDCard())
					browseTo(mCommon.getSDCard());
				else
					Common.showShortToast(getApplicationContext(), R.string.sd_notf);
			}
		});
		menu_refresh.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				refreshList();
			}
		});
		menu_sort.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				String[] menu = {getString(R.string.sort_name), getString(R.string.sort_time), getString(R.string.sort_size), getString(R.string.sort_type),
								getString(R.string.sort_name_down), getString(R.string.sort_time_down), getString(R.string.sort_size_down)};
				menuDialog = new AlertDialog.Builder(App_filemanager.this);
				menuDialog.setTitle(R.string.file_sort);
				final int item = getSortMethod(false);
				menuDialog.setSingleChoiceItems(menu, item, new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int witch)
					{
						if (item != witch)
						{
							SharedPreferences.Editor edt = file_spf.edit();
							edt.putInt("sort", witch);
							edt.commit();
							getSortMethod(true);
							mFileList.clear();
							if (CurrentDirectory.getParent() != null)//添加上一级目录
								mFileList.add(new FileItem(getString(R.string.up_one_level), "", getString(R.string.up_txt_level), FileType.FILE_TYPE_FOLDER, 0, 0));
							sortList(tFolderList, FILE_SORT, true);
							sortList(tFileList, FILE_SORT, false);
							mFileList.addAll(tFolderList);
							mFileList.addAll(tFileList);
							mAdapter = new FileListAdapter(App_filemanager.this, mFileList, mCommon, CurrentDirectory.getName(), new OnCheckdChange());
							mListview.setAdapter(mAdapter);
						}
						dialog.cancel();
					}
				});
				menuDialog.create();
				menuDialog.show();
			}
		});
		menu_bookmark.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				startActivity(new Intent(App_filemanager.this, Bookmark.class));
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});
		btn_bookmark.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				startActivity(new Intent(App_filemanager.this, Bookmark.class));
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});
		selc_copy.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				getPasteFiles(mCommon.getCheckedFile(mFileList), FileTemp.ACTION_COPY);
				mCommon.selectNotAll(mFileList);
				refreshListView();
				showCopyView();
			}
		});
		selc_cut.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				getPasteFiles(mCommon.getCheckedFile(mFileList), FileTemp.ACTION_CUT);
				mCommon.selectNotAll(mFileList);
				refreshListView();
				showCopyView();
			}
		});
		selc_all.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (FILE_CHECK_COUNT != mCommon.getCanSelectFile(mFileList))
					mCommon.selectAll(mFileList);
				else
					mCommon.selectNotAll(mFileList);
				refreshListView();
			}
		});
		selc_more.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (FILE_CHECK_COUNT != 1)
					return;
				FileItem tmpFile = mCommon.getCheckedFilesList(mFileList).get(0);
				if (!tmpFile.fileName.equals(getResources().getString(R.string.up_one_level)))
				{
					//显示操作菜单
					String[] menu;
					menuDialog = new AlertDialog.Builder(App_filemanager.this);
					menuDialog.setTitle(tmpFile.fileName);
					if (tmpFile.fileType.equals(FileType.FILE_TYPE_FOLDER))
					{
						menu = new String[]{getString(R.string.open), getString(R.string.rename), getString(R.string.delete),
							getString(R.string.dc_copy), getString(R.string.cut), getString(R.string.attr), getString(R.string.bookmark)};
						menuDialog.setItems(menu,new FolderListener(tmpFile));
					}
					else
					{
						menu = new String[]{getString(R.string.open), getString(R.string.rename), getString(R.string.delete), getString(R.string.dc_copy),
							getString(R.string.cut), getString(R.string.send), getString(R.string.attr), getString(R.string.bookmark)};
						menuDialog.setItems(menu,new FileListener(tmpFile));
					}
					menuDialog.create();
					menuDialog.show();
				}
				mCommon.selectNotAll(mFileList);
				refreshListView();
			}
		});
		selc_cancel.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				mCommon.selectNotAll(mFileList);
				refreshListView();
			}
		});
		selc_delete.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View p1)
			{
				if (FILE_CHECK_COUNT == 1)
				{
					File file = new File(mCommon.getCheckedFilePath(mFileList).get(0));
					if (file.isFile())
						deleteFile(file);
					else
						deleteFolder(file);
				}
				else if (FILE_CHECK_COUNT > 1)
				{
					deleteFiles(mCommon.getCheckedFile(mFileList));
				}
			}
		});
		selc_share.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View p1)
			{
				mCommon.sendFiles(mCommon.getCheckedFile(mFileList));
				mCommon.selectNotAll(mFileList);
				refreshListView();
			}
		});
		
		//长按事件
		menu_add.setOnLongClickListener(new View.OnLongClickListener()
		{
			public boolean onLongClick(View v)
			{
				Common.showShortToast(getApplicationContext(), R.string.file_new);
				return true;
			}
		});
		menu_home.setOnLongClickListener(new View.OnLongClickListener()
		{
			public boolean onLongClick(View v)
			{
				Common.showShortToast(getApplicationContext(), R.string.file_home);
				return true;
			}
		});
		menu_sdcard.setOnLongClickListener(new View.OnLongClickListener()
		{
			public boolean onLongClick(View v)
			{
				Common.showShortToast(getApplicationContext(), R.string.file_sd);
				return true;
			}
		});
		menu_refresh.setOnLongClickListener(new View.OnLongClickListener()
		{
			public boolean onLongClick(View v)
			{
				Common.showShortToast(getApplicationContext(), R.string.refresh);
				return true;
			}
		});
		menu_sort.setOnLongClickListener(new View.OnLongClickListener()
		{
			public boolean onLongClick(View v)
			{
				Common.showShortToast(getApplicationContext(), R.string.file_sort);
				return true;
			}
		});
		menu_bookmark.setOnLongClickListener(new View.OnLongClickListener()
		{
			public boolean onLongClick(View v)
			{
				Common.showShortToast(getApplicationContext(), R.string.file_book);
				return true;
			}
		});
		btn_bookmark.setOnLongClickListener(new View.OnLongClickListener()
		{
			public boolean onLongClick(View v)
			{
				Common.showShortToast(getApplicationContext(), R.string.file_book);
				return true;
			}
		});
		selc_copy.setOnLongClickListener(new View.OnLongClickListener()
		{
			public boolean onLongClick(View v)
			{
				Common.showShortToast(getApplicationContext(), R.string.dc_copy);
				return true;
			}
		});
		selc_cut.setOnLongClickListener(new View.OnLongClickListener()
		{
			public boolean onLongClick(View v)
			{
				Common.showShortToast(getApplicationContext(), R.string.cut);
				return true;
			}
		});
		selc_all.setOnLongClickListener(new View.OnLongClickListener()
		{
			public boolean onLongClick(View v)
			{
				Common.showShortToast(getApplicationContext(), R.string.check_all);
				return true;
			}
		});
		selc_more.setOnLongClickListener(new View.OnLongClickListener()
		{
			public boolean onLongClick(View v)
			{
				Common.showShortToast(getApplicationContext(), R.string.more);
				return true;
			}
		});
		selc_cancel.setOnLongClickListener(new View.OnLongClickListener()
		{
			public boolean onLongClick(View v)
			{
				Common.showShortToast(getApplicationContext(), R.string.cancel);
				return true;
			}
		});
		selc_delete.setOnLongClickListener(new View.OnLongClickListener()
		{
			public boolean onLongClick(View v)
			{
				Common.showShortToast(getApplicationContext(), R.string.delete);
				return true;
			}
		});
		selc_share.setOnLongClickListener(new View.OnLongClickListener()
		{
			public boolean onLongClick(View v)
			{
				Common.showShortToast(getApplicationContext(), R.string.file_send);
				return true;
			}
		});
		btn_paste.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				mCommon.getTmpFilesNewFile(tmpFilesList, CurrentDirectory.getAbsolutePath());
				getTmpfileExistAction(mCommon.getTmpFileExistCount(tmpFilesList));
				//Common.showShortToast(getApplicationContext(), "重复项目：" + mCommon.getTmpFileExistCount(tmpFilesList));
			}
		});
		btn_cancel.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				//mCommon.showDialogTip(dialogTip, "文件剪切板", mCommon.getTempFileList(tmpFilesList), null);
				tmpFilesList.clear();
				showCopyView();
			}
		});
	}
	
	//返回上一级目录
	private void upOneLevel(boolean cmd)
	{
		if(CurrentDirectory.getParent() != null)
		{
			browseTo(CurrentDirectory.getParentFile(),FileType.FILE_TYPE_FOLDER, cmd);
		}
		else
			this.browseTo(new File(ROOT_PATH));
	}
	
	private void refreshList()
	{
		browseTo(CurrentDirectory);
	}
	
	private void browseTo(File file)
	{
		browseTo(file, FileType.FILE_TYPE_FOLDER, false);
	}
	
	//浏览指定的目录,如果是文件则进行打开操作
	private void browseTo(File file, String type, boolean cmd)
	{
		IS_CMD = false;
		try
		{
			if (type.equals(FileType.FILE_TYPE_FOLDER))
			{
				/*if (cmd)
				{
					throw new Exception();
				}*/
				//setTitles(file);
				CurrentDirectory = file;
				fill(file.listFiles());
			}
			else
			{
				mCommon.openFile(file, type);
			}
		}
		catch (Exception e)
		{
			//setTitles(file);
			CurrentDirectory = file;
			//showDialogTip("cmd", Common.Join(mCommon.getFileFirdtList(file.getAbsolutePath()),","));
			fillcmd(file.getAbsolutePath());
		}
	}
	
	private void fillcmd(String path)
	{
		IS_CMD = true;
		mFileList.clear();
		tFolderList.clear();
		tFileList.clear();
		mFileList.add(new FileItem(getString(R.string.up_one_level), "", getString(R.string.up_txt_level), FileType.FILE_TYPE_FOLDER, 0, 0));
		if (mCommon.getFileListToCmd(path, tFolderList, tFileList) > 0)
		{
			sortList(tFolderList, FILE_SORT, true);
			sortList(tFileList, FILE_SORT, false);
			mFileList.addAll(tFolderList);
			mFileList.addAll(tFileList);
		}
		mAdapter = new FileListAdapter(this, mFileList, mCommon, CurrentDirectory.getName(), new OnCheckdChange());
		//为ListActivity添加一个ListAdapter
		mListview.setAdapter(mAdapter);
		refreshListView();
	}
	
	private void fill(File[] files)
	{
		//清空列表
		mFileList.clear();
		tFolderList.clear();
		tFileList.clear();
		if (CurrentDirectory.getParent() != null)//添加上一级目录
			mFileList.add(new FileItem(getString(R.string.up_one_level), "", getString(R.string.up_txt_level), FileType.FILE_TYPE_FOLDER, 0, 0));
		//Drawable currentIcon = null;//文件图标
		String fileType = "";//文件类型
		String details = "";//文件信息
		long fileSize = 0;//文件大小
		for (File currentFile : files)
		{
			if (!SHOW_HIDE)//判断是否显示隐藏文件
			{
				if (currentFile.getName().substring(0,1).equals("."))
					continue;
				if (currentFile.isHidden())
					continue;
			}
			fileType = "";
			details = "";
			fileSize = mCommon.getFileSize(currentFile);
			if (SHOW_DETAIL)//判断是否显示文件详细信息
			{
				details = mCommon.getFileDate(currentFile);
				if (currentFile.isFile())
					details += " " + mCommon.formatSize(fileSize);
			}
			if (currentFile.isDirectory())
			{
				//添加文件夹
				fileType = FileType.FILE_TYPE_FOLDER;
				tFolderList.add(new FileItem(currentFile.getName(), currentFile.getAbsolutePath(), details, fileType, currentFile.lastModified(), true, false));
			}
			else
			{
				//添加文件
				fileType = mCommon.getFileType(currentFile);
				//currentIcon = mCommon.getFileIcon(fileType);
				tFileList.add(new FileItem(currentFile.getName(), currentFile.getAbsolutePath(), details, fileType, fileSize, currentFile.lastModified(), true, false));
			}
		}
		//添加
		sortList(tFolderList, FILE_SORT, true);
		sortList(tFileList, FILE_SORT, false);
		//排序
		mFileList.addAll(tFolderList);
		mFileList.addAll(tFileList);
		mAdapter = new FileListAdapter(this, mFileList, mCommon, CurrentDirectory.getName(), new OnCheckdChange());
		mListview.setAdapter(mAdapter);
		refreshListView();
	}
	//文件排序方法
	private void sortList(ArrayList<FileItem> list, String method, boolean folder)
	{
		switch(method)
		{
			case FileSort.SORT_NAME:
			case FileSort.SORT_NAME_DOWN:
			case FileSort.SORT_TIME:
			case FileSort.SORT_TIME_DOWN:
				Collections.sort(list, new FileSort(method));
				break;
			case FileSort.SORT_SIZE:
			case FileSort.SORT_SIZE_DOWN:
			case FileSort.SORT_TYPE:
			{
				if (folder)
					Collections.sort(list, new FileSort(FileSort.SORT_NAME));
				else
					Collections.sort(list, new FileSort(method));
				break;
			}
		}
	}
	//获取排序方式
	private int getSortMethod(boolean init)
	{
		int index = file_spf.getInt("sort", 0);
		if (!init)
			return index;
		switch (index)
		{
			case 0:
				FILE_SORT = FileSort.SORT_NAME;
				break;
			case 1:
				FILE_SORT = FileSort.SORT_TIME;
				break;
			case 2:
				FILE_SORT = FileSort.SORT_SIZE;
				break;
			case 3:
				FILE_SORT = FileSort.SORT_TYPE;
				break;
			case 4:
				FILE_SORT = FileSort.SORT_NAME_DOWN;
				break;
			case 5:
				FILE_SORT = FileSort.SORT_TIME_DOWN;
				break;
			case 6:
				FILE_SORT = FileSort.SORT_SIZE_DOWN;
				break;
		}
		return index;
	}
	
	//保存当前浏览的位置
	SharedPreferences.Editor _edt;
	private void saveFilePath()
	{
		_edt = file_spf.edit();
		_edt.putString("ZDpath", CurrentDirectory.getAbsolutePath());
		_edt.commit();
	}
	
	//得到保存的位置
	private String getSavePath()
	{
		return file_spf.getString("ZDpath", "/");
	}
	
	String _titleString = "";
	//设置标题
	private void setTitles(File file)
	{
		_titleString = "";
		if (file.getParent() != null)
		{
			txt_title.setText(file.getName());
			txt_titPath.setVisibility(View.VISIBLE);
			txt_titPath.setText(file.getParent());
		}
		else
		{
			txt_title.setText(file.getAbsolutePath());
			txt_titPath.setVisibility(View.GONE);
		}
		if (file.getTotalSpace() > 0)
		{
			_titleString = getString(R.string.stor_use) + mCommon.formatSize(file.getTotalSpace() - file.getFreeSpace()) + " " + getString(R.string.stor_free) + mCommon.formatSize(file.getFreeSpace());
			if (file.canRead() && file.canWrite())
				_titleString = _titleString + " " + getString(R.string.stor_read) + "/" + getString(R.string.stor_write);
			else if (file.canRead() && !file.canWrite())
				_titleString = _titleString + " " + getString(R.string.stor_only);
		}
		else
			_titleString = getString(R.string.stor_only);
		txt_storage.setText(_titleString);
	}
	
	private void setTitles()
	{
		txt_titPath.setVisibility(View.VISIBLE);
		txt_title.setText(R.string.file_select);
		txt_titPath.setText(getString(R.string.file_checked) + " " + FILE_CHECK_COUNT + " " + getString(R.string.file_checkno));
	}
	
	//刷新ListView
	public void refreshListView()
	{
		mAdapter.notifyDataSetChanged();
		showSelectView();
	}
	
	//显示属性对话框
	private void showAttrDialog(final FileItem file)
	{
		ATTR_PATH = file.filePath;
		File _file = new File(file.filePath);
		View attrView = inflate.inflate(R.layout.file_details, null);
		attr_name = (TextView) attrView.findViewById(R.id.fileattr_name);
		attr_path = (TextView) attrView.findViewById(R.id.fileattr_path);
		attr_size = (TextView) attrView.findViewById(R.id.fileattr_size);
		attr_time = (TextView) attrView.findViewById(R.id.fileattr_time);
		attr_mime = (TextView) attrView.findViewById(R.id.fileattr_type);
		attr_md5t = (TextView) attrView.findViewById(R.id.fileattr_md5t);
		attr_md5 = (TextView) attrView.findViewById(R.id.fileattr_md5);
		attr_sha1 = (TextView) attrView.findViewById(R.id.fileattr_sha1);
		attr_attr = (TextView) attrView.findViewById(R.id.fileattr_attr);
		attr_user = (TextView) attrView.findViewById(R.id.fileattr_user);
		attr_owner = (TextView) attrView.findViewById(R.id.fileattr_owner);
		attr_link = (TextView) attrView.findViewById(R.id.fileattr_linkto);
		attr_sha1l = (TableRow) attrView.findViewById(R.id.file_title_sha1);
		attr_mimel = (TableRow) attrView.findViewById(R.id.file_title_mime);
		attr_linkl = (TableRow) attrView.findViewById(R.id.file_title_linkl);
		attr_md5.setText(R.string.prop_calcul);
		if (file.fileType.equals(FileType.FILE_TYPE_FOLDER))
		{
			attr_size.setText(R.string.prop_calcul);
			attr_md5t.setText(R.string.prop_contain);
			attr_sha1l.setVisibility(View.GONE);
			attr_mimel.setVisibility(View.GONE);
			if (!file.isCmd)
			{
				folderThread = new FolderSizeThread(_file);
				folderThread.start();
			}
		}
		else
		{
			attr_sha1.setText(R.string.prop_calcul);
			attr_size.setText(mCommon.formatSize(file.size,true));//大小
			md5thread = new FileMD5Thread(_file);
			md5thread.start();
			attr_mime.setText(mCommon.getMimeType(file.fileName));//MIME
		}
		String [] attrs = ProgressClass.getFileDetails(_file);
		if (attrs != null)
		{
			attr_attr.setText(attrs[0].substring(1));//权限
			attr_owner.setText(attrs[1]);//所有者
			attr_user.setText(attrs[2]);//用户组
			if (attrs[0].substring(0, 1).equals("l"))
				attr_link.setText(attrs[attrs.length - 1]);
			else
				attr_linkl.setVisibility(View.GONE);
		}
		else
			attr_linkl.setVisibility(View.GONE);
		attr_name.setText(file.fileName);//名称
		attr_path.setText(mCommon.getParentPath(file.filePath));//位置
		attr_time.setText(mCommon.getFileDate(file.time));//时间戳
		attrDialog = new AlertDialog.Builder(App_filemanager.this);
		attrDialog.setTitle(R.string.attr);
		attrDialog.setView(attrView);
		attrDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.cancel();
			}
		});
		attrDialog.setOnCancelListener(new DialogInterface.OnCancelListener()
		{
			@Override
			public void onCancel(DialogInterface p1)
			{
				try
				{
					ATTR_PATH = "";
					if (md5thread != null)
					{
						md5thread.interrupt();
						md5thread = null;
					}
					if (folderThread != null)
					{
						folderThread.interrupt();
						folderThread = null;
					}
				}
				catch (Exception e)
				{
					ExceptionHandler.log("以下错误在文件属性对话框关闭时关闭线程出现：" + e.toString());
				}
			}
		});
		attrDialog.create();
		attrDialog.show();
	}
	
	//删除文件
	private void deleteFile(final File file)
	{
		final String title = getString(R.string.delete_file);
		mCommon.showDialogTip(dialogTip, title, getString(R.string.delete_warn), new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface p1, int p2)
			{
				dialogWait = showProgressDialog(title, getString(R.string.deleting_file));
				dialogWait.show();
				if (mCommon.deleteFile(file))
				{
					Common.showShortToast(getApplicationContext(), R.string.delete_success);
					refreshList();
				}
				else
					Common.showShortToast(getApplicationContext(), R.string.delete_fail);
				dialogWait.cancel();
			}
		});
	}
	
	//删除文件夹
	private void deleteFolder(final File file)
	{
		final String title = getString(R.string.delete_folder);
		mCommon.showDialogTip(dialogTip, title, getString(R.string.delete_warn), new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface p1, int p2)
			{
				dialogWait = showProgressDialog(title, getString(R.string.deleting_file));
				dialogWait.show();
				new Thread()
				{
					public void run()
					{
						try
						{
							Thread.sleep(1000);
							Message msg = new Message();
							msg.what = 4;
							if (mCommon.deleteFolder(file))
								msg.obj = (Object)getString(R.string.delete_success);
							else
								msg.obj = (Object)getString(R.string.delete_fail);
							handler.sendMessage(msg);
						}
						catch (Exception e)
						{
							
						}
						handler.sendEmptyMessage(5);
					}
				}.start();
			}
		});
	}
	
	//删除多个文件(夹)
	private void deleteFiles(final File[] file)
	{
		final String title = getString(R.string.delete_moreFile);
		mCommon.showDialogTip(dialogTip, title, getString(R.string.delete_warn_more), new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface p1, int p2)
			{
				dialogWait = showProgressDialog(title, getString(R.string.deleting_file));
				dialogWait.show();
				new Thread()
				{
					public void run()
					{
						try
						{
							Thread.sleep(1000);
							Message msg = new Message();
							msg.what = 4;
							int[] result = mCommon.deleteMoreFiles(file);
							msg.obj = (Object)getString(R.string.operat_complete) + "" + result[0] + getString(R.string.success) + ", " + result[1] + getString(R.string.fail);
							handler.sendMessage(msg);
						}
						catch (Exception e)
						{

						}
						handler.sendEmptyMessage(5);
					}
				}.start();
			}
		});
	}
	
	private void renameFile(final File file)
	{
		showEditDialog(getString(R.string.rename), file.getName(), new AlertDialog.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				String value = txt_edit.getText().toString();
				if (mCommon.rename(file, value))
				{
					Common.showShortToast(getApplicationContext(),R.string.operate_success);
					refreshList();
				}
				else
					Common.showShortToast(getApplicationContext(),R.string.operate_fail);
			}
		});
	}
	
	public void showEditDialog(String title, String defaults, DialogInterface.OnClickListener okClick)
	{
		View dialogview = inflate.inflate(R.layout.editview, null);
		//设置EditText
		txt_edit = (EditText) dialogview.findViewById(R.id.txt_editView);
		txt_edit.setText(defaults);

		dialogOpr = new AlertDialog.Builder(App_filemanager.this);
		dialogOpr.setTitle(title);
		dialogOpr.setView(dialogview);
		dialogOpr.setPositiveButton(R.string.ok, okClick);
		dialogOpr.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.cancel();
			}
		});
		dialogOpr.show();
	}
	
	public Dialog showProgressDialog(String title, String message)
	{
		return progressDialog.show(this, title, message, false, false);
	}
	
	//添加书签
	public void addBookmark(String path, String type)
	{
		SharedPreferences.Editor edter = book_spf.edit();
		int bookcount = book_spf.getInt("count", 0);
		edter.putString("bk_"+bookcount, path);
		edter.putString("tp_"+bookcount, type);
		edter.putInt("count", bookcount + 1);
		edter.commit();
	}
	
	public void showSelectView()
	{
		FILE_CHECK_COUNT = mCommon.getCheckedFiles(mFileList);
		if (SELECTED=(FILE_CHECK_COUNT > 0))
		{
			menuBar.setVisibility(View.INVISIBLE);
			selectBar.setVisibility(View.VISIBLE);
			setTitles();
		}
		else
		{
			menuBar.setVisibility(View.VISIBLE);
			selectBar.setVisibility(View.GONE);
			setTitles(CurrentDirectory);
		}
		if (FILE_CHECK_COUNT == 1)
		{
			selc_more.setVisibility(View.VISIBLE);
			selc_share.setVisibility(View.GONE);
		}
		else
		{
			selc_more.setVisibility(View.GONE);
			selc_share.setVisibility(View.VISIBLE);
		}
	}
	
	private void showCopyView()
	{
		if (tmpFilesList.size() > 0)
			copyBar.setVisibility(View.VISIBLE);
		else
			copyBar.setVisibility(View.GONE);
	}
	
	private void getPasteFiles(File[] files, int action)
	{
		tmpFilesList.clear();
		for (File file:files)
			getPasteFile(file, action);
	}
	
	private void getPasteFile(File file, int action)
	{
		tmpFilesList.add(new FileTemp(file, action));
	}
	
	private void getTmpfileExistAction(int exsistCount)
	{
		if (exsistCount == 0)
		{
			pasteFiles();
			//Common.showShortToast(getApplicationContext(), "这时可以开始粘贴了");
			return;
		}
		ArrayList<Integer> indexs = mCommon.getTmpFileExistIndex(tmpFilesList);
		if (indexs.size() != exsistCount)
		{
			handler.sendEmptyMessage(7);
			return;
		}
		if (exsistCount > 1)
		{
			showExsistDialog(indexs, 0);
		}
		else if (exsistCount == 1)
		{
			final FileTemp fi = tmpFilesList.get(indexs.get(0));
			if (fi.isSure)
			{
				pasteFiles();
				return;
			}
			showExistFileOpreateDialog(fi.tmpFile.getName(), (fi.tmpFile.isFile() ? R.string.cover : R.string.merge),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface d, int s) {//覆盖
						fi.action = FileTemp.ACTION_COVER;
						pasteFiles();
						//Common.showShortToast(getApplicationContext(), "这时可以开始粘贴了");
					}
				}, 
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface d, int s) {//取消
						tmpFilesList.clear();
						showCopyView();
					}
				});
		}
	}
	
	private void showExsistDialog(final ArrayList<Integer> indexs, final int index)
	{
		if (index >= indexs.size())
		{
			//Common.showShortToast(getApplicationContext(), "这时可以开始粘贴了");
			pasteFiles();
			return;
		}
		final FileTemp tmp = tmpFilesList.get(indexs.get(index));
		if (tmp.isSure)
		{
			showExsistDialog(indexs, index + 1);
			return;
		}
		showExistFilesOpreateDialog(tmp.tmpFile.getName(), (tmp.tmpFile.isFile() ? R.string.cover : R.string.merge),
			new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface d, int s) {//覆盖
					if (chk_exsit.isChecked()) {
						mCommon.setExistFileAction(tmpFilesList, FileTemp.ACTION_COVER);
						//Common.showShortToast(getApplicationContext(), "这时可以开始粘贴了");
						pasteFiles();
					} else {
						tmp.action = FileTemp.ACTION_COVER;
						tmp.isSure = true;
						showExsistDialog(indexs, index + 1);
					}
				}
			}, 
			new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface d, int s) {//跳过
					if (chk_exsit.isChecked()) {
						mCommon.setExistFileAction(tmpFilesList, FileTemp.ACTION_SKIP);
						//Common.showShortToast(getApplicationContext(), "这时可以开始粘贴了");
						pasteFiles();
					}
					else {
						tmp.action = FileTemp.ACTION_SKIP;
						tmp.isSure = true;
						showExsistDialog(indexs, index + 1);
					}
				}
			},
			new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface d, int s) {//取消
					tmpFilesList.clear();
					showCopyView();
				}
			});
	}
	
	private void showExistFileOpreateDialog(String fileName, int posId, DialogInterface.OnClickListener cover, DialogInterface.OnClickListener cancel)
	{
		dialogTip = new AlertDialog.Builder(App_filemanager.this);
		dialogTip.setTitle(R.string.item_exist);
		dialogTip.setMessage(fileName + getString(R.string.item_exist_msg));
		dialogTip.setPositiveButton(posId, cover);
		dialogTip.setNegativeButton(R.string.cancel, cancel);
		dialogTip.create();
		dialogTip.show();
	}
	
	private void showExistFilesOpreateDialog(String fileName, int posId, DialogInterface.OnClickListener cover, DialogInterface.OnClickListener skip, DialogInterface.OnClickListener cancel)
	{
		View view = inflate.inflate(R.layout.file_copyex, null);
		txt_exFilemsg = (TextView) view.findViewById(R.id.txt_exFileMessage);
		chk_exsit = (CheckBox) view.findViewById(R.id.chk_exFileCheck);
		txt_exFilemsg.setText(fileName + " " + getString(R.string.item_exist_msg));
		dialogTip = new AlertDialog.Builder(App_filemanager.this);
		dialogTip.setTitle(R.string.item_exist);
		dialogTip.setView(view);
		dialogTip.setPositiveButton(posId, cover);
		dialogTip.setNeutralButton(R.string.skip, skip);
		dialogTip.setNegativeButton(R.string.cancel, cancel);
		dialogTip.create();
		dialogTip.show();
	}
	
	//粘贴文件
	public void pasteFiles()
	{
		String title = getString(R.string.moving);
		if (tmpFilesList.get(0).oldAction == FileTemp.ACTION_COPY)
			title = getString(R.string.copying);
		View view = inflate.inflate(R.layout.file_copy, null);
		copy_fileName = (TextView) view.findViewById(R.id.file_copyName);
		copy_Percent = (TextView) view.findViewById(R.id.file_copyPercent);
		fileProgress = (ProgressBar) view.findViewById(R.id.file_copyProgress);
		dialogOpr = new AlertDialog.Builder(App_filemanager.this);
		dialogOpr.setTitle(title);
		dialogOpr.setView(view);
		dialogOpr.setCancelable(false);
		dialogWait = dialogOpr.create();
		dialogWait.show();
		new Thread()
		{
			public void run()
			{
				try
				{
					Thread.sleep(1000);
					Message msg = new Message();
					fileOperate = new FileOperate(tmpFilesList);
					int[] result = fileOperate.start(new FileOperate.OnFileOperation()
					{
						@Override
						public void onFileOperation(String fileName, int total, int progress)
						{
							Message msg2 = new Message();
							msg2.what = 6;
							msg2.obj = (Object) fileName;
							int percent = Float.valueOf(((float)progress / (float)total) * 100).intValue();
							msg2.arg1 = percent;
							handler.sendMessage(msg2);
						}
						
						@Override
						public void onFileOperations(String fileName, long total, long progress)
						{
							Message msg2 = new Message();
							msg2.what = 6;
							msg2.obj = (Object) fileName;
							int percent = Float.valueOf((float)((float)progress / total) * 100).intValue();
							msg2.arg1 = percent;
							handler.sendMessage(msg2);
						}
					});
					handler.sendEmptyMessage(7);
					msg.what = 4;
					msg.obj = (Object)getString(R.string.operat_complete) + "" + result[0] + getString(R.string.success) + ", " + result[1] + getString(R.string.fail) + ", " + result[2] + getString(R.string.skip);
					handler.sendMessage(msg);
				}
				catch (Exception e)
				{

				}
				handler.sendEmptyMessage(5);
			}
		}.start();
	}
	
	public class FileMD5Thread extends Thread
	{
		private File _file;
		public FileMD5Thread(File file)
		{
			_file = file;
		}
		@Override
		public void run()
		{
			try
			{
				Message msg,msg1;
				msg = new Message();
				msg1 = new Message();
				msg.what = 1;
				msg1.what = 2;
				msg.obj = (Object)MD5Util.getFileMD5(getString(R.string.access_denied), _file);
				msg1.obj = (Object)MD5Util.getFileSHA1(getString(R.string.access_denied), _file);
				sleep(10);
				if (ATTR_PATH.equals(_file.getAbsolutePath()))
				{
					handler.sendMessage(msg);
					handler.sendMessage(msg1);
				}
			}
			catch (Exception e)
			{
				ExceptionHandler.log("FileMD5ThreadException():" + e.toString());
			}
		}
	}
	
	public class FolderSizeThread extends Thread
	{
		File _file;
		public FolderSizeThread(File file)
		{
			_file = file;
		}
		@Override
		public void run()
		{
			try
			{
				sleep(10);
				Message msg = new Message();
				msg.what = 3;
				long [] sizes = mCommon.fordersTotalSize(_file);
				msg.obj = (Object)mCommon.formatSize(sizes[0],true);
				msg.arg1 = (int)sizes[1];
				msg.arg2 = (int)sizes[2];
				if (ATTR_PATH.equals(_file.getAbsolutePath()))
					handler.sendMessage(msg);
			}
			catch (Exception e)
			{
				ExceptionHandler.log("FolderSizeThreadException():" + e.toString());
			}
		}
	}
	
	Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case 1:
					attr_md5.setText(msg.obj.toString());
					break;
				case 2:
					attr_sha1.setText(msg.obj.toString());
					break;
				case 3:
					attr_size.setText(msg.obj.toString());
					attr_md5.setText(msg.arg1 + getString(R.string.prop_files) + ", " + msg.arg2 + getString(R.string.prop_folders));
					break;
				case 4://显示Toast信息
					Common.showShortToast(getApplicationContext(), msg.obj.toString());
					break;
				case 5://关闭对话框
					dialogWait.cancel();
					refreshList();
					break;
				case 6://显示百分比
					copy_fileName.setText(msg.obj.toString());
					copy_Percent.setText(msg.arg1 + "%");
					fileProgress.setProgress(msg.arg1);
					break;
				case 7:
					tmpFilesList.clear();
					showCopyView();
					break;
			}
		}
	};
	
	private class FolderListener implements DialogInterface.OnClickListener
	{
		private FileItem tmpFile;
		private File file;
		
		public FolderListener(FileItem _tmpFile)
		{
			tmpFile = _tmpFile;
			file = new File(_tmpFile.filePath);
		}
		
		public void onClick(DialogInterface dialog, int which)
		{
			switch(which)
			{
				case 0://打开
				{
					if (file.isFile())
						mCommon.openFile(file, tmpFile.fileType);
					else
						browseTo(file);
					break;
				}
				case 1://重命名
					renameFile(file);
					break;
				case 2://删除
					deleteFolder(file);
					break;
				case 3://复制
					tmpFilesList.clear();
					getPasteFile(file, FileTemp.ACTION_COPY);
					showCopyView();
					break;
				case 4://剪切
					tmpFilesList.clear();
					getPasteFile(file, FileTemp.ACTION_CUT);
					showCopyView();
					break;
				case 5://属性
					showAttrDialog(tmpFile);
					break;
				case 6://添加书签
					addBookmark(tmpFile.filePath, tmpFile.fileType);
					break;
			}
		}
	}
	private class FileListener implements DialogInterface.OnClickListener
	{
		private FileItem tmpFile;
		private File file;

		public FileListener(FileItem _tmpFile)
		{
			tmpFile = _tmpFile;
			file = new File(_tmpFile.filePath);
		}

		public void onClick(DialogInterface dialog, int which)
		{
			switch(which)
			{
				case 0://打开
				{
					if (file.isFile())
						mCommon.openFile(file, tmpFile.fileType);
					else
						browseTo(file);
					break;
				}
				case 1://重命名
					renameFile(file);
					break;
				case 2://删除
					deleteFile(file);
					break;
				case 3://复制
					tmpFilesList.clear();
					getPasteFile(file, FileTemp.ACTION_COPY);
					showCopyView();
					break;
				case 4://剪切
					tmpFilesList.clear();
					getPasteFile(file, FileTemp.ACTION_CUT);
					showCopyView();
					break;
				case 5://发送
					mCommon.sendFile(file, tmpFile.fileType);
					break;
				case 6://属性
					showAttrDialog(tmpFile);
					break;
				case 7://添加书签
					addBookmark(tmpFile.filePath, tmpFile.fileType);
					break;
			}
		}
	}
	
	private class FileCmdListener implements DialogInterface.OnClickListener
	{
		private FileItem tmpFile;
		private File file;

		public FileCmdListener(FileItem _tmpFile)
		{
			tmpFile = _tmpFile;
			file = new File(_tmpFile.filePath);
		}

		public void onClick(DialogInterface dialog, int which)
		{
			switch(which)
			{
				case 0://打开
				{
					if (tmpFile.fileType.equals(FileType.FILE_TYPE_FOLDER))
						browseTo(file);
					else
						mCommon.openFile(file, tmpFile.fileType);
					break;
				}
				case 1://属性
					showAttrDialog(tmpFile);
					break;
				case 2://添加书签
					addBookmark(tmpFile.filePath, tmpFile.fileType);
					break;
			}
		}
	}
	
	private class OnCheckdChange implements FileListAdapter.OnCheckedChange
	{
		@Override
		public void onCheckeFinish(int position, boolean check)
		{
			mFileList.get(position).checked = check;
			refreshListView();
		}
    }
	
	public class FastScrollListener implements AbsListView.OnScrollListener, Runnable
	{
		AbsListView abs;
		int lastState;
		Handler delayh = new Handler();
		@Override
		public void onScrollStateChanged(AbsListView p1, int p2)
		{
			abs = p1;
			lastState = p2;
			if (p2 == SCROLL_STATE_IDLE)
			{
				Shipment.FILE_IS_SCROLL = false;
				postDisable();
				if (Shipment.SHOW_THUMBNAIL)
					mAdapter.notifyDataSetChanged();
			}
			else
			{
				abs.setFastScrollEnabled(true);
				Shipment.FILE_IS_SCROLL = true;
			}
		}

		@Override
		public void onScroll(AbsListView p1, int p2, int p3, int p4)
		{
			abs = p1;
			delayh.removeCallbacks(this);
			postDisable();
		}

		@Override
		public void run()
		{
			abs.setFastScrollEnabled(lastState != SCROLL_STATE_IDLE);
		}

		private void postDisable()
		{
			delayh.postDelayed(this, 500);
		}
	}
	
	public void setMyTheme()
	{
		if (IS_INIT)
		{
			MyThemes.setThemes(this,titleLayout);
			MyThemes.getThemes(footLayout);
			MyThemes.getThemes(menuBar);
			MyThemes.getThemes(selectBar);
			MyThemes.getThemes(copyBar);
			IS_INIT = false;
		}
		if (BOOKMARK_STATUS)
		{
			BOOKMARK_STATUS = false;
			File bookFile = new File(BOOKMARK_PATH);
			if (bookFile.exists())
				browseTo(bookFile, mCommon.getFileType(bookFile), false);
		}
	}

	@Override
	protected void onPause()
	{
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		setMyTheme();
		super.onResume();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}
	
	//返回键功能定制
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		if (keyCode == KeyEvent.KEYCODE_BACK) 
		{
			if (CurrentDirectory.getParent() != null)
			{
				upOneLevel(IS_CMD);
				return false;
			}
			else
			{
				saveFilePath();
				finish();
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		}
		return super.onKeyDown(keyCode,event);
	}
}
