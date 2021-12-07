package com.skipservices.parsers;

import java.io.Serializable;

/**
 * Created by DELL on 10/04/2016.
 */

public class DisposalModel implements Serializable{
    private String id;
    private String name;

    public DisposalModel(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public DisposalModel() {

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
}