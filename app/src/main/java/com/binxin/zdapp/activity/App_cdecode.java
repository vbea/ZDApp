package com.binxin.zdapp.activity;

import java.util.Hashtable;
import java.util.Date;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.BufferedOutputStream;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.os.Environment;
import android.net.Uri;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.content.Intent;
import android.content.Context;
import android.content.ClipboardManager;
import android.content.ClipData;
import android.content.DialogInterface;
import android.text.InputType;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import com.binxin.zdapp.R;
import com.binxin.zdapp.classes.MyThemes;
import com.binxin.zdapp.classes.DecHelper;
import com.binxin.zdapp.classes.ColorPickerDialog;

//生成二维码
public class App_cdecode extends Activity
{
	private ImageView sweepIV;
	private EditText sweepEdt;
	private Button okBtn,btnColor,btnSave,btnCopy;
	private int QR_WIDTH = 300, QR_HEIGHT = 300;
	private Bitmap bitmap = null;
	private int defCol = 0xFF000000;
	private ColorPickerDialog dialog;
	private LinearLayout titleLayout,pwdLayout;
	private CheckBox chkLock,chkPwd;
	private EditText txtPsd;
	private TextView txtPwd, txtTest;
	private boolean isCreate = false;
	private String qrStr = "";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cdecode);

		ImageButton close = (ImageButton) findViewById(R.id.btn_close);
		sweepIV = (ImageView) findViewById(R.id.test_iv);
		sweepEdt = (EditText) findViewById(R.id.CodeEdt);
		okBtn = (Button) findViewById(R.id.btn_ResultCode);
		btnSave = (Button) findViewById(R.id.btn_SaveCode);
		btnColor = (Button) findViewById(R.id.btn_codeColor);
		btnCopy = (Button) findViewById(R.id.btn_copyCode);
		chkLock = (CheckBox) findViewById(R.id.chkLocked);
		chkPwd = (CheckBox) findViewById(R.id.chkPassword);
		txtPsd = (EditText) findViewById(R.id.cde_txtPsd);
		txtPwd = (TextView) findViewById(R.id.cde_textPsd);
		txtTest = (TextView) findViewById(R.id.cdecode_testText);
		pwdLayout = (LinearLayout) findViewById(R.id.cde_pwdlayout);
		titleLayout = (LinearLayout) findViewById(R.id.themeLayout);

		close.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				finish();
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
			
		okBtn.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				if (sweepEdt.getText().toString().trim().length() != 0)
				{
					if (chkLock.isChecked())
					{
						//加密
						try
						{
							DecHelper dec = new DecHelper(txtPsd.getText().toString());
							qrStr = "#正德应用扫一扫#"+dec.encrypt(sweepEdt.getText().toString());
						}
						catch (Exception e)
						{
							Toast.makeText(getApplicationContext(),"加密失败，请重试",Toast.LENGTH_SHORT).show();
						}
					}
					else
						qrStr = sweepEdt.getText().toString();
					txtTest.setText(qrStr);
					createQRImage(qrStr);
				}
				else
				{
					Toast.makeText(getApplicationContext(),"请输入文本信息",Toast.LENGTH_SHORT).show();
					isCreate = false;
				}
			}
		});
		
		btnSave.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				if (!isCreate)
				{
					Toast.makeText(getApplicationContext(),"请先生成后再操作",Toast.LENGTH_SHORT).show();
					return;
				}
				String path = getSDPash();
				if (path != null)
				{
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss",Locale.SIMPLIFIED_CHINESE);
					path += "/ZDApp/Images/"+sdf.format(new Date())+".png";
				}
				else
				{
					return;
				}
				if (bitmap != null)
				{
					try
					{
						saveBitmapToFile(bitmap, path);
						//Toast.makeText(getApplicationContext(),"保存成功！",Toast.LENGTH_SHORT).show();
					}
					catch(IOException e)
					{
						Toast.makeText(getApplicationContext(),"保存失败！",Toast.LENGTH_SHORT);
					}
					catch(Exception e)
					{
						Toast.makeText(getApplicationContext(),"操作失败！",Toast.LENGTH_SHORT);
					}
				}
				else
					Toast.makeText(getApplicationContext(),"未生成二维码，保存失败！",Toast.LENGTH_SHORT).show();
			}
		});
  
    	btnColor.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				setTheme(android.R.style.Theme_DeviceDefault_Light);
				dialog = new ColorPickerDialog(App_cdecode.this, defCol,"选择颜色",new ColorPickerDialog.OnColorChangedListener()
				{
					@Override
					public void colorChanged(int color)
					{
						defCol = color;
						if (isCreate)
							createQRImage(qrStr);
					}
				});
				//dialog.setTitle("选择颜色");
				dialog.create();
				dialog.show();
			}
		});
		
		chkLock.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				if (chkLock.isChecked())
				{
					pwdLayout.setVisibility(View.VISIBLE);
					//txtPsd.setText("");
				}
				else
					pwdLayout.setVisibility(View.GONE);
			}
		});
		chkPwd.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				int index = txtPsd.getSelectionEnd();
				if (chkPwd.isChecked())
				{
					//设置密码框
					txtPsd.setInputType(InputType.TYPE_CLASS_TEXT);
					txtPsd.setInputType(0x90);
					txtPsd.setSelection(index);
				}
				else
				{
					txtPsd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
					txtPsd.setInputType(0x81);
					txtPsd.setSelection(index);
				}
			}
		});
		txtPwd.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				if (!chkLock.isChecked())
				{
					chkLock.setChecked(true);
					pwdLayout.setVisibility(View.VISIBLE);
					//txtPsd.setText("");
				}
				else
				{
					pwdLayout.setVisibility(View.GONE);
					chkLock.setChecked(false);
				}
			}
		});
	}

	//要转换的地址或字符串,可以是中文
	public void createQRImage(String url)
	{
		//判断URL合法性
		if (url == null || "".equals(url) || url.length() < 1)
		{
			return;
		}
		try
		{
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			//图像数据转换，使用了矩阵转换
			BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
			int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
			//下面这里按照二维码的算法，逐个生成二维码的图片，
			//两个for循环是图片横列扫描的结果
			for (int y = 0; y < QR_HEIGHT; y++)
			{
				for (int x = 0; x < QR_WIDTH; x++)
				{
					if (bitMatrix.get(x, y))
					{
						pixels[y * QR_WIDTH + x] = defCol;
					}
					else
					{
						pixels[y * QR_WIDTH + x] = 0xffffffff;
					}
				}
			}
			WindowManager wm = this.getWindowManager(); 
			int width = wm.getDefaultDisplay().getWidth();
			//int height = wm.getDefaultDisplay().getHeight();
			sweepIV.setLayoutParams(new LinearLayout.LayoutParams(width,width));
			//生成二维码图片的格式，使用ARGB_8888
			bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
			//显示到一个ImageView上面
			sweepIV.setImageBitmap(bitmap);
			isCreate = true;
		}
		catch (WriterException e)
		{
			e.printStackTrace();
		}
	}
	//保存图片
	public void saveBitmapToFile(Bitmap bitmap, String file) throws IOException
	{
        BufferedOutputStream os = null;
        try
		{
            File _file = new File(file);
            // String _filePath_file.replace(File.separatorChar +
            // file.getName(), "");
            int end = file.lastIndexOf(File.separator);
            String _filePath = file.substring(0, end);
            File filePath = new File(_filePath);
            if (!filePath.exists())
			{
                filePath.mkdirs();
            }
            _file.createNewFile();
            os = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
			Toast.makeText(getApplicationContext(),"图片保存在：" + file,Toast.LENGTH_SHORT).show();
			MediaStore.Images.Media.insertImage(getContentResolver(),bitmap,"","");
			Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			Uri uri = Uri.fromFile(_file);
			intent.setData(uri);
			this.sendBroadcast(intent);
        }
		finally
		{
            if (os != null)
			{
                try
				{
					bitmap.recycle();
                    os.close();
                }
				catch (IOException e)
				{
                    throw e;
                }
            }
        }
    }
	
	public String getSDPash()
	{
		String path = null;
		if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
		{
			path = Environment.getExternalStorageDirectory().toString();
			//Toast.makeText(getApplicationContext(),path,Toast.LENGTH_SHORT).show();
		}
		else
		{
			Toast.makeText(getApplicationContext(),"SD卡未挂载",Toast.LENGTH_SHORT).show();
			return null;
		}
		return path;
	}

	@Override
	protected void onPause()
	{
		// TODO: Implement this method
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		MyThemes.setThemes(this, titleLayout);
		super.onResume();
		if (chkLock.isChecked())
		{
			pwdLayout.setVisibility(View.VISIBLE);
			//txtPsd.setText("");
		}
		else
			pwdLayout.setVisibility(View.GONE);
	}

	@Override
	protected void onDestroy()
	{
		// TODO: Implement this method
		super.onDestroy();
	}
}

