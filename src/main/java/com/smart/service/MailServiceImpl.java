package com.smart.service;

import com.smart.model.Mail;
import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Service
public class MailServiceImpl implements MailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendEmail(Mail mail) {
        String to = mail.getMailTo();
        String cc = mail.getMailCc();
        String bcc = mail.getMailBcc();
        String subject = mail.getMailSubject();
        String sender = mail.getMailFrom();
        String bodyHtml = mail.getMailContent();

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            // Add subject, from and to lines.
            message.setSubject(subject, "UTF-8");
            message.setFrom(new InternetAddress(sender));
            if (StringUtils.hasLength(to)) {
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            }
            if (StringUtils.hasLength(cc)) {
                message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
            }
            if (StringUtils.hasLength(bcc)) {
                message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(bcc));
            }

            // Create a multipart/alternative child container.
            MimeMultipart msg_body = new MimeMultipart("alternative");

            // Create a wrapper for the HTML and text parts.
            MimeBodyPart wrap = new MimeBodyPart();

            // Define the HTML part.

            // Encode the HTML content and set the character encoding.
            if (bodyHtml != null) {
                MimeBodyPart htmlPart = new MimeBodyPart();
                htmlPart.setContent(bodyHtml, "text/html; charset=UTF-8");
                htmlPart.setHeader("Content-Transfer-Encoding", "base64");
                msg_body.addBodyPart(htmlPart);
            }
            // Add the text and HTML parts to the child container.

            // Add the child container to the wrapper object.
            wrap.setContent(msg_body);

            // Create a multipart/mixed parent container.
            MimeMultipart msg = new MimeMultipart("mixed");

            // Add the parent container to the message.
            message.setContent(msg);

            //send message
            javaMailSender.send(message);

        } catch (Exception ex) {
            throw new RuntimeException(String.format("unable to send mail to %s cc %s bcc %s subject %s error %s", to, cc, bcc, subject, ex.getMessage()), ex);
        }

    }
}
