package org.web.vexamine.dao.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Instantiates a new user authority vo.
 *
 * @param signedInUser the signed in user
 * @param mailId the mail id
 * @param roles the roles
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthorityVo {
	
	private String signedInUser;
	
	private String mailId;
	
	private List<String> roles;
}
