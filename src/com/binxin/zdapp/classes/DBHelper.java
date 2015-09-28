package com.binxin.zdapp.classes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//import com.binxin.zdapp.classes.ContactColumn;

//通讯录应用程序数据库程序
public class DBHelper extends SQLiteOpenHelper
{
	public static final String DATABASE_NAME = "Contacts2.db"; //数据库名
	public static final int DATABASE_VERSION = 2; //版本
	public static final String CONTACTS_TABLE = "contacts"; //新表名
	//创建表
	private static final String DATABASE_CREATE = 
	"CREATE TABLE " + CONTACTS_TABLE +" ("					
	+ ContactColumn.ID+" integer primary key autoincrement,"
	+ ContactColumn.SORT+" text,"
	+ ContactColumn.NAME+" text,"
	+ ContactColumn.MOBILENUM+" text,"
	+ ContactColumn.HOMENUM+" text,"
	+ ContactColumn.QQ+" text,"
	+ ContactColumn.ADDRESS+" text,"
	+ ContactColumn.EMAIL+" text,"
	+ ContactColumn.BLOG+" text,"
	+ ContactColumn.KEYS+" text,"
	+ ContactColumn.DELETED + " bit,"
	+ ContactColumn.MORE + " text,"
	+ ContactColumn.IMG + " text,"
	+ ContactColumn.CREATE + " datetime,"
	+ ContactColumn.MODIFY + " datetime);";
	
	public DBHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(DATABASE_CREATE);
	}
	public static void CreateTable(SQLiteDatabase db)
	{
		db.execSQL(DATABASE_CREATE);
	}
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		db.execSQL("DROP TABLE IF EXISTS " + CONTACTS_TABLE);
		onCreate(db);
	}
}

