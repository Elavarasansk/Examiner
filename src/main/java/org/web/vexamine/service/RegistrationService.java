package org.web.vexamine.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.web.vexamine.dao.entity.Role;
import org.web.vexamine.dao.entity.UserAuthorityInfo;
import org.web.vexamine.dao.entity.UserCredentials;
import org.web.vexamine.dao.repository.RoleRepository;
import org.web.vexamine.dao.repository.UserAuthorityInfoRepository;
import org.web.vexamine.dao.repository.UserCredentialRepository;
import org.web.vexamine.dao.vo.UserRegistrationForm;
import org.web.vexamine.utils.CommonFunctions;
import org.web.vexamine.utils.PasswordUtils;
import org.web.vexamine.utils.VexamineConstants;
import org.web.vexamine.utils.constants.QAConstants;
import org.web.vexamine.utils.storage.CookieSessionStorage;

/**
 * The Class RegistrationService.
 */
@Service
public class RegistrationService {

	@Autowired
	private UserCredentialRepository userCredentialRepo;

	@Autowired
	private UserAuthorityInfoRepository userAuthorityInfoRepo;

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private MailSendingService mailSendingService;

	@Value("classpath:template/candidate_template.xls")
	Resource candidateTemplateResource;

	/**
	 * Register app user.
	 *
	 * @param registrationForm
	 *            the registration form
	 * @return the user credentials
	 */
	public UserCredentials registerCandidate(UserRegistrationForm registrationForm) {
		String mailId = registrationForm.getMailId();
		String password;
		if(registrationForm.getSwitchType().equals("true")) {
			password = passwordReset();
		}else {
			password = registrationForm.getPassword();
		}
		UserAuthorityInfo userAuthorityInfo = userAuthorityInfoRepo.findByUserCredentialsMailId(mailId);
		if(!ObjectUtils.isEmpty(userAuthorityInfo)) {
			return userAuthorityInfo.getUserCredentials();
		}
		return this.registerUser(mailId, password, registrationForm);
	}

	/**
	 * Register app user.
	 *
	 * @param registrationForm
	 *            the registration form
	 * @return the user credentials
	 */
	public UserCredentials registerAppUser(UserRegistrationForm registrationForm) {
		String mailId = registrationForm.getMailId();
		String password;
		if(registrationForm.getSwitchType().equals("true")) {
			password = passwordReset();
		}else {
			password = registrationForm.getPassword();
		}
		UserCredentials userCredentials = this.registerUser(mailId, password, registrationForm);
		return userCredentials;
	}

	/**
	 * Sets the user authority.
	 *
	 * @param userCredentials
	 *            the user credentials
	 * @param roleType
	 *            the role type
	 * @return the user authority info
	 */
	public UserAuthorityInfo setUserAuthority(UserCredentials userCredentials, String roleType) {
		String mailId = userCredentials.getMailId();
		Long userId = userCredentials.getId();

		UserAuthorityInfo existingAuthority = userAuthorityInfoRepo.findByUserCredentialsId(userId);
		if(!ObjectUtils.isEmpty(existingAuthority)) {
			return existingAuthority;
		}

		String sessionUser = getSessionUser(mailId);
		Role roleDetails = roleRepo.findByType(roleType);
		UserAuthorityInfo userAuth = UserAuthorityInfo.builder().userCredentials(userCredentials).userRole(roleDetails)
				.createUser(sessionUser).updateUser(sessionUser).build();
		return userAuthorityInfoRepo.save(userAuth);
	}

	/**
	 * Sets the all user authority.
	 *
	 * @param userCredentialsList
	 *            the user credentials list
	 * @param roleType
	 *            the role type
	 * @return the list
	 */
	public List<UserAuthorityInfo> setAllUserAuthority(List<UserCredentials> userCredentialsList, String roleType) {
		String sessionUser = getSessionUser(StringUtils.EMPTY);

		List<Long> userCredIdList = userCredentialsList.stream().map(UserCredentials::getId).collect(Collectors.toList());
		List<UserAuthorityInfo> existingUserAuthList = userAuthorityInfoRepo.findByUserCredentialsIdIn(userCredIdList);

		List<Long> existingUserCredList = existingUserAuthList.stream().map(userAuthorityInfo -> userAuthorityInfo.getUserCredentials().getId())
				.collect(Collectors.toList());

		userCredentialsList = userCredentialsList.stream().filter(userCredentials -> existingUserCredList.indexOf(userCredentials.getId()) == -1)
				.collect(Collectors.toList());

		Role roleDetails = roleRepo.findByType(roleType);
		List<UserAuthorityInfo> authorityList = userCredentialsList.stream().map(user -> UserAuthorityInfo.builder()
				.userCredentials(user)
				.userRole(roleDetails)
				.createUser(sessionUser)
				.createDate(new Date())
				.build()).collect(Collectors.toList());
		authorityList.addAll(existingUserAuthList);

		return userAuthorityInfoRepo.saveAll(authorityList);
	}

