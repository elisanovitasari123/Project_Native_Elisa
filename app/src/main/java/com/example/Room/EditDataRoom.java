package com.example.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.example.mobilehotelgroup2.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.Map;

public class EditDataRoom extends AppCompatActivity {

    private EditText editNamaKamar, editTipeKamar, editHarga;
    private FloatingActionButton updateButton; // Mengubah dari Button ke FloatingActionButton
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_room);

        editNamaKamar = findViewById(R.id.namaKamar);
        editTipeKamar = findViewById(R.id.tipeKamar);
        editHarga = findViewById(R.id.hargaKamar);

        updateButton = findViewById(R.id.btnSaveKamar); // Mengubah inisialisasi

        // Mendapatkan data dari intent
        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getStringExtra("roomid");
            editNamaKamar.setText(intent.getStringExtra("namaKamar"));
            editTipeKamar.setText(intent.getStringExtra("tipeKamar"));
            editHarga.setText(intent.getStringExtra("harga"));
        }

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });
    }

    private void updateData() {
        final String namaKamar = editNamaKamar.getText().toString().trim();
        final String tipeKamar = editTipeKamar.getText().toString().trim();
        final String harga = editHarga.getText().toString().trim();


        // Validasi input
        if (namaKamar.isEmpty() || tipeKamar.isEmpty() || harga.isEmpty() ) {
            Toast.makeText(EditDataRoom.this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }

        // URL untuk skrip PHP yang akan melakukan update data
        String url = new ConfigurasiRoom().baseUrl() + "update_room.php";

        // Buat request baru
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Tangani respons dari server
                        Log.d("EditDataRoom", "Response: " + response);
                        Toast.makeText(EditDataRoom.this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show();
                        sendRefreshFlag(true); // Mengirim flag refresh untuk memuat ulang data
                        finish(); // Tutup aktivitas
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Tangani kesalahan
                        Log.e("EditDataRoom", "Error: " + error.getMessage());
                        Toast.makeText(EditDataRoom.this, "Gagal memperbarui data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Buat parameter untuk request
                Map<String, String> params = new HashMap<>();
                params.put("roomid", id);
                params.put("namaKamar", namaKamar);
                params.put("tipeKamar", tipeKamar);
                params.put("harga", harga);
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
