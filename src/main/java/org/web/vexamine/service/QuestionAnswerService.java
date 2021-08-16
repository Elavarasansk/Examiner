package org.web.vexamine.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.web.vexamine.constants.VexamineTestConstants;
import org.web.vexamine.dao.entity.QuestionAnswer;
import org.web.vexamine.dao.entity.QuestionBank;
import org.web.vexamine.dao.entity.TestCategory;
import org.web.vexamine.dao.repository.QuestionAnswerRepo;
import org.web.vexamine.dao.repository.QuestionBankRepo;
import org.web.vexamine.dao.repository.TestCategoryRepo;
import org.web.vexamine.dao.repository.specification.QuestionAnswerSpecification;
import org.web.vexamine.dao.vo.QaFilterVo;
import org.web.vexamine.dao.vo.QuestionAnswerSingleVo;
import org.web.vexamine.dao.vo.QuestionAnswerVo;
import org.web.vexamine.utils.storage.CookieSessionStorage;

/**
 * The Class QuestionAnswerService.
 */
@Service
public class QuestionAnswerService {

	/** The question answer repo. */
	@Autowired
	private QuestionAnswerRepo questionAnswerRepo;

	/** The question bank service. */
	@Autowired
	private QuestionBankService questionBankService;

	@Autowired
	private TestCategoryRepo testCategoryRepo;

	@Autowired
	private QuestionBankRepo questionBankRepo;

	/**
	 * Adds the question answer.
	 *
	 * @param questionAnswerVo
	 *            the question answer vo
	 * @return the question answer
	 */
	public QuestionAnswer addQuestionAnswer(QuestionAnswerVo questionAnswerVo) {
		String sessionUser = !ObjectUtils.isEmpty(CookieSessionStorage.get()) ? CookieSessionStorage.get().getUserName()
		        : StringUtils.EMPTY;
		QuestionBank questionBank = questionBankService.getQuestionBank(questionAnswerVo.getQuestionBankId());
		QuestionAnswer questionAnswer = QuestionAnswer.builder().questionBank(questionBank).createUser(sessionUser)
		        .updateUser(sessionUser).build();
		BeanUtils.copyProperties(questionAnswerVo, questionAnswer);
		return questionAnswerRepo.save(questionAnswer);
	}

	/**
	 * Adds the all question answer.
	 *
	 * @param questionAnswerVoList
	 *            the question answer vo list
	 * @return the list
	 */
	public List<QuestionAnswer> addAllQuestionAnswer(List<QuestionAnswerVo> questionAnswerVoList) {
		List<QuestionAnswer> questionAnswerList = new ArrayList<>();
		List<String> questionBankList = questionAnswerVoList.stream().map(mapper -> mapper.getQuestionBankName())
		        .collect(Collectors.toList());
		Map<String, QuestionBank> questionBankMap = new HashMap<>();
		questionBankService.getQuestionBankList(questionBankList).stream().forEach(questionBank -> {
			questionBankMap.put(questionBank.getQuestionBankName(), questionBank);
		});
		String sessionUser = !ObjectUtils.isEmpty(CookieSessionStorage.get()) ? CookieSessionStorage.get().getUserName()
		        : StringUtils.EMPTY;
		for (QuestionAnswerVo questionAnswerVo : questionAnswerVoList) {
			QuestionAnswer questionAnswer = QuestionAnswer.builder()
			        .questionBank(questionBankMap.get(questionAnswerVo.getQuestionBankName())).createUser(sessionUser)
			        .updateUser(sessionUser).build();
			BeanUtils.copyProperties(questionAnswerVo, questionAnswer);
			questionAnswerList.add(questionAnswer);
		}
		return questionAnswerRepo.saveAll(questionAnswerList);
	}

	/**
	 * Delete question answer.
	 *
	 * @param id
	 *            the id
	 */
	public void deleteQuestionAnswer(Long id) {
		questionAnswerRepo.deleteById(id);
	}

	/**
	 * Delete by question bank id.
	 *
	 * @param id
	 *            the id
	 */
	public void deleteByQuestionBankId(Long id) {
		questionAnswerRepo.deleteByQuestionBankId(id);
	}

	/**
	 * Update question answer.
	 *
	 * @param questionAnswerVo
	 *            the question answer vo
	 * @return the question answer
	 */
	public QuestionAnswer updateQuestionAnswer(QuestionAnswerVo questionAnswerVo) {
		String sessionUser = !ObjectUtils.isEmpty(CookieSessionStorage.get()) ? CookieSessionStorage.get().getUserName()
		        : StringUtils.EMPTY;
		QuestionBank questionBank = questionBankService.getQuestionBank(questionAnswerVo.getQuestionBankId());
		QuestionAnswer questionAnswer = QuestionAnswer.builder().questionBank(questionBank).updateUser(sessionUser)
		        .build();
		BeanUtils.copyProperties(questionAnswerVo, questionAnswer);
		return questionAnswerRepo.save(questionAnswer);
	}

