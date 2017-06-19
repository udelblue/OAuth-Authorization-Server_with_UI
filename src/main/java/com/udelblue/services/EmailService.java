package com.udelblue.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.udelblue.util.EmailUtil;

@Service
public class EmailService {
	final Logger log = LoggerFactory.getLogger(EmailService.class);

	@Value("${app.smtp}")
	private String smtpAddress;

	@Value("${app.smtpfrom}")
	private String smtpfrom;

	public boolean SendEmail(String to, String subject, String body) {
		return EmailUtil.sendEmail(to, smtpfrom, subject, body, smtpAddress);
	}

}
