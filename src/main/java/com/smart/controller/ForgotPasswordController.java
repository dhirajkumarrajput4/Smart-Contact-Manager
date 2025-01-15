package com.smart.controller;

import java.util.Optional;
import java.util.Random;

import com.smart.model.Mail;
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
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Controller
public class ForgotPasswordController {

    private final UserRepository userRepository;

    private final PasswordGenerator passwordGenerator;

    private final PasswordEncoder passwordEncoder;

    private final MailService mailService;

    private final TemplateEngine templateEngine;

    @Autowired
    public ForgotPasswordController(UserRepository userRepository, PasswordGenerator passwordGenerator,
                                    PasswordEncoder passwordEncoder, MailService mailService, TemplateEngine templateEngine) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
        this.passwordGenerator = passwordGenerator;
        this.templateEngine = templateEngine;

    }

    private static final Logger LOGGER = LoggerFactory.getLogger(ForgotPasswordController.class);

    @GetMapping("/forgot_password")
    public String forgotPassword(Model model) {
        model.addAttribute("title", "Forgot-Password");
        return "forgot_password";
    }

    @PostMapping("/forgot_password")
    public String forgotPasswordProcess(@RequestParam("username") String email, HttpSession session) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found."));
        String generatedRandomPassword = passwordGenerator.generateRandomPassword();
        user.setPassword(passwordEncoder.encode(generatedRandomPassword));
        userRepository.save(user);
        LOGGER.info("Password has reset successfully");

        session.setAttribute("message",
                new Message("Your password updated successfully: " + generatedRandomPassword, "alert-success"));
        return "forgot_password";
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordForm(Model model) {

        model.addAttribute("title", "Forgot-Password");

        return "forgot_password_form";
    }

    @PostMapping("/sendOTP")
    public String sendOtp(@RequestParam("username") String email, Model model, HttpSession session) {
        model.addAttribute("title", "Verify-OTP");
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            session.setAttribute("message", new Message("User doesn't exist with this email", "alert-danger"));
            return "forgot_password_form";
        } else {
            //generate otp
            Integer otp = generateOTP();
            LOGGER.info("OTP is :" + otp);
            session.setAttribute("otp", otp);
            session.setAttribute("emailId", email);

            //Generate mail with html body
            Mail mail = generateMailWithBody(otp, email);

            //send mail
            mailService.sendEmail(mail);
            session.setAttribute("message", new Message("OTP send successfully to your email", "alert-success"));
            return "verify_OTP";
        }
    }

    @PostMapping("verify_otp")
    public String verifyOtp(@RequestParam("otp") int otp, Model modle, HttpSession session) {
        int myOtp = (int) session.getAttribute("otp");
        if (otp == myOtp) {
            modle.addAttribute("title", "Update-Password");
            return "change_password";
        } else {
            session.setAttribute("message", new Message("You entered wrong otp !!", "alert-danger"));
            return "verify_OTP";
        }
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam("newPassword") String newPassword, @RequestParam("confirmPassword") String confirmPassword,
                                 HttpSession session) {
        String emailId = (String) session.getAttribute("emailId");
        User user = userRepository.findByEmail(emailId).orElseThrow(()->new RuntimeException("User not found"));
        if (newPassword.equals(confirmPassword)) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            session.setAttribute("message", new Message("Your password change successfully", "alert-success"));
            return "redirect:/signin";
        } else {
            session.setAttribute("message", new Message("Your password is not match with confirm password", "alert-danger"));
            return "change_password";
        }
    }

    private Mail generateMailWithBody(Integer otp, String email) {
        //for send mail
        Mail mail = new Mail();
        mail.setMailTo(email);
        mail.setMailSubject("Smart-Contact-Manager OTP");
        //mail content
        Context context = new Context();
        context.setVariable("title", "Welcome to Smart-Contact-Manager");
        context.setVariable("message", "Your OTP is : " + otp);

        String htmlContent = templateEngine.process("email/email-template", context);
        mail.setMailContent(htmlContent);
        return mail;
    }

    private static Integer generateOTP() {
        return new Random().nextInt(900000) + 100000;
    }

}