	/**
	 * Register user.
	 *
	 * @param mailId
	 *            the mail id
	 * @param password
	 *            the password
	 * @return the user credentials
	 */
	private UserCredentials registerUser(String mailId, String password, UserRegistrationForm registrationForm) {
		String sessionUser = getSessionUser(mailId);
		registrationForm.setPassword(password);
		if (checkUserExists(mailId)) {
			throw new DuplicateKeyException(
					"User - \"" + mailId + "\" already exists." + " So user cannot be registered");
		}

		String secureSalt = PasswordUtils.generateSALT(30);
		byte[] hashedPassword = PasswordUtils.generateSecurePassword(password, secureSalt);

		UserCredentials userCredentials = UserCredentials.builder().mailId(mailId).userName(mailId)
				.hashedPassword(hashedPassword).hashedSalt(secureSalt.getBytes()).createUser(sessionUser)
				.updateUser(sessionUser).inviteSent(false).build();

		dispatchMailingThread(registrationForm, userCredentials);
		return userCredentialRepo.save(userCredentials);
	}

	/**
	 * Gets the session user.
	 *
	 * @param userName
	 *            the user name
	 * @return the session user
	 */
	private String getSessionUser(String userName) {
		String sessionUser = VexamineConstants.SYSTEM_USER;
		if (!ObjectUtils.isEmpty(CookieSessionStorage.get())) {
			sessionUser = CookieSessionStorage.get().getUserName();
			if (StringUtils.isEmpty(sessionUser)) {
				sessionUser = VexamineConstants.SYSTEM_USER;
			}
		}
		return sessionUser;
	}

	/**
	 * Check user exists.
	 *
	 * @param userName
	 *            the user name
	 * @return true, if successful
	 */
	private boolean checkUserExists(String userName) {
		UserCredentials userCredentials = userCredentialRepo.findByMailId(userName);
		return !ObjectUtils.isEmpty(userCredentials);
	}

	/**
	 * Check any user exists.
	 *
	 * @param mailIdList
	 *            the mail id list
	 * @return true, if successful
	 */
	private boolean checkAnyUserExists(List<String> mailIdList) {
		Set<String> mailIdSet = new HashSet<>(mailIdList);
		if (mailIdList.size() != mailIdSet.size()) {
			return true;
		}
		return userCredentialRepo.existsByMailIdIn(mailIdList);
	}

	/**
	 * Check and remove all existing user auth.
	 *
	 * @param authorityList
	 *            the authority list
	 */
	private void checkAndRemoveAllExistingUserAuth(List<UserAuthorityInfo> authorityList) {
		List<Long> userIdList = authorityList.stream().map(UserAuthorityInfo::getId).collect(Collectors.toList());
		List<UserAuthorityInfo> userAuthInfoList = userAuthorityInfoRepo.findByUserCredentialsIdIn(userIdList);
		userAuthorityInfoRepo.deleteAll(userAuthInfoList);
	}

	/**
	 * Fetch user credentials.
	 *
	 * @return the user credentials
	 */
	public UserCredentials fetchUserCredentials() {
		UserCredentials userCredentials = userCredentialRepo.findByMailId(CookieSessionStorage.get().getUserName());
		if (ObjectUtils.isEmpty(userCredentials)) {
			return null;
		}
		return userCredentials;
	}

	/**
	 * Gets the exists mail id.
	 *
	 * @param mailIdList
	 *            the mail id list
	 * @return the exists mail id
	 */
	private String getExistsMailId(List<String> mailIdList) {
		List<String> idList = userCredentialRepo.getAllMailId(mailIdList);
		return idList.stream().findFirst().orElse(StringUtils.EMPTY);
	}

