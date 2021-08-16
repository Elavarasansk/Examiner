package org.web.vexamine.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web.vexamine.dao.entity.QuestionAnswer;
import org.web.vexamine.dao.vo.QaFilterVo;
import org.web.vexamine.dao.vo.QuestionAnswerSingleVo;
import org.web.vexamine.dao.vo.QuestionAnswerVo;
import org.web.vexamine.service.QuestionAnswerService;

/**
 * The Class QuestionAnswerController.
 */
@RestController
@RequestMapping("/question/answer")
public class QuestionAnswerController {

	@Autowired
	private QuestionAnswerService questionAnswerService;

	/**
	 * Adds the question answer.
	 *
	 * @param questionAnswerVo
	 *            the question answer vo
	 * @return the question answer
	 */
	@PostMapping("/add")
	public QuestionAnswer addQuestionAnswer(@RequestBody QuestionAnswerVo questionAnswerVo) {
		return questionAnswerService.addQuestionAnswer(questionAnswerVo);
	}

	/**
	 * Adds the all question answer.
	 *
	 * @param questionAnswerVoList
	 *            the question answer vo list
	 * @return the list
	 */
	@PostMapping("/add/all")
	public List<QuestionAnswer> addAllQuestionAnswer(@RequestBody List<QuestionAnswerVo> questionAnswerVoList) {
		return questionAnswerService.addAllQuestionAnswer(questionAnswerVoList);
	}

	/**
	 * Delete question answer.
	 *
	 * @param id
	 *            the id
	 */
	@DeleteMapping("/delete/{id}")
	public void deleteQuestionAnswer(@PathVariable(value = "id") long id) {
		questionAnswerService.deleteQuestionAnswer(id);
	}

	/**
	 * Delete by question bank id.
	 *
	 * @param id
	 *            the id
	 */
	@DeleteMapping("/delete/question/bank/{id}")
	public void deleteByQuestionBankId(@PathVariable(value = "id") long id) {
		questionAnswerService.deleteByQuestionBankId(id);
	}

	/**
	 * Update question answer.
	 *
	 * @param questionAnswerVo
	 *            the question answer vo
	 * @return the question answer
	 */
	@PostMapping("/update")
	public QuestionAnswer updateQuestionAnswer(@RequestBody QuestionAnswerVo questionAnswerVo) {
		return questionAnswerService.updateQuestionAnswer(questionAnswerVo);
	}

	/**
	 * Update all question answer.
	 *
	 * @param questionAnswerVoList
	 *            the question answer vo list
	 * @return the list
	 */
	@PostMapping("/update/all")
	public List<QuestionAnswer> updateAllQuestionAnswer(@RequestBody List<QuestionAnswerVo> questionAnswerVoList) {
		return questionAnswerService.updateAllQuestionAnswer(questionAnswerVoList);
	}

	/**
	 * Gets the by question bank.
	 *
	 * @return the by question bank
	 */
	@GetMapping("/count")
	public long getByQuestionBank() {
		return questionAnswerService.getQuestionAnswerCount();
	}

	/**
	 * Gets the count by question bank.
	 *
	 * @param questionBankId
	 *            the question bank id
	 * @return the count by question bank
	 */
	@GetMapping("/count/{questionBankId}")
	public long getCountByQuestionBank(@PathVariable(value = "questionBankId") Long questionBankId) {
		return questionAnswerService.getCountByQuestionBank(questionBankId);
	}

	/**
	 * Gets the count by question bank.
	 *
	 * @param questionBankName
	 *            the question bank name
	 * @return the count by question bank
	 */
	@GetMapping("name/count/{questionBankName}")
	public long getCountByQuestionBank(@PathVariable(value = "questionBankName") String questionBankName) {
		return questionAnswerService.getCountByQuestionBankName(questionBankName);
	}

	/**
	 * Gets the by question bank.
	 *
	 * @param questionAnswerVo
	 *            the question answer vo
	 * @return the by question bank
	 */
	@PostMapping("/find/question/bank")
	public List<QuestionAnswer> getByQuestionBank(@RequestBody QuestionAnswerVo questionAnswerVo) {
		return questionAnswerService.getByQuestionBank(questionAnswerVo);
	}

	/**
	 * Search question answer.
	 *
	 * @param questionAnswerVo
	 *            the question answer vo
	 * @return the map
	 */
	@PostMapping("/find")
	public Map<String, Object> searchQuestionAnswer(@RequestBody QuestionAnswerVo questionAnswerVo) {
		return questionAnswerService.searchQuestionAnswer(questionAnswerVo);
	}

	/**
	 * Gets the all question answer.
	 *
	 * @param questionAnswerVo
	 *            the question answer vo
	 * @return the all question answer
	 */
	@PostMapping("/find/all")
	public Map<String, Object> getAllQuestionAnswer(@RequestBody QuestionAnswerVo questionAnswerVo) {
		return questionAnswerService.getAllQuestionAnswer(questionAnswerVo);
	}

	/**
	 * Find by paging filter.
	 *
	 * @param qaFilter
	 *            the qa filter
	 * @param pageable
	 *            the pageable
	 * @return the page
	 */
	@PostMapping("find/paged")
	public Page<QuestionAnswer> findByPagingFilter(@RequestBody QaFilterVo qaFilter,
	        @PageableDefault(value = 10, page = 0, sort = "id") Pageable pageable) {
		return questionAnswerService.findAllQuestionAnswers(qaFilter, pageable);
	}

	/**
	 * Do update answer edit mode.
	 *
	 * @param questionAnswerVo
	 *            the question answer vo
	 * @return the question answer
	 */
	@PostMapping("question/editmode")
	public QuestionAnswer doUpdateAnswerEditMode(@RequestBody QuestionAnswerVo questionAnswerVo) {
		return questionAnswerService.doUpdateAnswerEditMode(questionAnswerVo);
	}

	/**
	 * add a single question
	 * 
	 * @param questionAnswerSingleVo
	 * @return question answer
	 */
	@PostMapping("/addsingle")
	public QuestionAnswer addQuestionAnswerSingle(@RequestBody QuestionAnswerSingleVo questionAnswerSingleVo) {
		return questionAnswerService.addSingleQuestionAnswer(questionAnswerSingleVo);
	}

}