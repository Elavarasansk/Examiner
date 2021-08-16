package org.web.vexamine.service;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.activity.InvalidActivityException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.web.vexamine.constants.VexamineTestConstants;
import org.web.vexamine.dao.entity.ManagerCredit;
import org.web.vexamine.dao.entity.QuestionAnswer;
import org.web.vexamine.dao.entity.QuestionBank;
import org.web.vexamine.dao.entity.TestAssignment;
import org.web.vexamine.dao.entity.TestResult;
import org.web.vexamine.dao.entity.UserAuthorityInfo;
import org.web.vexamine.dao.enumclass.StatusType;
import org.web.vexamine.dao.repository.QuestionAnswerRepo;
import org.web.vexamine.dao.repository.TestAssignmentRepository;
import org.web.vexamine.dao.repository.TestResultRepository;
import org.web.vexamine.dao.repository.UserAuthorityInfoRepository;
import org.web.vexamine.dao.repository.specification.TestAssignmentSpecification;
import org.web.vexamine.dao.vo.ManagerCreditVo;
import org.web.vexamine.dao.vo.TestAssignmentCandidateFilter;
import org.web.vexamine.dao.vo.TestAssignmentVo;
import org.web.vexamine.utils.CommonFunctions;
import org.web.vexamine.utils.VexamineConstants;
import org.web.vexamine.utils.storage.CookieSessionStorage;

import javassist.NotFoundException;

/**
 * The Class TestAssignmentService.
 */
@Service
public class TestAssignmentService {

	public static final int QUESTION_TIME_LIMIT = 5; // Minutes

	@Autowired
	private TestAssignmentRepository testAssignmentRepository;

	@Autowired
	private QuestionAnswerRepo questionAnswerRepo;

	@Autowired
	private TestResultRepository testResultRepo;

	@Autowired
	private UserAuthorityService userAuthorityService;

	@Autowired
	private ScheduleExecutorService scheduleExecutorService;

	@Autowired
	private QuestionBankService questionBankService;

	@Autowired
	private TestSummaryService testSummaryService;

	@Autowired
	private MailSendingService mailSendingService;

	@Autowired
	private UserAuthorityInfoRepository userAuthorityInfoRepo;

	@Autowired
	private ManagerCreditService managerCreditService;

	private static final DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa");


	/**
	 * Adds the test assignment.
	 *
	 * @param testAssignmentVo
	 *            the test assignment vo
	 * @return the list
	 * @throws Exception 
	 */
	public List<TestAssignment> addTestAssignment(TestAssignmentVo testAssignmentVo) throws Exception {
		QuestionBank questionBank = questionBankService.getQuestionBank(testAssignmentVo.getQuestionBankId());
		List<UserAuthorityInfo> userAuthorityInfo = userAuthorityService
				.getUserAuthorityByMailIdList(testAssignmentVo.getAssigneeList(), VexamineConstants.CANDIDATE_ROLE);
		String sessionUser = !ObjectUtils.isEmpty(CookieSessionStorage.get()) ? CookieSessionStorage.get().getUserName()
				: StringUtils.EMPTY;

		// TODO: Based on client requirement uncomment this section
		/*
		 * if (checkTestAssignmentExists(userAuthorityInfo)) { TestAssignment
		 * testAssignment = findTestAssignmentExists(userAuthorityInfo); String mailId =
		 * testAssignment.getUserAuthorityInfo().getUserCredentials().getMailId();
		 * String questionBankName =
		 * testAssignment.getQuestionBank().getQuestionBankName(); throw new
		 * DuplicateKeyException("User - \"" + mailId + "\" with Test - \"" +
		 * questionBankName + "\" unattended." + " So test cannot be created"); }
		 */


		if (checkTestAssignmentAndQuestionBankExists(userAuthorityInfo,testAssignmentVo.getQuestionBankId())) {
			TestAssignment testAssignment = findTestAssignmentExists(userAuthorityInfo);
			String mailId = testAssignment.getUserAuthorityInfo().getUserCredentials().getMailId();
			String questionBankName = testAssignment.getQuestionBank().getQuestionBankName();
			throw new DuplicateKeyException("User - \"" + mailId + "\" with Test - \"" + questionBankName
					+ "\" unattended." + " So test cannot be created");
		}
		long testCount = Long.valueOf(testAssignmentVo.getAssigneeList().size());
		ManagerCredit managerCredit = managerCreditService.findByCreditByMailId(sessionUser);
		if(Objects.isNull(managerCredit)) {
			throw new Exception("Please purchase credits to assign test.");
		}
		long purchasedCredits = Objects.nonNull(managerCredit.getPurchasedCredits()) ? managerCredit.getPurchasedCredits() : 0 ;
		long usedCredits = Objects.nonNull(managerCredit.getUsedCredits()) ?  managerCredit.getUsedCredits() : 0 ;
		if(purchasedCredits == 0) {
			throw new Exception("Please purchase credits to assign test.");
		}
		if(Math.addExact(usedCredits, testCount) > purchasedCredits) {
			throw new Exception("Your credit limit exceeds...! "+" Please purchase credits to assign test");
		}


		Map<String, UserAuthorityInfo> resultMap = new HashMap<>();
		userAuthorityInfo.stream().forEach(user -> {
			resultMap.put(user.getUserCredentials().getMailId(), user);
		});
		List<TestAssignment> testAssignmentList = testAssignmentVo.getAssigneeList().stream().map(user -> {
			return TestAssignment.builder().createUser(sessionUser).updateUser(sessionUser)
					.allowedTime(testAssignmentVo.getAllowedTime())
					.expirationTime(testAssignmentVo.getExpirationTime()).status(StatusType.NEW.getType())
					.inviteSent(false).expired(false).questionBank(questionBank)
					.questionsCount(testAssignmentVo.getQuestionsCount()).userAuthorityInfo(resultMap.get(user))
					.allowedStartDate(testAssignmentVo.getAllowedStartDate()).build();
		}).collect(Collectors.toList());
		List<TestAssignment> resultList = testAssignmentRepository.saveAll(testAssignmentList);
		dispatchMailSendThread(testAssignmentVo, resultList, questionBank);
		List<Long> assignList = resultList.stream().map(TestAssignment::getId).collect(Collectors.toList());
		scheduleExecutorService.schedule(sessionUser, testAssignmentVo.getExpirationTime(), assignList,
				testAssignmentVo.getQuestionBankName());
		managerCreditService.updateCredit(ManagerCreditVo.builder().usedCredits(Long.valueOf(resultList.size())).build());
		return resultList;
	}


