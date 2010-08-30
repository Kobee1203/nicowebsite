package org.nds.mail.service.gmail;

import org.apache.commons.mail.EmailException;
import org.nds.mail.service.MailSenderService;

public class GmailSenderService extends MailSenderService {

	public GmailSenderService(String username, String password) throws EmailException {
		super(username.contains("@") ? username : username + "@gmail.com", username, password, 587, "smtp.gmail.com");
		getEmail().setTLS(true);
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
