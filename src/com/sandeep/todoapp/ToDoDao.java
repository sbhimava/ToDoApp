package com.sandeep.todoapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ToDoDao {
	private SQLiteDatabase db;
	private DBHelper dbHelper;
	
	public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";
	
	// Priority constants
	public static final int PRIORITY_NORMAL = 0;
	public static final int PRIORITY_IMPORTANT = 1;
	public static final int PRIORITY_EXTREME = 2;
	public static final String[] PRIORITY_STRINGS = {"NORMAL", "IMPORTANT", "EXTREMELY IMPORTANT"};
	
	public static final int COMPLETED_FALSE = 0;
	public static final int COMPLETED_TRUE = 1;
	public static final int REPEAT_NONE = 0;			// repeat in days
	
	private String[] allColumns = {
		DBHelper.COLUMN_ID,
		DBHelper.COLUMN_NAME,
		DBHelper.COLUMN_DESCRIPTION,
		DBHelper.COLUMN_PRIORITY,
		DBHelper.COLUMN_CREATED,
		DBHelper.COLUMN_DUEDATE,
		DBHelper.COLUMN_REMIND,
		DBHelper.COLUMN_REPEAT,
		DBHelper.COLUMN_COMPLETED
	};
	
	public ToDoDao(Context context) {
		dbHelper = new DBHelper(context);
	}
	
	public void open() throws SQLException {
		db = dbHelper.getReadableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public ToDoItem createItem(String name, String description, int priority,
			Date dueDate, Date remind, int repeat, int completed) throws ParseException {
		ContentValues values = new ContentValues();
		values.put(DBHelper.COLUMN_NAME, name);
		if (description != null) {
			values.put(DBHelper.COLUMN_DESCRIPTION, description);
		}
		if (priority < PRIORITY_NORMAL || priority > PRIORITY_EXTREME) {
			priority = PRIORITY_NORMAL;
		}
		values.put(DBHelper.COLUMN_PRIORITY, priority);
		if (dueDate != null) {
			values.put(DBHelper.COLUMN_DUEDATE, dateToString(dueDate, null));
			// Only if there is a due date
			if (REPEAT_NONE < repeat) {
				values.put(DBHelper.COLUMN_REPEAT, repeat);
			}
		}
		if (remind != null) {
			values.put(DBHelper.COLUMN_REMIND, dateToString(remind, null));
		}
		if (COMPLETED_FALSE != completed || COMPLETED_TRUE != completed) {
			completed = COMPLETED_FALSE;
		}
		values.put(DBHelper.COLUMN_COMPLETED, completed);
		long id = db.insert(DBHelper.TABLE_ITEMS, null, values);
		Cursor cursor = db.query(DBHelper.TABLE_ITEMS, allColumns, DBHelper.COLUMN_ID + " = " + id, null, null, null, null);
		cursor.moveToFirst();
		ToDoItem item = cursorToItem(cursor);
		return item;
	}
	
	public int updateItem (long id, String name, String description, int priority,
			Date dueDate, Date remind, int repeat, int completed) throws ParseException {
		ContentValues values = new ContentValues();
		values.put(DBHelper.COLUMN_NAME, name);
		if (description != null) {
			values.put(DBHelper.COLUMN_DESCRIPTION, description);
		}
		if (priority < PRIORITY_NORMAL || priority > PRIORITY_EXTREME) {
			priority = PRIORITY_NORMAL;
		}
		values.put(DBHelper.COLUMN_PRIORITY, priority);
		if (dueDate != null) {
			values.put(DBHelper.COLUMN_DUEDATE, dateToString(dueDate, null));
			// Only if there is a due date
			if (REPEAT_NONE < repeat) {
				values.put(DBHelper.COLUMN_REPEAT, repeat);
			}
		}
		if (remind != null) {
			values.put(DBHelper.COLUMN_REMIND, dateToString(remind, null));
		}
		if (COMPLETED_FALSE != completed || COMPLETED_TRUE != completed) {
			completed = COMPLETED_FALSE;
		}
		String[] whereArgs = {Long.toString(id)};
		int result = db.update(DBHelper.TABLE_ITEMS, values, "id=?", whereArgs);
		return result;
	}
	
	public void deleteItem(ToDoItem item) {
		long id = item.getId();
		db.delete(DBHelper.TABLE_ITEMS, DBHelper.COLUMN_ID + " = " + id, null);
	}
	
	public ArrayList<ToDoItem> getAllItems() throws ParseException {
		ArrayList<ToDoItem> items = new ArrayList<ToDoItem>();
		Cursor cursor = db.query(DBHelper.TABLE_ITEMS, allColumns, null, null, null, null, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			ToDoItem item = cursorToItem(cursor);
			items.add(item);
			cursor.moveToNext();
		}
		cursor.close();
		return items;
	}

	private ToDoItem cursorToItem(Cursor cursor) throws ParseException {
		ToDoItem item = new ToDoItem();
		long id = cursor.getLong(0);
		item.setId(id);
		item.setName(cursor.getString(1));
		try {
			item.setDescription(cursor.getString(2));
		} catch(Exception e) {
			Log.d("TODO", "Description is null for " + id);
		}
		item.setPriority(cursor.getInt(3));
		item.setCreated(stringToDate(cursor.getString(4), null, null));
		Log.i("TODO", "Created " + item.getCreated().toString());
		try {
			item.setDueDate(stringToDate(cursor.getString(5), null, null));
		} catch(Exception e) {
			Log.d("TODO", "No due date for " + id);
		}
		try {
			item.setRemind(stringToDate(cursor.getString(6), null, null));
		} catch(Exception e) {
			Log.d("TODO", "No reminder set for " + id);
		}
		item.setRepeat(cursor.getInt(7));
		item.setCompleted(cursor.getInt(8));
		return item;
	}
	
	@SuppressLint("SimpleDateFormat")
	public Date stringToDate(String strDate, TimeZone fromTimeZone, TimeZone toTimeZone)
			throws ParseException{
		if (strDate == null) return null;
		if (fromTimeZone == null) {
			fromTimeZone = TimeZone.getTimeZone("UTC");
		}
		if (toTimeZone == null) {
			toTimeZone = TimeZone.getDefault();
		}
		SimpleDateFormat fromFormat = new SimpleDateFormat(DATE_FORMAT);
		fromFormat.setTimeZone(fromTimeZone);
		Date fromDate = fromFormat.parse(strDate);
		SimpleDateFormat toFormat = new SimpleDateFormat(DATE_FORMAT);
		toFormat.setTimeZone(toTimeZone);
		return toFormat.parse(toFormat.format(fromDate));
	}
	
	@SuppressLint("SimpleDateFormat")
	public String dateToString(Date date, TimeZone toTimeZone)
			throws ParseException {
		if (date == null) return null;
		if (toTimeZone == null) {
			toTimeZone = TimeZone.getTimeZone("UTC");
		}
		SimpleDateFormat toFormat = new SimpleDateFormat(DATE_FORMAT);
		toFormat.setTimeZone(toTimeZone);
		Log.i("TODO", toFormat.format(date));
		return toFormat.format(date);
	}
}
