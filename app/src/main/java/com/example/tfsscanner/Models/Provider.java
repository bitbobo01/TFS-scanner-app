package com.example.tfsscanner.Models;

import java.io.Serializable;
import java.util.Date;

public class Provider implements Serializable {
    public int ProviderId;

    public String Name;

    public String Address;

    public Date ReceivedDate ;

    public Treatments Treatment ;

    public Packaging Packaging;

    public Date ProvideDate;

    public int getProviderId() {
        return ProviderId;
    }

    public void setProviderId(int providerId) {
        ProviderId = providerId;
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

    public Date getReceivedDate() {
        return ReceivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        ReceivedDate = receivedDate;
    }

    public Treatments getTreatment() {
        return Treatment;
    }

    public void setTreatment(Treatments treatment) {
        Treatment = treatment;
    }

    public com.example.tfsscanner.Models.Packaging getPackaging() {
        return Packaging;
    }

    public void setPackaging(com.example.tfsscanner.Models.Packaging packaging) {
        Packaging = packaging;
    }

    public Date getProvideDate() {
        return ProvideDate;
    }

    public void setProvideDate(Date provideDate) {
        ProvideDate = provideDate;
    }
}
