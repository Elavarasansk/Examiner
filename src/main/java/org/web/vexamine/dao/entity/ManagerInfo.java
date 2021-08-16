package org.web.vexamine.dao.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Basic;
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
 * Instantiates a new manager info.
 *
 * @param id the id
 * @param userAuthorityInfo the user authority info
 * @param company the company
 * @param questionBank the question bank
 * @param expirationDate the expiration date
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
@Table(name = "vexamine_manager_info")
public class ManagerInfo implements Serializable {
	
	private static final long serialVersionUID = -1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "user_auth_id")
	private UserAuthorityInfo userAuthorityInfo;
	
	private String company;
	
	@OneToOne
	@JoinColumn(name = "question_bank_id",nullable=true)
	private QuestionBank questionBank; 

	private Timestamp expirationDate;

	private String createUser;

	@CreationTimestamp
	private Date createDate;

	private String updateUser;

	@UpdateTimestamp
	private Date updateDate;

}
