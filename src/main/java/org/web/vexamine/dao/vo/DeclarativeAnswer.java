package org.web.vexamine.dao.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Instantiates a new declarative answer.
 *
 * @param question the question
 * @param correctAnswer the correct answer
 * @param answer the answer
 * @param id the id
 * @param testAssignmentId the test assignment id
 * @param validAnswer the valid answer
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeclarativeAnswer {
	
	private String question;
	
	private String correctAnswer;
	
	private String answer;
	
	private Long id;
	
	private Long testAssignmentId;
	
	private boolean validAnswer;
}
