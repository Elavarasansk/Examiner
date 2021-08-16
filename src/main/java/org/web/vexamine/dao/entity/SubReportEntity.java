package org.web.vexamine.dao.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubReportEntity {
	
	private String question;
	
	private String answer;
	
	private String correctAnswer;

}
