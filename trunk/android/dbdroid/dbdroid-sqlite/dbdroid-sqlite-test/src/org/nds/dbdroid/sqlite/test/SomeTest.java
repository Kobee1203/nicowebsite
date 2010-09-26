package org.nds.dbdroid.sqlite.test;


import java.io.IOException;

import junit.framework.Assert;

import org.nds.dbdroid.DataBaseManager;
import org.nds.dbdroid.exception.DBDroidException;
import org.nds.dbdroid.log.Logger;
import org.nds.dbdroid.sqlite.SQLiteDataBaseManager;
import org.nds.dbdroid.sqlite.test.dao.TestDao;

import android.test.AndroidTestCase;


public class SomeTest extends AndroidTestCase {

	private static final Logger log = Logger.getLogger(SomeTest.class);
	
    public void testSomething() throws Throwable {
       Assert.assertTrue(1 + 1 == 2);
    }

    public void testSomethingElse() throws Throwable {
       Assert.assertTrue(1 + 1 == 3);
    }
    
    public void testContext() throws IOException, DBDroidException {	
    	log.debug("dbdroid folder: " + getContext().getAssets().list("dbdroid").toString());
    	
    	DataBaseManager dbManager = new SQLiteDataBaseManager(getContext().getAssets().open("dbdroid/dbdroid.xml"), getContext(), "dbdroid/test-sqlite.db", null, 1);
    	dbManager.open();
    	
    	TestDao testDao = dbManager.getDAO(TestDao.class);
    	
    	testDao.findAll();
    	
    	dbManager.close();
    }
}
