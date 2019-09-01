package com.binxin.zdapp.classes;

import java.util.List;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageInfo;

public class CheckBxaPackage
{
	public boolean isAvilible(Context context, String packageName)
	{ 
		final PackageManager packageManager = context.getPackageManager();//获取packagemanager 
		List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);//获取所有已安装程序的包信息 
		List<String> pName = new ArrayList<String>();//用于存储所有已安装程序的包名 
		//从pinfo中将包名字逐一取出，加入pName list中 
		if(pinfo != null)
		{ 
			for(int i = 0; i < pinfo.size(); i++)
			{ 
				String pn = pinfo.get(i).packageName; 
				pName.add(pn); 
			} 
		} 
		return pName.contains(packageName);//判断pName中是否有目标程序的包名，有TRUE，没有FALSE 
    } 
}
