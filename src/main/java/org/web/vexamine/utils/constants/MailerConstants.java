package org.web.vexamine.utils.constants;

/**
 * The Class MailerConstants.
 */
public class MailerConstants {

	public static final String MAIL_SMTP_HOST = "mail.smtp.host";

	public static final String MAIL_SMTP_PORT = "mail.smtp.port";

	public static final String MAIL_SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";

	public static final String MAIL_SMTP_SSL_TRUST = "mail.smtp.ssl.trust";

	public static final String MAIL_SMTP_AUTH = "mail.smtp.auth";

	public static final String REGISTRATION_MAIL_SUBJECT = "Vexamine - Registration Successful!!";
	
	public static final String RESET_MAIL_SUBJECT = "Vexamine - Password Reset Successful!!";

	public static final String MANAGER_REGISTRATION_MAIL_BODY = "Greetings,\n\nYou have been successfully registered as a Manager for the "
	        + "company %s in Vexamine. Please find below the login credentials."
	        + "\n\nMail ID - %s\nPassword - %s\n\nLogin URL - %s" + "\n\nBest Regards,\nTeam Vexamine.";

	public static final String CANDIDATE_REGISTRATION_MAIL_BODY = "Greetings,\n\nYou have been successfully registered as a Candidate "
	        + "in Vexamine. Please find below the login credentials."
	        + "\n\nMail ID - %s\nPassword - %s\n\nLogin URL - %s" + "\n\nBest Regards,\nTeam Vexamine.";

	public static final String ASSIGN_TEST_SUBJECT = "Vexamine - Test Assigned";

	public static final String ASSIGN_TEST_BODY = "Greetings,\n\nYou have been assigned to attend a test on the category - %s "
	        + "in the Vexamine Application.\n\nTest URL - %s"
	        + "\n\nNOTE - The test will expire on %s.\n\nBest Regards,\nTeam Vexamine";

	public static final String REGISTRATION_FAILED = "%s registration mail sending failed for Email-ID %s";

	public static final String ASSIGNMENT_FAILED = "Test Assignment mail sending failed for Email-ID %s";

	public static final String MANAGER_ROLE = "Manager";

	public static final String CANDIDATE_ROLE = "Candidate";

	public static final String TEST_REPORT_SUBJECT = "Vexamine - Test Report!!";

	public static final String TEST_REPORT_BODY = "Greetings ,\n\nThe test conducted on the Question Bank - %s has been"
	        + " completed.\n\nPlease find attached the Test Report.\n\nBest Regards,\nTeam Vexamine";

	public static final String TEST_REPORT_FAILED = "Report mail sending failed";

	public static final String REGISTRATION_RESET_SUBJECT = "Vexamine - Password Reset!!";

	public static final String REGISTRATION_RESET_BODY = "Greetings,\n\nYour password has been changed.\nKindly find below the new login details."
	        + "\n\nMail ID - %s\nPassword - %s\n\nURL - %s\n\nBest Regards,\nTeam Vexamine.";

	public static final String CONTACT_US_SUBJECT = "Vexamine - Contact Form!!";

	public static final String CONTACT_US_BODY = "Greetings,\n\nThis is a mail regarding a person who has shown their interest in contacting us.\n\n"
	        + "Sender Mail ID - %s.\n\nSender Name - %s\n\nSubject from sender:\n%s\n\nMessage from sender:\n%s\n\nBest Regards.";

	public static final String LOGIN_URL = "";

	public static final String TEST_ATTEND_URL = "";
	
	public static final String CANDIDATE_RESET_MAIL_BODY = "Greetings,\n\nYour password have been successfully updated "
	        + "in Vexamine. Please find below the updated login credentials."
	        + "\n\nMail ID - %s\nPassword - %s\n\nLogin URL - %s" + "\n\nBest Regards,\nTeam Vexamine.";

}
