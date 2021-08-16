package org.web.vexamine.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.web.vexamine.dao.entity.ManagerInfo;
import org.web.vexamine.dao.entity.UserAuthorityInfo;
import org.web.vexamine.dao.entity.UserCredentials;
import org.web.vexamine.dao.vo.UserRegistrationForm;
import org.web.vexamine.service.ManagerCreditService;
import org.web.vexamine.service.ManagerInfoService;
import org.web.vexamine.service.RegistrationService;
import org.web.vexamine.utils.VexamineConstants;

/**
 * The Class RegistrationController.
 */
@RestController
@RequestMapping("/credentials/")
public class RegistrationController {

	@Autowired
	private RegistrationService registrationService;

	@Autowired
	private ManagerInfoService managerInfoService;
	
	@Autowired
	private ManagerCreditService managerCreditService;

	/**
	 * Adds the candidate.
	 *
	 * @param registrationForm the registration form
	 * @return the user authority info
	 */
	@PostMapping("add/candidate")
	// @RoleAuthorisation(type = RoleCategories.MANAGER)
	public UserAuthorityInfo addCandidate(@RequestBody UserRegistrationForm registrationForm) {
		UserCredentials userCredentials = registrationService.registerCandidate(registrationForm);
		UserAuthorityInfo candidateAuthInfo = registrationService.setUserAuthority(userCredentials, VexamineConstants.CANDIDATE_ROLE);
		managerCreditService.mapCandidateToManager(candidateAuthInfo);
		return candidateAuthInfo;
	}

	/**
	 * Adds the manager.
	 *
	 * @param registrationForm the registration form
	 * @return the list
	 */
	@PostMapping("add/manager")
	// @RoleAuthorisation(type = RoleCategories.SUPER_ADMIN)
	public List<ManagerInfo> addManager(@RequestBody UserRegistrationForm registrationForm) {
		UserCredentials userCredentials = registrationService.registerAppUser(registrationForm);
		UserAuthorityInfo userAuthorityInfo = registrationService.setUserAuthority(userCredentials,
		        VexamineConstants.MANAGER_ROLE);
		managerCreditService.registerManagerCredit(userAuthorityInfo,registrationForm.getPurchasedCredits());
		return managerInfoService.registerMangerInfo(registrationForm, userAuthorityInfo);
	}

	/**
	 * Adds the super admin.
	 *
	 * @param registrationForm the registration form
	 */
	@PostMapping("add/superadmin")
	// @RoleAuthorisation(type = RoleCategories.SUPER_ADMIN)
	public void addSuperAdmin(@RequestBody UserRegistrationForm registrationForm) {
		UserCredentials userCredentials = registrationService.registerAppUser(registrationForm);
		registrationService.setUserAuthority(userCredentials, VexamineConstants.SUPER_ADMIN_ROLE);
	}

	/**
	 * Download candidate template.
	 *
	 * @param response the response
	 * @throws IOException 
	 */
	@GetMapping("download/candidate/template")
	public void downloadCandidateTemplate(HttpServletResponse response) throws IOException {
		registrationService.getCandidateRegisterTemplate(response);
	}

	/**
	 * Parses the file.
	 *
	 * @param file the file
	 * @return the list
	 * @throws Exception the exception
	 */
	@PostMapping("parse/file")
	public List<Map<String, Object>> parseFile(@RequestParam("file") MultipartFile file) throws Exception {
		return registrationService.readTemplate(file);
	}

	/**
	 * Adds the all candidate.
	 *
	 * @param candidateList the candidate list
	 * @return the list
	 * @throws Exception the exception
	 */
	@PostMapping("add/all/candidate")
	public List<UserAuthorityInfo> addAllCandidate(@RequestBody List<String> candidateList) throws Exception {
		List<UserRegistrationForm> regFormList = registrationService.formatData(candidateList);
		List<UserCredentials> userCredentialsList = registrationService.registerAllAppUser(regFormList);
		List<UserAuthorityInfo> candidateAuthorityList = registrationService.setAllUserAuthority(userCredentialsList, VexamineConstants.CANDIDATE_ROLE);
		managerCreditService.mapCandidateToManagerBatch(candidateAuthorityList);
		return candidateAuthorityList;
	}

	/**
	 * Reset user.
	 *
	 * @param registrationForm the registration form
	 * @return the user credentials
	 */
	@PostMapping("/reset")
	public UserCredentials resetUser(@RequestBody UserRegistrationForm registrationForm) {
		UserCredentials userCredentials = registrationService.registerResetUser(registrationForm);
		return userCredentials;
	}
	
	/**
	 * Reset User Password.
	 *
	 * @param registrationForm the registration form
	 * @return the user credentials
	 */
	@PostMapping("/user/reset/password")
	public UserCredentials resetCandidate(@RequestBody UserRegistrationForm registrationForm) {
		UserCredentials userCredentials = registrationService.updateCandidatePassword(registrationForm);
		return userCredentials;
	}
}