	/**
	 * Check test assignment exists.
	 *
	 * @param userAuthorityInfo the user authority info
	 * @param questionBankName the question bank name
	 * @return true, if successful
	 */
	private boolean checkTestAssignmentAndQuestionBankExists(List<UserAuthorityInfo> userAuthorityInfo,Long questionBankId) {
		List<Long> userAuthorityList = userAuthorityInfo.stream().map(UserAuthorityInfo::getId)
				.collect(Collectors.toList());
		return testAssignmentRepository.existsByUserAuthorityInfoIdInAndQuestionBankIdAndStatusIn(userAuthorityList,
				questionBankId,Arrays.asList(StatusType.NEW.getType(), StatusType.INPROGRESS.getType()));
	}


	/**
	 * Check test assignment exists.
	 *
	 * @param userAuthorityInfo
	 *            the user authority info
	 * @return true, if successful
	 */
	private boolean checkTestAssignmentExists(List<UserAuthorityInfo> userAuthorityInfo) {
		List<Long> userAuthorityList = userAuthorityInfo.stream().map(UserAuthorityInfo::getId)
				.collect(Collectors.toList());
		return testAssignmentRepository.existsByUserAuthorityInfoIdInAndStatusIn(userAuthorityList,
				Arrays.asList(StatusType.NEW.getType(), StatusType.INPROGRESS.getType()));
	}

	/**
	 * Find test assignment exists.
	 *
	 * @param userAuthorityInfo
	 *            the user authority info
	 * @return the test assignment
	 */
	private TestAssignment findTestAssignmentExists(List<UserAuthorityInfo> userAuthorityInfo) {
		List<Long> userAuthorityList = userAuthorityInfo.stream().map(UserAuthorityInfo::getId)
				.collect(Collectors.toList());
		List<TestAssignment> assignList = testAssignmentRepository.findByUserAuthorityInfoIdInAndStatusIn(
				userAuthorityList, Arrays.asList(StatusType.NEW.getType(), StatusType.INPROGRESS.getType()));
		return assignList.stream().findFirst().orElse(new TestAssignment());

	}

	/**
	 * Delete test assignment.
	 *
	 * @param id
	 *            the id
	 */
	public void deleteTestAssignment(Long id) {
		testAssignmentRepository.deleteById(id);
	}