	/**
	 * Update all question answer.
	 *
	 * @param questionAnswerVoList
	 *            the question answer vo list
	 * @return the list
	 */
	public List<QuestionAnswer> updateAllQuestionAnswer(List<QuestionAnswerVo> questionAnswerVoList) {
		List<QuestionAnswer> questionAnswerList = new ArrayList<>();
		List<String> questionBankList = questionAnswerVoList.stream().map(mapper -> mapper.getQuestionBankName())
		        .collect(Collectors.toList());
		Map<String, QuestionBank> questionBankMap = new HashMap<>();
		questionBankService.getQuestionBankList(questionBankList).stream().forEach(questionBank -> {
			questionBankMap.put(questionBank.getQuestionBankName(), questionBank);
		});
		String sessionUser = !ObjectUtils.isEmpty(CookieSessionStorage.get()) ? CookieSessionStorage.get().getUserName()
		        : StringUtils.EMPTY;
		for (QuestionAnswerVo questionAnswerVo : questionAnswerVoList) {
			QuestionAnswer questionAnswer = QuestionAnswer.builder()
			        .questionBank(questionBankMap.get(questionAnswerVo.getQuestionBankName())).updateUser(sessionUser)
			        .build();
			BeanUtils.copyProperties(questionAnswerVo, questionAnswer);
			questionAnswerList.add(questionAnswer);
		}
		return questionAnswerRepo.saveAll(questionAnswerList);
	}

	/**
	 * Gets the question answer count.
	 *
	 * @return the question answer count
	 */
	public long getQuestionAnswerCount() {
		return questionAnswerRepo.count();
	}

	/**
	 * Gets the count by question bank.
	 *
	 * @param questionBankId
	 *            the question bank id
	 * @return the count by question bank
	 */
	public long getCountByQuestionBank(Long questionBankId) {
		return questionAnswerRepo.countByQuestionBankId(questionBankId);
	}

	/**
	 * Gets the count by question bank name.
	 *
	 * @param questionBankName
	 *            the question bank name
	 * @return the count by question bank name
	 */
	public long getCountByQuestionBankName(String questionBankName) {
		return questionAnswerRepo.countByQuestionBankQuestionBankName(questionBankName);
	}

	/**
	 * Gets the by question bank.
	 *
	 * @param questionAnswerVo
	 *            the question answer vo
	 * @return the by question bank
	 */
	public List<QuestionAnswer> getByQuestionBank(QuestionAnswerVo questionAnswerVo) {
		Pageable pageable = PageRequest.of(questionAnswerVo.getOffset(), questionAnswerVo.getLimit(),
		        Sort.by(VexamineTestConstants.QUESTION));
		Optional<List<QuestionAnswer>> questionBankList = questionAnswerRepo
		        .findByQuestionBankId(questionAnswerVo.getId(), pageable);
		return questionBankList.isPresent() ? questionBankList.get() : Collections.emptyList();
	}

	/**
	 * Search question answer.
	 *
	 * @param questionAnswerVo
	 *            the question answer vo
	 * @return the map
	 */
	public Map<String, Object> searchQuestionAnswer(QuestionAnswerVo questionAnswerVo) {
		Pageable pageable = PageRequest.of(questionAnswerVo.getOffset(), questionAnswerVo.getLimit(),
		        Sort.by(VexamineTestConstants.QUESTION));
		Page<QuestionAnswer> questionPage = questionAnswerRepo
		        .findByQuestionContainingIgnoreCase(questionAnswerVo.getQuestion(), pageable);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put(VexamineTestConstants.VALUE, questionPage.getContent());
		resultMap.put(VexamineTestConstants.COUNT, questionPage.getTotalElements());
		return resultMap;
	}

	/**
	 * Gets the all question answer.
	 *
	 * @param questionAnswerVo
	 *            the question answer vo
	 * @return the all question answer
	 */
	public Map<String, Object> getAllQuestionAnswer(QuestionAnswerVo questionAnswerVo) {
		Pageable pageable = PageRequest.of(questionAnswerVo.getOffset(), questionAnswerVo.getLimit(),
		        Sort.by(VexamineTestConstants.QUESTION));
		Page<QuestionAnswer> questionPage = questionAnswerRepo.findAll(pageable);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put(VexamineTestConstants.VALUE, questionPage.getContent());
		resultMap.put(VexamineTestConstants.COUNT, questionPage.getTotalElements());
		return resultMap;
	}

