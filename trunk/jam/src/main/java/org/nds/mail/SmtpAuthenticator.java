package org.nds.mail;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

import org.nds.common.Util;

public class SmtpAuthenticator extends Authenticator {

	private final String username;
	private final String password;

	public SmtpAuthenticator(String propertiesPath) {
		this(Util.load(propertiesPath, MailConstants.DEFAULT_RESOURCE_PATH));
	}

	public SmtpAuthenticator(Properties props) {
		this(props.getProperty(MailConstants.MAIL_SMTP_USERNAME), props.getProperty(MailConstants.MAIL_SMTP_PASSWORD));
	}

	public SmtpAuthenticator(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@Override
	public PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(username, password);
	}
}