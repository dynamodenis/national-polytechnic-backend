package com.mabawa.nnpdairy.services;

import com.mabawa.nnpdairy.models.PCategoryz;
import com.mabawa.nnpdairy.models.TCategoryz;
import com.mabawa.nnpdairy.repository.PCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PCategoryzService {
    @Autowired
    private PCategoryRepository pCategoryRepository;

    public List<PCategoryz> getAllTList(){
        return pCategoryRepository.getAllCategory();
    }

    public Optional<PCategoryz> getTcategoryById(UUID id){
        return pCategoryRepository.getCategoryById(id);
    }

    public List<PCategoryz> filterTCategoryz(String name, Pageable pageable){
        return  pCategoryRepository.getCategoryByNameLike(name, pageable);
    }

    public Optional<PCategoryz> getTcategoryByName(String name){
        return pCategoryRepository.getCategoryByName(name);
    }

    public PCategoryz create(PCategoryz pCategoryz){
        return pCategoryRepository.saveAndFlush(pCategoryz);
    }

    public PCategoryz update(PCategoryz pCategoryz){
        return pCategoryRepository.save(pCategoryz);
    }

    public void updateTcategory(String nme, UUID id, Integer typ){
        pCategoryRepository.updateCategory(nme, typ, id);
    }

    public void deleteTcategory(UUID id){
        pCategoryRepository.deleteCategoryById(id);
    }

    public void deleteAllTCategory(){
        pCategoryRepository.deleteAllCategory();
    }

    public List<PCategoryz> getNameContaining(String nme){
        return pCategoryRepository.findByNameContaining(nme);
    }
}
