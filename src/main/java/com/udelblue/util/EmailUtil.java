package com.udelblue.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailUtil {

	public static boolean sendEmail(String to, String from, String subject, String body, String host) {

		final Logger log = LoggerFactory.getLogger(EmailUtil.class);
		boolean sent = false;
		// Get system properties
		Properties properties = System.getProperties();
		// Setup mail server
		properties.setProperty("mail.smtp.host", host);
		// Get the default Session object.
		Session session = Session.getDefaultInstance(properties);
		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);
			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));
			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			// Set Subject: header field
			message.setSubject(subject);
			// Now set the actual message
			message.setContent(body, "text/html; charset=utf-8");
			// message.setText(body);

			// Send message
			Transport.send(message);
			log.info("Email sent |to: " + to + "|from: " + from + "|subject: " + subject + "|body: " + body);
			sent = true;
		} catch (MessagingException mex) {
			mex.printStackTrace();
			log.error("Email not sent |to: " + to + "|from: " + from + "|subject: " + subject + "|body: " + body);
			sent = false;
		}

		return sent;
	}

}
