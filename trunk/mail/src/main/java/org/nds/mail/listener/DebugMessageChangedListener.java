package org.nds.mail.listener;

import javax.mail.event.MessageChangedEvent;

public class DebugMessageChangedListener implements javax.mail.event.MessageChangedListener {

	@Override
	public void messageChanged(MessageChangedEvent e) {
		System.out.println("messageChanged : " + e);
		System.out.flush();
	}

}
