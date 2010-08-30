package org.nds.mail.listener;

import javax.mail.event.ConnectionEvent;

public class DebugConnectionListener implements javax.mail.event.ConnectionListener {

	@Override
	public void closed(ConnectionEvent e) {
		System.out.println("Connection closed : " + e);
		System.out.flush();
	}

	@Override
	public void disconnected(ConnectionEvent e) {
		System.out.println("Connection disconnected : " + e);
		System.out.flush();
	}

	@Override
	public void opened(ConnectionEvent e) {
		System.out.println("Connection opened : " + e);
		System.out.flush();
	}

}
