package org.web.vexamine.mailservices;

import org.web.vexamine.utils.constants.MailerConstants;

/**
 * The Class RegistrationMailer.
 */
public class RegistrationMailer {

	/**
	 * Compose registration mail body.
	 *
	 * @param role the role
	 * @param company the company
	 * @param mailId the mail id
	 * @return the string
	 */
	public static String composeRegistrationMailBody(String role, String company, String mailId) {
		return String.format(MailerConstants.MANAGER_REGISTRATION_MAIL_BODY, role, company, mailId);
	}

}
