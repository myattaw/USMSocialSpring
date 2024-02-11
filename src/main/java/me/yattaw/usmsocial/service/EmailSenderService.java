package me.yattaw.usmsocial.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import me.yattaw.usmsocial.entities.user.User;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailSenderService {

    private final JavaMailSender mailSender;
    private final Configuration freemarkerConfig;

    @Async
    public void sendEmail(User user, String subject, String body, String verifyLink, String buttonText) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            // Set the 'from' address using the environment variable
            helper.setFrom(System.getenv("LOGIN_SMTP_USERNAME"));

            // Set the recipient and subject
            helper.setTo(user.getEmail());
            helper.setSubject(subject);

            // Process FreeMarker template
            Map<String, Object> model = new HashMap<>();
            model.put("firstName", user.getFirstName());
            model.put("lastName", user.getLastName());
            model.put("subject", subject);
            model.put("body", body);
            model.put("verifyLink", verifyLink);
            model.put("buttonText", buttonText);

            String htmlContent = getFreeMarkerTemplateContent(model);
            helper.setText(htmlContent, true);

            // Send the email
            mailSender.send(mimeMessage);
        } catch (MessagingException | TemplateException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getFreeMarkerTemplateContent(Map<String, Object> model)
            throws IOException, TemplateException {
        Template template = freemarkerConfig.getTemplate("email-template.ftl");
        return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
    }

}
