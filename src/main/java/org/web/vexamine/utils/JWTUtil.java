package org.web.vexamine.utils;

import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;
import org.web.vexamine.utils.vo.WebTokenInfo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * The Class JWTUtil.
 */
public class JWTUtil {

	/**
	 * Builds the web token.
	 *
	 * @param signedInUser the signed in user
	 * @return the string
	 */
	public static String buildWebToken(String signedInUser) {
		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);

		JwtBuilder webTokenBuilder = Jwts.builder()
				.claim("signedInUser", signedInUser)
				.setSubject(signedInUser)
				.setIssuedAt(now)
				.setId(UUID.randomUUID().toString())
//				.setExpiration(WebTokenInfo.SessionExpirationTime)
				.signWith(SignatureAlgorithm.HS256, WebTokenInfo.SessionSecretKey);

		return webTokenBuilder.compact();
	}

	/**
	 * Gets the token subject.
	 *
	 * @param httpServletRequest the http servlet request
	 * @return the token subject
	 */
	public static String getTokenSubject(HttpServletRequest httpServletRequest){
		String webToken = CookieUtil.requestTokenFromCookie(httpServletRequest);
		if(StringUtils.isEmpty(webToken)) {
			return null;
		}
		return parseCookieSession(webToken).getSubject();
	}

	/**
	 * Parses the cookie session.
	 *
	 * @param webToken the web token
	 * @return the claims
	 */
	public static Claims parseCookieSession(String webToken) {
		Claims jsonClaim = Jwts.parser()
				.setSigningKey(WebTokenInfo.SessionSecretKey)
				.parseClaimsJws(webToken)
				.getBody();
		return jsonClaim;
	}

}