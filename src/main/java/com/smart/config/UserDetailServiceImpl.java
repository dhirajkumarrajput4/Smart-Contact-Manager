package com.smart.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.smart.dao.UserRepository;
import com.smart.entities.User;

import javax.swing.text.html.Option;
import java.util.Optional;

public class UserDetailServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// fetching user from database
		Optional<User> userByUsername = Optional.ofNullable(this.userRepository.findByEmail(username).orElseThrow(() ->new UsernameNotFoundException("Could not found user !!")));
        return new CustomUserDetails(userByUsername.get());
	}

}
