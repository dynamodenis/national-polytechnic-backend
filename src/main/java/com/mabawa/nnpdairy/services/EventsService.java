package com.mabawa.nnpdairy.services;

import com.mabawa.nnpdairy.models.Events;
import com.mabawa.nnpdairy.repository.EventsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EventsService {
    @Autowired
    private EventsRepository eventsRepository;

    public List<Events> getAllEvents()
    {
        return eventsRepository.findAll();
    }

    public Optional<Events> findEventsById(UUID id)
    {
        return eventsRepository.findById(id);
    }

    public Optional<Events> findByTopic(String topic)
    {
        return eventsRepository.findEventsByTopic(topic);
    }

    public List<Events> findTopicContaining(String topic)
    {
        return eventsRepository.findEventsByTopicContainingIgnoreCase(topic);
    }

    public List<Events> filterEventsByTime(Timestamp stime, Timestamp etime)
    {
        return eventsRepository.findEventsByStimeAndEtime(stime, etime);
    }

    public List<Events> filterEventsByStatus(Integer status)
    {
        return eventsRepository.findEventsByStatus(status);
    }

    public Events create(Events events)
    {
        return eventsRepository.saveAndFlush(events);
    }

    public Events update(Events events)
    {
        return eventsRepository.saveAndFlush(events);
    }

    public void updateEventStatus(Integer status, UUID id)
    {
        eventsRepository.updateEventsById(status, id);
    }

    public void deleteEventById(UUID id)
    {
        eventsRepository.deleteEventsById(id);
    }

    public void deleteAllEvent()
    {
        eventsRepository.deleteAllEvents();
    }
}
