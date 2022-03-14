package com.mabawa.nnpdairy.services.mongo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mabawa.nnpdairy.models.mongo.ConsultantsProfile;
import com.mabawa.nnpdairy.repository.ConsultantsRepository;
import com.mabawa.nnpdairy.repository.mongo.ConsultantsProfileRepository;
import com.mabawa.nnpdairy.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class ConsultantsProfileService {
    @Autowired
    private ConsultantsProfileRepository consultantsProfileRepository;

    @Autowired
    private ImageService imageService;

    public ConsultantsProfile getProfile(String id){
        return consultantsProfileRepository.findById(id).get();
    }

    public List<ConsultantsProfile> getConsultantProf(String conId){
        List<ConsultantsProfile> consultantsProfileList = consultantsProfileRepository.getConsultantsProfileByConsultantId(conId);
        consultantsProfileList.forEach(consultantsProfile -> {
            if (consultantsProfile.getImage() != null) {
                consultantsProfile.setImageDownload(Base64.getEncoder().encodeToString(imageService.decompressBytes(consultantsProfile.getImage().getData())));
            }
        });
        return consultantsProfileList;
    }

    public List<String> getConsultantProfImage(String conId){
        List<ConsultantsProfile> consultantsProfileList = consultantsProfileRepository.getConsultantsProfileByConsultantId(conId);
        List<String> imageDonwloads = new ArrayList<>();
        consultantsProfileList.forEach(consultantsProfile -> {
            if (consultantsProfile.getImage() != null) {
                imageDonwloads.add(Base64.getEncoder().encodeToString(imageService.decompressBytes(consultantsProfile.getImage().getData())));
            }
        });
        return imageDonwloads;
    }

    public String addProfile(ConsultantsProfile consultantsProfile){
        return  consultantsProfileRepository.insert(consultantsProfile).getConsultantId();
    }

    public void deleteConsultantProfile(String conId){
        consultantsProfileRepository.deleteConsultantsProfileByConsultantId(conId);
    }
}
