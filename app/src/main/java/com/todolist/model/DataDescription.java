package com.todolist.model;

import com.google.gson.annotations.SerializedName;

public class DataDescription {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("state")
    private int state;

    public DataDescription(int id, String name, int state) {
        this.id = id;
        this.name = name;
        this.state = state;
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

    public int isState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

}