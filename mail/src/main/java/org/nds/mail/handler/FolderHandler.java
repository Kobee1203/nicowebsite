package org.nds.mail.handler;

import javax.mail.Folder;
import javax.mail.MessagingException;

public interface FolderHandler {

	public void visit(Folder folder) throws MessagingException;
}
