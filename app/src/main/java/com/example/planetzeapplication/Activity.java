package com.example.planetzeapplication;

public abstract class Activity {

    long activityId;
    String activityName;
    String category;
    String subtype;
    double baseEmission;
    double sessionEmission;
    double magnitude;
    boolean tracking;
    long habitTracker;

    public Activity() {
        this.activityId = 0;
        this.activityName = "";
        this.category = "";
        this.subtype = "";
        this.magnitude = 0;
        this.baseEmission = 0;
    }

    public Activity(String activityName, String category, String subtype, double magnitude) {
        this.activityName = activityName;
        this.category = category;
        if (category.equalsIgnoreCase("transportation") && subtype.equalsIgnoreCase("")) {
            this.subtype = "DEFAULT VEHICLE NEEDS TO BE OBTAINED FROM ANOTHER FILE";
        }
        else {
            this.subtype = subtype;
        }
        this.activityId = assignId();
        this.magnitude = magnitude;
        this.baseEmission = computeBaseEmission();
    }

    protected abstract long assignId();

    protected abstract double computeBaseEmission();

    protected abstract double computeSessionEmission();

    public long getActivityId() {
        return activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public String getCategory() {
        return category;
    }

    public String getSubtype() {
        return subtype;
    }

    public double getBaseEmission() {
        return baseEmission;
    }

    public double getSessionEmission() {
        return sessionEmission;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public void setActivityId(long id) {
        this.activityId = id;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

    /*
    public void setActivityName(String name) {
        this.activityName = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

    public void setBaseEmission() {
        this.baseEmission = computeBaseEmission();
    }

    public void setSessionEmission() {
        this.sessionEmission = computeSessionEmission();
    }


    @Override
    public String toString() {
        return("ID: " + activityId +
                ", Name: " + activityName +
                ", Category: " + category +
                ", Base Emission: " + baseEmission);
    }

     */
}
