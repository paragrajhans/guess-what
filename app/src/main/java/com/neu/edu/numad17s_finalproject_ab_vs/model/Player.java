package com.neu.edu.numad17s_finalproject_ab_vs.model;

/**
 * Created by ashishbulchandani on 13/04/17.
 */

public class Player {

    String name;
    String regId;
    public long status;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }
}
