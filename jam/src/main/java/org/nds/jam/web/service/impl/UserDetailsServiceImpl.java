package org.nds.jam.web.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nds.jam.security.encoding.GenericPasswordEncoder;
import org.nds.jam.web.jpa.bean.Rights;
import org.nds.jam.web.jpa.bean.User;
import org.nds.jam.web.service.UserManager;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Implements a strategy to perform authentication.
 */
public class UserDetailsServiceImpl implements Serializable, UserDetailsService {

	private final Log logger = LogFactory.getLog(UserDetailsServiceImpl.class);

	private UserManager userManager;

	private GenericPasswordEncoder passwordEncoder;

	private SaltSource saltSource;

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void setPasswordEncoder(GenericPasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public final void setSaltSource(SaltSource saltSource) {
		this.saltSource = saltSource;
	}

	private String encodePassword(final UserDetails userDetails) {
		Object salt = null;

		if (this.saltSource != null) {
			salt = this.saltSource.getSalt(userDetails);
		}
		return passwordEncoder.encodePassword(userDetails.getPassword(), salt);
	}

	/**
	 * Get the user object corresponding to the given login, check the password stored
	 * into the secured session context and grant corresponding rights for the user.
	 * 
	 * @param username
	 *            : username
	 * @return an UserDetails object representing the authenticated user and his rights
	 *         if the authentication is successfull.
	 */
	public UserDetails loadUserByUsername(String username) {
		logger.info("Trying to Load the User with username: " + username + " and password [PROTECTED] from database and LDAP directory");
		try {
			logger.info("Searching the user with username: " + username + " in database");
			User user = userManager.getUser(username);

			if (user == null) {
				logger.error("User with login: " + username + " not found in database. Authentication failed for user " + username);
				throw new UsernameNotFoundException("user not found in database");
			}
			logger.info("user with login: " + username + " found in database");

			Set<Rights> rights = user.getRights();
			List<GrantedAuthority> arrayAuths = new ArrayList<GrantedAuthority>();

			for (Rights right : rights) {
				arrayAuths.add(new GrantedAuthorityImpl(right.getRole()));
			}

			logger.debug("Create User for acegi features for User with login: " + username);
			org.springframework.security.core.userdetails.User authUser = new org.springframework.security.core.userdetails.User(username, user
			        .getPassword(), true, true, true, true, arrayAuths);
			// org.springframework.security.userdetails.User encodedAuthUser = new
			// org.springframework.security.userdetails.User(authUser.getUsername(), encodePassword(authUser), authUser.isEnabled(),
			// authUser.isAccountNonExpired(), authUser.isCredentialsNonExpired(), authUser.isAccountNonLocked(), authUser.getAuthorities());
			logger.info("user with login: " + username + " authenticated");

			return authUser;
			// return encodedAuthUser;
		} catch (DataAccessException e) {
			logger.error("Cannot retrieve Data from Database server : " + e.getMessage() + ". Authentication failed for user " + username);
			throw new UsernameNotFoundException("user not found", e);
		}
	}
}