package com.neu.edu.numad17s_finalproject_ab_vs.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by vaibhavshukla on 3/2/17.
 */

@IgnoreExtraProperties
public class User {

     String emailId;
     int gameId;
     boolean active;
    boolean isActive;
    String name;
    String tokenId;
    String score = "0";



    public User(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
