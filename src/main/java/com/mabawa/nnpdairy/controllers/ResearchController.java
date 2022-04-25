package com.mabawa.nnpdairy.controllers;

import com.mabawa.nnpdairy.constants.Constants;
import com.mabawa.nnpdairy.models.Research;
import com.mabawa.nnpdairy.models.Response;
import com.mabawa.nnpdairy.models.Trainings;
import com.mabawa.nnpdairy.models.mongo.RMaterials;
import com.mabawa.nnpdairy.models.mongo.RResources;
import com.mabawa.nnpdairy.services.ImageService;
import com.mabawa.nnpdairy.services.ResearchService;
import com.mabawa.nnpdairy.services.mongo.RResourceService;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping({"api/v1/research"})
public class ResearchController {
    @Autowired
    private ResearchService researchService;

    @Autowired
    private RResourceService rResourceService;

    @Autowired
    private ImageService imageService;

    String title = Constants.TITLES[0];

    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Response> addNewResearch(@RequestPart("research") String researchStr, String topic, String url, @RequestPart("images") List<MultipartFile> content) {
        Research research = researchService.getJson(researchStr);
        if (research.getDescription().isEmpty()){
            String msg = "Missing research description.";
            return new ResponseEntity<Response>(this.RResponse(this.title, Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        }

        Optional<Research> optionalResearch = researchService.getResearchByDescription(research.getDescription());
        if (optionalResearch.isPresent()) {
            String msg = "A research already exists By Description Provided.";
            return new ResponseEntity<Response>(this.RResponse(this.title, Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        }

        LocalDateTime lastLocal = LocalDateTime.now();
        research.setCreated(Timestamp.valueOf(lastLocal));

        research = researchService.create(research);

        RMaterials rMaterials = new RMaterials();
        if (content != null){
            rMaterials.setResearchId(research.getId().toString());
            rMaterials.setTitle(topic);
            rMaterials.setUrl(url);
            try{
                List<RResources> rResourcesList = new ArrayList<>();
                for (MultipartFile con : content)
                {
                    RResources rResources = new RResources();
                    rResources.setImage(new Binary(BsonBinarySubType.BINARY, imageService.compressBytes(con.getBytes())));

                    rResourcesList.add(rResources);

                    rResources.setImageDownload(Base64.getEncoder().encodeToString(imageService.decompressBytes(rResources.getImage().getData())));
                }
                rMaterials.setrResources(rResourcesList);

                String resStr = rResourceService.addResources(rMaterials);

                System.out.printf("Resources : " + resStr);
            }catch (IOException ex){
                System.out.printf("Error : " + ex.toString());
                //throw new UnsupportedMediaException("Invalid or unsupported Media type.");
            }
        }
        List<RMaterials> rMaterialsList = new ArrayList<>();
        rMaterialsList.add(rMaterials);

        research.setrMaterials(rMaterialsList);

        HashMap suppzMap = new HashMap();
        suppzMap.put("research", research);
        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[0], suppzMap);
    }

    @PutMapping(path = {"edit-research/{id}"}, consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Response> editResearch(@PathVariable UUID id, @RequestPart("research") String researchStr, String topic, String url, @RequestPart("images") List<MultipartFile> content) {
        Research research = researchService.getJson(researchStr);
        Research savedResearch = researchService.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("No research found By ID Provided."));

        research.setId(savedResearch.getId());
        research = researchService.update(research);

        RMaterials rMaterials = new RMaterials();
        if (content != null){
            rResourceService.deleteResources(savedResearch.getId().toString());

            rMaterials.setResearchId(research.getId().toString());
            rMaterials.setTitle(topic);
            rMaterials.setUrl(url);
            try{
                List<RResources> rResourcesList = new ArrayList<>();
                for (MultipartFile con : content)
                {
                    RResources rResources = new RResources();
                    rResources.setImage(new Binary(BsonBinarySubType.BINARY, imageService.compressBytes(con.getBytes())));

                    rResourcesList.add(rResources);

                    rResources.setImageDownload(Base64.getEncoder().encodeToString(imageService.decompressBytes(rResources.getImage().getData())));
                }
                rMaterials.setrResources(rResourcesList);

                String resStr = rResourceService.addResources(rMaterials);

                System.out.printf("Resources : " + resStr);
            }catch (IOException ex){
                System.out.printf("Error : " + ex.toString());
                //throw new UnsupportedMediaException("Invalid or unsupported Media type.");
            }
        }
        List<RMaterials> rMaterialsList = new ArrayList<>();
        rMaterialsList.add(rMaterials);

        research.setrMaterials(rMaterialsList);

        HashMap suppzMap = new HashMap();
        suppzMap.put("research", research);
        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[0], suppzMap);
    }

    @PutMapping(path = {"edit-research-resources/{id}"}, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Response> editResearchResources(@PathVariable UUID id, String url, @RequestPart("images") List<MultipartFile> content) {
        Research savedResearch = researchService.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("No research found By ID Provided."));

        RMaterials rMaterials = new RMaterials();
        if (content != null){
            rResourceService.deleteResources(id.toString());

            rMaterials.setResearchId(id.toString());
            rMaterials.setTitle(savedResearch.getTopic());
            rMaterials.setUrl(url);
            try{
                List<RResources> rResourcesList = new ArrayList<>();
                for (MultipartFile con : content)
                {
                    RResources rResources = new RResources();
                    rResources.setImage(new Binary(BsonBinarySubType.BINARY, imageService.compressBytes(con.getBytes())));

                    rResourcesList.add(rResources);

                    rResources.setImageDownload(Base64.getEncoder().encodeToString(imageService.decompressBytes(rResources.getImage().getData())));
                }
                rMaterials.setrResources(rResourcesList);

                String resStr = rResourceService.addResources(rMaterials);

                System.out.printf("Resources : " + resStr);
            }catch (IOException ex){
                System.out.printf("Error : " + ex.toString());
                //throw new UnsupportedMediaException("Invalid or unsupported Media type.");
            }
        }
        List<RMaterials> rMaterialsList = new ArrayList<>();
        rMaterialsList.add(rMaterials);

        savedResearch.setrMaterials(rMaterialsList);

        HashMap suppzMap = new HashMap();
        suppzMap.put("research", savedResearch);
        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[0], suppzMap);
    }

    @GetMapping
    public ResponseEntity<Response> getAllResearch() {
        List<Research> researchList = researchService.getAllResearch();

        HashMap suppzMap = new HashMap();
        suppzMap.put("research", researchList);
        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[3], suppzMap);
    }

