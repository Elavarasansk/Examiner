package org.web.vexamine.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web.vexamine.dao.entity.ManagerCredit;
import org.web.vexamine.dao.vo.ManagerCreditVo;
import org.web.vexamine.service.ManagerCreditService;

@RestController
@RequestMapping("/manager/credit")
public class ManagerCreditController {
	
	@Autowired
	private ManagerCreditService managerCreditService;
	

	@PostMapping("/add")
	public ManagerCredit addAllManager(@RequestBody ManagerCreditVo managerCreditVo) {
		return managerCreditService.addManagerCredit(managerCreditVo);
	}
	
	@PostMapping("/update")
	public ManagerCredit updateCredit(@RequestBody ManagerCreditVo managerCreditVo) {
		return managerCreditService.updateCredit(managerCreditVo);
	}
	
	@PostMapping("/update/purchase/credit")
	public ManagerCredit updatePurchasedCredits(@RequestBody ManagerCreditVo managerCreditVo) throws Exception {
		return managerCreditService.updatePurchasedCredits(managerCreditVo);
	}
	
	@GetMapping("/find/credit")
	public ManagerCredit findByCreditByMailId() {
		return managerCreditService.findByCreditByMailId();
	}

	@GetMapping("/search/mailid")
	public List<String> searchCandidate() {
		return managerCreditService.searchCandidate();
	}

	@GetMapping("/find/all/mailid")
	public Map<String, Object> getAllCandidate(){
		return managerCreditService.getAllCandidate();
	}


	

}