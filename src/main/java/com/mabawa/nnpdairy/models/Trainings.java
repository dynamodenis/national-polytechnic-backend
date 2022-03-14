package com.mabawa.nnpdairy.models;

import com.mabawa.nnpdairy.models.mongo.TMaterials;
import com.mabawa.nnpdairy.utils.JsonTypeB;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "trainings")
@TypeDef(name = "JsonTypeB", typeClass = JsonTypeB.class)
public class Trainings {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;
    private UUID category;
    @Type(type = "JsonTypeB")
    private Trainers trainers;
    private Integer duration;
    private String description;
    private String notes;
    private String topic;
    private Integer sel;
    private Timestamp created;

    @Transient
    private List<TMaterials> tMaterials;

    @Transient
    private List<String> imagesDownload;

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCategory() {
        return category;
    }
    public void setCategory(UUID category) {
        this.category = category;
    }

    public Trainers getTrainers() {
        return trainers;
    }
    public void setTrainers(Trainers trainers) {
        this.trainers = trainers;
    }

    public Integer getDuration() {
        return duration;
    }
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getTopic() {
        return topic;
    }
    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Integer getSel() {
        return sel;
    }
    public void setSel(Integer sel) {
        this.sel = sel;
    }

    public Timestamp getCreated() {
        return created;
    }
    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public List<TMaterials> gettMaterials() {
        return tMaterials;
    }
    public void settMaterials(List<TMaterials> tMaterials) {
        this.tMaterials = tMaterials;
    }

    public List<String> getImagesDownload() {
        return imagesDownload;
    }
    public void setImagesDownload(List<String> imagesDownload) {
        this.imagesDownload = imagesDownload;
    }
}
