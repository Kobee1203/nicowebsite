package org.nds.dbdroid.sqlite.test;


import java.io.IOException;
import java.io.InputStream;

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
    	String[] array = getContext().getAssets().list("/assets");
    	Log.d(getClass().getName(), array.toString());
    	InputStream is = getContext().getAssets().open("test");
    	Assert.assertNotNull(is);
    }
}
