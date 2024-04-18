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
/**
 * Service class responsible for sending emails to users using the configured JavaMailSender.
 *
 * <p>
 * This class utilizes FreeMarker templates to generate HTML email content.
 * </p>
 *
 * <p>
 * The {@code @Service} annotation marks this class as a Spring-managed service component.
 * </p>
 *
 * <p>
 * The {@code @RequiredArgsConstructor} annotation automatically generates a constructor that initializes
 * all final fields with arguments.
 * </p>
 *
 * @version 17 April 2024
 */
@Service
@RequiredArgsConstructor
public class EmailSenderService {

    private final JavaMailSender mailSender;
    private final Configuration freemarkerConfig;

    /**
     * Asynchronously sends an email to the specified user.
     *
     * @param user       The user to whom the email will be sent.
     * @param subject    The subject of the email.
     * @param body       The body of the email.
     * @param verifyLink The verification link included in the email.
     * @param buttonText The text displayed on the button in the email.
     */
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
            model.put("firstName", user.getFirstName() == null ? "" : user.getFirstName());
            model.put("lastName", user.getLastName() == null ? "" : user.getLastName());
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