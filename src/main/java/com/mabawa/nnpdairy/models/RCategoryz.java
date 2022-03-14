package com.mabawa.nnpdairy.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "rcategoryz")
public class RCategoryz {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;
    private String name;
    private Integer sel;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Africa/Nairobi")
    private Timestamp init_dte;

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
}
