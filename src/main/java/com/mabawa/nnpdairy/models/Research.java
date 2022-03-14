package com.mabawa.nnpdairy.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mabawa.nnpdairy.models.mongo.RMaterials;
import com.mabawa.nnpdairy.utils.JsonTypeB;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "research")
@TypeDef(name = "JsonTypeB", typeClass = JsonTypeB.class)
public class Research {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;
    private String description;
    private UUID category;
    @Type(type = "JsonTypeB")
    private Trainers trainers;
    private String topic;
    private Integer sel;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Africa/Nairobi")
    private Timestamp created;

    @Transient
    private List<RMaterials> rMaterials;

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
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

    public List<RMaterials> getrMaterials() {
        return rMaterials;
    }
    public void setrMaterials(List<RMaterials> rMaterials) {
        this.rMaterials = rMaterials;
    }
}
