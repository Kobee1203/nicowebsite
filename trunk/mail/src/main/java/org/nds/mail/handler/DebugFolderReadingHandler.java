package org.nds.mail.handler;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;

public class DebugFolderReadingHandler implements FolderReadingHandler {

	@Override
	public void visit(Folder folder) throws MessagingException {
		onRead(folder);
	}

	@Override
	public void onRead(Folder folder) throws MessagingException {
		Folder folders[] = folder.list();
		for (int i = 0; i < folders.length; ++i) {
			System.out.println("f------- " + folders[i].getName());
			System.out.flush();
		}
		Message msgs[] = folder.getMessages();
		for (int i = 0; i < msgs.length; ++i) {
			Message message = msgs[i];
			StringBuffer flags = new StringBuffer("m");
			if (!message.isSet(Flags.Flag.SEEN)) {
				flags.append("U");
			} else {
				flags.append("-");
			}
			if (message.isSet(Flags.Flag.RECENT)) {
				flags.append("R");
			} else {
				flags.append("-");
			}
			if (message.isSet(Flags.Flag.DELETED)) {
				flags.append("D");
			} else {
				flags.append("-");
			}
			flags.append("---- ");

			System.out.println(flags.toString() + message.getMessageNumber() + ' ' + message.getSubject() + message.getLineCount());
			System.out.flush();
		}
	}

}
