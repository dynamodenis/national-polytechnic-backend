package com.mabawa.nnpdairy.controllers;

import com.mabawa.nnpdairy.constants.Constants;
import com.mabawa.nnpdairy.models.Response;
import com.mabawa.nnpdairy.models.Trainings;
import com.mabawa.nnpdairy.models.mongo.TMaterials;
import com.mabawa.nnpdairy.models.mongo.TMaterialsData;
import com.mabawa.nnpdairy.services.ImageService;
import com.mabawa.nnpdairy.services.TrainingsService;
import com.mabawa.nnpdairy.services.mongo.TMaterialService;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.stylesheets.LinkStyle;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping({"api/v1/training"})
public class TrainingController {
    @Autowired
    private TrainingsService trainingsService;

    @Autowired
    private TMaterialService tMaterialService;

    @Autowired
    private ImageService imageService;

    String title = Constants.TITLES[0];

    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Response> addNewTrainers(@RequestPart("training") String training, String topic, String url, @RequestPart("images") List<MultipartFile> content) {
        Trainings trainings = trainingsService.getJson(training);
        if (trainings.getDescription().isEmpty()) {
            String msg = "Missing training description.";
            return new ResponseEntity<Response>(this.TResponse(this.title, Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        }
        Optional<Trainings> trainingsOptional = trainingsService.getTrainingByDescription(trainings.getDescription());
        if (trainingsOptional.isPresent()) {
            String msg = "A Training already exists By Description Provided.";
            return new ResponseEntity<Response>(this.TResponse(this.title, Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        } else {
            LocalDateTime lastLocal = LocalDateTime.now();
            trainings.setCreated(Timestamp.valueOf(lastLocal));
            trainings.setTopic(topic);
            trainings = trainingsService.create(trainings);

            TMaterials tMaterials = new TMaterials();
            if (content != null){
                try{
                    tMaterials.setTrainingsId(trainings.getId().toString());
                    tMaterials.setTitle("");
                    tMaterials.setUrl(url);
                    List<TMaterialsData> tMaterialsList = new ArrayList<>();
                    for (MultipartFile con : content)
                    {
                        TMaterialsData tMaterialsData =  new TMaterialsData();
                        tMaterialsData.setContent(new Binary(BsonBinarySubType.BINARY, imageService.compressBytes(con.getBytes())));
                        tMaterialsData.setType(1);

                        tMaterialsList.add(tMaterialsData);

                        tMaterialsData.setContentDownload(Base64.getEncoder().encodeToString(imageService.decompressBytes(tMaterialsData.getContent().getData())));
                    }
                    tMaterials.settMaterialsData(tMaterialsList);

                    String matStr = tMaterialService.addTMaterial(tMaterials);

                    System.out.printf("Training : " + matStr);
                }catch (IOException ex){
                    System.out.printf("Error : " + ex.toString());
                }
            }
            List<TMaterials> tMaterialsList = new ArrayList<>();
            tMaterialsList.add(tMaterials);
            
            trainings.settMaterials(tMaterialsList);

            HashMap suppzMap = new HashMap();
            suppzMap.put("trainings", trainings);
            return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[0], suppzMap);
        }
    }

    @PutMapping(path = {"edit-training/{id}"}, consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Response> editTraining(@PathVariable UUID id, @RequestPart("training") String training, String topic, String url, @RequestPart("images") List<MultipartFile> content) {
        Trainings trainings = trainingsService.getJson(training);
        Optional<Trainings> optionalTrainings = trainingsService.findById(id);
        if (!optionalTrainings.isPresent()) {
            String msg = "No Training found By ID Provided.";
            return new ResponseEntity<Response>(this.TResponse(this.title, Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        } else {
            Trainings savedTraining = optionalTrainings.get();
            trainings.setId(savedTraining.getId());
            trainings.setTopic(topic);
            trainings = trainingsService.update(trainings);

            List<TMaterials> tMaterialsList = new ArrayList<>();

            TMaterials tMaterials = new TMaterials();
            if (content != null){
                tMaterialService.deleteTraining(savedTraining.getId().toString());

                try{
                    tMaterials.setTrainingsId(trainings.getId().toString());
                    tMaterials.setTitle("");
                    tMaterials.setUrl(url);
                    List<TMaterialsData> tMaterialsListD = new ArrayList<>();
                    for (MultipartFile con : content)
                    {
                        TMaterialsData tMaterialsData =  new TMaterialsData();
                        tMaterialsData.setContent(new Binary(BsonBinarySubType.BINARY, imageService.compressBytes(con.getBytes())));
                        tMaterialsData.setType(1);

                        tMaterialsListD.add(tMaterialsData);

                        tMaterialsData.setContentDownload(Base64.getEncoder().encodeToString(imageService.decompressBytes(tMaterialsData.getContent().getData())));
                    }
                    tMaterials.settMaterialsData(tMaterialsListD);

                    String matStr = tMaterialService.addTMaterial(tMaterials);

                    tMaterialsList.add(tMaterials);
                    System.out.printf("Training : " + matStr);
                }catch (IOException ex){
                    System.out.printf("Error : " + ex.toString());
                }
            }else{
                tMaterialsList = tMaterialService.getTrainingTMaterials(trainings.getId().toString());
            }

            trainings.settMaterials(tMaterialsList);

            HashMap hashMap = new HashMap();
            hashMap.put("training", trainings);
            return new ResponseEntity<Response>(this.TResponse(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[0], hashMap), HttpStatus.OK);
        }
    }

    @PutMapping(path = {"edit-training-materials/{id}"}, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Response> editTrainingMaterials(@PathVariable UUID id, String url, @RequestPart("images") List<MultipartFile> content) {
        Optional<Trainings> optionalTrainings = trainingsService.findById(id);
        if (!optionalTrainings.isPresent()) {
            String msg = "No Training found By ID Provided.";
            return new ResponseEntity<Response>(this.TResponse(this.title, Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        } else {
            Trainings savedTraining = optionalTrainings.get();

            List<TMaterials> tMaterialsList = new ArrayList<>();

            TMaterials tMaterials = new TMaterials();
            if (content != null){
                tMaterialService.deleteTraining(savedTraining.getId().toString());

                try{
                    tMaterials.setTrainingsId(id.toString());
                    tMaterials.setTitle("");
                    tMaterials.setUrl(url);
                    List<TMaterialsData> tMaterialsListD = new ArrayList<>();
                    for (MultipartFile con : content)
                    {
                        TMaterialsData tMaterialsData =  new TMaterialsData();
                        tMaterialsData.setContent(new Binary(BsonBinarySubType.BINARY, imageService.compressBytes(con.getBytes())));
                        tMaterialsData.setType(1);

                        tMaterialsListD.add(tMaterialsData);

                        tMaterialsData.setContentDownload(Base64.getEncoder().encodeToString(imageService.decompressBytes(tMaterialsData.getContent().getData())));
                    }
                    tMaterials.settMaterialsData(tMaterialsListD);

                    String matStr = tMaterialService.addTMaterial(tMaterials);

                    tMaterialsList.add(tMaterials);

                    System.out.printf("Training : " + matStr);
                }catch (IOException ex){
                    System.out.printf("Error : " + ex.toString());
                }
            }else{
                tMaterialsList = tMaterialService.getTrainingTMaterials(savedTraining.getId().toString());
            }

            savedTraining.settMaterials(tMaterialsList);

            HashMap hashMap = new HashMap();
            hashMap.put("training", savedTraining);
            return new ResponseEntity<Response>(this.TResponse(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[0], hashMap), HttpStatus.OK);
        }
    }

    @GetMapping
    public ResponseEntity<Response> getAllTrainings() {
        List<Trainings> trainingsList = trainingsService.getAllTraining();


        HashMap suppzMap = new HashMap();
        suppzMap.put("trainings", trainingsList);
        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[3], suppzMap);
    }

    @GetMapping(path = "/with-images-list")
    public ResponseEntity<Response> getAllTrainingsWithImages() {
        List<Trainings> trainingsList = trainingsService.getAllTrainingWithImagesList();

        HashMap suppzMap = new HashMap();
        suppzMap.put("trainings", trainingsList);
        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[3], suppzMap);
    }

    @GetMapping(path = {"/{id}"})
    public ResponseEntity<Response> getTrainingsById(@PathVariable UUID id) {
        Optional<Trainings> trainingsOptional = trainingsService.findById(id);
        if (!trainingsOptional.isPresent()) {
            String msg = "Training doesn't exists By ID Provided.";
            return new ResponseEntity<Response>(this.TResponse(this.title, Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        } else {
            Trainings trainings = trainingsOptional.get();
            trainings.settMaterials(tMaterialService.getTrainingTMaterials(trainings.getId().toString()));
            HashMap suppzMap = new HashMap();
            suppzMap.put("training", trainings);
            return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[3], suppzMap);
        }
    }

    @GetMapping(path = {"/filter-by-category/{categoryId}"})
    public ResponseEntity<Response> filterTrainingsByCategoryId(@PathVariable UUID categoryId) {
        List<Trainings> trainingsList = trainingsService.filterTrainingsByCategoryID(categoryId);
        if (trainingsList.isEmpty()) {
            String msg = "Training doesn't exists By Category ID Provided.";
            return new ResponseEntity<Response>(this.TResponse(this.title, Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        } else {
            trainingsList.forEach(trainings -> {
                trainings.settMaterials(tMaterialService.getTrainingTMaterials(trainings.getId().toString()));
            });

            HashMap suppzMap = new HashMap();
            suppzMap.put("training", trainingsList);
            return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[3], suppzMap);
        }
    }

    @DeleteMapping(path = {"/delete/{id}"})
    public ResponseEntity<Response> deleteTrainingById(@PathVariable UUID id) {
        Optional<Trainings> trainingsOptional = trainingsService.findById(id);
        if (!trainingsOptional.isPresent()) {
            String msg = "Training doesn't exists By ID Provided.";
            return new ResponseEntity<Response>(this.TResponse(this.title, Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        } else {
            tMaterialService.deleteTraining(id.toString());
            trainingsService.deleteTrainingById(id);
            return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[4], new HashMap());
        }
    }

    private ResponseEntity<Response> getResponseEntity(String title, String status, Integer success, String msg, HashMap map) {
        return new ResponseEntity<Response>(this.TResponse(title, status, success, msg, map), HttpStatus.OK);
    }

    private Response TResponse(String title, String status, Integer success, String msg, HashMap map) {
        Response response = new Response();
        response.setTitle(title);
        response.setStatus(status);
        response.setMessage(msg);
        response.setSuccess(success);
        response.setData(map);
        return response;
    }
}