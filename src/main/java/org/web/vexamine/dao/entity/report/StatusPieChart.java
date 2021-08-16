package org.web.vexamine.dao.entity.report;

import lombok.Builder;
import lombok.Data;

/**
 * @author
 *
 */
@Data
@Builder
public class StatusPieChart {
	
	private long newCount;
	
	private long inprogressCount;
	
	private long completedCount;
	
	private long expiredCount;
	
	private long rejectedCount;

}
