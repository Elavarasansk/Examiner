package org.web.vexamine.dao.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Instantiates a new question answer vo.
 *
 * @param id the id
 * @param questionBankId the question bank id
 * @param question the question
 * @param mcqOption1 the mcq option 1
 * @param mcqOption2 the mcq option 2
 * @param mcqOption3 the mcq option 3
 * @param mcqOption4 the mcq option 4
 * @param choiceOption1 the choice option 1
 * @param choiceOption2 the choice option 2
 * @param answer the answer
 * @param questionType the question type
 * @param questionBankName the question bank name
 * @param limit the limit
 * @param offset the offset
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionAnswerVo {	

	private Long id;

	private Long questionBankId;
	
	private String question;
	
	private String mcqOption1;

	private String mcqOption2;

	private String mcqOption3;

	private String mcqOption4;
	
	private String choiceOption1;
	
	private String choiceOption2;

	private String answer;

	private Integer questionType;
	
	private String questionBankName;
	
	private Integer limit;
	
	private Integer offset;

}
