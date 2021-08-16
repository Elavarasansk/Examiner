package org.web.vexamine.dao.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Instantiates a new question answer.
 *
 * @param id the id
 * @param question the question
 * @param mcqOption1 the mcq option 1
 * @param mcqOption2 the mcq option 2
 * @param mcqOption3 the mcq option 3
 * @param mcqOption4 the mcq option 4
 * @param choiceOption1 the choice option 1
 * @param choiceOption2 the choice option 2
 * @param answer the answer
 * @param questionBank the question bank
 * @param questionType the question type
 * @param createUser the create user
 * @param createDate the create date
 * @param updateUser the update user
 * @param updateDate the update date
 */
@Data
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
@Table(name = "vexamine_question_answer")
public class QuestionAnswer implements Serializable {

	private static final long serialVersionUID = -1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String question;

	private String mcqOption1;

	private String mcqOption2;

	private String mcqOption3;

	private String mcqOption4;
	
	private String choiceOption1;
	
	private String choiceOption2;

	private String answer;

	@OneToOne
	@JoinColumn(name = "question_bank_id")
	private QuestionBank questionBank;
	
	private Integer questionType;

	private String createUser;

	@CreationTimestamp
	private Date createDate;

	private String updateUser;

	@UpdateTimestamp
	private Date updateDate; 


}