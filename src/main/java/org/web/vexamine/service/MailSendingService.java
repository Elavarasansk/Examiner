package org.web.vexamine.service;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web.vexamine.dao.vo.ContactFormVo;
import org.web.vexamine.utils.constants.MailerConstants;

import lombok.extern.slf4j.Slf4j;

/**
 * The Class MailSendingService.
 */
@Service

/** The Constant log. */
@Slf4j
public class MailSendingService {

	/** The report mailer. */
	@Autowired
	private ReportMailer reportMailer;

	public boolean sendManagerRegistrationMail(String mailId, String password, String company) {
		try {
			String mailSubject = MailerConstants.REGISTRATION_MAIL_SUBJECT;
			String url = MailerConstants.LOGIN_URL;
			String mailBody = String.format(MailerConstants.MANAGER_REGISTRATION_MAIL_BODY, company, mailId, password,
			        url);
			String recipients[] = { mailId };
			reportMailer.addToRecipients(recipients);
			reportMailer.composeAndSendMail(mailSubject, mailBody);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(String.format("%s::%s::%s", this.getClass().getSimpleName(), "sendManagerRegistrationMail",
			        String.format(MailerConstants.REGISTRATION_FAILED, MailerConstants.MANAGER_ROLE, mailId)));
			return false;
		}
	}

	/**
	 * Send candidate registration mail.
	 *
	 * @param mailId
	 *            the mail id
	 * @param password
	 *            the password
	 */
	public boolean sendCandidateRegistrationMail(String mailId, String password) {
		try {
			String mailSubject = MailerConstants.REGISTRATION_MAIL_SUBJECT;
			String url = MailerConstants.LOGIN_URL;
			String mailBody = String.format(MailerConstants.CANDIDATE_REGISTRATION_MAIL_BODY, mailId, password, url);
			String recipients[] = { mailId };
			reportMailer.addToRecipients(recipients);
			reportMailer.composeAndSendMail(mailSubject, mailBody);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(String.format("%s::%s::%s", this.getClass().getSimpleName(), "sendCandidateRegistrationMail",
			        String.format(MailerConstants.REGISTRATION_FAILED, MailerConstants.CANDIDATE_ROLE, mailId)));
			return false;
		}
	}
	
	/**
	 * Send candidate Reset password mail.
	 *
	 * @param mailId
	 *            the mail id
	 * @param password
	 *            the password
	 */
	public boolean sendCandidateResetMail(String mailId, String password) {
		try {
			String mailSubject = MailerConstants.RESET_MAIL_SUBJECT;
			String url = MailerConstants.LOGIN_URL;
			String mailBody = String.format(MailerConstants.CANDIDATE_RESET_MAIL_BODY, mailId, password, url);
			String recipients[] = { mailId };
			reportMailer.addToRecipients(recipients);
			reportMailer.composeAndSendMail(mailSubject, mailBody);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(String.format("%s::%s::%s", this.getClass().getSimpleName(), "sendCandidateResetMail",
			        String.format(MailerConstants.REGISTRATION_FAILED, MailerConstants.CANDIDATE_ROLE, mailId)));
			return false;
		}
	}

	/**
	 * Send test assignment mail.
	 *
	 * @param mailId
	 *            the mail id
	 * @param category
	 *            the category
	 * @param expirationTime
	 *            the expiration time
	 * @return true, if successful
	 */
	public boolean sendTestAssignmentMail(String mailId, String category, String expirationTime) {
		try {
			String mailSubject = MailerConstants.ASSIGN_TEST_SUBJECT;
			String url = MailerConstants.TEST_ATTEND_URL;
			String mailBody = String.format(MailerConstants.ASSIGN_TEST_BODY, category, url, expirationTime);
			String recipients[] = { mailId };
			reportMailer.addToRecipients(recipients);
			reportMailer.composeAndSendMail(mailSubject, mailBody);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(String.format("%s::%s::%s", this.getClass().getSimpleName(), "sendTestAssignmentMail",
			        String.format(MailerConstants.ASSIGNMENT_FAILED, mailId)));
			return false;
		}
	}

	/**
	 * Send test report mail.
	 *
	 * @param mailId
	 *            the mail id
	 * @param file
	 *            the file
	 * @param questionBankName
	 *            the question bank name
	 */
	public boolean sendTestReportMail(String mailId, File file, String questionBankName) {
		try {
			String mailSubject = MailerConstants.TEST_REPORT_SUBJECT;
			String mailBody = String.format(MailerConstants.TEST_REPORT_BODY, questionBankName);
			String recipients[] = { mailId };
			File filePaths[] = { file };
			reportMailer.addToRecipients(recipients);
			reportMailer.composeAndSendMail(mailSubject, mailBody, filePaths);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(String.format("%s::%s::%s", this.getClass().getSimpleName(), "sendTestReportMail",
			        MailerConstants.TEST_REPORT_FAILED, mailId));
			return false;
		}
	}

	public boolean sendContactMail(ContactFormVo contactFormVo) {
		String senderName = contactFormVo.getName();
		String senderEmail = contactFormVo.getEmail();
		String subject = contactFormVo.getSubject();
		String message = contactFormVo.getMessage();
		try {
			String mailSubject = MailerConstants.CONTACT_US_SUBJECT;
			String mailBody = String.format(MailerConstants.CONTACT_US_BODY, senderEmail, senderName, subject, message);
			String recipients[] = { "support@vexamine.com", "sales@vexamine.com" };
			reportMailer.addToRecipients(recipients);
			reportMailer.composeAndSendMail(mailSubject, mailBody);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