	/**
	 * Register all app user.
	 *
	 * @param registrationList
	 *            the registration list
	 * @return the list
	 * @throws Exception
	 *             the exception
	 */
	public List<UserCredentials> registerAllAppUser(List<UserRegistrationForm> registrationList) throws Exception {
		String managerMailId = CookieSessionStorage.get().getUserName();
		List<String> mailList = registrationList.stream().map(UserRegistrationForm::getMailId)
				.collect(Collectors.toList());

		List<UserCredentials> existingCandidateList = userCredentialRepo.findByMailIdIn(mailList);
		List<String> existingCandidates = existingCandidateList.stream().map(UserCredentials::getMailId)
				.collect(Collectors.toList());

		Set<String> newMailList = mailList.stream().filter(mailId -> existingCandidates.indexOf(mailId)==-1)
				.collect(Collectors.toSet());

		List<UserCredentials> userCredentialList = newMailList.stream().map(mailId -> {
			String password = passwordReset();
			String secureSalt = PasswordUtils.generateSALT(30);
			byte[] hashedPassword = PasswordUtils.generateSecurePassword(password, secureSalt);

			return UserCredentials.builder()
					.mailId(mailId)
					.userName(mailId)
					.password(password)
					.hashedPassword(hashedPassword)
					.hashedSalt(secureSalt.getBytes())
					.inviteSent(false)
					.createUser(managerMailId)
					.createDate(new Date())
					.build();
		}).collect(Collectors.toList());
		
		existingCandidateList.stream().forEach(userCreds -> userCreds.setPassword("You Have an Existing Account and password has sent already."
				+ "\n So Please check your previous mail from Vexamine"));
		if(CollectionUtils.isNotEmpty(existingCandidateList )) {
		userCredentialList.addAll(existingCandidateList);
		}
		userCredentialList = userCredentialRepo.saveAll(userCredentialList);
		dispatchMailingThread(userCredentialList);
		return userCredentialList;
	}

	/**
	 * Register all app user.
	 *
	 * @param registrationList
	 *            the registration list
	 * @return the list
	 * @throws Exception
	 *             the exception
	 */
	public List<UserCredentials> registerAllAppUserOld(List<UserRegistrationForm> registrationList) throws Exception {
		List<String> mailList = registrationList.stream().map(UserRegistrationForm::getMailId)
				.collect(Collectors.toList());
		validate(mailList);
		if (checkAnyUserExists(mailList)) {
			String mailId = getExistsMailId(mailList);
			throw new DuplicateKeyException(
					"User - \"" + mailId + "\" already exists." + " So user cannot be registered");
		}
		String sessionUser = getSessionUser(StringUtils.EMPTY);
		List<UserCredentials> insertList = registrationList.stream().map(user -> {
			String password = passwordReset();
			String secureSalt = PasswordUtils.generateSALT(30);
			byte[] hashedPassword = PasswordUtils.generateSecurePassword(password, secureSalt);
			UserCredentials userCredentials = UserCredentials.builder().mailId(user.getMailId())
					.userName(user.getMailId()).hashedPassword(hashedPassword).hashedSalt(secureSalt.getBytes())
					.createUser(sessionUser).updateUser(sessionUser).inviteSent(false).build();
			dispatchMailingThread(UserRegistrationForm.builder().mailId(user.getMailId()).password(password).build(),
					userCredentials);
			return userCredentials;
		}).collect(Collectors.toList());
		return userCredentialRepo.saveAll(insertList);
	}

	/**
	 * Gets the candidate register template.
	 *
	 * @param response the response
	 * @return the candidate register template
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void getCandidateRegisterTemplate(HttpServletResponse response) throws IOException{
		InputStream candidateTemplateInputStream = candidateTemplateResource.getInputStream();
		CommonFunctions.fileDownloader(response, candidateTemplateInputStream);
	}

	/**
	 * Format data.
	 *
	 * @param candidateList
	 *            the candidate list
	 * @return the list
	 */
	public List<UserRegistrationForm> formatData(List<String> candidateList) {
		List<UserRegistrationForm> registrationList = candidateList.stream()
				.map(record -> UserRegistrationForm.builder().mailId(record).password(record).build())
				.collect(Collectors.toList());
		return registrationList;
	}

