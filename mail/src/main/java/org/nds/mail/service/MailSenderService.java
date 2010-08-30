package org.nds.mail.service;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

public class MailSenderService {

	private final Email email;

	public MailSenderService(String fromMail, String username, String password, int port, String hostName) throws EmailException {
		email = new HtmlEmail();
		email.setSmtpPort(port);
		email.setAuthenticator(new DefaultAuthenticator(username, password));
		email.setDebug(false);
		email.setHostName(hostName);
		email.setFrom(fromMail);
	}

	// Send mail
	public void sendMail() throws EmailException {
		email.send();
	}

	public Email getEmail() {
		return email;
	}
}
