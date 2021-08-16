package org.web.vexamine.dao.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

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
@Table(name = "vexamine_manager_credit")
public class ManagerCredit implements Serializable {
	
	private static final long serialVersionUID = -1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "manager_auth_id")
	private UserAuthorityInfo userAuthorityInfo;

	/*@Transient
	private Set<UserAuthorityInfo> candidateList ;*/
	
	@ManyToMany
	@Column(name = "candidate_list")
	@JoinColumn(name = "id")
	private Set<UserAuthorityInfo> candidateList ;
	
	private Long purchasedCredits;
	
	private Long usedCredits;	
	
	private String updateUser;			
	
	private String createUser;
	
	@CreationTimestamp
	private Date createDate;

	@UpdateTimestamp
	private Date updateDate;

}
