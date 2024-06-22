package io.links.server.service;

import io.links.server.utils.LoggingUtils;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

import static io.links.server.utils.LoggingUtils.getLoggingUtils;

@Service
@RequiredArgsConstructor
public class MailService {

    @Value("${spring.mail.username}")
    private String mailUsername;
    private final TemplateEngine thymeleafTemplateEngine;
    private final JavaMailSender javaMailSender;
    private final LoggingUtils loggingUtils = getLoggingUtils(this.getClass());

        public void sendMail(String recipientMailAddress, String subject, String text) {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            try {
                helper.setFrom(mailUsername);
                helper.setTo(recipientMailAddress);
                helper.setSubject(subject);
                helper.setText(text, true);
            } catch (Exception e) {
                loggingUtils.logStackTraceAndMessage(e);
                return;
            }

            javaMailSender.send(mimeMessage);
    }

    public void sendTemplateMail(
            String templateName,
            String recipientMailAddress,
            String subject,
            Map<String, Object> variables
    ) {
            String html = getHtmlFromTemplate(templateName, variables);
            sendMail(
                    recipientMailAddress,
                    subject,
                    html
            );
    }

    public String getHtmlFromTemplate(String templateName, Map<String, Object> variables) {
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(variables);
        return thymeleafTemplateEngine.process(templateName + ".html", thymeleafContext);
    }
}
