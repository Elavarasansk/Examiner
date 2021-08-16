package org.web.vexamine.mailservices;

import org.web.vexamine.utils.constants.MailerConstants;

/**
 * The Class TestAssignmentMailer.
 */
public class TestAssignmentMailer {

	/**
	 * Compose test assignment mail body.
	 *
	 * @param name the name
	 * @param scheduledDate the scheduled date
	 * @param url the url
	 * @param expirationDate the expiration date
	 * @return the string
	 */
	public static String composeTestAssignmentMailBody(String name, String scheduledDate, String url,
	        String expirationDate) {
		return String.format(MailerConstants.ASSIGN_TEST_BODY, name, scheduledDate, url, expirationDate);
	}

}