	/**
	 * Gets the count by user id.
	 *
	 * @param id
	 *            the id
	 * @return the count by user id
	 */
	public Long getCountByUserId(Long id) {
		return testAssignmentRepository.countByUserAuthorityInfoId(id);
	}

	/**
	 * Gets the test assignment.
	 *
	 * @param id
	 *            the id
	 * @return the test assignment
	 */
	public TestAssignment getTestAssignment(Long id) {
		Optional<TestAssignment> testAssignment = testAssignmentRepository.findById(id);
		return testAssignment.isPresent() ? testAssignment.get() : null;
	}

	/**
	 * Gets the test assignment list.
	 *
	 * @param testAssignmentVo
	 *            the test assignment vo
	 * @return the test assignment list
	 */
	public Page<TestAssignment> getTestAssignmentList(TestAssignmentVo testAssignmentVo) {
		Pageable pageable = PageRequest.of(testAssignmentVo.getOffset(), testAssignmentVo.getLimit());
		TestAssignmentSpecification spec = new TestAssignmentSpecification(testAssignmentVo);
		Page<TestAssignment> testAssignment = testAssignmentRepository.findAll(spec, pageable);
		return testAssignment;
	}

	/**
	 * Gets the by test status.
	 *
	 * @param status
	 *            the status
	 * @return the by test status
	 */
	public List<TestAssignment> getByTestStatus(String status) {
		return testAssignmentRepository.findByStatus(status);
	}

	/**
	 * Update expired status.
	 *
	 * @param assigneeListList
	 *            the assignee list list
	 */
	public void updateExpiredStatus(List<Long> assigneeList) {
		List<TestAssignment> list = testAssignmentRepository.findByIdInAndStatus(assigneeList, StatusType.NEW.getType());
		if(CollectionUtils.isEmpty(list)) {
			return;
		}
		list.stream().forEach(data->{
			data.setStatus(StatusType.EXPIRED.getType());
			data.setUpdateDate(new Date());
		});		
		Map<String, Long> dataMap = list.stream().collect(Collectors.groupingBy(TestAssignment::getCreateUser, Collectors.counting()));
		managerCreditService.addCredit(dataMap);
		testAssignmentRepository.saveAll(list);
	}

	/**
	 * Gets the my assigned test.
	 *
	 * @return the my assigned test
	 */
	public TestAssignment getMyAssignedTest() {
		String loggedInUser = CookieSessionStorage.get().getUserName();
		UserAuthorityInfo userAuthInfo = userAuthorityService.getUserByMailId(loggedInUser);
		List<TestAssignment> myAssignedTests = testAssignmentRepository
				.findByUserAuthorityInfoIdOrderByCreateDateDesc(userAuthInfo.getId());

		// myAssignedTests = myAssignedTests.stream().filter(rec ->
		// rec.getStatus().equalsIgnoreCase(StatusType.NEW.getType())
		// ||
		// rec.getStatus().equalsIgnoreCase(StatusType.INPROGRESS.getType())).collect(Collectors.toList());

		if (CollectionUtils.isEmpty(myAssignedTests)) {
			return TestAssignment.builder().build();
		}
		return myAssignedTests.get(0);
	}

	/**
	 * Gets the all my assigned tests.
	 *
	 * @param filter
	 *            the filter
	 * @return the all my assigned tests
	 */
	public List<TestAssignment> getAllMyAssignedTests(TestAssignmentCandidateFilter filter) {
		String loggedInUser = CookieSessionStorage.get().getUserName();
		UserAuthorityInfo userAuthInfo = userAuthorityService.getUserByMailId(loggedInUser);
		return testAssignmentRepository.findCandidateSpecific(userAuthInfo.getId(),
				containsJoiner(StringUtils.isEmpty(filter.getCategory()) ? StringUtils.EMPTY : filter.getCategory()));
	}


