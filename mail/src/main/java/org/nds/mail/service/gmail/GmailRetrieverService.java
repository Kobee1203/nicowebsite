package org.nds.mail.service.gmail;

import java.util.Properties;

import javax.mail.URLName;

import org.nds.mail.service.MailRetrieverService;
import org.nds.mail.service.gmail.GmailHelper.GmailProtocol;

public class GmailRetrieverService extends MailRetrieverService {

	public GmailRetrieverService(String username, String password, GmailProtocol protocol, String defaultFolderName) {
		super(new URLName(protocol.getProtocol(), protocol.getHost(), protocol.getPort(), "", username, password), defaultFolderName);

		Properties props = new Properties();

		switch (protocol) {
			case POP3:
				props.put("mail.pop3.socketFactory.class", GmailHelper.SSL_FACTORY);
				props.put("mail.pop3.socketFactory.fallback", "false");
				props.put("mail.pop3.starttls.enable", "true");
				props.put("mail.pop3.socketFactory.port", "995");
				break;
			case IMAP:
				props.put("mail.imap.socketFactory.class", GmailHelper.SSL_FACTORY);
				props.put("mail.iamp.socketFactory.fallback", "false");
				props.put("mail.imap.starttls.enable", "true");
				props.put("mail.imap.socketFactory.port", "993");
				break;
			default:
				break;
		}

		setSessionProperties(props);
	}

	/*
	 * Inbox
	 * [Gmail]/All Mail – This folder contains all of your Gmail messages.
	 * [Gmail]/Drafts – Your drafts.
	 * [Gmail]/Sent Mail – Messages you sent to other people.
	 * [Gmail]/Spam – Messages marked as spam.
	 * [Gmail]/Starred – Starred messages.
	 * [Gmail]/Trash – Messages deleted from Gmail.
	 * Each of your custom labels also becomes an IMAP folder.
	 * Messages that have more than one labels will be in multiple IMAP folders.
	 * Your email client may create its own special folders such as "Drafts", "Sent Items", "Deleted Items".
	 * These folders only have special meaning to your email client and not Gmail.
	 * These special folders can be seen in the web Gmail client as "[Imap]/Drafts", "[Imap]/Deleted Items".
	 * To Gmail, these are just additional labels with no special meaning.
	 */
}
