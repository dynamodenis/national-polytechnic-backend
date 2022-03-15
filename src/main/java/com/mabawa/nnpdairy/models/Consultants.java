package com.mabawa.nnpdairy.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mabawa.nnpdairy.models.mongo.ConsultantsProfile;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "consultants")
public class Consultants {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;
    private String name;
    private String email;
    private String phone;
    private UUID userid;
    private String pdescr;
    private String expertise;
    private String projects;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Africa/Nairobi")
    private Timestamp created;

    @Transient
    List<ConsultantsProfile> consultantsProfileList;

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

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UUID getUserid() {
        return userid;
    }
    public void setUserid(UUID userid) {
        this.userid = userid;
    }

    public String getPdescr() {
        return pdescr;
    }
    public void setPdescr(String pdescr) {
        this.pdescr = pdescr;
    }

    public String getExpertise() {
        return expertise;
    }
    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public String getProjects() {
        return projects;
    }
    public void setProjects(String projects) {
        this.projects = projects;
    }

    public Timestamp getCreated() {
        return created;
    }
    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public List<ConsultantsProfile> getConsultantsProfileList() {
        return consultantsProfileList;
    }
    public void setConsultantsProfileList(List<ConsultantsProfile> consultantsProfileList) {
        this.consultantsProfileList = consultantsProfileList;
    }

    public String getImageDownloads() {
        return imageDownloads;
    }
    public void setImageDownloads(String imageDownloads) {
        this.imageDownloads = imageDownloads;
    }
}
