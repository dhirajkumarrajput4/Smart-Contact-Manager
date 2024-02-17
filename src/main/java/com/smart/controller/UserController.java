package com.smart.controller;

import com.smart.dao.UserRepository;
import com.smart.entities.Contacts;
import com.smart.entities.User;
import com.smart.helper.Message;
import com.smart.service.ContactService;
import com.smart.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.*;
import java.security.Principal;
import java.util.Optional;
import java.util.function.Predicate;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private ContactService contactService;

	@ModelAttribute
	public void addCommanData(Model model, Principal principal) { // This method will add command-data to all handler
																	// amd return data to page
		String username = principal.getName();
		Optional<User> user = userService.findByEmail(username);
		model.addAttribute("user", user.get());
	}

	@GetMapping("/index")
	public String dashboard(Model m, Principal principal) {
		return "normal/user_dashboard";
	}

	@GetMapping("/add-contact")
	public String addContactForm(Model model) {
		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new Contacts());
		return "normal/add_contact_form";
	}

	@PostMapping("/process-contact")
	public String processContactForm(@ModelAttribute Contacts contact, @RequestParam("profileImg") MultipartFile file,
			Principal principal, HttpSession session) {
		try {
			User user = userService.getUserByUsername(principal.getName());
			if (!file.isEmpty()) {
				contact.setProfilePic(file.getOriginalFilename());
				File savedFile = new ClassPathResource("static/profileImages").getFile();
				Path path = Paths.get(savedFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			} else {
				contact.setProfilePic("contact.png");
			}
			contact.setUser(user);
			user.getContact().add(contact);
			userService.save(user);
			session.setAttribute("message", new Message("Your contact is added !! Add more...", "alert-success"));
		} catch (Exception exception) {
			session.setAttribute("message", new Message("Something went wrong !! try again...", "alert-danger"));
			exception.printStackTrace();
		}
		return "normal/add_contact_form";
	}

	@GetMapping("/show-contacts/{page}")
	public String getAllContacts(Model model, Principal principal, @PathVariable("page") Integer page) {
		model.addAttribute("title", "Contact List");
		User userByUsername = userService.getUserByUsername(principal.getName());
		Pageable pageable = PageRequest.of(page, 5);
		Page<Contacts> contacts = this.contactService.findContactsByUser(userByUsername.getId(), pageable);

		model.addAttribute("contacts", contacts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", contacts.getTotalPages());
		return "normal/show-contacts";
	}

	@GetMapping("/{id}/contact")
	public String getContactDetails(Model model, @PathVariable("id") Integer id, Principal principal) {

		Optional<Contacts> contactsOptional = this.contactService.findById(id);
		Contacts contact = contactsOptional.get();
		User userByUsername = userService.getUserByUsername(principal.getName());
		if (userByUsername.getId() == contact.getUser().getId()) {
			model.addAttribute("contact", contact);
			model.addAttribute("title", contact.getName());
		}
		return "normal/contact_details";
	}

	@GetMapping("/contact/delete/{cId}")
	public String deleteContact(Principal principal, @PathVariable("cId") Integer id, HttpSession session) {
		Optional<Contacts> contactsOptional = this.userService.getUserByUsername(principal.getName()).getContact()
				.stream().filter(contact -> contact.getcId() == id).findFirst();
		try {
			User user = userService.getUserByUsername(principal.getName());
			user.getContact().remove(contactsOptional.get());
			this.userService.save(user);
//            this.contactService.delete(contactsOptional.get());
			session.setAttribute("message", new Message("Contact deleted successfully", "alert-success"));
		} catch (Exception exception) {
			session.setAttribute("message", new Message(exception.getMessage(), "alert-danger"));
		}
		return "redirect:/user/show-contacts/0";
	}

	@PostMapping("update-contact/{cId}")
	public String updateContactForm(Model model, @PathVariable("cId") Integer cId) {
		model.addAttribute("title", "Update Contact");
		Optional<Contacts> contact = contactService.findById(cId);
		model.addAttribute("contact", contact.get());
		return "normal/update_contact_form";
	}

	@PostMapping("/process-update-contact")
	public String processUpdateContact(Model model, HttpSession httpSession, @ModelAttribute Contacts contact,
			@RequestParam("profileImg") MultipartFile file, Principal principal, HttpSession session) {
		try {
			User user = userService.getUserByUsername(principal.getName());
			Optional<Contacts> contactsOptional = contactService.findById(contact.getcId());
			if (!file.isEmpty()) {
				if (contactsOptional.get().getProfilePic() != null) {
					// delete old file
					File deleteFile = new ClassPathResource("static/profileImages").getFile();
					File fileForDelete = new File(deleteFile, contactsOptional.get().getProfilePic());
					fileForDelete.delete();
				}
				// add new file
				contact.setProfilePic(file.getOriginalFilename());
				File savedFile = new ClassPathResource("static/profileImages").getFile();
				Path path = Paths.get(savedFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			} else {
				contact.setProfilePic(contactsOptional.get().getProfilePic());
			}
			contact.setUser(user);
			contactService.save(contact);
			session.setAttribute("message", new Message("Contact successfully updated", "alert-success"));
		} catch (Exception exception) {
			session.setAttribute("message", new Message(exception.getMessage(), "alert-danger"));
			exception.printStackTrace();
		}
		return "redirect:/user/" + contact.getcId() + "/contact";
	}

	@GetMapping("/profile")
	public String profile(Model model) {
		model.addAttribute("title", "Profile");

		return "normal/viewProfile";
	}

}
