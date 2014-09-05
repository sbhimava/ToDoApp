package com.sandeep.todoapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{

	public static final String TABLE_ITEMS = "todoitems";
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_PRIORITY = "priority";
	public static final String COLUMN_CREATED = "created";
	public static final String COLUMN_DUEDATE = "duedate";
	public static final String COLUMN_REMIND = "remind";
	public static final String COLUMN_REPEAT = "repeat";
	public static final String COLUMN_COMPLETED = "completed";
	
	private static final String DB_NAME = "todoitems.db";
	private static final int DB_VERSION = 1;
	
	// Create statement
	private static final String DB_CREATE = "create table " + TABLE_ITEMS
			+ "( " + COLUMN_ID + " integer primary key autoincrement"
			+ ", " + COLUMN_NAME + " text not null"
			+ ", " + COLUMN_DESCRIPTION + " text"
			+ ", " + COLUMN_PRIORITY + " integer not null default 1"
			+ ", " + COLUMN_CREATED + " text default (strftime('%Y-%m-%dT%H:%M:%f', 'now'))"
			+ ", " + COLUMN_DUEDATE + " text"
			+ ", " + COLUMN_REMIND + " text"
			+ ", " + COLUMN_REPEAT + " integer default 0"
			+ ", " + COLUMN_COMPLETED + " integer not null default 0"
			+ " )";
	
	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DB_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		// Upgrade database code
	}
	
}
