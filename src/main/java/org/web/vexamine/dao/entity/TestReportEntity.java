package org.web.vexamine.dao.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TestReportEntity {
	
	private String mailId;
	
	private long answeredCorrect;

	private long answeredWrong;
	
	private long unanswered;
	
	private long totalMarksObtained;
	
	private long questionsCount;
	
}
