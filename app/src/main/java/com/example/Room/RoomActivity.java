package com.example.Room;

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
import com.example.Villa.AdaptorVilla;
import com.example.mobilehotelgroup2.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RoomActivity extends AppCompatActivity implements AdaptorRoom .DataChangeListener {
    private static final int REQUEST_CODE_SIMPAN_DATA = 1;
    private static final int EDIT_DATA_REQUEST = 2;
    private static final int RELOAD_DELAY_MS = 1000; // 1 second delay
    private ListView listViewRoom;
    private ProgressBar progressBarRoom;
    private ArrayList<GetDataRoom> modelRoom;
    private AdaptorRoom adaptorRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        listViewRoom = findViewById(R.id.listRoom);
        progressBarRoom = findViewById(R.id.progressBarRoom);

        FloatingActionButton fab = findViewById(R.id.fabRoom);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(RoomActivity.this, SimpanDataRoom.class);
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
        String url = new ConfigurasiRoom().baseUrl() + "list_room.php";
        if (progressBarRoom != null) {
            progressBarRoom.setVisibility(View.VISIBLE);
        }

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                response -> {
                    if (progressBarRoom != null) {
                        progressBarRoom.setVisibility(View.GONE);
                    }
                    Log.d("RoomActivity", "Response: " + response);

                    try {
                        // Pastikan respons adalah JSON
                        JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.has("data")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            modelRoom = new ArrayList<>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject getDataRoom = jsonArray.getJSONObject(i);
                                modelRoom.add(new GetDataRoom(
                                        getDataRoom.getString("roomid"),
                                        getDataRoom.getString("namaKamar"),
                                        getDataRoom.getString("tipeKamar"),
                                        getDataRoom.getString("harga")
                                ));
                            }

                            adaptorRoom = new AdaptorRoom(RoomActivity.this, modelRoom, progressBarRoom, RoomActivity.this);
                            listViewRoom.setAdapter(adaptorRoom);
                            Log.d("RoomActivity", "Data loaded successfully");
                        } else {
                            Log.e("RoomActivity", "No 'data' key in JSON response");
                            Toast.makeText(RoomActivity.this, "Error: 'data' key missing in response", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Log.e("RoomActivity", "JSON Parsing error: " + e.getMessage());
                        Toast.makeText(RoomActivity.this, "Error parsing data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                },
                error -> {
                    if (progressBarRoom != null) {
                        progressBarRoom.setVisibility(View.GONE);
                    }
                    Log.e("RoomActivity", "Volley error: " + error.getMessage());
                    Toast.makeText(RoomActivity.this, "Error loading data", Toast.LENGTH_SHORT).show();
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    @Override
    public void onDataChanged() {
        Log.d("RoomActivity", "Data change detected, reloading data");
        reloadData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == REQUEST_CODE_SIMPAN_DATA || requestCode == EDIT_DATA_REQUEST) && resultCode == RESULT_OK && data != null) {
            boolean refresh = data.getBooleanExtra("refreshflag", false);
            if (refresh) {
                Log.d("RoomActivity", "Refresh flag received, reloading data...");
                new Handler().postDelayed(this::loadData, RELOAD_DELAY_MS);
            }
        }
    }

    private void reloadData() {
        loadData();
    }
}
