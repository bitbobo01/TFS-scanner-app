package com.example.tfsscanner.Models;

import java.io.Serializable;
import java.util.Date;

public class Vaccination implements Serializable {
    public Date VaccinationDate ;

    public String VaccinationType;

    public Date getVaccinationDate() {
        return VaccinationDate;
    }

    public void setVaccinationDate(Date vaccinationDate) {
        VaccinationDate = vaccinationDate;
    }

    public String getVaccinationType() {
        return VaccinationType;
    }

    public void setVaccinationType(String vaccinationType) {
        VaccinationType = vaccinationType;
    }
}
