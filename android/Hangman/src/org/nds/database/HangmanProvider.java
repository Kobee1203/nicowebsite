package org.nds.database;

import org.nds.dbdroid.helper.DataBaseHelper;
import org.nds.dbdroid.helper.SQLiteDataBaseHelper;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class HangmanProvider extends ContentProvider {
	
	private final String TAG = getClass().getName();
	
	public final Uri CONTENT_URI = Uri.parse("content://"+getClass().getCanonicalName().toLowerCase());
	
	// Declare DAOs
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// Call DAO according to uri
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCreate() {
		Context context = getContext();
		DataBaseHelper dbHelper = new SQLiteDataBaseHelper(context, "hangman", null, 1);
		dbHelper.open();
		Log.d(TAG, "database initialized.");
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		Log.d(TAG, "QUERY!");
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		Log.d(TAG, "UPDATE!");
		return 0;
	}

}
