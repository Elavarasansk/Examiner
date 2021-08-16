package org.web.vexamine.controller;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web.vexamine.dao.vo.ContactFormVo;
import org.web.vexamine.service.HomePageService;

/**
 * The Class LoginController.
 */
@RestController
@RequestMapping("home/")
public class HomePageController {

	@Autowired
	private HomePageService homePageService;

	@PostMapping("contactform")
	public void sendContactFormDetails(@RequestBody ContactFormVo contactFormVo) {
		homePageService.sendContactFormDetails(contactFormVo);
	}
	
	@PostMapping("categorydata")
	public Map<String, Set<String>> getCategoryForHome() {
		return homePageService.getCategoryForHome();
	}

}
