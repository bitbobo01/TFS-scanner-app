package com.example.tfsscanner.RetrofitService;

import com.example.tfsscanner.Models.Food;
import com.example.tfsscanner.Models.Photo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RetrofitService {

    @GET("api/Farm/food/foodDetail/{foodId}")
    Call<Food> FoodDetails(@Path("foodId") int foodId);
    @GET("https://api.myjson.com/bins/rl7kx")
    Call<Food> Food();

}
