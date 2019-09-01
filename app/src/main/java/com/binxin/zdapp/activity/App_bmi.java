package com.binxin.zdapp.activity;

import java.math.BigDecimal;
import java.lang.Math;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.LinearLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.Html;

import com.binxin.zdapp.R;
import com.binxin.zdapp.classes.Common;
import com.binxin.zdapp.classes.MyThemes;

public class App_bmi extends Activity
{
	private LinearLayout titleLayout;
	private EditText txt_height,txt_weight;
	private TextView bm_type,bm_value,bm_tips;
	private Spinner gender;
	private ProgressBar progress;
	private int BM_SEX = 0;
	private float bm_bmi;
	private boolean isFat = false;
	private SharedPreferences spf;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_Holo_Light_NoActionBar);
		setContentView(R.layout.bm_layout);
		
		ImageButton back = (ImageButton) findViewById(R.id.btn_close);
		Button btn_cal = (Button) findViewById(R.id.bm_btnbmi);
		gender = (Spinner) findViewById(R.id.bm_Spinner);
		txt_height = (EditText) findViewById(R.id.bm_txtHeight);
		txt_weight = (EditText) findViewById(R.id.bm_txtWeight);
		bm_type = (TextView) findViewById(R.id.bm_bmType);
		bm_value = (TextView) findViewById(R.id.bm_bmiValue);
		bm_tips = (TextView) findViewById(R.id.bm_tips);
		progress = (ProgressBar) findViewById(R.id.bm_progress);
		titleLayout = (LinearLayout) findViewById(R.id.themeLayout);
		
		spf = getSharedPreferences("bmi_config", Context.MODE_PRIVATE);
		ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.bm_gender, android.R.layout.simple_spinner_item);//设置下拉列表的风格
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		gender.setAdapter(adapter);
		gender.setOnItemSelectedListener(new SpinnerXMLSelectedListener());
		gender.setSelection(spf.getInt("gender",0));
		progress.setBackgroundResource(R.drawable.bm_progress);
		
		back.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View view)
			{
				finish();
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
		btn_cal.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				try
				{
					if (txt_height.getText().toString().trim().length() <= 0)
					{
						Common.showShortToast(getApplicationContext(),R.string.bm_hheight);
						return;
					}
					if (txt_weight.getText().toString().trim().length() <= 0)
					{
						Common.showShortToast(getApplicationContext(),R.string.bm_hweight);
						return;
					}
					progress.setProgress(0);
					progress.setSecondaryProgress(0);
					float height,weight;
					height = Integer.parseInt(txt_height.getText().toString().trim());
					weight = Float.valueOf(txt_weight.getText().toString().trim());
					if (invalide(height,weight))
					{
						bm_bmi = calbmi(height,weight);
						BigDecimal bd = new BigDecimal(bm_bmi);
						bm_bmi = bd.setScale(1,BigDecimal.ROUND_HALF_UP).floatValue();
						bm_value.setText(""+bm_bmi);
						bm_type.setText(Html.fromHtml(getString(R.string.bm_type) + bmitotype(bm_bmi)));
						if (BM_SEX == 1 || BM_SEX == 2)
							progress.setProgress(getProgress(bm_bmi));
						else
							progress.setSecondaryProgress(getProgress(bm_bmi));
						bm_tips.setText(getStandard(height,weight));
					}
				}
				catch (Exception e)
				{
					bm_value.setText("0");
					bm_tips.setText("");
					bm_type.setText("");
					Common.showShortToast(getApplicationContext(),R.string.bm_out);
				}
			}
		});
	}
	
	private float calbmi(float height,float weight)
	{
		float hei = height / 100;
		float expon = hei * hei;
		return weight / expon;
	}
	
	private boolean invalide(float height,float weight) throws Exception
	{
		boolean state = false;
		switch (BM_SEX)
		{
			case 0:
				if (height >= 120 && height <= 200 && weight >= 20 && weight <= 250)
					state = true;
				break;
			case 1:
				if (height >= 125 && height <= 220 && weight >= 30 && weight <= 300)
					state = true;
				break;
			case 2:
				if (height >= 40 && height <= 150 && weight >= 3 && weight <= 40)
					state = true;
				break;
			case 3:
				if (height >= 35 && height <= 150 && weight >= 2 && weight <= 40)
					state = true;
				break;
		}
		if (!state)
			throw new Exception();
		return state;
	}
	
	private String bmitotype(float bmi)
	{
		String type = "";
		switch (BM_SEX)
		{
			case 0://女
			{
				if (bmi < 18.5f)
					type = "<font color='#FF88F7'>" + getString(R.string.bm_lean) + "</font>";
				else if (bmi >= 18.5f && bmi < 23.0f)
					type = "<font color='#1E90FF'>" + getString(R.string.bm_normal) + "</font>";
				else if (bmi >= 23.0f && bmi < 27.0f)
				{
					type = "<font color='#F75733'>" + getString(R.string.bm_chubby) + "</font>";
					isFat = true;
				}
				else if (bmi >= 27.0f)
					type = "<font color='#FF359A'>" + getString(R.string.bm_fat) + "</font>";
				break;
			}
			case 1://男
			{
				if (bmi < 18.5f)
					type = "<font color='#FF88F7'>" + getString(R.string.bm_lean) + "</font>";
				else if (bmi >= 18.5f && bmi < 24.0f)
					type = "<font color='#1E90FF'>" + getString(R.string.bm_normal) + "</font>";
				else if (bmi >= 24.0f && bmi < 28.0f)
					type = "<font color='#F75733'>" + getString(R.string.bm_chubby) + "</font>";
				else if (bmi >= 28.0f)
					type = "<font color='#FF359A'>" + getString(R.string.bm_fat) + "</font>";
				break;
			}
			case 2://男童
			{
				if (bmi < 17.0f)
					type = "<font color='#FF88F7'>" + getString(R.string.bm_lean) + "</font>";
				else if (bmi >= 17.0f && bmi < 22.2f)
					type = "<font color='#1E90FF'>" + getString(R.string.bm_normal) + "</font>";
				else if (bmi >= 22.2f && bmi < 24.8f)
					type = "<font color='#F75733'>" + getString(R.string.bm_chubby) + "</font>";
				else if (bmi >= 24.8f)
					type = "<font color='#FF359A'>" + getString(R.string.bm_fat) + "</font>";
				break;
			}
			case 3://女童
			{
				if (bmi < 16.9f)
					type = "<font color='#FF88F7'>" + getString(R.string.bm_lean) + "</font>";
				else if (bmi >= 16.9f && bmi < 22.1f)
					type = "<font color='#1E90FF'>" + getString(R.string.bm_normal) + "</font>";
				else if (bmi >= 22.1f && bmi < 24.5f)
				{
					type = "<font color='#F75733'>" + getString(R.string.bm_chubby) + "</font>";
					isFat = true;
				}
				else if (bmi >= 24.5f)
					type = "<font color='#FF359A'>" + getString(R.string.bm_fat) + "</font>";
				break;
			}
		}
		return type;
	}
	
	private int getProgress(float bmi)
	{
		int progres = 0;
		if (bmi < 18.5f)
			progres = (int)(bmi * 2.0f);
		else if (bmi >= 18.5f && bmi < 24.0f)
			progres = (int)(bmi * 2.5f);
		else if (bmi >= 24.0f)
			progres = (int)(bmi * 3.0f);
		if (isFat)
		{
			progres = (int)(bmi * 3.0f);
			isFat = false;
		}
		return progres;
	}
	
	private String getStandard(float height,float weight)
	{
		String strs = "";
		switch (BM_SEX)
		{
			case 0://女
			{
				strs = getString(R.string.bm_stheight) + "\n" + getWeightByBMI(height,18.5f) + "~" + getWeightByBMI(height,23.0f) + "kg\n";
				strs += getString(R.string.bm_stweight) + "\n" + getHeightByBMI(weight, 23.0f) + "~" + getHeightByBMI(weight, 18.5f) + "cm";
				break;
			}
			case 1://男
			{
				strs = getString(R.string.bm_stheight) + "\n" + getWeightByBMI(height,18.5f) + "~" + getWeightByBMI(height,24.0f) + "kg\n";
				strs += getString(R.string.bm_stweight) + "\n" + getHeightByBMI(weight, 24.0f) + "~" + getHeightByBMI(weight, 18.5f) + "cm";
				break;
			}
			case 2://男童
			{
				strs = getString(R.string.bm_stheight) + "\n" + getWeightByBMI(height,17.0f) + "~" + getWeightByBMI(height,22.2f) + "kg\n";
				strs += getString(R.string.bm_stweight) + "\n" + getHeightByBMI(weight, 22.2f) + "~" + getHeightByBMI(weight, 17.0f) + "cm";
				break;
			}
			case 3://女童
			{
				strs = getString(R.string.bm_stheight) + "\n" + getWeightByBMI(height,16.9f) + "~" + getWeightByBMI(height,22.1f) + "kg\n";
				strs += getString(R.string.bm_stweight) + "\n" + getHeightByBMI(weight, 22.1f) + "~" + getHeightByBMI(weight, 16.9f) + "cm";
				break;
			}
		}
		return strs;
	}
	
	private int getHeightByBMI(float weight,float bmi)
	{
		return (int)(Math.sqrt((double)(weight / bmi)) * 100);
	}
	
	private float getWeightByBMI(float height,float bmi)
	{
		float hei = height / 100;
		return new BigDecimal(hei * hei * bmi).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
	}
	
	public class SpinnerXMLSelectedListener implements AdapterView.OnItemSelectedListener
	{
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
		{
			BM_SEX = arg2;
		}
		public void onNothingSelected(AdapterView<?> arg0)
		{

		}
	}
	
	public void setMyTheme()
	{
		MyThemes.setThemes(this,titleLayout);
		progress.setProgress(0);
		progress.setSecondaryProgress(0);
	}

	@Override
	protected void onResume()
	{
		setMyTheme();
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
		progress.setProgress(0);
		progress.setSecondaryProgress(0);
		SharedPreferences.Editor edt = spf.edit();
		edt.putInt("gender",BM_SEX);
		edt.commit();
		super.onDestroy();
	}
}
