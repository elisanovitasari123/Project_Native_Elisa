package com.example.customer;

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

public class EditDataCustomer extends AppCompatActivity {

    private EditText editNama, editNomorHp, editAlamat;
    private FloatingActionButton updateButton; // Mengubah dari Button ke FloatingActionButton
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_customer);

        editNama = findViewById(R.id.nama);
        editNomorHp = findViewById(R.id.phone);
        editAlamat = findViewById(R.id.addres);

        updateButton = findViewById(R.id.btnSave); // Mengubah inisialisasi

        // Mendapatkan data dari intent
        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getStringExtra("id");
            editNama.setText(intent.getStringExtra("nama"));
            editNomorHp.setText(intent.getStringExtra("phone"));
            editAlamat.setText(intent.getStringExtra("addres"));
        }

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });
    }

    private void updateData() {
        final String nama = editNama.getText().toString().trim();
        final String nomorHp = editNomorHp.getText().toString().trim();
        final String alamat = editAlamat.getText().toString().trim();


        // Validasi input
        if (nama.isEmpty() || alamat.isEmpty() || nomorHp.isEmpty() ) {
            Toast.makeText(EditDataCustomer.this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }

        // URL untuk skrip PHP yang akan melakukan update data
        String url = new Configurasi().baseUrl() + "updatecustomer.php";

        // Buat request baru
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Tangani respons dari server
                        Log.d("EditDataActivity", "Response: " + response);
                        Toast.makeText(EditDataCustomer.this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show();
                        sendRefreshFlag(true); // Mengirim flag refresh untuk memuat ulang data
                        finish(); // Tutup aktivitas
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Tangani kesalahan
                        Log.e("EditDataActivity", "Error: " + error.getMessage());
                        Toast.makeText(EditDataCustomer.this, "Gagal memperbarui data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Buat parameter untuk request
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("nama", nama);
                params.put("phone", nomorHp);
                params.put("addres", alamat);
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
