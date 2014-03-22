package com.pasali.ulak;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MsgDAO {

	private SQLiteDatabase db;
	private DatabaseHelper dbHelper;

	public MsgDAO(Context context) {
		dbHelper = new DatabaseHelper(context);
	}

	public void addMsg(Message msg) {
		db = dbHelper.getWritableDatabase();
		ContentValues vals = new ContentValues();
		vals.put(DatabaseHelper.COLUMN_NO, msg.getNo());
		vals.put(DatabaseHelper.COLUMN_BODY, msg.getBody());
		db.insert(DatabaseHelper.TABLE_MSG, null, vals);
		dbHelper.close();
	}

	
	public ArrayList<String> getAllMsgsByNo(String no){
		ArrayList<String> msgs = new ArrayList<String>();
		db = dbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_MSG
				+ " WHERE number= ?", new String[] { no});
		if (cursor.moveToFirst()) {
			do {
				msgs.add(cursor.getString(2));
			} while (cursor.moveToNext());
		}
		return msgs;
		
	}
	
	public Message getMsg(long id) {
		db = dbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_MSG
				+ " WHERE _id= ?", new String[] { String.valueOf(id) });
		Message msg = null;
		if (cursor != null && cursor.moveToFirst()) {
			msg = new Message(cursor.getString(1), cursor.getString(2));
		}
		cursor.close();
		dbHelper.close();
		return msg;
	}

	public int getLastId() {
		db = dbHelper.getReadableDatabase();
		String sql = "SELECT MAX(_id) FROM " + DatabaseHelper.TABLE_MSG;
		Cursor cursor = db.rawQuery(sql, null);
		cursor.moveToFirst();
		int ID = cursor.getInt(0);
		cursor.close();
		dbHelper.close();
		return ID;
	}

	public ConcurrentHashMap<String, String> getAllNo() {
		ConcurrentHashMap<String, String> numbers = new ConcurrentHashMap<String, String>();
		db = dbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select * from " + DatabaseHelper.TABLE_MSG, null);
		if (cursor.moveToFirst()) {
			do {
				numbers.put(
						cursor.getString(1),
						String.valueOf(cursor.getInt(0)));
			} while (cursor.moveToNext());
		}
		dbHelper.close();
		return numbers;
	}

	public void delAllMsg(String no) {
		db = dbHelper.getWritableDatabase();
		db.delete(DatabaseHelper.TABLE_MSG, DatabaseHelper.COLUMN_NO + " = ?",
				new String[] {no});
		dbHelper.close();
	}
	
	public void delMsg(long id) {
		db = dbHelper.getWritableDatabase();
		db.delete(DatabaseHelper.TABLE_MSG, DatabaseHelper.COLUMN_ID + " = ?",
				new String[] { String.valueOf(id)});
		dbHelper.close();
	}

	public void emptyTable() {
		db = dbHelper.getWritableDatabase();
		db.delete(DatabaseHelper.TABLE_MSG, null, null);
		dbHelper.close();
	}
}
