package org.nds.dbdroid.sqlite.test;


import java.io.IOException;
import java.io.InputStream;

import org.nds.dbdroid.DataBaseManager;
import org.nds.dbdroid.sqlite.helper.SQLiteDataBaseHelper;
import org.nds.dbdroid.sqlite.test.dao.TestDao;

import junit.framework.Assert;
import android.test.AndroidTestCase;
import android.util.Log;


public class SomeTest extends AndroidTestCase {

    public void testSomething() throws Throwable {
       Assert.assertTrue(1 + 1 == 2);
    }

    public void testSomethingElse() throws Throwable {
       Assert.assertTrue(1 + 1 == 3);
    }
    
    public void testContext() throws IOException {
    	/*String[] array = getContext().getAssets().list("/assets");
    	Log.d(getClass().getName(), array.toString());
    	InputStream is = getContext().getAssets().open("test");
    	Assert.assertNotNull(is);*/
    	
    	DataBaseManager dbHelper = new SQLiteDataBaseHelper(getContext(), "test", null, 1);
    	dbHelper.open();
    	
    	TestDao testDao = dbHelper.getDAO(TestDao.class);
    	
    	testDao.findAll();
    	
    	dbHelper.close();
    }
}
