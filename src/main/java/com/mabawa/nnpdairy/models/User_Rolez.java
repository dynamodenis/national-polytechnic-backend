package com.mabawa.nnpdairy.models;

import java.io.Serializable;
import java.util.List;

public class User_Rolez implements Serializable {
    private List training_support;
    private List research_consultancy_innovation;
    private List services_marketplace;
    private List consultancy;
    private List users;
    private List dashboard;

    public List getTraining_support() {
        return training_support;
    }
    public void setTraining_support(List training_support) {
        this.training_support = training_support;
    }

    public List getResearch_consultancy_innovation() {
        return research_consultancy_innovation;
    }
    public void setResearch_consultancy_innovation(List research_consultancy_innovation) {
        this.research_consultancy_innovation = research_consultancy_innovation;
    }

    public List getServices_marketplace() {
        return services_marketplace;
    }
    public void setServices_marketplace(List services_marketplace) {
        this.services_marketplace = services_marketplace;
    }

    public List getConsultancy() {
        return consultancy;
    }
    public void setConsultancy(List consultancy) {
        this.consultancy = consultancy;
    }

    public List getUsers() {
        return users;
    }
    public void setUsers(List users) {
        this.users = users;
    }

    public List getDashboard() {
        return dashboard;
    }
    public void setDashboard(List dashboard) {
        this.dashboard = dashboard;
    }
}
