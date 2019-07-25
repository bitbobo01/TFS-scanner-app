package com.example.tfsscanner.RetrofitService;

import com.example.tfsscanner.Models.Food;
import com.example.tfsscanner.Models.Photo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitService {
    @GET("api/Staff/getFoodData")
    Call<Food> FoodDetails(@Query("id") int foodId);
    @GET("https://api.myjson.com/bins/rl7kx")
    Call<Food> Food();

}
