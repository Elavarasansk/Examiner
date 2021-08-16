package org.web.vexamine.dao.entity.report;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TestDetailsReportEntity {
	
	private String questionBankName;
	
	private String category;
	
	private String subcategory;
	
	private long totalCandidate;
	
	private long questionsCount;
	
	private long newCount;
	
	private long inprogressCount;
	
	private long completedCount;
	
	private long expiredCount;
	
	private long rejectedCount;

	private String newLabel;
	
	private String inprogressLabel;
	
	private String completedLabel;
	
	private String expiredLabel;
	
	private String rejectedLabel;

}
