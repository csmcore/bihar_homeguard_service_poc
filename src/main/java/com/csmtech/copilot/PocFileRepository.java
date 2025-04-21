package com.csmtech.copilot;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PocFileRepository extends JpaRepository<PocFile, Long> {

	@Query("from PocFile where applicant.id =:id order by id desc")
	List<PocFile> getfiledata(Long id);

}
