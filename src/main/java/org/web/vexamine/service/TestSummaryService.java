package org.web.vexamine.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.web.vexamine.constants.VexamineTestConstants;
import org.web.vexamine.dao.entity.TestAssignment;
import org.web.vexamine.dao.entity.TestResult;
import org.web.vexamine.dao.entity.TestSummary;
import org.web.vexamine.dao.entity.UserAuthorityInfo;
import org.web.vexamine.dao.repository.TestAssignmentRepository;
import org.web.vexamine.dao.repository.TestResultRepository;
import org.web.vexamine.dao.repository.TestSummaryRepository;
import org.web.vexamine.dao.repository.specification.TestSummarySpecification;
import org.web.vexamine.dao.vo.TestSummaryVo;
import org.web.vexamine.utils.ReportUtils;
import org.web.vexamine.utils.storage.CookieSessionStorage;

/**
 * The Class TestSummaryService.
 */
@Service
public class TestSummaryService {

	@Autowired
	private TestSummaryRepository testSummaryRepository;

	@Autowired
	private TestAssignmentRepository testAssignmentRepository;

	@Autowired
	private TestResultRepository testResultRepo;
	
	@Autowired
	private TestResultService testResultService;

	@Autowired
	private UserAuthorityService userAuthorityService;

	/**
	 * Adds the summary.
	 *
	 * @param testSummaryVo the test summary vo
	 * @return the test summary
	 */
	public TestSummary addSummary(TestSummaryVo testSummaryVo) {
		String sessionUser = null;
		if(!ObjectUtils.isEmpty(CookieSessionStorage.get())) {
			sessionUser = CookieSessionStorage.get().getUserName();
		}
		UserAuthorityInfo userAuthorityInfo = userAuthorityService.getUserByMailId(testSummaryVo.getMailIdName());
		Optional<TestAssignment> testAssignment = testAssignmentRepository.findById(testSummaryVo.getTestAssignmentId());
		TestSummary testSummary = TestSummary.builder()
				.createUser(sessionUser)
				.updateUser(sessionUser)
				.testAssignment(testAssignment.get())
				.build();		
		BeanUtils.copyProperties(testSummaryVo, testSummary);		
		return testSummaryRepository.save(testSummary);
	}

	/**
	 * Delete summary.
	 *
	 * @param id the id
	 */
	public void deleteSummary(Long id) {
		testSummaryRepository.deleteById(id);
	}

	/**
	 * Gets the test summary count.
	 *
	 * @return the test summary count
	 */
	public long getTestSummaryCount() {
		return testSummaryRepository.count();
	}

	/**
	 * Gets the count by mail id.
	 *
	 * @param mailId the mail id
	 * @return the count by mail id
	 */
	public long getCountByMailId(String mailId) {
		//TODO:
		/*return testSummaryRepository.countByUserAuthorityInfoUserCredentialsMailId(mailId);*/
		return 0L;
	}

	/**
	 * Gets the summary.
	 *
	 * @param id the id
	 * @return the summary
	 */
	public TestSummary getSummary(Long id) {
		Optional<TestSummary> testSummary = testSummaryRepository.findById(id) ; 
		return testSummary.isPresent() ? testSummary.get() : null;		
	}

	/**
	 * Gets the all summary.
	 *
	 * @param testSummaryVo the test summary vo
	 * @return the all summary
	 */
	public List<TestSummary> getAllSummary(TestSummaryVo testSummaryVo) {
		//TODO:
		/*Pageable pageable =  PageRequest.of(testSummaryVo.getOffset(), testSummaryVo.getLimit(), Sort.by(MooduleConductConstants.TEST_DATE).descending());
		Page<TestSummary> testSummaryList = testSummaryRepository
				.findByUserAuthorityInfoUserCredentialsMailIdContainingIgnoreCaseOrTestAssignmentQuestionBankQuestionBankNameContainingIgnoreCase(testSummaryVo.getSearchText(),testSummaryVo.getSearchText(),pageable);	
		return testSummaryList.hasContent() ? testSummaryList.getContent() : Collections.emptyList() ;*/

		return null;
	}

