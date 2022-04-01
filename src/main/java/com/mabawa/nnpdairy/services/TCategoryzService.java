package com.mabawa.nnpdairy.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mabawa.nnpdairy.models.TCategoryz;
import com.mabawa.nnpdairy.models.Trainings;
import com.mabawa.nnpdairy.models.mongo.TCategoryResources;
import com.mabawa.nnpdairy.repository.TCategoryzRepository;
import com.mabawa.nnpdairy.services.mongo.TCategoryzResService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TCategoryzService {
    @Autowired
    private TCategoryzRepository tCategoryzRepository;

    @Autowired
    private TCategoryzResService tCategoryzResService;

    @Autowired
    private Gson gson;

    public List<TCategoryz> getAllTList(){
        List<TCategoryz> tCategoryzList = tCategoryzRepository.getAllCategory();
        tCategoryzList.forEach(tCategoryz -> {
            tCategoryz.setImageDownloads(tCategoryzResService.getTcategoryResourceString(tCategoryz.getId().toString()));
        });
        return tCategoryzList;
    }

    public List<TCategoryz> getAllTImagesList(){
        List<TCategoryz> tCategoryzList = tCategoryzRepository.getAllCategory();

        tCategoryzList.forEach(tCategoryz -> {
            String imagesList = tCategoryzResService.getTcategoryImages(tCategoryz.getId().toString());
            tCategoryz.setImageDownloads(imagesList);
        });
        return tCategoryzList;
    }

    public Optional<TCategoryz> getTcategoryById(UUID id){
        return tCategoryzRepository.getCategoryById(id);
    }

    public List<TCategoryz> filterTCategoryz(String name, Pageable pageable){
        List<TCategoryz> tCategoryzList = tCategoryzRepository.getCategoryByNameLike(name, pageable);
        tCategoryzList.forEach(tCategoryz -> {
            tCategoryz.setImageDownloads(tCategoryzResService.getTcategoryResourceString(tCategoryz.getId().toString()));
        });
        return tCategoryzList;
    }

    public Optional<TCategoryz> getTcategoryByName(String name){
        return tCategoryzRepository.getCategoryByName(name);
    }

    public TCategoryz create(TCategoryz tCategoryz){
        return tCategoryzRepository.saveAndFlush(tCategoryz);
    }

    public TCategoryz updateTcategory(TCategoryz tCategoryz){
        return tCategoryzRepository.save(tCategoryz);
    }

    public void deleteTcategory(UUID id){
        tCategoryzResService.deleteTcategoryResources(id.toString());
        tCategoryzRepository.deleteCategoryById(id);
    }

    public void deleteAllTCategory(){
        tCategoryzRepository.deleteAllCategory();
    }

    public List<TCategoryz> getNameContaining(String nme){
        return tCategoryzRepository.findByNameContainingIgnoreCase(nme);
    }

    public TCategoryz getJson(String tcategory){
        TCategoryz tCategoryz = gson.fromJson(tcategory, TCategoryz.class);

        return tCategoryz;
    }
}
