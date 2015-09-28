package com.binxin.zdapp.update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.content.pm.PackageManager;
import android.content.pm.PackageInfo;
import com.binxin.zdapp.R;
import android.content.res.*;

public class UpdateManager
{
	/* 下载中 */
	private static final int DOWNLOAD = 1;
	/* 下载结束 */
	private static final int DOWNLOAD_FINISH = 2;
	/* 保存解析的XML信息 */
	HashMap<String, String> mHashMap = null;
	/* 下载保存路径 */
	private String mSavePath;
	/* 记录进度条数量 */
	private int progress;
	/* 是否取消更新 */
	private boolean cancelUpdate = false;

	private Context mContext;
	/* 更新进度条 */
	private ProgressBar mProgress;
	private Dialog mDownloadDialog;

	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			// 正在下载
			case DOWNLOAD:
				// 设置进度条位置
				mProgress.setProgress(progress);
				break;
			case DOWNLOAD_FINISH:
				// 安装文件
				installApk();
				break;
			default:
				break;
			}
		};
	};

	public UpdateManager(Context context)
	{
		this.mContext = context;
	}

	//检测软件更新
	/*public void checkUpdate()
	{
		if (isUpdate())
			// 显示提示对话框
			showNoticeDialog();
		else
			Toast.makeText(mContext, "已是最新版本",Toast.LENGTH_LONG).show();
	}*/

	//检查软件是否有更新版本
	public void checkUpdate(Context context)
	{
		Toast.makeText(context,"isUpdate开始",Toast.LENGTH_SHORT).show();
		// 把version.xml放到网络上，然后获取文件信息
		//InputStream inStream = ParseXmlService.class.getClassLoader().getResourceAsStream("version.xml");
		// 解析XML文件。 由于XML文件比较小，因此使用DOM方式进行解析
		//HttpURLConnection con = null;
		ParseXmlService service = new ParseXmlService();
		try
		{
			URL url = new URL("http://182.254.211.235:80/ZDApp/update.xml");
			URLConnection Huconn = (HttpURLConnection)url.openConnection();
			//con = (HttpURLConnection) Huconn;
			Huconn.setConnectTimeout(6*1000);
			//Huconn.setReadTimeout(3000);
			//con.setReadTimeout(3000);
			//con.setRequestMethod("GET");
			InputStream inStream = Huconn.getInputStream();
			mHashMap = service.parseXml(inStream);
			Toast.makeText(context,"读取数据",Toast.LENGTH_SHORT).show();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Toast.makeText(context,"检查更新失败",Toast.LENGTH_SHORT).show();
			return;
		}
		if (mHashMap != null)
		{
			int versionCode = context.getResources().getInteger(R.integer.versionCode);
			int serviceCode = 6800;//Integer.valueOf(mHashMap.get("version"));
			// 版本判断
			if (serviceCode > versionCode)
			{
				showNoticeDialog();
			}
			else
				Toast.makeText(context, "已是最新版本"+serviceCode+"as"+versionCode,Toast.LENGTH_LONG).show();
		}
		else
			Toast.makeText(context, "未检测到版本信息",Toast.LENGTH_LONG).show();
	}

	//获取软件版本号
	/*private int getVersionCode(Context context)
	{
		int versionCode = 0;
		try
		{
			PackageManager manage = context.getPackageManager();
			PackageInfo packageIn = manage.getPackageInfo("com.binxin.zdapp",0);
			// 获取软件版本号，对应AndroidManifest.xml下android:versionCode
			//versionCode = context.getPackageManager().getPackageInfo("", 0).versionCode;
			versionCode = packageIn.versionCode;
		} catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return versionCode;
	}*/

	//显示软件更新对话框
	private void showNoticeDialog()
	{
		// 构造对话框
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("新软件"+mHashMap.get("vername"));
		builder.setMessage("发现新版本\n新版特性："+mHashMap.get("newmsg"));
		// 更新
		builder.setPositiveButton("现在更新", new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				// 显示下载对话框
				showDownloadDialog();
			}
		});
		// 稍后更新
		builder.setNegativeButton("下次再说", new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
		Dialog noticeDialog = builder.create();
		noticeDialog.show();
	}

	//显示软件下载对话框
	private void showDownloadDialog()
	{
		// 构造软件下载对话框
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("正在更新");
		// 给下载对话框增加进度条
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.download, null);
		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
		builder.setView(v);
		// 取消更新
		builder.setNegativeButton(R.string.cancel, new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				// 设置取消状态
				cancelUpdate = true;
			}
		});
		mDownloadDialog = builder.create();
		mDownloadDialog.show();
		// 现在文件
		downloadApk();
	}

	//下载apk文件
	private void downloadApk()
	{
		// 启动新线程下载软件
		new downloadApkThread().start();
	}
	//下载文件线程
	private class downloadApkThread extends Thread
	{
		@Override
		public void run()
		{
			try
			{
				// 判断SD卡是否存在，并且是否具有读写权限
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
				{
					// 获得存储卡的路径
					mSavePath = Environment.getExternalStorageDirectory().toString() + "/ZDApp/Download";
					URL url = new URL(mHashMap.get("url"));
					// 创建连接
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.connect();
					// 获取文件大小
					int length = conn.getContentLength();
					// 创建输入流
					InputStream is = conn.getInputStream();

					File file = new File(mSavePath);
					// 判断文件目录是否存在
					if (!file.exists())
					{
						file.mkdir();
					}
					File apkFile = new File(mSavePath, "Update.apk");
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					// 缓存
					byte buf[] = new byte[1024];
					// 写入到文件中
					do
					{
						int numread = is.read(buf);
						count += numread;
						// 计算进度条位置
						progress = (int) (((float) count / length) * 100);
						// 更新进度
						mHandler.sendEmptyMessage(DOWNLOAD);
						if (numread <= 0)
						{
							// 下载完成
							mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
							break;
						}
						// 写入文件
						fos.write(buf, 0, numread);
					} while (!cancelUpdate);// 点击取消就停止下载.
					fos.close();
					is.close();
				}
			} catch (MalformedURLException e)
			{
				e.printStackTrace();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			// 取消下载对话框显示
			mDownloadDialog.dismiss();
		}
	};

	//安装APK文件
	private void installApk()
	{
		File apkfile = new File(mSavePath, mHashMap.get("name"));
		if (!apkfile.exists())
		{
			return;
		}
		// 通过Intent安装APK文件
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		mContext.startActivity(i);
	}
}
