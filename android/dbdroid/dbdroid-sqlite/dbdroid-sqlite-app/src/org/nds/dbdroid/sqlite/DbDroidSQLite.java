package org.nds.dbdroid.sqlite;

import java.io.IOException;

import org.nds.dbdroid.DataBaseManager;
import org.nds.dbdroid.exception.DBDroidException;

import android.app.Activity;
import android.os.Bundle;

public class DbDroidSQLite extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        try {
			DataBaseManager dbManager = new SQLiteDataBaseManager(getAssets().open("dbdroid/dbdroid.xml"), getApplicationContext(), "dbdroid/test-sqlite.db", null, 1);
		} catch (DBDroidException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}