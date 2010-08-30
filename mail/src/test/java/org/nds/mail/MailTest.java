package org.nds.mail;

import java.util.Properties;

import javax.mail.MessagingException;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.nds.mail.listener.DebugConnectionListener;
import org.nds.mail.listener.DebugFolderListener;
import org.nds.mail.listener.DebugMessageChangedListener;
import org.nds.mail.listener.DebugMessageCountListener;
import org.nds.mail.listener.DebugStoreListener;
import org.nds.mail.service.MailRetrieverService;
import org.nds.mail.service.MailSenderService;
import org.nds.mail.service.gmail.GmailHelper;
import org.nds.mail.service.gmail.GmailRetrieverService;
import org.nds.mail.service.gmail.GmailSenderService;

public class MailTest {

	private static MailRetrieverService retrieveService;
	private static MailSenderService sendService;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Properties props = new Properties();
		props.loadFromXML(MailTest.class.getResourceAsStream("mail-properties.xml"));

		sendService = new GmailSenderService(props.getProperty("mail.smtp.username"), props.getProperty("mail.smtp.password"));

		retrieveService = new GmailRetrieverService(props.getProperty("mail.smtp.username"), props.getProperty("mail.smtp.password"), GmailHelper.GmailProtocol.IMAP, null);

		retrieveService.addStoreListener(new DebugConnectionListener());
		retrieveService.addStoreListener(new DebugFolderListener());
		retrieveService.addStoreListener(new DebugStoreListener());

		retrieveService.addFolderListener(new DebugConnectionListener());
		retrieveService.addFolderListener(new DebugFolderListener());
		retrieveService.addFolderListener(new DebugMessageChangedListener());
		retrieveService.addFolderListener(new DebugMessageCountListener());

		// service.setFolderReadingHandler(new DebugFolderReadingHandler());

		retrieveService.open();
		// service.start();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		retrieveService.close();
		// service.stopThread();
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testMailRetriever() throws MessagingException {
		System.out.println("#### TEST MAIL RETRIEVER");
		retrieveService.selectFolder("..");
		retrieveService.selectFolder("[Gmail]/Spam");
		retrieveService.displayCurrentFolderContent();
	}

	@Test
	public void testMailSender() throws EmailException {
		sendService.getEmail().addTo("nicolas.dosssantos@gmail.com");
		((HtmlEmail) sendService.getEmail()).attach(getClass().getResource("email.jpg"), "email image", "Email in attachment");
		sendService.sendMail();
	}

	@Test
	public void testSendLocalizedMail() {

	}

	@Test
	public void testListenNewMail() {

	}

	@Test
	public void testReadAllMessages() {

	}

	@Test
	public void testReadAllMessagesInSpecificFolder() {

	}

	@Test
	public void testReadUnreadMessages() {

	}

	@Test
	public void testReadUnreadMessagesInSpecificFolder() {

	}

	@Test
	public void testReadFlaggedMessages() {

	}

	@Test
	public void testReadFlaggedMessagesInSpecificFolder() {

	}
}
