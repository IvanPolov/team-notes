package com.gbdevteam.teamnotes.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;


import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class EmailSender {

    private final JavaMailSender emailSender;


    public void sendEmail(String emailTo, UUID link) throws MessagingException {

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
        helper.setTo(emailTo);
        helper.setSubject("Registration on Team Notes service");

        StringBuilder letter = new StringBuilder();
        letter.append("<h3>Registration for the Team Notes service is almost complete/</h3>");
        letter.append("<br>Please confirm your registration with Team Notes Service!");
        letter.append("<br>Click on this <a href=' ");
        letter.append("http://localhost:8180/team-notes/api/v1/signup/confirm/?email=" + emailTo + "&uuId=" + link);
        letter.append("'><i><b>link</b></i></a> to complete the registration.");
        letter.append("<br><br><i>If you do not do this within 24 hours, your account will be blocked.");
        letter.append("<br><br><br><b>Yours sincerely,<br>" +
                "\"Team Notes\".</b><hr>");

        String htmlMsg = letter.toString();
        log.info(htmlMsg);
        message.setContent(htmlMsg, "text/html");
        this.emailSender.send(message);
    }
}
