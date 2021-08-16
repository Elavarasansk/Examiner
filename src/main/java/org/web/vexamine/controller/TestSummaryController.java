package org.web.vexamine.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web.vexamine.dao.entity.TestSummary;
import org.web.vexamine.dao.vo.TestSummaryVo;
import org.web.vexamine.service.TestSummaryService;

/**
 * The Class TestSummaryController.
 */
@RestController
@RequestMapping("/test/summary")
public class TestSummaryController {

	@Autowired
	private TestSummaryService testSummaryService;

	/**
	 * Adds the test reult.
	 *
	 * @param testSummaryVo the test summary vo
	 * @return the test summary
	 */
	@PostMapping("/add")
	public TestSummary addTestReult(@RequestBody TestSummaryVo testSummaryVo){
		return testSummaryService.addSummary(testSummaryVo);
	}
	
	/**
	 * Delete summary.
	 *
	 * @param id the id
	 */
	@DeleteMapping("/delete/{id}")
	public void  deleteSummary(@PathVariable(value="id") long id){
		testSummaryService.deleteSummary(id);
	}

	/**
	 * Gets the all summary.
	 *
	 * @param testSummaryVo the test summary vo
	 * @return the all summary
	 */
	@PostMapping("/find/all")
	public List<TestSummary> getAllSummary(@RequestBody TestSummaryVo testSummaryVo){
		return testSummaryService.getAllSummary(testSummaryVo);
	}

	/**
	 * Gets the summary.
	 *
	 * @param id the id
	 * @return the summary
	 */
	@GetMapping("/find/{id}")
	public TestSummary getSummary(@PathVariable(value="id") long id){
		return testSummaryService.getSummary(id);
	}

	/**
	 * Search all summary.
	 *
	 * @param testSummaryVo the test summary vo
	 * @return the map
	 */
	@PostMapping("/search")
	public Map<String, Object> searchAllSummary(@RequestBody TestSummaryVo testSummaryVo){
		return testSummaryService.searchAllSummary(testSummaryVo);
	}

}