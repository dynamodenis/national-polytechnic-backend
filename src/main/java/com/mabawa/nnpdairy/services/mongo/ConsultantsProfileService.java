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
        List<ConsultantsProfile> consultantsProfileList = consultantsProfileRepository.getConsultantsProfileById(conId);
        consultantsProfileList.forEach(consultantsProfile -> {
            if (consultantsProfile.getImage() != null) {
                consultantsProfile.setImageDownload(Base64.getEncoder().encodeToString(imageService.decompressBytes(consultantsProfile.getImage().getData())));
            }
        });
        return consultantsProfileList;
    }

    public String getConsultantProfImage(String conId){
        List<ConsultantsProfile> consultantsProfileList = consultantsProfileRepository.getConsultantsProfileById(conId);
        String[] imageDonwloads = {""};
        consultantsProfileList.forEach(consultantsProfile -> {
            if (consultantsProfile.getImage() != null) {
                imageDonwloads[0] = Base64.getEncoder().encodeToString(imageService.decompressBytes(consultantsProfile.getImage().getData()));
            }
        });
        return imageDonwloads[0];
    }

    public String addProfile(ConsultantsProfile consultantsProfile){
        return  consultantsProfileRepository.save(consultantsProfile).getId();
    }

    public void deleteConsultantProfile(String conId){
        consultantsProfileRepository.deleteConsultantsProfileById(conId);
    }

    public void deleteAllConsultantProfile(){
        consultantsProfileRepository.deleteAll();
    }
}
