package com.example.K_Fasilitas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.K_Fasilitas.ConfigurasiKFasilitas;
import com.example.Fasilitas.ConfigurasiFasilitas;
import com.example.Room.ConfigurasiRoom;
import com.example.Villa.ConfigurasiVilla;
import com.example.mobilehotelgroup2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SimpanDataKFasilitas extends AppCompatActivity {

    private EditText namaVilla, nama_fasilitas, namaKamar, status_fasilitas;
    private Button saveK;
    private Spinner spinnerNamaKamar, spinnerNamaFasilitas, spinnerNamaVilla;
    private ArrayList<String> namaListVilla, namaListRoom, namaListFasilitas;
    private Map<String, String> namaToIdMapVilla, namaToIdMapRoom, namaToIdMapFasilitas;
    private ArrayAdapter<String> adaptorVilla, adaptorNamaKamar, adaptorFasilitas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_k_f_spinner);

        // Initialize UI components
        namaVilla = findViewById(R.id.namaVilla);
        nama_fasilitas = findViewById(R.id.nama_fasilitas);
        namaKamar = findViewById(R.id.namaKamar);
        status_fasilitas = findViewById(R.id.status_fasilitas);
        spinnerNamaVilla = findViewById(R.id.villaSpinner);
        spinnerNamaFasilitas = findViewById(R.id.fasilitasSpinner);
        spinnerNamaKamar = findViewById(R.id.spinner_namaKamar);
        saveK = findViewById(R.id.saveK);

        // Initialize lists and maps
        namaListVilla = new ArrayList<>();
        namaToIdMapVilla = new HashMap<>();
        fetchNamaDataVilla();

        namaListFasilitas = new ArrayList<>();
        namaToIdMapFasilitas = new HashMap<>();
        fetchNamaDataFasilitas();

        namaListRoom = new ArrayList<>();
        namaToIdMapRoom = new HashMap<>();
        fetchNamaDataRoom();

        // Set item selected listeners for spinners
        setSpinnerListeners();

        // Set listener for save button
        saveK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpanDataKFasilitas();
            }
        });
    }

    private void setSpinnerListeners() {
        // Spinner item selected listener for namaVilla
        spinnerNamaVilla.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != 0) {
                    String item = adapterView.getItemAtPosition(position).toString();
                    namaVilla.setText(item); // Set the selected item in the EditText
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
            }
        });

        // Spinner item selected listener for namaKamar
        spinnerNamaKamar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != 0) {
                    String item = adapterView.getItemAtPosition(position).toString();
                    namaKamar.setText(item); // Set the selected item in the EditText
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
            }
        });

        // Spinner item selected listener for namaFasilitas
        spinnerNamaFasilitas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != 0) {
                    String item = adapterView.getItemAtPosition(position).toString();
                    nama_fasilitas.setText(item); // Set the selected item in the EditText
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
            }
        });
    }

    private void fetchNamaDataVilla() {
        String url = new ConfigurasiVilla().baseUrl() + "get_villa.php";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("villa");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String id_villa = jsonObject.optString("id_villa");
                                String NamaVilla = jsonObject.optString("namavilla");
                                Log.d("FetchNamaDataVilla", "ID: " + id_villa + ", Nama Villa: " + NamaVilla);
                                namaListVilla.add(NamaVilla);
                                namaToIdMapVilla.put(NamaVilla, id_villa);
                            }
                            adaptorVilla = new ArrayAdapter<>(SimpanDataKFasilitas.this,
                                    android.R.layout.simple_spinner_item, namaListVilla);
                            adaptorVilla.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerNamaVilla.setAdapter(adaptorVilla);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("FetchNamaDataVilla", "JSON parsing error: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("FetchNamaDataVilla", "Volley error: " + error.getMessage());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    private void fetchNamaDataFasilitas() {
        String url = new ConfigurasiFasilitas().baseUrl() + "get_fasilitas.php";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("fasilitas");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String id_fasilitas = jsonObject.optString("id_fasilitas");
                                String namaFasilitas = jsonObject.optString("nama_fasilitas");
                                Log.d("FetchNamaDataFasilitas", "ID: " + id_fasilitas + ", Nama Fasilitas: " + namaFasilitas);
                                namaListFasilitas.add(namaFasilitas);
                                namaToIdMapFasilitas.put(namaFasilitas, id_fasilitas);
                            }
                            adaptorFasilitas = new ArrayAdapter<>(SimpanDataKFasilitas.this,
                                    android.R.layout.simple_spinner_item, namaListFasilitas);
                            adaptorFasilitas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerNamaFasilitas.setAdapter(adaptorFasilitas);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("FetchNamaDataFasilitas", "JSON parsing error: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("FetchNamaDataFasilitas", "Volley error: " + error.getMessage());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    private void fetchNamaDataRoom() {
        String url = new ConfigurasiRoom().baseUrl() + "get_room.php";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("room");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String roomid = jsonObject.optString("roomid");
                                String namaKamar = jsonObject.optString("namaKamar");
                                Log.d("FetchNamaDataRoom", "ID: " + roomid + ", Nama Kamar: " + namaKamar);
                                namaListRoom.add(namaKamar);
                                namaToIdMapRoom.put(namaKamar, roomid);
                            }
                            adaptorNamaKamar = new ArrayAdapter<>(SimpanDataKFasilitas.this,
                                    android.R.layout.simple_spinner_item, namaListRoom);
                            adaptorNamaKamar.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerNamaKamar.setAdapter(adaptorNamaKamar);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("FetchNamaDataRoom", "JSON parsing error: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("FetchNamaDataRoom", "Volley error: " + error.getMessage());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    private void simpanDataKFasilitas() {
        final String NamaVilla = namaVilla.getText().toString().trim();
        final String NamaFasilitas = nama_fasilitas.getText().toString().trim();
        final String NamaKamar = namaKamar.getText().toString().trim();
        final String StatusFasilitas = status_fasilitas.getText().toString().trim();

        if (NamaVilla.isEmpty() || NamaFasilitas.isEmpty() || NamaKamar.isEmpty() || StatusFasilitas.isEmpty()) {
            Toast.makeText(SimpanDataKFasilitas.this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }

//        String idVilla = namaToIdMapVilla.get(NamaVilla);
//        if (idVilla == null) {
//            Toast.makeText(SimpanDataKFasilitas.this, "Nama villa tidak ditemukan", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        String idFasilitas = namaToIdMapFasilitas.get(NamaFasilitas);
//        if (idFasilitas == null) {
//            Toast.makeText(SimpanDataKFasilitas.this, "Nama fasilitas tidak ditemukan", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        String idRoom = namaToIdMapRoom.get(NamaKamar);
//        if (idRoom == null) {
//            Toast.makeText(SimpanDataKFasilitas.this, "Nama kamar tidak ditemukan", Toast.LENGTH_SHORT).show();
//            return;
//        }

        String url = new ConfigurasiKFasilitas().baseUrl() + "create_k_fasilitas.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("SimpanDataKFasilitas", "Response: " + response);
                        if (response.equalsIgnoreCase("duplicate")) {
                            Toast.makeText(SimpanDataKFasilitas.this, "Data sudah ada!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SimpanDataKFasilitas.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                            sendRefreshFlag(true);
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("SimpanDataKFasilitas", "Error: " + error.getMessage());
                        Toast.makeText(SimpanDataKFasilitas.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("namaVilla", NamaVilla);
                params.put("nama_fasilitas", NamaFasilitas);
                params.put("namaKamar", NamaKamar);
                params.put("status_fasilitas", StatusFasilitas);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void sendRefreshFlag(boolean refresh) {
        Intent intent = new Intent();
        intent.putExtra("refreshflag", refresh);
        setResult(RESULT_OK, intent);
    }
}
