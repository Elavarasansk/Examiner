package org.web.vexamine.dao.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Instantiates a new question bank vo.
 *
 * @param id the id
 * @param categoryId the category id
 * @param questionBankName the question bank name
 * @param offset the offset
 * @param limit the limit
 * @param suggestLimit the suggest limit
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionBankVo {

	private Long id;
	
	private Long categoryId;
	
	private String questionBankName;
	
	private Integer offset;
	
	private Integer limit;
	
	private Integer suggestLimit;
}
