package org.web.vexamine.dao.vo;

import java.sql.Timestamp;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Instantiates a new manager vo.
 *
 * @param id the id
 * @param company the company
 * @param userId the user id
 * @param mailId the mail id
 * @param questionBankList the question bank list
 * @param questionBankName the question bank name
 * @param expirationDate the expiration date
 * @param suggestLimit the suggest limit
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManagerVo {

	private Long id;

	private String company;
	
	private Long userId;
	
	private String mailId;

	private List<String> questionBankList;

	private String questionBankName;
	
	private Timestamp expirationDate;
	
	private Integer suggestLimit;

}
