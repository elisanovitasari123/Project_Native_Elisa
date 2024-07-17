package com.example.Fasilitas;

import java.io.Serializable;

public class GetDataFasilitas implements Serializable {
    private final String id_fasilitas;
    private final String nama_fasilitas;
    private final String kategori;


    public GetDataFasilitas(String id_fasilitas, String nama_fasilitas, String kategori) {
        this.id_fasilitas = id_fasilitas;
        this.nama_fasilitas = nama_fasilitas;
        this.kategori = kategori;
    }

    public String getIdFasilitas() {
        return id_fasilitas;
    }

    public String getNamaFasilitas() {
        return nama_fasilitas;
    }

    public String getKategori() {
        return kategori;
    }

}
