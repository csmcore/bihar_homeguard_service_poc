package com.csmtech.copilot;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "TBL_AI_POCFILE")
@Data
public class PocFile {

		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		@Column(name = "ID")
	    private Long id;

		@OneToOne
	    @JoinColumn(name = "APPLICANT_ID")
	    private ApplicantDetails applicant;

	    @Column(name = "PHOTO")
	    private String photo;

	    @Column(name = "SIGNATURE_ENG")
	    private String signatureEng;

	    @Column(name = "SIGNATURE_HINDI")
	    private String signatureHindi;

	    @Column(name = "RESIDENT")
	    private String resident;

	    @Column(name = "PHYSICAL")
	    private String physical;

	    @Column(name = "MATRIC")
	    private String matric;

	    @Column(name = "GRADUATION")
	    private String graduation;

	    @Column(name = "GRADUATION_MARK")
	    private String graduationMark;

	    @Column(name = "CREATED_ON")
	    private Date createdOn;
}
