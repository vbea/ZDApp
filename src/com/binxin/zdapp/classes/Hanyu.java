package com.binxin.zdapp.classes;

import java.util.Set;
import java.util.HashSet;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class Hanyu
{
	private HanyuPinyinOutputFormat format = null;
	private String [] pinyin;

	public Hanyu()
	{
		format = new HanyuPinyinOutputFormat();
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		format.setVCharType(HanyuPinyinVCharType.WITH_V);
		pinyin = null;
	}

	public String[] getCharacterToPinyinArray(char c)
	{
		try
		{
			pinyin = PinyinHelper.toHanyuPinyinStringArray(c, format);
		}
		catch (BadHanyuPinyinOutputFormatCombination e)
		{

		}
		if (pinyin == null)
			return null;
		return pinyin;
	}

	public String getCharacterToPinyin(char c)
	{
		try
		{
			String strs = String.valueOf(c);
			if (strs.equals("曾"))
			{
				pinyin = new String[]{"zeng"};
			}
			else if (strs.equals("单"))
			{
				pinyin = new String[]{"shan"};
			}
			else
				pinyin = PinyinHelper.toHanyuPinyinStringArray(c, format);
		}
		catch (BadHanyuPinyinOutputFormatCombination e)
		{

		}
		if (pinyin == null)
			return null;
		return pinyin[0];
	}

	public String getPinYinSoryKey(String name)
	{
		StringBuilder sb = new StringBuilder();
		StringBuilder sr = new StringBuilder();
		String tmpPinyin = null;
		for (int i=0;i<name.length();i++)
		{
			tmpPinyin = getCharacterToPinyin(name.charAt(i));
			if (tmpPinyin == null)
				sb.append(name.charAt(i));
			else
			{
				sb.append(tmpPinyin);
				sr.append(tmpPinyin.substring(0,1));
			}
		}
		return sb.toString()+" "+sr.toString();
	}

	public String getPinYinSoryKeys(String name)
	{
		StringBuilder sb = new StringBuilder();
		String[] tmpPinyin = null;
		for (int i=0;i<name.length();i++)
		{
			tmpPinyin = getCharacterToPinyinArray(name.charAt(i));
			if (tmpPinyin == null)
				sb.append(name.charAt(i));
			else
			{
				Set<String> set = new HashSet<String>();
				for (String s : tmpPinyin) set.add(s);
				for (Object s : set.toArray()) sb.append(s);
			}
		}
		return sb.toString();
	}
}
