package org.web.vexamine.dao.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Instantiates a new user details vo.
 *
 * @param name the name
 * @param mailId the mail id
 * @param role the role
 * @param suggestLimit the suggest limit
 * @param offset the offset
 * @param limit the limit
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsVo {
	
	private String name;
	
	private String mailId;
	
	private String role;
	
	private Integer suggestLimit;
	
	private Integer offset;
	
	private Integer limit;

}
