package org.web.vexamine.service;

import java.util.Arrays;

import javax.security.sasl.AuthenticationException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.web.vexamine.dao.entity.UserAuthorityInfo;
import org.web.vexamine.dao.entity.UserCredentials;
import org.web.vexamine.dao.repository.UserAuthorityInfoRepository;
import org.web.vexamine.dao.repository.UserCredentialRepository;
import org.web.vexamine.dao.vo.CredentialAuthorityVo;
import org.web.vexamine.utils.CookieUtil;
import org.web.vexamine.utils.JWTUtil;
import org.web.vexamine.utils.PasswordUtils;
import org.web.vexamine.utils.storage.CookieSessionStorage;
import org.web.vexamine.utils.vo.CookieSessionInfo;

import javassist.NotFoundException;

/**
 * The Class LoginService.
 */
@Service
public class LoginService {

	/** The user credential repo. */
	@Autowired
	private UserCredentialRepository userCredentialRepo;

	/** The user authority info repo. */
	@Autowired
	private UserAuthorityInfoRepository userAuthorityInfoRepo;
	
	@Autowired
	private TestAssignmentService testAssignmentService;

	/**
	 * Authorize application.
	 *
	 * @param userName the user name
	 * @param password the password
	 * @return the string
	 * @throws AuthenticationException the authentication exception
	 */
	public String authorizeApplication(String userName, String password)throws AuthenticationException {
		UserCredentials userCredentials = checkUserExists(userName);
		validatePassword(password, userCredentials);
		return getUserAuthDetails(userCredentials.getId());
	}

	/**
	 * Validate password.
	 *
	 * @param password the password
	 * @param userCredentials the user credentials
	 * @return true, if successful
	 * @throws AuthenticationException the authentication exception
	 */
	private boolean validatePassword(String password, UserCredentials userCredentials)
			throws AuthenticationException {
		byte[] secureSalt = userCredentials.getHashedSalt();
		byte[] hashedPassword = PasswordUtils.hashPasswordWithSalt(password.toCharArray(), secureSalt);
		if(Arrays.equals(hashedPassword, userCredentials.getHashedPassword())){
			return true;
		}

		throw new AuthenticationException("Invalid Credentials for"
				+ " user - \""+userCredentials.getMailId()+"\"");
	}

	/**
	 * Check user exists.
	 *
	 * @param userName the user name
	 * @return the user credentials
	 */
	private UserCredentials checkUserExists(String userName) {
		UserCredentials userCredentials = userCredentialRepo.findByMailId(userName);
		if(ObjectUtils.isEmpty(userCredentials)) {
			throw new IllegalArgumentException("User - \""+userName+"\" doesn't exists."
					+ " So please verify your userName");
		}
		return userCredentials;
	}

	/**
	 * Gets the user auth details.
	 *
	 * @param userId the user id
	 * @return the user auth details
	 */
	private String getUserAuthDetails(Long userId) {
		UserAuthorityInfo userAuthInfo = userAuthorityInfoRepo.findByUserCredentialsId(userId);
		String roleType = userAuthInfo.getUserRole().getType();

		String redirectionHtml = "/home.html";  
		switch(roleType) {
			case "CANDIDATE":
				redirectionHtml = "/candidate.html";  
				testAssignmentService.updateTestResults(userAuthInfo);
				break;
			case "MANAGER":
				redirectionHtml = "/manager.html";  
				break;
			case "SUPER_ADMIN":
				redirectionHtml = "/admin.html";  
				break;
		}
		return redirectionHtml;
	}

	/**
	 * Maintain user session.
	 *
	 * @param userName the user name
	 * @param servletResponse the servlet response
	 * @return the cookie
	 */
	public Cookie maintainUserSession(String userName, HttpServletResponse servletResponse) {
		String webToken = JWTUtil.buildWebToken(userName);

		CookieSessionInfo cookieSessionInfo = CookieSessionInfo.builder()
				.webToken(webToken)
				.path("/")      // allows reuests from path mentioned as prefix.
				.maxAge(-1)     // Session will be Maintained till browser shutsdown.
				.httpsFlag(false)
				//				.domain("vexamine.com")
				.build();
		return CookieUtil.create(cookieSessionInfo);
	}

	/**
	 * Gets the user authority.
	 *
	 * @param httpRequest the http request
	 * @return the user authority
	 * @throws NotFoundException the not found exception
	 */
	public CredentialAuthorityVo getUserAuthority(HttpServletRequest httpRequest) throws NotFoundException {
		String webToken = CookieUtil.requestTokenFromCookie(httpRequest);
		if(StringUtils.isEmpty(webToken)) {
			return CredentialAuthorityVo.builder()
					.username(null)
					.role(null)
					.roleInfo(null)
					.build();
		}
		
		String currentLoginUser = CookieSessionStorage.get().getUserName();
		
		UserCredentials userCredentials = checkUserExists(currentLoginUser);
		UserAuthorityInfo userAuthInfo = userAuthorityInfoRepo.findByUserCredentialsId(userCredentials.getId());
		
		return CredentialAuthorityVo.builder()
				.username(currentLoginUser)
				.role(userAuthInfo.getUserRole().getType())
				.roleInfo(userAuthInfo.getUserRole().getDescription())
				.build();
	}

}
