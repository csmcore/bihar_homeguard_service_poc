/**
 * 
 */
package com.csmtech.copilot;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 
 */
@Entity
@Data
@Table(name = "USER_DETAILS_MAIN")
public class UserDetailsMain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USERID")
    private Long userId;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "AUTHVALUE")
    private String authValue;

    @Column(name = "GROUPID")
    private Integer groupId;

    @Column(name = "CREATED_TIME")
    private Date createdTime;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PASSWORD_UPDATE_TIME")
    private Date passwordUpdateTime;

    @Column(name = "PASSWORD_UPDATE_FLAG")
    private Integer passwordUpdateFlag;

    @Column(name = "STATUS_FLAG")
    private Integer statusFlag;
    
}
