package org.web.vexamine.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.util.WebUtils;
import org.web.vexamine.utils.storage.CookieSession;
import org.web.vexamine.utils.storage.CookieSessionStorage;
import org.web.vexamine.utils.vo.CookieSessionInfo;
import org.web.vexamine.utils.vo.WebTokenInfo;

import io.jsonwebtoken.Claims;

/**
 * The Class CookieUtil.
 */
public class CookieUtil {
	
	
    /**
     * Creates the.
     *
     * @param cookieSessionInfo the cookie session info
     * @return the cookie
     */
    public static Cookie create(CookieSessionInfo cookieSessionInfo) {
        Cookie cookie = new Cookie(WebTokenInfo.WebTokenCookieName, cookieSessionInfo.getWebToken());
        cookie.setSecure(cookieSessionInfo.getHttpsFlag());
//        cookie.setDomain(cookieSessionInfo.getDomain());// domain-name if exists. Ex: https://www.vexamine.com/
        cookie.setMaxAge(cookieSessionInfo.getMaxAge());
        cookie.setPath(cookieSessionInfo.getPath());
        cookie.setHttpOnly(true);
        return cookie;
    }

    /**
     * Destroy.
     *
     * @param servletResponse the servlet response
     */
    public static void destroy(HttpServletResponse servletResponse) {
        Cookie cookie = new Cookie(WebTokenInfo.WebTokenCookieName, null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        servletResponse.addCookie(cookie);
    }

    /**
     * Request token from cookie.
     *
     * @param servletRequest the servlet request
     * @return the string
     */
    public static String requestTokenFromCookie(HttpServletRequest servletRequest) {
        Cookie sessionCookie = WebUtils.getCookie(servletRequest, WebTokenInfo.WebTokenCookieName);
        return sessionCookie != null ? sessionCookie.getValue() : null;
    }
    
    /**
     * Local cookie storage.
     *
     * @param httpRequest the http request
     * @param jsonClaim the json claim
     */
    public static void localCookieStorage(HttpServletRequest httpRequest, Claims jsonClaim) {
    	CookieSession cookieSession = CookieSession.builder()
    			.userName((String) jsonClaim.get("signedInUser"))
    			.userRole(null)
    			.remoteIp(httpRequest.getRemoteAddr())
    			.sessionId(jsonClaim.getId())
    			.build();
        CookieSessionStorage.set(cookieSession);
    }
}

