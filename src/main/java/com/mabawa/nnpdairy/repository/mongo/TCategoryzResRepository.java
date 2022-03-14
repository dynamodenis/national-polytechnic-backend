package com.mabawa.nnpdairy.repository.mongo;

import com.mabawa.nnpdairy.models.mongo.RMaterials;
import com.mabawa.nnpdairy.models.mongo.TCategoryResources;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TCategoryzResRepository extends MongoRepository<TCategoryResources, String> {
    public List<TCategoryResources> getTCategoryResourcesByTcategoryzId(String tcategoryzId);

    public void deleteTCategoryResourcesByTcategoryzId(String tcategoryzId);
}
