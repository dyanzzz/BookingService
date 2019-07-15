package com.hyundaimobil.bookingservice.data;

/**
 * Created by User HMI on 10/30/2017.
 */

public class ListBookingData {
    private String idBooking, nomor, coyDealer, idDealer, namaDealer, date, time, remarks, note, statusBooking;

    public ListBookingData() {}

    public ListBookingData(String idBooking, String nomor, String coyDealer, String idDealer, String namaDealer, String date, String time, String remarks, String note, String statusBooking) {
        this.idBooking      = idBooking;
        this.nomor          = nomor;
        this.coyDealer      = coyDealer;
        this.idDealer       = idDealer;
        this.namaDealer     = namaDealer;
        this.date           = date;
        this.time           = time;
        this.remarks        = remarks;
        this.note           = note;
        this.statusBooking  = statusBooking;
    }

    public String getIdBooking() {
        return idBooking;
    }
    public void setIdBooking(String idBooking) {
        this.idBooking = idBooking;
    }

    public String getNomor() {
        return nomor;
    }
    public void setNomor(String nomor) {
        this.nomor = nomor;
    }

    public String getCoyDealer() {
        return coyDealer;
    }
    public void setCoyDealer(String coyDealer) {
        this.coyDealer = coyDealer;
    }

    public String getIdDealer() {
        return idDealer;
    }
    public void setIdDealer(String idDealer) {
        this.idDealer = idDealer;
    }

    public String getNamaDealer() {
        return namaDealer;
    }
    public void setNamaDealer(String namaDealer) {
        this.namaDealer = namaDealer;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    public String getRemarks() {
        return remarks;
    }
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }

    public String getStatusBooking() {
        return statusBooking;
    }
    public void setStatusBooking(String statusBooking) {
        this.statusBooking = statusBooking;
    }

}
