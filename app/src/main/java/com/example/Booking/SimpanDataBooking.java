package com.example.Booking;

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

public class SimpanDataBooking extends AppCompatActivity {

    private EditText nama, namaKamar, check_in_date, check_out_date, total_price;
    private Button saveBooking;
    private Spinner spinnerNamaKamar, spinnerNama;
    private ArrayList<String> namaList, namaListRoom;
    private Map<String, String> namaToIdMap, namaToIdMapRoom;
    private ArrayAdapter<String> adaptor, adaptorNamaKamar;
    private ImageView gambar_check_in, gambar_check_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_spinner);

        // Initialize UI components
        nama = findViewById(R.id.nama);
        namaKamar = findViewById(R.id.namaKamar);
        check_in_date = findViewById(R.id.check_in_date);
        check_out_date = findViewById(R.id.check_out_date);
        total_price = findViewById(R.id.total_price);
        saveBooking = findViewById(R.id.saveBooking);
        spinnerNama = findViewById(R.id.customerSpinner);
        spinnerNamaKamar = findViewById(R.id.spinner_namaKamar);
        gambar_check_in=findViewById(R.id.check_in_dateCalender);
        gambar_check_out=findViewById(R.id.check_out_dateCalender);

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
        spinnerNama.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != 0) {
                    String item = adapterView.getItemAtPosition(position).toString();
                    nama.setText(item); // Set the selected item in the EditText
                    Toast.makeText(SimpanDataBooking.this, "Nama Booking: " + item, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(SimpanDataBooking.this, "Nama Kamar: " + item, Toast.LENGTH_SHORT).show();
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
                showDatePickerDialog(check_in_date);
            }
        });

        // Listener untuk check-out date
        gambar_check_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(check_out_date);
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

        DatePickerDialog datePickerDialog = new DatePickerDialog(SimpanDataBooking.this,
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
        String url = new Configurasi().baseUrl() + "get_customers.php";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("customer");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id = jsonObject.optString("id");
                        String nama = jsonObject.optString("nama");
                        Log.d("FetchNamaData", "ID: " + id + ", Nama: " + nama);
                        namaList.add(nama);
                        namaToIdMap.put(nama, id);
                    }
                    adaptor = new ArrayAdapter<>(SimpanDataBooking.this,
                            android.R.layout.simple_spinner_item, namaList);
                    adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerNama.setAdapter(adaptor);
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
                    adaptorNamaKamar = new ArrayAdapter<>(SimpanDataBooking.this,
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
        final String Nama = nama.getText().toString().trim();
        final String NamaKamar = namaKamar.getText().toString().trim();
        final String Check_in_date = check_in_date.getText().toString().trim();
        final String Check_out_date = check_out_date.getText().toString().trim();
        final String Total_price = total_price.getText().toString().trim();

        if (Nama.isEmpty() || NamaKamar.isEmpty() || Check_in_date.isEmpty() || Check_out_date.isEmpty() || Total_price.isEmpty()) {
            Toast.makeText(SimpanDataBooking.this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }

        String idCustomer = namaToIdMap.get(Nama);
        if (idCustomer == null) {
            Toast.makeText(SimpanDataBooking.this, "Nama pelanggan tidak ditemukan", Toast.LENGTH_SHORT).show();
            return;
        }

        String idRoom = namaToIdMapRoom.get(NamaKamar);
        if (idRoom == null) {
            Toast.makeText(SimpanDataBooking.this, "Nama kamar tidak ditemukan", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = new ConfigurasiBooking().baseUrl() + "create_booking.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("SimpanData", "Response: " + response);
                        if (response.equalsIgnoreCase("duplicate")) {
                            Toast.makeText(SimpanDataBooking.this, "Data sudah ada!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SimpanDataBooking.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                            sendRefreshFlag(true);
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("SimpanData", "Error: " + error.getMessage());
                        Toast.makeText(SimpanDataBooking.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nama", Nama);
                params.put("namaKamar", NamaKamar);
                params.put("check_in_date", Check_in_date);
                params.put("check_out_date", Check_out_date);
                params.put("total_price", Total_price);
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
