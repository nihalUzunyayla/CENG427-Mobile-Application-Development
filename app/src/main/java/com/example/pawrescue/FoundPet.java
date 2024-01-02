package com.example.pawrescue;

import android.graphics.Bitmap;

public class FoundPet {
    private String type;
    private String gender;
    private int estimatedAge;
    private Bitmap photoBitmap;

    public FoundPet(String type, String gender, int estimatedAge, Bitmap photoBitmap) {
        this.type = type;
        this.gender = gender;
        this.estimatedAge = estimatedAge;
        this.photoBitmap = photoBitmap;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getEstimatedAge() {
        return estimatedAge;
    }

    public void setEstimatedAge(int estimatedAge) {
        this.estimatedAge = estimatedAge;
    }

    public Bitmap getPhotoBitmap() {
        return photoBitmap;
    }

    public void setPhotoBitmap(Bitmap photoBitmap) {
        this.photoBitmap = photoBitmap;
    }
}
