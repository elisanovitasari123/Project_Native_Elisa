package com.example.Booking;

import java.io.Serializable;

public class GetDataBooking implements Serializable {
    private String booking_id;
    private String nama;
    private String namaKamar;
    private String check_in_date;
    private String check_out_date;
    private Integer total_price;

    public GetDataBooking(String booking_id, String nama, String namaKamar,String check_in_date, String check_out_date, Integer total_price) {
        this.booking_id = booking_id;
        this.nama = nama;
        this.namaKamar=namaKamar;
        this.check_in_date = check_in_date;
        this.check_out_date = check_out_date;
        this.total_price = total_price;
    }

    public String getBookingId() {
        return booking_id;
    }

    public String getNama() {
        return nama;
    }

    public String getNamaKamar() {
        return namaKamar;
    }

    public String getCheckInDate() {
        return check_in_date;
    }
    public String getCheckOutDate() {
        return check_out_date;
    }
    public Integer getTotalPrice() {
        return total_price;
    }
}
