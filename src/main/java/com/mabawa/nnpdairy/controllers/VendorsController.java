package com.mabawa.nnpdairy.controllers;

import com.mabawa.nnpdairy.constants.Constants;
import com.mabawa.nnpdairy.models.Response;
import com.mabawa.nnpdairy.models.Vendors;
import com.mabawa.nnpdairy.services.VendorsService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping({"api/v1/vendors"})
public class VendorsController {
    @Autowired
    private VendorsService vendorsService;

    String title = Constants.TITLES[0];

    @PostMapping
    public ResponseEntity<Response> addNewSupplier(@RequestBody Vendors vendors) {
        if (!vendors.getName().isEmpty()) {
            Optional<Vendors> vendorOptional = vendorsService.getVendorByName(vendors.getName());
            if (vendorOptional.isPresent()) {
                String msg = "A Vendor already exists By Name Provided.";
                return new ResponseEntity<Response>(this.VResponse(this.title, Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
            } else {
                LocalDateTime lastLocal = LocalDateTime.now();
                vendors.setCreated(Timestamp.valueOf(lastLocal));
                vendors = vendorsService.create(vendors);

                HashMap suppzMap = new HashMap();
                suppzMap.put("vendor", vendors);
                return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[0], suppzMap);
            }
        } else {
            String msg = "Missing Params.";
            return new ResponseEntity<Response>(this.VResponse(this.title, Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping({"edit-vendor/{id}"})
    public ResponseEntity<Response> editVendor(@PathVariable UUID id, @RequestBody Vendors vendors) {
        Optional<Vendors> optionalVendors = vendorsService.findById(vendors.getId());
        if (!optionalVendors.isPresent()) {
            String msg = "No Vendor found By ID Provided.";
            return new ResponseEntity<Response>(this.VResponse(this.title, Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        } else {
            Vendors savedVendor = optionalVendors.get();
            vendors.setId(savedVendor.getId());
            vendors = vendorsService.update(vendors);

            HashMap hashMap = new HashMap();
            hashMap.put("vendor", vendors);
            return new ResponseEntity<Response>(this.VResponse(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[0], hashMap), HttpStatus.OK);
        }
    }

    @GetMapping
    public ResponseEntity<Response> getAllVendors() {
        List<Vendors> vendorList = vendorsService.getAllVendors();

        HashMap suppzMap = new HashMap();
        suppzMap.put("vendors", vendorList);
        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[3], suppzMap);
    }

    @GetMapping(path = {"/{id}"})
    public ResponseEntity<Response> getVendorById(@PathVariable UUID id) {
        Optional<Vendors> vendorOptional = vendorsService.findById(id);
        if (!vendorOptional.isPresent()) {
            String msg = "Vendor doesn't exists By ID Provided.";
            return new ResponseEntity<Response>(this.VResponse(this.title, Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        } else {
            HashMap suppzMap = new HashMap();
            suppzMap.put("vendor", vendorOptional.get());
            return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[0], suppzMap);
        }
    }

    @DeleteMapping(path = {"/delete/{id}"})
    public ResponseEntity<Response> deleteVendorById(@PathVariable UUID id) {
        Optional<Vendors> vendorOptional = vendorsService.findById(id);
        if (!vendorOptional.isPresent()) {
            String msg = "Vendor doesn't exists By ID Provided.";
            return new ResponseEntity<Response>(this.VResponse(this.title, Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        } else {
            vendorsService.deleteById(id);
            return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[4], new HashMap());
        }
    }

    private ResponseEntity<Response> getResponseEntity(String title, String status, Integer success, String msg, HashMap map) {
        return new ResponseEntity<Response>(this.VResponse(title, status, success, msg, map), HttpStatus.OK);
    }

    private Response VResponse(String title, String status, Integer success, String msg, HashMap map) {
        Response response = new Response();
        response.setTitle(title);
        response.setStatus(status);
        response.setMessage(msg);
        response.setSuccess(success);
        response.setData(map);
        return response;
    }
}
