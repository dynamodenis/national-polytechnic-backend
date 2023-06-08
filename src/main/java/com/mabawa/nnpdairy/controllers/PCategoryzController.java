package com.mabawa.nnpdairy.controllers;

import com.mabawa.nnpdairy.constants.Constants;
import com.mabawa.nnpdairy.models.*;
import com.mabawa.nnpdairy.services.MarketplaceService;
import com.mabawa.nnpdairy.services.PCategoryzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping({"api/v1/p-category"})
public class PCategoryzController {
    @Autowired
    private PCategoryzService pCategoryzService;
    @Autowired
    private MarketplaceService marketplaceService;
    String title = Constants.TITLES[0];

    @PostMapping
    public ResponseEntity<Response> addNewTCategory(@RequestBody PCategoryz pCategoryz) {
        Optional<PCategoryz> optionalPCategoryz = pCategoryzService.getTcategoryByName(pCategoryz.getName());
        if (optionalPCategoryz.isPresent()) {
            String msg = "A Category already exists By Name Provided.";
            return new ResponseEntity<Response>(this.PResponse(this.title, Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        }
        LocalDateTime lastLocal = LocalDateTime.now();
        pCategoryz.setInit_dte(Timestamp.valueOf(lastLocal));
        pCategoryz = pCategoryzService.create(pCategoryz);

        HashMap catzMap = new HashMap();
        catzMap.put("pcategory", pCategoryz);
        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[0], catzMap);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Response> editCategory(@PathVariable UUID id, @RequestBody PCategoryz pCategoryz) {
        PCategoryz pCategoryzSaved = pCategoryzService.getTcategoryById(id)
                .orElseThrow(()-> new EntityNotFoundException("A Category doesn't exists By ID Provided."));

        pCategoryz.setId(pCategoryzSaved.getId());

        pCategoryz = pCategoryzService.update(pCategoryz);

        HashMap catzMap = new HashMap();
        catzMap.put("pcategory", pCategoryz);
        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[0], catzMap);
    }

    @DeleteMapping(path = {"/delete/{id}"})
    public ResponseEntity<Response> deleteCategoryById(@PathVariable UUID id) {
        PCategoryz category = pCategoryzService.getTcategoryById(id)
                .orElseThrow(()-> new EntityNotFoundException("No such Category By Id Provided."));

        pCategoryzService.deleteTcategory(id);
        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[4], new HashMap());
    }

    @DeleteMapping(path = {"/deleteAll"})
    public ResponseEntity<Response> deleteAllCategory() {
        pCategoryzService.deleteAllTCategory();
        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[4], new HashMap());
    }

    @GetMapping
    public ResponseEntity<Response> getCategoryList() {
        List<PCategoryz> categoryList = pCategoryzService.getAllTList();

        HashMap catzMap = new HashMap();
        catzMap.put("pcategory", categoryList);
        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[3], catzMap);
    }

    @GetMapping(path = "/marketplace-types")
    public ResponseEntity<Response> getMarketPlaceTypesList() {
        List<MarketplaceTypes> categoryList = marketplaceService.getAllTypes();

        HashMap catzMap = new HashMap();
        catzMap.put("mtypes", categoryList);
        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[3], catzMap);
    }

    @GetMapping(path = {"/{id}"})
    public ResponseEntity<Response> getCategoryById(@PathVariable UUID id) {
        PCategoryz category = pCategoryzService.getTcategoryById(id)
                .orElseThrow(()-> new EntityNotFoundException("No such Category By Id Provided."));

        HashMap catzMap = new HashMap();
        catzMap.put("mcategory", category);
        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[3], catzMap);
    }

    @GetMapping(path = {"/filter/{name}"})
    public ResponseEntity<Response> getCategoryNameLike(@PathVariable String name, @RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
        List<PCategoryz> tCategoryzs = pCategoryzService.getNameContaining(name);
        if (tCategoryzs == null || tCategoryzs.isEmpty()) {
            String msg = "No such Category By Name Provided.";
            return new ResponseEntity<Response>(this.PResponse(this.title, Constants.STATUS[1], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        } else {
            Pageable paging = PageRequest.of(pageNo, pageSize);
            List<PCategoryz> categoryList = pCategoryzService.filterTCategoryz(name, paging);

            HashMap catzMap = new HashMap();
            catzMap.put("pcategory", categoryList);
            return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[3], catzMap);
        }
    }

    private ResponseEntity<Response> getResponseEntity(String title, String status, Integer success, String msg, HashMap map) {
        return new ResponseEntity<Response>(this.PResponse(title, status, success, msg, map), HttpStatus.OK);
    }

    private Response PResponse(String title, String status, Integer success, String msg, HashMap map) {
        Response response = new Response();
        response.setTitle(title);
        response.setStatus(status);
        response.setMessage(msg);
        response.setSuccess(success);
        response.setData(map);
        return response;
    }

}
