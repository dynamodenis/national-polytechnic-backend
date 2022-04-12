package com.mabawa.nnpdairy.repository.mongo;

import com.mabawa.nnpdairy.models.mongo.TMaterials;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TMaterialRepository extends MongoRepository<TMaterials, String> {
    @Query("{ 'trainingsId' : ?0 }")
    Optional<TMaterials> getTMaterialsByTrainingsId(String trainingsId);

    public void deleteTMaterialsByTrainingsId(String trainingsId);
}
