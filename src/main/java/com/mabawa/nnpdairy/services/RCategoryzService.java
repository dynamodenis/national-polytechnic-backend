package com.mabawa.nnpdairy.services;

import com.mabawa.nnpdairy.models.RCategoryz;
import com.mabawa.nnpdairy.models.TCategoryz;
import com.mabawa.nnpdairy.repository.RCategoryzRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RCategoryzService {
    @Autowired
    private RCategoryzRepository rCategoryzRepository;

    public List<RCategoryz> getAllTList(){
        return rCategoryzRepository.getAllCategory();
    }

    public Optional<RCategoryz> getRcategoryById(UUID id){
        return rCategoryzRepository.getCategoryById(id);
    }

    public List<RCategoryz> filterRCategoryz(String name, Pageable pageable){
        return  rCategoryzRepository.getCategoryByNameLike(name, pageable);
    }

    public Optional<RCategoryz> getTcategoryByName(String name){
        return rCategoryzRepository.getCategoryByName(name);
    }

    public RCategoryz create(RCategoryz rCategoryz){
        return rCategoryzRepository.saveAndFlush(rCategoryz);
    }

    public RCategoryz update(RCategoryz rCategoryz){
        return rCategoryzRepository.save(rCategoryz);
    }

    public void updateTcategory(String nme, UUID id){
        rCategoryzRepository.updateCategory(nme, id);
    }

    public void deleteRcategory(UUID id){
        rCategoryzRepository.deleteCategoryById(id);
    }

    public void deleteAllRCategory(){
        rCategoryzRepository.deleteAllCategory();
    }

    public List<RCategoryz> getNameContaining(String nme){
        return rCategoryzRepository.findByNameContaining(nme);
    }

}
