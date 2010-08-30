package org.nds.mail.service;

import org.nds.mail.handler.FolderReadingHandler;

public class MailRetrieverThread extends Thread {

	private MailRetrieverService service;

	private Boolean stop = Boolean.FALSE;

	// suspend current folder reading execution for a specified period
	private final long suspendTime = 5000;

	private FolderReadingHandler folderReadingHandler;

	public MailRetrieverThread(MailRetrieverService service) {
		super("Mail Retriever Service Thread");
		this.service = service;
	}

	public void setMailRetrieverService(MailRetrieverService service) {
		this.service = service;
	}

	public void setFolderReadingHandler(FolderReadingHandler handler) {
		this.folderReadingHandler = handler;
	}

	@Override
	public void run() {
		try {
			this.service.open();

			this.stop = Boolean.FALSE;
			while (!stop) {

				// Read from the handler
				this.service.accept(folderReadingHandler);

				Thread.sleep(suspendTime);
			}

			this.service.close();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void stopThread() {
		synchronized (stop) {
			this.stop = Boolean.TRUE;
		}
	}
}
