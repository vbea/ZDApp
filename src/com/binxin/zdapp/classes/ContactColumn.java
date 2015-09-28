package com.binxin.zdapp.classes;

import android.provider.BaseColumns;
//正德应用-私密通讯录
//定义数据
public class ContactColumn implements BaseColumns
{
	public ContactColumn()
	{
	}
	//列名
	public static final String ID = "id";//ID，自增长
	public static final String SORT = "sort_key";//排序列
	public static final String NAME = "name";//姓名
	public static final String MOBILENUM = "mobileNumber";//移动电话
	public static final String HOMENUM = "homeNumber";//家庭电话
	public static final String QQ = "qq";//QQ号码
	public static final String ADDRESS = "address";//地址
	public static final String EMAIL = "email";//邮箱
	public static final String BLOG = "blog";//博客
	public static final String KEYS = "keys";//关键字
	public static final String DELETED = "deleted";//是否删除
	public static final String MORE = "more";//更多
	public static final String IMG = "img";//头像
	public static final String CREATE = "CreateDate";//创建时间
	public static final String MODIFY = "LastModifyDate";//最后一次修改时间
	//列 索引值
	public static final int ID_COLUMN = 0;
	public static final int SORT_COLUMN = 1;
	public static final int NAME_COLUMN = 2;
	public static final int MOBILENUM_COLUMN = 3;
	public static final int HOMENUM_COLUMN = 4;
	public static final int QQ_COLUMN = 5;
	public static final int ADDRESS_COLUMN = 6;
	public static final int EMAIL_COLUMN = 7;
	public static final int BLOG_COLUMN = 8;
	public static final int KEYS_COLUMN = 9;
	public static final int DELETED_COLUMN = 10;
	public static final int MORE_COLUMN = 11;
	public static final int IMG_COLUMN = 12;
	public static final int CREATE_COLUMN = 13;
	public static final int MODIFY_COLUMN = 14;

	//查询结果
	public static final String[] PROJECTION ={
		ID,
		SORT,
		NAME,
		MOBILENUM,
		HOMENUM,
		QQ,
		ADDRESS,
		EMAIL,
		BLOG,
	};
}

