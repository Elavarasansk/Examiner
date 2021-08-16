package org.web.vexamine.dao.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CredentialAuthorityVo {
	
	private String username;
	
	private String role;
	
	private String roleInfo;

}
