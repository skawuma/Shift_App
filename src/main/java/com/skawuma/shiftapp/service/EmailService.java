package com.skawuma.shiftapp.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * @author samuelkawuma
 * @package com.skawuma.shiftapp.service
 * @project Shift-App
 * @date 10/12/25
 */

@Service
public class EmailService {

    private final JavaMailSender sender;

    public EmailService(JavaMailSender sender) {
        this.sender = sender;
    }

    // ✅ Simple text version
    public void sendSimple(String to, String subject, String body) {
        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, false); // plain text
            helper.setFrom("success@samuelkawuma.com");
            sender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send simple email: " + e.getMessage(), e);
        }
    }

    // ✅ HTML version with styled formatting
    public void sendHtml(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // true = enable HTML
            helper.setFrom("success@samuelkawuma.com");
            sender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send HTML email: " + e.getMessage(), e);
        }
    }
    public void sendToEmployee(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("success@samuelkawuma.com"); // ✅ Explicitly set From address
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // true → send as HTML

            sender.send(message);
            System.out.println("✅ Email sent successfully to " + to);
        } catch (MessagingException e) {
            throw new RuntimeException("❌ Failed to send email: " + e.getMessage(), e);
        }
    }
}
