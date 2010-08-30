package org.nds.mail.listener;

import javax.mail.event.StoreEvent;

public class DebugStoreListener implements javax.mail.event.StoreListener {

	@Override
	public void notification(StoreEvent e) {
		if (e.getMessageType() == StoreEvent.ALERT) {
			System.out.println("StoreEvent ALART : " + e.getMessage());
		} else if (e.getMessageType() == StoreEvent.NOTICE) {
			System.out.println("StoreEvent NOTICE : " + e.getMessage());
		}
		System.out.flush();
	}

}
