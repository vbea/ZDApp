package com.binxin.zdapp.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.util.ArrayList;

import com.binxin.zdapp.classes.ExceptionHandler;

public class FileOperate
{
	private ArrayList<FileTemp> tmpList;
	long total = 0;
	long unit = 0;
	private int totals = 0;
	private int units = 0;
	
	public FileOperate(ArrayList<FileTemp> tmp)
	{
		tmpList = tmp;
		totals = 0;
		calculTotalFile();
	}
	
	public int[] start(OnFileOperation opt)
	{
		int[] state = new int[3];
		for (FileTemp tmp : tmpList)
		{
			if (units >= totals)
				break;
			if (tmp.oldAction == FileTemp.ACTION_COPY) {
				if (tmp.exist) { //已存在
					if (tmp.tmpFile.isFile()) { //复制文件
						if (tmp.action == FileTemp.ACTION_SKIP) {
							units++;
							state[2]++;
							continue;
						} else if (tmp.action == FileTemp.ACTION_COVER) {
							opt.onFileOperations(tmp.tmpFile.getName(), total, unit);
							if (copyFile(tmp.tmpFile, tmp.newFile, opt))
								state[0]++;
							else
								state[1]++;
							units++;
						}
					}
					else { //复制文件夹
						if (tmp.action == FileTemp.ACTION_SKIP) {
							units+=tmp.tmpFile.list().length;
							state[2]++;
							continue;
						}
						else if (tmp.action == FileTemp.ACTION_COVER) {
							opt.onFileOperations(tmp.tmpFile.getName(), total, unit);
							int[] res =copyFolder(tmp.tmpFile, tmp.newFile, false, opt);
							units++;
							state[0] += res[0];
							state[1] += res[1];
						}
					}
				}
				else //不存在
				{
					if (tmp.tmpFile.isFile()) {
						opt.onFileOperations(tmp.tmpFile.getName(), total, unit);
						if (copyFile(tmp.tmpFile, tmp.newFile, opt))
							state[0]++;
						else
							state[1]++;
						units++;
					} else {
						opt.onFileOperations(tmp.tmpFile.getName(), total, unit);
						int[] res =copyFolder(tmp.tmpFile, tmp.newFile, false, opt);
						units++;
						state[0] += res[0];
						state[1] += res[1];
					}
				}
			}
			else if (tmp.oldAction == FileTemp.ACTION_CUT)
			{
				if (tmp.exist) { //已存在
					if (tmp.tmpFile.isFile()) { //移动文件
						if (tmp.action == FileTemp.ACTION_SKIP) {
							units++;
							state[2]++;
							continue;
						} else if (tmp.action == FileTemp.ACTION_COVER) {
							opt.onFileOperation(tmp.tmpFile.getName(), totals, units);
							if (moveFile(tmp.tmpFile, tmp.newFile))
								state[0]++;
							else
								state[1]++;
							units++;
						}
					}
					else { //移动文件夹
						if (tmp.action == FileTemp.ACTION_SKIP) {
							units+=tmp.tmpFile.list().length;
							state[2]++;
							continue;
						}
						else if (tmp.action == FileTemp.ACTION_COVER) {
							opt.onFileOperation(tmp.tmpFile.getName(), totals, units);
							int[] res = moveFolder(tmp.tmpFile, tmp.newFile);
							state[0] += res[0];
							state[1] += res[1];
						}
					}
				}
				else //不存在
				{
					if (tmp.tmpFile.isFile()) {
						opt.onFileOperation(tmp.tmpFile.getName(), totals, units);
						if (moveFile(tmp.tmpFile, tmp.newFile))
							state[0]++;
						else
							state[1]++;
						units++;
					} else {
						opt.onFileOperation(tmp.tmpFile.getName(), totals, units);
						int[] res = moveFolder(tmp.tmpFile, tmp.newFile);
						state[0] += res[0];
						state[1] += res[1];
						units++;
					}
				}
			}
		}
		return state;
	}
	
	//计算总文件数量
	private void calculTotalFile()
	{
		for (FileTemp tmp : tmpList)
		{
			if (tmp.tmpFile.isFile())
			{
				totals++;
				total+=tmp.tmpFile.length();
			}
			else
				calculChild(tmp.tmpFile);
		}
	}
	
