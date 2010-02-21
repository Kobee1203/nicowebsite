package org.nds.johndog.web.formbean;

import java.io.Serializable;

public class RegisterForm implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String login;
	private String password;
	private String confirmPassword;
	private String email;
	
	public RegisterForm() {	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
