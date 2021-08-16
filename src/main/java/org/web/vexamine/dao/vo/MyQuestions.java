package org.web.vexamine.dao.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MyQuestions {

	private Long id;
	
	private String question;

	private String mcqOption1;

	private String mcqOption2;

	private String mcqOption3;

	private String mcqOption4;

	private String choiceOption1;
	
	private String choiceOption2;

	private Integer questionType;
	
	private String selectedAnswer;

	private Long resultId;
	
}
