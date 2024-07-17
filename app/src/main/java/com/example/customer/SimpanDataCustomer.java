package com.example.customer;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mobilehotelgroup2.R;

import java.util.HashMap;
import java.util.Map;

public class SimpanDataCustomer extends AppCompatActivity {

    private EditText editTextNama, editTextAlamat, editTextNomorHp;
    private Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_customer);

        editTextNama = findViewById(R.id.nama);
        editTextNomorHp = findViewById(R.id.phone);
        editTextAlamat = findViewById(R.id.addres);
        buttonSave = findViewById(R.id.btnSave);

        buttonSave.setOnClickListener(v -> {
            Log.d("SimpanDataCustomer", "Save button clicked");
            saveData();
        });
    }

    private void saveData() {
        final String nama = editTextNama.getText().toString().trim();
        final String phone = editTextNomorHp.getText().toString().trim();
        final String alamat = editTextAlamat.getText().toString().trim();

        if (nama.isEmpty() || alamat.isEmpty() || phone.isEmpty()) {
            Toast.makeText(SimpanDataCustomer.this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = new Configurasi().baseUrl() + "tambahcustomer.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("SimpanDataCustomer", "Response: " + response);
                    Toast.makeText(SimpanDataCustomer.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                    finish();
                },
                error -> {
                    Log.e("SimpanDataCustomer", "Error: " + error.getMessage());
                    Toast.makeText(SimpanDataCustomer.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nama", nama);
                params.put("phone", phone);
                params.put("addres", alamat);

                Log.d("SimpanDataCustomer", "Params: " + params.toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}
