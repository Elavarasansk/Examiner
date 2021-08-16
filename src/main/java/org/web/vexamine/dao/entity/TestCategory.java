package org.web.vexamine.dao.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Instantiates a new test category.
 *
 * @param id the id
 * @param category the category
 * @param subCategory the sub category
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
@Table(name = "vexamine_test_category")
public class TestCategory implements Serializable {

	private static final long serialVersionUID = -1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String category;

	private String subCategory;
	
	private String createUser;

	@CreationTimestamp
	private Date createDate;
	
	private String updateUser;
	
	@UpdateTimestamp
	private Date updateDate; 


}
