package org.web.vexamine.dao.entity;

import java.io.Serializable;
import java.sql.Timestamp;
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
 * Instantiates a new test result.
 *
 * @param id the id
 * @param userAuthorityInfo the user authority info
 * @param questionAnswer the question answer
 * @param testAssignment the test assignment
 * @param validAnswer the valid answer
 * @param status the status
 * @param answer the answer
 * @param timeTaken the time taken
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
@Table(name = "vexamine_test_results")
public class TestResult implements Serializable {

	private static final long serialVersionUID = -1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "user_auth_id")
	private UserAuthorityInfo userAuthorityInfo;

	@OneToOne
	@JoinColumn(name = "question_id")
	private QuestionAnswer questionAnswer;		
	
	@OneToOne
	@JoinColumn(name = "test_assignment_id")
	private TestAssignment testAssignment;	
	
	private Boolean validAnswer;
	
	private String status;
	
	private String answer;

	private Timestamp timeTaken;
	
	private String createUser;

	@CreationTimestamp
	private Date createDate;
	
	private String updateUser;
	
	@UpdateTimestamp
	private Date updateDate; 
}
