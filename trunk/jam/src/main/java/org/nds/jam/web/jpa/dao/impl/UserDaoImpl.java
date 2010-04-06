package org.nds.jam.web.jpa.dao.impl;

import org.nds.jam.web.jpa.bean.User;
import org.nds.jam.web.jpa.dao.UserDao;
import org.nds.jam.web.jpa.dao.generic.GenericHibernateDAO;

public class UserDaoImpl extends GenericHibernateDAO<User, Long> implements UserDao {

}
