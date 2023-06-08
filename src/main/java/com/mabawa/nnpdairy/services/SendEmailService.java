package com.mabawa.nnpdairy.services;

import com.mabawa.nnpdairy.models.ContactUs;
import com.mabawa.nnpdairy.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Random;

@Service
public class SendEmailService {
    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private PasswordEncryption passwordEncryption;

    @Autowired
    private UserService userService;

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

    public void sendResetOtp(User user) {
        try {
            Random ran = new Random();
            Integer otp = ran.nextInt(100000);
            String encrypt = passwordEncryption.encrypt(String.valueOf(otp));
            user.setPassword(encrypt);
            user.setFirstTimeLogin(1);

            User updUser = userService.update(user);

            String msg = "Dear " + user.getName() + ", please use the One Time Password below in order to proceed to set up new password." +
                    "\n"+
                    "\n"+
                    "\n"+
                    "" + otp +
                    "\n"+
                    "\n"+
                    "\n"+
                    "Regards, " +
                    "\n" +
                    "Administrator, " +
                    "\n" +
                    "NNP Dairy Platform"+
                    "\n" +
                    "";
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(NOREPLY_ADDRESS);
            message.setTo(user.getMail());
            message.setSubject("NNP Dairy Platform OTP");
            message.setText(msg);

            emailSender.send(message);
        } catch (MailException exception) {
            exception.printStackTrace();
        }
    }

    public void sendRegistrationOtp(String msg, String mail) {
        try {

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(NOREPLY_ADDRESS);
            message.setTo(mail);
            message.setSubject("NNP Dairy Platform OTP");
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
