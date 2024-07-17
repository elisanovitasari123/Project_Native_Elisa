package com.example.Fasilitas;

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

public class EditDataFasilitas extends AppCompatActivity {

    private EditText editNamaFasilitas, editKategori;
    private FloatingActionButton updateButton; // Mengubah dari Button ke FloatingActionButton
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_fasilitas);

        editNamaFasilitas = findViewById(R.id.namaFasil);
        editKategori = findViewById(R.id.kateg);
        updateButton = findViewById(R.id.btnSaveFasil); // Mengubah inisialisasi

        // Mendapatkan data dari intent
        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getStringExtra("id_fasilitas");
            editNamaFasilitas.setText(intent.getStringExtra("nama_fasilitas"));
            editKategori.setText(intent.getStringExtra("kategori"));
        }

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });
    }

    private void updateData() {
        final String nama_fasilitas = editNamaFasilitas.getText().toString().trim();
        final String kategori = editKategori.getText().toString().trim();

        // Validasi input
        if (nama_fasilitas.isEmpty() || kategori.isEmpty() ) {
            Toast.makeText(EditDataFasilitas.this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }

        // URL untuk skrip PHP yang akan melakukan update data
        String url = new ConfigurasiFasilitas().baseUrl() + "update_fasilitas.php";

        // Buat request baru
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Tangani respons dari server
                        Log.d("EditDataFasilitas", "Response: " + response);
                        Toast.makeText(EditDataFasilitas.this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show();
                        sendRefreshFlag(true); // Mengirim flag refresh untuk memuat ulang data
                        finish(); // Tutup aktivitas
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Tangani kesalahan
                        Log.e("EditDataFasilitas", "Error: " + error.getMessage());
                        Toast.makeText(EditDataFasilitas.this, "Gagal memperbarui data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Buat parameter untuk request
                Map<String, String> params = new HashMap<>();
                params.put("id_fasilitas", id);
                params.put("nama_fasilitas", nama_fasilitas);
                params.put("kategori", kategori);
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
