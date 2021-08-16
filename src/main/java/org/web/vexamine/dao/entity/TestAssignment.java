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
 * Instantiates a new test assignment.
 *
 * @param id the id
 * @param userAuthorityInfo the user authority info
 * @param questionBank the question bank
 * @param questionsCount the questions count
 * @param inviteSent the invite sent
 * @param status the status
 * @param expired the expired
 * @param expirationTime the expiration time
 * @param testStartTime the test start time
 * @param testEndTime the test end time
 * @param allowedStartDate the allowed start date
 * @param createUser the create user
 * @param createDate the create date
 * @param updateUser the update user
 * @param updateDate the update date
 */
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "vexamine_test_assignment")
public class TestAssignment  implements Serializable{
	
	private static final long serialVersionUID = -1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "user_auth_id")
	private UserAuthorityInfo userAuthorityInfo;

	@OneToOne
	@JoinColumn(name = "question_bank_id")
	private QuestionBank questionBank;

	private Integer questionsCount;
	
	private Boolean inviteSent;

	private String status;

	private Boolean expired;

	private Timestamp expirationTime;

	private Timestamp testStartTime;

	private Timestamp testEndTime;

	private Timestamp allowedStartDate;

	private Long allowedTime;
	
	private String createUser;

	
	@CreationTimestamp
	private Date createDate;

	
	private String updateUser;

	
	@UpdateTimestamp
	private Date updateDate;


}
