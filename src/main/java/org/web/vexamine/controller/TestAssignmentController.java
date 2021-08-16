package org.web.vexamine.controller;

import java.util.List;

import javax.activity.InvalidActivityException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web.vexamine.dao.entity.TestAssignment;
import org.web.vexamine.dao.vo.TestAssignmentCandidateFilter;
import org.web.vexamine.dao.vo.TestAssignmentVo;
import org.web.vexamine.service.TestAssignmentService;

import javassist.NotFoundException;

/**
 * The Class TestAssignmentController.
 */
@RestController
@RequestMapping("/test/assignment")
public class TestAssignmentController {

	@Autowired
	private TestAssignmentService testAssignmentService;

	/**
	 * Adds the test assignment.
	 *
	 * @param testAssignmentVo the test assignment vo
	 * @return the list
	 * @throws Exception 
	 */
	@PostMapping("/add")
	public List<TestAssignment> addTestAssignment(@RequestBody TestAssignmentVo testAssignmentVo) throws Exception {
		return testAssignmentService.addTestAssignment(testAssignmentVo);
	}

	/**
	 * Delete test assignment.
	 *
	 * @param id the id
	 */
	@DeleteMapping("/delete/{id}")
	public void deleteTestAssignment(@PathVariable(value = "id") long id) {
		testAssignmentService.deleteTestAssignment(id);
	}

	/**
	 * Gets the test assignment.
	 *
	 * @param id the id
	 * @return the test assignment
	 */
	@GetMapping("/find/{id}")
	public TestAssignment getTestAssignment(@PathVariable(value = "id") long id) {
		return testAssignmentService.getTestAssignment(id);
	}

	/**
	 * Gets the test assignment list.
	 *
	 * @param testAssignmentVo the test assignment vo
	 * @return the test assignment list
	 */
	@PostMapping("/find/all")
	public Page<TestAssignment> getTestAssignmentList(@RequestBody TestAssignmentVo testAssignmentVo) {
		return testAssignmentService.getTestAssignmentList(testAssignmentVo);
	}

	/**
	 * Gets the by test status.
	 *
	 * @param status the status
	 * @return the by test status
	 */
	@GetMapping("/status/{status}")
	public List<TestAssignment> getByTestStatus(@PathVariable(value = "id") String status) {
		return testAssignmentService.getByTestStatus(status);
	}

	/**
	 * Gets the count by user id.
	 *
	 * @param id the id
	 * @return the count by user id
	 */
	@GetMapping("/count/{id}")
	public Long getCountByUserId(@PathVariable(value = "id") Long id) {
		return testAssignmentService.getCountByUserId(id);
	}

	/**
	 * Gets the my assigned test.
	 *
	 * @return the my assigned test
	 */
	@GetMapping("/mine")
	public TestAssignment getMyAssignedTest() {
		return testAssignmentService.getMyAssignedTest();
	}
	
	/**
	 * Gets the all my assigned test.
	 *
	 * @param candidateSpecificFilter the candidate specific filter
	 * @return the all my assigned test
	 */
	@PostMapping("/mine/all")
	public List<TestAssignment> getAllMyAssignedTest(@RequestBody TestAssignmentCandidateFilter candidateSpecificFilter) {
		return testAssignmentService.getAllMyAssignedTests(candidateSpecificFilter);
	}

	/**
	 * Gets the my assigned test.
	 *
	 * @param id the id
	 * @return the my assigned test
	 * @throws InvalidActivityException the invalid activity exception
	 * @throws NotFoundException the not found exception
	 */
	@GetMapping("/start/{id}")
	public TestAssignment getMyAssignedTest(@PathVariable(value = "id") long id)
	        throws Exception {
		return testAssignmentService.startAssignedTest(id);
	}

	/**
	 * Submit test.
	 *
	 * @param id the id
	 * @return the test assignment
	 */
	@GetMapping("/complete/{id}")
	public TestAssignment submitTest(@PathVariable(value = "id") long id) {
		return testAssignmentService.completeTest(id);
	}

	/**
	 * Expire test.
	 *
	 * @param id the id
	 * @return the test assignment
	 */
	@GetMapping("/expire/{id}")
	public TestAssignment expireTest(@PathVariable(value = "id") long id) {
		return testAssignmentService.expireTest(id);
	}

	/**
	 * Do reject.
	 *
	 * @param rejectList the reject list
	 * @return the integer
	 */
	@PostMapping("/reject/status")
	public Integer doReject(@RequestBody List<Long> rejectList) {
		return testAssignmentService.doReject(rejectList);
	}

}