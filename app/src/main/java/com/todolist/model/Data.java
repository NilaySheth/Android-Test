package com.todolist.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("data")
    private List<DataDescription> results;

    public List<DataDescription> getResults() {
        return results;
    }

    public void setResults(List<DataDescription> results) {
        this.results = results;
    }

}
