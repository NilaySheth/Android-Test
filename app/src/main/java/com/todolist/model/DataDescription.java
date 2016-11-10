package com.todolist.model;

import com.google.gson.annotations.SerializedName;

public class DataDescription {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("state")
    private int state;
    private boolean isSelected;

    public DataDescription() {
    }

    public DataDescription(int id, String name, int state, boolean isSelected) {
        this.id = id;
        this.name = name;
        this.state = state;
        this.isSelected = isSelected;
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

}