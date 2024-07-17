package com.example.Booking;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mobilehotelgroup2.R;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditBooking extends AppCompatActivity {

    private EditText editNama, editNamaKamar, editCheckInDate, editCheckOutDate, editTotalPrice;
    private Button updateButton;
    private String booking_id;

    private ImageView gambar_check_in, gambar_check_out;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_booking);

        editNama = findViewById(R.id.edit_nama);
        editNamaKamar = findViewById(R.id.edit_namaKamar);
        editCheckInDate = findViewById(R.id.edit_check_in_date);
        editCheckOutDate = findViewById(R.id.edit_check_out_date);
        editTotalPrice = findViewById(R.id.edit_total_price);
        updateButton = findViewById(R.id.saveBooking);
        gambar_check_in=findViewById(R.id.check_in_dateCalender);
        gambar_check_out=findViewById(R.id.check_out_dateCalender);

        // Mendapatkan data dari intent
        Intent intent = getIntent();
        if (intent != null) {
            booking_id = intent.getStringExtra("booking_id");
            editNama.setText(intent.getStringExtra("nama"));
            editNamaKamar.setText(intent.getStringExtra("namaKamar"));
            editCheckInDate.setText(intent.getStringExtra("check_in_date"));
            editCheckOutDate.setText(intent.getStringExtra("check_out_date"));
            editTotalPrice.setText(intent.getStringExtra("total_price"));
        }

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });
    }
    // Listener untuk tanggal check-in dan check-out
    private void setupDatePickerListeners() {
        // Listener untuk check-in date
        gambar_check_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(editCheckInDate);
            }
        });

        // Listener untuk check-out date
        gambar_check_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(editCheckOutDate);
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

        DatePickerDialog datePickerDialog = new DatePickerDialog(EditBooking.this,
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

    private void updateData() {
        final String nama = editNama.getText().toString().trim();
        final String namaKamar = editNamaKamar.getText().toString().trim();
        final String check_in_date = editCheckInDate.getText().toString().trim();
        final String check_out_date = editCheckOutDate.getText().toString().trim();
        final String total_price = editTotalPrice.getText().toString().trim();

        // Validasi input
        if (nama.isEmpty() || namaKamar.isEmpty() || check_in_date.isEmpty() || check_out_date.isEmpty() || total_price.isEmpty()) {
            Toast.makeText(EditBooking.this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }

        // URL untuk skrip PHP yang akan melakukan update data
        String url = new ConfigurasiBooking().baseUrl() + "update_booking.php";

        // Buat request baru
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Tangani respons dari server
                        Log.d("EditBooking", "Response: " + response);
                        if (response.equalsIgnoreCase("success")) {
                            Toast.makeText(EditBooking.this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show();
                            sendRefreshFlag(true); // Mengirim flag refresh untuk memuat ulang data
                            finish(); // Tutup aktivitas
                        } else {
                            Toast.makeText(EditBooking.this, "berhasil memperbarui data", Toast.LENGTH_SHORT).show();
                            sendRefreshFlag(true); // Mengirim flag refresh untuk memuat ulang data
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Tangani kesalahan
                        Log.e("EditBooking", "Error: " + error.getMessage());
                        Toast.makeText(EditBooking.this, "Gagal memperbarui data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Buat parameter untuk request
                Map<String, String> params = new HashMap<>();
                params.put("booking_id", booking_id);
                params.put("nama", nama);
                params.put("namaKamar", namaKamar);
                params.put("check_in_date", check_in_date);
                params.put("check_out_date", check_out_date);
                params.put("total_price", total_price); // Sesuaikan dengan nama kolom di server PHP
                Log.d("EditBooking", "Params: " + params.toString()); // Log parameter untuk debugging
                return params;
            }
        };

        // Tambahkan request ke RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void sendRefreshFlag(boolean refresh) {
        Intent intent = new Intent();
        intent.putExtra("refreshflag", refresh);
        setResult(RESULT_OK, intent);
    }
}
