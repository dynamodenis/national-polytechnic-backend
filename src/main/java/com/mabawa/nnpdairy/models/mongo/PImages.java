package com.mabawa.nnpdairy.models.mongo;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "pimages")
public class PImages {
//    @Id
//    private String id;
    @Id
    private String id;

    private String pName;

    private Binary image;

    private String imageDownload;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getpName() {
        return pName;
    }
    public void setpName(String pName) {
        this.pName = pName;
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