	public void updateTestResults(UserAuthorityInfo userAuthInfo) {
		List<TestAssignment> testAssignmentList = testAssignmentRepository.
				findByUserAuthorityInfoIdInAndStatusIn(Arrays.asList(userAuthInfo.getId()),
						Arrays.asList(StatusType.NEW.getType(),StatusType.INPROGRESS.getType()));
		testAssignmentList.stream().forEach(data ->{
			Calendar now = Calendar.getInstance();
			long allowedTime = data.getAllowedTime();	
			if( Objects.nonNull(data.getTestStartTime())) { 
				now.setTimeInMillis(data.getTestStartTime().getTime());
				now.add(Calendar.SECOND,(int)allowedTime );
				Timestamp endTime = new Timestamp(now.getTime().getTime());	
				Calendar current = Calendar.getInstance();
				if(new Timestamp(current.getTime().getTime()).after(endTime)) {
					data.setStatus(StatusType.EXPIRED.getType());
					data.setExpired(true);
					data.setUpdateDate(new Date());
				}
			}else {
				Timestamp currentTime = new Timestamp(now.getTime().getTime());	
				if(currentTime.after(data.getExpirationTime())) {
					data.setStatus(StatusType.EXPIRED.getType());
					data.setExpired(true);
					data.setUpdateDate(new Date());
				}				
			}
		});
		
		List<TestAssignment> insertList = testAssignmentList.stream().filter(data->data.getStatus().equals(StatusType.EXPIRED.getType()))
				.collect(Collectors.toList());
		if(CollectionUtils.isNotEmpty(insertList)) {
			testAssignmentRepository.saveAll(insertList);
		}		
	}


	/**
	 * Contains joiner.
	 *
	 * @param value
	 *            the value
	 * @return the string
	 */
	private String containsJoiner(String value) {
		return String.format("%%%s%%", value.toLowerCase());
	}

	/**
	 * Start assigned test.
	 *
	 * @param id
	 *            the id
	 * @return the test assignment
	 * @throws Exception 
	 */
	public TestAssignment startAssignedTest(long id) throws Exception {
		TestAssignment assignedTest = getAssignedTest(id);
		String loggedInUser = CookieSessionStorage.get().getUserName();
		Calendar now = Calendar.getInstance();
		if(assignedTest.getAllowedStartDate().after(new Timestamp(now.getTime().getTime()))) {
			ZonedDateTime zonedDateTime = assignedTest.getAllowedStartDate().toLocalDateTime().atZone(ZoneId.systemDefault());
			throw new Exception("\""+assignedTest.getQuestionBank().getQuestionBankName() +"\" test schedule to start at \""+ format.format(Date.from(zonedDateTime.toInstant()))+"\"");
		}

		if (!loggedInUser.equals(assignedTest.getUserAuthorityInfo().getUserCredentials().getMailId())) {
			throw new InvalidActivityException("Unauthorized Usage. Please Check");
		}

		assignedTest.setTestStartTime(new Timestamp(now.getTime().getTime()));
		long newExpiryTime = assignedTest.getAllowedTime();
		now.add(Calendar.SECOND, (int) newExpiryTime);
		assignedTest.setExpirationTime(new Timestamp(now.getTime().getTime()));
		assignedTest.setStatus(StatusType.INPROGRESS.getType());
		assignedTest.setUpdateUser(loggedInUser);
		assignQuestionsToCandidate(assignedTest);

		return testAssignmentRepository.save(assignedTest);
	}

	/**
	 * Assign questions to candidate.
	 *
	 * @param assignedTest
	 *            the assigned test
	 */
	public void assignQuestionsToCandidate(TestAssignment assignedTest) {
		long qbId = assignedTest.getQuestionBank().getId();
		long qnsCount = assignedTest.getQuestionsCount();
		Optional<List<QuestionAnswer>> questionAnswerOpt = questionAnswerRepo.findByQuestionBankIdOrderByIdDesc(qbId);

		List<QuestionAnswer> questionAnswerList = questionAnswerOpt.isPresent() ? questionAnswerOpt.get() : null;

		if (CollectionUtils.isEmpty(questionAnswerList)) {
			try {
				throw new NotFoundException("Questions doesnot Exists");
			} catch (NotFoundException e) {
				e.printStackTrace();
			}
		}
		List<Long> qaIds = questionAnswerList.stream().map(QuestionAnswer::getId).collect(Collectors.toList());
		long upperBound = qaIds.get(0);
		long lowerBound = qaIds.get(qaIds.size() - 1);
		Set<Long> questionsList = CommonFunctions.getRandomList((int) qnsCount, (int) lowerBound, (int) upperBound,
				qaIds);

		List<QuestionAnswer> qaList = questionAnswerList.stream().filter(qa -> questionsList.contains(qa.getId()))
				.collect(Collectors.toList());
		assignTests(qaList, assignedTest);
	}

