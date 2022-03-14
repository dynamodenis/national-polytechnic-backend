package com.mabawa.nnpdairy.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "userz")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;
    private String name;
    private Integer sel;
    private Integer admin;
    private String mail;
    private UUID role;
    private Integer status;
    private String password;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Africa/Nairobi")
    private Timestamp created;
    private String phone;
    @Column(name = "isVerified", columnDefinition = "boolean default false", nullable = false)
    private boolean isVerified;
    @Column(name = "otpNumber", columnDefinition = "integer default 0")
    private Integer otpNumber;
    private Long otpExpiration;
    @Column(name = "firstTimeLogin", columnDefinition = "integer default 0")
    private Integer firstTimeLogin;
    private Integer type;
    @Transient
    private List roles;

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

    public Integer getAdmin() {
        return admin;
    }
    public void setAdmin(Integer admin) {
        this.admin = admin;
    }

    public String getMail() {
        return mail;
    }
    public void setMail(String mail) {
        this.mail = mail;
    }

    public UUID getRole() {
        return role;
    }
    public void setRole(UUID role) {
        this.role = role;
    }

    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public Timestamp getCreated() {
        return created;
    }
    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isVerified() {
        return isVerified;
    }
    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public Integer getOtpNumber() {
        return otpNumber;
    }
    public void setOtpNumber(Integer otpNumber) {
        this.otpNumber = otpNumber;
    }

    public Long getOtpExpiration() {
        return otpExpiration;
    }
    public void setOtpExpiration(Long otpExpiration) {
        this.otpExpiration = otpExpiration;
    }

    public Integer getFirstTimeLogin() {
        return firstTimeLogin;
    }
    public void setFirstTimeLogin(Integer firstTimeLogin) {
        this.firstTimeLogin = firstTimeLogin;
    }

    public Integer getType() {
        return type;
    }
    public void setType(Integer type) {
        this.type = type;
    }

    public List getRoles() {
        return roles;
    }
    public void setRoles(List roles) {
        this.roles = roles;
    }
}
