package org.web.vexamine.dao.enumclass;

import lombok.Getter;

/**
 * The Enum QuestionStatus.
 */
public enum QuestionStatus {
	
	ANSWERED("Answered"),
	
	UNANSWERED("Unanswered");	

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	@Getter
	private String type;

	/**
	 * Instantiates a new question status.
	 *
	 * @param type the type
	 */
	QuestionStatus(String type) {
		this.type = type;
	}
}
