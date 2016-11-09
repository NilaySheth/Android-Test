package com.todolist.retrofit;

import com.todolist.model.Data;

import retrofit2.Call;
import retrofit2.http.GET;


public interface ApiInterface {
    @GET("u/6890301/tasks.json")
    Call<Data> getTODOData();
}
