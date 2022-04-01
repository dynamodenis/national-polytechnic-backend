package com.mabawa.nnpdairy.controllers;

import com.mabawa.nnpdairy.constants.Constants;
import com.mabawa.nnpdairy.models.Response;
import com.mabawa.nnpdairy.models.TCategoryz;
import com.mabawa.nnpdairy.models.mongo.TCategoryResources;
import com.mabawa.nnpdairy.services.ImageService;
import com.mabawa.nnpdairy.services.TCategoryzService;
import com.mabawa.nnpdairy.services.mongo.TCategoryzResService;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
@RequestMapping({"api/v1/t-category"})
public class TCategoryzController {
    @Autowired
    private TCategoryzService tCategoryzService;

    @Autowired
    private TCategoryzResService tCategoryzResService;

    @Autowired
    private ImageService imageService;

    String title = Constants.TITLES[0];

    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Response> addNewTCategory(@RequestPart("tcategory") String tcategory, @RequestPart("image") MultipartFile image) {
        TCategoryz tCategoryz = tCategoryzService.getJson(tcategory);
        Optional<TCategoryz>  catzOptional = tCategoryzService.getTcategoryByName(tCategoryz.getName());
        if (catzOptional.isPresent())
        {
            String msg = "A Category already exists By Name Provided.";
            return new ResponseEntity<Response>(this.TResponse(this.title, Constants.STATUS[1], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        }
        LocalDateTime lastLocal = LocalDateTime.now();
        tCategoryz.setInit_dte(Timestamp.valueOf(lastLocal));
        tCategoryz = tCategoryzService.create(tCategoryz);
        String imgStr = "";
        if (image != null && !image.isEmpty()){
            try{
                TCategoryResources tCategoryResources = new TCategoryResources();
                tCategoryResources.setId(tCategoryz.getId().toString());
                tCategoryResources.setImageTitle(tCategoryz.getName());
                tCategoryResources.setImage(new Binary(BsonBinarySubType.BINARY, imageService.compressBytes(image.getBytes())));

                String tcatzResStr = tCategoryzResService.addTcategoryResources(tCategoryResources);

                imgStr = Base64.getEncoder().encodeToString(imageService.decompressBytes(tCategoryResources.getImage().getData()));
                tCategoryResources.setImageDownload(imgStr);
            }catch (IOException ex){
                System.out.printf("Error : " + ex.toString());
                //throw new UnsupportedMediaException("Invalid or unsupported Media type.");
            }
        }

        tCategoryz.setImageDownloads(imgStr);

        HashMap catzMap = new HashMap();
        catzMap.put("tcategory", tCategoryz);
        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[0], catzMap);
    }

    @PutMapping(path = {"/{id}"}, consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Response> editCategory(@PathVariable UUID id, @RequestPart("tcategory") String tcategory, @RequestPart("image") MultipartFile image) {
        TCategoryz tCategoryz = tCategoryzService.getJson(tcategory);
        TCategoryz savedTCategoryz = tCategoryzService.getTcategoryById(id)
                .orElseThrow(()-> new EntityNotFoundException("A Category doesn't exists By ID Provided."));

        tCategoryz.setId(savedTCategoryz.getId());
        tCategoryz.setInit_dte(savedTCategoryz.getInit_dte());
        tCategoryzService.updateTcategory(tCategoryz);

        //List<TCategoryResources> tCategoryResourcesList = new ArrayList<>();
        String imgStr = "";
        if (image != null && !image.isEmpty()){
            tCategoryzResService.deleteTcategoryResources(tCategoryz.getId().toString());
            try{
                TCategoryResources tCategoryResources = new TCategoryResources();

                tCategoryResources.setId(tCategoryz.getId().toString());
                tCategoryResources.setImageTitle(tCategoryz.getName());
                tCategoryResources.setImage(new Binary(BsonBinarySubType.BINARY, imageService.compressBytes(image.getBytes())));

                String tcatzResStr = tCategoryzResService.addTcategoryResources(tCategoryResources);

                imgStr = Base64.getEncoder().encodeToString(imageService.decompressBytes(tCategoryResources.getImage().getData()));
                tCategoryResources.setImageDownload(imgStr);

                //tCategoryResourcesList.add(tCategoryResources);
            }catch (IOException ex){
                System.out.printf("Error : " + ex.toString());
                //throw new UnsupportedMediaException("Invalid or unsupported Media type.");
            }
        }else{
            imgStr = tCategoryzResService.getTcategoryResourceString(tCategoryz.getId().toString());
        }
        tCategoryz.setImageDownloads(imgStr);

        HashMap catzMap = new HashMap();
        catzMap.put("tcategory", tCategoryz);
        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[0], catzMap);
    }

    @PutMapping(path = {"/edit-tcategory-image/{id}"}, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Response> editCategoryImage(@PathVariable UUID id, @RequestPart("image") MultipartFile image) {
        TCategoryz savedTCategoryz = tCategoryzService.getTcategoryById(id)
                .orElseThrow(()-> new EntityNotFoundException("A Category doesn't exists By ID Provided."));

        //List<TCategoryResources> tCategoryResourcesList = new ArrayList<>();
        TCategoryResources tCategoryResources = new TCategoryResources();
        String imgStr = "";
        if (image != null && !image.isEmpty()){
            tCategoryzResService.deleteTcategoryResources(id.toString());
            try{
                tCategoryResources.setId(savedTCategoryz.getId().toString());
                tCategoryResources.setImageTitle(savedTCategoryz.getName());
                tCategoryResources.setImage(new Binary(BsonBinarySubType.BINARY, imageService.compressBytes(image.getBytes())));

                String tcatzResStr = tCategoryzResService.addTcategoryResources(tCategoryResources);

                imgStr = Base64.getEncoder().encodeToString(imageService.decompressBytes(tCategoryResources.getImage().getData()));
                tCategoryResources.setImageDownload(imgStr);

                //tCategoryResourcesList.add(tCategoryResources);
            }catch (IOException ex){
                System.out.printf("Error : " + ex.toString());
                //throw new UnsupportedMediaException("Invalid or unsupported Media type.");
            }
        }else{
            imgStr = tCategoryzResService.getTcategoryResourceString(savedTCategoryz.getId().toString());
        }

        savedTCategoryz.setImageDownloads(imgStr);

        HashMap catzMap = new HashMap();
        catzMap.put("tcategory", savedTCategoryz);
        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[0], catzMap);
    }

    @DeleteMapping(path = {"/delete/{id}"})
    public ResponseEntity<Response> deleteCategoryById(@PathVariable UUID id) {
        TCategoryz category = tCategoryzService.getTcategoryById(id)
                .orElseThrow(()-> new EntityNotFoundException("No such Category By Id Provided."));

        tCategoryzService.deleteTcategory(id);
        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[4], new HashMap());
    }

