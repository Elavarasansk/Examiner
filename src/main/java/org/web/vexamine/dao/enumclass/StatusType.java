package org.web.vexamine.dao.enumclass;

import lombok.Getter;

/**
 * The Enum StatusType.
 */
public enum StatusType {
	
	NEW("New"),
	
	INPROGRESS("Inprogress"),
	
	SUBMITTED("Submitted"),
	
	COMPLETED("Completed"),
	
	EXPIRED("Expired"),
	
	REJECTED("Rejected");	

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	@Getter
	private String type;

	/**
	 * Instantiates a new status type.
	 *
	 * @param type the type
	 */
	StatusType(String type) {
		this.type = type;
	}

}
