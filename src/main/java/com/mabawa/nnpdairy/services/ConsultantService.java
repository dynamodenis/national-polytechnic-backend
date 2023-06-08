package com.mabawa.nnpdairy.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.mabawa.nnpdairy.models.Consultants;
import com.mabawa.nnpdairy.models.mongo.ConsultantsProfile;
import com.mabawa.nnpdairy.repository.ConsultantsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ConsultantService {
    @Autowired
    private ConsultantsRepository consultantsRepository;

    @Autowired
    private Gson gson;

    public List<Consultants> getAllConsultants(){
        return  consultantsRepository.findAll();
    }

    public Optional<Consultants> findById(UUID id){
        return  consultantsRepository.findById(id);
    }

    public Optional<Consultants> getConsultantByName(String name){
        return consultantsRepository.getConsultantByName(name);
    }

    public List<Consultants> getConsultantByUserId(UUID userid)
    {
        return consultantsRepository.findConsultantsByUserid(userid);
    }

    public List<Consultants> getNameContaining(String nme){
        return consultantsRepository.findByNameContaining(nme);
    }

    public Consultants create(Consultants consultants){
        return  consultantsRepository.saveAndFlush(consultants);
    }

    public Consultants update(Consultants consultants){
        return consultantsRepository.save(consultants);
    }

    public void deleteById(UUID id){
        consultantsRepository.deleteConsultantsById(id);
    }

    public void deleteAllConsultants(){
        consultantsRepository.deleteAllConsultants();
    }

    public Consultants getJson(String consultant){
        Consultants consultants = gson.fromJson(consultant, Consultants.class);

        return consultants;
    }
}
