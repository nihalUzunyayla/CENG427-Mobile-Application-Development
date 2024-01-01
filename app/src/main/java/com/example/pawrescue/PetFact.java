package com.example.pawrescue;

import com.google.gson.annotations.SerializedName;

public class PetFact {
    @SerializedName("text")
    private String text;

    public String getText() {
        return text;
    }
}
