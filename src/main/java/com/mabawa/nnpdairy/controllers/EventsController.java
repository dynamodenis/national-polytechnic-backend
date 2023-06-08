package com.mabawa.nnpdairy.controllers;

import com.mabawa.nnpdairy.constants.Constants;
import com.mabawa.nnpdairy.models.Events;
import com.mabawa.nnpdairy.models.Response;
import com.mabawa.nnpdairy.services.EventsService;
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
@RequestMapping({"api/v1/events"})
public class EventsController {
    @Autowired
    private EventsService eventsService;

    String title = Constants.TITLES[0];

    @PostMapping
    public ResponseEntity<Response> addNewEvent(@RequestBody Events events)
    {
        if (events.getTopic().isEmpty()){
            String msg = "Event Topic not provided.";
            return new ResponseEntity<Response>(this.EResponse(this.title, Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        }

        if (events.getAppuser().toString().isEmpty()){
            String msg = "Provide User associated with event";
            return new ResponseEntity<Response>(this.EResponse(this.title, Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        }

        if (events.getEtime().before(events.getStime()))
        {
            String msg = "Invalid Date/Time provided.";
            return new ResponseEntity<Response>(this.EResponse(this.title, Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        }

        Optional<Events> eventsOptional = eventsService.findByTopic(events.getTopic());
        if (eventsOptional.isPresent())
        {
            String msg = "An Event Exists by topic provided.";
            return new ResponseEntity<Response>(this.EResponse(this.title, Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        }

        LocalDateTime lastLocal = LocalDateTime.now();
        events.setCreated(Timestamp.valueOf(lastLocal));

        events = eventsService.create(events);

        HashMap eventMap = new HashMap();
        eventMap.put("event", events);
        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[0], eventMap);
    }

    @PutMapping(path = {"edit-events/{id}"})
    public ResponseEntity<Response> editEvents(@PathVariable UUID id, @RequestBody Events events)
    {
        Events savedEvent = eventsService.findEventsById(id)
                .orElseThrow(()-> new EntityNotFoundException("No Event found By ID Provided."));

        if (events.getEtime().before(events.getStime()))
        {
            String msg = "Invalid Date/Time provided.";
            return new ResponseEntity<Response>(this.EResponse(this.title, Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        }

        events.setId(savedEvent.getId());
        events.setCreated(savedEvent.getCreated());

        events = eventsService.update(events);

        HashMap eventMap = new HashMap();
        eventMap.put("event", events);

        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[0], eventMap);
    }

    @PutMapping(path = {"edit-event-status/{id}/{status}"})
    public ResponseEntity<Response> editEventsStatus(@PathVariable UUID id, @PathVariable Integer status)
    {
        Events savedEvent = eventsService.findEventsById(id)
                .orElseThrow(()-> new EntityNotFoundException("No Event found By ID Provided."));

        eventsService.updateEventStatus(status, id);

        HashMap eventMap = new HashMap();
        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[0], eventMap);
    }

    @GetMapping
    public ResponseEntity<Response> getAllEvents()
    {
        List<Events> eventsList = eventsService.getAllEvents();

        HashMap eventMap = new HashMap();
        eventMap.put("events", eventsList);
        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[3], eventMap);
    }

    @GetMapping(path = {"/{id}"})
    public ResponseEntity<Response> getEventById(@PathVariable UUID id)
    {
        Events savedEvent = eventsService.findEventsById(id)
                .orElseThrow(()-> new EntityNotFoundException("No Event found By ID Provided."));

        HashMap hashMap = new HashMap();
        hashMap.put("event", savedEvent);
        return new ResponseEntity<Response>(this.EResponse(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[3], hashMap), HttpStatus.OK);
    }

    @GetMapping(path = {"/filter-by-topic/{topic}"})
    public ResponseEntity<Response> getEventByTopic(@PathVariable String topic)
    {
        List<Events> eventsList = eventsService.findTopicContaining(topic);

        HashMap hashMap = new HashMap();
        hashMap.put("event", eventsList);
        return new ResponseEntity<Response>(this.EResponse(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[3], hashMap), HttpStatus.OK);
    }

    @GetMapping(path = {"/filterbytime/{stime}/{etime}"})
    public ResponseEntity<Response> getEventByTime(@PathVariable Timestamp stime, @PathVariable Timestamp etime)
    {
        List<Events> eventsList = eventsService.filterEventsByTime(stime, etime);

        HashMap hashMap = new HashMap();
        hashMap.put("event", eventsList);
        return new ResponseEntity<Response>(this.EResponse(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[3], hashMap), HttpStatus.OK);
    }

    @GetMapping(path = {"/filter-by-status/{status}"})
    public ResponseEntity<Response> getEventByStatus(@PathVariable Integer status)
    {
        List<Events> eventsList = eventsService.filterEventsByStatus(status);

        HashMap hashMap = new HashMap();
        hashMap.put("event", eventsList);
        return new ResponseEntity<Response>(this.EResponse(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[3], hashMap), HttpStatus.OK);
    }

    @DeleteMapping(path = {"/{id}"})
    public ResponseEntity<Response> deleteEventById(@PathVariable UUID id)
    {
        Events savedEvent = eventsService.findEventsById(id)
                .orElseThrow(()-> new EntityNotFoundException("No Event found By ID Provided."));

        eventsService.deleteEventById(id);

        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[4], new HashMap());
    }

    @DeleteMapping(path = {"/deleteAllEvents"})
    public ResponseEntity<Response> deleteAllEvents()
    {
        eventsService.deleteAllEvent();

        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[4], new HashMap());
    }

    private ResponseEntity<Response> getResponseEntity(String title, String status, Integer success, String msg, HashMap map) {
        return new ResponseEntity<Response>(this.EResponse(title, status, success, msg, map), HttpStatus.OK);
    }

    private Response EResponse(String title, String status, Integer success, String msg, HashMap map) {
        Response response = new Response();
        response.setTitle(title);
        response.setStatus(status);
        response.setMessage(msg);
        response.setSuccess(success);
        response.setData(map);
        return response;
    }
}
