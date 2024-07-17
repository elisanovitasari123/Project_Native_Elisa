package com.example.Pembersihan;

import java.io.Serializable;

public class GetDataPembersihan implements Serializable {
    private String id_p;
    private String namaKamar;
    private String nama_karyawan;
    private String tanggal;
    private String deskripsi;

    public GetDataPembersihan(String id_p, String namaKamar, String nama_karyawan, String tanggal, String deskripsi) {
        this.id_p = id_p;
        this.namaKamar=namaKamar;
        this.nama_karyawan=nama_karyawan;
        this.tanggal = tanggal;
        this.deskripsi = deskripsi;
    }

    public String getIdP() {
        return id_p;
    }

    public String getNamaKamar() {
        return namaKamar;
    }
    public String getNamaKaryawan() { return nama_karyawan; }

    public String getTanggal() {
        return tanggal;
    }
    public String getDeskripsi() {
        return deskripsi;
    }

}
