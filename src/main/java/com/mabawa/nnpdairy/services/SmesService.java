package com.mabawa.nnpdairy.services;

import com.mabawa.nnpdairy.models.Smes;
import com.mabawa.nnpdairy.models.Vendors;
import com.mabawa.nnpdairy.repository.SmesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SmesService {
    @Autowired
    private SmesRepository smesRepository;

    public List<Smes> getAllSmes(){
        return  smesRepository.findAll();
    }

    public Optional<Smes> findById(UUID id){
        return  smesRepository.findById(id);
    }

    public Optional<Smes> getSmeByName(String name){
        return smesRepository.getSmesByName(name);
    }

    public List<Smes> getNameContaining(String nme){
        return smesRepository.findByNameContaining(nme);
    }

    public Smes create(Smes smes){
        return  smesRepository.saveAndFlush(smes);
    }

    public Smes update(Smes smes){
        return smesRepository.saveAndFlush(smes);
    }

    public void deleteById(UUID id){
        smesRepository.deleteSmesById(id);
    }

    public void deleteAllSMes(){
        smesRepository.deleteAllSmes();
    }
}
