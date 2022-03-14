package com.mabawa.nnpdairy.models.mongo;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tcategoryresources")
public class TCategoryResources {
    @Id
    private String id;

    private String tcategoryzId;

    private Binary image;

    private String imageTitle;

    private String imageDownload;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getTcategoryzId() {
        return tcategoryzId;
    }
    public void setTcategoryzId(String tcategoryzId) {
        this.tcategoryzId = tcategoryzId;
    }

    public Binary getImage() {
        return image;
    }
    public void setImage(Binary image) {
        this.image = image;
    }

    public String getImageTitle() {
        return imageTitle;
    }
    public void setImageTitle(String imageTitle) {
        this.imageTitle = imageTitle;
    }

    public String getImageDownload() {
        return imageDownload;
    }
    public void setImageDownload(String imageDownload) {
        this.imageDownload = imageDownload;
    }
}
