package org.web.vexamine.service;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.annotation.PostConstruct;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web.vexamine.utils.constants.MailerConstants;

import lombok.extern.slf4j.Slf4j;

/** The Constant log. */
@Slf4j
@Service
public class ReportMailer {

	/** The host. */
	@Value("${report.mail.host}")
	private String host;

	/** The port. */
	@Value("${report.mail.port}")
	private String port;

	/** The sender id. */
	@Value("${report.mail.senderid}")
	private String senderId;

	/** The sender password. */
	@Value("${report.mail.senderpassword}")
	private String senderPassword;

	/** The session. */
	private Session session;

	/** The to recipients. */
	private Address[] toRecipients = new InternetAddress[0];

	/** The cc recipients. */
	private Address[] ccRecipients = new InternetAddress[0];

	/** The bcc recipients. */
	private Address[] bccRecipients = new InternetAddress[0];

	/**
	 * Instantiates a new report mailer.
	 */
	public ReportMailer() {
		emptyRecipients();
	}

	/**
	 * Configure mail.
	 */
	@PostConstruct
	private void configureMail() {
		Properties props = new Properties();
		props.put(MailerConstants.MAIL_SMTP_HOST, host);
		props.put(MailerConstants.MAIL_SMTP_PORT, port);
		props.put(MailerConstants.MAIL_SMTP_STARTTLS_ENABLE, "true");
		props.put(MailerConstants.MAIL_SMTP_SSL_TRUST, host);
		props.put(MailerConstants.MAIL_SMTP_AUTH, "true");
		this.session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(senderId, senderPassword);
			}
		});
	}

	/**
	 * Adds the to recipients.
	 *
	 * @param address
	 *            the address
	 * @throws AddressException
	 *             the address exception
	 */
	public void addToRecipients(String... address) throws AddressException {
		String[] addressArr = address;
		this.toRecipients = new InternetAddress[address.length];
		for (int counter = 0; counter < addressArr.length; counter++) {
			this.toRecipients[counter] = new InternetAddress(addressArr[counter]);
		}
	}

	/**
	 * Adds the CC recipients.
	 *
	 * @param address
	 *            the address
	 * @throws AddressException
	 *             the address exception
	 */
	public void addCCRecipients(String... address) throws AddressException {
		String[] addressArr = address;
		this.ccRecipients = new InternetAddress[address.length];
		for (int counter = 0; counter < addressArr.length; counter++) {
			this.ccRecipients[counter] = new InternetAddress(addressArr[counter]);
		}
	}

	/**
	 * Adds the BCC recipients.
	 *
	 * @param address
	 *            the address
	 * @throws AddressException
	 *             the address exception
	 */
	public void addBCCRecipients(String... address) throws AddressException {
		String[] addressArr = address;
		this.bccRecipients = new InternetAddress[address.length];
		for (int counter = 0; counter < addressArr.length; counter++) {
			this.bccRecipients[counter] = new InternetAddress(addressArr[counter]);
		}
	}

	/**
	 * Compose and send mail.
	 *
	 * @param subject
	 *            the subject
	 * @param content
	 *            the content
	 * @throws MessagingException
	 *             the messaging exception
	 */
	public void composeAndSendMail(String subject, String content) throws MessagingException {
		try {
			MimeMessage message = new MimeMessage(session);
			if (this.toRecipients.length == 0 && this.ccRecipients.length == 0 && this.bccRecipients.length == 0)
				return;
			if (this.toRecipients.length > 0)
				message.addRecipients(Message.RecipientType.TO, this.toRecipients);
			if (this.ccRecipients.length > 0)
				message.addRecipients(Message.RecipientType.CC, this.ccRecipients);
			if (this.bccRecipients.length > 0)
				message.addRecipients(Message.RecipientType.BCC, this.bccRecipients);
			message.setSubject(subject);
			message.setText(content);
			message.setSender(new InternetAddress("noreply@vexamine.com"));
			Transport.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
			log.error("Failed to send mail for " + Arrays.toString(this.toRecipients));
			throw new MessagingException("Mail send failed.");
		} finally {
			emptyRecipients();
		}
	}

	/**
	 * Compose and send mail.
	 *
	 * @param subject
	 *            the subject
	 * @param content
	 *            the content
	 * @param filePaths
	 *            the file paths
	 * @throws MessagingException
	 *             the messaging exception
	 */
	public void composeAndSendMail(String subject, String content, File... filePaths) throws MessagingException {
		List<File> fileList = Arrays.asList(filePaths);
		try {
			MimeMessage message = new MimeMessage(session);
			Multipart multipart = new MimeMultipart();
			if (this.toRecipients.length == 0 && this.ccRecipients.length == 0 && this.bccRecipients.length == 0)
				return;
			if (this.toRecipients.length > 0)
				message.addRecipients(Message.RecipientType.TO, this.toRecipients);
			if (this.ccRecipients.length > 0)
				message.addRecipients(Message.RecipientType.CC, this.ccRecipients);
			if (this.bccRecipients.length > 0)
				message.addRecipients(Message.RecipientType.BCC, this.bccRecipients);
			for (File file : fileList) {
				BodyPart body = new MimeBodyPart();
				body.setDataHandler(new DataHandler(new FileDataSource(file)));
				body.setFileName(file.getName());
				multipart.addBodyPart(body);
			}
			message.setContent(multipart);
			message.setSubject(subject);
			message.setText(content);
			message.setSender(new InternetAddress("noreply@vexamine.com"));
			Transport.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
			log.error("Failed to send mail for " + Arrays.toString(this.toRecipients));
			throw new MessagingException("Mail send failed.");
		} finally {
			fileList.stream().forEach(file -> FileUtils.deleteQuietly(file));
			emptyRecipients();
		}
	}

	/**
	 * Empty recipients.
	 */
	private void emptyRecipients() {
		toRecipients = new InternetAddress[0];
		ccRecipients = new InternetAddress[0];
		bccRecipients = new InternetAddress[0];
	}
}
