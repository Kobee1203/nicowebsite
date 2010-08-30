package org.nds.mail.service;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.FolderNotFoundException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.event.ConnectionListener;
import javax.mail.event.FolderListener;
import javax.mail.event.MessageChangedListener;
import javax.mail.event.MessageCountListener;
import javax.mail.event.StoreListener;
import javax.mail.search.SearchTerm;

import org.nds.mail.handler.FolderHandler;
import org.nds.mail.renderable.Renderable;
import org.nds.mail.renderable.RenderableMessage;

public class MailRetrieverService {

	private static final String DEFAULT_FOLDER_NAME = "INBOX";

	private final URLName urlName;
	private final String defaultFolderName;

	private Store store;

	private Authenticator authenticator;
	private Properties sessionProperties;

	private Folder currentFolder;

	private final List<ConnectionListener> storeConnectionListeners = new ArrayList<ConnectionListener>();
	private final List<FolderListener> storeFolderListeners = new ArrayList<FolderListener>();
	private final List<StoreListener> storeStoreListeners = new ArrayList<StoreListener>();

	private final List<ConnectionListener> folderConnectionListeners = new ArrayList<ConnectionListener>();
	private final List<FolderListener> folderFolderListeners = new ArrayList<FolderListener>();
	private final List<MessageChangedListener> folderMessageChangedListeners = new ArrayList<MessageChangedListener>();
	private final List<MessageCountListener> folderMessageCountListeners = new ArrayList<MessageCountListener>();

	private boolean debug = false;
	private PrintStream debugOutput;

	public MailRetrieverService(URLName urlName) {
		this(urlName, null);
	}

	public MailRetrieverService(URLName urlName, String defaultFolderName) {
		this.urlName = urlName;
		this.defaultFolderName = defaultFolderName;
	}

