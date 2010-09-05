package org.nds.dbdroid.dao;

import org.nds.dbdroid.dao.AndroidDAO;
import org.nds.dbdroid.helper.DataBaseHelper;
import org.nds.dbdroid.entity.Test;

public class TestDao extends AndroidDAO<Test> {

	public TestDao(DataBaseHelper dbHelper) {
		super(dbHelper);
	}

}
