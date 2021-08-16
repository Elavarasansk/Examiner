package org.web.vexamine.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web.vexamine.dao.entity.TestCategory;
import org.web.vexamine.dao.repository.TestCategoryRepo;
import org.web.vexamine.dao.vo.ContactFormVo;

@Service
public class HomePageService {

	@Autowired
	private MailSendingService mailSendingService;

	@Autowired
	private TestCategoryRepo testCategoryRepo;
	
	public void sendContactFormDetails(ContactFormVo contactFormVo) {
		mailSendingService.sendContactMail(contactFormVo);
	}
	
	public Map<String, Set<String>> getCategoryForHome() {
		List<TestCategory> testCategory = testCategoryRepo.findAll();
		Map<String, Set<String>> resMap = new HashMap<>();
		testCategory.stream().forEach(rec -> {
			String category = rec.getCategory();
			String subCategory = rec.getSubCategory();
			if(resMap.keySet().contains(category)) {
				Set<String> subCategoryList = resMap.get(category);
				subCategoryList.add(subCategory);
			} else {
				resMap.put(category, Arrays.asList(subCategory).stream().collect(Collectors.toSet()));
			}
		});
		return resMap;
	}

}
