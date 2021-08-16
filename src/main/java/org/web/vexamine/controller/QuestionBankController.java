package org.web.vexamine.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.web.vexamine.dao.entity.QuestionBank;
import org.web.vexamine.dao.vo.QuestionBankFilterVo;
import org.web.vexamine.dao.vo.QuestionBankVo;
import org.web.vexamine.service.QuestionBankService;

/**
 * The Class QuestionBankController.
 */
@RestController
@RequestMapping("/question/bank")
public class QuestionBankController {

	@Autowired
	private QuestionBankService questionBankService;

	/**
	 * Adds the question bank.
	 *
	 * @param questionBankVo the question bank vo
	 * @return the question bank
	 */
	@PostMapping("/add")
	public QuestionBank addQuestionBank(@RequestBody QuestionBankVo questionBankVo) {
		return questionBankService.addQuestionBank(questionBankVo);
	}

	/**
	 * Delete question bank.
	 *
	 * @param id the id
	 */
	@DeleteMapping("/delete/{id}")
	public void deleteQuestionBank(@PathVariable(value = "id") long id) {
		questionBankService.deleteQuestionBank(id);
	}

	/**
	 * Gets the question bank count.
	 *
	 * @return the question bank count
	 */
	@GetMapping("/count")
	public long getQuestionBankCount() {
		return questionBankService.getQuestionBankCount();
	}

	/**
	 * Gets the all question bank.
	 *
	 * @param questionBankVo the question bank vo
	 * @return the all question bank
	 */
	@PostMapping("/find/all")
	public Map<String, Object> getAllQuestionBank(@RequestBody QuestionBankVo questionBankVo) {
		return questionBankService.getAllQuestionBank(questionBankVo);
	}

	/**
	 * Gets the all question bank.
	 *
	 * @return the all question bank
	 */
	@GetMapping("/find/all/question")
	public List<String> getAllQuestionBank() {
		return questionBankService.findAllQuestionBank();
	}

	/**
	 * Gets the question bank.
	 *
	 * @param id the id
	 * @return the question bank
	 */
	@GetMapping("/find/{id}")
	public QuestionBank getQuestionBank(@PathVariable(value = "id") long id) {
		return questionBankService.getQuestionBank(id);
	}

	/**
	 * Gets the question bank.
	 *
	 * @param questionBankName the question bank name
	 * @return the question bank
	 */
	@GetMapping("/find")
	public QuestionBank getQuestionBank(@RequestParam String questionBankName) {
		return questionBankService.getQuestionBank(questionBankName);
	}

	/**
	 * Search question bank by category.
	 *
	 * @param questionBankVo the question bank vo
	 * @return the list
	 */
	@PostMapping("category/search")
	public List<QuestionBank> searchQuestionBankByCategory(@RequestBody QuestionBankVo questionBankVo) {
		return questionBankService.searchQuestionBankByCategory(questionBankVo);
	}

	/**
	 * Search question bank by name.
	 *
	 * @param questionBankVo the question bank vo
	 * @return the list
	 */
	@PostMapping("name/search")
	public List<String> searchQuestionBankByName(@RequestBody QuestionBankVo questionBankVo) {
		return questionBankService.searchQuestionBankByName(questionBankVo);
	}

	/**
	 * Gets the qb template.
	 *
	 * @param response the response
	 * @return the qb template
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@PostMapping("/download/template")
	public void getQbTemplate(HttpServletResponse response) throws IOException {
		questionBankService.getQbTemplate(response);
	}
	
	/**
	 * Upload question bank.
	 *
	 * @param file the file
	 * @param category the category
	 * @param subCategory the sub category
	 * @param questionBankName the question bank name
	 * @return the map
	 * @throws IllegalStateException the illegal state exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@PostMapping("/upload")
	public Map<String, Set<String>> uploadQuestionBank(@RequestParam("file") MultipartFile file, @RequestParam String category,
			@RequestParam String subCategory, @RequestParam String questionBankName)
					throws IllegalStateException, IOException {
		questionBankService.uploadQuestionBank(file, category, subCategory, questionBankName);
		return findQuestionBankAndGroup();
	}

	/**
	 * Adds the question bank bulk.
	 *
	 * @param file the file
	 * @param selectedCategory the selected category
	 * @param selectedSubCategory the selected sub category
	 * @param questionBankName the question bank name
	 * @throws IllegalStateException the illegal state exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@PostMapping("QB/bulkupload")
	public void addQuestionBankBulk(@RequestParam("file") MultipartFile file, @RequestParam String selectedCategory,
			@RequestParam String selectedSubCategory, @RequestParam String questionBankName)
					throws IllegalStateException, IOException {
		questionBankService.addQuestionBankBulk(file, selectedCategory, selectedSubCategory, questionBankName);
	}

	/**
	 * Find question bank and group.
	 *
	 * @return the map
	 */
	@GetMapping("/groups")
	public Map<String, Set<String>> findQuestionBankAndGroup() {
		return questionBankService.findQuestionBankGroup();
	}
	
	
	/**
	 * Find question bank and group.
	 *
	 * @return the map
	 */
	@GetMapping("/groups/cascade")
	public List<Map<String,Object>> findQuestionBankCascade() {
		return questionBankService.findQuestionBankCascade();
	}
	
	/**
	 * Find question bank and group.
	 *
	 * @param qbFilter the qb filter
	 * @return the list
	 */
	@PostMapping("/filter")
	public List<QuestionBank> findQuestionBankAndGroup(@RequestBody QuestionBankFilterVo qbFilter) {
		return questionBankService.filterAndFetchQuestionBank(qbFilter);
	}

}

