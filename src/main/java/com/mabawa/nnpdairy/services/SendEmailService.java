package com.mabawa.nnpdairy.services;

import com.mabawa.nnpdairy.models.ContactUs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;

@Service
public class SendEmailService {
    @Autowired
    private JavaMailSender emailSender;

    private static final String NOREPLY_ADDRESS = "dairy@thenyeripoly.ac.ke";

    public void sendSimpleMessage(ContactUs contactUs) {
        try {
            String msg = contactUs.getMessage() +
                          "\n"+
                          "\n"+
                          "\n"+
                          "Regards, " +
                          "\n" +
                          "" + contactUs.getName() +
                          "\n" +
                          "" + contactUs.getEmail() +
                          "\n" +
                          "" + contactUs.getPhone();
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(NOREPLY_ADDRESS);
            message.setTo(NOREPLY_ADDRESS);
            message.setSubject(contactUs.getSubject());
            message.setText(msg);

            emailSender.send(message);
        } catch (MailException exception) {
            exception.printStackTrace();
        }
    }

//    public void sendEmailUsingFreeMarkerTemplate(ContactUs contactUs) throws IOException, TemplateException, MessagingException
//    {
//
//    }
}
