package org.nds.jam.security.encoding;

import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.encoding.PasswordEncoder;

public class GenericPasswordEncoder implements PasswordEncoder {

	PasswordEncoder passwordEncoder;

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public String encodePassword(String rawPass, Object salt) throws DataAccessException {
		return this.passwordEncoder.encodePassword(rawPass, salt);
	}

	@Override
	public boolean isPasswordValid(String encPass, String rawPass, Object salt) throws DataAccessException {
		return this.passwordEncoder.isPasswordValid(encPass, rawPass, salt);
	}

}
