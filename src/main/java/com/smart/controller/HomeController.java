package com.smart.controller;

import com.smart.entities.Role;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;


@Controller
public class HomeController {

	// Create object of user repository using autowired
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@RequestMapping("/")
	public String home(Model model) {
		model.addAttribute("title", "Home-SmartContactManager");
		return "home";
	}

	@RequestMapping("/about")
	public String about(Model model) {
		model.addAttribute("title", "About-SmartContactManager");
		return "about";
	}

	@RequestMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title", "Register-SmartContactManager");
		model.addAttribute("user", new User());
		return "signup";
	}
	@GetMapping("/signin")
	public String customLogin(Model model){
		model.addAttribute("title", "Login-SmartContactManager");
		return "login";
	}


	@RequestMapping(value = "/do_action", method = RequestMethod.POST)
	public String register(@Valid @ModelAttribute("user") User user, BindingResult result1,
						   @RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model,
						   HttpSession session) {
		try {
/*			if (session.getAttribute("message") != null) {
				session.removeAttribute("message");
			}*/
			if (!agreement) {
				throw new Exception("You have not agreed the terms and conditions");
			}
			if (result1.hasErrors()) {
				model.addAttribute("user", user);
				return "signup";
			}
			user.setRole("ROLE_USER"); // set field manual
			user.setEnabled(true);

			// encode password
			user.setPassword(this.passwordEncoder.encode(user.getPassword()));

			// save user using userRepository
			User result = this.userRepository.save(user);

			model.addAttribute("user", new User());
			session.setAttribute("message", new Message("Successfully registered !! ", "alert-success"));
			return "signup";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Something went wrong !! " + e.getMessage(), "alert-danger"));
			return "signup";
		}
	}
}
