package com.hyundaimobil.bookingservice.data;

/**
 * Created by User HMI on 2/2/2018.
 */

public class ListMaintenanceServiceData {
    private String nomor, coy, lts, name;

    public ListMaintenanceServiceData() {}

    public ListMaintenanceServiceData(String nomor, String coy, String lts, String name) {
        this.nomor  = nomor;
        this.coy    = coy;
        this.lts    = lts;
        this.name   = name;
    }

    public String getNomor() {
        return nomor;
    }
    public void setNomor(String nomor) {
        this.nomor = nomor;
    }

    public String getCoy() {
        return coy;
    }
    public void setCoy(String coy) {
        this.coy = coy;
    }

    public String getLts() {
        return lts;
    }
    public void setLts(String lts) {
        this.lts = lts;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

}
