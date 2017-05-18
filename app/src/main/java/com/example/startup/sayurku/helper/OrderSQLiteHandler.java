/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 * */
package com.example.startup.sayurku.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ImageView;

import com.example.startup.sayurku.persistence.Item;
import com.example.startup.sayurku.persistence.User;

import java.util.ArrayList;
import java.util.List;


public class OrderSQLiteHandler extends SQLiteOpenHelper {

	private static final String TAG = OrderSQLiteHandler.class.getSimpleName();

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "order_sayurku";

	// Login table name
	private static final String TABLE_NAME = "ORDER_TABLE";

	// Login Table Columns names
	private static final String KEY_ID = "name";
	private static final String KEY_1 = "price";
	private static final String KEY_2 = "description";
	private static final String KEY_3 = "metric";
	private static final String KEY_4 = "photo";

	public OrderSQLiteHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
				+ KEY_ID + " TEXT UNIQUE," + KEY_1 + " INTEGER,"
				+ KEY_2 + " TEXT,"+ KEY_3 + " TEXT," + KEY_4 + " TEXT" + ")";
		db.execSQL(CREATE_TABLE);

		Log.d(TAG, "Database tables created");
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

		// Create tables again
		onCreate(db);
	}

	/**
	 * Storing user details in database
	 * */
	public boolean addItem(Item item) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ID,item.name);
		values.put(KEY_1, item.price); // Name
		values.put(KEY_2, item.description); // Email
		values.put(KEY_3, item.metric); // Phone
		values.put(KEY_4, item.photo); // Phone
		// Inserting Row
		String Query = ("Select * from " + TABLE_NAME + " where " + KEY_ID + "=?") ;
		Cursor cursor = db.rawQuery(Query, new String[]{item.name});
		if(cursor.getCount() <= 0){
			cursor.close();
			long id = db.insert(TABLE_NAME, null, values);
			db.close(); // Closing database connection
			return true;

		}
		else
		{
			cursor.close();
			long id = db.insert(TABLE_NAME, null, values);
			db.close(); // Closing database connection
			return false;
		}




	}


	public void updateItem(Item item) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ID,item.name);
		values.put(KEY_1, item.price); // Name
		values.put(KEY_2, item.description); // Email
		values.put(KEY_3, item.metric); // Phone
		values.put(KEY_4, item.photo); // Phone

		// Inserting Row
		long id = db.update(TABLE_NAME, values, KEY_ID+"="+item.name,null);
		db.close(); // Closing database connection

		Log.d(TAG, "New user inserted into sqlite: " + id);
	}

	/**
	 * Getting user data from database
	 * */
	public List<Item> getItem() {
		List<Item> items = new ArrayList<>();
		String selectQuery = "SELECT  * FROM " + TABLE_NAME;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);


		if (cursor .moveToFirst()) {
			while (cursor.isAfterLast() == false) {
				Item item = new Item();
				item.name =cursor.getString(cursor.getColumnIndex(KEY_ID));
				item.price=cursor.getInt(cursor.getColumnIndex(KEY_1));
				item.description =cursor.getString(cursor.getColumnIndex(KEY_2));
				item.metric=cursor.getString(cursor.getColumnIndex(KEY_3));
				item.photo=cursor.getString(cursor.getColumnIndex(KEY_4));

				items.add(item);
				cursor.moveToNext();
			}
		}
		cursor.close();
		db.close();
		// return user
		Log.d(TAG, "Fetching user from Sqlite: " + items.toString());

		return items;
	}

	/**
	 * Re crate database Delete all tables and create them again
	 * */
	public void deleteItem() {
		SQLiteDatabase db = this.getWritableDatabase();
		// Delete All Rows
		db.delete(TABLE_NAME, null, null);
		db.close();

		Log.d(TAG, "Deleted all user info from sqlite");
	}

}
