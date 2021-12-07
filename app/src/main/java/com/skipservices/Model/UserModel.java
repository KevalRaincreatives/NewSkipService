package com.skipservices.Model;

/**
 * Created by DELL on 03/24/2016.
 */
public class UserModel {
    int id;
    String userId,name,username,email,password;

    public UserModel(int id, String userId, String name, String username, String email, String password) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String TableName(){
        return "Details3";
    }
    public UserModel() {
        // TODO Auto-generated constructor stub
    }
}
