package com.binxin.zdapp.activity;

import android.app.Activity;
import android.app.AlertDialog;  
import android.content.DialogInterface;  
import android.content.SharedPreferences;    
import java.io.IOException;  
import java.io.BufferedReader;  
import java.io.InputStreamReader;  
import java.io.Closeable;
import android.widget.Toast;
import android.content.*;
import java.lang.*;  

import com.binxin.zdapp.R;

class Eula {      
	private static final String ASSET_EULA = "EULA";      
	private static final String PREFERENCE_EULA_ACCEPTED = "eula.accepted";      
	private static final String PREFERENCES_EULA = "eula";        
	  
	
	static interface OnEulaAgreedTo {
		/**           
		 * VBE Atelier       
		 */          
		void onEulaAgreedTo();      
	}        
	    
	static boolean show(final Activity activity) {          
		final SharedPreferences preferences = activity.getSharedPreferences(PREFERENCES_EULA, Activity.MODE_PRIVATE);          
		if (!preferences.getBoolean(PREFERENCE_EULA_ACCEPTED, false)) {              
			final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
			builder.setIcon(R.drawable.zdapp);
			builder.setTitle(R.string.eula_title);              
			//builder.setCancelable(true);
            builder.setMessage("第一次打开手电筒，请点击始终，以后可快速打开手电筒。\n手电筒开启后，可点击返回键关闭(点击屏幕也可执行开关手电筒功能)");
			builder.setPositiveButton(R.string.eula_accept, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {                      
					accept(preferences);                      
					if (activity instanceof OnEulaAgreedTo) {                          
						((OnEulaAgreedTo) activity).onEulaAgreedTo();                      
					}                  
				}              
			});              
			
			builder.setNegativeButton(R.string.eula_refuse, new DialogInterface.OnClickListener()
			{                  
				public void onClick(DialogInterface dialog, int which)
				{
					refuse(activity);
					//Toast t = Toast.makeText(Eula.this, "未能激活手电筒", Toast.LENGTH_SHORT);
					//t.show();
				}              
			});              
			
			builder.setOnCancelListener(new DialogInterface.OnCancelListener() {                  
				public void onCancel(DialogInterface dialog) {                      
					refuse(activity);                  
				}              
			});              
			
			//builder.setMessage(readEula(activity));              
			builder.create().show();              
			return false;          
		}          
		
		return true;      
	}        
	
	private static void accept(SharedPreferences preferences) {          
		preferences.edit().putBoolean(PREFERENCE_EULA_ACCEPTED, true).commit();      
	}        
	
	private static void refuse(Activity activity) {          
		activity.finish();      
	}        
	
	private static CharSequence readEula(Activity activity) {          
		BufferedReader in = null;          
		try {              
			in = new BufferedReader(new InputStreamReader(activity.getAssets().open(ASSET_EULA)));              
			String line;              
			StringBuilder buffer = new StringBuilder();              
			while ((line = in.readLine()) != null) buffer.append(line).append('\n');              
			return buffer;          
		} catch (IOException e) {              
			return "";          
		} 
		
		finally {              
			closeStream(in);          
		}      
	}        
	
	/*Closes*/
	private static void closeStream(Closeable stream) {          
		if (stream != null) {              
			try {                  
				stream.close();              
			} catch (IOException e) {                  
				// Ignore              
			}          
		}      
	}  
} 
