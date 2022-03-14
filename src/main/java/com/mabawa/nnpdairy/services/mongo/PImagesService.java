package com.mabawa.nnpdairy.services.mongo;

import com.mabawa.nnpdairy.models.mongo.PImages;
import com.mabawa.nnpdairy.repository.mongo.PImagesRepository;
import com.mabawa.nnpdairy.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class PImagesService {
    @Autowired
    private PImagesRepository pImagesRepository;

    @Autowired
    private ImageService imageService;

    public List<PImages> getProductImage(String pID){
        List<PImages> pImagesList = pImagesRepository.getProductImageByProductId(pID);
        pImagesList.forEach(pImages -> {
            pImages.setImageDownload(Base64.getEncoder().encodeToString(imageService.decompressBytes(pImages.getImage().getData())));
        });

        return pImagesList;
    }

    public List<String> getProductImageList(String pID){
        List<String> imageList = new ArrayList<>();
        List<PImages> pImagesList = pImagesRepository.getProductImageByProductId(pID);
        pImagesList.forEach(pImages -> {
            imageList.add(Base64.getEncoder().encodeToString(imageService.decompressBytes(pImages.getImage().getData())));
        });

        return imageList;
    }

    public String addPImage(PImages pImages){
        return pImagesRepository.insert(pImages).getId();
    }

    public void deleteProductImage(String pId){
        pImagesRepository.deleteByProductId(pId);
    }
}
