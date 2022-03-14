package com.mabawa.nnpdairy.models.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "rmaterials")
public class RMaterials {
    @Id
    private String id;

    private String researchId;

    private String title;

    private String url;

    private List<RResources> rResources;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getResearchId() {
        return researchId;
    }
    public void setResearchId(String researchId) {
        this.researchId = researchId;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public List<RResources> getrResources() {
        return rResources;
    }
    public void setrResources(List<RResources> rResources) {
        this.rResources = rResources;
    }
}
