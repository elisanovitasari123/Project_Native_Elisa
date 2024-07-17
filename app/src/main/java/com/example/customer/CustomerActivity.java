package com.example.customer;

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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mobilehotelgroup2.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CustomerActivity extends AppCompatActivity implements Adaptor.DataChangeListener {
    private static final int REQUEST_CODE_SIMPAN_DATA = 1;
    private static final int EDIT_DATA_REQUEST = 2;
    private static final int RELOAD_DELAY_MS = 1000; // 1 second delay
    private ListView listView;
    private ProgressBar progressBar;
    private ArrayList<GetData> model;
    private Adaptor adaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        listView = findViewById(R.id.list);
        progressBar = findViewById(R.id.progressBar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(CustomerActivity.this, SimpanDataCustomer.class);
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
        String url = new Configurasi().baseUrl() + "listdatacustomer.php";
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                response -> {
                    if (progressBar != null) {
                        progressBar.setVisibility(View.GONE);
                    }
                    Log.d("CustomerActivity", "Response: " + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        model = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject getData = jsonArray.getJSONObject(i);
                            model.add(new GetData(
                                    getData.getString("id"),
                                    getData.getString("nama"),
                                    getData.getString("phone"),
                                    getData.getString("addres")
                            ));
                        }

                        adaptor = new Adaptor(CustomerActivity.this, model, progressBar, CustomerActivity.this);
                        listView.setAdapter(adaptor);
                        Log.d("CustomerActivity", "Data loaded successfully");

                    } catch (JSONException e) {
                        Log.e("CustomerActivity", "JSON Parsing error: " + e.getMessage());
                        Toast.makeText(CustomerActivity.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                        reloadData();
                    }
                },
                error -> {
                    if (progressBar != null) {
                        progressBar.setVisibility(View.GONE);
                    }
                    Log.e("CustomerActivity", "Volley error: " + error.getMessage());
                    Toast.makeText(CustomerActivity.this, "Terjadi kesalahan saat memuat data", Toast.LENGTH_SHORT).show();
                    reloadData();
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    @Override
    public void onDataChanged() {
        Log.d("CustomerActivity", "Data change detected, reloading data");
        reloadData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == REQUEST_CODE_SIMPAN_DATA || requestCode == EDIT_DATA_REQUEST) && resultCode == RESULT_OK && data != null) {
            boolean refresh = data.getBooleanExtra("refreshflag", false);
            if (refresh) {
                Log.d("CustomerActivity", "Refresh flag received, reloading data...");
                new Handler().postDelayed(() -> loadData(), RELOAD_DELAY_MS);
            }
        }
    }

    private void reloadData() {
        loadData();
    }
}
