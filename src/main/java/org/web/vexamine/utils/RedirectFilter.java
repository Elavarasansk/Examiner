package org.web.vexamine.utils;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.web.vexamine.dao.entity.UserAuthorityInfo;
import org.web.vexamine.dao.entity.UserCredentials;
import org.web.vexamine.dao.repository.UserAuthorityInfoRepository;
import org.web.vexamine.dao.repository.UserCredentialRepository;
import org.web.vexamine.utils.constants.Servletconstants;
import org.web.vexamine.utils.storage.CookieSessionStorage;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class RedirectFilter.
 */
@Component

/** The Constant log. */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RedirectFilter implements Filter {

	@Autowired
	private UserAuthorityInfoRepository userAuthInfoRepo;

	@Autowired
	private UserCredentialRepository userCredsRepo;

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws ServletException, IOException {
		HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
		HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

		String requestURI = httpRequest.getRequestURI();
		String redirectURI = httpRequest.getContextPath() + "/home";

		boolean isLoggedIn = validateSession(httpRequest, httpResponse);
		boolean isBasic = allowBasicPages(requestURI);
		boolean isStatic = allowStaticResources(requestURI);

		if (isLoggedIn) {
			if (requestURI.equals("/")) { //isBasic ||  
				httpResponse.sendRedirect(httpRequest.getContextPath() + "/home");
			} else if (requestURI.contains(".html")) {
				checkUserRole(requestURI, httpRequest, httpResponse, chain);
			} else {
				chain.doFilter(httpRequest, httpResponse);
			}
		} else if (isStatic) {
			chain.doFilter(httpRequest, httpResponse);
		} else if (isBasic) {
			chain.doFilter(httpRequest, httpResponse);
		} else {
			httpResponse.sendRedirect(redirectURI);
		}
	}

	/**
	 * Allow basic pages.
	 *
	 * @param requestURI the request URI
	 * @return true, if successful
	 */
	private boolean allowBasicPages(String requestURI) {
		return (requestURI.contains(Servletconstants.HOME) || requestURI.contains("authenticate")
				|| requestURI.contains(Servletconstants.LOGIN));
	}

	/**
	 * Allow static resources.
	 *
	 * @param requestURI the request URI
	 * @return true, if successful
	 */
	private boolean allowStaticResources(String requestURI) {
		return (requestURI.contains("built/") || requestURI.contains(".png") || requestURI.contains(".ico")
				|| requestURI.contains(".jpg"));
	}

	/**
	 * Validate session.
	 *
	 * @param httpRequest the http request
	 * @param httpResponse the http response
	 * @return true, if successful
	 */
	private boolean validateSession(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		String webToken = CookieUtil.requestTokenFromCookie(httpRequest);
		if (!StringUtils.isEmpty(webToken)) {
			try {
				Claims jsonClaim = JWTUtil.parseCookieSession(webToken); // store the information in ThreadLocal.
				CookieUtil.localCookieStorage(httpRequest, jsonClaim);
				return true;
			} catch (ExpiredJwtException | MalformedJwtException e) {
				CookieUtil.destroy(httpResponse);
				return false;
			}
		}
		return false;
	}

	/**
	 * Check user role.
	 *
	 * @param requestURI the request URI
	 * @param httpRequest the http request
	 * @param httpResponse the http response
	 * @param chain the chain
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ServletException the servlet exception
	 */
	private void checkUserRole(String requestURI, HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			FilterChain chain) throws IOException, ServletException {
		String contextPath = httpRequest.getContextPath();
		String loggedInUser = CookieSessionStorage.get().getUserName();
		UserCredentials userCredentials = userCredsRepo.findByMailId(loggedInUser);
		UserAuthorityInfo userAuthDetails = userAuthInfoRepo.findByUserCredentialsId(userCredentials.getId());

		String userRole = userAuthDetails.getUserRole().getType();
		String redirectPath = contextPath + "/home.html";


		if(requestURI.contains("home.html")) {
			chain.doFilter(httpRequest, httpResponse);
			return;
		}

		switch (userRole) {
		case Servletconstants.SUPER_ADMIN:
			if (!requestURI.contains("admin")) {
				redirectPath = contextPath + "/admin.html";
				httpResponse.sendRedirect(redirectPath);
			}
			break;

		case Servletconstants.MANAGER:
			if (!requestURI.contains("manager")) {
				redirectPath = contextPath + "/manager.html";
				httpResponse.sendRedirect(redirectPath);
			}
			break;

		case Servletconstants.CANDIDATE:
			if (!requestURI.contains("candidate")) {
				redirectPath = contextPath + "/candidate.html";
				httpResponse.sendRedirect(redirectPath);
			}
			break;
		}
		chain.doFilter(httpRequest, httpResponse);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.info(String.format("%s::%s::%s", getClass(), getClass().getSimpleName(), "Redirection Filter intiated"));
	}

	@Override
	public void destroy() {
		log.info(String.format("%s::%s::%s", getClass(), getClass().getSimpleName(), "Redirection Filter stopped"));
	}

}