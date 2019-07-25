package com.example.tfsscanner.Models;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Food implements Serializable {
    @SerializedName("FoodId")
    public int FoodId;
    @SerializedName("Breed")
    public String Breed;
    @SerializedName("Category")
    public String Category;
    @SerializedName("Farm")
    public Farm Farm;
    @SerializedName("Provider")
    public Provider Provider;
    @SerializedName("Distributor")
    public Distributor Distributor;
    @SerializedName("Providers")
    public List<Provider> Providers;
    @SerializedName("Distributors")
    public List<Distributor> Distributors;
    @SerializedName("StartedDate")
    public Date StartedDate;

    public Food(int foodId, String breed, String category, com.example.tfsscanner.Models.Farm farm, com.example.tfsscanner.Models.Provider provider, com.example.tfsscanner.Models.Distributor distributor, List<com.example.tfsscanner.Models.Provider> providers, List<com.example.tfsscanner.Models.Distributor> distributors, Date startedDate) {
        FoodId = foodId;
        Breed = breed;
        Category = category;
        Farm = farm;
        Provider = provider;
        Distributor = distributor;
        Providers = providers;
        Distributors = distributors;
        StartedDate = startedDate;
    }

    public Food() {
    }

    public int getFoodId() {
        return FoodId;
    }

    public void setFoodId(int foodId) {
        FoodId = foodId;
    }

    public String getBreed() {
        return Breed;
    }

    public void setBreed(String breed) {
        Breed = breed;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public com.example.tfsscanner.Models.Farm getFarm() {
        return Farm;
    }

    public void setFarm(com.example.tfsscanner.Models.Farm farm) {
        Farm = farm;
    }

    public com.example.tfsscanner.Models.Provider getProvider() {
        return Provider;
    }

    public void setProvider(com.example.tfsscanner.Models.Provider provider) {
        Provider = provider;
    }

    public com.example.tfsscanner.Models.Distributor getDistributor() {
        return Distributor;
    }

    public void setDistributor(com.example.tfsscanner.Models.Distributor distributor) {
        Distributor = distributor;
    }

    public List<com.example.tfsscanner.Models.Provider> getProviders() {
        return Providers;
    }

    public void setProviders(List<com.example.tfsscanner.Models.Provider> providers) {
        Providers = providers;
    }

    public List<com.example.tfsscanner.Models.Distributor> getDistributors() {
        return Distributors;
    }

    public void setDistributors(List<com.example.tfsscanner.Models.Distributor> distributors) {
        Distributors = distributors;
    }

    public Date getStartedDate() {
        return StartedDate;
    }

    public void setStartedDate(Date startedDate) {
        StartedDate = startedDate;
    }
}