package com.binxin.zdapp.classes;

import java.lang.String;
import java.lang.Integer;

public class ScaleThransform
{
	public static String changeTO(int aS, int bS, String changedStr)//转换的方法
	{
		Integer tStation=new Integer(1);
		
		tStation=tStation.valueOf(changedStr.trim(),aS);//先转化为A进制
		return tStation.toString(tStation,bS);//再转化为B进制并返回
	}
}
