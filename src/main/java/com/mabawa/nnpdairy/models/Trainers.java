package com.mabawa.nnpdairy.models;

import java.io.Serializable;
import java.util.List;

public class Trainers implements Serializable {
    private List trainers;

    public List getTrainers() {
        return trainers;
    }
    public void setTrainers(List trainers) {
        this.trainers = trainers;
    }
}