	/**
	 * Open the connection to the mailbox
	 */
	public void open() {
		Properties props = System.getProperties();
		props.putAll(sessionProperties);

		Session session = Session.getDefaultInstance(props, authenticator);
		session.setDebug(debug);
		session.setDebugOut(debugOutput);

		try {
			store = session.getStore(urlName);
			addListeners(store);
			store.connect();

			// Open the Folder
			String folderName = DEFAULT_FOLDER_NAME;
			if (defaultFolderName != null) {
				folderName = defaultFolderName;
			}
			currentFolder = store.getDefaultFolder();
			currentFolder = currentFolder.getFolder(folderName);

			if (currentFolder == null) {
				throw new FolderNotFoundException(currentFolder, "No default folder");
			}

			addListeners(currentFolder);

			if (!currentFolder.exists()) {
				System.out.println("The folder " + getCurrentFolderPath() + " does not exist.");
				currentFolder.create(Folder.READ_WRITE | Folder.HOLDS_FOLDERS | Folder.HOLDS_MESSAGES);
			}

			// try to open read/write and if that fails try read-only
			try {
				currentFolder.open(Folder.READ_WRITE);
			} catch (MessagingException ex) {
				currentFolder.open(Folder.READ_ONLY);
			}
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Close the connection to the mailbox
	 */
	public void close() {
		try {
			// Close current folder and remove listeners
			currentFolder.close(false);
			removeListeners(currentFolder);

			// Close store and remove listeners
			store.close();
			removeListeners(store);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Set the debug setting and set the stream to be used for debugging output for this session.
	 * If out is null, System.out will be used.
	 * 
	 * @param debug
	 *            Debug setting
	 * @param out
	 *            The PrintStream to use for debugging output
	 */
	public void setDebug(boolean debug, PrintStream out) {
		this.debug = debug;
		this.debugOutput = out;
	}

	public void setSessionProperties(Properties sessionProperties) {
		this.sessionProperties = sessionProperties;
	}

	/**
	 * Add a listener for Connection events on the current Store.
	 * 
	 * @param listener
	 */
	public void addStoreListener(ConnectionListener listener) {
		storeConnectionListeners.add(listener);
	}

	/**
	 * Add a listener for Folder events on any Folder object obtained from the current Store.
	 * 
	 * @param listener
	 */
	public void addStoreListener(FolderListener listener) {
		storeFolderListeners.add(listener);
	}

	/**
	 * Add a listener for StoreEvents on the current Store.
	 * 
	 * @param listener
	 */
	public void addStoreListener(StoreListener listener) {
		storeStoreListeners.add(listener);
	}

	/**
	 * Add a listener for Connection events on current Folder.
	 * 
	 * @param listener
	 */
	public void addFolderListener(ConnectionListener listener) {
		folderConnectionListeners.add(listener);
	}

	/**
	 * Add a listener for Folder events on the current Folder.
	 * 
	 * @param listener
	 */
	public void addFolderListener(FolderListener listener) {
		folderFolderListeners.add(listener);
	}

	/**
	 * Add a listener for MessageChanged events on the current Folder.
	 * 
	 * @param listener
	 */
	public void addFolderListener(MessageChangedListener listener) {
		folderMessageChangedListeners.add(listener);
	}

	/**
	 * Add a listener for MessageCount events on the current Folder.
	 * 
	 * @param listener
	 */
	public void addFolderListener(MessageCountListener listener) {
		folderMessageCountListeners.add(listener);
	}

	/**
	 * method used in the Visitor Pattern. Visit the current folder.
	 * 
	 * @param handler
	 *            is the visitor in the Visitor Pattern
	 */
	public void accept(FolderHandler handler) {
		try {
			if (handler != null) {
				handler.visit(currentFolder);
			}
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	protected void addListeners(Store store) {
		for (ConnectionListener listener : storeConnectionListeners) {
			store.addConnectionListener(listener);
		}
		for (FolderListener listener : storeFolderListeners) {
			store.addFolderListener(listener);
		}
		for (StoreListener listener : storeStoreListeners) {
			store.addStoreListener(listener);
		}
	}

	protected void removeListeners(Store store) {
		for (ConnectionListener listener : storeConnectionListeners) {
			store.removeConnectionListener(listener);
		}
		for (FolderListener listener : storeFolderListeners) {
			store.removeFolderListener(listener);
		}
		for (StoreListener listener : storeStoreListeners) {
			store.removeStoreListener(listener);
		}
	}

	protected void addListeners(Folder folder) {
		for (ConnectionListener listener : folderConnectionListeners) {
			folder.addConnectionListener(listener);
		}
		for (FolderListener listener : folderFolderListeners) {
			folder.addFolderListener(listener);
		}
		for (MessageCountListener listener : folderMessageCountListeners) {
			folder.addMessageCountListener(listener);
		}
		for (MessageChangedListener listener : folderMessageChangedListeners) {
			folder.addMessageChangedListener(listener);
		}
	}

	protected void removeListeners(Folder folder) {
		for (ConnectionListener listener : folderConnectionListeners) {
			folder.removeConnectionListener(listener);
		}
		for (FolderListener listener : folderFolderListeners) {
			folder.removeFolderListener(listener);
		}
		for (MessageCountListener listener : folderMessageCountListeners) {
			folder.removeMessageCountListener(listener);
		}
		for (MessageChangedListener listener : folderMessageChangedListeners) {
			folder.removeMessageChangedListener(listener);
		}
	}

	/**
	 * Display the messages/sub-folders from the current current folder
	 */
	public void displayCurrentFolderContent() {
		System.out.println("__________________________");
		System.out.flush();
		try {
			Folder folders[] = currentFolder.list();
			for (int i = 0; i < folders.length; ++i) {
				System.out.println("f------- " + folders[i].getName());
				System.out.flush();
			}
			Message msgs[] = currentFolder.getMessages();
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
				flags.append("----");

				System.out.println(flags.toString() + ' ' + message.getMessageNumber() + ' ' + message.getSubject() + ' ' + message.getLineCount());
				System.out.flush();
			}
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		System.out.println("__________________________");
		System.out.flush();
	}

	/**
	 * Display message content. The message is selected by the message index and retrieved in the current folder
	 * 
	 * @param idx
	 *            Message index
	 */
	public void displayMessageContent(int idx) {
		try {
			Message message = getMessage(idx);

			Renderable msg = new RenderableMessage(message);
			System.out.println(msg.getBodytext() + "(attachments: " + msg.getAttachmentCount() + ")");
			System.out.flush();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getCurrentFolderPath() {
		return currentFolder.getFullName();
	}

	/**
	 * Expunge (permanently remove) messages marked DELETED.
	 * 
	 * @return array of expunged Message objects
	 * @throws MessagingException
	 */
	public Message[] expungeCurrentFolder() throws MessagingException {
		return currentFolder.expunge();
	}

	/**
	 * Select a new folder.
	 * 
	 * @param name
	 *            the new folder name. if this is '..', the current folder parent is selected.
	 * @throws MessagingException
	 */
	public void selectFolder(String name) throws MessagingException {
		synchronized (currentFolder) {
			if (name.equals("..")) {
				Folder folder = currentFolder.getParent();
				if (folder != null) {
					currentFolder.close(true);
					removeListeners(currentFolder);
					addListeners(folder);
					currentFolder = folder;
				}
			} else {
				Folder folder = currentFolder.getFolder(name);
				if (!folder.exists()) {
					System.out.println("Folder " + name + " not exists.");
					System.out.flush();
				} else {
					addListeners(folder);
					// try to open read/write and if that fails try read-only
					try {
						folder.open(Folder.READ_WRITE);
					} catch (MessagingException ex) {
						folder.open(Folder.READ_ONLY);
					}
					removeListeners(currentFolder);
					currentFolder = folder;
				}
			}
		}
	}

	/**
	 * Create a new folder in the current folder
	 * 
	 * @param name
	 *            folder name to created
	 * @throws MessagingException
	 */
	public void createFolder(String name) throws MessagingException {
		Folder folder = currentFolder.getFolder(name);
		// addListeners(folder);
		if (!folder.exists()) {
			folder.create(Folder.READ_WRITE | Folder.HOLDS_FOLDERS | Folder.HOLDS_MESSAGES);
		}
		// removeListeners(folder);
	}

	/**
	 * Delete a folder in the current folder.
	 * 
	 * @param name
	 *            folder name to deleted
	 * @throws MessagingException
	 */
	public void deleteFolder(String name) throws MessagingException {
		Folder folder = currentFolder.getFolder(name);
		// addListeners(folder);
		folder.delete(false);
		// removeListeners(folder);
	}

	/**
	 * Retrieve a Message object in the current folder, according to the message index.
	 * 
	 * @param idx
	 *            Message index
	 * @return Message object
	 * @throws MessagingException
	 */
	public Message getMessage(int idx) throws MessagingException {
		return currentFolder.getMessage(idx);
	}

	/**
	 * Search this Folder for messages matching the specified search criterion.
	 * Returns an array containing the matching messages.
	 * Returns an empty array if no matches were found.
	 * 
	 * @param term
	 *            the search criterion
	 * @return array of matching messages
	 * @throws MessagingException
	 */
	public Message[] getMessages(SearchTerm term) throws MessagingException {
		if (term != null) {
			return currentFolder.search(term);
		} else {
			return currentFolder.getMessages();
		}
	}

	/**
	 * Retrieve a Message object in the current folder, according to the message index, mark this message deleted.
	 * 
	 * @param idx
	 *            Message index
	 * @throws MessagingException
	 */
	public void deleteMessage(int idx) throws MessagingException {
		Message message = getMessage(idx);
		deleteMessage(message);
	}

	/**
	 * Mark this message deleted.
	 * 
	 * @param message
	 *            Message object
	 * @throws MessagingException
	 */
	public void deleteMessage(Message message) throws MessagingException {
		message.setFlag(Flags.Flag.DELETED, true);
	}

	/**
	 * Mark these messages deleted.
	 * 
	 * @param messages
	 * @throws MessagingException
	 */
	public void deleteMessages(Message[] messages) throws MessagingException {
		currentFolder.setFlags(messages, new Flags(Flags.Flag.DELETED), true);
	}

}
