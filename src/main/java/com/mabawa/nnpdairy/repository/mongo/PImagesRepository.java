package com.mabawa.nnpdairy.repository.mongo;

import com.mabawa.nnpdairy.models.mongo.PImages;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PImagesRepository extends MongoRepository<PImages, String> {
    @Query("{ 'id' : ?0 }")
    Optional<PImages> getProductImageById(String id);

    public void deleteById(String id);
}
