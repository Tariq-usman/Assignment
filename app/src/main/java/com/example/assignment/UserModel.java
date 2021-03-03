package com.example.assignment;

public class UserModel {
    String id, email,password,location;

    public UserModel(String id, String email, String password, String location) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.location = location;
    }

    public UserModel(String email, String password, String location) {
        this.email = email;
        this.password = password;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
