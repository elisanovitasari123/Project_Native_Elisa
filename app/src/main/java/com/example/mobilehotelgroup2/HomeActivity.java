package com.example.mobilehotelgroup2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.Booking.DataBooking;
import com.example.Fasilitas.FasilitasActivity;
import com.example.K_Fasilitas.DataKFasilitas;
import com.example.Karyawan.KaryawanActivity;
import com.example.Pembersihan.DataPembersihan;
import com.example.Room.RoomActivity;
import com.example.Villa.VillaActivity;
import com.example.customer.CustomerActivity;


public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button btnCustomer = findViewById(R.id.btnCustomer2);
        btnCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, CustomerActivity.class);
                startActivity(intent);
            }
        });
        Button btnRoom = findViewById(R.id.btnRoom2);
        btnRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, RoomActivity.class);
                startActivity(intent);
            }
        });

        Button btnVilla = findViewById(R.id.btnVilla2);
        btnVilla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, VillaActivity.class);
                startActivity(intent);
            }
        });
        Button btnFasilitas = findViewById(R.id.btnFasilitas);
        btnFasilitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, FasilitasActivity.class);
                startActivity(intent);
            }
        });
        Button btnKaryawan = findViewById(R.id.btnKaryawan);
        btnKaryawan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, KaryawanActivity.class);
                startActivity(intent);
            }
        });
        Button btnBooking = findViewById(R.id.btnBooking);
        btnBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, DataBooking.class);
                startActivity(intent);
            }
        });
        Button btnKFasilitas = findViewById(R.id.btnKFasilitas);
        btnKFasilitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, DataKFasilitas.class);
                startActivity(intent);
            }
        });
        Button btnP = findViewById(R.id.btnPembersihan);
        btnP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, DataPembersihan.class);
                startActivity(intent);
            }
        });

    }
}