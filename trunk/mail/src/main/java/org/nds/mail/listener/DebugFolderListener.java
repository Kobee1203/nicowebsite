package org.nds.mail.listener;

import javax.mail.event.FolderEvent;

public class DebugFolderListener implements javax.mail.event.FolderListener {

	@Override
	public void folderCreated(FolderEvent e) {
		System.out.println("folderCreated : " + e);
		System.out.flush();
	}

	@Override
	public void folderDeleted(FolderEvent e) {
		System.out.println("folderDeleted : " + e);
		System.out.flush();
	}

	@Override
	public void folderRenamed(FolderEvent e) {
		System.out.println("folderRenamed : " + e);
		System.out.flush();
	}

}
