package com.smart.controller;

import com.smart.dao.UserRepository;
import com.smart.entities.Contacts;
import com.smart.entities.User;
import com.smart.helper.Message;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.*;
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
	public String processContactForm(@ModelAttribute Contacts contact, @RequestParam("profileImg") MultipartFile file,
									 Principal principal, HttpSession session){
		try {
			User user = userRepository.getUserByUsername(principal.getName());
			if(!file.isEmpty()){
				contact.setProfilePic(file.getOriginalFilename());
				File savedFile = new ClassPathResource("static/profileImages").getFile();
				Path path = Paths.get(savedFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
			}
			contact.setUser(user);
			user.getContact().add(contact);
			userRepository.save(user);
			session.setAttribute("message",new Message("Your contact is added !! Add more...","alert-success"));
		}
		catch (Exception exception){
			session.setAttribute("message",new Message("Something went wrong !! try again...","alert-danger"));
			exception.printStackTrace();
		}
		return "normal/add_contact_form";
	}

}
