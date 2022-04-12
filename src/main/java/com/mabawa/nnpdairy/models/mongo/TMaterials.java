package com.mabawa.nnpdairy.models.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "tmaterials")
public class TMaterials {
    @Id
    private String id;

    private String trainingsId;

    private String title;

    private String url;

    private List<TMaterialsData> tMaterialsData;

    @Transient
    private List<String> tMImages;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getTrainingsId() {
        return trainingsId;
    }
    public void setTrainingsId(String trainingsId) {
        this.trainingsId = trainingsId;
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

    public List<TMaterialsData> gettMaterialsData() {
        return tMaterialsData;
    }
    public void settMaterialsData(List<TMaterialsData> tMaterialsData) {
        this.tMaterialsData = tMaterialsData;
    }

    public List<String> gettMImages() {
        return tMImages;
    }
    public void settMImages(List<String> tMImages) {
        this.tMImages = tMImages;
    }
}
