package org.web.vexamine.dao.vo;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Instantiates a new test summary vo.
 *
 * @param id the id
 * @param userAuthId the user auth id
 * @param mailId the mail id
 * @param mailIdName the mail id name
 * @param questionBankId the question bank id
 * @param questionBankName the question bank name
 * @param category the category
 * @param subCategory the sub category
 * @param testAssignmentId the test assignment id
 * @param questionTaken the question taken
 * @param answeredCorrect the answered correct
 * @param answeredWrong the answered wrong
 * @param unanswered the unanswered
 * @param totalMarkObtained the total mark obtained
 * @param timeTaken the time taken
 * @param testDate the test date
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
public class TestSummaryVo {

	private Long id;

	private Long userAuthId;
	
	private Long mailId;
	
	private String mailIdName;

	private Long questionBankId;
	
	private String questionBankName;
	
	private String category;

	private String subCategory;
	
	private Long testAssignmentId;
	
	private Integer questionTaken;

	private Integer answeredCorrect;

	private Integer answeredWrong;

	private Integer unanswered;

	private Integer totalMarkObtained;

	private Timestamp timeTaken;
	
	private Timestamp testDate;
	
	private Integer limit;
	
	private Integer offset;
	
	private String searchText;
	
    private String sortKey;
    
    private String sortType; 
    
    private Boolean reportSent;
	

}
