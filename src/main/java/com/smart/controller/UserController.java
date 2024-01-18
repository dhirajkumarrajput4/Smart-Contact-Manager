package com.smart.controller;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserRepository userRepository;
	@GetMapping("/index")
	public String dashboard(Model module, Principal principal) {
		String username = principal.getName();
		Optional<User> user = userRepository.findByEmail(username);
		module.addAttribute("user",user.get());
		return "normal/user_dashboard";
	}
}