    @DeleteMapping(path = {"/deleteAll"})
    public ResponseEntity<Response> deleteAllCategory() {
        tCategoryzService.deleteAllTCategory();
        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[4], new HashMap());
    }

    @GetMapping
    public ResponseEntity<Response> getCategoryList() {
        List<TCategoryz> categoryList = tCategoryzService.getAllTList();

        HashMap catzMap = new HashMap();
        catzMap.put("tcategory", categoryList);
        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[3], catzMap);
    }

    @GetMapping("/with-images-list")
    public ResponseEntity<Response> getCategoryListwithImages() {
        List<TCategoryz> categoryList = tCategoryzService.getAllTImagesList();

        HashMap catzMap = new HashMap();
        catzMap.put("tcategory", categoryList);
        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[3], catzMap);
    }

    @GetMapping(path = {"/{id}"})
    public ResponseEntity<Response> getCategoryById(@PathVariable UUID id) {
        TCategoryz tCategoryz = tCategoryzService.getTcategoryById(id)
                .orElseThrow(()-> new EntityNotFoundException("No such Category By Id Provided."));

        tCategoryz.setImageDownloads(tCategoryzResService.getTcategoryResourceString(tCategoryz.getId().toString()));

        HashMap catzMap = new HashMap();
        catzMap.put("tcategory", tCategoryz);
        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[3], catzMap);
    }

    @GetMapping(path = {"/filter/{name}"})
    public ResponseEntity<Response> getCategoryNameLike(@PathVariable String name, @RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
        List<TCategoryz> tCategoryzs = tCategoryzService.getNameContaining(name);
        if (tCategoryzs == null || tCategoryzs.isEmpty()) {
            String msg = "No such Category By Name Provided.";
            return new ResponseEntity<Response>(this.TResponse(this.title, Constants.STATUS[1], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        } else {
            Pageable paging = PageRequest.of(pageNo, pageSize);
            List<TCategoryz> categoryList = tCategoryzService.filterTCategoryz(name, paging);

            HashMap catzMap = new HashMap();
            catzMap.put("tcategory", categoryList);
            return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[3], catzMap);
        }
    }

    @GetMapping(path = "/t-category-resources/{id}")
    public ResponseEntity<Response> getTCategoryResources(@PathVariable UUID id)
    {
        TCategoryResources category = tCategoryzResService.getTcategoryResource(id.toString())
                .orElseThrow(()-> new EntityNotFoundException("No resources found with Category By Id Provided."));

        HashMap catzMap = new HashMap();
        catzMap.put("tcategoryres", category);

        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[3], catzMap);
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
