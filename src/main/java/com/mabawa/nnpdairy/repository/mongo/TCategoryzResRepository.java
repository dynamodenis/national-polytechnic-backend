package com.mabawa.nnpdairy.repository.mongo;

import com.mabawa.nnpdairy.models.mongo.RMaterials;
import com.mabawa.nnpdairy.models.mongo.TCategoryResources;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TCategoryzResRepository extends MongoRepository<TCategoryResources, String> {
    public List<TCategoryResources> getTCategoryResourcesById(String id);

    public void deleteTCategoryResourcesById(String id);

    public Optional<TCategoryResources> findTCategoryResourcesById(String id);
}
