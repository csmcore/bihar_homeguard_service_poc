package com.csmtech.copilot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailsMainRepository extends JpaRepository<UserDetailsMain, Long> {

	UserDetailsMain findByUsername(String username);

	UserDetailsMain findByUsernameIgnoreCase(String username);
	
	@Query("SELECT u FROM UserDetailsMain u WHERE LOWER(u.username) =:username")
	UserDetailsMain findByUsernameLOWER(String username);

}
