package org.web.vexamine.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.web.vexamine.dao.vo.TestSummaryVo;
import org.web.vexamine.service.ReportGeneratorService;

/**
 * The Class ReportController.
 */
@RestController
@RequestMapping("/report/download/")
public class ReportController {

	@Autowired
	private ReportGeneratorService  reportGeneratorService;

	/**
	 * Download test summary.
	 *
	 * @param testSummaryId the test summary id
	 * @param response the response
	 * @throws Exception the exception
	 */
	@GetMapping("test/summary/{testSummaryId}")
	public void downloadTestSummary(@PathVariable(value="testSummaryId") Long testSummaryId  ,HttpServletResponse response) throws Exception {
		File file = reportGeneratorService.generateReport(testSummaryId);
		InputStream inputStream = new FileInputStream(file);      
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename="+file.getName()); 
		IOUtils.copy(inputStream, response.getOutputStream());
		response.flushBuffer();
		inputStream.close();
		FileUtils.deleteQuietly(file);
	}
	
	/**
	 * Download report by assignment.
	 *
	 * @param questionBankName the question bank name
	 * @param expirationDate the expiration date
	 * @param response the response
	 * @throws Exception the exception
	 */
	@GetMapping("test/assignment")
	public void downloadReportByAssignment(@RequestParam String questionBankName  ,@RequestParam Timestamp expirationDate, HttpServletResponse response) throws Exception {
		File file = reportGeneratorService.generateReportByAssignment(questionBankName,expirationDate);
		InputStream inputStream = new FileInputStream(file);      
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename="+file.getName()); 
		IOUtils.copy(inputStream, response.getOutputStream());
		response.flushBuffer();
		inputStream.close();
		FileUtils.deleteQuietly(file);
	}
	
	
	@GetMapping("/mail/{testSummaryId}")
	public boolean sendReportByMail(@PathVariable(value="testSummaryId") Long testSummaryId  )throws Exception {
		return reportGeneratorService.sendReportByMail(testSummaryId);
	}


}