	/**
	 * Find all question answers.
	 *
	 * @param qaFilter
	 *            the qa filter
	 * @param pageable
	 *            the pageable
	 * @return the page
	 */
	public Page<QuestionAnswer> findAllQuestionAnswers(QaFilterVo qaFilter, Pageable pageable) {
		QuestionAnswerSpecification x = new QuestionAnswerSpecification(qaFilter);
		Page<QuestionAnswer> pagedQa = questionAnswerRepo.findAll(x, pageable);
		return pagedQa;
		// return pagedQa.getContent();
	}

	/**
	 * Do update answer edit mode.
	 *
	 * @param questionAnswerVo
	 *            the question answer vo
	 * @return the question answer
	 */
	public QuestionAnswer doUpdateAnswerEditMode(QuestionAnswerVo questionAnswerVo) {
		Optional<QuestionAnswer> questionAnswerOpt = questionAnswerRepo.findById(questionAnswerVo.getId());
		if (questionAnswerOpt.isPresent()) {
			QuestionAnswer questionAnswer = questionAnswerOpt.get();
			questionAnswer.setQuestion(questionAnswerVo.getQuestion().trim());
			questionAnswer.setAnswer(questionAnswerVo.getAnswer().trim());
			int questionType = questionAnswerVo.getQuestionType();
			switch (questionType) {
			case 1:
				formTwoChoiceVo(questionAnswer, questionAnswerVo);
				break;
			case 2:
				formMultipleChoiceVo(questionAnswer, questionAnswerVo);
				break;
			default:
				break;
			}
			return questionAnswerRepo.save(questionAnswer);
		}
		return QuestionAnswer.builder().build();
	}

	/**
	 * Form two choice vo.
	 *
	 * @param questionAnswer
	 *            the question answer
	 * @param questionAnswerVo
	 *            the question answer vo
	 */
	private void formTwoChoiceVo(QuestionAnswer questionAnswer, QuestionAnswerVo questionAnswerVo) {
		questionAnswer.setQuestion(questionAnswerVo.getQuestion().trim());
		questionAnswer.setChoiceOption1(questionAnswerVo.getChoiceOption1().trim());
		questionAnswer.setChoiceOption2(questionAnswerVo.getChoiceOption2().trim());
		questionAnswer.setAnswer(questionAnswerVo.getAnswer().trim());
	}

	/**
	 * Form multiple choice vo.
	 *
	 * @param questionAnswer
	 *            the question answer
	 * @param questionAnswerVo
	 *            the question answer vo
	 */
	private void formMultipleChoiceVo(QuestionAnswer questionAnswer, QuestionAnswerVo questionAnswerVo) {
		questionAnswer.setQuestion(questionAnswerVo.getQuestion().trim());
		questionAnswer.setMcqOption1(questionAnswerVo.getMcqOption1().trim());
		questionAnswer.setMcqOption2(questionAnswerVo.getMcqOption2().trim());
		questionAnswer.setMcqOption3(questionAnswerVo.getMcqOption3().trim());
		questionAnswer.setMcqOption4(questionAnswerVo.getMcqOption4().trim());
		questionAnswer.setAnswer(questionAnswerVo.getAnswer().trim());
	}

	/**
	 * add a single question
	 * 
	 * @param questionAnswerSingleVo
	 * @return QuestionAnswer
	 */
	public QuestionAnswer addSingleQuestionAnswer(QuestionAnswerSingleVo questionAnswerSingleVo) {
		String sessionUser = !ObjectUtils.isEmpty(CookieSessionStorage.get()) ? CookieSessionStorage.get().getUserName()
		        : StringUtils.EMPTY;
		QuestionAnswer questionAnswer = QuestionAnswer.builder().createUser(sessionUser).updateUser(sessionUser)
		        .build();
		BeanUtils.copyProperties(questionAnswerSingleVo, questionAnswer);
		TestCategory testCategory = testCategoryRepo.findByCategoryAndSubCategory(questionAnswerSingleVo.getCategory(),
		        questionAnswerSingleVo.getSubCategory());
		QuestionBank questionBank = questionBankRepo.findByTestCategoryIdAndQuestionBankName(testCategory.getId(),
		        questionAnswerSingleVo.getQuestionBankName());
		questionAnswer.setQuestionBank(questionBank);
		return questionAnswerRepo.save(questionAnswer);
	}

}