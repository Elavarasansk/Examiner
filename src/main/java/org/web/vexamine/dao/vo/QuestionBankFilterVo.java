package org.web.vexamine.dao.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Instantiates a new question bank filter vo.
 *
 * @param category the category
 * @param subCategory the sub category
 * @param questionBankName the question bank name
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionBankFilterVo {

	private String category;

	private String subCategory;
	
	private String questionBankName;
}
