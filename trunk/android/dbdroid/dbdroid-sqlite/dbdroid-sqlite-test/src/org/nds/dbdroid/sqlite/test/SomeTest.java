package org.nds.dbdroid.sqlite.test;

import java.io.IOException;
import java.io.InputStream;

import junit.framework.Assert;

import org.nds.dbdroid.DataBaseManager;
import org.nds.dbdroid.exception.DBDroidException;
import org.nds.dbdroid.log.Logger;
import org.nds.dbdroid.sqlite.SQLiteDataBaseManager;
import org.nds.dbdroid.sqlite.dao.Object1Dao;
import org.nds.dbdroid.sqlite.test.dao.TestDao;
import org.nds.package_info.ClassPathPackageInfo;
import org.nds.package_info.ClassPathPackageInfoSource;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.test.AndroidTestCase;

public class SomeTest extends AndroidTestCase {

    private static final Logger log = Logger.getLogger(SomeTest.class);

    public void testSomething() throws Throwable {
        Assert.assertTrue(1 + 1 == 2);
    }

    public void testSomethingElse() throws Throwable {
        Assert.assertTrue(1 + 1 == 3);
    }

    public void testPackageInfo() throws NameNotFoundException, ClassNotFoundException {
        // ask for the code of the foreign context to be included and to ignore any security given by the cross-process(owner) execution
        // in working-environment to error checking ...
        Context tmpCtxt = getContext().createPackageContext("org.nds.dbdroid.sqlite", Context.CONTEXT_INCLUDE_CODE + Context.CONTEXT_IGNORE_SECURITY);

        Class<?> c0 = tmpCtxt.getClassLoader().loadClass("org.nds.dbdroid.sqlite.DbDroidSQLite");
        Class<?> c = tmpCtxt.getClassLoader().loadClass("org.nds.dbdroid.sqlite.test.dao.TestDao");
        // do normal Java-Reflection things with c

        // Apk paths used to search for test classes when using
        // TestSuiteBuilders.
        getContext().getPackageManager().getSystemSharedLibraryNames();
        getContext().fileList();
        getContext().getDir(".", Context.MODE_WORLD_READABLE);
        getContext().getApplicationContext().getPackageManager().getSystemSharedLibraryNames();
        getContext().getApplicationContext().fileList();
        getContext().getApplicationContext().getDir(".", Context.MODE_WORLD_READABLE);
        String[] apkPaths = { getContext().getPackageName(), getContext().getApplicationContext().getPackageName(),
                getContext().getCacheDir().getAbsolutePath(), getContext().getFilesDir().getAbsolutePath(),
                getContext().getApplicationContext().getCacheDir().getAbsolutePath(),
                getContext().getApplicationContext().getFilesDir().getAbsolutePath() };
        ClassPathPackageInfoSource.setApkPaths(apkPaths);

        ClassPathPackageInfoSource classPathSource = new ClassPathPackageInfoSource();
        // classPathSource.setClassLoader(tmpCtxt.getClassLoader());

        ClassPathPackageInfo cppi = classPathSource.getPackageInfo("org.nds.dbdroid.sqlite.test.dao");
        System.out.println(cppi.getPackageName() + ": " + cppi.getTopLevelClassesRecursive());

        for (ClassPathPackageInfo packageInfo : cppi.getSubpackages()) {
            System.out.println(packageInfo.getPackageName() + ": " + packageInfo.getTopLevelClassesRecursive());
        }
    }

    public void testContext() throws IOException, DBDroidException, NameNotFoundException {
        // ask for the code of the foreign context to be included and to ignore any security given by the cross-process(owner) execution
        // in working-environment to error checking ...
        Context ctx = getContext().createPackageContext("org.nds.dbdroid.sqlite", Context.CONTEXT_INCLUDE_CODE + Context.CONTEXT_IGNORE_SECURITY);

        InputStream config = getContext().getAssets().open("dbdroid/dbdroid.xml");

        DataBaseManager dbManager = new SQLiteDataBaseManager(config, getContext(), "dbdroid/test-sqlite.db", null, 1);
        dbManager.setClassLoader(ctx.getClassLoader());
        dbManager.open();

        Object1Dao object1Dao = dbManager.getDAO(Object1Dao.class);
        object1Dao.findAll();

        TestDao testDao = dbManager.getDAO(TestDao.class);
        testDao.findAll();

        dbManager.close();
    }
}