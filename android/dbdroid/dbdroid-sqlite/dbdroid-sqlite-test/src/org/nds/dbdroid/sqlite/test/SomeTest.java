package org.nds.dbdroid.sqlite.test;


import java.io.IOException;

import junit.framework.Assert;

import org.nds.dbdroid.DataBaseManager;
import org.nds.dbdroid.exception.DBDroidException;
import org.nds.dbdroid.sqlite.SQLiteDataBaseManager;
import org.nds.dbdroid.sqlite.test.dao.TestDao;

import android.test.AndroidTestCase;


public class SomeTest extends AndroidTestCase {

    public void testSomething() throws Throwable {
       Assert.assertTrue(1 + 1 == 2);
    }

    public void testSomethingElse() throws Throwable {
       Assert.assertTrue(1 + 1 == 3);
    }
    
    public void testContext() throws IOException, DBDroidException {
    	/*String[] array = getContext().getAssets().list("/assets");
    	Log.d(getClass().getName(), array.toString());
    	InputStream is = getContext().getAssets().open("test");
    	Assert.assertNotNull(is);*/
    	
    	DataBaseManager dbManager = new SQLiteDataBaseManager(getContext().getAssets().open("dbdroid/dbdroid.xml"), getContext(), "dbdroid/test-sqlite.db", null, 1);
    	dbManager.open();
    	
    	TestDao testDao = dbManager.getDAO(TestDao.class);
    	
    	testDao.findAll();
    	
    	dbManager.close();
    }
}
