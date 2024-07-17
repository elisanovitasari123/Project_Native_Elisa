package com.example.K_Fasilitas;

import java.io.Serializable;

public class GetDataKFasilitas implements Serializable {
    private String id_k_fasilitas;
    private String namaVilla;
    private String nama_fasilitas;
    private String namaKamar;
    private String status_fasilitas;


    public GetDataKFasilitas(String id_k_fasilitas, String namaVilla, String nama_fasilitas, String namaKamar, String status_fasilitas) {
        this.id_k_fasilitas= id_k_fasilitas;
        this.namaVilla = namaVilla;
        this.nama_fasilitas = nama_fasilitas;
        this.namaKamar=namaKamar;
        this.status_fasilitas = status_fasilitas;
    }

    public String getIdKFasilitas() {
        return id_k_fasilitas;
    }

    public String getNamaVilla() {
        return namaVilla;
    }
    public String getNamaFasilitas() {
        return nama_fasilitas;
    }

    public String getNamaKamar() {
        return namaKamar;
    }

    public String getStatusFasilitas() {return status_fasilitas;}

}
