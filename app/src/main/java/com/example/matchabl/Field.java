package com.example.matchabl;

import org.json.JSONArray;

public class Field {
    private int id;
    private String name;
    private String address;
    private double rating;
    private JSONArray facilitySports;

    public Field(int id, String name, String address, double rating, JSONArray facilitySports) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.rating = rating;
        this.facilitySports = facilitySports;
    }

    // Getters and setters for all fields
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public double getRating() {
        return rating;
    }

    public JSONArray getFacilitySports() {
        return facilitySports;
    }
}
