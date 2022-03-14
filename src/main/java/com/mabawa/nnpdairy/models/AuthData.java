package com.mabawa.nnpdairy.models;

import java.util.UUID;

public class AuthData {
    private UUID id;
    private String name;
    private String phone;
    private String email;
    private String password;
    private int prm;

    public AuthData() {
    }

    public AuthData(UUID id, String name, String password, String fone, String mail, int prm) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.phone = fone;
        this.email = mail;
        this.prm = prm;
    }

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

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public int getPrm() {
        return prm;
    }
    public void setPrm(int prm) {
        this.prm = prm;
    }
}
