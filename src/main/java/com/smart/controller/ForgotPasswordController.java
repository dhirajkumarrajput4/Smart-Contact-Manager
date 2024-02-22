package com.smart.controller;

import java.util.Optional;
import java.util.Random;

import com.smart.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;
import com.smart.helper.PasswordGenerator;

import jakarta.servlet.http.HttpSession;

@Controller
public class ForgotPasswordController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordGenerator passwordGenerator;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private MailService mailService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ForgotPasswordController.class);

	@GetMapping("/forgot_password")
	public String forgotPassword(Model model) {
		model.addAttribute("title", "Forgot-Password");
		return "forgot_password";
	}

	@PostMapping("/forgot_password")
	public String forgotPassowrdProccess(@RequestParam("username") String email, HttpSession session) {
		Optional<User> userOptional = userRepository.findByEmail(email);
		User user = userOptional.get();
		String generatedRandomPassword = passwordGenerator.generateRandomPassword();
		user.setPassword(passwordEncoder.encode(generatedRandomPassword));
		userRepository.save(user);
		LOGGER.info("Password has resetted : " + generatedRandomPassword);

		session.setAttribute("message",
				new Message("Your password resetted successfully: " + generatedRandomPassword, "alert-success"));
		return "forgot_password";
	}
	
	@GetMapping("/forgot-password")
	public String forgotPasswordForm(Model model) {
		
		model.addAttribute("title", "Forgot-Password");
		
		return "forgot_password_form";
	}
	
	@PostMapping("/sendOTP")
	public String sendOtp(@RequestParam("username") String email,Model model,HttpSession session) {
		model.addAttribute("title", "Varify-OTP");
		Random random = new Random(100000);
		int otp = random.nextInt(999999);
		
		LOGGER.info("OTP is :"+otp);
		
		session.setAttribute("message",new Message("OTP send successfully to your email","alert-success"));
		return "varify_OTP";
	}
	
	@PostMapping("varify_otp")
	public String varifyOtp(@RequestParam("otp")String otp,Model modle) {
		
		return "change_password";
	}



}
