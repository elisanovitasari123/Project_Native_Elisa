package com.example.Karyawan;

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
import com.example.Room.ConfigurasiRoom;
import com.example.mobilehotelgroup2.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.Map;

public class EditDataKaryawan extends AppCompatActivity {

    private EditText editNamaKaryawan, editNoHp, editAlamat;
    private FloatingActionButton updateButton; // Mengubah dari Button ke FloatingActionButton
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_karyawan);

        editNamaKaryawan = findViewById(R.id.namaK);
        editNoHp = findViewById(R.id.no_hpK);
        editAlamat = findViewById(R.id.hargaK);

        updateButton = findViewById(R.id.btnSaveK); // Mengubah inisialisasi

        // Mendapatkan data dari intent
        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getStringExtra("id_karyawan");
            editNamaKaryawan.setText(intent.getStringExtra("nama_karyawan"));
            editNoHp.setText(intent.getStringExtra("no_hp"));
            editAlamat.setText(intent.getStringExtra("alamat"));
        }

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });
    }

    private void updateData() {
        final String nama_karyawan = editNamaKaryawan.getText().toString().trim();
        final String no_hp = editNoHp.getText().toString().trim();
        final String alamat = editAlamat.getText().toString().trim();


        // Validasi input
        if (nama_karyawan.isEmpty() || no_hp.isEmpty() ||alamat.isEmpty() ) {
            Toast.makeText(EditDataKaryawan.this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }

        // URL untuk skrip PHP yang akan melakukan update data
        String url = new ConfigurasiKaryawan().baseUrl() + "update_Karyawan.php";

        // Buat request baru
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Tangani respons dari server
                        Log.d("EditDatakaryawan", "Response: " + response);
                        Toast.makeText(EditDataKaryawan.this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show();
                        sendRefreshFlag(true); // Mengirim flag refresh untuk memuat ulang data
                        finish(); // Tutup aktivitas
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Tangani kesalahan
                        Log.e("EditDataKaryawan", "Error: " + error.getMessage());
                        Toast.makeText(EditDataKaryawan.this, "Gagal memperbarui data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Buat parameter untuk request
                Map<String, String> params = new HashMap<>();
                params.put("id_karyawan", id);
                params.put("nama_karyawan", nama_karyawan);
                params.put("no_hp", no_hp);
                params.put("alamat", alamat);
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
