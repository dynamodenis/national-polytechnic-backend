package com.mabawa.nnpdairy.services.mongo;

import com.mabawa.nnpdairy.models.mongo.PImages;
import com.mabawa.nnpdairy.repository.mongo.PImagesRepository;
import com.mabawa.nnpdairy.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class PImagesService {
    @Autowired
    private PImagesRepository pImagesRepository;

    @Autowired
    private ImageService imageService;

    public String getProductImage(String pID){
        String imgStr = "";
        Optional<PImages> pImagesList = pImagesRepository.getProductImageById(pID);
        if (pImagesList.isPresent())
        {
            imgStr = Base64.getEncoder().encodeToString(imageService.decompressBytes(pImagesList.get().getImage().getData()));
        }

        return imgStr;
    }

    public List<PImages> getProductImageList(String pID){
        List<PImages> imageList = new ArrayList<>();
        Optional<PImages> pImagesList = pImagesRepository.getProductImageById(pID);
        if (pImagesList.isPresent())
        {
            imageList.add(pImagesList.get());
        }

        return imageList;
    }

    public Optional<PImages> getProductResource(String id)
    {
        Optional<PImages> optionalPImages = pImagesRepository.findById(id);

        return optionalPImages;
    }

    public String addPImage(PImages pImages){
        return pImagesRepository.save(pImages).getId();
    }

    public void deleteProductImage(String pId){
        pImagesRepository.deleteById(pId);
    }

    public void deleteAllProductImage(){
        pImagesRepository.deleteAll();
    }
}
