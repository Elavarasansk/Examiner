package org.web.vexamine.dao.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
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
 * Instantiates a new role.
 *
 * @param id the id
 * @param type the type
 * @param description the description
 * @param createUser the create user
 * @param updateUser the update user
 * @param createDate the create date
 * @param updateDate the update date
 */
@Data
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
@Table(name = "vexamine_role")
public class Role implements Serializable {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -1L;
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String type;

    private String description;
    
    private String createUser;
    
    private String updateUser;

    @CreationTimestamp
    private Timestamp createDate;
    
    @UpdateTimestamp
    private Date updateDate;

}
