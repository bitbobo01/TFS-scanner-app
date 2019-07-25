package com.example.tfsscanner.Models;

import java.util.Date;

class Packaging {
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
