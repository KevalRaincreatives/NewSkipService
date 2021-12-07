package com.skipservices.Model;

/**
 * Created by DELL on 03/24/2016.
 */
public class SecondMan2Model {
    int id;
    String manId,name,email;

    public SecondMan2Model(int id, String manId, String name, String email) {
        this.id = id;
        this.manId = manId;
        this.name = name;
        this.email = email;
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

    public String getManId() {
        return manId;
    }

    public void setManId(String manId) {
        this.manId = manId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String TableName(){
        return "SecondMan";
    }
    public SecondMan2Model() {
        // TODO Auto-generated constructor stub
    }
}
