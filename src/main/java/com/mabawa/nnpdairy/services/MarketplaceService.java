package com.mabawa.nnpdairy.services;

import com.mabawa.nnpdairy.models.MarketplaceTypes;
import com.mabawa.nnpdairy.repository.MarketplaceTypesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarketplaceService {
    @Autowired
    private MarketplaceTypesRepository marketplaceTypesRepository;

    public List<MarketplaceTypes> getAllTypes(){
        return marketplaceTypesRepository.findAll();
    }
}
