package com.example.Karyawan;

import java.io.Serializable;

public class GetDataKaryawan implements Serializable {
    private final String id_karyawan;
    private final String nama_karyawan;
    private final String no_hp;
    private final String alamat;

    public GetDataKaryawan(String id_karyawan, String nama_karyawan, String no_hp, String alamat) {
        this.id_karyawan = id_karyawan;
        this.nama_karyawan = nama_karyawan;
        this.no_hp = no_hp;
        this.alamat = alamat;
    }

    public String getIdKaryawan() {
        return id_karyawan;
    }

    public String getNamaKaryawan() {
        return nama_karyawan;
    }

    public String getNoHp() {return no_hp;
    }

    public String getAlamat() {return alamat;
    }
}
