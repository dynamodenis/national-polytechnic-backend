package com.mabawa.nnpdairy.controllers;

import com.mabawa.nnpdairy.constants.Constants;
import com.mabawa.nnpdairy.models.RCategoryz;
import com.mabawa.nnpdairy.models.Response;
import com.mabawa.nnpdairy.models.TCategoryz;
import com.mabawa.nnpdairy.services.RCategoryzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping({"api/v1/r-category"})
public class RCategoryzController {
    @Autowired
    private RCategoryzService rCategoryzService;

    String title = Constants.TITLES[0];

    @PostMapping
    public ResponseEntity<Response> addNewTCategory(@RequestBody RCategoryz rCategoryz) {
        Optional<RCategoryz> catzOptional = rCategoryzService.getTcategoryByName(rCategoryz.getName());
        if (catzOptional.isPresent()) {
            String msg = "A Category already exists By Name Provided.";
            return new ResponseEntity<Response>(this.TResponse(this.title, Constants.STATUS[1], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        } else {
            LocalDateTime lastLocal = LocalDateTime.now();
            rCategoryz.setInit_dte(Timestamp.valueOf(lastLocal));
            rCategoryz = rCategoryzService.create(rCategoryz);

            HashMap catzMap = new HashMap();
            catzMap.put("r-category", rCategoryz);
            return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[0], catzMap);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> editCategory(@PathVariable UUID id, @RequestBody RCategoryz rCategoryz) {
        Optional<RCategoryz> catzOptional = rCategoryzService.getRcategoryById(id);
        if (!catzOptional.isPresent()) {
            String msg = "A Category doesn't exists By ID Provided.";
            return new ResponseEntity<Response>(this.TResponse(this.title, Constants.STATUS[1], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        } else {
            RCategoryz savedRcategoryz = catzOptional.get();
            rCategoryz.setId(savedRcategoryz.getId());

            rCategoryz = rCategoryzService.update(rCategoryz);

            HashMap catzMap = new HashMap();
            catzMap.put("r-category", rCategoryz);
            return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[0], catzMap);
        }
    }

    @DeleteMapping(path = {"/delete/{id}"})
    public ResponseEntity<Response> deleteCategoryById(@PathVariable UUID id) {
        Optional<RCategoryz> categoryOptional = rCategoryzService.getRcategoryById(id);
        if (!categoryOptional.isPresent()) {
            String msg = "No such Category By Id Provided.";
            return new ResponseEntity<Response>(this.TResponse(this.title, Constants.STATUS[1], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        } else {
            rCategoryzService.deleteRcategory(id);
            return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[4], new HashMap());
        }
    }

    @DeleteMapping(path = {"/deleteAll"})
    public ResponseEntity<Response> deleteAllCategory() {
        rCategoryzService.deleteAllRCategory();
        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[4], new HashMap());
    }

    @GetMapping
    public ResponseEntity<Response> getCategoryList() {
        List<RCategoryz> categoryList = rCategoryzService.getAllTList();

        HashMap catzMap = new HashMap();
        catzMap.put("r-category", categoryList);
        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[3], catzMap);
    }

    @GetMapping(path = {"/{id}"})
    public ResponseEntity<Response> getCategoryById(@PathVariable UUID id) {
        Optional<RCategoryz> categoryOptional = rCategoryzService.getRcategoryById(id);
        if (!categoryOptional.isPresent()) {
            String msg = "No such Category By Id Provided.";
            return new ResponseEntity<Response>(this.TResponse(this.title, Constants.STATUS[1], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        } else {

            HashMap catzMap = new HashMap();
            catzMap.put("r-category", categoryOptional.get());
            return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[3], catzMap);
        }
    }

    @GetMapping(path = {"/filter/{name}"})
    public ResponseEntity<Response> getCategoryNameLike(@PathVariable String name, @RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
        List<RCategoryz> rCategoryzs = rCategoryzService.getNameContaining(name);
        if (rCategoryzs == null || rCategoryzs.isEmpty()) {
            String msg = "No such Category By Name Provided.";
            return new ResponseEntity<Response>(this.TResponse(this.title, Constants.STATUS[1], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        } else {
            Pageable paging = PageRequest.of(pageNo, pageSize);
            List<RCategoryz> categoryList = rCategoryzService.filterRCategoryz(name, paging);

            HashMap catzMap = new HashMap();
            catzMap.put("r-category", categoryList);
            return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[3], catzMap);
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