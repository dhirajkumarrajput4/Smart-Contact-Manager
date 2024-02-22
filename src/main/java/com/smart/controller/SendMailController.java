package com.smart.controller;

import com.smart.model.Mail;
import com.smart.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@RestController
@RequestMapping("/mailService")
public class SendMailController {


    @Autowired
    private MailService mailService;

    @Autowired
    private TemplateEngine templateEngine;

    @PostMapping("/send")
    public ResponseEntity<String> sendMail(){
        Mail mail = new Mail();

        mail.setMailFrom("mydesktop2662@gmail.com");
        mail.setMailTo("dhirajkumarrajput4@gmail.com");
        mail.setMailSubject("Spring Boot - Email demo");

        //mail content
        Context context = new Context();
        context.setVariable("title", "Dynamic Title");
        context.setVariable("message", "Hello, this is a dynamic message!");

        String htmlContent = templateEngine.process("/email/email-template", context);

        mail.setMailContent(htmlContent);

        mailService.sendEmail(mail);

        mailService.sendEmail(mail);
        return ResponseEntity.ok("Mail send Successfully");
    }

    @PostMapping("/test")
    public ResponseEntity<String> test(){

        return ResponseEntity.ok("Working fine");
    }

}
