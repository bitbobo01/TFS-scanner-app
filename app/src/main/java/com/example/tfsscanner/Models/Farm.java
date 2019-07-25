package com.example.tfsscanner.Models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class Farm {
    public int FarmId;

    public String Name;

    public String Address ;

    public List<String> Feedings ;

    public List<Vaccination> Vaccinations ;

    public Date CertificationDate ;

    public String CertificationNumber ;

    public Date FoodSentDate;

    public int getFarmId() {
        return FarmId;
    }

    public void setFarmId(int farmId) {
        FarmId = farmId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public List<String> getFeedings() {
        return Feedings;
    }

    public void setFeedings(List<String> feedings) {
        Feedings = feedings;
    }

    public List<Vaccination> getVaccinations() {
        return Vaccinations;
    }

    public void setVaccinations(List<Vaccination> vaccinations) {
        Vaccinations = vaccinations;
    }

    public Date getCertificationDate() {
        return CertificationDate;
    }

    public void setCertificationDate(Date certificationDate) {
        CertificationDate = certificationDate;
    }

    public String getCertificationNumber() {
        return CertificationNumber;
    }

    public void setCertificationNumber(String certificationNumber) {
        CertificationNumber = certificationNumber;
    }

    public Date getFoodSentDate() {
        return FoodSentDate;
    }

    public void setFoodSentDate(Date foodSentDate) {
        FoodSentDate = foodSentDate;
    }
}
