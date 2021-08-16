package org.web.vexamine.controller;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web.vexamine.dao.vo.CredentialAuthorityVo;
import org.web.vexamine.dao.vo.UserRegistrationForm;
import org.web.vexamine.service.LoginService;
import org.web.vexamine.utils.CookieUtil;

import javassist.NotFoundException;

/**
 * The Class LoginController.
 */
@RestController
@RequestMapping("/")
public class LoginController {

	@Autowired
	private LoginService loginService;
	
	/**
	 * Redirect vexamine home.
	 *
	 * @param httpRequest the http request
	 * @param httpResponse the http response
	 */
	@GetMapping("/home")
	public void redirectVexamineHome(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		try {
			httpResponse.sendRedirect("/home.html");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Login user.
	 *
	 * @param signUpForm the sign up form
	 * @param httpRequest the http request
	 * @param httpResponse the http response
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@PostMapping("/validate/login")
	public String loginUser(@RequestBody UserRegistrationForm signUpForm,HttpServletRequest httpRequest, HttpServletResponse httpResponse)throws IOException {
		String userName = signUpForm.getMailId();
		String password = signUpForm.getPassword();

		String redirectionHtml = loginService.authorizeApplication(userName, password);

		Cookie cookie = loginService.maintainUserSession(userName, httpResponse);
		httpResponse.addCookie(cookie);
		return redirectionHtml;
	}

	/**
	 * Logout session.
	 *
	 * @param httpRequest the http request
	 * @param httpResponse the http response
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@GetMapping("/logout")
	public void logoutSession(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException{
		CookieUtil.destroy(httpResponse);
		httpResponse.sendRedirect("/login");
	}
	
	/**
	 * Gets the userauthentication.
	 *
	 * @param httpRequest the http request
	 * @return the userauthentication
	 * @throws NotFoundException the not found exception
	 */
	@GetMapping("/authenticate/user")
	public CredentialAuthorityVo getUserauthentication(HttpServletRequest httpRequest) throws NotFoundException{
		return loginService.getUserAuthority(httpRequest);
	}

}
