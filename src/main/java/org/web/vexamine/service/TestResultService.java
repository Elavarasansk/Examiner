package org.web.vexamine.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.web.vexamine.dao.entity.QuestionAnswer;
import org.web.vexamine.dao.entity.TestResult;
import org.web.vexamine.dao.entity.UserAuthorityInfo;
import org.web.vexamine.dao.enumclass.QuestionType;
import org.web.vexamine.dao.repository.QuestionAnswerRepo;
import org.web.vexamine.dao.repository.TestResultRepository;
import org.web.vexamine.dao.repository.UserAuthorityInfoRepository;
import org.web.vexamine.dao.vo.DeclarativeAnswer;
import org.web.vexamine.dao.vo.MyQuestions;
import org.web.vexamine.dao.vo.TestResultVo;
import org.web.vexamine.utils.VexamineConstants;
import org.web.vexamine.utils.storage.CookieSessionStorage;

import javassist.NotFoundException;

/**
 * The Class TestResultService.
 */
@Service
public class TestResultService {

	@Autowired
	private TestResultRepository testResultRepository;

	@Autowired
	private UserAuthorityInfoRepository userAuthorityInfoRepo;

	@Autowired
	private QuestionAnswerRepo questionAnswerRepo;

	/**
	 * Adds the test reult.
	 *
	 * @param testResultVo the test result vo
	 * @return the test result
	 */
	public TestResult addTestReult(TestResultVo testResultVo) {		
		String sessionUser = null;
		if(!ObjectUtils.isEmpty(CookieSessionStorage.get())) {
			sessionUser = CookieSessionStorage.get().getUserName();
		}
		UserAuthorityInfo userAuthorityInfo = userAuthorityInfoRepo.findByUserCredentialsId(testResultVo.getUserAuthId());
		Optional<QuestionAnswer> questionAnswer = questionAnswerRepo.findById(testResultVo.getQuestionId());
		TestResult testResult = TestResult.builder()
				.createUser(sessionUser)
				.updateUser(sessionUser)
				.userAuthorityInfo(userAuthorityInfo)
				.questionAnswer(questionAnswer.isPresent() ? questionAnswer.get() : null )
				.build();
		BeanUtils.copyProperties(testResultVo, testResult);
		return testResultRepository.save(testResult);
	}

	/**
	 * Delete test result.
	 *
	 * @param id the id
	 */
	public void  deleteTestResult(Long id) {	
		testResultRepository.deleteById(id);
	}

	/**
	 * Adds the test reult list.
	 *
	 * @param testResultVoList the test result vo list
	 * @return the list
	 */
	public List<TestResult> addTestReultList(List<TestResultVo> testResultVoList) {	
		String sessionUser = null;
		if(!ObjectUtils.isEmpty(CookieSessionStorage.get())) {
			sessionUser = CookieSessionStorage.get().getUserName();
		}
		List<TestResult> testResultList = Collections.emptyList();
		for( TestResultVo testResultVo :   testResultVoList ) {
			UserAuthorityInfo userAuthorityInfo = userAuthorityInfoRepo.findByUserCredentialsId(testResultVo.getUserAuthId());
			Optional<QuestionAnswer> questionAnswer = questionAnswerRepo.findById(testResultVo.getQuestionId());
			testResultList.add(TestResult.builder()
					.createUser(sessionUser)
					.updateUser(sessionUser)
					.userAuthorityInfo(userAuthorityInfo)
					.questionAnswer(questionAnswer.isPresent() ? questionAnswer.get() : null )
					.build());
		};
		return testResultRepository.saveAll(testResultList);
	}

	/**
	 * Gets the by assignment id optional.
	 *
	 * @param testId the test id
	 * @return the by assignment id optional
	 */
	public List<TestResult> getByAssignmentIdOptional(Long testId) {
		Optional<List<TestResult>> resultList = testResultRepository.findByTestAssignmentId(testId);
		return resultList.isPresent() ? resultList.get() : Collections.emptyList();
	}

