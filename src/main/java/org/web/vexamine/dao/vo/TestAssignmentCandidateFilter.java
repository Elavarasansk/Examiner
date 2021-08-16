package org.web.vexamine.dao.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Instantiates a new test assignment candidate filter.
 *
 * @param authorityId the authority id
 * @param questionBankName the question bank name
 * @param category the category
 * @param subCategory the sub category
 * @param status the status
 * @param allowedStartDateSort the allowed start date sort
 * @param expirationTimeSort the expiration time sort
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestAssignmentCandidateFilter {

	private Long authorityId;
	
	private String questionBankName;
	
	private String category;
	
	private String subCategory;
	
	private String status ;
		
	private String allowedStartDateSort;
	
	private String expirationTimeSort;
	
}