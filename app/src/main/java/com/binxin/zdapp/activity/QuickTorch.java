package com.binxin.zdapp.activity;    

import android.app.Activity;  
import android.content.Intent;  
import android.util.Log;    

public class QuickTorch extends Activity {      
	private static final String TAG = QuickTorch.class.getSimpleName();        
	
	 
	@Override    
	public void onStart() {      
		super.onStart();      
		Log.d(TAG, "onStart()");      
		if (App_torch.getTorch() == null) {        
			Log.d(TAG, "torch == null");        
			Intent intent = new Intent(this, App_torch.class);        
			startActivity(intent);      
		} else {        
			Log.d(TAG, "torch != null");      
		}      
		
		finish();    
	}  
}    
