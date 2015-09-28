package com.binxin.zdapp.activity;  
  
import android.app.Activity;  
import android.os.Bundle;  
import android.view.WindowManager;
import com.binxin.zdapp.classes.PointerLocationView;
  
/** 多点测试
 * Demonstrates wrapping a layout in a ScrollView. 
 *  邠心工作室
 */  
public class PointerLocation extends Activity {  
    @Override  
    protected void onCreate(Bundle icicle) {  
        super.onCreate(icicle);  
        setContentView(new PointerLocationView(this));  
          
        // Make the screen full bright for this activity.   
       /* WindowManager.LayoutParams lp = getWindow().getAttributes();  
        lp.screenBrightness = 1.0f;  
        getWindow().setAttributes(lp);  */
    }  
}  
