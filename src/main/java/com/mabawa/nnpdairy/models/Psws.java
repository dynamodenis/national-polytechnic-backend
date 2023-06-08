package com.mabawa.nnpdairy.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "psws")
public class Psws {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private UUID userid;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp lastdate;
    private String psw1;
    private String psw2;
    private String psw3;
    private String psw4;
    private String psw5;
    private int lastpsw;
    @Transient
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User userz;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public UUID getUserid() {
        return userid;
    }
    public void setUserid(UUID userid) {
        this.userid = userid;
    }

    public Timestamp getLastdate() {
        return lastdate;
    }
    public void setLastdate(Timestamp lastdate) {
        this.lastdate = lastdate;
    }

    public String getPsw1() {
        return psw1;
    }
    public void setPsw1(String psw1) {
        this.psw1 = psw1;
    }

    public String getPsw2() {
        return psw2;
    }
    public void setPsw2(String psw2) {
        this.psw2 = psw2;
    }

    public String getPsw3() {
        return psw3;
    }
    public void setPsw3(String psw3) {
        this.psw3 = psw3;
    }

    public String getPsw4() {
        return psw4;
    }
    public void setPsw4(String psw4) {
        this.psw4 = psw4;
    }

    public String getPsw5() {
        return psw5;
    }
    public void setPsw5(String psw5) {
        this.psw5 = psw5;
    }

    public int getLastpsw() {
        return lastpsw;
    }
    public void setLastpsw(int lastpsw) {
        this.lastpsw = lastpsw;
    }

    public User getUserz() {
        return userz;
    }
    public void setUserz(User userz) {
        this.userz = userz;
    }
}
