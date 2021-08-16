package org.web.vexamine.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.web.vexamine.constants.VexamineTestConstants;
import org.web.vexamine.dao.entity.ReportEntity;
import org.web.vexamine.dao.entity.SubReportEntity;
import org.web.vexamine.dao.entity.TestAssignment;
import org.web.vexamine.dao.entity.TestReportEntity;
import org.web.vexamine.dao.entity.TestSummary;
import org.web.vexamine.dao.entity.report.StatusPieChart;
import org.web.vexamine.dao.entity.report.TestDetailsReportEntity;
import org.web.vexamine.dao.enumclass.StatusType;
import org.web.vexamine.dao.repository.ReportGeneratorRepository;
import org.web.vexamine.dao.repository.ReportGeneratorRepository.TestResultInfo;
import org.web.vexamine.utils.CustomizePieChart;
import org.web.vexamine.utils.CustomizeTestPieChart;
import org.web.vexamine.utils.ReportUtils;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

/** The Constant log. */
@Slf4j
@Service
public class ReportGeneratorService  {

	/** The test summary service. */
	@Autowired
	private TestSummaryService testSummaryService;

	/** The report generator repository. */
	@Autowired
	private ReportGeneratorRepository reportGeneratorRepository;

	/** The test assignment service. */
	@Autowired
	private TestAssignmentService  testAssignmentService;
	
	/** The test assignment service. */
	@Autowired
	private MailSendingService  mailSendingService;
	
	@Value("classpath:design/report.jrxml")
	Resource mainReportJrxml;
	
	@Value("classpath:design/sub_report.jrxml")
	Resource subReportJrxml;
	
	@Value("classpath:design/manager_report.jrxml")
	Resource managerReportJrxml;
	
	@Value("classpath:design/manager_sub_report.jrxml")
	Resource managerSubReportJrxml;
	
	
	


