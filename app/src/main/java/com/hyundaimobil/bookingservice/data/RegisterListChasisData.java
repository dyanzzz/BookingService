package com.hyundaimobil.bookingservice.data;

/**
 * Created by User HMI on 2/7/2018.
 */

public class RegisterListChasisData {
    private String nomor, chasis, engine, nopol, type, colour, kdtype, kdcolour;

    public RegisterListChasisData() {}

    public RegisterListChasisData(String nomor, String chasis, String engine, String nopol, String type, String colour, String kdtype, String kdcolour) {
        this.nomor      = nomor;
        //this.cust_name  = cust_name;
        this.chasis     = chasis;
        this.engine     = engine;
        this.nopol      = nopol;
        this.type       = type;
        this.colour     = colour;

        this.kdtype     = kdtype;
        this.kdcolour   = kdcolour;
    }

    public String getNomor() {
        return nomor;
    }
    public void setNomor(String nomor) {
        this.nomor = nomor;
    }
/*
    public String getName() {
        return cust_name;
    }
    public void setName(String cust_name) {
        this.cust_name = cust_name;
    }
*/
    public String getChasis() {
        return chasis;
    }
    public void setChasis(String chasis) {
        this.chasis = chasis;
    }

    public String getEngine() {
        return engine;
    }
    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getNopol() {
        return nopol;
    }
    public void setNopol(String nopol) {
        this.nopol = nopol;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getColor() {
        return colour;
    }
    public void setColor(String colour) {
        this.colour = colour;
    }

    public String getKdType() {
        return kdtype;
    }
    public void setKdType(String kdtype) {
        this.kdtype = kdtype;
    }

    public String getKdColor() {
        return kdcolour;
    }
    public void setKdColor(String kdcolour) {
        this.kdcolour = kdcolour;
    }

}
