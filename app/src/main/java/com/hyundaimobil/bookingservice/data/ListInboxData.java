package com.hyundaimobil.bookingservice.data;

public class ListInboxData {
    private String idInbox, nomor, date, title, message;

    public ListInboxData() {}

    public ListInboxData(String idInbox, String nomor, String date, String title, String message) {
        this.idInbox    = idInbox;
        this.nomor      = nomor;
        this.date       = date;
        this.title      = title;
        this.message    = message;
    }

    public String getIdInbox() {
        return idInbox;
    }
    public void setIdInbox(String idInbox) {
        this.idInbox = idInbox;
    }

    public String getNomor() {
        return nomor;
    }
    public void setNomor(String nomor) {
        this.nomor = nomor;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

}
