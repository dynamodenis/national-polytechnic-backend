package com.mabawa.nnpdairy.services.mongo;

import com.mabawa.nnpdairy.models.mongo.TMaterials;
import com.mabawa.nnpdairy.models.mongo.TMaterialsData;
import com.mabawa.nnpdairy.repository.mongo.TMaterialRepository;
import com.mabawa.nnpdairy.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class TMaterialService {
    @Autowired
    private TMaterialRepository tMaterialRepository;

    @Autowired
    private ImageService imageService;

    public TMaterials getTMaterialById(String id){
        return tMaterialRepository.findById(id).get();
    }

    public String addTMaterial(TMaterials tMaterials){
        return  tMaterialRepository.insert(tMaterials).getTrainingsId();
    }

    public List<TMaterials> getTrainingTMaterials(String trainingId){
        List<TMaterials> tMaterialsList = tMaterialRepository.getTMaterialsByTrainingsId(trainingId);
        tMaterialsList.forEach(tMaterials -> {
            List<TMaterialsData> tMaterialsDataList = tMaterials.gettMaterialsData();
            tMaterialsDataList.forEach(tMaterialsData -> {
                if (tMaterialsData.getType() == 1){
                    tMaterialsData.setContentDownload(Base64.getEncoder().encodeToString(imageService.decompressBytes(tMaterialsData.getContent().getData())));
                }
            });
        });
        return  tMaterialsList;
    }

    public List<String> getTrainingTMaterialsImages(String trainingId){
        List<TMaterials> tMaterialsList = tMaterialRepository.getTMaterialsByTrainingsId(trainingId);
        List<String> imageDownloads = new ArrayList<>();
        tMaterialsList.forEach(tMaterials -> {
            List<TMaterialsData> tMaterialsDataList = tMaterials.gettMaterialsData();
            tMaterialsDataList.forEach(tMaterialsData -> {
                if (tMaterialsData.getType() == 1){
                    imageDownloads.add(Base64.getEncoder().encodeToString(imageService.decompressBytes(tMaterialsData.getContent().getData())));
                }
            });
        });
        return  imageDownloads;
    }

    public void deleteTraining(String tId){
        tMaterialRepository.deleteTMaterialsByTrainingsId(tId);
    }
}
