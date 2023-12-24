package com.example.pawrescue;
public class Pet {
    private String name;
    private String type;
    private int age;
    private String gender;
    private String photo;

    public Pet(String name, String type, int age, String gender, String photo) {
        this.name = name;
        this.type = type;
        this.age = age;
        this.gender = gender;
        this.photo = photo;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }
}