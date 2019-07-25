package com.example.tfsscanner.Models;

import java.io.Serializable;
import java.util.Date;

public class Distributor implements Serializable {
    public int DistributorId ;

    public String Name ;

    public Date ReceivedDate;

    public int getDistributorId() {
        return DistributorId;
    }

    public void setDistributorId(int distributorId) {
        DistributorId = distributorId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Date getReceivedDate() {
        return ReceivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        ReceivedDate = receivedDate;
    }
}
