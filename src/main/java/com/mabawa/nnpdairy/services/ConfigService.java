package com.mabawa.nnpdairy.services;

import com.mabawa.nnpdairy.models.Config;
import com.mabawa.nnpdairy.repository.ConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfigService {
    @Autowired
    private ConfigRepository configRepository;

    public Config get(){
        return configRepository.getConfigBy().get();
    }

}
