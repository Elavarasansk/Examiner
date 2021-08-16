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
import org.web.vexamine.dao.entity.ManagerInfo;
import org.web.vexamine.dao.vo.ManagerVo;
import org.web.vexamine.service.ManagerInfoService;

/**
 * The Class ManagerInfoController.
 */
@RestController
@RequestMapping("/manager/info")
public class ManagerInfoController {

	@Autowired
	private ManagerInfoService managerInfoService;

	/**
	 * Adds the all manager.
	 *
	 * @param managerVo the manager vo
	 * @return the list
	 */
	@PostMapping("/add/all")
	public List<ManagerInfo> addAllManager(@RequestBody ManagerVo managerVo) {
		return managerInfoService.addAllMangerInfo(managerVo);
	}
	
	/**
	 * Find by mail id.
	 *
	 * @param mailId the mail id
	 * @return the list
	 */
	@PostMapping("/find/mail/{mailId}")
	public List<String> findByMailId(@PathVariable(value = "mailId") String mailId) {
		return managerInfoService.findByMailId(mailId);
	}
	
	/**
	 * Find all question bank.
	 *
	 * @return the list
	 */
	@GetMapping("/find/all/question/bank")
	public List<String> findAllQuestionBank() {
		return managerInfoService.findAllQuestionBank();
	}

	/**
	 * Gets the all question bank.
	 *
	 * @return the all question bank
	 */
	@PostMapping("/find/all")
	public List<ManagerInfo> getAllQuestionBank() {
		return managerInfoService.findAllManager();
	}
	
	/**
	 * Gets the manager summary.
	 *
	 * @param managerVo the manager vo
	 * @return the manager summary
	 */
	@PostMapping("/find/all/summary")
	public List<Map<String, Object>> getManagerSummary(@RequestBody ManagerVo managerVo) {
		return managerInfoService.getManagerSummary(managerVo);
	}

	/**
	 * Delete by manger by id.
	 *
	 * @param id the id
	 */
	@DeleteMapping("/delete/{id}")
	public void deleteByMangerById(@PathVariable(value="id") Long id) {
		managerInfoService.deleteByMangerById(id);
	}

	/**
	 * Delete by manger mail id.
	 *
	 * @param mailId the mail id
	 */
	@DeleteMapping("/mailid/delete/{mailId}")
	public void deleteByMangerMailId(@PathVariable(value="mailId") String mailId) {
		managerInfoService.deleteByMangerMailId(mailId);
	}

	/**
	 * Gets the manager info count.
	 *
	 * @return the manager info count
	 */
	@GetMapping("/count")
	public Long getManagerInfoCount() {
		return managerInfoService.getManagerInfoCount();
	}

	/**
	 * Gets the count by mail id.
	 *
	 * @param mailId the mail id
	 * @return the count by mail id
	 */
	@GetMapping("/mailld/count")
	public Long getCountByMailId(@PathVariable(value="mailId") String mailId) {
		return managerInfoService.getCountByMailId(mailId);
	}

	/**
	 * Search question bank.
	 *
	 * @param managerVo the manager vo
	 * @return the list
	 */
	@PostMapping("/search/question/bank")
	public List<String> searchQuestionBank(@RequestBody ManagerVo managerVo) {
		return managerInfoService.searchQuestionBank(managerVo);
	}

	/**
	 * Search question bank by manager.
	 *
	 * @param managerVo the manager vo
	 * @return the list
	 */
	@PostMapping("/search/manager/question/bank")
	public List<String> searchQuestionBankByManager(@RequestBody ManagerVo managerVo) {
		return managerInfoService.searchQuestionBankByManager(managerVo);
	}

	/**
	 * Do all manager details search.
	 *
	 * @param managerVo the manager vo
	 * @return the map
	 */
	@PostMapping("/search/manager/alldetails")
  public Map<String, Object> doAllManagerDetailsSearch(@RequestBody ManagerVo managerVo) {
    return managerInfoService.doAllManagerDetailsSearch(managerVo);
  }
	
}
