package org.nds.mail.handler;

import javax.mail.Folder;
import javax.mail.MessagingException;

public interface FolderReadingHandler extends FolderHandler {
	public void onRead(Folder folder) throws MessagingException;
}
