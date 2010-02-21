package org.nds.johndog.hibernate.dao.impl;

import org.nds.johndog.hibernate.dao.UserDAO;
import org.nds.johndog.hibernate.dao.generic.GenericHibernateDAO;
import org.nds.johndog.model.User;


public class UserDAOImpl extends GenericHibernateDAO<User, Long> implements UserDAO {

}
