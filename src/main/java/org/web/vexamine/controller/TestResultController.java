package org.web.vexamine.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web.vexamine.dao.entity.TestResult;
import org.web.vexamine.dao.vo.DeclarativeAnswer;
import org.web.vexamine.dao.vo.MyQuestions;
import org.web.vexamine.dao.vo.TestResultVo;
import org.web.vexamine.service.TestResultService;
import org.web.vexamine.service.TestSummaryService;

/**
 * The Class TestResultController.
 */
@RestController
@RequestMapping("/test/result")
public class TestResultController {

	@Autowired
	private TestResultService testResultService;
	
	@Autowired
	private TestSummaryService testSummaryService;
	
	/**
	 * Adds the test reult.
	 *
	 * @param testResultVo the test result vo
	 * @return the test result
	 */
	@PostMapping("/add")
	public TestResult addTestReult(@RequestBody TestResultVo testResultVo){
		return testResultService.addTestReult(testResultVo);
	}

	/**
	 * Adds the test reult list.
	 *
	 * @param testResultVoList the test result vo list
	 * @return the list
	 */
	@PostMapping("/add/all")
	public List<TestResult> addTestReultList(@RequestBody List<TestResultVo> testResultVoList){
		return testResultService.addTestReultList(testResultVoList);
	}

	/*@GetMapping("/assignment/{assignId}")
	public List<TestResultInfo> getByAssignmentId(@PathVariable(value="assignId") long assignId){
		return testResultService.getByAssignmentId(assignId);
	}*/

	/**
	 * Delete test result.
	 *
	 * @param id the id
	 */
	@DeleteMapping("/delete/{id}")
	public void  deleteTestResult(@PathVariable(value="id") long id){
		testResultService.deleteTestResult(id);
	}

	/**
	 * Gets the assigned questions.
	 *
	 * @param assignId the assign id
	 * @return the assigned questions
	 */
	@GetMapping("/my/questions/{assignId}")
	public List<MyQuestions> getAssignedQuestions(@PathVariable(value="assignId") long assignId) {
		return testResultService.getMyQuestions(assignId);
	}
	
	/**
	 * Gets the declarative questions.
	 *
	 * @param assignId the assign id
	 * @return the declarative questions
	 */
	@GetMapping("/declarative/{assignId}")
	public List<DeclarativeAnswer> getDeclarativeQuestions(@PathVariable(value="assignId") long assignId) {
		return testResultService.getDeclarativeQuestions(assignId);
	}
	
	/**
	 * Gets the my answer.
	 *
	 * @param resultId the result id
	 * @param choosenAnswer the choosen answer
	 * @return the my answer
	 */
	@GetMapping("/my/answer/{resultId}/{answer}")
	public void getMyAnswer(@PathVariable(value="resultId") long resultId, @PathVariable(value="answer") String choosenAnswer) {
		testResultService.setMyAnswer(resultId, choosenAnswer);
	}
	
	/**
	 * Update test result.
	 *
	 * @param testResultVo the test result vo
	 * @return the list
	 */
	@PostMapping("/update")
	public List<TestResult> updateTestResult(@RequestBody TestResultVo testResultVo){
		List<TestResult> testResultList = testResultService.updateTestResult(testResultVo);
		testSummaryService.updateSummary(testResultList,testResultVo.getSummaryId());
		return testResultList;
		
	}
	

}
