package com.skipservices.parsers;

import java.io.Serializable;

/**
 * Created by DELL on 10/04/2016.
 */

public class SecondManModel implements Serializable{
    private String id;
    private String name;
    private String email;

    public SecondManModel(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public SecondManModel() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}