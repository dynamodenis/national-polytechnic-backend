package com.mabawa.nnpdairy.services;

import com.google.gson.Gson;
import com.mabawa.nnpdairy.models.Consultants;
import com.mabawa.nnpdairy.models.Research;
import com.mabawa.nnpdairy.repository.ResearchRepository;
import com.mabawa.nnpdairy.services.mongo.RResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ResearchService {
    @Autowired
    private ResearchRepository researchRepository;

    @Autowired
    private RResourceService rResourceService;

    @Autowired
    private Gson gson;

    public Optional<Research> getResearchByDescription(String res){
        return researchRepository.getResearchByDescription(res);
    }

    public Optional<Research> findById(UUID id){
        return researchRepository.findById(id);
    }

    public List<Research> getAllResearch(){
        List<Research> researchList = researchRepository.findAll();
        researchList.forEach(research -> {
            research.setrMaterials(rResourceService.getResearchResources(research.getId().toString()));
        });
        return researchList;
    }

    public Research create(Research research){
        return researchRepository.saveAndFlush(research);
    }

    public Research update(Research research){
        return  researchRepository.save(research);
    }

    public void deleteResearchById(UUID rId){
        researchRepository.deleteResearchById(rId);
    }

    public void deleteAllResearch(){
        researchRepository.deleteAllResearch();
    }

    public Research getJson(String res){
        Research research = gson.fromJson(res, Research.class);

        return research;
    }
}