	/**
	 * Read template.
	 *
	 * @param file
	 *            the file
	 * @return the list
	 * @throws Exception
	 *             the exception
	 */
	public List<Map<String, Object>> readTemplate(MultipartFile file) throws Exception {
		File inputFile = null;
		try {
			inputFile = new File(System.getProperty("java.io.tmpdir")+File.separator+file.getName()
			+System.currentTimeMillis()+FilenameUtils.getExtension(file.getOriginalFilename()));
			
			file.transferTo(inputFile);
			Workbook workbook = WorkbookFactory.create(inputFile);
			Sheet sheet = workbook.getSheet("CandidateList");
			DataFormatter dataFormat = new DataFormatter();

			Set<String> userRegFormList = new HashSet<>();
			int lastRowIndex = sheet.getLastRowNum();
			for(int curRow=QAConstants.START_ROW; curRow<=lastRowIndex; curRow++) {
				Row row = sheet.getRow(curRow);
				Cell questionCell = row.getCell(1);
				String mailId = dataFormat.formatCellValue(questionCell);
				userRegFormList.add(mailId);
			}
			workbook.close();
			return userRegFormList.stream().filter(value -> StringUtils.isNotEmpty(value)).map(value -> {
				Map<String, Object> map = new HashMap<>();
				map.put("mailId", value);
				return map;
			}).collect(Collectors.toList());
		} catch (Exception e) {
			e.printStackTrace();
			Log.error("Candidate Upload failed", e);
			throw new Exception("Uploaded xls file format is wrong." + " So please verify the xls file");
		} finally {
			FileUtils.deleteQuietly(inputFile);
		}
	}

	/**
	 * Validate.
	 *
	 * @param mailIdList
	 *            the mail id list
	 * @throws AddressException 
	 * @throws Exception
	 *             the exception
	 */
	public void validate(List<String> mailIdList) throws AddressException {
		try {
			for (String mailId : mailIdList) {
				InternetAddress emailAddr = new InternetAddress(mailId);
				emailAddr.validate();
			}
		} catch (AddressException ex) {
			Log.error("Candidate Upload failed for the E-mail " + ex.getRef());
			throw new AddressException(ex.getMessage() + " for the E-mail " + ex.getRef());
		}
	}

	/**
	 * Password reset.
	 *
	 * @return the string
	 */
	public String passwordReset() {
		Random randomNumGenerator = new Random();
		String randomString = String.valueOf(randomNumGenerator.nextInt(100000000));
		String resetPassword;
		if (randomString.length() == 8) {
			resetPassword = generateResetPassword(randomString);

		} else {
			String appendChar = "7";
			for (int idx = randomString.length(); idx < 8; ++idx) {
				randomString = randomString.concat(appendChar);
				System.out.println("Random Number = " + randomString);
			}
			resetPassword = generateResetPassword(randomString);
		}
		return resetPassword;
	}

	/**
	 * Generate reset password.
	 *
	 * @param randomString
	 *            the random string
	 * @return the string
	 */
	private String generateResetPassword(String randomString) {
		int[] changeIndex = { 3, 7 };
		char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
		if (randomString.length() == 0) {
			return null;
		}
		StringBuilder generatedString = null;
		generatedString = new StringBuilder(randomString);
		for (int index : changeIndex) {
			generatedString.setCharAt(index, alphabet[index]);
		}
		return generatedString.toString();
	}

	/**
	 * Register reset user.
	 *
	 * @param registrationForm
	 *            the registration form
	 * @return the user credentials
	 */
	public UserCredentials registerResetUser(UserRegistrationForm registrationForm) {
		String mailId = registrationForm.getMailId();
		String password = passwordReset();
		UserCredentials userCredentials = this.ResetUserPassword(mailId, password);
		return userCredentials;
	}

	/**
	 * Reset user password.
	 *
	 * @param mailId
	 *            the mail id
	 * @param password
	 *            the password
	 * @return the user credentials
	 */
	private UserCredentials ResetUserPassword(String mailId, String password) {
		if (checkUserExists(mailId)) {
			UserCredentials userCredentials = userCredentialRepo.findByMailId(mailId);

			String secureSalt = PasswordUtils.generateSALT(30);
			byte[] hashedPassword = PasswordUtils.generateSecurePassword(password, secureSalt);

			userCredentials.setHashedPassword(hashedPassword);
			userCredentials.setHashedSalt(secureSalt.getBytes());
			return userCredentialRepo.save(userCredentials);
		} else {
			throw new EmptyResultDataAccessException(
					"User - \"" + mailId + "\" is not exists." + " So please register the user", 1);
		}
	}

