package io.links.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    @Value("${spring.mail.username}")
    private String mailUsername;

    private final JavaMailSender javaMailSender;

        public void sendMail(String recipientMailAddress, String subject, String text) {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(mailUsername);
            simpleMailMessage.setTo(recipientMailAddress);
            simpleMailMessage.setSubject(subject);
            simpleMailMessage.setText(text);

            javaMailSender.send(simpleMailMessage);
    }
}
