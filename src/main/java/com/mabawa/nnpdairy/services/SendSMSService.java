package com.mabawa.nnpdairy.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mabawa.nnpdairy.constants.Constants;
import com.mabawa.nnpdairy.models.MessageBody;
import com.mabawa.nnpdairy.models.MessageBodySema;
import com.mabawa.nnpdairy.models.MessagesSent;
import com.mabawa.nnpdairy.models.User;
import com.mabawa.nnpdairy.repository.MessageSentRepository;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class SendSMSService {
    @Autowired
    private MessageSentRepository messageSentRepository;

    @Autowired
    private PasswordEncryption passwordEncryption;

    @Autowired
    private UserService userService;

    public void sendSMS(MessageBody messageBody, OkHttpClient client)
    {
        messageBody.setProductID(Constants.PRODUCT_ID);
        try{
            Gson gson =new GsonBuilder().create();
            String messageBodyJson = gson.toJson(messageBody);

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, messageBodyJson);

            Request request = new Request.Builder()
                    .url(Constants.SMS_SANDBOX_URL)
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("X-Token", Constants.X_TOKEN)
                    .build();

            Response response = client.newCall(request).execute();
            System.out.println(response);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void PrepSms(MessageBody messageBody, User user, OkHttpClient client)
    {
        messageBody.setSms("Dear " + user.getName() + ", your verification code is " + String.valueOf(user.getOtpNumber()));
        messageBody.setMsisdn(user.getPhone());

        LocalDateTime lastLocal = LocalDateTime.now();
        final MessagesSent messagesSent = new MessagesSent();
        messagesSent.setTextMessage(messageBody.getSms());
        messagesSent.setSent(0);
        messagesSent.setDate(Timestamp.valueOf(lastLocal));
        messagesSent.setMsisdn(messagesSent.getMsisdn());
        messagesSent.setStatus(Constants.WAITING_FEEDBACK);

        MessagesSent sent = messageSentRepository.saveAndFlush(messagesSent);
        String callbackUrl = "http://34.67.196.163:8181/api/v1/sms_callback/callback/" + String.valueOf(sent.getId()) + "/" + String.valueOf(sent.getId());
        messageBody.setCallbackURL(callbackUrl);
        sendSMS(messageBody, client);
    }

    public void PrepSmsSema(MessageBodySema messageBodySema, User user, OkHttpClient client)
    {
        messageBodySema.setText("Dear " + user.getName() + ", your verification to access NNP Diary platform code is " + String.valueOf(user.getOtpNumber()));
        messageBodySema.setRecipients(user.getPhone());

        LocalDateTime lastLocal = LocalDateTime.now();
        final MessagesSent messagesSent = new MessagesSent();
        messagesSent.setTextMessage(messageBodySema.getText());
        messagesSent.setSent(0);
        messagesSent.setDate(Timestamp.valueOf(lastLocal));
        messagesSent.setMsisdn(messagesSent.getMsisdn());
        messagesSent.setStatus(Constants.WAITING_FEEDBACK);

        MessagesSent sent = messageSentRepository.saveAndFlush(messagesSent);
//        String callbackUrl = "http://34.67.196.163:8181/api/v1/sms_callback/callback/" + String.valueOf(sent.getId()) + "/" + String.valueOf(sent.getId());
//        messageBody.setCallbackURL(callbackUrl);
//        sendSMS(messageBody, client);
    }

    public void sendResetPasswordSms(User user, OkHttpClient client) throws IOException{
        MessageBody messageBody = new MessageBody();
        Random ran = new Random();
        Integer otp = ran.nextInt(100000);
        String encrypt = passwordEncryption.encrypt(String.valueOf(otp));
        user.setPassword(encrypt);
        user.setFirstTimeLogin(1);

        User updUser = userService.update(user);

        messageBody.setSms("Dear " + user.getName() + ", use " + String.valueOf(user.getOtpNumber()) + " as your pin and proceed to set your new " +
        "password");
        messageBody.setMsisdn(user.getPhone());

        LocalDateTime lastLocal = LocalDateTime.now();
        final MessagesSent messagesSent = new MessagesSent();

        MessagesSent sent = messageSentRepository.saveAndFlush(messagesSent);
        String callbackUrl = "http://34.67.196.163:8181/api/v1/sms_callback/callback/" + String.valueOf(sent.getId()) + "/" + String.valueOf(sent.getId());
        messageBody.setCallbackURL(callbackUrl);
        sendSMS(messageBody, client);
    }

    public void sendSemaSMS(MessageBodySema messageBodySema, OkHttpClient client)
    {
        try{
            Gson gson =new GsonBuilder().create();
            String messageBodyJson = gson.toJson(messageBodySema);

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, messageBodyJson);

            Request request = new Request.Builder()
                    .url(Constants.SMS_SANDBOX_URL)
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("AuthToken", Constants.X_TOKEN)
                    .build();

            Response response = client.newCall(request).execute();
            System.out.println(response);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
