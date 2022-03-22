package com.mabawa.nnpdairy.controllers;

import com.mabawa.nnpdairy.constants.Constants;
import com.mabawa.nnpdairy.models.Appointments;
import com.mabawa.nnpdairy.models.Consultants;
import com.mabawa.nnpdairy.models.Response;
import com.mabawa.nnpdairy.models.mongo.ConsultantsProfile;
import com.mabawa.nnpdairy.services.AppointmentService;
import com.mabawa.nnpdairy.services.ConsultantService;
import com.mabawa.nnpdairy.services.ImageService;
import com.mabawa.nnpdairy.services.mongo.ConsultantsProfileService;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping({"api/v1/consultants"})
public class ConsultantController {
    @Autowired
    private ConsultantService consultantService;

    @Autowired
    private ConsultantsProfileService consultantsProfileService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private AppointmentService appointmentService;

    String title = Constants.TITLES[0];

    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Response> addNewConsultant(@RequestPart("consultant") String consultant, @RequestPart("image") MultipartFile image) {
        Consultants consultants = consultantService.getJson(consultant);
        if (consultants.getTitle().isEmpty())
        {
            String msg = "Please provide Consultant Title.";
            return new ResponseEntity<Response>(this.CResponse(this.title, Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        }
        if (!consultants.getName().isEmpty()) {
            Optional<Consultants> consultantsOptional = consultantService.getConsultantByName(consultants.getName());
            if (consultantsOptional.isPresent()) {
                String msg = "A Consultant already exists By Name Provided.";
                return new ResponseEntity<Response>(this.CResponse(this.title, Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
            } else {
                LocalDateTime lastLocal = LocalDateTime.now();
                consultants.setCreated(Timestamp.valueOf(lastLocal));
                consultants = consultantService.create(consultants);

                List<ConsultantsProfile> consultantsProfileList = new ArrayList<>();

                if (image != null && !image.isEmpty()){
                    try{
                        ConsultantsProfile consultantsProfile = new ConsultantsProfile();
                        consultantsProfile.setId(consultants.getId().toString());
                        consultantsProfile.setTitle(consultants.getName());
                        consultantsProfile.setImage(new Binary(BsonBinarySubType.BINARY, imageService.compressBytes(image.getBytes())));

                        String profStr = consultantsProfileService.addProfile(consultantsProfile);

                        consultantsProfile.setImageDownload(Base64.getEncoder().encodeToString(imageService.decompressBytes(consultantsProfile.getImage().getData())));

                        consultantsProfileList.add(consultantsProfile);
                    }catch (IOException ex){
                        System.out.printf("Error : " + ex.toString());
                    }
                }

                consultants.setConsultantsProfileList(consultantsProfileList);

                HashMap suppzMap = new HashMap();
                suppzMap.put("consultant", consultants);
                return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[0], suppzMap);
            }
        } else {
            String msg = "Missing Params.";
            return new ResponseEntity<Response>(this.CResponse(this.title, Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(path = {"edit-consultant/{id}"}, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Response> editConsultant(@PathVariable UUID id, @RequestPart("consultant") String consultant, @RequestPart("image") MultipartFile image) {
        Consultants consultants = consultantService.getJson(consultant);
        Optional<Consultants> optionalConsultants = consultantService.findById(id);
        if (!optionalConsultants.isPresent()) {
            String msg = "No Consultant found By ID Provided.";
            return new ResponseEntity<Response>(this.CResponse(this.title, Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        } else {
            Consultants savedConsultant = optionalConsultants.get();
            consultants.setId(savedConsultant.getId());
            consultants.setCreated(savedConsultant.getCreated());
            consultants = consultantService.update(consultants);

            List<ConsultantsProfile> consultantsProfileList = new ArrayList<>();

            if (image != null && !image.isEmpty()){
                consultantsProfileService.deleteConsultantProfile(savedConsultant.getId().toString());

                try{
                    ConsultantsProfile consultantsProfile = new ConsultantsProfile();
                    consultantsProfile.setId(consultants.getId().toString());
                    consultantsProfile.setTitle(consultants.getName());
                    consultantsProfile.setImage(new Binary(BsonBinarySubType.BINARY, imageService.compressBytes(image.getBytes())));

                    String profStr = consultantsProfileService.addProfile(consultantsProfile);

                    consultantsProfile.setImageDownload(Base64.getEncoder().encodeToString(imageService.decompressBytes(consultantsProfile.getImage().getData())));

                    consultantsProfileList.add(consultantsProfile);
                }catch (IOException ex){
                    System.out.printf("Error : " + ex.toString());
                }
            }else{
                consultantsProfileList = consultantsProfileService.getConsultantProf(consultants.getId().toString());
            }

            consultants.setConsultantsProfileList(consultantsProfileList);

            HashMap hashMap = new HashMap();
            hashMap.put("consultant", consultants);
            return new ResponseEntity<Response>(this.CResponse(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[0], hashMap), HttpStatus.OK);
        }
    }

    @PutMapping(path = {"edit-consultant-image/{id}"}, consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Response> editConsultantImage(@PathVariable UUID id, @RequestPart("image") MultipartFile image) {
        Optional<Consultants> optionalConsultants = consultantService.findById(id);
        if (!optionalConsultants.isPresent()) {
            String msg = "No Consultant found By ID Provided.";
            return new ResponseEntity<Response>(this.CResponse(this.title, Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        } else {
            Consultants savedConsultant = optionalConsultants.get();

            List<ConsultantsProfile> consultantsProfileList = new ArrayList<>();

            if (image != null && !image.isEmpty()){
                consultantsProfileService.deleteConsultantProfile(savedConsultant.getId().toString());

                try{
                    ConsultantsProfile consultantsProfile = new ConsultantsProfile();
                    consultantsProfile.setId(savedConsultant.getId().toString());
                    consultantsProfile.setTitle(savedConsultant.getName());
                    consultantsProfile.setImage(new Binary(BsonBinarySubType.BINARY, imageService.compressBytes(image.getBytes())));

                    String profStr = consultantsProfileService.addProfile(consultantsProfile);

                    consultantsProfile.setImageDownload(Base64.getEncoder().encodeToString(imageService.decompressBytes(consultantsProfile.getImage().getData())));

                    consultantsProfileList.add(consultantsProfile);
                }catch (IOException ex){
                    System.out.printf("Error : " + ex.toString());
                }
            }else{
                consultantsProfileList = consultantsProfileService.getConsultantProf(savedConsultant.getId().toString());
            }

            savedConsultant.setConsultantsProfileList(consultantsProfileList);

            HashMap hashMap = new HashMap();
            hashMap.put("consultant", savedConsultant);
            return new ResponseEntity<Response>(this.CResponse(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[0], hashMap), HttpStatus.OK);
        }
    }

    @GetMapping
    public ResponseEntity<Response> getAllConsultants() {
        List<Consultants> consultantsList = consultantService.getAllConsultants();
        consultantsList.forEach(consultants -> {
            String consultantId = consultants.getId().toString();
            consultants.setImageDownloads(consultantsProfileService.getConsultantProfImage(consultantId));
        });

        HashMap suppzMap = new HashMap();
        suppzMap.put("consultants", consultantsList);
        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[3], suppzMap);
    }

    @GetMapping(path = "/with-images-list")
    public ResponseEntity<Response> getAllConsultantsWithImage() {
        List<Consultants> consultantsList = consultantService.getAllConsultants();
        consultantsList.forEach(consultants -> {
            String consultantId = consultants.getId().toString();
            consultants.setImageDownloads(consultantsProfileService.getConsultantProfImage(consultantId));
        });

        HashMap suppzMap = new HashMap();
        suppzMap.put("consultants", consultantsList);
        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[3], suppzMap);
    }

    @GetMapping(path = {"/{id}"})
    public ResponseEntity<Response> getConsultantById(@PathVariable UUID id) {
        Optional<Consultants> consultantsOptional = consultantService.findById(id);
        if (!consultantsOptional.isPresent()) {
            String msg = "Consultant doesn't exists By ID Provided.";
            return new ResponseEntity<Response>(this.CResponse(this.title, Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        } else {
            Consultants consultants = consultantsOptional.get();
            consultants.setImageDownloads(consultantsProfileService.getConsultantProfImage(consultants.getId().toString()));
            HashMap suppzMap = new HashMap();
            suppzMap.put("consultant", consultants);
            return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[3], suppzMap);
        }
    }

    @DeleteMapping(path = {"/delete/{id}"})
    public ResponseEntity<Response> deleteConsultantById(@PathVariable UUID id) {
        Optional<Consultants> consultantsOptional = consultantService.findById(id);
        if (!consultantsOptional.isPresent()) {
            String msg = "Consultant doesn't exists By ID Provided.";
            return new ResponseEntity<Response>(this.CResponse(this.title, Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        } else {
            consultantsProfileService.deleteConsultantProfile(id.toString());
            consultantService.deleteById(id);
            return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[4], new HashMap());
        }
    }

    @PostMapping(path = "new-appointment")
    public ResponseEntity<Response> addNewAppointment(@RequestBody Appointments appointments){
        if (appointments.getAppuser().toString().isEmpty() || appointments.getConsultant().toString().isEmpty()){
            String msg = "Missing Params.";
            return new ResponseEntity<Response>(this.CResponse(this.title, Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        }
        if (appointments.getDuration().toString().isEmpty() || appointments.getDfactor().toString().isEmpty()){
            String msg = "Missing Params.";
            return new ResponseEntity<Response>(this.CResponse(this.title, Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        }
        Optional<Appointments> optionalAppointments = appointmentService.getUserAppointment(appointments.getAppuser(), appointments.getConsultant(), 1);
        if (optionalAppointments.isPresent()){
            String msg = "Provided User already has an OPEN appointment with selected Consultant .";
            return new ResponseEntity<Response>(this.CResponse(this.title, Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        }

        Timestamp eTime = new Timestamp(new Date().getTime());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(appointments.getStime().getTime());
        if (appointments.getDfactor().equals("mins")){
            calendar.add(Calendar.MINUTE, appointments.getDuration());
        }else if (appointments.getDfactor().equals("hrs")){
            calendar.add(Calendar.HOUR, appointments.getDuration());
        }
        else
        {
            String msg = "Invalid timing factor provided.";
            return new ResponseEntity<Response>(this.CResponse(this.title, Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        }
        eTime = new Timestamp(calendar.getTime().getTime());

        List<Appointments> appointmentsList = appointmentService.getAppointmentListBetweenStime(appointments.getConsultant(), appointments.getStime(), eTime, 2);
        if (!appointmentsList.isEmpty())
        {
            String msg = "Consultant provided already has an Appointment on timimg provided.";
            return new ResponseEntity<Response>(this.CResponse(this.title, Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        }
        LocalDateTime lastLocal = LocalDateTime.now();
        appointments.setCreated(Timestamp.valueOf(lastLocal));

        appointments = appointmentService.create(appointments);

        HashMap suppzMap = new HashMap();
        suppzMap.put("appointment", appointments);
        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[0], suppzMap);
    }

    @PutMapping(path = "edit-appointment/{id}")
    public ResponseEntity<Response> editAppointment(@PathVariable UUID id, @RequestBody Appointments appointments){
        if (appointments.getAppuser().toString().isEmpty() || appointments.getConsultant().toString().isEmpty()){
            String msg = "Missing Params.";
            return new ResponseEntity<Response>(this.CResponse(this.title, Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        }
        Optional<Appointments> optionalAppointments = appointmentService.findById(id);
        if (!optionalAppointments.isPresent()){
            String msg = "Appointment not found by ID provided.";
            return new ResponseEntity<Response>(this.CResponse(this.title, Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        }

        Timestamp eTime = new Timestamp(new Date().getTime());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(appointments.getStime().getTime());
        if (appointments.getDfactor().equals("mins")){
            calendar.add(Calendar.MINUTE, appointments.getDuration());
        }else if (appointments.getDfactor().equals("hrs")){
            calendar.add(Calendar.HOUR, appointments.getDuration());
        }
        else
        {
            String msg = "Invalid timing factor provided.";
            return new ResponseEntity<Response>(this.CResponse(this.title, Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        }
        eTime = new Timestamp(calendar.getTime().getTime());

        List<Appointments> appointmentsList = appointmentService.getAppointmentListBetweenStime(appointments.getConsultant(), appointments.getStime(), eTime, 2);
        if (!appointmentsList.isEmpty())
        {
            String msg = "Consultant provided already has an Appointment on timimg provided.";
            return new ResponseEntity<Response>(this.CResponse(this.title, Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        }

        Appointments savedAppointment = optionalAppointments.get();
        appointments.setCreated(savedAppointment.getCreated());
        appointments.setId(savedAppointment.getId());

        appointments = appointmentService.update(appointments);

        HashMap suppzMap = new HashMap();
        suppzMap.put("appointment", appointments);
        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[0], suppzMap);
    }

    @GetMapping(path = "getAllAppointments")
    public ResponseEntity<Response> getAllAppointments() {
        List<Appointments> appointmentsList = appointmentService.findAllAppointments();

        HashMap suppzMap = new HashMap();
        suppzMap.put("appointments", appointmentsList);
        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[3], suppzMap);
    }

    @GetMapping(path = "getAllConsultantAppointments/{consultantId}")
    public ResponseEntity<Response> getAllConsultantAppointments(@PathVariable UUID consultantId) {
        List<Appointments> appointmentsList = appointmentService.getConsultantAppointments(consultantId);

        HashMap suppzMap = new HashMap();
        suppzMap.put("appointments", appointmentsList);
        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[3], suppzMap);
    }

    @GetMapping(path = "filterConsultantAppointments/{consultantId}/{status}")
    public ResponseEntity<Response> filterConsultantAppointments(@PathVariable UUID consultantId, @PathVariable Integer status) {
        List<Appointments> appointmentsList = appointmentService.filterConsultantAppointments(consultantId, status);

        HashMap suppzMap = new HashMap();
        suppzMap.put("appointments", appointmentsList);
        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[3], suppzMap);
    }

    @GetMapping(path = "getAllUserAppointments/{userId}")
    public ResponseEntity<Response> getAllUserAppointments(@PathVariable UUID userId) {
        List<Appointments> appointmentsList = appointmentService.getUserAppointments(userId);

        HashMap suppzMap = new HashMap();
        suppzMap.put("appointments", appointmentsList);
        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[3], suppzMap);
    }

    @GetMapping(path = "filterUserAppointments/{userId}/{status}")
    public ResponseEntity<Response> filterUserAppointments(@PathVariable UUID userId, @PathVariable Integer status) {
        List<Appointments> appointmentsList = appointmentService.filterUserAppointments(userId, status);

        HashMap suppzMap = new HashMap();
        suppzMap.put("appointments", appointmentsList);
        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[3], suppzMap);
    }

    @DeleteMapping(path = {"/delete-appointment/{id}"})
    public ResponseEntity<Response> deleteAppointmentById(@PathVariable UUID id) {
        Optional<Appointments> optionalAppointments = appointmentService.findById(id);
        if (!optionalAppointments.isPresent()) {
            String msg = "Appointment doesn't exists By ID Provided.";
            return new ResponseEntity<Response>(this.CResponse(this.title, Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        } else {
            appointmentService.deleteById(id);
            return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[4], new HashMap());
        }
    }

    @DeleteMapping(path = {"/delete-appointment-consultant/{consultant}"})
    public ResponseEntity<Response> deleteAppointmentByConsultant(@PathVariable UUID consultant) {
        appointmentService.deleteAppointmentByConsultant(consultant);
        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[4], new HashMap());
    }

    @DeleteMapping(path = {"/delete-appointment-user/{appuser}"})
    public ResponseEntity<Response> deleteAppointmentByUser(@PathVariable UUID appuser) {
        appointmentService.deleteAppointmentByUser(appuser);
        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[4], new HashMap());
    }

    private ResponseEntity<Response> getResponseEntity(String title, String status, Integer success, String msg, HashMap map) {
        return new ResponseEntity<Response>(this.CResponse(title, status, success, msg, map), HttpStatus.OK);
    }

    private Response CResponse(String title, String status, Integer success, String msg, HashMap map) {
        Response response = new Response();
        response.setTitle(title);
        response.setStatus(status);
        response.setMessage(msg);
        response.setSuccess(success);
        response.setData(map);
        return response;
    }
}
