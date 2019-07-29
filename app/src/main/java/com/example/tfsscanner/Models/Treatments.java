package com.example.tfsscanner.Models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Treatments implements Serializable {
    public Date TreatmentDate ;

    public List<String> TreatmentProcess ;

    public Date getTreatmentDate() {
        return TreatmentDate;
    }

    public void setTreatmentDate(Date treatmentDate) {
        TreatmentDate = treatmentDate;
    }

    public List<String> getTreatmentProcess() {
        return TreatmentProcess;
    }

    public void setTreatmentProcess(List<String> treatmentProcess) {
        TreatmentProcess = treatmentProcess;
    }
}
