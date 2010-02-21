package org.nds.johndog.web.service.impl;

import java.util.List;

import org.nds.johndog.hibernate.dao.RightsDAO;
import org.nds.johndog.hibernate.dao.UserDAO;
import org.nds.johndog.model.Rights;
import org.nds.johndog.model.User;
import org.nds.johndog.web.service.UserManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContextException;

public class UserManagerImpl implements UserManager, InitializingBean {

	UserDAO userDAO;
	RightsDAO rightsDAO;

	@Override
	public void afterPropertiesSet() throws Exception {
		// check beans
		if (userDAO == null) throw new ApplicationContextException("Must set userDAO bean property on " + getClass());
		if (rightsDAO == null) throw new ApplicationContextException("Must set rightsDAO bean property on " + getClass());

		// init data
	}

	/** Getters / setters **/

	public void setRightsDAO(RightsDAO rightsDAO) {
		this.rightsDAO = rightsDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	
	/** End Getters / setters **/
	
	public void deleteUser(String login) {
		userDAO.makeTransient(getUser(login));
	}

	public User getUser(String login) {
		List<User> users = userDAO.findByExample(new User(login, null));
		if(users!=null && users.size()>0) {
			return users.get(0);
		}
		return null;
	}

	public User saveUser(User user) {
		return userDAO.makePersistent(user);
	}
	
	public Rights saveRights(Rights auth) {
		return rightsDAO.makePersistent(auth);
	}

}