    @GetMapping(path = {"/{id}"})
    public ResponseEntity<Response> getResearchById(@PathVariable UUID id) {
        Research research = researchService.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Research doesn't exists By ID Provided."));

        research.setrMaterials(rResourceService.getResearchResources(research.getId().toString()));
        HashMap suppzMap = new HashMap();
        suppzMap.put("research", research);
        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[3], suppzMap);
    }

    @DeleteMapping(path = {"/delete/{id}"})
    public ResponseEntity<Response> deleteResearchById(@PathVariable UUID id) {
        Research research = researchService.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Research doesn't exists By ID Provided."));

        rResourceService.deleteResources(id.toString());
        researchService.deleteResearchById(id);

        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[4], new HashMap());
    }

    @DeleteMapping(path = {"/deleteAllResearch"})
    public ResponseEntity<Response> deleteAllResearch() {
        rResourceService.deleteAllResources();
        researchService.deleteAllResearch();

        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[4], new HashMap());
    }

    private ResponseEntity<Response> getResponseEntity(String title, String status, Integer success, String msg, HashMap map) {
        return new ResponseEntity<Response>(this.RResponse(title, status, success, msg, map), HttpStatus.OK);
    }

    private Response RResponse(String title, String status, Integer success, String msg, HashMap map) {
        Response response = new Response();
        response.setTitle(title);
        response.setStatus(status);
        response.setMessage(msg);
        response.setSuccess(success);
        response.setData(map);
        return response;
    }
}
