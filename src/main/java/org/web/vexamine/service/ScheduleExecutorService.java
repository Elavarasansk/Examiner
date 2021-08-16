package org.web.vexamine.service;

import java.io.File;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web.vexamine.dao.repository.TestAssignmentRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * The Class ScheduleExecutorService.
 */
@Service

/** The Constant log. */
@Slf4j
public class ScheduleExecutorService {

	/** The test assignment repository. */
	@Autowired
	private TestAssignmentService testAssignmentService;

	/** The report generator service. */
	@Autowired
	private ReportGeneratorService reportGeneratorService;

	/** The mail sending service. */
	@Autowired
	private MailSendingService mailSendingService;

	/**
	 * Schedule.
	 *
	 * @param mailId the mail id
	 * @param expirationDate the expiration date
	 * @param assigneeList the assignee list
	 * @param questionBankName the question bank name
	 */
	public void schedule(String mailId, Timestamp expirationDate, List<Long> assigneeList, String questionBankName) {
		ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
		Calendar calendar = Calendar.getInstance();
		long duration = Math.subtractExact(expirationDate.getTime(), calendar.getTime().getTime());
		long delay = TimeUnit.MILLISECONDS.toMinutes(duration);
		ses.schedule(execute(mailId, assigneeList, expirationDate, questionBankName), delay, TimeUnit.MINUTES);
		ses.shutdown();
	}

	/**
	 * Execute.
	 *
	 * @param mailId the mail id
	 * @param assigneeList the assignee list
	 * @param expirationDate the expiration date
	 * @param questionBankName the question bank name
	 * @return the runnable
	 */
	private Runnable execute(String mailId, List<Long> assigneeList, Timestamp expirationDate,
	        String questionBankName) {
		Runnable runnable = new Runnable() {
			public void run() {
				testAssignmentService.updateExpiredStatus(assigneeList);
				File file = null;
				try {
					file = reportGeneratorService.generateReportByAssignment(questionBankName, expirationDate);
					mailSendingService.sendTestReportMail(mailId, file, questionBankName);
				} catch (Exception e) {
					e.printStackTrace();
					log.error("Exception while generating report " + e.getMessage());
				} finally {
					FileUtils.deleteQuietly(file);
				}
			}
		};
		return runnable;

	}

}