	/**
	 * Assign tests.
	 *
	 * @param questionAnswerList
	 *            the question answer list
	 * @param assignedTest
	 *            the assigned test
	 */
	public void assignTests(List<QuestionAnswer> questionAnswerList, TestAssignment assignedTest) {
		String logggedInUser = CookieSessionStorage.get().getUserName();
		UserAuthorityInfo userAuthInfo = userAuthorityInfoRepo.findByUserCredentialsMailId(logggedInUser);
		List<TestResult> testResultList = questionAnswerList.stream()
				.map(qa -> TestResult.builder().questionAnswer(qa).createUser(logggedInUser).createDate(new Date())
						.testAssignment(assignedTest).userAuthorityInfo(userAuthInfo).build())
				.collect(Collectors.toList());
		testResultRepo.saveAll(testResultList);
	}

	/**
	 * Gets the assigned test.
	 *
	 * @param id
	 *            the id
	 * @return the assigned test
	 */
	private TestAssignment getAssignedTest(long id) {
		Optional<TestAssignment> optAssignedTest = testAssignmentRepository.findById(id);
		TestAssignment assignedTest = optAssignedTest.isPresent() ? optAssignedTest.get() : null;

		if (ObjectUtils.isEmpty(assignedTest)) {
			try {
				throw new NotFoundException("Assignment Not found. Please Check.");
			} catch (NotFoundException e) {
				e.printStackTrace();
			}
		}
		return assignedTest;
	}

	/**
	 * Complete test.
	 *
	 * @param id
	 *            the id
	 * @return the test assignment
	 */
	public TestAssignment completeTest(long id) {
		String logggedInUser = CookieSessionStorage.get().getUserName();
		Date currDate = new Date();
		TestAssignment assignedTest = getAssignedTest(id);
		assignedTest.setExpired(false);
		assignedTest.setExpirationTime(null);
		assignedTest.setStatus(VexamineConstants.COMPLETE_STATUS);
		assignedTest.setTestEndTime(new Timestamp(currDate.getTime()));
		assignedTest.setUpdateUser(logggedInUser);
		assignedTest = testAssignmentRepository.save(assignedTest);
		testSummaryService.summarizeTestFromResults(assignedTest);
		return assignedTest;
	}

	/**
	 * Expire test.
	 *
	 * @param id
	 *            the id
	 * @return the test assignment
	 */
	public TestAssignment expireTest(long id) {
		String logggedInUser = CookieSessionStorage.get().getUserName();
		TestAssignment assignedTest = getAssignedTest(id);
		assignedTest.setExpired(true);
		assignedTest.setStatus(VexamineConstants.EXPIRED_STATUS);
		assignedTest.setUpdateUser(logggedInUser);
		assignedTest = testAssignmentRepository.save(assignedTest);
		testSummaryService.summarizeTestFromResults(assignedTest);
		return assignedTest;
	}

	/**
	 * Do reject.
	 *
	 * @param rejectList
	 *            the reject list
	 * @return the integer
	 */
	public Integer doReject(List<Long> rejectList) {
		int rejectedCount = testAssignmentRepository.updateRejectStatus(rejectList);
		managerCreditService.addCredit(ManagerCreditVo.builder().usedCredits(Long.valueOf(rejectedCount)).build());
		return rejectedCount;
	}

	/**
	 * Gets the test by question bank name.
	 *
	 * @param questionBankName
	 *            the question bank name
	 * @param expirationDate
	 *            the expiration date
	 * @return the test by question bank name
	 */
	public List<TestAssignment> getTestByQuestionBankName(String questionBankName, Timestamp expirationDate) {
		return testAssignmentRepository.findByQuestionBankQuestionBankNameAndExpirationTime(questionBankName,
				expirationDate);
	}

	/**
	 * Dispatch mail send thread.
	 *
	 * @param testAssignmentVo
	 *            the test assignment vo
	 * @param resultList
	 * @param questionBank
	 *            the question bank
	 * @param resultMap
	 *            the result map
	 */
	private void dispatchMailSendThread(TestAssignmentVo testAssignmentVo, List<TestAssignment> testAssignmentList,
			QuestionBank questionBank) {
		if (testAssignmentVo.getInviteSent() == null || !testAssignmentVo.getInviteSent()) {
			return;
		}
		testAssignmentList.stream().forEach(testAssignment -> {
			Thread mailThread = new Thread(new Runnable() {
				@Override
				public void run() {
					boolean mailSent = mailSendingService.sendTestAssignmentMail(
							testAssignment.getUserAuthorityInfo().getUserCredentials().getMailId(),
							questionBank.getTestCategory().getCategory(),
							testAssignmentVo.getExpirationTime().toGMTString());
					testAssignment.setInviteSent(mailSent);
					testAssignmentRepository.save(testAssignment);
				}
			});
			mailThread.start();
		});
	}
}