	/**
	 * runs mail sender as a thread
	 *
	 * @param registrationForm
	 *            consisting mailid, password, company
	 * @param userCredentials
	 *            entry in db
	 */
	private void dispatchMailingThread(UserRegistrationForm registrationForm, UserCredentials userCredentials) {
		String mailId = registrationForm.getMailId();
		String password = registrationForm.getPassword();
		String company = registrationForm.getCompany();
		Thread mailThread = new Thread(new Runnable() {
			@Override
			public void run() {
				boolean inviteSent = false;
				try {
					if (Objects.nonNull(company))
						inviteSent = mailSendingService.sendManagerRegistrationMail(mailId, password, company);
					else
						inviteSent = mailSendingService.sendCandidateRegistrationMail(mailId, password);
				} catch (Exception e) {
					e.printStackTrace();
				}
				userCredentials.setInviteSent(inviteSent);
				userCredentialRepo.save(userCredentials);
			}
		});
		mailThread.start();
	}

	private void dispatchMailingThread(List<UserCredentials> userCredentialList) {
		Thread mailThread = new Thread(new Runnable() {
			@Override
			public void run() {
				userCredentialList.forEach(userCredential -> {
					boolean inviteSent = false;
					String mailId = userCredential.getMailId();
					String password = userCredential.getPassword();
					inviteSent = mailSendingService.sendCandidateRegistrationMail(mailId, password);
					userCredential.setInviteSent(inviteSent);
				});
				userCredentialRepo.saveAll(userCredentialList);
			}
		});
		mailThread.start();
	}

	/**
	 * runs mail sender as a thread
	 *
	 * @param registrationForm
	 *            consisting mailid, password, company
	 * @param userCredentials
	 *            entry in db
	 */
	private void dispatchMailingThread_old(UserRegistrationForm registrationForm, UserCredentials userCredentials) {
		String mailId = registrationForm.getMailId();
		String password = registrationForm.getPassword();
		String company = registrationForm.getCompany();
		Thread mailThread = new Thread(new Runnable() {
			@Override
			public void run() {
				boolean inviteSent = false;
				try {
					if (Objects.nonNull(company))
						inviteSent = mailSendingService.sendManagerRegistrationMail(mailId, password, company);
					else
						inviteSent = mailSendingService.sendCandidateRegistrationMail(mailId, password);
				} catch (Exception e) {
					e.printStackTrace();
				}
				userCredentials.setInviteSent(inviteSent);
				userCredentialRepo.save(userCredentials);
			}
		});
		mailThread.start();
	}
	
	public UserCredentials updateCandidatePassword(UserRegistrationForm registrationForm) {
		String mailId = registrationForm.getMailId();
		String password = passwordReset();
		registrationForm.setPassword(password);
		String sessionUser = getSessionUser(mailId);
		UserCredentials userExistingCredentials = userCredentialRepo.findByMailId(mailId);
		boolean checkUser = ObjectUtils.isEmpty(userExistingCredentials);
		if (checkUser) {
			throw new DuplicateKeyException(
					"User - \"" + mailId + "\" doesnot exists." + " So we cannot reset the password");
		}

		String secureSalt = PasswordUtils.generateSALT(30);
		byte[] hashedPassword = PasswordUtils.generateSecurePassword(password, secureSalt);

		UserCredentials userCredentials = UserCredentials.builder().id(userExistingCredentials.getId()).mailId(mailId).userName(mailId)
				.hashedPassword(hashedPassword).hashedSalt(secureSalt.getBytes()).createUser(sessionUser)
				.updateUser(sessionUser).inviteSent(false).build();
    
		dispatchResetMailingThread(registrationForm, userCredentials);
		return userCredentialRepo.save(userCredentials);
	}
	
	/**
	 * runs mail sender as a thread
	 *
	 * @param registrationForm
	 *            consisting mailid, password, company
	 * @param userCredentials
	 *            entry in db
	 */
	private void dispatchResetMailingThread(UserRegistrationForm registrationForm, UserCredentials userCredentials) {
		String mailId = registrationForm.getMailId();
		String password = registrationForm.getPassword();
		Thread mailThread = new Thread(new Runnable() {
			@Override
			public void run() {
				boolean inviteSent = false;
				try {
						inviteSent = mailSendingService.sendCandidateResetMail(mailId, password);
				} catch (Exception e) {
					e.printStackTrace();
				}
				userCredentials.setInviteSent(inviteSent);
				userCredentialRepo.save(userCredentials);
			}
		});
		mailThread.start();
	}
}

