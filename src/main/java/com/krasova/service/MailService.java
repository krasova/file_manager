package com.krasova.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by osamo on 3/24/2017.
 */
@Slf4j
@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    public void notify(List<String> fileNames) {
        try {
            mailSender.send(mimeMessage -> {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage,
                        true);
                message.setTo("olga.v.krasova@gmail.com");
                message.setFrom("olga.v.krasova@gmail.com");
                message.setSubject("New files");
                message.setText(fileNames.toString());
            });
        } catch (Exception e) {
            log.error("Notify call failed",
                    e);
        }
    }
}
