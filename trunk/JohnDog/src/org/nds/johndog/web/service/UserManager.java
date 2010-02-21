package org.nds.johndog.web.service;

import org.nds.johndog.model.Rights;
import org.nds.johndog.model.User;

public interface UserManager {

	User getUser(String login);
	User saveUser(User user);
	void deleteUser(String login);
	
	Rights saveRights(Rights auth);
}
