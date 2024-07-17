package com.example.customer;

import java.io.Serializable;

public class GetData implements Serializable {
    private final String id;
    private final String nama;
    private final String phone;
    private final String addres;

    public GetData(String id, String nama, String phone, String addres) {
        this.id = id;
        this.nama = nama;
        this.phone = phone;
        this.addres = addres;
    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddres() {
        return addres;
    }
}
