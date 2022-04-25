package com.mabawa.nnpdairy.services;

import com.mabawa.nnpdairy.models.Vendors;
import com.mabawa.nnpdairy.repository.VendorsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VendorsService {
    @Autowired
    private VendorsRepository vendorsRepository;

    public List<Vendors> getAllVendors(){
        return  vendorsRepository.findAll();
    }

    public Optional<Vendors> findById(UUID id){
        return  vendorsRepository.findById(id);
    }

    public Optional<Vendors> getVendorByName(String name){
        return vendorsRepository.getVendorByName(name);
    }

    public List<Vendors> getNameContaining(String nme){
        return vendorsRepository.findByNameContaining(nme);
    }

    public Vendors create(Vendors vendors){
        return  vendorsRepository.saveAndFlush(vendors);
    }

    public Vendors update(Vendors vendors){
        return vendorsRepository.saveAndFlush(vendors);
    }

    public void deleteById(UUID id){
        vendorsRepository.deleteVendorById(id);
    }

    public void deleteAllVendor(){
        vendorsRepository.deleteAllVendor();
    }
}
