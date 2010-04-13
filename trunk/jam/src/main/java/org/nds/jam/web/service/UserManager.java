package org.nds.jam.web.service;

import java.util.List;

import org.nds.jam.web.jpa.bean.Rights;
import org.nds.jam.web.jpa.bean.User;

public interface UserManager {

	User getUser(Long id);

	User getUser(String username);

	List<User> getUsers();

	User saveUser(User user);

	void deleteUser(String login);

	Rights saveRights(Rights auth);
}
