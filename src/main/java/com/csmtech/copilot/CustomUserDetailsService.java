package com.csmtech.copilot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	@Autowired
	private UserDetailsMainRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println(repository.findAll());
		UserDetailsMain user = repository.findByUsernameIgnoreCase(username.toLowerCase());
		System.out.println(user + "  " + username);
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getAuthValue(),
				new ArrayList<>());
	}
}
