package com.example.Villa;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Villa.ConfigurasiVilla;
import com.example.mobilehotelgroup2.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SimpanDataVilla extends AppCompatActivity {

    private EditText editTextNamaVilla, editTextKontak, editTextEmail, editTextLokasi;
    private Button buttonSave;
    private static final String TAG = "SimpanDataVilla"; // Tag untuk logging

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_villa);

        editTextNamaVilla = findViewById(R.id.namaVilla);
        editTextKontak = findViewById(R.id.kontak);
        editTextEmail= findViewById(R.id.email);
        editTextLokasi= findViewById(R.id.lokasi);
        buttonSave = findViewById(R.id.btnSaveVilla);

        buttonSave.setOnClickListener(v -> {
            Log.d(TAG, "Save button clicked");
            saveData();
        });
    }

    private void saveData() {
        final String namaVilla = editTextNamaVilla.getText().toString().trim();
        final String kontak = editTextKontak.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String lokasi = editTextLokasi.getText().toString().trim();

        if (namaVilla.isEmpty() || kontak.isEmpty() || email.isEmpty() || lokasi.isEmpty()) {
            Toast.makeText(SimpanDataVilla.this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = new ConfigurasiVilla().baseUrl() + "create_villa.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response: " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if ("success".equals(status)) {
                                Toast.makeText(SimpanDataVilla.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(SimpanDataVilla.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "Error parsing JSON: " + e.getMessage());
                            Toast.makeText(SimpanDataVilla.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error: " + error.getMessage());
                        Toast.makeText(SimpanDataVilla.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("namaVilla", namaVilla);
                params.put("kontak", kontak);
                params.put("email", email);
                params.put("lokasi", lokasi);

                Log.d(TAG, "Params: " + params.toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}
