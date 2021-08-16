package org.web.vexamine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web.vexamine.service.ReportMailer;

/**
 * The Class MailApiController.
 */
@RestController
@RequestMapping("mail/")
public class MailApiController {

	@Autowired
	private ReportMailer reportMailer;

	/**
	 * Send email.
	 *
	 * @throws Exception the exception
	 */
	@RequestMapping("send")
	public void sendEmail() throws Exception {
			reportMailer.addToRecipients("123.45@gmail.com");
			reportMailer.composeAndSendMail("Subject", "Messagebody");
	}
}
