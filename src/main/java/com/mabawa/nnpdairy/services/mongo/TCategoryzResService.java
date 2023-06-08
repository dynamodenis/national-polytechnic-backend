package com.mabawa.nnpdairy.services.mongo;

import com.google.gson.Gson;
import com.mabawa.nnpdairy.models.mongo.RMaterials;
import com.mabawa.nnpdairy.models.mongo.RResources;
import com.mabawa.nnpdairy.models.mongo.TCategoryResources;
import com.mabawa.nnpdairy.repository.mongo.TCategoryzResRepository;
import com.mabawa.nnpdairy.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class TCategoryzResService {
    @Autowired
    private TCategoryzResRepository tCategoryzResRepository;

    @Autowired
    private ImageService imageService;

    public List<TCategoryResources> getTcategoryResources(String tcategoryResId){
        List<TCategoryResources> tCategoryResourcesList = tCategoryzResRepository.getTCategoryResourcesById(tcategoryResId);
        tCategoryResourcesList.forEach(tCategoryResources -> {
            if (tCategoryResources.getImage() != null){
                tCategoryResources.setImageDownload(Base64.getEncoder().encodeToString(imageService.decompressBytes(tCategoryResources.getImage().getData())));
            }
        });
        return tCategoryResourcesList;
    }

    public Optional<TCategoryResources> getTcategoryResource(String tcategoryResId){
        Optional<TCategoryResources> optionalTCategoryResources = tCategoryzResRepository.findTCategoryResourcesById(tcategoryResId);


        return optionalTCategoryResources;
    }

    public String getTcategoryResourceString(String tcategoryResId){
        String imgStr = "";
        Optional<TCategoryResources> optionalTCategoryResources = tCategoryzResRepository.findTCategoryResourcesById(tcategoryResId);
        if (optionalTCategoryResources.isPresent())
        {
            imgStr = Base64.getEncoder().encodeToString(imageService.decompressBytes(optionalTCategoryResources.get().getImage().getData()));
        }

        return imgStr;
    }

    public String getTcategoryImages(String tcategoryResId){
        String[] imagesList = {""};
        List<TCategoryResources> tCategoryResourcesList = tCategoryzResRepository.getTCategoryResourcesById(tcategoryResId);
        tCategoryResourcesList.forEach(tCategoryResources -> {
            if (tCategoryResources.getImage() != null){
                imagesList[0] = Base64.getEncoder().encodeToString(imageService.decompressBytes(tCategoryResources.getImage().getData()));
            }
        });
        return imagesList[0];
    }

    public String addTcategoryResources(TCategoryResources tCategoryResources){
        return tCategoryzResRepository.save(tCategoryResources).getId();
    }

    public  void deleteTcategoryResources(String tcategoryResId){
        tCategoryzResRepository.deleteTCategoryResourcesById(tcategoryResId);
    }

    public  void deleteAllTcategoryResources(){
        tCategoryzResRepository.deleteAll();
    }
}
