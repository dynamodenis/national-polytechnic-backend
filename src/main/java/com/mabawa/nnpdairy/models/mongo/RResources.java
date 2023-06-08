package com.mabawa.nnpdairy.models.mongo;

import org.bson.types.Binary;

import java.io.InputStream;

public class RResources {
    private Binary image;

    private String imageTitle;

    private String imageDownload;

    private InputStream stream;

    private String videoTitle;

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

    public InputStream getStream() {
        return stream;
    }
    public void setStream(InputStream stream) {
        this.stream = stream;
    }

    public String getVideoTitle() {
        return videoTitle;
    }
    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }
}
