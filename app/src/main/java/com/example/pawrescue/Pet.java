package com.example.pawrescue;

import android.graphics.Bitmap;

public class Pet {
    private String name;
    private String type;
    private int age;
    private String gender;
    private Bitmap photoBitmap;
    private String state; // New variable

    public Pet(String name, String type, int age, String gender, Bitmap photoBitmap, String state) {
        this.name = name;
        this.type = type;
        this.age = age;
        this.gender = gender;
        this.photoBitmap = photoBitmap;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Bitmap getPhotoBitmap() {
        return photoBitmap;
    }

    public void setPhotoBitmap(Bitmap photoBitmap) {
        this.photoBitmap = photoBitmap;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
