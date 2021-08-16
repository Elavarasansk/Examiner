package org.web.vexamine.utils.vo;

import java.util.Date;

/**
 * The Interface WebTokenInfo.
 */
public interface WebTokenInfo{

	String WebTokenCookieName = "VEXAMINE-JWT-TOKEN";
	
	Date SessionExpirationTime =  new Date(System.currentTimeMillis() + 3600000);
	
	String SessionSecretKey = "testConduct-VEXAMINE";

}