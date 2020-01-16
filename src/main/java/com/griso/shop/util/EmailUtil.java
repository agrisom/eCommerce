package com.griso.shop.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;

@Component
public class EmailUtil {

    protected static final Log LOG = LogFactory.getLog(EmailUtil.class.getName());

    @Autowired
    private JavaMailSender javaMailSender;

    private EmailUtil() {}

    public boolean isValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    public void send(String destination, String subject, String content) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(destination);
            helper.setSubject("eCommerce - " + subject);
            helper.setBcc("shop.agrisom@gmail.com");
            helper.setText(content, true);

            LOG.info("Sending email:\nTo: " + destination + "\nSubject: " + subject + "\nContent: " + content);
            javaMailSender.send(message);
        } catch (Exception e) {
            LOG.error("Cannot send email");
            LOG.info(e.getMessage());
            LOG.info(e.getStackTrace());
        }
    }

}
