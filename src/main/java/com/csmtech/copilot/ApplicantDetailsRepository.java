/**
 * 
 */
package com.csmtech.copilot;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * 
 */

@Repository
public interface ApplicantDetailsRepository extends JpaRepository<ApplicantDetails, Long> {

	@Query("FROM ApplicantDetails ORDER BY id DESC")
    List<ApplicantDetails> getalldata();

	@Query("select count(*) from ApplicantDetails where applicationNo =:applicationNo")
	Integer getCountByApplicationNo(String applicationNo);
	
	@Query("from ApplicantDetails where applicationNo =:applicationNo")
	ApplicantDetails getByApplicationNo(String applicationNo);
	
	@Query("select count(*) from ApplicantDetails where mobile =:applicationNo")
	Integer getCountBymobileNo(String applicationNo);
	
	@Query("from ApplicantDetails where mobile =:applicationNo")
	ApplicantDetails getBymobileNo(String applicationNo);
}

