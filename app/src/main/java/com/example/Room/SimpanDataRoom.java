package com.example.Room;

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
import com.example.mobilehotelgroup2.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SimpanDataRoom extends AppCompatActivity {

    private EditText editTextNamakamar, editTextTipeKamar, editTextHarga;
    private Button buttonSave;
    private static final String TAG = "SimpanDataRoom"; // Tag untuk logging

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_room);

        editTextNamakamar = findViewById(R.id.namaRoom);
        editTextTipeKamar = findViewById(R.id.tipeRoom);
        editTextHarga = findViewById(R.id.harga);
        buttonSave = findViewById(R.id.btnSaveRoom);

        buttonSave.setOnClickListener(v -> {
            Log.d(TAG, "Save button clicked");
            saveData();
        });
    }

    private void saveData() {
        final String namaKamar = editTextNamakamar.getText().toString().trim();
        final String tipeKamar = editTextTipeKamar.getText().toString().trim();
        final String harga = editTextHarga.getText().toString().trim();

        if (namaKamar.isEmpty() || tipeKamar.isEmpty() || harga.isEmpty()) {
            Toast.makeText(SimpanDataRoom.this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = new ConfigurasiRoom().baseUrl() + "create_room.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response: " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if ("success".equals(status)) {
                                Toast.makeText(SimpanDataRoom.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(SimpanDataRoom.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "Error parsing JSON: " + e.getMessage());
                            Toast.makeText(SimpanDataRoom.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error: " + error.getMessage());
                        Toast.makeText(SimpanDataRoom.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("namaKamar", namaKamar);
                params.put("tipeKamar", tipeKamar);
                params.put("harga", harga);

                Log.d(TAG, "Params: " + params.toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}
