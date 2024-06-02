package com.example.matchabl;

public class Field {
    private int id;
    private String name;
    private String address;
    private double rating;

    public Field(int id, String name, String address, double rating) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.rating = rating;
    }

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
}
