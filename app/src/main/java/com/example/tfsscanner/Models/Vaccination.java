package com.example.tfsscanner.Models;

import java.util.Date;

class Vaccination {
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