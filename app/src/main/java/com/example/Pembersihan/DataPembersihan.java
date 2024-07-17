package com.example.Pembersihan;


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
import com.example.Pembersihan.AdaptorPembersihan;
import com.example.Pembersihan.ConfigurasiPembersihan;
import com.example.Pembersihan.GetDataPembersihan;
import com.example.Booking.SimpanDataBooking;
import com.example.mobilehotelgroup2.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DataPembersihan extends AppCompatActivity implements AdaptorPembersihan.DataChangeListener {
    private static final int REQUEST_CODE_SIMPAN_DATA = 1;
    private static final int EDIT_DATA_REQUEST = 2;
    private static final int RELOAD_DELAY_MS = 3000;
    private ListView listView;
    private ProgressBar progressBar;
    private ArrayList<GetDataPembersihan> model;
    private AdaptorPembersihan adaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembersihan);

        listView = findViewById(R.id.listP);
        progressBar = findViewById(R.id.progressBarP);

        FloatingActionButton fab = findViewById(R.id.fabP);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DataPembersihan.this, SimpanDataPembersihan.class);
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
        String url = new ConfigurasiPembersihan().baseUrl() + "list_pembersihan.php";
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
                                model.add(new GetDataPembersihan(
                                        getData.getString("id_p"),
                                        getData.getString("namaKamar"),
                                        getData.getString("nama_karyawan"),
                                        getData.getString("tanggal"),
                                        getData.getString("deskripsi")
                                ));
                            }

                            adaptor = new AdaptorPembersihan(DataPembersihan.this, model, progressBar, DataPembersihan.this);
                            listView.setAdapter(adaptor);
                            adaptor.notifyDataSetChanged();

                            Log.d("DataPembersihan", "Data loaded successfully");

                        } catch (JSONException e) {
                            Log.e("DataPembersihan", "JSON Parsing error: " + e.getMessage());
                            Toast.makeText(DataPembersihan.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                            reloadData();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressBar.setVisibility(View.GONE);
                        Log.e("DataPembersihan", "Volley error: " + volleyError.getMessage());
                        Toast.makeText(DataPembersihan.this, "Terjadi kesalahan saat memuat data", Toast.LENGTH_SHORT).show();
                        reloadData();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    @Override
    public void onDataChanged() {
        Log.d("DataPembersihan", "Data change detected, reloading data");
        reloadData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SIMPAN_DATA || requestCode == EDIT_DATA_REQUEST) {
            if (resultCode == RESULT_OK && data != null) {
                boolean refresh = data.getBooleanExtra("refreshflag", false);
                if (refresh) {
                    Log.d("DataPembersihan", "Refresh flag received, reloading data...");
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
