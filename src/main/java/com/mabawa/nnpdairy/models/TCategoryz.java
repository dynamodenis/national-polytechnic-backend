package com.mabawa.nnpdairy.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mabawa.nnpdairy.models.mongo.TCategoryResources;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tcategoryz")
public class TCategoryz {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;
    private String name;
    private String descr;
    private String eligible;
    private Integer sel;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Africa/Nairobi")
    private Timestamp init_dte;

    @Transient
    List<TCategoryResources> tCategoryResourcesList;

    @Transient
    private String imageDownloads;

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescr() {
        return descr;
    }
    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getEligible() {
        return eligible;
    }
    public void setEligible(String eligible) {
        this.eligible = eligible;
    }

    public Integer getSel() {
        return sel;
    }
    public void setSel(Integer sel) {
        this.sel = sel;
    }

    public Timestamp getInit_dte() {
        return init_dte;
    }
    public void setInit_dte(Timestamp init_dte) {
        this.init_dte = init_dte;
    }

    public List<TCategoryResources> gettCategoryResourcesList() {
        return tCategoryResourcesList;
    }
    public void settCategoryResourcesList(List<TCategoryResources> tCategoryResourcesList) {
        this.tCategoryResourcesList = tCategoryResourcesList;
    }

    public String getImageDownloads() {
        return imageDownloads;
    }
    public void setImageDownloads(String imageDownloads) {
        this.imageDownloads = imageDownloads;
    }
}
