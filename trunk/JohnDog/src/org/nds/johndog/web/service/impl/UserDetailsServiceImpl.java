package org.nds.johndog.web.service.impl;


import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nds.johndog.model.Rights;
import org.nds.johndog.model.User;
import org.nds.johndog.web.service.UserManager;
import org.nds.security.encoding.GenericPasswordEncoder;
import org.springframework.dao.DataAccessException;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.providers.dao.SaltSource;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.security.userdetails.UsernameNotFoundException;

/**
 * Implements a strategy to perform authentication.
 */
public class UserDetailsServiceImpl implements UserDetailsService {

	private final Log logger = LogFactory.getLog(UserDetailsServiceImpl.class);
	
	private UserManager userManager;
	
	private GenericPasswordEncoder passwordEncoder;
	
	private SaltSource saltSource;
	
	/**
     * setter to allows spring to inject userManager implementation
     * @param userManager : object (implementation of UserManager interface) to inject.
     */
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

    public final void setPasswordEncoder(GenericPasswordEncoder passwordEncoder) {
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
	 * @param login: user login
	 * @return an UserDetails object representing the authenticated user and his rights 
	 * if the authentication is successfull.
	 */
	public UserDetails loadUserByUsername(String login) {
		logger.info("Trying to Load the User with login: "+login+" and password [PROTECTED] from database and LDAP directory");
		try{
			logger.info("Searching the user with login: "+login+" in database");
			User user = userManager.getUser(login);

			if (user == null) {
				logger.error("User with login: "+login+" not found in database. Authentication failed for user "+login);
				throw new UsernameNotFoundException("user not found in database");
			}
			logger.info("user with login: "+login+" found in database");
			
			Set<Rights> rights = user.getRights();
			GrantedAuthority[] arrayAuths = new GrantedAuthority[rights.size()+1];
			int i=0;
			arrayAuths[i++]= new GrantedAuthorityImpl("ROLE_AUTH");
			
			for (Rights right : rights) {
				arrayAuths[i++]= new GrantedAuthorityImpl("ROLE_"+right.getLabel());
			}
			
			logger.debug("Create User for acegi features for User with login: "+login);
			org.springframework.security.userdetails.User acegiUser = new org.springframework.security.userdetails.User(login, user.getPassword(), true, true, true, true, arrayAuths);
			//org.springframework.security.userdetails.User encodedAcegiUser = new org.springframework.security.userdetails.User(acegiUser.getUsername(), encodePassword(acegiUser), acegiUser.isEnabled(), acegiUser.isAccountNonExpired(), acegiUser.isCredentialsNonExpired(), acegiUser.isAccountNonLocked(), acegiUser.getAuthorities());
			logger.info("user with login: "+login+" authenticated");
			
			return acegiUser;
			//return encodedAcegiUser;
		}
		catch (DataAccessException e){
			logger.error("Cannot retrieve Data from Database server : "+e.getMessage()+". Authentication failed for user "+login);
			throw new UsernameNotFoundException("user not found", e);
		}
	}
}