	/**
	 * Generate report.
	 *
	 * @param testSummaryId the test summary id
	 * @return the file
	 * @throws Exception the exception
	 */
	public File generateReport(Long testSummaryId) throws Exception {
		Pair<List<ReportEntity>, List<SubReportEntity>> dataPair = formData(testSummaryId);
		try {
			CustomizePieChart customizePieChart = new CustomizePieChart();
			JasperReport jasperReport = customizePieChart.getJasperReport(mainReportJrxml);		
			JasperReport jasperSubReport = JasperCompileManager.compileReport(JRXmlLoader.load(subReportJrxml.getInputStream()));
			String fileName = dataPair.getLeft().stream().findFirst().map(data->{
				return	new StringBuilder(data.getMailId()).append("_").append(data.getQuestionBankName()).toString();
			}).orElse(VexamineTestConstants.PDF_NAME);
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dataPair.getLeft());
			JRBeanCollectionDataSource dataSubSource = new JRBeanCollectionDataSource(dataPair.getRight());
			Map<String,Object> params = new HashMap<String,Object>();
			File outputFile = new File(fileName.concat(VexamineTestConstants.PDF));
			JasperPrint jasperPrint =  JasperFillManager.fillReport(jasperReport,params,dataSource);
			JasperPrint jasperSubPrint =  JasperFillManager.fillReport(jasperSubReport,params,dataSubSource);		
			JasperExportManager.exportReportToPdfFile(jasperPrint,outputFile.getAbsolutePath());
			OutputStream output = new FileOutputStream(outputFile);
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setExporterInput(SimpleExporterInput.getInstance(Arrays.asList(jasperPrint,jasperSubPrint)));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(output)); 
			SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
			exporter.setConfiguration(configuration);
			exporter.exportReport();
			output.close();
			return outputFile;
		}catch(Exception e) {
			e.printStackTrace();
			log.error("Exception while generating report "+e.getMessage());
			throw new Exception("Report download failed.");

		}
	}


	/**
	 * Form data.
	 *
	 * @param testSummaryId the test summary id
	 * @return the pair
	 */
	public Pair<List<ReportEntity>, List<SubReportEntity>> formData(Long testSummaryId) {
		TestSummary testSummary = testSummaryService.getSummary(testSummaryId);
		TestAssignment testAssignment = testSummary.getTestAssignment();
		Long testAssignmentId = testAssignment.getId();		
		List<TestResultInfo> testResultList = reportGeneratorRepository.findByTestAssignmentId(testAssignmentId);
		ReportEntity reportEntity = ReportEntity.builder()
				.mailId(testAssignment.getUserAuthorityInfo().getUserCredentials().getMailId())
				.questionBankName(testAssignment.getQuestionBank().getQuestionBankName())
				.category(testAssignment.getQuestionBank().getTestCategory().getCategory())
				.testDate(String.valueOf(testSummary.getCreateDate()))
				.questionTaken(testAssignment.getQuestionsCount())
				.rightanswer(testSummary.getAnsweredCorrect())
				.wronganswer(testSummary.getAnsweredWrong())
				.unanswer(testSummary.getUnanswered())
				.testDate(ReportUtils.formatDate(testSummary.getCreateDate()))
				.rightLabel(VexamineTestConstants.RIGHT_ANSWER)
				.wrongLabel(VexamineTestConstants.WRONG_ANSWER)
				.timeTaken(ReportUtils.formTimeTaken(testSummary.getTimeTaken()))
				.build();
		List<SubReportEntity>  subReportList = testResultList.stream().map(result ->{
			return SubReportEntity.builder()
					.question(result.getQuestionAnswer().getQuestion())
					.answer(result.getAnswer())
					.correctAnswer(result.getQuestionAnswer().getAnswer())
					.build();
		}).collect(Collectors.toList());
		return  Pair.of(Arrays.asList(reportEntity),subReportList);
	}

	/**
	 * Form data by question bank.
	 *
	 * @param questionBankName the question bank name
	 * @param expirationDate the expiration date
	 * @param testDetailsReportEntity the test details report entity
	 * @param subReportList the sub report list
	 * @param statusPieChart the status pie chart
	 * @throws Exception the exception
	 */
	private void formDataByQuestionBank(String questionBankName, Timestamp expirationDate,TestDetailsReportEntity testDetailsReportEntity, List<TestReportEntity> subReportList, StatusPieChart statusPieChart) throws Exception {
		List<TestAssignment> testAssignmentList = testAssignmentService.getTestByQuestionBankName(questionBankName,expirationDate);
		List<Long> testAssignmentIdList = testAssignmentList.stream().map(TestAssignment::getId).collect(Collectors.toList());
		List<TestSummary> testSummaryList = testSummaryService.getSummaryByTestAssignment(testAssignmentIdList);
		Map<String, Long> statusMap = testAssignmentList.stream().collect(Collectors.groupingBy(TestAssignment::getStatus,Collectors.counting()));
		StatusPieChart statusChart = StatusPieChart.builder()
				.newCount(statusMap.containsKey(StatusType.NEW.getType()) ? statusMap.get(StatusType.NEW.getType()) : 0 )
				.inprogressCount(statusMap.containsKey(StatusType.INPROGRESS.getType()) ? statusMap.get(StatusType.INPROGRESS.getType()) : 0 )
				.completedCount(statusMap.containsKey(StatusType.COMPLETED.getType()) ? statusMap.get(StatusType.COMPLETED.getType()) : 0 )
				.expiredCount(statusMap.containsKey(StatusType.EXPIRED.getType()) ? statusMap.get(StatusType.EXPIRED.getType()) : 0 )
				.rejectedCount(statusMap.containsKey(StatusType.REJECTED.getType()) ? statusMap.get(StatusType.REJECTED.getType()) : 0 )
				.build();
		BeanUtils.copyProperties(statusPieChart, statusChart);
		TestAssignment testAssignment =	testAssignmentList.stream().findFirst().orElse(new TestAssignment());
		TestDetailsReportEntity testDetails = TestDetailsReportEntity.builder()
				.questionBankName(questionBankName)
				.questionsCount(testAssignment.getQuestionsCount())
				.category(testAssignment.getQuestionBank().getTestCategory().getCategory())
				.subcategory(testAssignment.getQuestionBank().getTestCategory().getSubCategory())
				.totalCandidate(testAssignmentList.size())
				.newLabel(StatusType.NEW.getType())
				.inprogressLabel(StatusType.INPROGRESS.getType())
				.completedLabel(StatusType.COMPLETED.getType())
				.expiredLabel(StatusType.EXPIRED.getType())
				.rejectedLabel(StatusType.REJECTED.getType())
				.newCount(statusPieChart.getNewCount())
				.inprogressCount(statusPieChart.getInprogressCount())
				.completedCount(statusPieChart.getCompletedCount())
				.expiredCount(statusPieChart.getExpiredCount())
				.rejectedCount(statusPieChart.getRejectedCount())
				.build();
		BeanUtils.copyProperties(testDetailsReportEntity, testDetails);
		List<TestReportEntity> reportList = testSummaryList.stream().map(test -> {
			return TestReportEntity.builder().mailId(test.getTestAssignment().getUserAuthorityInfo().getUserCredentials().getMailId())
					.questionsCount(test.getTestAssignment().getQuestionsCount())
					.answeredCorrect(test.getAnsweredCorrect())
					.answeredWrong(test.getAnsweredWrong())
					.unanswered(test.getUnanswered())
					.totalMarksObtained(Objects.nonNull(test.getTotalMarkObtained()) ? test.getTotalMarkObtained() : 0)
					.build(); }).collect(Collectors.toList());
		subReportList.addAll(reportList);
	}


	/**
	 * Generate report by assignment.
	 *
	 * @param questionBankName the question bank name
	 * @param expirationDate the expiration date
	 * @return the file
	 * @throws Exception the exception
	 */
	public File generateReportByAssignment(String questionBankName, Timestamp expirationDate) throws Exception {
		try {
			TestDetailsReportEntity testDetailsReportEntity = TestDetailsReportEntity.builder().build();
			List<JasperPrint> reportList = new ArrayList<>();
			StatusPieChart statusPieChart = StatusPieChart.builder().build();
			List<TestReportEntity> subReportList = new ArrayList<>();
			formDataByQuestionBank(questionBankName ,expirationDate,testDetailsReportEntity ,subReportList,statusPieChart);
			CustomizeTestPieChart customizePieChart = new CustomizeTestPieChart();
			JasperReport jasperReport = customizePieChart.getJasperReport(managerReportJrxml);		
			JasperReport jasperSubReport = JasperCompileManager.compileReport(JRXmlLoader.load(managerSubReportJrxml.getInputStream()));
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(Arrays.asList(testDetailsReportEntity));
			Map<String,Object> params = new HashMap<>();
			JRBeanCollectionDataSource tableDataSource = new JRBeanCollectionDataSource(Arrays.asList(statusPieChart));
			params.put("testSummaryDataSource", tableDataSource);
			JasperPrint jasperPrint =  JasperFillManager.fillReport(jasperReport,params,dataSource);
			reportList.add(jasperPrint);
			if(CollectionUtils.isNotEmpty(subReportList)) {
				Map<String,Object> subReportParams = new HashMap<>();
				JRBeanCollectionDataSource subDataSource = new JRBeanCollectionDataSource(subReportList);
				subReportParams.put("testResultDataSource", subDataSource);
				JasperPrint jasperSubReporPrint =  JasperFillManager.fillReport(jasperSubReport,subReportParams,subDataSource);
				reportList.add(jasperSubReporPrint);
			}	
			File outputFile = new File(questionBankName.concat(VexamineTestConstants.PDF));
			OutputStream output = new FileOutputStream(outputFile);
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setExporterInput(SimpleExporterInput.getInstance(reportList));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(output)); 
			SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
			exporter.setConfiguration(configuration);
			exporter.exportReport();
			output.close();
			return outputFile;
		}catch(Exception e) {
			e.printStackTrace();
			log.error("Exception while generating report "+e.getMessage());
			throw new Exception("Report download failed.");

		}
	}


	public boolean sendReportByMail(Long testSummaryId) throws Exception {
		File file = null;
		boolean isMailSend = false;
		TestSummary testSummary = testSummaryService.getSummary(testSummaryId);
		String mailId = testSummary.getTestAssignment().getUserAuthorityInfo().getUserCredentials().getMailId();
		String questionBankName = testSummary.getTestAssignment().getQuestionBank().getQuestionBankName();
		try {	
		file = generateReport(testSummaryId);
		isMailSend = mailSendingService.sendTestReportMail(mailId, file, questionBankName);
		if(!isMailSend) {
			throw new Exception("Mail to the following candidate has been failed "+" - \""+mailId+"\"");
		}
		}catch(Exception e) {
			throw new Exception("Mail to the following candidate has been failed "+" - \""+mailId+"\"");
		}finally {
			FileUtils.deleteQuietly(file);
		}
		return isMailSend;
	}

}
