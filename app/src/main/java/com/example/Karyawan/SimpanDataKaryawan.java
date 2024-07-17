package com.example.Karyawan;

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
import com.example.Room.ConfigurasiRoom;
import com.example.mobilehotelgroup2.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SimpanDataKaryawan extends AppCompatActivity {

    private EditText editTextNamaKaryawan, editTextNoHp, editTextAlamat;
    private Button buttonSave;
    private static final String TAG = "SimpanDataKaryawan"; // Tag untuk logging

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_karyawan);

        editTextNamaKaryawan = findViewById(R.id.namaKaryawan);
        editTextNoHp = findViewById(R.id.no_hp);
        editTextAlamat = findViewById(R.id.alamat);
        buttonSave = findViewById(R.id.btnSaveK);

        buttonSave.setOnClickListener(v -> {
            Log.d(TAG, "Save button clicked");
            saveData();
        });
    }

    private void saveData() {
        final String nama_karyawan = editTextNamaKaryawan.getText().toString().trim();
        final String no_hp = editTextNoHp.getText().toString().trim();
        final String alamat = editTextAlamat.getText().toString().trim();

        if (nama_karyawan.isEmpty() || no_hp.isEmpty() || alamat.isEmpty()) {
            Toast.makeText(SimpanDataKaryawan.this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = new ConfigurasiKaryawan().baseUrl() + "create_karyawan.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response: " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if ("success".equals(status)) {
                                Toast.makeText(SimpanDataKaryawan.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(SimpanDataKaryawan.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "Error parsing JSON: " + e.getMessage());
                            Toast.makeText(SimpanDataKaryawan.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error: " + error.getMessage());
                        Toast.makeText(SimpanDataKaryawan.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nama_karyawan", nama_karyawan);
                params.put("no_hp", no_hp);
                params.put("alamat", alamat);

                Log.d(TAG, "Params: " + params.toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}
