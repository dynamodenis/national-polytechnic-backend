package com.mabawa.nnpdairy.models.mongo;

import org.bson.types.Binary;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Document(collection = "s_images")
public class StockUpload {
    @Id
    private String id;
    private String itemCode;
    private String itemName;
    private Binary image;
    private String imageStr;
    private String description;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getItemCode() {
        return itemCode;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Binary getImage() {
        return image;
    }
    public void setImage(Binary image) {
        this.image = image;
    }

    public String getImageStr() {
        return imageStr;
    }
    public void setImageStr(String imageStr) {
        this.imageStr = imageStr;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
