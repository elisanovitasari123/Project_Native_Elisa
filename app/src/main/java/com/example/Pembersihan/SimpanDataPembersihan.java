package com.example.Pembersihan;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.Booking.ConfigurasiBooking;
import com.example.Karyawan.ConfigurasiKaryawan;
import com.example.Room.ConfigurasiRoom;
import com.example.customer.Configurasi;
import com.example.mobilehotelgroup2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SimpanDataPembersihan extends AppCompatActivity {

    private EditText namaKamar, nama_karyawan, tanggal, deskripsi;
    private Button saveBooking;
    private Spinner spinnerNamaKamar, spinnerNamaKaryawan;
    private ArrayList<String> namaList, namaListRoom;
    private Map<String, String> namaToIdMap, namaToIdMapRoom;
    private ArrayAdapter<String> adaptor, adaptorNamaKamar;
    private ImageView gambar_check_in;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembersihan_spinner);

        // Initialize UI components
        namaKamar = findViewById(R.id.namaKamar);
        nama_karyawan = findViewById(R.id.nama_karyawan);
        tanggal = findViewById(R.id.tanggal);
        deskripsi = findViewById(R.id.deskripsi);
        saveBooking = findViewById(R.id.saveBooking);
        spinnerNamaKaryawan = findViewById(R.id.KaryawanSpinner);
        spinnerNamaKamar = findViewById(R.id.spinner_namaKamar);
        gambar_check_in=findViewById(R.id.check_in_dateCalender);


        // Set up the spinner with predefined items
        namaListRoom = new ArrayList<>();
        namaToIdMapRoom = new HashMap<>();
        fetchNamaDataRoom();

        // Set up listeners for date pickers
        setupDatePickerListeners();

        // Fetch data from the database for spinnerNama
        namaList = new ArrayList<>();
        namaToIdMap = new HashMap<>();
        fetchNamaData();

        // Spinner item selected listener for nama
        spinnerNamaKaryawan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != 0) {
                    String item = adapterView.getItemAtPosition(position).toString();
                    nama_karyawan.setText(item); // Set the selected item in the EditText
                    Toast.makeText(SimpanDataPembersihan.this, "Nama Karyawan: " + item, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(SimpanDataPembersihan.this, "Nama Kamar: " + item, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
            }
        });

        // Set listener for save button
        saveBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpanData();
            }
        });
    }

    // Listener untuk tanggal check-in dan check-out
    private void setupDatePickerListeners() {
        // Listener untuk check-in date
        gambar_check_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(tanggal);
            }
        });
    }

    // Method untuk menampilkan dialog date picker
    public void showDatePickerDialog(View view) {
        final EditText editText = (EditText) view;
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(SimpanDataPembersihan.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Month is 0-based, so add 1 to display correct month
                        String selectedDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        editText.setText(selectedDate);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }




    private void fetchNamaData() {
        String url = new ConfigurasiKaryawan().baseUrl() + "get_karyawan.php";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("karyawan");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id_karyawan = jsonObject.optString("id_karyawan");
                        String nama_karyawan = jsonObject.optString("nama_karyawan");
                        Log.d("FetchNamaData", "ID: " + id_karyawan + ", Nama: " + nama_karyawan);
                        namaList.add(nama_karyawan);
                        namaToIdMap.put(nama_karyawan, id_karyawan);
                    }
                    adaptor = new ArrayAdapter<>(SimpanDataPembersihan.this,
                            android.R.layout.simple_spinner_item, namaList);
                    adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerNamaKaryawan.setAdapter(adaptor);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("FetchNamaData", "JSON parsing error: " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("FetchNamaData", "Volley error: " + error.getMessage());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    private void fetchNamaDataRoom() {
        String url = new ConfigurasiRoom().baseUrl() + "get_room.php";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, null, new Response.Listener<JSONObject>() {
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
                    adaptorNamaKamar = new ArrayAdapter<>(SimpanDataPembersihan.this,
                            android.R.layout.simple_spinner_item, namaListRoom);
                    adaptorNamaKamar.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerNamaKamar.setAdapter(adaptorNamaKamar);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("FetchNamaDataRoom", "JSON parsing error: " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("FetchNamaDataRoom", "Volley error: " + error.getMessage());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    private void simpanData() {
        final String NamaKamar = namaKamar.getText().toString().trim();
        final String NamaKaryawan = nama_karyawan.getText().toString().trim();
        final String Tanggal = tanggal.getText().toString().trim();
        final String Deskripsi = deskripsi.getText().toString().trim();

        if ( NamaKamar.isEmpty() || NamaKaryawan.isEmpty() || Tanggal.isEmpty() || Deskripsi.isEmpty()) {
            Toast.makeText(SimpanDataPembersihan.this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }

        String idKaryawan = namaToIdMap.get(NamaKaryawan);
        if (idKaryawan == null) {
            Toast.makeText(SimpanDataPembersihan.this, "Nama pelanggan tidak ditemukan", Toast.LENGTH_SHORT).show();
            return;
        }

        String idRoom = namaToIdMapRoom.get(NamaKamar);
        if (idRoom == null) {
            Toast.makeText(SimpanDataPembersihan.this, "Nama kamar tidak ditemukan", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = new ConfigurasiPembersihan().baseUrl() + "create_pembersihan.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("SimpanData", "Response: " + response);
                        if (response.equalsIgnoreCase("duplicate")) {
                            Toast.makeText(SimpanDataPembersihan.this, "Data sudah ada!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SimpanDataPembersihan.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                            sendRefreshFlag(true);
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("SimpanData", "Error: " + error.getMessage());
                        Toast.makeText(SimpanDataPembersihan.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("namaKamar", NamaKamar);
                params.put("nama_karyawan", NamaKaryawan);
                params.put("tanggal", Tanggal);
                params.put("deskripsi", Deskripsi);
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
