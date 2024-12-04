package com.example.planetzeapplication;

public class User {
    private String fullName;
    private String email;
    private Boolean doneSurvey;

    public User() {
    }

    public User(String fullName, String email) {
        this.fullName = fullName;
        this.email = email;
        this.doneSurvey = false;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }
    public Boolean getDoneSurvey() {
        return doneSurvey;
    }

}

