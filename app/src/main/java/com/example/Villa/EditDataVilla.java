package com.example.Villa;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.example.Villa.ConfigurasiVilla;
import com.example.mobilehotelgroup2.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.Map;

public class EditDataVilla extends AppCompatActivity {

    private EditText editNamaVilla, editKontak, editEmail, editLokasi;
    private FloatingActionButton updateButton; // Mengubah dari Button ke FloatingActionButton
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_villa);

        editNamaVilla = findViewById(R.id.namaV);
        editKontak = findViewById(R.id.kontakV);
        editEmail = findViewById(R.id.emailV);
        editLokasi = findViewById(R.id.lokasiV);
        updateButton = findViewById(R.id.btnSaveV); // Mengubah inisialisasi

        // Mendapatkan data dari intent
        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getStringExtra("id_villa");
            editNamaVilla.setText(intent.getStringExtra("namaVilla"));
            editKontak.setText(intent.getStringExtra("kontak"));
            editEmail.setText(intent.getStringExtra("email"));
            editLokasi.setText(intent.getStringExtra("lokasi"));
        }

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });
    }

    private void updateData() {
        final String namaVilla = editNamaVilla.getText().toString().trim();
        final String kontak = editKontak.getText().toString().trim();
        final String email = editEmail.getText().toString().trim();
        final String lokasi = editLokasi.getText().toString().trim();

        // Validasi input
        if (namaVilla.isEmpty() || kontak.isEmpty() || email.isEmpty() ) {
            Toast.makeText(EditDataVilla.this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }

        // URL untuk skrip PHP yang akan melakukan update data
        String url = new ConfigurasiVilla().baseUrl() + "update_villa.php";

        // Buat request baru
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Tangani respons dari server
                        Log.d("EditDataVilla", "Response: " + response);
                        Toast.makeText(EditDataVilla.this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show();
                        sendRefreshFlag(true); // Mengirim flag refresh untuk memuat ulang data
                        finish(); // Tutup aktivitas
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Tangani kesalahan
                        Log.e("EditDataVilla", "Error: " + error.getMessage());
                        Toast.makeText(EditDataVilla.this, "Gagal memperbarui data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Buat parameter untuk request
                Map<String, String> params = new HashMap<>();
                params.put("id_villa", id);
                params.put("namaVilla", namaVilla);
                params.put("kontak", kontak);
                params.put("email", email);
                params.put("lokasi", lokasi);
                Log.d("EditDataActivity", "Params: " + params.toString()); // Log parameter untuk debugging
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
