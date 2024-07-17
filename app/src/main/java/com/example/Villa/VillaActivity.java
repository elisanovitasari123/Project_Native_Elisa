package com.example.Villa;

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
import com.example.mobilehotelgroup2.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VillaActivity extends AppCompatActivity implements AdaptorVilla.DataChangeListener {
    private static final int REQUEST_CODE_SIMPAN_DATA = 1;
    private static final int EDIT_DATA_REQUEST = 2;
    private static final int RELOAD_DELAY_MS = 1000; // 1 second delay
    private ListView listViewVilla;
    private ProgressBar progressBarVilla;
    private ArrayList<GetDataVilla> modelVilla;
    private AdaptorVilla adaptorVilla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_villa);

        listViewVilla = findViewById(R.id.listVilla);
        progressBarVilla = findViewById(R.id.progressBarVilla);

        FloatingActionButton fab = findViewById(R.id.fabVilla);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(VillaActivity.this, SimpanDataVilla.class);
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
        String url = new ConfigurasiVilla().baseUrl() + "list_villa.php";
        if (progressBarVilla != null) {
            progressBarVilla.setVisibility(View.VISIBLE);
        }

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                response -> {
                    if (progressBarVilla != null) {
                        progressBarVilla.setVisibility(View.GONE);
                    }
                    Log.d("VillaActivity", "Response: " + response);

                    try {
                        // Pastikan respons adalah JSON
                        JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.has("data")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            modelVilla = new ArrayList<>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject getDataVilla = jsonArray.getJSONObject(i);
                                modelVilla.add(new GetDataVilla(
                                        getDataVilla.getString("id_villa"),
                                        getDataVilla.getString("namaVilla"),
                                        getDataVilla.getString("kontak"),
                                        getDataVilla.getString("email"),
                                        getDataVilla.getString("lokasi")
                                ));
                            }

                            adaptorVilla = new AdaptorVilla(VillaActivity.this, modelVilla, progressBarVilla, VillaActivity.this);
                            listViewVilla.setAdapter(adaptorVilla);
                            Log.d("VillaActivity", "Data loaded successfully");
                        } else {
                            Log.e("VillaActivity", "No 'data' key in JSON response");
                            Toast.makeText(VillaActivity.this, "Error: 'data' key missing in response", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Log.e("RoomActivity", "JSON Parsing error: " + e.getMessage());
                        Toast.makeText(VillaActivity.this, "Error parsing data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                },
                error -> {
                    if (progressBarVilla != null) {
                        progressBarVilla.setVisibility(View.GONE);
                    }
                    Log.e("Villactivity", "Volley error: " + error.getMessage());
                    Toast.makeText(VillaActivity.this, "Error loading data", Toast.LENGTH_SHORT).show();
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    @Override
    public void onDataChanged() {
        Log.d("VillaActivity", "Data change detected, reloading data");
        reloadData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == REQUEST_CODE_SIMPAN_DATA || requestCode == EDIT_DATA_REQUEST) && resultCode == RESULT_OK && data != null) {
            boolean refresh = data.getBooleanExtra("refreshflag", false);
            if (refresh) {
                Log.d("VillaActivity", "Refresh flag received, reloading data...");
                new Handler().postDelayed(this::loadData, RELOAD_DELAY_MS);
            }
        }
    }

    private void reloadData() {
        loadData();
    }
}
