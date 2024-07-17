package com.example.Karyawan;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Karyawan.AdaptorKaryawan;
import com.example.Karyawan.ConfigurasiKaryawan;
import com.example.Karyawan.GetDataKaryawan;
import com.example.Karyawan.EditDataKaryawan;
import com.example.mobilehotelgroup2.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class KaryawanActivity extends AppCompatActivity implements AdaptorKaryawan.DataChangeListener {
    private static final int REQUEST_CODE_SIMPAN_DATA = 1;
    private static final int EDIT_DATA_REQUEST = 2;
    private static final int RELOAD_DELAY_MS = 1000; // 1 second delay
    private ListView listViewKaryawan;
    private ProgressBar progressBarKaryawan;
    private ArrayList<GetDataKaryawan> modelKaryawan;
    private AdaptorKaryawan adaptorKaryawan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karyawan);

        listViewKaryawan = findViewById(R.id.namaKaryawan);
        progressBarKaryawan = findViewById(R.id.progressBarKaryawan);

        FloatingActionButton fab = findViewById(R.id.fabK);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(KaryawanActivity.this, SimpanDataKaryawan.class);
            startActivityForResult(intent, REQUEST_CODE_SIMPAN_DATA);
        });

        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        String url = new ConfigurasiKaryawan().baseUrl() + "list_karyawan.php";
        if (progressBarKaryawan != null) {
            progressBarKaryawan.setVisibility(View.VISIBLE);
        }

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                response -> {
                    if (progressBarKaryawan != null) {
                        progressBarKaryawan.setVisibility(View.GONE);
                    }
                    Log.d("KaryawanActivity", "Response: " + response);

                    try {
                        // Pastikan respons adalah JSON
                        JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.has("data")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            modelKaryawan = new ArrayList<>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject getDataKaryawan = jsonArray.getJSONObject(i);
                                modelKaryawan.add(new GetDataKaryawan(
                                        getDataKaryawan.getString("id_karyawan"),
                                        getDataKaryawan.getString("nama_karyawan"),
                                        getDataKaryawan.getString("no_hp"),
                                        getDataKaryawan.getString("alamat")
                                ));
                            }

                            adaptorKaryawan = new AdaptorKaryawan(KaryawanActivity.this, modelKaryawan, progressBarKaryawan, KaryawanActivity.this);
                            listViewKaryawan.setAdapter(adaptorKaryawan);
                            Log.d("Karyawanctivity", "Data loaded successfully");
                        } else {
                            Log.e("KaryawanActivity", "No 'data' key in JSON response");
                            Toast.makeText(KaryawanActivity.this, "Error: 'data' key missing in response", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Log.e("KaryawanActivity", "JSON Parsing error: " + e.getMessage());
                        Toast.makeText(KaryawanActivity.this, "Error parsing data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                },
                error -> {
                    if (progressBarKaryawan != null) {
                        progressBarKaryawan.setVisibility(View.GONE);
                    }
                    Log.e("KaryawanActivity", "Volley error: " + error.getMessage());
                    Toast.makeText(KaryawanActivity.this, "Error loading data", Toast.LENGTH_SHORT).show();
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    @Override
    public void onDataChanged() {
        Log.d("KaryawanActivity", "Data change detected, reloading data");
        reloadData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == REQUEST_CODE_SIMPAN_DATA || requestCode == EDIT_DATA_REQUEST) && resultCode == RESULT_OK && data != null) {
            boolean refresh = data.getBooleanExtra("refreshflag", false);
            if (refresh) {
                Log.d("KaryawanActivity", "Refresh flag received, reloading data...");
                new Handler().postDelayed(this::loadData, RELOAD_DELAY_MS);
            }
        }
    }

    private void reloadData() {
        loadData();
    }
}
