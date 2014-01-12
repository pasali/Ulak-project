package com.pasali.ulak;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	public static final String TABLE_MSG = "TextMsg";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NO = "number";
	public static final String COLUMN_BODY = "body";
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "MessagesDB";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	private static final String DATABASE_CREATE = "create table " + TABLE_MSG
			+ "(" + COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_NO + " text not null, " + COLUMN_BODY + " text not null"
			+ ");";

	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		Log.w(DatabaseHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_MSG);
		onCreate(database);
	}
}
