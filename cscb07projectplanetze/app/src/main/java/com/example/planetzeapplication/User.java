package com.example.planetzeapplication;
import java.util.*;
public class User {
    private String fullName;
    private String email;

    ArrayList<DayTracker> days;
    double totEmission;


    public User() {
    }

    public User(String fullName, String email) {
        this.fullName = fullName;
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

}

