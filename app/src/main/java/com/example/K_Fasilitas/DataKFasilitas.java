package com.example.K_Fasilitas;


import android.annotation.SuppressLint;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.K_Fasilitas.AdaptorKFasilitas;
import com.example.K_Fasilitas.ConfigurasiKFasilitas;
import com.example.K_Fasilitas.GetDataKFasilitas;
import com.example.K_Fasilitas.SimpanDataKFasilitas;
import com.example.mobilehotelgroup2.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DataKFasilitas extends AppCompatActivity implements AdaptorKFasilitas.DataChangeListener {
    private static final int REQUEST_CODE_SIMPAN_DATA = 1;
    private static final int EDIT_DATA_REQUEST = 2;
    private static final int RELOAD_DELAY_MS = 3000;
    private ListView listView;
    private ProgressBar progressBar;
    private ArrayList<GetDataKFasilitas> model;
    private AdaptorKFasilitas adaptor;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_k_fasilitas);

        listView = findViewById(R.id.listK);
        progressBar = findViewById(R.id.progressBarK);

        FloatingActionButton fab = findViewById(R.id.fabK);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DataKFasilitas.this, SimpanDataKFasilitas.class);
                startActivityForResult(intent, REQUEST_CODE_SIMPAN_DATA);
            }
        });

        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        String url = new ConfigurasiKFasilitas().baseUrl() + "list_k_fasilitas.php";
        progressBar.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            model = new ArrayList<>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject getData = jsonArray.getJSONObject(i);
                                model.add(new GetDataKFasilitas(
                                        getData.getString("id_k_fasilitas"),
                                        getData.getString("namaVilla"),
                                        getData.getString("nama_fasilitas"),
                                        getData.getString("namaKamar"),
                                        getData.getString("status_fasilitas")
                                ));
                            }

                            adaptor = new AdaptorKFasilitas(DataKFasilitas.this, model, progressBar, DataKFasilitas.this);
                            listView.setAdapter(adaptor);
                            adaptor.notifyDataSetChanged();

                            Log.d("DataKFasilitas", "Data loaded successfully");

                        } catch (JSONException e) {
                            Log.e("DataKFasilitas", "JSON Parsing error: " + e.getMessage());
                            Toast.makeText(DataKFasilitas.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                            reloadData();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressBar.setVisibility(View.GONE);
                        Log.e("DataKFasilitas", "Volley error: " + volleyError.getMessage());
                        Toast.makeText(DataKFasilitas.this, "Terjadi kesalahan saat memuat data", Toast.LENGTH_SHORT).show();
                        reloadData();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    @Override
    public void onDataChanged() {
        Log.d("DataKFasilitas", "Data change detected, reloading data");
        reloadData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SIMPAN_DATA || requestCode == EDIT_DATA_REQUEST) {
            if (resultCode == RESULT_OK && data != null) {
                boolean refresh = data.getBooleanExtra("refreshflag", false);
                if (refresh) {
                    Log.d("DataKFasilitas", "Refresh flag received, reloading data...");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadData();
                        }
                    }, RELOAD_DELAY_MS);
                }
            }
        }
    }

    private void reloadData() {
        loadData();
    }
}
