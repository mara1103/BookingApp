package com.example.myrecyclerview2.classes;


import java.io.Serializable;
import java.util.ArrayList;

public class Destination implements Serializable {
    int id;
    String name;
    String photo;
    String description;
    int price;
    //private ArrayList<Review> reviews;

    public Destination(String name, String photo, String description, int price){ //, ArrayList<Review> reviews) {
        this.name = name;
        this.photo = photo;
        this.description = description;
        this.price = price;
        //this.reviews = reviews;
    }

    public Destination(int id, String name, String photo, String description, int price) { //, String EMPTY
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.description = description;
        this.price = price;
        //this.EMPTY = EMPTY;
    }

    String EMPTY = "NOT DEFINED";

    public Destination() {
        this.id=0;
        this.name = EMPTY;
        this.photo = EMPTY;
        this.description = EMPTY;
        this.price = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
//    public ArrayList<Review> getReviews() {
//        return reviews;
//    }
//
//    public void setReviews(ArrayList<Review> reviews) {
//        this.reviews = reviews;
//    }
}