	/**
	 * Search all summary.
	 *
	 * @param testSummaryVo the test summary vo
	 * @return the map
	 */
	public Map<String, Object> searchAllSummary(TestSummaryVo testSummaryVo) {
		Pageable pageable = PageRequest.of(testSummaryVo.getOffset(),testSummaryVo.getLimit());	
		TestSummarySpecification spec = new TestSummarySpecification(testSummaryVo);		
		Page<TestSummary> summaryList = testSummaryRepository.findAll(spec,pageable);		
		List<Long> testAssignmentList = summaryList.stream().map(test->test.getTestAssignment().getId()).collect(Collectors.toList());
		Set<Long> testResultSet = testResultService.getDescriptiveTypeQuestion(testAssignmentList);
		List<Map<String, Object>> dataList = summaryList.getContent().stream().map(summary->{
			Map<String,Object> resultMap = new HashMap<>(); 
			resultMap.put("id", summary.getId());
			resultMap.put("testAssignmentId", summary.getTestAssignment().getId());
			resultMap.put("mailId", summary.getTestAssignment().getUserAuthorityInfo().getUserCredentials().getMailId());
			resultMap.put("questionBankName", summary.getTestAssignment().getQuestionBank().getQuestionBankName());
			resultMap.put("category", summary.getTestAssignment().getQuestionBank().getTestCategory().getCategory());	
			resultMap.put("subCategory", summary.getTestAssignment().getQuestionBank().getTestCategory().getSubCategory());			
			resultMap.put("questionTaken", summary.getTestAssignment().getQuestionsCount());
			resultMap.put("answeredCorrect", summary.getAnsweredCorrect());
			resultMap.put("answeredWrong", summary.getAnsweredWrong());				
			resultMap.put("unanswered",summary.getUnanswered());	
			resultMap.put("totalMarkObtained", summary.getTotalMarkObtained());
			resultMap.put("timeTaken",ReportUtils.formTimeTaken(summary.getTimeTaken()));		
			resultMap.put("testDate", ReportUtils.formatDate(summary.getCreateDate()));	
			resultMap.put("isDescriptive", testResultSet.contains(summary.getTestAssignment().getId()) ? true : false );	
			return resultMap;
		}).collect(Collectors.toList());	
		Map<String,Object> resultMap = new HashMap<>();
		resultMap.put(VexamineTestConstants.VALUE, dataList);
		resultMap.put(VexamineTestConstants.COUNT,summaryList.getTotalElements());
		return resultMap;
	}
	
	
	/**
	 * Update summary.
	 *
	 * @param testResultList the test result list
	 * @param summaryId the summary id
	 */
	public void updateSummary(List<TestResult> testResultList,Long summaryId) {
		String userName = CookieSessionStorage.get().getUserName();
		Optional<TestResult> testResult = testResultList.stream().findFirst();
		if(!testResult.isPresent()) {
		}
		Map<Boolean, Long> resultData = validateTestResult(testResult.get().getTestAssignment());
		long correct = resultData.containsKey(true) ? resultData.get(true) : 0L;
		long wrong = resultData.containsKey(false) ?  resultData.get(false) : 0L;
		Optional<TestSummary> summaryOption = testSummaryRepository.findById(summaryId);
		if( !summaryOption.isPresent() ) {
			return ;
		}
		TestSummary summary = summaryOption.get();
		 summary.setAnsweredCorrect(correct);
		 summary.setAnsweredWrong(wrong);
		 summary.setUnanswered(testResult.get().getTestAssignment().getQuestionsCount() - (correct + wrong));
		 summary.setUpdateDate(Calendar.getInstance().getTime());
		 summary.setUpdateUser(userName);
	 	 testSummaryRepository.save(summary);
	}


	/**
	 * Summarize test from results.
	 *
	 * @param testAssignment the test assignment
	 */
	public void summarizeTestFromResults(TestAssignment testAssignment) {
		String userName = CookieSessionStorage.get().getUserName();
		Map<Boolean, Long> resultData = validateTestResult(testAssignment);
		long correct = resultData.containsKey(true) ? resultData.get(true) : 0;
		long wrong = resultData.containsKey(false) ?  resultData.get(false) : 0;
		long timeTaken = Math.subtractExact(testAssignment.getTestEndTime().getTime(), testAssignment.getTestStartTime().getTime());
		TestSummary summary = TestSummary.builder()
				.testAssignment(testAssignment)
				.answeredCorrect(correct)
				.answeredWrong(wrong)
				.unanswered(testAssignment.getQuestionsCount() - (correct + wrong))
				.timeTaken(timeTaken)
				.createUser(userName)
				.createDate(new Date())
				.build();
		testSummaryRepository.save(summary);
	}
	
	/**
	 * Gets the summary by test assignment.
	 *
	 * @param testAssignmentIdList the test assignment id list
	 * @return the summary by test assignment
	 */
	public List<TestSummary> getSummaryByTestAssignment(List<Long> testAssignmentIdList) {
		return testSummaryRepository.findByTestAssignmentIdInOrderByCreateDateDesc(testAssignmentIdList);
	}

	/**
	 * Validate test result.
	 *
	 * @param testAssignment the test assignment
	 * @return the map
	 */
	private Map<Boolean, Long> validateTestResult(TestAssignment testAssignment) {
		long testAssignmentId = testAssignment.getId();
		Optional<List<TestResult>> testResultsOpt = testResultRepo.findByTestAssignmentId(testAssignmentId);
		List<TestResult> testResultList = testResultsOpt.isPresent()? testResultsOpt.get():new ArrayList<>();

		Map<Boolean, Long> resultData = testResultList.stream().filter(result -> result.getValidAnswer() != null)
				.collect(Collectors.groupingBy(TestResult::getValidAnswer, Collectors.counting()));
		return resultData;
	}

}
