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
 * Instantiates a new test summary.
 *
 * @param id the id
 * @param testAssignment the test assignment
 * @param answeredCorrect the answered correct
 * @param answeredWrong the answered wrong
 * @param unanswered the unanswered
 * @param totalMarkObtained the total mark obtained
 * @param timeTaken the time taken
 * @param createUser the create user
 * @param createDate the create date
 * @param updateUser the update user
 * @param updateDate the update date
 */

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vexamine_test_summary")
public class TestSummary implements Serializable{
	
	private static final long serialVersionUID = -1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "test_assignment_id")
	private TestAssignment testAssignment;
	
	private Long answeredCorrect;

	private Long answeredWrong;

	private Long unanswered;

	private Long totalMarkObtained;

	private Long timeTaken;
	
	private String createUser;
	
	private Boolean reportSent;
	
	@CreationTimestamp
	private Date createDate;
	
	private String updateUser;
	
	@UpdateTimestamp
	private Date updateDate;


}
