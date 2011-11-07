/**
 *  Program: Shopping List
 *  Author: Matteo Fini <mf.calimero@gmail.com>
 *  Year: 2011
 *  
 *	This file is part of "Shopping List".
 *	"Liturgia delle Ore" is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  "Shopping List" is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  You should have received a copy of the GNU General Public License
 *  along with Foobar.  If not, see <http://www.gnu.org/licenses/>. 
 */
package com.matteofini.shoppinglist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ShoppingDb{
	
	private SQLiteDatabase db;
	private ShoppingDbOpenHelper dbh;
	private final Context ctx;
	
	private static final String NAME = "shoppingdb";
	private static final int VERSION = 1;
	private static final String ID = "_id";

	private static final String TABLE_GLOBAL = 
		"create table global ("+ID+" integer primary key autoincrement, title text not null, date integer default NULL);";
	
	private static final String TABLE_LIST = "create table list ("+ID+" integer primary key references global ("+ID+") on delete cascade, content text default NULL);";
	
	private class ShoppingDbOpenHelper extends SQLiteOpenHelper{
		public ShoppingDbOpenHelper(Context context) {
			super(context, NAME, null, VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(TABLE_GLOBAL);
			db.execSQL(TABLE_LIST);
			Log.println(Log.INFO, "ShoppingDbOpenHelper", "Shoppingdb created");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w("ShoppingDbOpenHelper", "Upgrading database from version " + oldVersion + " to "
	                + newVersion + ", which will destroy all old data");
	        db.execSQL("DROP TABLE IF EXISTS "+TABLE_GLOBAL);
	        db.execSQL("DROP TABLE IF EXISTS "+TABLE_LIST);
	        onCreate(db);
		}
	}
	
	public ShoppingDb(Context ctx){
		this.ctx = ctx;
	}
	
	
	public ShoppingDb open(){
		dbh = new ShoppingDbOpenHelper(ctx);
		db = dbh.getWritableDatabase();
		return this;
	}
	
	public void close(){
		dbh.close();
	}
	
	public Cursor getShoppingList(){
		Cursor c;
		c = db.rawQuery("SELECT G._id, title, date, substr(content,1,20) as summary FROM global G JOIN list L on G._id = L._id", null);
		//c = db.query("global", new String[]{"_id", "title", "content"}, null, null, null, null, null);	// all list
		c.moveToFirst();
		return c;
	}
	
	public Cursor getListTitle(long _id){
		Cursor c;
		c = db.query("global", new String[]{"title"}, "_id="+_id, null, null, null, null);
		c.moveToFirst();
		return c;
	}
	
	public Cursor getListDate(long _id){
		Cursor c;
		c = db.query("global", new String[]{"date"}, "_id="+_id, null, null, null, null);
		c.moveToFirst();
		return c;
	}

	public Cursor getListContent(long _id){
		Cursor c;
		c = db.query("list", new String[]{"content"}, "_id="+_id, null, null, null, null);
		c.moveToFirst();
		return c;
	}
	
	public long addList(String title){
		ContentValues cv = new ContentValues();
		cv.put("title", title);
		java.util.Date date = new java.util.Date();
		cv.put("date", date.getTime());
		long res = db.insertOrThrow("global", null, cv);
		db.execSQL("INSERT INTO list DEFAULT VALUES");
		return res;
	}
	
	public int addListContent(long _id, String content){
		ContentValues cv  = new ContentValues();
		cv.put("content", content);
		int res = db.update("list", cv, "_id="+_id, null);
		return res;
	}
	
	public int deleteList(long _id){
		int res = db.delete("global", "_id="+_id, null);
		return res;
	}
	
}
