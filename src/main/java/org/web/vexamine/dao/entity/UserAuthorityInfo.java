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
 * Instantiates a new user authority info.
 *
 * @param id the id
 * @param userCredentials the user credentials
 * @param userRole the user role
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
@Table(name = "vexamine_user_authority_info")
public class UserAuthorityInfo implements Serializable {
	
	private static final long serialVersionUID = -1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "user_id")
	private UserCredentials userCredentials;
	
	@OneToOne
	@JoinColumn(name = "user_role_id")
	private Role userRole;

	private String createUser;

	@CreationTimestamp
	private Date createDate;

	private String updateUser;

	@UpdateTimestamp
	private Date updateDate;

	
/*
 * TODO need to fix
 * 
 * 	*//**
	 * Gets the user credentials.
	 *
	 * @return the user credentials
	 *//*
	public UserCredentials getUserCredentials() {
		userCredentials.setHashedPassword(null);
		userCredentials.setHashedSalt(null);
		return userCredentials;
	}*/

}
