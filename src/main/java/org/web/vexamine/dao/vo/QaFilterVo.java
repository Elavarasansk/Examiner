package org.web.vexamine.dao.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Instantiates a new qa filter vo.
 *
 * @param question the question
 * @param category the category
 * @param subCategory the sub category
 * @param questionBankName the question bank name
 * @param questionType the question type
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QaFilterVo {

	private String question;
	
	private String category;
	
	private String subCategory;
	
	private String questionBankName;
	
	private Integer questionType;
	
}
