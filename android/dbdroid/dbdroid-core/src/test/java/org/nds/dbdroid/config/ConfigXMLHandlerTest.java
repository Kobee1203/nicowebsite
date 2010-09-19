package org.nds.dbdroid.config;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.nds.dbdroid.DataBaseManager;
import org.nds.dbdroid.dao.Dao1;
import org.nds.dbdroid.entity.Entity2;
import org.nds.dbdroid.exception.DBDroidException;
import org.nds.dbdroid.mock.MockDataBaseManager;

public class ConfigXMLHandlerTest extends TestCase {

	public void testConfigParsing() {
		try {
			DataBaseManager dbManager = new MockDataBaseManager(getClass().getResourceAsStream("dbdroid.xml"));
			dbManager.open();
			Dao1 dao1 = dbManager.getDAO(Dao1.class);
			Assert.assertNotNull(dao1);
			dao1.findById("2");
			dao1.findAll();
			dbManager.findById("10", Entity2.class);
			dbManager.findAll(Entity2.class);
			dbManager.close();
		} catch (DBDroidException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
}
