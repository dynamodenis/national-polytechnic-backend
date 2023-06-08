package com.mabawa.nnpdairy.controllers;

import com.mabawa.nnpdairy.constants.Constants;
import com.mabawa.nnpdairy.models.Response;
import com.mabawa.nnpdairy.models.Smes;
import com.mabawa.nnpdairy.models.Vendors;
import com.mabawa.nnpdairy.services.SmesService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping({"api/v1/smes"})
public class SmesController {
    @Autowired
    private SmesService smesService;

    String title = Constants.TITLES[0];

    @PostMapping
    public ResponseEntity<Response> addNewSme(@RequestBody Smes smes) {
        if (!smes.getName().isEmpty()) {
            Optional<Smes> optionalSmes = smesService.getSmeByName(smes.getName());
            if (optionalSmes.isPresent()){
                String msg = "An SME already exists By Name Provided.";
                return new ResponseEntity<Response>(this.SResponse(this.title, Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
            }
            LocalDateTime lastLocal = LocalDateTime.now();
            smes.setCreated(Timestamp.valueOf(lastLocal));
            smes = smesService.create(smes);

            HashMap suppzMap = new HashMap();
            suppzMap.put("sme", smes);
            return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[0], suppzMap);
        } else {
            String msg = "Missing Params.";
            return new ResponseEntity<Response>(this.SResponse(this.title, Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping({"edit-sme/{id}"})
    public ResponseEntity<Response> editSme(@PathVariable UUID id, @RequestBody Smes smes) {
        Smes savedSme = smesService.findById(smes.getId())
                .orElseThrow(()-> new EntityNotFoundException("No SME found By ID Provided."));

        smes.setId(savedSme.getId());
        smes.setCreated(savedSme.getCreated());
        smes = smesService.update(smes);

        HashMap hashMap = new HashMap();
        hashMap.put("sme", smes);
        return new ResponseEntity<Response>(this.SResponse(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[0], hashMap), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Response> getAllSmes() {
        List<Smes> smesList = smesService.getAllSmes();

        HashMap suppzMap = new HashMap();
        suppzMap.put("smes", smesList);
        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[3], suppzMap);
    }

    @GetMapping(path = {"/{id}"})
    public ResponseEntity<Response> getSmeById(@PathVariable UUID id) {
        Smes sme = smesService.findById(id).orElseThrow(()-> new EntityNotFoundException("SME doesn't exists By ID Provided."));

        HashMap suppzMap = new HashMap();
        suppzMap.put("sme", sme);
        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[3], suppzMap);
    }

    @DeleteMapping(path = {"/delete/{id}"})
    public ResponseEntity<Response> deleteSmeById(@PathVariable UUID id) {
        Smes smes = smesService.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("SME doesn't exists By ID Provided."));

        smesService.deleteById(id);
        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[4], new HashMap());
    }

    @DeleteMapping(path = {"/deleteAllSmes"})
    public ResponseEntity<Response> deleteAllSme() {
        smesService.deleteAllSMes();
        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[4], new HashMap());
    }

    private ResponseEntity<Response> getResponseEntity(String title, String status, Integer success, String msg, HashMap map) {
        return new ResponseEntity<Response>(this.SResponse(title, status, success, msg, map), HttpStatus.OK);
    }

    private Response SResponse(String title, String status, Integer success, String msg, HashMap map) {
        Response response = new Response();
        response.setTitle(title);
        response.setStatus(status);
        response.setMessage(msg);
        response.setSuccess(success);
        response.setData(map);
        return response;
    }
}
