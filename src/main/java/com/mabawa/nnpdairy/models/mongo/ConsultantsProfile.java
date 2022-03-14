package com.mabawa.nnpdairy.models.mongo;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "consultantsprofile")
public class ConsultantsProfile {
    @Id
    private String id;

    private String consultantId;

    private String title;

    private Binary image;

    private String imageDownload;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getConsultantId() {
        return consultantId;
    }
    public void setConsultantId(String consultantId) {
        this.consultantId = consultantId;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public Binary getImage() {
        return image;
    }
    public void setImage(Binary image) {
        this.image = image;
    }

    public String getImageDownload() {
        return imageDownload;
    }
    public void setImageDownload(String imageDownload) {
        this.imageDownload = imageDownload;
    }
}