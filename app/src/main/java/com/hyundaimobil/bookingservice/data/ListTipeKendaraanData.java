package com.hyundaimobil.bookingservice.data;

/**
 * Created by User HMI on 1/16/2018.
 */

public class ListTipeKendaraanData {
    private String id, nomor, namaKendaraan, kdKendaraan, image;

    public ListTipeKendaraanData() {}

    public ListTipeKendaraanData(String id, String nomor, String namaKendaraan, String kdKendaraan, String image) {
        this.id             = id;
        this.nomor          = nomor;
        this.namaKendaraan  = namaKendaraan;
        this.kdKendaraan    = kdKendaraan;
        this.image    = image;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getNomor() {
        return nomor;
    }
    public void setNomor(String nomor) {
        this.nomor = nomor;
    }

    public String getNamaKendaraan() {
        return namaKendaraan;
    }
    public void setNamaKendaraan(String namaKendaraan) {
        this.namaKendaraan = namaKendaraan;
    }

    public String getKdKendaraan() {
        return kdKendaraan;
    }
    public void setKdKendaraan(String kdKendaraan) {
        this.kdKendaraan = kdKendaraan;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

}
