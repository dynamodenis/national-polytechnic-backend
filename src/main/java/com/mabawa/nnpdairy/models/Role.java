package com.mabawa.nnpdairy.models;

import com.mabawa.nnpdairy.utils.JsonType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "rolez")
@TypeDef(name = "JsonType", typeClass = JsonType.class)
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;
    private String name;
    @Column(name = "descr")
    private String desc;
    private Integer admin;
    private Integer status;
    private Integer sel;
    @Column(name = "user_rolez")
    @Type(type = "JsonType")
    private User_Rolez rolez;

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

    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getAdmin() {
        return admin;
    }
    public void setAdmin(Integer admin) {
        this.admin = admin;
    }

    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSel() {
        return sel;
    }
    public void setSel(Integer sel) {
        this.sel = sel;
    }

    public User_Rolez getRolez() {
        return rolez;
    }
    public void setRolez(User_Rolez rolez) {
        this.rolez = rolez;
    }
}
