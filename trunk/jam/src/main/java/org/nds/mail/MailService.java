package org.nds.mail;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nds.common.Util;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.mail.MailParseException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 * @author Nicolas
 */
public final class MailService {

	protected final Log logger = LogFactory.getLog(getClass());

	private final JavaMailSender mailSender;
	private final SimpleMailMessage templateMessage;
	private final Properties properties;

	private List<Resource> resourceAttachments;

	public MailService(JavaMailSender mailSender, SimpleMailMessage templateMessage) {
		this(mailSender, templateMessage, (String) null);
	}

	public MailService(JavaMailSender mailSender, SimpleMailMessage templateMessage, String propertiesPath) {
		this(mailSender, templateMessage, Util.load(propertiesPath, MailConstants.DEFAULT_RESOURCE_PATH));
	}

	public MailService(JavaMailSender mailSender, SimpleMailMessage templateMessage, Properties props) {
		this.mailSender = mailSender;
		this.templateMessage = templateMessage;
		this.properties = props;
	}

	public final void sendMail(String mail) {
		sendMail(mail, null, null);
	}

	public final void sendMail(String mail, Properties props) {
		sendMail(mail, props, null);
	}

	public final void sendMail(String mail, Properties props, List<Resource> attachments) {
		MimeMessage message = mailSender.createMimeMessage();

		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setSentDate(new Date());
			helper.setFrom(templateMessage.getFrom());
			helper.setTo(mail);
			helper.setSubject(templateMessage.getSubject());

			String content = templateMessage.getText();
			if (props != null) {
				for (Map.Entry<Object, Object> entry : props.entrySet()) {
					content = content.replace("%{" + entry.getKey() + "}", (String) entry.getValue());
				}
			}
			if (properties != null) {
				for (Map.Entry<Object, Object> entry : properties.entrySet()) {
					content = content.replace("%{" + entry.getKey() + "}", (String) entry.getValue());
				}
			}
			helper.setText(content);

			// Attachments
			if (attachments != null) {
				for (Resource attachment : attachments) {
					helper.addAttachment(attachment.getFilename(), attachment);
				}
			}
			if (resourceAttachments != null) {
				for (Resource attachment : resourceAttachments) {
					helper.addAttachment(attachment.getFilename(), attachment);
				}
			}

		} catch (MessagingException e) {
			throw new MailParseException(e);
		}
		mailSender.send(message);
	}

	public final void setAttachments(List<Object> attachments) {
		for (Object obj : attachments) {
			if (obj instanceof Resource) {
				resourceAttachments.add((Resource) obj);
			} else if (obj instanceof File) {
				Resource resource = Util.getResource((File) obj);
				resourceAttachments.add(resource);
			} else {
				logger.warn("Object type unknown for attachments: " + obj.getClass() + ": " + obj.toString());
			}
		}
	}

	public static final void main(String[] args) {
		ApplicationContext appCtx = new ClassPathXmlApplicationContext(new String[] { "spring/mail-service.xml" });

		MailService mailService = (MailService) appCtx.getBean("mailTest");
		mailService.sendMail("nicolas.dossantos@gmail.com");
	}
}