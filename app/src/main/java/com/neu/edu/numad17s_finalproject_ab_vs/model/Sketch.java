package com.neu.edu.numad17s_finalproject_ab_vs.model;

import java.util.ArrayList;

/**
 * Created by vaibhavshukla on 4/16/17.
 */

public class Sketch {

    private String mouseAction = "";
    private String mouseLocation = "";
    private String mouseColor = "";
    private String sketchID = "";
    private boolean joined =false;
    private boolean hosted = false;
    private String user1 = "";
    private String user2 = "";
    private ArrayList<String> guesses = new ArrayList<>();

    public Sketch() {
    }

    public ArrayList<String> getGuesses() {
        return guesses;
    }

    public void setGuesses(ArrayList<String> guesses) {
        this.guesses = guesses;
    }

    public String getUser2() {
        return user2;
    }

    public void setUser2(String user2) {
        this.user2 = user2;
    }

    public String getUser1() {
        return user1;
    }

    public void setUser1(String user1) {
        this.user1 = user1;
    }

    public boolean getHosted() {
        return hosted;
    }

    public void setHosted(boolean hosted) {
        this.hosted = hosted;
    }

    public boolean getJoined() {
        return joined;
    }

    public void setJoined(boolean joined) {
        this.joined = joined;
    }

    public String getSketchID() {
        return sketchID;
    }

    public void setSketchID(String sketchID) {
        this.sketchID = sketchID;
    }

    public String getMouseAction() {
        return mouseAction;
    }

    public void setMouseAction(String mouseAction) {
        this.mouseAction = mouseAction;
    }

    public String getMouseLocation() {
        return mouseLocation;
    }

    public void setMouseLocation(String mouseLocation) {
        this.mouseLocation = mouseLocation;
    }

    public String getMouseColor() {
        return mouseColor;
    }

    public void setMouseColor(String mouseColor) {
        this.mouseColor = mouseColor;
    }
}
