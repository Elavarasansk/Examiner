package org.web.vexamine.dao.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Instantiates a new question answer vo.
 *
 * @param question
 *            the question
 * @param mcqOption1
 *            the mcq option 1
 * @param mcqOption2
 *            the mcq option 2
 * @param mcqOption3
 *            the mcq option 3
 * @param mcqOption4
 *            the mcq option 4
 * @param choiceOption1
 *            the choice option 1
 * @param choiceOption2
 *            the choice option 2
 * @param answer
 *            the answer
 * @param questionType
 *            the question type
 * @param questionBankName
 *            the question bank name
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionAnswerSingleVo {

	private String category;

	private String subCategory;

	private String questionBankName;

	private Integer questionType;

	private String question;

	private String mcqOption1;

	private String mcqOption2;

	private String mcqOption3;

	private String mcqOption4;

	private String choiceOption1;

	private String choiceOption2;

	private String answer;

}
