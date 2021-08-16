package org.web.vexamine.utils.vo;

import javax.servlet.http.HttpServletResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Instantiates a new cookie session info.
 *
 * @param servletResponse the servlet response
 * @param webToken the web token
 * @param httpsFlag the https flag
 * @param maxAge the max age
 * @param domain the domain
 * @param path the path
 */
@Data
@Builder
@AllArgsConstructor

public class CookieSessionInfo{
	
	private HttpServletResponse servletResponse;
	
	private String webToken;
	
	private Boolean httpsFlag;
	
	private Integer maxAge;
	
	private String domain;
	
	private String path;
	
}