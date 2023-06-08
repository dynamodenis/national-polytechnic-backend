package com.mabawa.nnpdairy.controllers;

import com.mabawa.nnpdairy.constants.Constants;
import com.mabawa.nnpdairy.models.Faq;
import com.mabawa.nnpdairy.models.Response;
import com.mabawa.nnpdairy.services.FaqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping({"api/v1/faq"})
public class FaqController {
    @Autowired
    private FaqService faqService;

    String title = Constants.TITLES[0];

    @PostMapping
    public ResponseEntity<Response> addNewFaq(@RequestBody Faq faq)
    {
        if (faq.getQuestion().isEmpty() || faq.getAnswer().isEmpty())
        {
            String msg = "Missing Params.";
            return new ResponseEntity<Response>(this.FResponse(this.title, Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        }

        LocalDateTime lastLocal = LocalDateTime.now();
        faq.setCreated(Timestamp.valueOf(lastLocal));

        faq = faqService.create(faq);

        HashMap faqMap = new HashMap();
        faqMap.put("faq", faq);
        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[0], faqMap);
    }

    @PutMapping(path = {"edit-faq/{id}"})
    public ResponseEntity<Response> editFaq(@PathVariable UUID id, @RequestBody Faq faq)
    {
        Faq savedFaq = faqService.findFaqById(id)
                .orElseThrow(()-> new EntityNotFoundException("No FAQ found By ID Provided."));

        faq.setId(savedFaq.getId());
        faq.setCreated(savedFaq.getCreated());

        faq = faqService.update(faq);

        HashMap hashMap = new HashMap();
        hashMap.put("faq", faq);
        return new ResponseEntity<Response>(this.FResponse(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[0], hashMap), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Response> getAllFaqs()
    {
        List<Faq> faqList = faqService.getAllFaq();

        HashMap faqMap = new HashMap();
        faqMap.put("faqs", faqList);
        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[3], faqMap);
    }

    @GetMapping(path = {"/{id}"})
    private ResponseEntity<Response> getFaqById(@PathVariable UUID id)
    {
        Faq savedFaq = faqService.findFaqById(id)
                .orElseThrow(()-> new EntityNotFoundException("No FAQ found By ID Provided."));

        HashMap hashMap = new HashMap();
        hashMap.put("faq", savedFaq);
        return new ResponseEntity<Response>(this.FResponse(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[3], hashMap), HttpStatus.OK);
    }

    @DeleteMapping(path = {"/{id}"})
    private ResponseEntity<Response> deleteFaqById(@PathVariable UUID id)
    {
        Faq savedFaq = faqService.findFaqById(id)
                .orElseThrow(()-> new EntityNotFoundException("No FAQ found By ID Provided."));

        faqService.deleteFaqById(id);

        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[4], new HashMap());
    }

    @DeleteMapping(path = {"/deleteAllFaq"})
    private ResponseEntity<Response> deleteAllFaqs()
    {
        faqService.deleteAllFaq();

        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[4], new HashMap());
    }

    private ResponseEntity<Response> getResponseEntity(String title, String status, Integer success, String msg, HashMap map) {
        return new ResponseEntity<Response>(this.FResponse(title, status, success, msg, map), HttpStatus.OK);
    }

    private Response FResponse(String title, String status, Integer success, String msg, HashMap map) {
        Response response = new Response();
        response.setTitle(title);
        response.setStatus(status);
        response.setMessage(msg);
        response.setSuccess(success);
        response.setData(map);
        return response;
    }
}
