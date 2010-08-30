package org.nds.mail.listener;

import javax.mail.event.TransportEvent;

public class DebugTransportListener implements javax.mail.event.TransportListener {

	@Override
	public void messageDelivered(TransportEvent e) {
		System.out.println("messageDelivered : " + e);
		System.out.flush();
	}

	@Override
	public void messageNotDelivered(TransportEvent e) {
		System.out.println("messageNotDelivered : " + e);
		System.out.flush();
	}

	@Override
	public void messagePartiallyDelivered(TransportEvent e) {
		System.out.println("messagePartiallyDelivered : " + e);
		System.out.flush();
	}

}
