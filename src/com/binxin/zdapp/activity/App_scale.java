package com.binxin.zdapp.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.view.View.OnClickListener;
import android.text.InputFilter;
import android.text.Editable;
import android.text.TextWatcher;

import com.binxin.zdapp.classes.ScaleThransform;
import com.binxin.zdapp.classes.MyThemes;
import com.binxin.zdapp.R;
import android.widget.GridLayout.*;

public class App_scale extends Activity
{
	private ArrayAdapter adapter, adapter2;
	private String sca_As;
	private String sca_Bs;
	private EditText etText, etAfter;
	private TextView warningText;
	private LinearLayout titleLayout;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scale);
		
		Button btChange=(Button) findViewById(R.id.btn_scale);
		ImageButton back = (ImageButton) findViewById(R.id.btn_close);
		Spinner aspn = (Spinner) findViewById(R.id.spn_aScale);
		Spinner bspn = (Spinner) findViewById(R.id.spn_bScale);
		etText = (EditText) findViewById(R.id.edt_sctext);
		etAfter = (EditText) findViewById(R.id.edt_scaAfter);
		warningText = (TextView) findViewById(R.id.scaleTextWarning);
		titleLayout = (LinearLayout) findViewById(R.id.themeLayout);
		//添加监听
		/*etText.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4)
			{
				// TODO: Implement this method
			}
			@Override
			public void onTextChanged(CharSequence p1, int p2, int p3, int p4)
			{
				// TODO: Implement this method
			}
			@Override
			public void afterTextChanged(Editable s)
			{
				String tmp = "";
				String digits = setDigits(sca_As);
				String str = s.toString();
				if (str.equals(tmp))
				{
					return;// 如果tmp==str则返回，因为这是我们设置的结果。否则会形成死循环。
				}
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < str.length(); i++)
				{
					if (digits.indexOf(str.charAt(i)) >= 0)
					{
						// 判断字符是否在可以输入的字符串中
						sb.append(str.charAt(i));// 如果是，就添加到结果里，否则跳过
					}
				}
				tmp = sb.toString();// 设置tmp，因为下面一句还会导致该事件被触发
				etText.setText(tmp);// 设置结果
				etText.invalidate();
			}
		});*/
		etAfter.setKeyListener(null);
		//A进制
		adapter = ArrayAdapter.createFromResource(this, R.array.hexadecimal, android.R.layout.simple_spinner_item);//设置下拉列表的风格
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//将adapter2 添加到spinner中
		aspn.setAdapter(adapter);

		//添加事件Spinner事件监听
		aspn.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				sca_As = fomatStr(adapter.getItem(arg2).toString());
				edtFormat(sca_As);
				warningText.setText(setDigits(sca_As));
			}
			public void onNothingSelected(AdapterView<?> arg0)
			{
				//Toast.makeText(getApplicationContext(), "取消选中", Toast.LENGTH_SHORT).show();
			}
		});
		//设置默认值
		//aspn.setVisibility(View.VISIBLE);
		
		//B进制
		adapter2 = ArrayAdapter.createFromResource(this, R.array.hexadecimal2, android.R.layout.simple_spinner_item);//设置下拉列表的风格
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//将adapter2 添加到spinner中
		bspn.setAdapter(adapter2);

		//添加事件Spinner事件监听
		bspn.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				sca_Bs = fomatStr(adapter2.getItem(arg2).toString());
			}
			public void onNothingSelected(AdapterView<?> arg0)
			{

			}
		});
		//设置默认值
		//bspn.setVisibility(View.VISIBLE);
		btChange.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				try
				{
					if(etText.getText().toString().length()<1)
					{
						Toast.makeText(App_scale.this,"请输入相关数据  ",Toast.LENGTH_SHORT).show();		
					}
					else
					{
						etAfter.setText(ScaleThransform.changeTO(strToInt(sca_As),strToInt(sca_Bs),etText.getText().toString()));
					}
				}
				catch (NumberFormatException e)
				{
					Toast.makeText(getApplicationContext(), "输入字符格式不正确",Toast.LENGTH_SHORT).show();
				}
				catch (Exception e)
				{
					Toast.makeText(getApplicationContext(), "转换失败！",Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		back.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				finish();
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
    }
	private int strToInt(String str)
	{
		return Integer.parseInt(str);
	}
	private String fomatStr(String str)
	{
		return str.substring(0, str.length()-2);
	}
	private void edtFormat(String stredx)
	{
		switch (stredx)
		{
			case "2":
				setFilter(31);
				break;
			case "4":
				setFilter(15);
				break;
			case "6":
				setFilter(12);
				break;
			case "8":
				setFilter(10);
				break;
			case "10":
				setFilter(9);
				break;
			case "16":
				setFilter(8);
				break;
		}
	}
	private void setFilter(int Length)
	{
		etText.setFilters(new InputFilter[]
		{
			new InputFilter.LengthFilter(Length)
		});
	}
	public String setDigits(String digits)
	{
		String strDigits = "";
		switch (digits)
		{
			case "2":
				strDigits = "01";
				break;
			case "4":
				strDigits = "0123";
				break;
			case "6":
				strDigits = "012345";
				break;
			case "8":
				strDigits = "01234567";
				break;
			case "10":
				strDigits = "0123456789";
				break;
			case "16":
				strDigits = "0123456789abcdefABCDEF";
				break;
		}
		return "提示：当前只能输入“"+strDigits+"”等字符。";
	}
	public void setMyTheme()
	{
		android.content.SharedPreferences sube = getSharedPreferences("zdapp", android.content.Context.MODE_PRIVATE);
		int code = sube.getInt("themeCode", 0);
		MyThemes.setThemes(this,titleLayout, code);
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		setMyTheme();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
	}

	@Override
	protected void onDestroy()
	{
		//释放占用的内存资源
		super.onDestroy();
		//System.exit(0);
	}
}
