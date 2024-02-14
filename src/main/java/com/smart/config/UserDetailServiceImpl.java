package com.smart.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.smart.dao.UserRepository;
import com.smart.entities.User;

public class UserDetailServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// fetching user from database
		User userByUsername = this.userRepository.getUserByUsername(username);
		if (userByUsername == null) {
			throw new UsernameNotFoundException("Could not found user !!");
		}
		CustomUserDetails customUserDetails = new CustomUserDetails(userByUsername);
		return customUserDetails;
	}

}
