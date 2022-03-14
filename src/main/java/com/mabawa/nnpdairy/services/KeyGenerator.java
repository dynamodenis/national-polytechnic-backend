package com.mabawa.nnpdairy.services;

import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;

@Service
public class KeyGenerator {
    public String getKeyByUser(String mail) {
        String key = "";

        try {
            byte[] mailDigest = mail.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] thaDigest = md.digest(mailDigest);
            key = DatatypeConverter.printHexBinary(thaDigest);
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        return key;
    }
}
