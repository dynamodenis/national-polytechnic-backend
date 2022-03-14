package com.mabawa.nnpdairy.services.mongo;

import com.mabawa.nnpdairy.models.mongo.RMaterials;
import com.mabawa.nnpdairy.models.mongo.RResources;
import com.mabawa.nnpdairy.repository.mongo.RResourcesRepository;
import com.mabawa.nnpdairy.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;

@Service
public class RResourceService {
    @Autowired
    private RResourcesRepository rResourcesRepository;

    @Autowired
    private ImageService imageService;

    public List<RMaterials> getResearchResources(String researchId){
        List<RMaterials> rMaterialsList = rResourcesRepository.getRMaterialsByResearchId(researchId);
        rMaterialsList.forEach(rMaterials -> {
            List<RResources> rResourcesList = rMaterials.getrResources();
            rResourcesList.forEach(rResources -> {
                if (rResources.getImage() != null){
                    rResources.setImageDownload(Base64.getEncoder().encodeToString(imageService.decompressBytes(rResources.getImage().getData())));
                }
            });
        });
        return rMaterialsList;
    }

    public String addResources(RMaterials rMaterials){
        return rResourcesRepository.insert(rMaterials).getResearchId();
    }

    public  void deleteResources(String researchId){
        rResourcesRepository.deleteRMaterialsByResearchId(researchId);
    }
}