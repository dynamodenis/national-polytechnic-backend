package com.mabawa.nnpdairy.services;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

@Service
public class PasswordEncryption {
    private static final String ALGO = "AES";
    private static final byte[] keyValue = new byte[]{77, 65, 98, 65, 87, 97, 49, 110, 110, 48, 86, 97, 116, 105, 111, 110};

    public String encrypt(String pws) {
        String encodedPsw = "";

        try {
            Key key = generateKey();
            Cipher c = Cipher.getInstance("AES");
            c.init(1, key);
            byte[] encVal = c.doFinal(pws.getBytes());
            encodedPsw = Base64.getEncoder().encodeToString(encVal);
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        return encodedPsw;
    }

    public String decrypt(String encryptedPsw) {
        String decodedPsw = "";

        try {
            Key key = generateKey();
            Cipher c = Cipher.getInstance("AES");
            c.init(2, key);
            byte[] decodedValue = Base64.getDecoder().decode(encryptedPsw);
            byte[] decValue = c.doFinal(decodedValue);
            decodedPsw = new String(decValue);
        } catch (Exception var7) {
            var7.printStackTrace();
        }

        return decodedPsw;
    }

    private static Key generateKey() {
        SecretKeySpec key = new SecretKeySpec(keyValue, "AES");
        return key;
    }
}
