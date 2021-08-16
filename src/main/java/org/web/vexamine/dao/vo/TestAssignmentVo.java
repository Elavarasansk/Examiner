package org.web.vexamine.dao.vo;

import java.sql.Timestamp;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Instantiates a new test assignment vo.
 *
 * @param id the id
 * @param userAuthId the user auth id
 * @param questionBankId the question bank id
 * @param mailId the mail id
 * @param questionBankName the question bank name
 * @param questionsCount the questions count
 * @param assigneeList the assignee list
 * @param inviteSent the invite sent
 * @param status the status
 * @param expired the expired
 * @param expirationTime the expiration time
 * @param testStartTime the test start time
 * @param testEndTime the test end time
 * @param allowedStartDate the allowed start date
 * @param limit the limit
 * @param offset the offset
 * @param searchText the search text
 * @param sortKey the sort key
 * @param sortType the sort type
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestAssignmentVo {

	private Long id;
	
	private Long userAuthId;
	
	private Long questionBankId;
	
	private String mailId;
	
	private String questionBankName;
	
	private Integer questionsCount;
	
	private List<String> assigneeList;
		
	private Boolean inviteSent;
	
	private String status ;
		
	private Boolean expired;
	
	private Timestamp expirationTime;
	
	private Timestamp testStartTime;
	
	private Timestamp testEndTime;
	
	private Timestamp allowedStartDate;
	
	private long allowedTime;
	
	private Integer limit;
	
	private Integer offset;
	
    private String searchText;
    
    private String sortKey;
    
    private String sortType; 

}