package com.example.pawrescue;

import android.graphics.Bitmap;

public class LostPet {
    private String name;
    private String type;
    private int age;
    private String gender;
    private Bitmap photoBitmap;
    private String lostAddress;

    public LostPet() {
    }

    public LostPet(String name, String type, int age, String gender, Bitmap photoBitmap, String lostAddress) {
        this.name = name;
        this.type = type;
        this.age = age;
        this.gender = gender;
        this.photoBitmap = photoBitmap;
        this.lostAddress = lostAddress;
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

    public String getLostAddress() {
        return lostAddress;
    }

    public void setLostAddress(String lostAddress) {
        this.lostAddress = lostAddress;
    }
}
