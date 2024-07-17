package com.example.Room;

import java.io.Serializable;

public class GetDataRoom implements Serializable {
    private final String roomid;
    private final String namaKamar;
    private final String tipeKamar;
    private final String harga;

    public GetDataRoom(String roomid, String namaKamar, String tipeKamar, String harga) {
        this.roomid = roomid;
        this.namaKamar = namaKamar;
        this.tipeKamar = tipeKamar;
        this.harga = harga;
    }

    public String getRoomId() {
        return roomid;
    }

    public String getNamaKamar() {
        return namaKamar;
    }

    public String getTipeKamar() {
        return tipeKamar;
    }

    public String getHarga() {
        return harga;
    }
}
