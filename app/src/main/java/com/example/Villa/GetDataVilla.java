package com.example.Villa;

import java.io.Serializable;

public class GetDataVilla implements Serializable {
    private final String id_villa;
    private final String namaVilla;
    private final String kontak;
    private final String email;
    private final String lokasi;

    public GetDataVilla(String id_villa, String namaVilla, String kontak, String email, String lokasi) {
        this.id_villa = id_villa;
        this.namaVilla = namaVilla;
        this.kontak = kontak;
        this.email = email;
        this.lokasi = lokasi;
    }

    public String getIdVilla() {
        return id_villa;
    }

    public String getNamaVilla() {
        return namaVilla;
    }

    public String getKontak() {
        return kontak;
    }

    public String getEmail() {
        return email;
    }
    public String getLokasi() {
        return lokasi;
    }
}
