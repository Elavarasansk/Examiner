package org.web.vexamine.dao.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Instantiates a new user credentials.
 *
 * @param id the id
 * @param mailId the mail id
 * @param userName the user name
 * @param hashedPassword the hashed password
 * @param hashedSalt the hashed salt
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
@Table(name = "vexamine_user_credentials")
public class UserCredentials implements Serializable {

  private static final long serialVersionUID = -1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String mailId;

	private String userName;

	private byte[] hashedPassword;

	private byte[] hashedSalt;
	
	private String createUser;

	private boolean inviteSent;

	@CreationTimestamp
	private Date createDate;

	private String updateUser;

	@UpdateTimestamp
	private Date updateDate;
	
	@Transient
	private String password;
}
