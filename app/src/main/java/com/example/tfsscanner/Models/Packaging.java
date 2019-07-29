package com.example.tfsscanner.Models;

import java.io.Serializable;
import java.util.Date;

public class Packaging implements Serializable {
    public Date EXPDate;

    public Date PackagingDate ;

    public Date getEXPDate() {
        return EXPDate;
    }

    public void setEXPDate(Date EXPDate) {
        this.EXPDate = EXPDate;
    }

    public Date getPackagingDate() {
        return PackagingDate;
    }

    public void setPackagingDate(Date packagingDate) {
        PackagingDate = packagingDate;
    }
}
