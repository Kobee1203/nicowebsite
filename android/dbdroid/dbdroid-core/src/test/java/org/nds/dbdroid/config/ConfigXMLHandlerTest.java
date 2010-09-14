package org.nds.dbdroid.config;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.nds.dbdroid.DataBaseManager;
import org.nds.dbdroid.exception.DBDroidException;
import org.nds.dbdroid.mock.MockDataBaseManager;

public class ConfigXMLHandlerTest extends TestCase {

	public void testConfigParsing() {
		try {
			DataBaseManager dbManager = new MockDataBaseManager(getClass().getResourceAsStream("dbdroid.xml"));
		} catch (DBDroidException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
}
