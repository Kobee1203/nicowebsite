package org.nds.mail.listener;

import javax.mail.event.MessageCountEvent;

public class DebugMessageCountListener implements javax.mail.event.MessageCountListener {

	@Override
	public void messagesAdded(MessageCountEvent e) {
		System.out.println("messageAdded : " + e);
		System.out.flush();
	}

	@Override
	public void messagesRemoved(MessageCountEvent e) {
		System.out.println("messageRemoved : " + e);
		System.out.flush();
	}

}
