package com.smart.controller;

import com.smart.dao.UserRepository;
import com.smart.entities.Contacts;
import com.smart.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserRepository userRepository;

	@ModelAttribute
	public void addCommanData(Model model,Principal principal){   //This method will add command-data to all handler amd return data to page
		String username = principal.getName();
		Optional<User> user = userRepository.findByEmail(username);
		model.addAttribute("user",user.get());
	}
	@GetMapping("/index")
	public String dashboard(Model m, Principal principal) {
		return "normal/user_dashboard";
	}
	@GetMapping("/add-contact")
	public String addContactForm(Model model){
		model.addAttribute("title","Add Contact");
		model.addAttribute("contact",new Contacts());
	return "normal/add_contact_form";
	}

	@PostMapping("/process-contact")
	public String processContactForm(@ModelAttribute Contacts contact,Principal principal){
		User user = userRepository.getUserByUsername(principal.getName());
		contact.setUser(user);
		user.getContact().add(contact);
		userRepository.save(user);
		return "normal/add_contact_form";
	}

}
