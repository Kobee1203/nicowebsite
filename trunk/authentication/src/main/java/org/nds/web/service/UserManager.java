package org.nds.web.service;

import org.nds.model.Rights;
import org.nds.model.User;

public interface UserManager {

	User getUser(String login);
	User saveUser(User user);
	void deleteUser(String login);
	
	Rights saveRights(Rights auth);
}
