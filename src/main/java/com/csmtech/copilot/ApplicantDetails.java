package com.csmtech.copilot;

import java.util.Date;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "TBL_AI_POC")
@Data
public class ApplicantDetails {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "APPLICANT_NAME")
    private String applicantName;
    
    @Column(name = "APPLICATION_NO")
    private String applicationNo;

    @Column(name = "FATHER_NAME")
    private String fatherName;

    @Column(name = "MOTHER_NAME")
    private String motherName;

    @Column(name = "DISTRICT")
    private String district;

    @Column(name = "BLOCK")
    private String block;

    @Column(name = "POST")
    private String post;

    @Column(name = "POLICE_STATION")
    private String policeStation;

    @Column(name = "VILLAGE")
    private String village;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "DOB")
    private Date dob;

    @Column(name = "GENDER")
    private String gender;

    @Column(name = "CASTE")
    private String caste;

    @Column(name = "EDUCATION")
    private String education;

    @Column(name = "MOBILE")
    private String mobile;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "CRIMINAL")
    private String criminal;

    @Column(name = "CASE_NUMBER")
    private String caseNumber;

    @Column(name = "FREEDOM")
    private String freedom;

    @Column(name = "ID_MARK1")
    private String idMark1;

    @Column(name = "ID_MARK2")
    private String idMark2;

    @Column(name = "CREATED_ON")
    private Date createdOn = new Date();

    @Column(name = "UPDATED_ON")
    private Date updatedOn = new Date();
    
    @Column(name = "final_submit_status")
    private Integer finalsubmitstatus;
    
    @Column(name = "userid")
    private Long userid;

}
