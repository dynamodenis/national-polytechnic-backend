package com.mabawa.nnpdairy.services.mongo;

import com.mabawa.nnpdairy.models.mongo.StockUpload;
import com.mabawa.nnpdairy.repository.mongo.UploadRepository;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UploadService {
    @Autowired
    private UploadRepository uploadRepository;

    public String create(StockUpload stockUpload)
    {
        return uploadRepository.save(stockUpload).getId();
    }
}