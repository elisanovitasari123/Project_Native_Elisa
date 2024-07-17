package com.example.K_Fasilitas;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.K_Fasilitas.ConfigurasiKFasilitas;
import com.example.mobilehotelgroup2.R;


import java.util.HashMap;
import java.util.Map;

public class   EditKFasilitas extends AppCompatActivity {

    private EditText editNamaVilla, editNamaFasilitas, editNamaKamar, editStatusFasilitas;
    private Button updateButton;
    private String id_k_fasilitas;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_k_fasilitas);

        editNamaVilla = findViewById(R.id.namaVilla);
        editNamaFasilitas = findViewById(R.id.nama_fasilitas);
        editNamaKamar = findViewById(R.id.namaKamar);
        editStatusFasilitas = findViewById(R.id.status_fasilitas);
        updateButton = findViewById(R.id.saveK);

        // Mendapatkan data dari intent
        Intent intent = getIntent();
        if (intent != null) {
            id_k_fasilitas = intent.getStringExtra("id_k_fasilitas");
            editNamaVilla.setText(intent.getStringExtra("namaVilla"));
            editNamaFasilitas.setText(intent.getStringExtra("nama_fasilitas"));
            editNamaKamar.setText(intent.getStringExtra("namaKamar"));
            editStatusFasilitas.setText(intent.getStringExtra("status_fasilitas"));
        }

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });
    }
    // Listener untuk tanggal check-in dan check-out


    private void updateData() {
        final String namaVilla = editNamaVilla.getText().toString().trim();
        final String namaFasilitas = editNamaFasilitas.getText().toString().trim();
        final String namaKamar = editNamaKamar.getText().toString().trim();
        final String status_fasilitas = editStatusFasilitas.getText().toString().trim();


        // Validasi input
        if (namaVilla.isEmpty() || namaFasilitas.isEmpty() || namaVilla.isEmpty() || status_fasilitas.isEmpty() ) {
            Toast.makeText(EditKFasilitas.this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }

        // URL untuk skrip PHP yang akan melakukan update data
        String url = new ConfigurasiKFasilitas().baseUrl() + "update_k_fasilitas.php";

        // Buat request baru
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Tangani respons dari server
                        Log.d("EditKFasilitas", "Response: " + response);
                        if (response.equalsIgnoreCase("success")) {
                            Toast.makeText(EditKFasilitas.this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show();
                            sendRefreshFlag(true); // Mengirim flag refresh untuk memuat ulang data
                            finish(); // Tutup aktivitas
                        } else {
                            Toast.makeText(EditKFasilitas.this, "berhasil memperbarui data", Toast.LENGTH_SHORT).show();
                            sendRefreshFlag(true); // Mengirim flag refresh untuk memuat ulang data
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Tangani kesalahan
                        Log.e("EditKFasilitas", "Error: " + error.getMessage());
                        Toast.makeText(EditKFasilitas.this, "Gagal memperbarui data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Buat parameter untuk request
                Map<String, String> params = new HashMap<>();
                params.put("id_k_fasilitas", id_k_fasilitas);
                params.put("namaVilla", namaVilla);
                params.put("nama_fasilitas", namaFasilitas);
                params.put("namaKamar", namaKamar);
                params.put("status_fasilitas", status_fasilitas);
                 // Sesuaikan dengan nama kolom di server PHP
                Log.d("EditKFasilitas", "Params: " + params.toString()); // Log parameter untuk debugging
                return params;
            }
        };

        // Tambahkan request ke RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void sendRefreshFlag(boolean refresh) {
        Intent intent = new Intent();
        intent.putExtra("refreshflag", refresh);
        setResult(RESULT_OK, intent);
    }
}
