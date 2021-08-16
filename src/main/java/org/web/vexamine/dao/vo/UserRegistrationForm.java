package org.web.vexamine.dao.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Instantiates a new user registration form.
 *
 * @param mailId the mail id
 * @param password the password
 * @param company the company
 * @param questionBankList the question bank list
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationForm{
	
	private String mailId;
	
	private String password;
	
	private String company;
	
	private List<String> questionBankList;
	
	private String switchType;
	
	private Long purchasedCredits;

}
