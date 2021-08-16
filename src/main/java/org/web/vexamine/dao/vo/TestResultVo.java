package org.web.vexamine.dao.vo;

import java.sql.Timestamp;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Instantiates a new test result vo.
 *
 * @param id the id
 * @param userAuthId the user auth id
 * @param questionId the question id
 * @param testAssignmentId the test assignment id
 * @param validAnswer the valid answer
 * @param status the status
 * @param timeTaken the time taken
 * @param answer the answer
 * @param updateMap the update map
 * @param summaryId the summary id
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestResultVo {

	private Long id;
	
	private Long userAuthId;
	
	private Long questionId;
	
	private Long testAssignmentId;
	
	private Boolean validAnswer;
	
	private String status;

	private Timestamp timeTaken;
	
	private String answer;
	
	private Map<Long,Boolean> updateMap;
	
	private Long summaryId;


}
