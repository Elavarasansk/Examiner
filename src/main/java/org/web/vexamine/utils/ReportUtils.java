package org.web.vexamine.utils;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;
import org.web.vexamine.constants.VexamineTestConstants;

/**
 * The Class ReportUtils.
 */
public class ReportUtils {

	/**
	 * Format date.
	 *
	 * @param date the date
	 * @return the string
	 */
	public static String formatDate(Object date) {
		if(ObjectUtils.isEmpty(date)) {
			return StringUtils.EMPTY;
		}
		Timestamp timestamp = Timestamp.valueOf(String.valueOf(date));
		return timestamp.toLocalDateTime().toLocalDate().toString();
	}

	/**
	 * Form time taken.
	 *
	 * @param timeStamp the time stamp
	 * @return the string
	 */
	public static String formTimeTaken(Long timeStamp) {
		if(timeStamp == 0) {
			return StringUtils.EMPTY;
		}
		StringBuilder  timeTakenBuilder= new StringBuilder();
		long minutes = TimeUnit.MILLISECONDS.toMinutes(timeStamp);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(timeStamp);
		if(minutes!= VexamineTestConstants.INITIAL ) {
		timeTakenBuilder.append(minutes).append(StringUtils.SPACE).append(VexamineTestConstants.MINUTES).append(StringUtils.SPACE);;
		long second =  Math.subtractExact(seconds,  minutes*60);
		 if(second > VexamineTestConstants.INITIAL ) {
				timeTakenBuilder.append(second).append(StringUtils.SPACE).append(VexamineTestConstants.SECONDS).append(StringUtils.SPACE);;
		 }
		}else if(seconds !=  VexamineTestConstants.INITIAL ) {
		timeTakenBuilder.append(seconds).append(StringUtils.SPACE).append(VexamineTestConstants.SECONDS).append(StringUtils.SPACE);;
		}
		
		return timeTakenBuilder.toString();
	}

}
