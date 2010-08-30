package org.nds.dbdroid.test;

import org.nds.dbdroid.helper.DataBaseHelper;
import org.nds.dbdroid.helper.SQLiteDataBaseHelper;

import android.test.AndroidTestCase;
import android.test.mock.MockContext;

public class DbDroidTest extends AndroidTestCase {

	public void test() {
		MockContext context = new MockContext();
		DataBaseHelper dbHelper = new SQLiteDataBaseHelper(context, "test", null, 1);
		dbHelper.open();
		
		dbHelper.close();
	}
}
