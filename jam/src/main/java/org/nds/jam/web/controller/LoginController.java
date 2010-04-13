package org.nds.jam.web.controller;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nds.jam.web.jpa.bean.User;
import org.nds.jam.web.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {

	protected final static Log log = LogFactory.getLog(LoginController.class);

	@Autowired
	UserManager userManager;

	public void afterPropertiesSet() throws Exception {
		if (userManager == null) {
			throw new ApplicationContextException("Must set userManager bean property on " + getClass());
		}
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public void getUsers() {
		List<User> users = userManager.getUsers();
		if (users != null) {
			for (User user : users) {
				log.debug("id=" + user.getId() + ", username=" + user.getUsername() + ", password=" + user.getPassword() + ", enabled="
				        + user.isEnabled() + ", rights=" + user.getRights());
			}
		}

	}
}
