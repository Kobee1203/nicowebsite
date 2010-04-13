package org.nds.jam.web.service.impl;

import java.util.List;

import org.nds.jam.web.jpa.bean.Rights;
import org.nds.jam.web.jpa.bean.User;
import org.nds.jam.web.jpa.dao.RightsDao;
import org.nds.jam.web.jpa.dao.UserDao;
import org.nds.jam.web.service.UserManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContextException;

public class UserManagerImpl implements UserManager, InitializingBean {

	UserDao userDao;
	RightsDao rightsDao;

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void setRightsDao(RightsDao rightsDao) {
		this.rightsDao = rightsDao;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// check beans
		if (userDao == null) {
			throw new ApplicationContextException("Must set userDao bean property on " + getClass());
		}
		if (rightsDao == null) {
			throw new ApplicationContextException("Must set rightsDao bean property on " + getClass());
		}

		// init data
	}

	public void deleteUser(String login) {
		userDao.makeTransient(getUser(login));
	}

	public User getUser(Long id) {
		User user = userDao.findById(id, false);
		return user;
	}

	public User getUser(String username) {
		List<User> users = userDao.findByExample(new User(username, null));
		if (users != null && users.size() > 0) {
			return users.get(0);
		}
		return null;
	}

	public List<User> getUsers() {
		return userDao.findAll();
	}

	public User saveUser(User user) {
		return userDao.makePersistent(user);
	}

	public Rights saveRights(Rights auth) {
		return rightsDao.makePersistent(auth);
	}

}
