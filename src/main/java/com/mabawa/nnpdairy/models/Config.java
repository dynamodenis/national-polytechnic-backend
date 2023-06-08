package com.mabawa.nnpdairy.models;

import javax.persistence.*;

@Entity
@Table(name = "config")
public class Config {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String compname;
    private String mail;
    private String licence;
    private Integer agepsw;
    private String support;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getCompname() {
        return compname;
    }
    public void setCompname(String compname) {
        this.compname = compname;
    }

    public String getMail() {
        return mail;
    }
    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getLicence() {
        return licence;
    }
    public void setLicence(String licence) {
        this.licence = licence;
    }

    public Integer getAgepsw() {
        return agepsw;
    }
    public void setAgepsw(Integer agepsw) {
        this.agepsw = agepsw;
    }

    public String getSupport() {
        return support;
    }
    public void setSupport(String support) {
        this.support = support;
    }
}
