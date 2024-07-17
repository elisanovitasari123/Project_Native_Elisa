package com.example.Fasilitas;

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

public class SimpanDataFasilitas extends AppCompatActivity {

    private EditText editTextNamaFasilitas, editTextKategori;
    private Button buttonSave;
    private static final String TAG = "SimpanDataFasilitas"; // Tag untuk logging

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_fasilitas);

        editTextNamaFasilitas = findViewById(R.id.namaFasilitas);
        editTextKategori = findViewById(R.id.kategori);
        buttonSave = findViewById(R.id.btnSaveFasilitas);

        buttonSave.setOnClickListener(v -> {
            Log.d(TAG, "Save button clicked");
            saveData();
        });
    }

    private void saveData() {
        final String nama_fasilitas = editTextNamaFasilitas.getText().toString().trim();
        final String kategori = editTextKategori.getText().toString().trim();


        if (nama_fasilitas.isEmpty() || kategori.isEmpty()) {
            Toast.makeText(SimpanDataFasilitas.this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = new ConfigurasiFasilitas().baseUrl() + "create_fasilitas.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response: " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if ("success".equals(status)) {
                                Toast.makeText(SimpanDataFasilitas.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(SimpanDataFasilitas.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "Error parsing JSON: " + e.getMessage());
                            Toast.makeText(SimpanDataFasilitas.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error: " + error.getMessage());
                        Toast.makeText(SimpanDataFasilitas.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nama_fasilitas", nama_fasilitas);
                params.put("kategori", kategori);

                Log.d(TAG, "Params: " + params.toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}
