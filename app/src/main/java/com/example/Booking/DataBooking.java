package com.example.Booking;


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

import com.example.Booking.ConfigurasiBooking;
import com.example.mobilehotelgroup2.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DataBooking extends AppCompatActivity implements AdaptorBooking.DataChangeListener {
    private static final int REQUEST_CODE_SIMPAN_DATA = 1;
    private static final int EDIT_DATA_REQUEST = 2;
    private static final int RELOAD_DELAY_MS = 3000;
    private ListView listView;
    private ProgressBar progressBar;
    private ArrayList<GetDataBooking> model;
    private AdaptorBooking adaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        listView = findViewById(R.id.listBooking);
        progressBar = findViewById(R.id.progressBarBooking);

        FloatingActionButton fab = findViewById(R.id.fabBooking);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DataBooking.this, SimpanDataBooking.class);
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
        String url = new ConfigurasiBooking().baseUrl() + "list_booking.php";
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
                                model.add(new GetDataBooking(
                                        getData.getString("booking_id"),
                                        getData.getString("nama"),
                                        getData.getString("namaKamar"),
                                        getData.getString("check_in_date"),
                                        getData.getString("check_out_date"),
                                        getData.getInt("total_price")
                                ));
                            }

                            adaptor = new AdaptorBooking(DataBooking.this, model, progressBar, DataBooking.this);
                            listView.setAdapter(adaptor);
                            adaptor.notifyDataSetChanged();

                            Log.d("DataBooking", "Data loaded successfully");

                        } catch (JSONException e) {
                            Log.e("DataBooking", "JSON Parsing error: " + e.getMessage());
                            Toast.makeText(DataBooking.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                            reloadData();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressBar.setVisibility(View.GONE);
                        Log.e("DataBooking", "Volley error: " + volleyError.getMessage());
                        Toast.makeText(DataBooking.this, "Terjadi kesalahan saat memuat data", Toast.LENGTH_SHORT).show();
                        reloadData();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    @Override
    public void onDataChanged() {
        Log.d("DataBooking", "Data change detected, reloading data");
        reloadData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SIMPAN_DATA || requestCode == EDIT_DATA_REQUEST) {
            if (resultCode == RESULT_OK && data != null) {
                boolean refresh = data.getBooleanExtra("refreshflag", false);
                if (refresh) {
                    Log.d("DataBooking", "Refresh flag received, reloading data...");
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