	/**
	 * Gets the declarative questions.
	 *
	 * @param assignId the assign id
	 * @return the declarative questions
	 */
	public List<DeclarativeAnswer> getDeclarativeQuestions(Long assignId) {
		Optional<List<TestResult>> testResultOption = testResultRepository.findByTestAssignmentIdAndQuestionAnswerQuestionTypeOrderById(assignId,QuestionType.DECLARATIVE.getType());
		List<TestResult> testResult = testResultOption.isPresent()? testResultOption.get():new ArrayList<>();
		return testResult.stream().map(qa -> {
			DeclarativeAnswer questions = DeclarativeAnswer.builder().build();
			questions.setQuestion(qa.getQuestionAnswer().getQuestion());
			questions.setId(qa.getId());
			questions.setTestAssignmentId(qa.getTestAssignment().getId());
			questions.setAnswer(StringUtils.isNotEmpty( qa.getAnswer()) ?  qa.getAnswer() :  StringUtils.EMPTY);
			questions.setCorrectAnswer( StringUtils.isNotEmpty(qa.getQuestionAnswer().getAnswer()) ?  qa.getQuestionAnswer().getAnswer() :  StringUtils.EMPTY );
			questions.setValidAnswer(BooleanUtils.toBoolean(qa.getValidAnswer()));
			return questions;
		}).collect(Collectors.toList());
	}

	/**
	 * Gets the my questions.
	 *
	 * @param assignId the assign id
	 * @return the my questions
	 */
	public List<MyQuestions> getMyQuestions(Long assignId) {

		Optional<List<TestResult>> testResultOption = testResultRepository.findByTestAssignmentIdOrderById(assignId);

		List<TestResult> testResult = testResultOption.isPresent()? testResultOption.get():new ArrayList<>();

		List<MyQuestions> myQuestionList = testResult.stream().map(qa -> {
			MyQuestions questions = MyQuestions.builder().build();
			BeanUtils.copyProperties(qa.getQuestionAnswer(), questions);
			questions.setResultId(qa.getId());
			questions.setSelectedAnswer(qa.getAnswer());
			return questions;
		}).collect(Collectors.toList());

		return myQuestionList;
	}

	/**
	 * Sets the my answer.
	 *
	 * @param resultId the result id
	 * @param choosenAnswer the choosen answer
	 */
	public void setMyAnswer(Long resultId, String choosenAnswer) {
		String logggedInUser = CookieSessionStorage.get().getUserName();
		Optional<TestResult> testResultOption = testResultRepository.findById(resultId);
		TestResult testResult = testResultOption.isPresent()? testResultOption.get():null;
		if(ObjectUtils.isEmpty(testResult)) {
			try {
				throw new NotFoundException("The result doesnot exists");
			} catch (NotFoundException e) {
				e.printStackTrace();
			}
		}
		testResult.setAnswer(choosenAnswer);
		testResult.setStatus(VexamineConstants.ATTEND_STATUS);
		testResult.setTimeTaken(new Timestamp(Calendar.getInstance().getTime().getTime()));
		testResult.setValidAnswer(testResult.getQuestionAnswer().getAnswer().equals(choosenAnswer));
		testResult.setUpdateUser(logggedInUser);
		testResultRepository.save(testResult);
	}

	/**
	 * Update test result.
	 *
	 * @param testResultVo the test result vo
	 * @return the list
	 */
	public List<TestResult> updateTestResult(TestResultVo testResultVo) {
		String logggedInUser = CookieSessionStorage.get().getUserName();
		List<Long> keysList = testResultVo.getUpdateMap().keySet().stream().map(Long::valueOf).collect(Collectors.toList());		
		List<TestResult>  testResultList = testResultRepository.findAllById(keysList);
		testResultList.stream().forEach(data->{
			if(keysList.contains(data.getId())) {
				data.setValidAnswer(testResultVo.getUpdateMap().get(data.getId()));
				data.setUpdateUser(logggedInUser);
				data.setUpdateDate(Calendar.getInstance().getTime());
			}
		});
		return testResultRepository.saveAll(testResultList);

	}


	public Set<Long> getDescriptiveTypeQuestion(List<Long> testIdList) {
		Optional<List<TestResult>> testResultList = testResultRepository.findByTestAssignmentIdInAndQuestionAnswerQuestionTypeOrderById(testIdList, QuestionType.DECLARATIVE.getType());
		if ( !testResultList.isPresent() ) {
			return Collections.emptySet();
		}
		return  testResultList.get().stream().map(test -> test.getTestAssignment().getId()).collect(Collectors.toSet());
	}


}
