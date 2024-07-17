package com.example.Fasilitas;

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

public class FasilitasActivity extends AppCompatActivity implements AdaptorFasilitas.DataChangeListener {
    private static final int REQUEST_CODE_SIMPAN_DATA = 1;
    private static final int EDIT_DATA_REQUEST = 2;
    private static final int RELOAD_DELAY_MS = 1000; // 1 second delay
    private ListView listViewFasilitas;
    private ProgressBar progressBarFasilitas;
    private ArrayList<GetDataFasilitas> modelFasilitas;
    private AdaptorFasilitas adaptorFasilitas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fasilitas);

        listViewFasilitas = findViewById(R.id.listFasilitas);
        progressBarFasilitas = findViewById(R.id.progressBarFasilitas);

        FloatingActionButton fab = findViewById(R.id.fabFasilitas);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(FasilitasActivity.this, SimpanDataFasilitas.class);
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
        String url = new ConfigurasiFasilitas().baseUrl() + "list_fasilitas.php";
        if (progressBarFasilitas != null) {
            progressBarFasilitas.setVisibility(View.VISIBLE);
        }

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                response -> {
                    if (progressBarFasilitas != null) {
                        progressBarFasilitas.setVisibility(View.GONE);
                    }
                    Log.d("FasilitasActivity", "Response: " + response);

                    try {
                        // Pastikan respons adalah JSON
                        JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.has("data")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            modelFasilitas = new ArrayList<>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject getDataFailitas= jsonArray.getJSONObject(i);
                                modelFasilitas.add(new GetDataFasilitas(
                                        getDataFailitas.getString("id_fasilitas"),
                                        getDataFailitas.getString("nama_fasilitas"),
                                        getDataFailitas.getString("kategori")
                                ));
                            }

                            adaptorFasilitas = new AdaptorFasilitas(FasilitasActivity.this, modelFasilitas, progressBarFasilitas, FasilitasActivity.this);
                            listViewFasilitas.setAdapter(adaptorFasilitas);
                            Log.d("FasilitasActivity", "Data loaded successfully");
                        } else {
                            Log.e("FasilitasActivity", "No 'data' key in JSON response");
                            Toast.makeText(FasilitasActivity.this, "Error: 'data' key missing in response", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Log.e("FasilitasActivity", "JSON Parsing error: " + e.getMessage());
                        Toast.makeText(FasilitasActivity.this, "Error parsing data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                },
                error -> {
                    if (progressBarFasilitas != null) {
                        progressBarFasilitas.setVisibility(View.GONE);
                    }
                    Log.e("FasilitasActivity", "Volley error: " + error.getMessage());
                    Toast.makeText(FasilitasActivity.this, "Error loading data", Toast.LENGTH_SHORT).show();
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    @Override
    public void onDataChanged() {
        Log.d("FasilitasActivity", "Data change detected, reloading data");
        reloadData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == REQUEST_CODE_SIMPAN_DATA || requestCode == EDIT_DATA_REQUEST) && resultCode == RESULT_OK && data != null) {
            boolean refresh = data.getBooleanExtra("refreshflag", false);
            if (refresh) {
                Log.d("FasilitasActivity", "Refresh flag received, reloading data...");
                new Handler().postDelayed(this::loadData, RELOAD_DELAY_MS);
            }
        }
    }

    private void reloadData() {
        loadData();
    }
}
