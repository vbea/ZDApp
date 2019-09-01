package com.binxin.zdapp.activity; 

import java.text.DecimalFormat;
import java.util.Date;

import android.app.Activity; 
import android.app.AlertDialog;
import android.os.Bundle; 
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.content.SharedPreferences;
import android.view.View.OnClickListener; 
import android.view.View; 
import android.view.KeyEvent;
import android.content.Context;
import android.content.Intent;
import android.content.DialogInterface;
import android.widget.TextView.*;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.text.TextWatcher;

import com.binxin.zdapp.R;
import com.binxin.zdapp.classes.MyThemes;
import android.text.*;

public class App_calculator extends Activity
{
	//声明控件
	private Button[] btn = new Button[10];
	private EditText et_led;
	private TextView calculator;
	private Button btn_div, btn_mul, btn_sub, btn_plus, btn_equal, btn_dot, btn_bksp, btn_ce, btn_c, btn_zf, btn_x, btn_rm, btn_mc, btn_mx, btn_ms, btn_x2, btn_pre, btn_gh, btn_pai, btn_jc, btn_c2; 
	private LinearLayout titleLayout;
	//定义变量
	public double predata = 0;
	private String mmdouble;
	public String preopt = "="; 
	public boolean vbegin = true; 
	protected TextView tv_mem,tv_week; 
	private SharedPreferences spf;
	private SharedPreferences.Editor spf_edt;

	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);  
		setContentView(R.layout.calculator);  
		//程序开始：
		//邠心工作室
		//输入框
		et_led = (EditText)findViewById(R.id.ed_led);
		AssetManager mgr = getAssets();//得到AssetManager
		Typeface tf = Typeface.createFromAsset(mgr, "fonts/zdapp.ttf");//根据路径得到Typeface
		et_led.setTypeface(tf);//设置字体 
		calculator = (TextView)findViewById(R.id.calculatorScreenTextView);
		tv_mem = (TextView) findViewById(R.id.ed_mr);
		tv_week = (TextView) findViewById(R.id.cal_weekday);
		titleLayout = (LinearLayout) findViewById(R.id.themeLayout);
		//按钮列表
		btn[0] = (Button)findViewById(R.id.mButton1);	//0
		btn[1] = (Button)findViewById(R.id.mButton2);	//1
		btn[2] = (Button)findViewById(R.id.mButton3);	//2
		btn[3] = (Button)findViewById(R.id.mButton4);	//3
		btn[4] = (Button)findViewById(R.id.mButton5);	//4
		btn[5] = (Button)findViewById(R.id.mButton6);	//5
		btn[6] = (Button)findViewById(R.id.mButton7);	//6
		btn[7] = (Button)findViewById(R.id.mButton8);	//7
		btn[8] = (Button)findViewById(R.id.mButton9);	//8
		btn[9] = (Button)findViewById(R.id.mButton10);	//9
		btn_sub = (Button)findViewById(R.id.mButton11); //-
		btn_mul = (Button)findViewById(R.id.mButton12); //×
		btn_div = (Button)findViewById(R.id.mButton13); //÷
		btn_plus = (Button)findViewById(R.id.mButton14); //+
		btn_equal = (Button)findViewById(R.id.mButton15); //=
		btn_dot = (Button)findViewById(R.id.mButton16);   //.
		btn_c = (Button)findViewById(R.id.mButton17); 	//C
		btn_ce = (Button)findViewById(R.id.mButton18); 	//CE
		btn_bksp = (Button)findViewById(R.id.mButton19);//←
		btn_x = (Button)findViewById(R.id.mButton20);	//MS
		btn_zf = (Button)findViewById(R.id.mButton21);	//±
		btn_rm = (Button)findViewById(R.id.mButton22);	//MR
		btn_mc = (Button)findViewById(R.id.mButton23);	//MC
		btn_mx = (Button)findViewById(R.id.mButton24);	//M+
		btn_ms = (Button)findViewById(R.id.mButton25);	//M-
		btn_x2 = (Button)findViewById(R.id.mButton26);	//X²
		btn_gh = (Button)findViewById(R.id.mButton27);	//√
		btn_pre = (Button)findViewById(R.id.mButton28);	//%
		btn_pai = (Button)findViewById(R.id.mButton29);	// π
		btn_jc = (Button) findViewById(R.id.mButton30);	// X!
		btn_c2 = (Button) findViewById(R.id.mButton31);	// 1/X
		ImageButton btn_close = (ImageButton)findViewById(R.id.btn_close);
		spf = getSharedPreferences("zdapp",Context.MODE_PRIVATE);
		spf_edt = spf.edit();
		mmdouble = spf.getString("calculator","");
		tv_mem.setText(mmdouble);
		Date dd = new Date();
		String weeks;
		switch(dd.getDay())
		{
			case 0:
				weeks = "SU";
				break;
			case 1:
				weeks = "MO";
				break;
			case 2:
				weeks = "TU";
				break;
			case 3:
				weeks = "WE";
				break;
			case 4:
				weeks = "TH";
				break;
			case 5:
				weeks = "FR";
				break;
			case 6:
				weeks = "SA";
				break;
			default:
				weeks = "ZD";
				break;
		}
		tv_week.setText(weeks);
		btn_close.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				finish();
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
		//程序开始
		for(int i = 0; i < 10; i++)
		{
			btn[i].setOnClickListener(actionPerformed);
		}
		btn_div.setOnClickListener(actionPerformed);
		btn_mul.setOnClickListener(actionPerformed); 
		btn_sub.setOnClickListener(actionPerformed); 
		btn_plus.setOnClickListener(actionPerformed); 
		btn_equal.setOnClickListener(actionPerformed); 
		btn_dot.setOnClickListener(actionPerformed); 
		btn_bksp.setOnClickListener(actionPerformed); 
		btn_ce.setOnClickListener(actionPerformed); 
		btn_c.setOnClickListener(actionPerformed);
		btn_x.setOnClickListener(actionPerformed);
		btn_zf.setOnClickListener(actionPerformed);
		btn_rm.setOnClickListener(actionPerformed);
		btn_mc.setOnClickListener(actionPerformed);
		btn_mx.setOnClickListener(actionPerformed);
		btn_ms.setOnClickListener(actionPerformed);
		btn_x2.setOnClickListener(actionPerformed);
		btn_gh.setOnClickListener(actionPerformed);
		btn_pre.setOnClickListener(actionPerformed);
		btn_pai.setOnClickListener(actionPerformed);
		btn_jc.setOnClickListener(actionPerformed);
		btn_c2.setOnClickListener(actionPerformed);
		//除法
		/*btn_sub.setOnClickListener(new OnClickListener(){public void onClick(View v){
			if (et_led.getText().toString() != "0")
			{calculator.setText("÷");}
		}});*/
		et_led.addTextChangedListener(new TextWatcher()
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
			public void afterTextChanged(Editable p1)
			{
				et_led.setSelection(et_led.getText().toString().length());
			}
		});
		btn_bksp.setOnLongClickListener(new OnLongClickListener()
		{
			@Override
			public boolean onLongClick(View p1)
			{
				if (et_led.getText().toString().length() > 0 && !et_led.getText().toString().equals("0"))
				{
					et_led.setText("0");
					vbegin = true;
					return true;
				}
				else
					return false;
			}
		});
	} 
	private OnClickListener actionPerformed = new OnClickListener()
	{
		public void onClick(View v)
		{
			String command = ((Button)v).getText().toString(),
			str = et_led.getText().toString();
			if (str.equals("除数不能为0") || str.equals("运算出错") || str.equals("∞"))
				str = "0";
			if(command.compareTo("←") == 0)
			{
				if(str.length() > 1) 
					et_led.setText(str.substring(0, str.length() - 1)); 
				else if(str.length() == 1)
				{
					et_led.setText("0");
					vbegin = true; 
				}
				if(et_led.getText().toString().compareTo("-") == 0)
				{
					et_led.setText("0");
					vbegin = true;
				}
			}
			else if(command.compareTo("C") == 0)
			{
				calculator.setText("Calculator");
				et_led.setText("0");
				vbegin = true;
				predata = 0;
				preopt = "=";
			}
			else if(command.compareTo("CE") == 0)
			{
				calculator.setText("Calculator");
				et_led.setText("0");
				vbegin = true;
			}
			else if(command.compareTo("MS") == 0)
			{
				if (!str.equals("0"))
				{
					setmm(str);
					tv_mem.setText(mmdouble);
				}
			}
			else if(command.compareTo("MR") == 0)
			{
				if(mmdouble != "")
				{
					et_led.setText(mmdouble);
					vbegin = false;
				}
			}
			else if(command.compareTo("M-") == 0)
			{
				if (mmdouble != "")
				{
					double tem = Double.parseDouble(mmdouble);
					tem -= Double.parseDouble(str);
					setmm(""+tem);
				}
				else
				{
					setmm(str);
				}
				tv_mem.setText(mmdouble);
			}
			else if(command.compareTo("M+") == 0)
			{
				if (mmdouble != "")
				{
					double tem = Double.parseDouble(mmdouble);
					tem += Double.parseDouble(str);
					setmm(tem+"");
				}
				else
				{
					setmm(str);
				}
				tv_mem.setText(mmdouble);
			}
			else if(command.compareTo("MC") == 0)
			{
				setmm("0");
				tv_mem.setText(mmdouble);
			}
			else if(command.compareTo("X²") == 0)
			{
				double temp = Double.parseDouble(str);
				setText(temp * temp);
				vbegin = true;
			}
			else if(command.compareTo("1/X") == 0)
			{
				double temp = Double.parseDouble(str);
				et_led.	setText(String.valueOf(1 / temp));
				vbegin = true;
			}
			else if(command.compareTo("π") == 0)
			{
				et_led.	setText("3.141592653589793284626433832795");
				predata = 3.141592653589793284626433832795;
			}
			else if(command.compareTo("X!") == 0)
			{
				setText(getStep(Double.parseDouble(str)));
				vbegin = true;
			}
			else if(command.compareTo("√") == 0)
			{
				double temp = Double.parseDouble(str);
				setText(Math.sqrt(temp));
				vbegin = true;
			}
			else if(command.compareTo("%") == 0)
			{
				double temp = Double.parseDouble(str);
				setText(temp / 100);
				vbegin = true;
			}
			else if(command.compareTo(".") == 0)
			{
				boolean nodot = (str.indexOf('.') == -1);
				/*if (et_led.getText().toString().equals("0"))
				{
					wtNumber("0");
					wtNumber(command);
				}*/
				if(nodot)
				{
					vbegin = false;
					wtNumber(".");
				}
			}
			else if(command.compareTo("0") == 0)
			{
				if (Double.parseDouble(str) != 0 || et_led.getText().toString().indexOf(".") > 0)
					wtNumber("0");
			}
			else if(command.compareTo("±") == 0)
			{
				try
				{
					double data = Double.parseDouble(str);
					data = data * (-1);
					setText(data);
				}
				catch(NumberFormatException err)
				{
					et_led.setText("Number Format ERROR!");
				} 
			}
			else if("123456789".indexOf(command) != -1)
			{
				wtNumber(command);
			}
			else if("+-×÷=".indexOf(command) != -1)
			{
				wtOperater(command);
			}
		}
	};
	private void wtNumber(String str)
	{
		if(vbegin)
			et_led.setText(str);
		else
			et_led.append(str);
		vbegin = false;
	}
	private void setmm(String str)
	{
		try
		{
			double d = Double.parseDouble(str);
			if (d != 0)
			{
				int dotl = 0;
				if (String.valueOf(d).indexOf(".") != -1)
				{
					dotl = String.valueOf(d).substring(String.valueOf(d).indexOf(".")).length();
				}
				DecimalFormat fort = new DecimalFormat();
				if (String.valueOf(d).indexOf("E") == -1 && (dotl > 18 || dotl < 15))
					mmdouble = fort.format(d).replace(",","");
				else
					mmdouble = String.valueOf(d);
			}
			else
			{
				mmdouble = "";
			}
			spf_edt.putString("calculator",mmdouble);
			spf_edt.commit();
		}
		catch(Exception e)
		{
			calculator.setText("Ms Error");
		}
	}
	private void wtOperater(String opt)
	{
		try
		{
			double temp = Double.parseDouble(et_led.getText().toString());
			if(vbegin)
				preopt = opt;
			else
			{
				if(preopt.equals("="))
				{
					predata = temp;
				}
				else if(preopt.equals("+"))
				{
					predata += temp;
				}
				else if(preopt.equals("-"))
				{
					predata -= temp;
					//calculator.setText("-");
				}
				else if(preopt.equals("×"))
				{
					predata *= temp;
					//calculator.setText("×");
				}
				else if(preopt.equals("÷"))
				{
					if(temp != 0)
					{
						predata /= temp;
					}
					else
					{
						throw new ArithmeticException();
					}
				}
				setText(predata);
				preopt = opt;
			}
		}
		//错误提示
		catch(NumberFormatException e)
		{
			calculator.setText("Error");
			et_led.setText("运算出错");
		}
		catch(ArithmeticException e)
		{
			calculator.setText("Error");
			et_led.setText("除数不能为0");
			preopt = "=";
		}
		finally
		{
			vbegin = true;
		}
	}
	
	private void setText(double d)
	{
		int dotl = 0;
		if (String.valueOf(d).indexOf(".") != -1)
		{
			dotl = String.valueOf(d).substring(String.valueOf(d).indexOf(".")).length();
		}
		DecimalFormat fort = new DecimalFormat();
		if (String.valueOf(d).indexOf("E") == -1 && (dotl > 18 || dotl < 15))
			et_led.setText(fort.format(d).replace(",",""));
		else
			et_led.setText(String.valueOf(d));
	}
	
	private double getStep(double tmp)
	{
		double temp = 1;
		for (int i = 1;i <= tmp; i++)
		{
			temp *= i;
		}
		return temp;
	}
	private void setWeek()
	{
		Date dd = new Date();
		tv_week.setText(dd.getDay());
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
	}

	@Override
	protected void onDestroy()
	{
		// TODO: Implement this method
		super.onDestroy();
	}
}
//邠心工作室
//计算器源码
//正德应用 5.3