	private void calculChild(File folder)
	{
		totals++;
		for (File file : folder.listFiles())
		{
			if (file.isFile())
			{
				totals++;
				total+=file.length();
			}
			else
				calculChild(file);
		}
	}
	
	//复制单个文件
	public boolean copyFile(File src, File target, OnFileOperation opt)
	{
		InputStream in = null;
		OutputStream out = null;
		BufferedInputStream bin = null;
		BufferedOutputStream bout = null;
		try
		{
			in = new FileInputStream(src);
			out = new FileOutputStream(target);
			bin = new BufferedInputStream(in);
			bout = new BufferedOutputStream(out);
			byte[] b = new byte[5120];
			int len = bin.read(b);
			while (len != -1)
			{
				bout.write(b, 0, len);
				len = bin.read(b);
				unit+=len;
				opt.onFileOperations(src.getName(), total, unit);
			}
			if (target.exists())
				return true;
		}
		catch (Exception e)
		{
			ExceptionHandler.log("copyFile():" + e.toString());
		}
		finally
		{
			try
			{
				if (bin != null)
					bin.close();
				if (bout != null)
					bout.close();
			}
			catch (IOException e)
			{
				ExceptionHandler.log("copyFile(finally):" + e.toString());
			}
		}
		return true;
	}
	
	private int[] copyFolder(File folder, File target, boolean existCover, OnFileOperation opt)
	{
		int[] state = new int[2];
		if (!target.exists())
		{
			if (!createFolder(folder, target))
			{
				state[1]++;
				return state;
			}
			units++;
		}
		for (File f : folder.listFiles())
		{
			if (units >= totals)
				break;
			File newf = new File(target.getAbsolutePath() + File.separator + f.getName());
			if (f.isFile())
			{
				if (newf.exists() && !existCover)
				{
					state[0]++;
					continue;
				}
				if (copyFile(f, newf, opt))
					state[0]++;
				else
					state[1]++;
				units++;
				opt.onFileOperations(f.getName(), total, unit);
			}
			else
			{
				int[] stat = copyFolder(f, newf, existCover, opt);
				state[0] += stat[0];
				state[1] += stat[1];
			}
		}
		return state;
	}
	
	private boolean moveFile(File oldFile, File newFile)
	{
		try
		{
			return oldFile.renameTo(newFile);
		}
		catch (Exception e)
		{
			ExceptionHandler.log("moveFile()" + e.toString());
		}
		return false;
	}
	
	private int[] moveFolder(File folder, File target)
	{
		int[] state = new int[2];
		if (moveFile(folder, target))
			state[0]++;
		else
			state[1]++;
		/*if (!target.exists())
		{
			if (!createFolder(folder, target))
			{
				state[1]++;
				units++;
				opt.onFileOperation("", totals, units);
				return state;
			}
		}
		if (folder.list().length == 0)
		{
			if (deleteFile(folder))
				state[0]++;
			else
				state[1]++;
			return state;
		}
		for (File f : folder.listFiles())
		{
			File newf = new File(target.getAbsolutePath() + File.separator + f.getName());
			if (f.isFile())
			{
				if (copyFile(f, newf))
				{
					state[0]++;
					deleteFile(f);
				}
				else
					state[1]++;
				units++;
				opt.onFileOperation(f.getName(), totals, units);
			}
			else
			{
				int[] stat = moveFolder(f, newf, opt);
				state[0] += stat[0];
				state[1] += stat[1];
			}
		}*/
		return state;
	}
	
	private boolean createFolder(File src, File tar)
	{
		try
		{
			return tar.mkdir();
		}
		catch (Exception e)
		{
			ExceptionHandler.log("createFolder():" + e.toString());
		}
		return false;
	}
	
	//删除文件
	public boolean deleteFile(File file)
	{
		boolean result = false;
		try
		{
			if (file != null)
				result = file.delete();
		}
		catch (Exception e)
		{
			ExceptionHandler.log("Delete File:" + e.getMessage());
			result = false;
		}
		return result;
	}
	
	public interface OnFileOperation
	{
		void onFileOperation(String fileName, int total, int progress);
		void onFileOperations(String fileName, long total, long current);
	}
}
