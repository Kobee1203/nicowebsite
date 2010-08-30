package org.nds.mail;

import java.util.Properties;

import org.springframework.mail.javamail.JavaMailSenderImpl;

public class MailSender extends JavaMailSenderImpl {

	public MailSender(Properties props) {
		String defaultEncoding = props.getProperty(MailConstants.MAIL_SMTP_DEFAULTENCODING);
		if (defaultEncoding != null) {
			setDefaultEncoding(defaultEncoding);
		}
		String host = props.getProperty(MailConstants.MAIL_SMTP_HOST);
		if (host != null) {
			setHost(host);
		}
		String port = props.getProperty(MailConstants.MAIL_SMTP_PORT);
		if (port != null) {
			setPort(Integer.valueOf(port));
		}
		String protocol = props.getProperty(MailConstants.MAIL_SMTP_PROTOCOL);
		if (protocol != null) {
			setProtocol(protocol);
		}
	}
}
