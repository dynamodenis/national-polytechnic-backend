package com.mabawa.nnpdairy.repository.mongo;

import com.mabawa.nnpdairy.models.mongo.PImages;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PImagesRepository extends MongoRepository<PImages, String> {
    @Query("{ 'productId' : ?0 }")
    List<PImages> getProductImageByProductId(String productId);

    public void deleteByProductId(String productId);
}
