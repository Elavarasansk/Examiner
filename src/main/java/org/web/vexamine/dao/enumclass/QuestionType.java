package org.web.vexamine.dao.enumclass;

import lombok.Getter;

/**
 * The Enum QuestionType.
 */
public enum QuestionType {

	DECLARATIVE(0), //descriptive answer

	TWO_CHOICE(1),  //yes or no

	MULTIPLE_CHOICE(2); //choosing the best answer

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	@Getter
	private Integer type;

	/**
	 * Instantiates a new question type.
	 *
	 * @param type the type
	 */
	QuestionType(Integer type) {
		this.type = type;
	}
}
