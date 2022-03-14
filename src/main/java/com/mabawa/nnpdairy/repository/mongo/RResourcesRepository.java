package com.mabawa.nnpdairy.repository.mongo;

import com.mabawa.nnpdairy.models.mongo.RMaterials;
import com.mabawa.nnpdairy.models.mongo.RResources;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RResourcesRepository extends MongoRepository<RMaterials, String> {
    public List<RMaterials> getRMaterialsByResearchId(String researchId);

    public void deleteRMaterialsByResearchId(String researchId);
}
