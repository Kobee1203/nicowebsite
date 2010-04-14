package org.nds.mail;

import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

public class MailService {

	private final JavaMailSender mailSender;
	private final SimpleMailMessage templateMessage;

	public MailService(JavaMailSender mailSender, SimpleMailMessage templateMessage) {
		this.mailSender = mailSender;
		this.templateMessage = templateMessage;
	}

	public void sendMail(String to, String dear, String content) {
		MimeMessage message = mailSender.createMimeMessage();

		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setSentDate(new Date());
			helper.setFrom(templateMessage.getFrom());
			helper.setTo(to);
			helper.setSubject(templateMessage.getSubject());
			helper.setText(String.format(templateMessage.getText(), dear, content), String.format(templateMessage.getText(), dear, content));

			FileSystemResource file = new FileSystemResource("C:\\eula.1028.txt");
			helper.addAttachment(file.getFilename(), file);
			FileSystemResource file2 = new FileSystemResource("C:\\eula.1031.txt");
			helper.addAttachment(file2.getFilename(), file2);

		} catch (MessagingException e) {
			throw new MailParseException(e);
		}
		mailSender.send(message);
	}

	public void sendMailTo(String email) {
		SimpleMailMessage msg = new SimpleMailMessage(templateMessage);
		msg.setSentDate(new Date());
		msg.setTo(email);
		msg.setText("This is a test.\nGo Spring!\n");
		try {
			this.mailSender.send(msg);
		} catch (MailException e) {
			System.err.println("Didn't work.");
			e.printStackTrace();
		}
	}

	public static final void main(String[] args) {
		ApplicationContext appCtx = new ClassPathXmlApplicationContext(new String[] { "spring/mail-service.xml" });

		MailService mailService = (MailService) appCtx.getBean("mailTest");
		// mailService.sendMailTo("nicolas.dossantos@gmail.com");
		mailService.sendMail("nicolas.dossantos@gmail.com", "Nico", "click <a href=\"#\">here</a> to validate your registration.");
	}
}