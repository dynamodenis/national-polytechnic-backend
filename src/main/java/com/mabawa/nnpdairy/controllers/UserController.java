package com.mabawa.nnpdairy.controllers;

import com.mabawa.nnpdairy.constants.Constants;
import com.mabawa.nnpdairy.models.*;
import com.mabawa.nnpdairy.services.*;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@RestController
@RequestMapping({"api/v1/user"})
public class UserController {
    @Autowired
    private ConfigService configService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired PswsService pswsService;

    @Autowired
    private PasswordEncryption passwordEncryption;

    @Autowired
    private KeyGenerator keyGenerator;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthUserDetailsService authUserDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private SendSMSService sendSMSService;

    @Autowired
    private SendEmailService sendEmailService;

    @Autowired
    private ConsultantService consultantService;

    String title = Constants.TITLES[0];

    @PostMapping
    public ResponseEntity<Response> addUser(@RequestBody User user) {
        String status;
        String msg;
        if (!user.getName().isEmpty() && !user.getPassword().isEmpty()){
            Optional<User>  optionalNme = userService.getUserByName(user.getName());
            if (optionalNme.isPresent()){
                msg = "A Vendor already exists By Name Provided.";
                return new ResponseEntity<Response>(this.UserResponse(this.title, Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
            }
            Optional<User> optionalFone = userService.getUserByPhone(user.getPhone());
            if (optionalFone.isPresent()){
                msg = "User with Phone No. provided already exists.";
                return new ResponseEntity<Response>(this.UserResponse(this.title, Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
            }
            if (!user.getMail().isEmpty()){
                Optional<User> optionalMail = userService.getUserByMail(user.getMail());
                if (optionalMail.isPresent()){
                    msg = "User with Email provided already exists.";
                    return new ResponseEntity<Response>(this.UserResponse(this.title, Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
                }
            }

            Role role = roleService.get(user.getRole())
                    .orElseThrow(()-> new EntityNotFoundException("That role Id does not exist"));
            String psw = this.passwordEncryption.encrypt(user.getPassword());
            LocalDateTime lastLocal = LocalDateTime.now();
            user.setPassword(psw);
            user.setCreated(Timestamp.valueOf(lastLocal));
            user.setVerified(true);
            User savedUser = userService.create(user);

            if (configService.get().getAgepsw() == 1){
                Psws psws = new Psws();
                psws.setUserid(savedUser.getId());
                psws.setLastdate(Timestamp.valueOf(lastLocal));
                psws.setPsw1(psw);
                psws.setPsw2("");
                psws.setPsw3("");
                psws.setPsw4("");
                psws.setPsw5("");
                psws.setLastpsw(1);
                Psws savedPsws = pswsService.create(psws);
            }

            HashMap hashMap = new HashMap();
            hashMap.put("user", savedUser);
            return new ResponseEntity<Response>(this.UserResponse(Constants.TITLES[0], Constants.STATUS[0], 1, Constants.MESSAGES[0], hashMap), HttpStatus.OK);
        }else{
            status = Constants.STATUS[2];
            msg = "Missing params";
            return new ResponseEntity<Response>(this.UserResponse(this.title, status, 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = {"/user-registration/{prm}"})
    public ResponseEntity<Response> userRegistration(@RequestBody User user, @PathVariable Integer prm) {
        String status;
        String msg;
        if (user.getType() == null)
        {
            status = Constants.STATUS[2];
            msg = "Missing User Type.";
            return new ResponseEntity<Response>(this.UserResponse(this.title, status, 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        }
        if (!user.getName().isEmpty() && !user.getPassword().isEmpty() && !user.getPhone().isEmpty()){
            if (user.getType() == 1)
            {
                status = Constants.STATUS[2];
                msg = "Wrong User Type provided.";
                return new ResponseEntity<Response>(this.UserResponse(this.title, status, 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
            }

            Optional<User> optionalFone = userService.getUserByPhone(user.getPhone());
            if (optionalFone.isPresent()){
                msg = "User with Phone No. provided already exists.";
                return new ResponseEntity<Response>(this.UserResponse(this.title, Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
            }

            if (!user.getMail().isEmpty()){
                Optional<User> optionalMail = userService.getUserByMail(user.getMail());
                if (optionalMail.isPresent()){
                    msg = "User with Email provided already exists.";
                    return new ResponseEntity<Response>(this.UserResponse(this.title, Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
                }
            }

            user.setRole(UUID.fromString("d25daf55-89d6-4e19-ba8b-824a988940c6"));

            Role role = roleService.get(user.getRole())
                    .orElseThrow(()-> new EntityNotFoundException("That role Id does not exist"));
            String psw = this.passwordEncryption.encrypt(user.getPassword());
            LocalDateTime lastLocal = LocalDateTime.now();
            user.setPassword(psw);
            user.setCreated(Timestamp.valueOf(lastLocal));
            user.setVerified(false);

            Psws psws = new Psws();

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(180, TimeUnit.SECONDS)
                    .readTimeout(180, TimeUnit.SECONDS)
                    .build();

            User savedUser = sendSMSSema(user, client, prm);

            if (configService.get().getAgepsw() == 1){
                psws.setUserid(savedUser.getId());
                psws.setLastdate(Timestamp.valueOf(lastLocal));
                psws.setPsw1(user.getPassword());
                psws.setPsw2("");
                psws.setPsw3("");
                psws.setPsw4("");
                psws.setPsw5("");
                psws.setLastpsw(1);
                Psws savedPsws = pswsService.create(psws);
            }

            user.setPassword(null);

            HashMap hashMap = new HashMap();
            hashMap.put("user", savedUser);
            return new ResponseEntity<Response>(this.UserResponse(Constants.TITLES[0], Constants.STATUS[0], 1, Constants.MESSAGES[0], hashMap), HttpStatus.OK);
        }else{
            status = Constants.STATUS[2];
            msg = "Missing params";
            return new ResponseEntity<Response>(this.UserResponse(this.title, status, 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/verify-phoneNumber")
    public ResponseEntity<Response> verifyPhoneNumber(@RequestBody User user) {
        String status;
        String msg;
        if (user.getPhone().isEmpty() || user.getOtpNumber().toString().isEmpty())
        {
            status = Constants.STATUS[2];
            msg = "Missing Phone number or OTP";
            return new ResponseEntity<Response>(this.UserResponse(this.title, status, 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        }

        User user1 = userService.verifyOtp(user.getPhone(), user.getOtpNumber())
                .orElseThrow(()-> new EntityNotFoundException("Invalid Phone No. or OTP."));

        if (user1.isVerified()) {
            status = Constants.STATUS[2];
            msg = "User already verified.";
            return new ResponseEntity<Response>(this.UserResponse(this.title, status, 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        }
        if (user1.getOtpExpiration() < System.currentTimeMillis()) {
            status = Constants.STATUS[2];
            msg = "OTP EXPIRED.";
            return new ResponseEntity<Response>(this.UserResponse(this.title, status, 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        }

        user1.setVerified(true);
        user1 = userService.update(user1);

        String psw = user1.getPassword();

        user1.setRoles(userService.getUserRoles(user1.getRole()));

        msg = "User Verified.";
        status = Constants.STATUS[0];

        HashMap userzMap = new HashMap();
        List roles = user1.getRoles();
        List userRolezs = new ArrayList();
        StringBuilder sb = new StringBuilder();
        Iterator var15 = roles.iterator();

        while(var15.hasNext()) {
            Role role = (Role)var15.next();
            sb.append(role.getName());
            sb.append(",");
            userRolezs.add(role.getRolez());
        }

        String rolz = sb.toString();
        if (rolz.length() > 0) {
            rolz = rolz.substring(0, rolz.length() - 1);
        }

        try {
            List<Consultants> consultantsList = consultantService.getConsultantByUserId(user1.getId());

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user1.getName(), psw));
            UserDetails userDetails = authUserDetailsService.loadUserByUsername(user1.getName());
            String jwtToken = jwtTokenUtil.generateToken(userDetails);
            userzMap.put("id", user1.getId());
            userzMap.put("name", user1.getName());
            userzMap.put("phone", user1.getPhone());
            userzMap.put("role", rolz);
            userzMap.put("roles", userRolezs);
            userzMap.put("token", jwtToken);
            if (!consultantsList.isEmpty())
            {
                consultantsList.forEach(consultants -> {
                    userzMap.put("consultantId", consultants.getId());
                });
            }

            return new ResponseEntity<Response>(this.UserResponse(Constants.TITLES[0], status, 1, msg, userzMap), HttpStatus.OK);
        }catch (BadCredentialsException var18) {
            status = Constants.STATUS[2];
            msg = "User verification failed.";

            return new ResponseEntity<Response>(this.UserResponse(Constants.TITLES[0], status, 0, msg, userzMap), HttpStatus.OK);
        }
    }

    private User sendSMS(User user, OkHttpClient client) {
        MessageBody messageBody = new MessageBody();
        Random ran = new Random();
        Integer otp = ran.nextInt(100000);

        user.setOtpNumber(otp);
        Long otpExpiry = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(30);
        user.setOtpExpiration(otpExpiry);

        User savedUser = userService.create(user);

        sendSMSService.PrepSms(messageBody, savedUser, client);


        return savedUser;
    }

    private User sendSMSSema(User user, OkHttpClient client, int prm) {
        Random ran = new Random();
        Integer otp = ran.nextInt(100000);

        user.setOtpNumber(otp);
        Long otpExpiry = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(30);
        user.setOtpExpiration(otpExpiry);

        User savedUser = userService.create(user);

        if (prm == 1)
        {
            MessageBodySema messageBodySema = new MessageBodySema();
            sendSMSService.PrepSmsSema(messageBodySema, savedUser, client);
        }else
        {
            String msg = "Dear " + user.getName() + ", please use the One Time Password below to verify your access to NNP Diary platform." +
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

            sendEmailService.sendRegistrationOtp(msg, user.getMail());
        }


        return savedUser;
    }

    @GetMapping
    public ResponseEntity<Response> getAllUsers() {
        List<User> userList = userService.getAllUsers();

        HashMap hashMap = new HashMap();
        hashMap.put("userList", userList);
        return new ResponseEntity<Response>(this.UserResponse(Constants.TITLES[0], Constants.STATUS[0], 1, Constants.MESSAGES[3], hashMap), HttpStatus.OK);
    }

    @GetMapping(path = {"/nnp-users"})
    public ResponseEntity<Response> getNnpUsers() {
        List<User> userList = userService.getNNpUsers(1);

        HashMap hashMap = new HashMap();
        hashMap.put("userList", userList);
        return new ResponseEntity<Response>(this.UserResponse(Constants.TITLES[0], Constants.STATUS[0], 1, Constants.MESSAGES[3], hashMap), HttpStatus.OK);
    }

    @GetMapping(path = {"/end-users"})
    public ResponseEntity<Response> getEndUsers() {
        List<User> userList = userService.getEndUsers(1);

        HashMap hashMap = new HashMap();
        hashMap.put("userList", userList);
        return new ResponseEntity<Response>(this.UserResponse(Constants.TITLES[0], Constants.STATUS[0], 1, Constants.MESSAGES[3], hashMap), HttpStatus.OK);
    }

    @GetMapping(path = {"/searchById/{id}"})
    public ResponseEntity<Response> getUserzByID(@PathVariable UUID id) {
        User user = userService.getUserById(id)
                .orElseThrow(()-> new EntityNotFoundException("No System User exists By ID Provided."));

        HashMap hashMap = new HashMap();
        hashMap.put("userList", user);
        return new ResponseEntity<Response>(this.UserResponse(Constants.TITLES[0], Constants.STATUS[0], 0, Constants.MESSAGES[3], hashMap), HttpStatus.OK);
    }

    @GetMapping(path = {"/searchByName/{name}"})
    public ResponseEntity<Response> getUserzByName(@PathVariable String name) {
        User user = userService.getUserByName(name)
                .orElseThrow(()-> new EntityNotFoundException("No System User exists By Name Provided."));

        HashMap hashMap = new HashMap();
        hashMap.put("userList", user);
        return new ResponseEntity<Response>(this.UserResponse(Constants.TITLES[0], Constants.STATUS[0], 0, Constants.MESSAGES[3], hashMap), HttpStatus.OK);
    }

    @GetMapping({"/user-roles"})
    public ResponseEntity<Response> getUserRoles() {
        List<Role> roleList = roleService.getUserRoles();

        HashMap hashMap = new HashMap();
        hashMap.put("roles", roleList);
        return new ResponseEntity<Response>(this.UserResponse(Constants.TITLES[0], Constants.STATUS[0], 1, Constants.MESSAGES[3], hashMap), HttpStatus.OK);
    }

    @GetMapping({"/{roleId}/roles"})
    public ResponseEntity<Response> getUsersByRole(@PathVariable UUID roleId) {
        List<User> userList = userService.findAllByRole(roleId);

        HashMap hashMap = new HashMap();
        hashMap.put("userList", userList);
        return new ResponseEntity<Response>(this.UserResponse(Constants.TITLES[0], Constants.STATUS[0], 1, Constants.MESSAGES[3], hashMap), HttpStatus.OK);
    }

    @PutMapping({"/{userId}/edit-user-details"})
    public ResponseEntity<Response> editUserDetails(@PathVariable UUID userId, @RequestBody User user) {
        User savedUser =userService.getUserById(userId)
                .orElseThrow(()-> new EntityNotFoundException("A User with that ID does not exist."));
        String status;
        String msg;

        if (!user.getPassword().isEmpty() && user.getPassword().trim().length() >= 6){
            String encPsw;
            if (!user.getRole().toString().isEmpty()) {
                Role role = roleService.get(user.getRole())
                        .orElseThrow(()-> new EntityNotFoundException("That role Id does not exist"));
            }

            String psw = user.getPassword();
            encPsw = this.passwordEncryption.encrypt(psw);
            user.setPassword(encPsw);
            LocalDateTime lastLocal = LocalDateTime.now();
            int succss = 0;
            if (configService.get().getAgepsw() == 1) {
                Psws psws = pswsService.getUserPasswords(userId, encPsw);
                int lpsws = psws.getLastpsw();
                if (lpsws == 0) {
                    psws.setLastpsw(1);
                    pswsService.create(psws);
                    status = Constants.STATUS[0];
                    msg = Constants.MESSAGES[0];
                    succss = 1;
                } else {
                    String psw1 = psws.getPsw1();
                    String psw2 = psws.getPsw2();
                    String psw3 = psws.getPsw3();
                    String psw4 = psws.getPsw4();
                    String psw5 = psws.getPsw5();
                    String[] xstsPsw = new String[]{psw1, psw2, psw3, psw4, psw5};
                    Stream var10000 = Arrays.stream(xstsPsw);
                    Objects.requireNonNull(encPsw);
                    boolean exists = var10000.anyMatch(encPsw::equals);
                    if (exists) {
                        status = Constants.STATUS[2];
                        msg = "Can't Re-use a Password already used.";
                        succss = 0;
                    } else {
                        if (lpsws == 1) {
                            psw2 = encPsw;
                        } else if (lpsws == 2) {
                            psw3 = encPsw;
                        } else if (lpsws == 3) {
                            psw4 = encPsw;
                        } else if (lpsws == 4) {
                            psw5 = encPsw;
                        } else if (lpsws == 5) {
                            psw1 = encPsw;
                        }

                        pswsService.updatePsws(Timestamp.valueOf(lastLocal), psw1, psw2, psw3, psw4, psw5, userId);

                        succss = 1;
                    }
                }
            }else{
                succss = 1;
            }

            user.setId(savedUser.getId());
            status = Constants.STATUS[0];
            msg = Constants.MESSAGES[0];
            savedUser = userService.update(user);
            HashMap hashMap = new HashMap();
            hashMap.put("user", savedUser);
            return new ResponseEntity<Response>(this.UserResponse(Constants.TITLES[0], status, Integer.valueOf(succss), msg, hashMap), HttpStatus.OK);
        }else{
            status = Constants.STATUS[2];
            msg = "Password Invalid or Blank";
            return new ResponseEntity<Response>(this.UserResponse(this.title, status, 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = {"/login"})
    public ResponseEntity<Response> authenticate(@RequestBody AuthData authData) {
        String msg = "";
        String status = "";

        if (authData.getPhone().isEmpty() || authData.getPassword().isEmpty()){
            status = Constants.STATUS[2];
            msg = "Missing params";
            return new ResponseEntity<Response>(this.UserResponse(this.title, status, 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        }

        HashMap userzMap = new HashMap();
        int succs = 0;
        int prm = authData.getPrm();
        UUID id = authData.getId();
        String srchStr = authData.getName();
        if (prm == 2){
            srchStr = authData.getPhone();
        }else if (prm == 4){
            srchStr = authData.getEmail();
        }else{
            srchStr = authData.getName();
        }
        String psw = this.passwordEncryption.encrypt(authData.getPassword());
        HttpStatus httpStatus = HttpStatus.OK;
        User userz = userService.getUserviaParam(prm, id, srchStr);
        if (userz == null) {
            if (prm == 1) {
                status = Constants.STATUS[2];
                msg = "No such User By Id Provided.";
                httpStatus = HttpStatus.BAD_REQUEST;
            } else if (prm == 2) {
                status = Constants.STATUS[2];
                msg = "No such User By Phone No. Provided.";
                httpStatus = HttpStatus.BAD_REQUEST;
            } else if (prm == 3) {
                status = Constants.STATUS[2];
                msg = "No such User By Name Provided.";
                httpStatus = HttpStatus.BAD_REQUEST;
            }
        }else {
            if (!userz.isVerified()){
                status = Constants.STATUS[2];
                msg = "Phone number not yet verified.";
                httpStatus = HttpStatus.BAD_REQUEST;
            }else{
                List<Consultants> consultantsList = consultantService.getConsultantByUserId(userz.getId());

                List roles = userz.getRoles();
                List userRolezs = new ArrayList();
                StringBuilder sb = new StringBuilder();
                Iterator var15 = roles.iterator();

                while(var15.hasNext()) {
                    Role role = (Role)var15.next();
                    sb.append(role.getName());
                    sb.append(",");
                    userRolezs.add(role.getRolez());
                }

                String rolz = sb.toString();
                if (rolz.length() > 0) {
                    rolz = rolz.substring(0, rolz.length() - 1);
                }

                try {
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userz.getName(), psw));
                    UserDetails userDetails = authUserDetailsService.loadUserByUsername(userz.getName());
                    String jwtToken = jwtTokenUtil.generateToken(userDetails);
                    userzMap.put("id", userz.getId());
                    userzMap.put("name", userz.getName());
                    userzMap.put("phone", userz.getPhone());
                    if (!userz.getMail().isEmpty())
                    {
                        userzMap.put("email", userz.getMail());
                    }
                    userzMap.put("isFirstLogin", userz.getFirstTimeLogin());
                    userzMap.put("role", rolz);
                    userzMap.put("roles", userRolezs);
                    userzMap.put("token", jwtToken);
                    if (!consultantsList.isEmpty())
                    {
                        consultantsList.forEach(consultants -> {
                            userzMap.put("consultantId", consultants.getId());
                        });
                    }
                    status = Constants.STATUS[0];
                    msg = Constants.MESSAGES[2];
                    succs = 1;
                }catch (BadCredentialsException var18) {
                    status = Constants.STATUS[2];
                    msg = "Incorrect Username or Password.";
                    succs = 0;
                    httpStatus = HttpStatus.BAD_REQUEST;
                }
            }
        }

        return new ResponseEntity<Response>(this.UserResponse(Constants.TITLES[0], status, Integer.valueOf(succs), msg, userzMap), httpStatus);
    }

    @PostMapping("/resendCode/{prm}")
    public ResponseEntity resendCode(@RequestBody User user, @PathVariable Integer prm) throws IOException {
        String status;
        String msg;
        if (user.getPhone().isEmpty())
        {
            status = Constants.STATUS[2];
            msg = "Missing Phone number.";
            return new ResponseEntity<Response>(this.UserResponse(this.title, status, 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        }

        User foneUser = userService.getUserByPhone(user.getPhone())
                .orElseThrow(()-> new EntityNotFoundException("No such User By Phone No. Provided."));

        user = foneUser;
        if (user.isVerified()) {
            status = Constants.STATUS[2];
            msg = "User already verified.";
            return new ResponseEntity<Response>(this.UserResponse(this.title, status, 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        }

        Psws psws = new Psws();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(180, TimeUnit.SECONDS)
                .readTimeout(180, TimeUnit.SECONDS)
                .build();

        User savedUser = sendSMSSema(user, client, prm);

        user.setPassword(null);

        HashMap hashMap = new HashMap();
        hashMap.put("user", savedUser);
        return new ResponseEntity<Response>(this.UserResponse(Constants.TITLES[0], Constants.STATUS[0], 1, Constants.MESSAGES[0], hashMap), HttpStatus.OK);
    }

    @PostMapping(path = {"/descrypter"})
    public ResponseEntity<Response> descrypter(@RequestBody AuthData authData) {
        String psw = ""; //passwordEncryption.decrypt(authData.getPassword());

        HashMap userzMap = new HashMap();
        userzMap.put("password", psw);
        return new ResponseEntity<Response>(this.UserResponse(Constants.TITLES[0], Constants.STATUS[0], 1, Constants.MESSAGES[2], userzMap), HttpStatus.OK);
    }

    @GetMapping(path = "/sendSMS/{phone}")
    public ResponseEntity<Response> sendSms(@PathVariable String phone)
    {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(180, TimeUnit.SECONDS)
                .readTimeout(180, TimeUnit.SECONDS)
                .build();
        MessageBody messageBody = new MessageBody();
        messageBody.setSms("Sample Text.");
        messageBody.setMsisdn(phone);
        String callbackUrl = "http://34.67.196.163:8181/api/v1/sms_callback/callback/" + String.valueOf(1234567) + "/" + String.valueOf(7654321);
        messageBody.setCallbackURL(callbackUrl);

        sendSMSService.sendSMS(messageBody, client);
        String msg = "Message Sent.";
        HashMap userzMap = new HashMap();

        return new ResponseEntity<Response>(this.UserResponse(Constants.TITLES[0], Constants.STATUS[0], 1, msg, userzMap), HttpStatus.OK);
    }

    @GetMapping(path = "/sendSMSSema/{phone}")
    public ResponseEntity<Response> sendSmsSema(@PathVariable String phone)
    {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(180, TimeUnit.SECONDS)
                .readTimeout(180, TimeUnit.SECONDS)
                .build();
        MessageBodySema messageBodySema = new MessageBodySema();
        messageBodySema.setText("Easy Peasy.");
        messageBodySema.setRecipients(phone);
//        String callbackUrl = "http://34.67.196.163:8181/api/v1/sms_callback/callback/" + String.valueOf(1234567) + "/" + String.valueOf(7654321);
//        messageBody.setCallbackURL(callbackUrl);

        sendSMSService.sendSemaSMS(messageBodySema, client);
        String msg = "Message Sent.";
        HashMap userzMap = new HashMap();

        return new ResponseEntity<Response>(this.UserResponse(Constants.TITLES[0], Constants.STATUS[0], 1, msg, userzMap), HttpStatus.OK);
    }

    @PutMapping(path = {"/updatepassword"})
    public ResponseEntity<Response> updatePassword(@RequestBody User userz) {
        User userOptional = userService.getUserById(userz.getId())
                .orElseThrow(()-> new EntityNotFoundException("No such User By Id Provided."));

        UUID userId = userz.getId();
        String psw = userz.getPassword();
        String msg = "";
        String status = "";
        int succss = 0;
        if (psw.length() < 6) {
            msg = "Password Invalid or Blank.";
            return new ResponseEntity<Response>(this.UserResponse(Constants.TITLES[0], Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        }

        String encPsw = this.passwordEncryption.encrypt(psw);
        if (configService.get().getAgepsw() == 1) {
            LocalDateTime lastLocal = LocalDateTime.now();
            Psws psws = pswsService.getUserPasswords(userId, encPsw);
            int lpsws = psws.getLastpsw();
            if (lpsws == 0) {
                psws.setLastpsw(1);
                pswsService.create(psws);
                status = Constants.STATUS[0];
                msg = Constants.MESSAGES[0];
                succss = 1;
            } else {
                String psw1 = psws.getPsw1();
                String psw2 = psws.getPsw2();
                String psw3 = psws.getPsw3();
                String psw4 = psws.getPsw4();
                String psw5 = psws.getPsw5();
                String[] xstsPsw = new String[]{psw1, psw2, psw3, psw4, psw5};
                Stream var10000 = Arrays.stream(xstsPsw);
                Objects.requireNonNull(encPsw);
                boolean exists = var10000.anyMatch(encPsw::equals);
                if (exists) {
                    status = Constants.STATUS[2];
                    msg = "Can't Re-use a Password already used.";
                    succss = 0;
                } else {
                    if (lpsws == 1) {
                        psw2 = encPsw;
                    } else if (lpsws == 2) {
                        psw3 = encPsw;
                    } else if (lpsws == 3) {
                        psw4 = encPsw;
                    } else if (lpsws == 4) {
                        psw5 = encPsw;
                    } else if (lpsws == 5) {
                        psw1 = encPsw;
                    }

                    pswsService.updatePsws(Timestamp.valueOf(lastLocal), psw1, psw2, psw3, psw4, psw5, userId);

                    succss = 1;
                }
            }
        }else{
            succss = 1;
        }

        userService.updateUserPassword(encPsw, userz.getId());
        return new ResponseEntity<Response>(this.UserResponse(Constants.TITLES[0], status, Integer.valueOf(succss), msg, new HashMap()), HttpStatus.OK);
    }

    @GetMapping("/reset-password/{phone}/{prm}")
    public ResponseEntity resetPassword(@PathVariable String phone, @PathVariable Integer prm){
        String msg = "";
        String status = "";

        User savedUser = new User();
        if (prm == 1)
        {
            savedUser = userService.getUserByPhone(phone)
                    .orElseThrow(()-> new EntityNotFoundException("No such User By Phone No. Provided."));

            if (savedUser.getPhone().isEmpty())
            {
                savedUser.setPhone(phone);
            }
        }else{
            savedUser = userService.getUserByMail(phone)
                    .orElseThrow(()-> new EntityNotFoundException("No such User By Email Provided."));

            if (savedUser.getMail().isEmpty())
            {
                savedUser.setMail(phone);
            }
        }

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(180, TimeUnit.SECONDS)
                .readTimeout(180, TimeUnit.SECONDS)
                .build();

        try{
            if (prm == 1) {
                sendSMSService.sendResetPasswordSms(savedUser, client);
            }else{
                sendEmailService.sendResetOtp(savedUser);
            }
        }catch (IOException ex){
            status = Constants.STATUS[2];
            msg = "Internal server error.";
            return new ResponseEntity<Response>(this.UserResponse(this.title, status, 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        }

        msg = "Password sent to the user.";
        status = Constants.STATUS[0];
        return new ResponseEntity<Response>(this.UserResponse(Constants.TITLES[0], status, 0, msg, new HashMap()), HttpStatus.OK);
    }

    @DeleteMapping(path = {"/delete/{id}"})
    public ResponseEntity<Response> deleteUserzByID(@PathVariable UUID id) {
        User userz = userService.getUserById(id)
                .orElseThrow(()-> new EntityNotFoundException("No System User exists By ID Provided."));

        if (userz.getAdmin() == 99) {
            String msg = "User cannot be deleted.";
            return new ResponseEntity<Response>(this.UserResponse(Constants.TITLES[0], Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        }

        pswsService.deleteByUserId(id);
        userService.deleteUserById(id);
        return new ResponseEntity<Response>(this.UserResponse(Constants.TITLES[0], Constants.STATUS[0], 0, Constants.MESSAGES[4], new HashMap()), HttpStatus.OK);
    }

    @PostMapping(path = {"/contact-us"})
    public ResponseEntity<Response> sendContactusEmail(@RequestBody ContactUs contactUs)
    {
        String msg = "";
        String status = "";
        if (contactUs.getEmail().isEmpty() || contactUs.getName().isEmpty() || contactUs.getMessage().isEmpty())
        {
            status = Constants.STATUS[2];
            msg = "Missing params";
            return new ResponseEntity<Response>(this.UserResponse(this.title, status, 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        }

        sendEmailService.sendSimpleMessage(contactUs);

        status = Constants.STATUS[0];
        msg = "Sending Email";
        return new ResponseEntity<Response>(this.UserResponse(this.title, status, 0, msg, new HashMap()), HttpStatus.OK);
    }


    private Response UserResponse(String title, String status, Integer success, String msg, HashMap map) {
        Response response = new Response();
        response.setTitle(title);
        response.setStatus(status);
        response.setMessage(msg);
        response.setSuccess(success);
        response.setData(map);
        return response;
    }
}
